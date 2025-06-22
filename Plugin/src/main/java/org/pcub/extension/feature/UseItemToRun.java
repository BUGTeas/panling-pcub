package org.pcub.extension.feature;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.pcub.extension.Common;
import org.pcub.extension.Common.State;
import org.pcub.extension.Main;
import org.pcub.extension.common.OperationLimiter;

public class UseItemToRun {
    private final Common common = Common.getInstance();
    private final Main main = common.main;

    // 键名定义
    private final NamespacedKey runCommandKey = new NamespacedKey(main, "run_command");
    private final NamespacedKey bedrockOnlyKey = new NamespacedKey(main, "bedrock_only");
    private final NamespacedKey blockUsageKey = new NamespacedKey(main, "block_usage");
    private final NamespacedKey placeholderKey = new NamespacedKey(main, "use_placeholder");

    private final OperationLimiter<Player> bedrockOffhandLimit = new OperationLimiter<>();
    private final OperationLimiter<Player> commandExecuteLimit = new OperationLimiter<>();


    public State checkCommandExecute(Player player, ItemMeta usedMeta, boolean isBedrock){
        if(usedMeta == null) return State.FAIL;
        PersistentDataContainer dataCont = usedMeta.getPersistentDataContainer();
        // 限定基岩版使用（默认启用）
        if (    !Boolean.FALSE.equals(dataCont.get(bedrockOnlyKey, PersistentDataType.BOOLEAN)) &&
                !isBedrock) return State.FAIL;
        // 检查命令
        String runCommand = dataCont.get(runCommandKey, PersistentDataType.STRING);
        if (runCommand == null) {
            // 阻止原物品功能（默认禁用）
            if (Boolean.TRUE.equals(dataCont.get(blockUsageKey, PersistentDataType.BOOLEAN))) {
                return State.LIMIT;
            }
            return State.FAIL;
        }
        // 执行命令（0.5 秒间隔，禁止连续）
        if (commandExecuteLimit.put(player, 10L) < 2) {
            // 过滤斜杠开头
            if (runCommand.startsWith("/")) runCommand = runCommand.substring(1);
            // 启用 PlaceholderAPI
            if (    Boolean.TRUE.equals(dataCont.get(placeholderKey, PersistentDataType.BOOLEAN)) &&
                    main.havePAPI) {
                runCommand = PlaceholderAPI.setPlaceholders(player, runCommand);
            }
            player.performCommand(runCommand);
        }
        // 阻止原物品功能（默认启用）
        if (Boolean.FALSE.equals(dataCont.get(blockUsageKey, PersistentDataType.BOOLEAN))) {
            return State.SUCCESS;
        }
        return State.SUCCESS_AND_LIMIT;
    }



    public boolean bedrockOffhand(Player player, Material usedType) {
        // 副手功能
        if (    player.getInventory().getItemInOffHand().getType() == Material.CARROT_ON_A_STICK &&
                usedType != Material.CARROT_ON_A_STICK &&
                usedType != Material.WARPED_FUNGUS_ON_A_STICK &&
                usedType != Material.BOW &&
                usedType != Material.CROSSBOW &&
                !Stacker.isForceStack(usedType) &&
                bedrockOffhandLimit.get(player) < 1) {
            // 发出执行请求
            common.scoreboardTool.useCarrotOnStick(player.getName(), 1);
            // 0.15秒CD
            bedrockOffhandLimit.put(player, 3L);
            return true;
        }
        return false;
    }
}

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
    // 键名定义
    private final NamespacedKey runCommandKey;
    private final NamespacedKey bedrockOnlyKey;
    private final NamespacedKey blockUsageKey;
    private final NamespacedKey placeholderKey;
    private final Main main;

    private final OperationLimiter bedrockOffhandLimit;
    private final OperationLimiter commandExecuteLimit;



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
            player.addScoreboardTag("pcub_player_interact");
            // 0.1秒CD
            bedrockOffhandLimit.put(player, 2L);
            return true;
        }
        return false;
    }



    // 构造
    public UseItemToRun(Common common) {
        Main main = common.main;
        this.main = main;
        this.bedrockOffhandLimit = new OperationLimiter(main);
        this.commandExecuteLimit = new OperationLimiter(main);
        this.runCommandKey = new NamespacedKey(main, "run_command");
        this.bedrockOnlyKey = new NamespacedKey(main, "bedrock_only");
        this.blockUsageKey = new NamespacedKey(main, "block_usage");
        this.placeholderKey = new NamespacedKey(main, "use_placeholder");
    }
}

package org.pcub.extension.feature;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.pcub.extension.Common;
import org.pcub.extension.Common.State;
import org.pcub.extension.Main;

import java.util.UUID;

public class UseItemToRun {
    // 键名定义
    private final NamespacedKey runCommandKey;
    private final NamespacedKey bedrockOnlyKey;
    private final NamespacedKey blockUsageKey;
    private final NamespacedKey placeholderKey;
    private final Common common;
    private final Main main;



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
        // 执行命令
        UUID playerUUID = player.getUniqueId();
        boolean runLimit = common.getOperationLimit("mb" + playerUUID, 1);
        common.setOperationLimit("mb" + playerUUID, 10L);
        if (!runLimit) {
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



    public void bedrockOffhand(Player targetPlayer, String targetName, Material usedType) {
        // 副手功能
        if (
            targetPlayer.getInventory().getItemInOffHand().getType() == Material.CARROT_ON_A_STICK &&
            usedType != Material.CARROT_ON_A_STICK &&
            usedType != Material.WARPED_FUNGUS_ON_A_STICK &&
            usedType != Material.BOW &&
            usedType != Material.CROSSBOW &&
            !Stacker.isForceStack(usedType) &&
            common.getScore("pcub_player_interact", targetName) == 0
        ) {
            // 发出执行请求
            common.setScore("pcub_player_interact", targetName, 1);
            // 0.1秒CD
            new BukkitRunnable() {
                @Override
                public void run() {
                    common.setScore("pcub_player_interact", targetName, 0);
                }
            }.runTaskLaterAsynchronously(main, 2L);
            if (common.debug) common.debugLogger(targetName + " 通过计分板向数据包请求");
        } else if (common.debug) common.debugLogger(targetName + " 主副手物品不满足条件/请求频率过高");
    }



    // 构造
    public UseItemToRun(Common common) {
        this.common = common;
        Main main = common.main;
        this.main = main;
        this.runCommandKey = new NamespacedKey(main, "run_command");
        this.bedrockOnlyKey = new NamespacedKey(main, "bedrock_only");
        this.blockUsageKey = new NamespacedKey(main, "block_usage");
        this.placeholderKey = new NamespacedKey(main, "use_placeholder");
    }
}

package org.pcub.extension.feature;

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

public class UseItemToRun {
    // 键名定义
    private final NamespacedKey runCommandKey;
    private final NamespacedKey bedrockOnlyKey;
    private final NamespacedKey blockUsageKey;
    private final Common common;
    private final Main main;



    public State checkCommandExecute(Player player, ItemMeta usedMeta, boolean isBedrock){
        String targetID = player.getUniqueId().toString();
        String targetName = player.getName();
        if(usedMeta == null) return State.FAIL;
        PersistentDataContainer dataCont = usedMeta.getPersistentDataContainer();
        String runCommand = dataCont.get(runCommandKey, PersistentDataType.STRING);
        // 未设置执行命令，或限定基岩版使用（默认）
        if (    runCommand == null ||
                !Boolean.FALSE.equals(dataCont.get(bedrockOnlyKey, PersistentDataType.BOOLEAN)) &&
                        !isBedrock) {
            return State.FAIL;
        }
        // 执行命令
        boolean limit = common.getOperationLimit("mb" + targetID, 1);
        common.setOperationLimit("mb" + targetID, 10L);
        if (!limit) {
            player.performCommand(runCommand);
        }
        // 阻止原物品功能（默认）
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
    }
}

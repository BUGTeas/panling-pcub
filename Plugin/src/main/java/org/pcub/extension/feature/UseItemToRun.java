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
    private final NamespacedKey shortcutKey;
    private final NamespacedKey menuKey;
    private final Common common;
    private final Main main;



    public State bedrockMenu(Player player, ItemMeta usedMeta){
        String targetID = player.getUniqueId().toString();
        String targetName = player.getName();
        if(usedMeta == null) return State.FAIL;
        PersistentDataContainer dataCont = usedMeta.getPersistentDataContainer();
        String shortcut = dataCont.get(shortcutKey, PersistentDataType.STRING);
        if (Boolean.TRUE.equals(dataCont.get(menuKey, PersistentDataType.BOOLEAN))) {
            // 打开菜单书
            common.setScore("pcub_open_bedrock_menu", targetName, 1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!common.getOperationLimit("mb" + targetID, 1)) {
                        // 隐藏物品》隐藏对话》菜单
                        // 获取记分项
                        boolean hideItem = common.getScore("pcub_hide_item_enable", targetName) == 1;
                        boolean hideTalk = common.getScore("pcub_hide_talk_enable", targetName) == 1;
                        if (hideItem && hideTalk) player.performCommand("forms open hide-task-be");
                        else if (hideItem) player.performCommand("forms open hide-item-be");
                        else if (hideTalk) player.performCommand("forms open hide-talk-be");
                        else player.performCommand("forms open menubook-be");
                    }
                    common.setOperationLimit("mb" + targetID, 10L);
                }
            }.runTaskLater(main, 2L);
            return State.SUCCESS;
        }
        if (shortcut != null) {
            // 命令捷径
            if (!common.getOperationLimit("mb" + targetID, 1)) player.performCommand(shortcut);
            common.setOperationLimit("mb" + targetID, 10L);
            return State.SUCCESS;
        }
        return State.FAIL;
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
        this.shortcutKey = new NamespacedKey(main, "run_command");
        this.menuKey = new NamespacedKey(main, "menubook");
    }
}

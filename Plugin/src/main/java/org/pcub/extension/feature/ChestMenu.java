package org.pcub.extension.feature;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.pcub.extension.Common;
import org.pcub.extension.Common.State;
import org.pcub.extension.feature.Stacker.StackerCommon;

public class ChestMenu {
    public static void readyOpen(String targetName, String targetID, Common common) {
        common.setTempScore("inventory_opened", targetID, 2);
        new BukkitRunnable() {
            @Override
            public void run(){
                common.setTempScore("inventory_opened", targetID, 0);
            }
        }.runTaskLaterAsynchronously(common.main,0L);
        if (common.debug) common.debugLogger(targetName + " 准备打开末影箱");
    }



    public static void open(String targetName, String targetID, Common common) {
        if (common.getTempScore("inventory_opened", targetID) == 2) {
            common.consoleExec("execute as " + targetID + " run function pld:system/chest_menu/open");
            if (common.debug) common.debugLogger(targetName + " 已打开末影箱");
        }
    }



    public static void close(Player targetPlayer, Common common) {
        String targetName = targetPlayer.getName();
        if (common.getScore("screen", targetName) >= 0) {
            common.consoleExec("execute as " + targetName + " run function #pcub:chest_menu_leave");
        }
    }



    // 检查是否为钱庄箱子按钮
    public static boolean isButton(StackerCommon stc) {
        // 检查是否有按钮标签
        if (stc.meta == null) return false;
        return stc.meta.getAsString().contains("clickable:1");
    }



    public static State checkWork(StackerCommon cursor, StackerCommon current, String targetUUIDStr, Common common) {
        boolean buttonOnCursor = ChestMenu.isButton(cursor);
        boolean buttonOnCurrent = ChestMenu.isButton(current);
        // 过滤操作
        boolean ep = common.getOperationLimit("ep" + targetUUIDStr, 5);
        if(buttonOnCurrent) common.setOperationLimit("ep" + targetUUIDStr, 10L);
        // 玩家频繁操作
        if (buttonOnCurrent && ep) {
            if (common.debug) common.debugLogger("需要取消 - 玩家频繁操作");
            return State.LIMIT;
        }
        // 箱子整理模组、GeyserMC 导致同一刻内的多次点击
        if (common.getTempScore("clicked", targetUUIDStr) == 1) {
            if (common.debug) common.debugLogger("需要取消 - 同一刻内多次点击");
            return State.LIMIT;
        }
        // 将其他物品与按钮互换
        if (cursor.type != null && cursor.type != Material.AIR && !buttonOnCursor && buttonOnCurrent) {
            if (common.debug) common.debugLogger("需要取消 - 将其他物品与按钮互换");
            return State.LIMIT;
        }
        if (!buttonOnCurrent) return State.FAIL;
        // 否则按下按钮开启限制，在下一刻执行按键函数并解除限制
        common.setTempScore("clicked", targetUUIDStr, 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                common.consoleExec("execute as " + targetUUIDStr + " run function #pcub:chest_menu_click");
                common.setTempScore("clicked", targetUUIDStr, 0);
            }
        }.runTaskLater(common.main, 0L);
        return State.SUCCESS;
    }
}

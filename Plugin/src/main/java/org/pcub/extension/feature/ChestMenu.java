package org.pcub.extension.feature;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.pcub.extension.Common;
import org.pcub.extension.Common.State;
import org.pcub.extension.common.OperationLimiter;
import org.pcub.extension.feature.Stacker.StackerCommon;

public class ChestMenu {
    private final Common common;
    private final OperationLimiter clickLimit;



    public void readyOpen(String playerName, String playerUUIDStr) {
        common.setTempScore("inventory_opened", playerUUIDStr, 2);
        new BukkitRunnable() {
            @Override
            public void run(){
                common.setTempScore("inventory_opened", playerUUIDStr, 0);
            }
        }.runTaskLaterAsynchronously(common.main,0L);
        if (common.debug) common.debugLogger(playerName + " 准备打开末影箱");
    }



    public void open(String playerName, String playerUUIDStr) {
        if (common.getTempScore("inventory_opened", playerUUIDStr) == 2) {
            common.consoleExec("execute as " + playerUUIDStr + " run function #pcub:chest_menu/open");
            if (common.debug) common.debugLogger(playerName + " 已打开末影箱");
        }
    }



    public void close(String playerName, String playerUUIDStr) {
        if (common.getScore("screen", playerName) >= 0) {
            common.consoleExec("execute as " + playerUUIDStr + " run function #pcub:chest_menu/leave");
        }
    }



    // 检查是否为钱庄箱子按钮
    public static boolean isButton(StackerCommon stc) {
        // 检查是否有按钮标签
        if (stc.meta == null) return false;
        return stc.meta.getAsString().contains("clickable:1");
    }



    public State checkWork(StackerCommon cursor, StackerCommon current, Player player) {
        boolean buttonOnCursor = ChestMenu.isButton(cursor);
        boolean buttonOnCurrent = ChestMenu.isButton(current);
        // 过滤操作
        boolean ep = clickLimit.get(player) >= 5;
        if(buttonOnCurrent) clickLimit.put(player, 10L);
        // 玩家频繁操作
        if (buttonOnCurrent && ep) {
            if (common.debug) common.debugLogger("需要取消 - 玩家频繁操作");
            return State.LIMIT;
        }
        String playerUUIDStr = player.getUniqueId().toString();
        // 箱子整理模组、GeyserMC 导致同一刻内的多次点击
        if (common.getTempScore("clicked", playerUUIDStr) == 1) {
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
        common.setTempScore("clicked", playerUUIDStr, 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                common.consoleExec("execute as " + playerUUIDStr + " run function #pcub:chest_menu/click");
                common.setTempScore("clicked", playerUUIDStr, 0);
            }
        }.runTaskLater(common.main, 0L);
        return State.SUCCESS;
    }

    public ChestMenu(Common common) {
        this.common = common;
        this.clickLimit = new OperationLimiter(common.main);
    }
}

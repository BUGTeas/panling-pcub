package org.pcub.extension.common;


import org.pcub.extension.Common;

import java.util.HashSet;
import java.util.HashMap;
import java.util.UUID;

public class CheckPlayerCrosshair {
    private final Common common = Common.getInstance();

    private final HashSet<UUID> playersWithoutCrosshair = new HashSet<>();
    private final HashMap<UUID, Integer> readySet = new HashMap<>();
    private final OperationLimiter<UUID> operationLimiter = new OperationLimiter<>();

    // 获取玩家当前是否为准星模式
    public boolean not(UUID player) {
        return playersWithoutCrosshair.contains(player);
    }

    // 设为准星模式
    public void setCrosshairWhenReach(UUID player, int targetCount) {
        // 已经是准星模式
        if (!not(player)) {
            // 打断未完成的取消累计
            readySet.remove(player);
            return;
        }
        // 限制连续频繁触发
        if (operationLimiter.put(player, 5L) > 1) return;
        // 次数累计
        Integer currentCount = readySet.get(player);
        int result = (currentCount == null ? targetCount : currentCount) - 1;
        // 满足累计条件则切换
        if (result <= 0) {
            readySet.remove(player);
            playersWithoutCrosshair.remove(player);
            if (common.debug) common.debugLogger("切换至准星模式");
        } else {
            readySet.put(player, result);
            if (common.debug) common.debugLogger("再按 " + result + " 次将切换至准星模式");
        }
    }

    // 取消准星模式
    public void cancelCrosshairWhenReach(UUID player, int targetCount) {
        // 已经取消准星模式
        if (not(player)) {
            // 打断未完成的设定累计
            readySet.remove(player);
            return;
        }
        // 限制连续频繁触发
        if (operationLimiter.put(player, 5L) > 1) return;
        // 次数累计
        Integer currentCount = readySet.get(player);
        int result = (currentCount == null ? targetCount : currentCount) - 1;
        // 满足累计条件则切换
        if (result <= 0) {
            readySet.remove(player);
            playersWithoutCrosshair.add(player);
            if (common.debug) common.debugLogger("切换至圆环模式");
        } else {
            readySet.put(player, result);
            if (common.debug) common.debugLogger("再按 " + result + " 次将切换至圆环模式");
        }
    }
}

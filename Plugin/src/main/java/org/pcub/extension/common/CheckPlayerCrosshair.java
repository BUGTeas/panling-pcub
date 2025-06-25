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

    // 获取判断结果，玩家是否为圆环模式
    public boolean not(UUID player) {
        return playersWithoutCrosshair.contains(player);
    }

    // 判断为准星模式
    public void determineTrue(UUID player) {
        resetCondition(player);
        playersWithoutCrosshair.remove(player);
        if (common.debug) common.debugLogger("判断为准星模式");
    }

    // 判断为圆环模式（直接点击，非准星）
    public void determineFalse(UUID player) {
        resetCondition(player);
        playersWithoutCrosshair.add(player);
        if (common.debug) common.debugLogger("判断为圆环模式");
    }

    // 累计达到次数，则判断为准星模式
    public void determineTrueWhenReach(UUID player, int targetCount) {
        // 已判断为准星
        if (!not(player)) {
            // 打断未达到的次数条件
            resetCondition(player);
            return;
        }
        // 限制连续频繁触发
        if (operationLimiter.put(player, 5L) > 1) return;
        // 次数累计
        Integer currentCount = readySet.get(player);
        int result = (currentCount == null ? targetCount : currentCount) - 1;
        // 是否达到次数
        if (result <= 0) {
            determineTrue(player);
        } else {
            readySet.put(player, result);
            if (common.debug) common.debugLogger("再按 " + result + " 次判断为准星");
        }
    }

    // 累计达到次数，则判断为圆环模式
    public void determineFalseWhenReach(UUID player, int targetCounts) {
        // 已判断为圆环
        if (not(player)) {
            // 打断未达到的次数条件
            resetCondition(player);
            return;
        }
        // 限制连续频繁触发
        if (operationLimiter.put(player, 5L) > 1) return;
        // 次数累计
        Integer currentCounts = readySet.get(player);
        int result = (currentCounts == null ? targetCounts : currentCounts) - 1;
        // 是否达到次数
        if (result <= 0) {
            determineFalse(player);
        } else {
            readySet.put(player, result);
            if (common.debug) common.debugLogger("再按 " + result + " 次判断为圆环");
        }
    }

    // 重置次数条件
    public void resetCondition(UUID player) {
        readySet.remove(player);
    }
}

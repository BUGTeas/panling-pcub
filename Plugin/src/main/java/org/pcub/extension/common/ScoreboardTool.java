package org.pcub.extension.common;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.pcub.extension.Common;
import org.pcub.extension.Main;

import java.util.Set;

public class ScoreboardTool {
    private final Main main;
    private final Common common;
    private final Scoreboard scoreboard;

    private Set<Objective> useCarrotOnStickObj;
    private boolean useCarrotOnStickExpired;
    private Set<Objective> sneakTimeObj;
    private boolean sneakTimeExpired;



    // 记分项过期
    private void setUseCarrotOnStickExpire() {
        useCarrotOnStickExpired = true;
        if (common.debug) common.debugLogger("使用胡萝卜钓竿记分项列表 已过期");
    }
    private void setSneakTimeExpire() {
        sneakTimeExpired = true;
        if (common.debug) common.debugLogger("潜行时长记分项列表 已过期");
    }



    // 刷新/初始化记分项
    public void loadUseCarrotOnStick() {
        useCarrotOnStickObj = scoreboard.getObjectivesByCriteria(Criteria.statistic(Statistic.USE_ITEM, Material.CARROT_ON_A_STICK));
        // 1 分钟后过期
        useCarrotOnStickExpired = false;
        new BukkitRunnable() {
            @Override
            public void run() {
                setUseCarrotOnStickExpire();
            }
        }.runTaskLaterAsynchronously(main, 1200L);
    }

    public void loadSneakTime() {
        sneakTimeObj = scoreboard.getObjectivesByCriteria(Criteria.statistic(Statistic.SNEAK_TIME));
        // 1 分钟后过期
        sneakTimeExpired = false;
        new BukkitRunnable() {
            @Override
            public void run() {
                setSneakTimeExpire();
            }
        }.runTaskLaterAsynchronously(main, 1200L);
    }



    // 触发
    public void useCarrotOnStick(String targetName, int count){
        // 同步初始化记分项列表
        if (useCarrotOnStickObj == null) {
            loadUseCarrotOnStick();
            if (common.debug) common.debugLogger("已初始化 使用胡萝卜钓竿记分项列表");
        }
        // 如果已经过期，异步刷新记分项列表
        if (useCarrotOnStickExpired) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    loadUseCarrotOnStick();
                    if (common.debug) common.debugLogger("已刷新 使用胡萝卜钓竿记分项列表");
                }
            }.runTaskAsynchronously(main);
        }
        // 遍历加分
        for (Objective objective : useCarrotOnStickObj) {
            Score score = objective.getScore(targetName);
            score.setScore(score.getScore() + count);
        }
        if (common.debug) common.debugLogger(targetName + " 的所有使用胡萝卜钓竿记分项加 " + count + " 分");
    }

    public void addSneakWhen0(String targetName, int count){
        // 同步初始化记分项列表
        if (sneakTimeObj == null) {
            loadSneakTime();
            if (common.debug) common.debugLogger("已初始化 潜行时长记分项列表");
        }
        // 如果已经过期，异步刷新记分项列表
        if (sneakTimeExpired) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    loadSneakTime();
                    if (common.debug) common.debugLogger("已刷新 潜行时长记分项列表");
                }
            }.runTaskAsynchronously(main);
        }
        // 遍历加分
        for (Objective objective : sneakTimeObj) {
            Score score = objective.getScore(targetName);
            if (score.getScore() == 0) score.setScore(count);
        }
        if (common.debug) common.debugLogger(targetName + " 的所有潜行时长记分项加 " + count + " 分");
    }



    public ScoreboardTool(Common common, Scoreboard scoreboard) {
        this.common = common;
        this.main = common.main;
        this.scoreboard = scoreboard;
    }
}

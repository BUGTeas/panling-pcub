package org.pcub.extension;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.geyser.api.GeyserApi;
import org.pcub.extension.common.ScoreboardTool;

public class Common {
    // 获取主类
    public final Main main;

    // 获取 API
    public final GeyserApi geyserApi;
    public final boolean geyserValid;
    public final FloodgateApi floodgateApi;
    public final boolean floodgateValid;

    // 通用返回标识
    public enum State {
        FAIL(false, false),
        SUCCESS(true, false),
        SUCCESS_AND_LIMIT(true, true),
        LIMIT(false, true);

        public final boolean success;
        public final boolean limit;

        State(boolean success, boolean limit) {
            this.success = success;
            this.limit = limit;
        }
    }

    // 控制台执行器
    public void consoleExec(String command){
        main.server.dispatchCommand(main.console, command);
    }

    // 调试日志
    public boolean debug = false; // 输出开关
    public void debugLogger(String info) {
        int worldTime = (int) Bukkit.getWorlds().get(0).getFullTime() % 1000;
        main.console.sendMessage("PCUB调试" + new char[]{'/','\\'}[worldTime % 2] + worldTime + " " + info);
    }



    // 获取原版记分板
    private final Scoreboard mainScoreboard;

    // 获取原版记分板分数
    public int getScore(String objectiveName, String target) {
        Objective objective = mainScoreboard.getObjective(objectiveName);
        if (objective == null) {
            main.getLogger().warning("获取分数失败，找不到记分项：“" + objectiveName + "”。（已返回0）");
            return 0;
        }
        return objective.getScore(target).getScore();
    }

    // 设置原版记分板分数
    public void setScore(String objectiveName, String target, int score) {
        Objective objective = mainScoreboard.getObjective(objectiveName);
        if (objective != null) objective.getScore(target).setScore(score);
        else main.getLogger().warning("设置分数失败，找不到记分项：“" + objectiveName + "”。");
    }

    // 创建临时记分板
    private final Scoreboard tempScoreboard;

    // 获取临时记分板分数
    public int getTempScore(String objectiveName, String target) {
        Objective objective = tempScoreboard.getObjective(objectiveName);
        if (objective != null) return objective.getScore(target).getScore();
        else return 0;
    }

    // 设置临时记分板分数
    public void setTempScore(String objectiveName, String target, int score) {
        Objective objective = tempScoreboard.getObjective(objectiveName);
        if (objective == null) objective = tempScoreboard.registerNewObjective(objectiveName, Criteria.DUMMY, "");
        objective.getScore(target).setScore(score);
    }



    // 潜行/使用胡萝卜钓竿记分板管理
    public final ScoreboardTool scoreboardTool;



    // (实验性) 弃用强制堆叠 & 交易伪叠放功能
    public boolean legacyStack = false;



    // 构造
    public Common(Main main) {
        this.main = main;
        ScoreboardManager scoreboardManager = main.server.getScoreboardManager();
        this.mainScoreboard = scoreboardManager.getMainScoreboard();
        this.tempScoreboard = scoreboardManager.getNewScoreboard();

        this.scoreboardTool = new ScoreboardTool(this, this.mainScoreboard);

        this.geyserApi = (main.haveGeyser) ? GeyserApi.api() : null;
        this.geyserValid = this.geyserApi != null;
        if (this.geyserValid) main.logger.info("已加载 Geyser API");

        this.floodgateApi = (main.haveFloodgate) ? FloodgateApi.getInstance() : null;
        this.floodgateValid = this.floodgateApi != null;
        if (this.floodgateValid) main.logger.info("已加载 Floodgate API");
    }
}

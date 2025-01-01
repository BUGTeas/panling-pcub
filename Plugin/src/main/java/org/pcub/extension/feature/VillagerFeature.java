package org.pcub.extension.feature;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.pcub.extension.Common;
import org.pcub.extension.Common.State;

public class VillagerFeature {
    // 村民被玩家点击后，在打开交易界面前执行
    public static State beforeOpen(Player targetPlayer, Entity targetEntity, Common common) {
        // 给玩家赋予 Tag 以便数据包函数检测到触发者
        targetPlayer.addScoreboardTag("interact_villager");
        // 以村民身份执行函数
        common.consoleExec("execute as " + targetEntity.getUniqueId() + " run function pcub:interact_villager");
        // 删除玩家 Tag
        targetPlayer.removeScoreboardTag("interact_villager");
        // 如果玩家被赋予 Tag “ignoreTradeUI” 则将其删除并屏蔽交易界面
        for (String tag : targetPlayer.getScoreboardTags()) if (tag.equals("ignoreTradeUI")) {
            targetPlayer.removeScoreboardTag("ignoreTradeUI");
            return State.LIMIT;
        }
        return State.SUCCESS;
    }
}

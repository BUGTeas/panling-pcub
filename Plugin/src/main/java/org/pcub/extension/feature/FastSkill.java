package org.pcub.extension.feature;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.pcub.extension.Common;
import org.pcub.extension.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FastSkill {
    private final Common common;
    private final Main main;
    private final Map<Player, BukkitRunnable> sneakSkill = new HashMap<>();



    // 检测
    public void check(Player targetPlayer, Boolean isSneaking){
        String targetName = targetPlayer.getName();
        UUID targetIDN = targetPlayer.getUniqueId();
        String targetID = targetIDN.toString();
        boolean isGeyser = common.geyserValid && common.geyserApi.isBedrockPlayer(targetIDN);
        boolean isFloodgate = common.floodgateValid && common.floodgateApi.isFloodgatePlayer(targetIDN);
        int enableFastskill = common.getScore("pcub_enable_fastskill", targetName);
        if (
            common.getScore("job", targetName) == 0 && (
                enableFastskill == 1 ||
                enableFastskill == 2 && (isFloodgate || isGeyser) ||
                enableFastskill == 3 && common.getTempScore("is_touch", targetID) == 1
            )
        ) {
            if (isSneaking) setSneak(targetPlayer, targetName, common.getScore("pcub_fastskill_duration", targetName));
            else cancelSneak(targetPlayer);
        }
    }



    // 设置潜行
    public void setSneak(Player player, String targetName, long duration) {
        ItemStack currentItem = player.getInventory().getItemInMainHand();
        ItemMeta currentMeta = currentItem.getItemMeta();
        if (currentItem.getType() == Material.CARROT_ON_A_STICK && currentMeta != null) {
            cancelSneak(player); // 异步可能导致 setSneak 在取消潜行前触发，在这里同步取消一遍，以免上一任务被覆盖无法取消导致反复潜行切换可能误判
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    ItemMeta afterMeta = player.getInventory().getItemInMainHand().getItemMeta();
                    if (afterMeta != null && afterMeta.getAsString().equals(currentMeta.getAsString())) {
                        common.scoreboardTool.useCarrotOnStick(targetName, 1);
                        common.scoreboardTool.addSneakWhen0(targetName, 1);
                    }
                }
            };
            sneakSkill.put(player, runnable);
            runnable.runTaskLaterAsynchronously(main, duration);
        }
    }



    // 取消潜行
    public void cancelSneak(Player player){
        BukkitRunnable runnable = sneakSkill.remove(player);
        if (runnable != null) runnable.cancel();
    }



    public FastSkill(Common common) {
        this.common = common;
        this.main = common.main;
    }
}

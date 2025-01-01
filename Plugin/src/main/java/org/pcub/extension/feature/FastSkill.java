package org.pcub.extension.feature;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.geyser.api.GeyserApi;
import org.pcub.extension.Common;
import org.pcub.extension.Main;

import java.util.ArrayList;
import java.util.UUID;

public class FastSkill {
    private final Common common;
    private final Main main;
    private final ArrayList<BukkitRunnable> sneakSkill = new ArrayList<>();
    private final ArrayList<Player> sneakSkillPlayer = new ArrayList<>();



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
            sneakSkillPlayer.add(player);
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    ItemMeta afterMeta = player.getInventory().getItemInMainHand().getItemMeta();
                    if (afterMeta != null && afterMeta.getAsString().equals(currentMeta.getAsString())) {
                        common.setScore("right_click_check", targetName, 1);
                        if (common.getScore("sneak_check", targetName) < 1) common.setScore("sneak_check", targetName, 1);
                    }
                }
            };
            sneakSkill.add(runnable);
            runnable.runTaskLaterAsynchronously(main, duration);
        }
    }



    // 取消潜行
    public void cancelSneak(Player target){
        int targetIndex = -1;
        for (int i = 0; i < sneakSkillPlayer.size(); i ++) if (sneakSkillPlayer.get(i) == target) {
            targetIndex = i;
            break;
        }
        if(targetIndex != -1) {
            sneakSkillPlayer.remove(targetIndex);
            sneakSkill.get(targetIndex).cancel();
            sneakSkill.remove(targetIndex);
        }
    }



    public FastSkill(Common common) {
        this.common = common;
        this.main = common.main;
    }
}

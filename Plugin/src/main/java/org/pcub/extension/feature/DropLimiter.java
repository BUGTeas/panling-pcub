package org.pcub.extension.feature;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.pcub.extension.Common;
import org.pcub.extension.Main;
import org.pcub.extension.Common.State;

public class DropLimiter {
    private final Common common;
    private final Main main;
    // 连续投掷变量
    private int dropLastAmount = -1;



    public State check(String targetName, String targetID, ItemStack usedItem, boolean isBedrock, boolean isTouch) {
        State limited = State.SUCCESS;
        boolean needCancel = common.getOperationLimit("dp" + targetID, 1);
        int dropAmount = usedItem.getAmount();
        // 当在限制范围，且相比同一刻上一次事件使用物品的叠放量不同（投出了）则取消
        // 这可能会使投掷限制在高延迟的创造模式下失效
        if(needCancel && dropLastAmount != dropAmount) {
            limited = State.LIMIT;
            if (common.debug) common.debugLogger(targetName + " 取消 - 投掷限制");
        }
        // 更新当前使用物品叠放量
        // 避免基岩版投出时同一刻触发多次事件，导致真正投出的触发被取消
        dropLastAmount = dropAmount;
        new BukkitRunnable() {
            @Override
            public void run() {
                dropLastAmount = -1;
            }
        }.runTaskLaterAsynchronously(main, 0L);
        // 连续投掷
        // 如果每次触发都会设置限制，而不等待上一限制结束，就能达到禁用连续投掷的效果
        int enableContinuous = common.getScore("pcub_enable_continuous", targetName),dropSpeed = 7;
        if (
            // 始终禁用
            enableContinuous == 0 ||
            // 仅基岩版禁用
            enableContinuous == 2 && isBedrock ||
            // 仅移动端禁用
            enableContinuous == 3 && isTouch
        ) needCancel = false;
        // 投掷速度单位为刻数/次
        else dropSpeed = common.getScore("pcub_drop_interval", targetName);
        // 如果当前未限制，且投掷间隔大于 0 刻（MC原版投掷间隔约4刻），则设置限制
        if (!needCancel && dropSpeed > 0) common.setOperationLimit("dp" + targetID, dropSpeed);
        return limited;
    }



    public DropLimiter(Common common){
        this.common = common;
        this.main = common.main;
    }
}

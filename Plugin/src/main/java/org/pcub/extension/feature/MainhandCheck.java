package org.pcub.extension.feature;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;

public class MainhandCheck {
    public Entity dropInInventory = null;

    public void work(Entity entity) {
        // 添加 Tag 供数据包检测
        entity.addScoreboardTag("pcub_mainhand_check");
    }

    public void checkInventoryClick(HumanEntity humanEntity, int clickedSlot, InventoryAction inventoryAction) {
        if (clickedSlot == humanEntity.getInventory().getHeldItemSlot()) {
            work(humanEntity);
        }
        if (inventoryAction.toString().startsWith("DROP_")) {
            dropInInventory = humanEntity;
        }
    }

    public void checkDropItem(Entity entity) {
        if (entity != dropInInventory) {
            work(entity);
        } else {
            dropInInventory = null;
        }
    }

    public void checkPickupItem(Entity entity) {
        if (entity instanceof Player) {
            work(entity);
        }
    }
}

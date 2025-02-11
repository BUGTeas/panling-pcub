package org.pcub.extension;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.floodgate.util.DeviceOs;
import org.pcub.extension.Common.State;
import org.pcub.extension.feature.*;

import java.util.Arrays;
import java.util.UUID;

public class EventListener implements Listener {
    private final Common common;
    private final Main main;
    private final UseItemToRun useItemToRun;
    private final FastSkill fastSkill;
    private final DropLimiter dropLimiter;
    private final Stacker stacker;
    private final MainhandCheck mainhandCheck;


    // 玩家进服事件
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerDisplay = player.getDisplayName();
        UUID playerUUID = player.getUniqueId();
        String playerUUIDStr = playerUUID.toString();
        boolean isGeyser = common.geyserValid && common.geyserApi.isBedrockPlayer(playerUUID);
        boolean isFloodgate = common.floodgateValid && common.floodgateApi.isFloodgatePlayer(playerUUID);
        // 当 Floodgate 登录出现异常时踢出玩家
        if (common.floodgateValid && !isFloodgate && isGeyser) {
            player.kickPlayer("登录系统出现异常， 请重新加入服务器。");
            main.logger.info("\n" + playerDisplay + " 因为 Floodgate 登录异常无法正常加入游戏");
            common.setTempScore("login_status", playerUUIDStr, 0);
        } else {
            common.setTempScore("is_touch", playerUUIDStr, (
                isFloodgate && Arrays.asList(
                    DeviceOs.GOOGLE,
                    DeviceOs.IOS,
                    DeviceOs.WINDOWS_PHONE,
                    DeviceOs.AMAZON
                ).contains(common.floodgateApi.getPlayer(playerUUID).getDeviceOs()) ||
                !common.floodgateValid && isGeyser
            ) ? 1 : 0);
            common.setTempScore("login_status", playerUUIDStr, (isFloodgate || isGeyser) ? 2 : 1);
            common.setTempScore("operating_limit_count", playerUUIDStr, 0);
            common.setTempScore("clicked", playerUUIDStr, 0);
            common.setTempScore("inventory_opened", playerUUIDStr, 0);
            common.setTempScore("pot_clicked", playerUUIDStr, 0);
            fastSkill.cancelSneak(player);
            String edition = (isFloodgate || isGeyser) ? "bedrock" : "java";
            new BukkitRunnable(){
                @Override
                public void run(){
                    common.consoleExec("execute as " + playerUUIDStr + " run function #pcub:player_join/" + edition);
                }
            }.runTaskLater(main, 0L);
        }
    }



    // 容器打开事件
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        ChestMenu.open(player.getName(), player.getUniqueId().toString(), common);
    }



    // 容器点击事件
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 获取玩家
        Player player = (Player) event.getWhoClicked();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();
        String playerUUIDStr = playerUUID.toString();
        // 获取事件类型
        InventoryAction currentAction = event.getAction();
        ClickType currentClick = event.getClick();
        Inventory currentInv = event.getClickedInventory();
        InventoryType currentInvType = (currentInv != null) ? currentInv.getType() : null;
        int currentSlot = event.getSlot();
        // 获取指针物品和槽位物品
        Stacker.StackerCommon current = new Stacker.StackerCommon(event.getCurrentItem());
        Stacker.StackerCommon cursor = new Stacker.StackerCommon(event.getCursor());

        // 调试信息
        if (common.debug) common.debugLogger(playerName + " " + currentClick + " " + currentInvType + " " + currentSlot + " " + currentAction + " 指针:" + cursor.type + " 槽位:" + current.type);

        if(common.legacyStack) {
            stacker.legacyStack(
                    event,
                    player,
                    playerName,
                    playerUUIDStr,
                    currentAction,
                    currentClick,
                    currentInv,
                    currentInvType,
                    currentSlot,
                    current,
                    cursor
            );
            return;
        }

        // 末影箱菜单
        if (
                common.getScore("screen", playerName) >= 0 &&
                ChestMenu.checkWork(cursor, current, playerUUIDStr, common).limit
        ) event.setCancelled(true);
        // 强制合并（常规交换）
        else if (currentAction == InventoryAction.SWAP_WITH_CURSOR) {
            int margeResult = new Stacker.MergeItem(current, cursor).checkWork();
            if (margeResult > 0 && common.debug) common.debugLogger(playerName + " 强制交换合并 x" + margeResult);
        }

        // 主手检测
        mainhandCheck.checkInventoryClick(player, currentSlot, currentAction);
    }



    // 容器关闭事件
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        ChestMenu.close((Player) event.getPlayer(), common);
    }



    // 实体捡起物品
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        LivingEntity livingEntity = event.getEntity();
        // 主手检测
        mainhandCheck.checkPickupItem(livingEntity);
    }



    // 玩家主手切换
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        // 主手检测
        mainhandCheck.work(player);
    }



    // 玩家丢出物品
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        // 主手检测
        mainhandCheck.checkDropItem(player);
    }



    // 玩家交互事件
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player targetPlayer = event.getPlayer();
        String targetName = targetPlayer.getName();
        UUID targetIDN = targetPlayer.getUniqueId();
        String targetID = targetIDN.toString();
        boolean isBedrock =
                common.geyserValid &&
                common.geyserApi.isBedrockPlayer(targetIDN)
            ||
                common.floodgateValid &&
                common.floodgateApi.isFloodgatePlayer(targetIDN);
        ItemStack usedItem = event.getItem();
        Action action = event.getAction();
        Block clickedBlock = event.getClickedBlock();
        // 调试
        if (common.debug) common.debugLogger(targetName + " " + action + " " + ((usedItem != null) ? usedItem.getType() : "") + " " + ((clickedBlock != null) ? clickedBlock.getType() : ""));
        boolean blockFunction = false;
        // 右键方块检测
        if (action == Action.RIGHT_CLICK_BLOCK) {
            Material targetMat = (clickedBlock != null) ? clickedBlock.getType() : Material.AIR;
            String targetStr = targetMat.toString();
            // 取消冒险玩家的食用蛋糕、破坏花盆操作
            if ((targetStr.startsWith("POTTED_") || targetMat == Material.CAKE) && targetPlayer.getGameMode() == GameMode.ADVENTURE) event.setCancelled(true);
            // 检查方块是否可操作
            else if ((!targetPlayer.isSneaking() || usedItem == null) && !targetMat.isAir()) {
                blockFunction = (
                    targetMat == Material.DISPENSER ||
                    targetMat == Material.LEVER ||
                    targetMat == Material.NOTE_BLOCK ||
                    targetMat == Material.DROPPER ||
                    targetMat == Material.JUKEBOX ||
                    targetMat == Material.HOPPER ||
                    targetMat == Material.CHEST ||
                    targetMat == Material.ENDER_CHEST ||
                    targetMat == Material.TRAPPED_CHEST ||
                    targetStr.endsWith("BUTTON")||
                    targetStr.endsWith("DOOR") ||
                    targetStr.endsWith("GATE") ||
                    targetStr.endsWith("SIGN")
                );
                // 开启钱庄箱子
                if (targetMat == Material.ENDER_CHEST) ChestMenu.readyOpen(targetName, targetID, common);
            }
        }
        if (action == Action.RIGHT_CLICK_AIR || !blockFunction && action == Action.RIGHT_CLICK_BLOCK) {
            ItemMeta usedMeta = null;
            if (usedItem != null) usedMeta = usedItem.getItemMeta();
            Material usedType = null;
            if (usedItem != null) usedType = usedItem.getType();
            // 雪球、丹药投掷限制
            if (
                (
                    usedType == Material.SNOWBALL ||
                    usedType == Material.SPLASH_POTION
                ) && dropLimiter.check(
                        targetName,
                        targetID,
                        usedItem,
                        isBedrock,
                        common.getTempScore("is_touch", targetID) == 1
                    ).limit
            ) event.setCancelled(true);
            // 使用物品执行命令
            State shortcutResult = useItemToRun.checkCommandExecute(targetPlayer, usedMeta, isBedrock);
            if (shortcutResult.limit) event.setCancelled(true);
            // 基岩版副手功能
            if (!shortcutResult.success && isBedrock) {
                useItemToRun.bedrockOffhand(targetPlayer, targetName, usedType);
            }
        }
    }



    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        if (
            entity.getType() == EntityType.VILLAGER &&
            VillagerFeature.beforeOpen(player, entity, common).limit
        ) event.setCancelled(true);
    }



    // 玩家切换潜行
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        fastSkill.check(event.getPlayer(), event.isSneaking());
    }

    // 玩家钓鱼
    //@EventHandler
    //public void onPlayerFish(PlayerFishEvent event) {
        //Player targetPlayer = event.getPlayer();
        //UUID targetIDN = targetPlayer.getUniqueId();
        //FloodgateApi fgInstance = FloodgateApi.getInstance();
        //boolean isBedrock = fgInstance.isFloodgatePlayer(targetIDN);
        // 基岩鱼竿菜单（实验）
        /// 该方案暂不能应用于***副手功能***，因为将物品转化为鱼竿会改变原有的物品标签
        //if(isBedrock) event.setCancelled(bedrockMenu(targetPlayer.getInventory().getItemInMainHand(), targetPlayer));
    //}

    public EventListener(Common common) {
        this.common = common;
        this.main = common.main;
        this.useItemToRun = new UseItemToRun(common);
        this.fastSkill = new FastSkill(common);
        this.dropLimiter = new DropLimiter(common);
        this.stacker = new Stacker(common);
        this.mainhandCheck = new MainhandCheck();
    }
}

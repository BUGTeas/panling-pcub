package org.pcub.extension;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.util.DeviceOs;
import org.geysermc.geyser.api.GeyserApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        //getLogger().info("盘灵无界核心插件正在加载...");
        server.getPluginManager().registerEvents(new eventListener(), this);
        Bukkit.getPluginCommand("pcub").setExecutor(new eventListener());
        Bukkit.getPluginCommand("pcub").setTabCompleter(new eventListener());
        if(!geyserVaild) getLogger().info("找不到 Geyser 插件，BE 玩家将无法通过菜单书查看成就进度。（可使用命令 “/geyser advancements”）");
//        if(!fgVaild) getLogger().info("找不到 Floodgate 插件，需要在启动参数中添加“-Djdk.util.jar.enableMultiRelease=force”，BE玩家才能正常使用菜单书。");
    }
    /*@Override
    public void onDisable() {
        getLogger().info("盘灵无界核心插件正在停止...");
    }*/

    //获取主类
    main myPlugin = this;

    //获取控制台
    Server server = getServer();
    CommandSender console = server.getConsoleSender();

    //检查Geyser插件
    boolean geyserVaild = getServer().getPluginManager().getPlugin("Geyser-Spigot") != null;
    //检查Floodgate插件
    boolean fgVaild = getServer().getPluginManager().getPlugin("floodgate") != null;

    public class testClass {
        public static void main(){}
    }

    public class eventListener implements Listener, CommandExecutor, TabExecutor {
        //控制台执行器
        public void plConsoleExec(String command){
            server.dispatchCommand(console, command);
        }

        //获取原版记分板
        Scoreboard mainscb = server.getScoreboardManager().getMainScoreboard();

        //获取原版记分板分数
        public int plGetScore(String objective, String target) {
            if (mainscb.getObjective(objective) != null) return mainscb.getObjective(objective).getScore(target).getScore();
            else {
                getLogger().warning("获取分数失败，找不到记分项：“" + objective + "”。（已返回0）");
                return 0;
            }
        }

        //设置原版记分板分数
        public void plSetScore(String objective, String target, int score) {
            if (mainscb.getObjective(objective) != null) mainscb.getObjective(objective).getScore(target).setScore(score);
            else getLogger().warning("设置分数失败，找不到记分项：“" + objective + "”。");
        }

        //创建临时记分板
        Scoreboard tempscb = server.getScoreboardManager().getNewScoreboard();

        //获取临时记分板分数
        public int plGetTempScore(String objective, String target) {
            if (tempscb.getObjective(objective) != null) return tempscb.getObjective(objective).getScore(target).getScore();
            else return 0;
        }

        //设置临时记分板分数
        public void plSetTempScore(String objective, String target, int score) {
            if (tempscb.getObjective(objective) == null) tempscb.registerNewObjective(objective, "dummy");
            tempscb.getObjective(objective).getScore(target).setScore(score);
        }

        //检查是否为钱庄箱子按钮
        public boolean isButton(ItemStack target) {
            boolean button = false;
            if(target != null && target.getItemMeta() != null) {
                String itemType = target.getType().toString().toLowerCase();
                String itemMeta = target.getItemMeta().getAsString();
                String[] btnList = {
                    "player_head",
                    "chest",
                    "blaze_powder",
                    "brewing_stand",
                    "clock",
                    "black_stained_glass_pane",
                    "gray_stained_glass_pane",
                    "wooden_axe",
                    "experience_bottle",
                    "diamond_chestplate",
                    "wooden_sword",
                    "leather_boots",
                    "brick",
                    "paper",
                    "snowball",
                    "iron_chestplate"
                };
                //检查是否为按钮类物品
                for (String btn : btnList) if (itemType.equals(btn)) {
                    //获取自定义标签
                    String[] metaList = itemMeta.substring(1, itemMeta.length() - 1).split(",");
                    //检查是否有按钮标签
                    for (String s : metaList) if (s.equals("clickable:1")) {
                        button = true;
                        break;
                    }
                    break;
                }
            }
            return button;
        }

        //频繁操作限制
        //设置状态
        public void setOperationLimit(String target, long delay) {
            int clickCnt = plGetTempScore("operating_limit_count", target);
            clickCnt ++;
            plSetTempScore("operating_limit_count", target, clickCnt);
            int lastClickCnt = clickCnt;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(plGetTempScore("operating_limit_count", target) == lastClickCnt) {
                        plSetTempScore("operating_limit_count", target, 0);
                    }
                }
            }.runTaskLater(myPlugin, delay);
        }
        //获取状态
        public boolean getOperationLimit(String target, int when) {
            return plGetTempScore("operating_limit_count", target) >= when;
        }

        /*
        ==================================
        ==========系统监听事件部分==========
        ==================================
         */

        //玩家进服事件
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player targetPlayer = event.getPlayer();
            String targetName = targetPlayer.getName();
            String targetDisplay = targetPlayer.getDisplayName();
            UUID targetIDN = targetPlayer.getUniqueId();
            String targetID = targetIDN.toString();
            FloodgateApi fgInstance = (!fgVaild) ? null : FloodgateApi.getInstance();
            FloodgatePlayer fgPlayer = (!fgVaild) ? null : fgInstance.getPlayer(targetIDN);
            boolean isGeyser = geyserVaild && GeyserApi.api().isBedrockPlayer(targetIDN);
            boolean isFloodgate = fgVaild && fgInstance.isFloodgatePlayer(targetIDN);
            //当Floodgate登录出现异常时踢出玩家
            if (fgVaild && !isFloodgate && geyserVaild && isGeyser) {
                targetPlayer.kickPlayer("登录系统出现异常， 请重新加入服务器。");
                getLogger().info("\n" + targetDisplay + " 因为 Floodgate 登录异常无法正常加入游戏");
                plSetTempScore("login_status", targetID, 0);
            } else {
                plSetTempScore("is_touch", targetID, (
                    isFloodgate && (
                        fgPlayer.getDeviceOs() == DeviceOs.GOOGLE ||
                        fgPlayer.getDeviceOs() == DeviceOs.IOS ||
                        fgPlayer.getDeviceOs() == DeviceOs.WINDOWS_PHONE
                    ) ||
                    !fgVaild && geyserVaild && isGeyser
                ) ? 1 : 0);
                plSetTempScore("login_status", targetID, (isFloodgate || isGeyser) ? 2 : 1);
                plSetTempScore("operating_limit_count", targetID, 0);
                plSetTempScore("clicked", targetID, 0);
                plSetTempScore("inventory_opened", targetID, 0);
                plSetTempScore("pot_clicked", targetID, 0);
                cancelSneak(targetPlayer);
                String edition = (isFloodgate || isGeyser) ? "bedrock" : "java";
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        plConsoleExec("execute as " + targetID + " run function #pcub:join_" + edition);
//                        if (plGetScore("fcub_hidden", targetName) == 0) plConsoleExec("execute as " + targetID + " run tellraw @a[name=!" + targetName + "] [{\"translate\":\"fcub.player." + edition + "\"},{\"text\":\" \"},{\"selector\":\"@s\",\"color\":\"yellow\"},{\"text\":\" \"},{\"translate\":\"fcub.player.joined\",\"color\":\"yellow\"}]");
//                        if (!(isFloodgate && !fgPlayer.isLinked())) targetPlayer.performCommand("bskin quiet " + targetName);
                    }
                }.runTaskLater(myPlugin, 0L);
//                getLogger().info("\n" + edition.toUpperCase().charAt(0) + edition.substring(1) + " 玩家 " + targetDisplay + "（" + targetName + "）加入了游戏");
            }
            //幻域无界独有
//            event.setJoinMessage(null);
        }

        //玩家退出事件
        //幻域无界独有
//        @EventHandler
//        public void onPlayerQuit(PlayerQuitEvent event) {
//            Player targetPlayer = event.getPlayer();
//            String targetName = targetPlayer.getName();
//            String targetDisplay = targetPlayer.getDisplayName();
//            UUID targetIDN = targetPlayer.getUniqueId();
//            String targetID = targetIDN.toString();
//            if (plGetTempScore("login_status", targetID) > 0) {
//                String edition = (plGetTempScore("login_status", targetID) > 1) ? "bedrock" : "java";
//                plConsoleExec("execute as " + targetID + " run function aiod:timer_sync");
//                if (plGetScore("fcub_hidden", targetName) == 0) plConsoleExec("execute as " + targetID + " run tellraw @a [{\"translate\":\"fcub.player." + edition + "\"},{\"text\":\" \"},{\"selector\":\"@s\",\"color\":\"yellow\"},{\"text\":\" \"},{\"translate\":\"fcub.player.left\",\"color\":\"yellow\"}]");
//                getLogger().info("\n" + edition.toUpperCase().charAt(0) + edition.substring(1) + " 玩家 " + targetDisplay + "（" + targetName + "）退出了游戏");
//                if(getServer().getOnlinePlayers().size() == 1) getLogger().info("所有玩家已退出！");
//            }
//            event.setQuitMessage(null);
//        }

        //容器打开事件
        @EventHandler
        public void onInventoryOpen(InventoryOpenEvent event) {
            Player targetPlayer = (Player) event.getPlayer();
            String targetName = targetPlayer.getName();
            String targetID = targetPlayer.getUniqueId().toString();
            //if (targetPlayer.isOp()) targetPlayer.chat(Bukkit.getWorld("world").getFullTime() + " 容器打开");
            int invOpened = plGetTempScore("inventory_opened", targetID);
            if (invOpened == 1) plSetScore("pcub_hopper_opened", targetName, 1);
            if (invOpened == 2) plConsoleExec("execute as " + targetID + " run function pld:system/chest_menu/open");
        }

        //快速移动变量
        Player moveUser = null;
        ItemStack moveFromCopy = null;
        Inventory moveFormInv = null;
        InventoryType moveFormInvType = null;
        int moveFormSlot = 0;

        //容器点击事件
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            //获取玩家
            Player targetPlayer = (Player) event.getWhoClicked();
            String targetName = targetPlayer.getName();
            String targetID = targetPlayer.getUniqueId().toString();
            //获取事件类型
            InventoryAction currentAction = event.getAction();
            ClickType currentClick = event.getClick();
            Inventory currentInv = event.getClickedInventory();
            InventoryType currentInvType = (currentInv != null) ? currentInv.getType() : null;
            int currentSlot = event.getSlot();
            //获取指针物品和槽位物品
            Material currentType = null,cursorType = null;
            ItemMeta currentMeta = null,cursorMeta = null;
            //String currentTypeStr = "{}",cursorTypeStr = "{}";
            String currentMetaStr = "{}",cursorMetaStr = "{}";
            //槽位物品
            ItemStack currentItem = event.getCurrentItem();
            if(currentItem != null) {
                currentType = currentItem.getType();
                //currentTypeStr = currentType.toString();
                currentMeta = currentItem.getItemMeta();
                if(currentMeta != null) currentMetaStr = currentMeta.getAsString();
            }
            //指针物品
            ItemStack cursorItem = event.getCursor();
            if(cursorItem != null) {
                cursorType = cursorItem.getType();
                //cursorTypeStr = cursorType.toString();
                cursorMeta = cursorItem.getItemMeta();
                if(cursorMeta != null) cursorMetaStr = cursorMeta.getAsString();
            }

            //调试信息
            //if (targetPlayer.isOp()) targetPlayer.chat(Bukkit.getWorld("world").getFullTime() + " " + currentClick + " " + currentInvType + " " + currentSlot + " " + event.getAction() + " 指针:" + cursorTypeStr/* + CursorMetaStr*/ + " 槽位:" + currentTypeStr/* + CurrentMetaStr*/);

            //将丹药的模型叠放转换为真实叠放
            if ((cursorType == Material.POTION || cursorType == Material.SPLASH_POTION) && currentInvType == InventoryType.PLAYER && potionModelToStack(cursorItem, cursorType, cursorMeta)) {
                cursorMeta = cursorItem.getItemMeta();
                cursorMetaStr = cursorMeta.getAsString();
            }
            //判断点击类型
            if (currentAction == InventoryAction.SWAP_WITH_CURSOR) {
                //强制合并（常规交换）
                int margeResult = margeItem(currentItem, cursorItem, currentType, currentMetaStr, cursorType, cursorMetaStr);
                //if (margeResult != 0) getLogger().info(targetName + "：已强制交换合并 " + margeResult + " 个。");
            } else if (currentAction == InventoryAction.HOTBAR_SWAP) {
                //强制合并&刷新（HOTBAR_SWAP）
                int hotbarSlot = event.getHotbarButton();
                if (currentSlot != hotbarSlot) {
                    ItemStack targetItem = targetPlayer.getInventory().getItem(hotbarSlot);
                    int margeResult = margeItem(currentItem, targetItem, currentType, currentMetaStr, null, null);
                    if (margeResult != 0){
                        event.setCancelled(true);
                        //getLogger().info(targetName + "：已强制快捷栏合并 " + margeResult + " 个。");
                    }
                    if (targetItem != null && isForceStack(targetItem.getType())) new BukkitRunnable() {
                        @Override
                        public void run() {
                            targetPlayer.updateInventory();
                        }
                    }.runTaskLaterAsynchronously(myPlugin,0L);
                }
            } else if (currentAction.toString().startsWith("PICKUP_") && isForceStack(currentType)) {
                //当变量被其他玩家占用、或同一刻下再次拿起，则阻止
                if (moveUser != null) event.setCancelled(true);
                else if (currentInvType != InventoryType.MERCHANT || currentSlot != 2) {
                    //移动
                    moveUser = targetPlayer;
                    moveFromCopy = (currentItem != null) ? new ItemStack(currentItem) : null;
                    moveFormInv = currentInv;
                    moveFormInvType = currentInvType;
                    moveFormSlot = currentSlot;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            moveUser = null;
                        }
                    }.runTaskLater(myPlugin, 2L);
                }
            }
            //NPC丹药修复
            /*
            Java
                7177519 LEFT MERCHANT PICKUP_ALL 指针:air 槽位:potion
                7177558 LEFT PLAYER PLACE_ALL 指针:potion 槽位:air
            BedrockMouse
                7192938 LEFT MERCHANT PICKUP_ALL 指针:air 槽位:potion
                7192938 RIGHT MERCHANT PICKUP_ALL 指针:potion 槽位:potion X T
                7192938 RIGHT MERCHANT PICKUP_ALL 指针:potion 槽位:potion X
            BedrockTouch/Shift
                7199539 RIGHT MERCHANT PICKUP_HALF 指针:air 槽位:potion
                7199539 RIGHT PLAYER PLACE_ONE 指针:potion 槽位:air X T
                7199539 LEFT MERCHANT PICKUP_ALL 指针:air 槽位:potion X
            */
            boolean notPickup = getOperationLimit("pu" + targetID, 1);
            //全局过滤多余操作
            if (notPickup) {
                if (currentAction.toString().startsWith("PICKUP_") || currentAction == InventoryAction.SWAP_WITH_CURSOR) event.setCancelled(true);
                setOperationLimit("pu" + targetID, 2L);
            }
            if (currentInvType == InventoryType.MERCHANT && currentSlot == 2) {
                //交易目标拿起
                //（鼠标左键）同一刻内第二次触发则开始自动归位
                if (
                    notPickup &&
                    currentAction == InventoryAction.PICKUP_ALL &&
                    currentClick == ClickType.RIGHT &&
                    cursorItem != null &&
                    cursorType != Material.AIR
                ) afterTrade(cursorItem, targetPlayer);
                //第一次点击
                else if (!notPickup) {
                    //限制一刻内不能拿起
                    setOperationLimit("pu" + targetID, 2L);
                    //当丹药存在模型数据时，阻止JE的Shift交易
                    if (currentAction == InventoryAction.MOVE_TO_OTHER_INVENTORY && currentMeta.hasCustomModelData() && isForceStack(currentType)) event.setCancelled(true);
                }
            } else if (plGetScore("screen", targetName) >= 0) {
                //钱庄箱子按钮点击
                boolean buttonOnCursor = isButton(cursorItem);
                boolean buttonOnCurrent = isButton(currentItem);
                //过滤操作
                if (
                    //玩家频繁操作
                    buttonOnCurrent && getOperationLimit("ep" + targetID, 5) ||
                    //箱子整理模组、GeyserMC 导致同一刻下的多次点击
                    plGetTempScore("clicked", targetID) > 0 ||
                    //将其他物品与按钮互换
                    cursorType != Material.AIR && !buttonOnCursor && buttonOnCurrent
                ) event.setCancelled(true);
                //否则按下按钮开启限制，在下一刻执行按键函数并解除限制
                else if (buttonOnCurrent) {
                    plSetTempScore("clicked", targetID, 1);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            plConsoleExec("execute as " + targetID + " run function #pcub:chest_menu_click");
                            plSetTempScore("clicked", targetID, 0);
                        }
                    }.runTaskLater(myPlugin, 0L);
                }
                //过滤玩家频繁操作
                if (buttonOnCurrent) setOperationLimit("ep" + targetID, 10L);
            }
            //放置/交换非常规叠放物品
            if(
                currentClick != ClickType.CREATIVE &&
                currentAction != InventoryAction.NOTHING &&
                currentAction != InventoryAction.PICKUP_ALL &&
                currentAction != InventoryAction.PICKUP_SOME &&
                currentAction != InventoryAction.PICKUP_HALF &&
                currentAction != InventoryAction.PICKUP_ONE && (
                    isForceStack(cursorType) ||
                    isForceStack(currentType)
                )
            ) {
                if (moveUser != null && moveUser != targetPlayer && !notPickup) event.setCancelled(true);
                //基岩版双击/Shift移动散开修复
                //当PICKUP_HALF触发时将变量origin设为current，下一刻清除
                //如果同一刻又触发了PLACE_ONE，则直接将current设为origin，然后将cursor、origin叠放设为0
                if (
                    moveUser == targetPlayer &&
                    (moveFormSlot != currentSlot || moveFormInvType != currentInvType)
                ) {
                    Material srcType = moveFromCopy.getType();
                    ItemMeta srcMeta = moveFromCopy.getItemMeta();
                    String srcMetaStr = (srcMeta != null) ? srcMeta.getAsString() : "";
                    //快速移动
                    //目标槽位没有任何物品，则触发此项
                    if (currentType == Material.AIR || currentType == null) {
                        if (!notPickup) event.setCancelled(true);
                        event.setCurrentItem(moveFromCopy);
                        if (cursorItem != null) cursorItem.setAmount(0);
                        //moveFrom.setAmount(0);
                        ItemStack srcSlotItem = moveFormInv.getItem(moveFormSlot);
                        if (srcSlotItem != null) srcSlotItem.setAmount(0);
                        getLogger().info(targetName + "：已强制移动。");
                        //限制一刻内不能拿起
                        setOperationLimit("pu" + targetID, 2L);
                    }
                    //双击合并
                    //目标已有物品与源物品相同，则触发此项
                    else if (currentType == srcType && srcMetaStr.equals(currentMetaStr)) {
                        if (!notPickup) event.setCancelled(true);
                        ArrayList<ItemStack> itemList = new ArrayList<>();
                        int itemOriginAmount = currentItem.getAmount() + ((cursorItem != null) ? cursorItem.getAmount() : 0);
                        int itemAmount = itemOriginAmount;
                        //从当前容器获取同类物品
                        for (int i = 0; i < currentInv.getSize(); i ++) {
                            ItemStack item = currentInv.getItem(i);
                            ItemMeta tempMeta = (item != null) ? item.getItemMeta() : null;
                            if (
                                i != currentSlot &&
                                item != null &&
                                item.getType() == srcType &&
                                srcMetaStr.equals((tempMeta != null) ? tempMeta.getAsString() : "")
                            ) {
                                itemList.add(item);
                                itemAmount += item.getAmount();
                            }
                        }
                        //如果当前容器不是玩家背包，则另外从背包获取一遍
                        if (currentInvType != InventoryType.PLAYER) for (int i = 0; i < 36; i ++) {
                            ItemStack item = targetPlayer.getInventory().getItem(i);
                            ItemMeta tempMeta = (item != null) ? item.getItemMeta() : null;
                            if (
                                item != null &&
                                item.getType() == srcType &&
                                srcMetaStr.equals((tempMeta != null) ? tempMeta.getAsString() : "")
                            ) {
                                itemList.add(item);
                                itemAmount += item.getAmount();
                            }
                        }
                        //将双击处的物品设置成同类物品数量的总和，超出64的部分保持原样
                        if (itemAmount > 64) {
                            int remainderAmount = 64 - itemOriginAmount;
                            currentItem.setAmount(64);
                            for (ItemStack itemStack : itemList) {
                                if (itemStack.getAmount() < remainderAmount) {
                                    remainderAmount -= itemStack.getAmount();
                                    itemStack.setAmount(0);
                                } else {
                                    itemStack.setAmount(itemStack.getAmount() - remainderAmount);
                                    break;
                                }
                            }
                        } else {
                            currentItem.setAmount(itemAmount);
                            for (ItemStack itemStack : itemList) itemStack.setAmount(0);
                        }
                        //清空指针上的多余
                        if (cursorItem != null) cursorItem.setAmount(0);
                        getLogger().info(targetName + "：已强制双击合并。");
                        //限制一刻内不能拿起
                        setOperationLimit("pu" + targetID, 2L);
                    }
                }
                //（触屏单击/Shift）同一刻内第二次触发则开始自动归位
                else if (
                    notPickup &&
                    cursorItem != null &&
                    cursorType != Material.AIR
                ) {
                    afterTrade(cursorItem, targetPlayer);
                    event.setCancelled(true);
                }
                //放置物品后强制刷新物品栏
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        targetPlayer.updateInventory();
                    }
                }.runTaskLaterAsynchronously(myPlugin,0L);
            }
            //丹药放入（0.4旧版）
            //plSetScore("pcub_inventory_fix_delay", targetPlayer.getName(), 2);
        }

        //容器关闭事件
        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            Player targetPlayer = (Player) event.getPlayer();
            String targetName = targetPlayer.getName();
            if (plGetScore("screen", targetName) >= 0) {
                plConsoleExec("execute as " + targetName + " run function #pcub:chest_menu_leave");
            }
            if (plGetScore("pcub_hopper_opened", targetName) == 1) new BukkitRunnable() {
                @Override
                public void run() {
                    plSetScore("pcub_hopper_opened", targetName, 0);
                }
            }.runTaskLaterAsynchronously(myPlugin,5L);
        }

        //玩家交互事件
        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event) {
            Player targetPlayer = event.getPlayer();
            String targetName = targetPlayer.getName();
            UUID targetIDN = targetPlayer.getUniqueId();
            String targetID = targetIDN.toString();
            boolean isGeyser = geyserVaild && GeyserApi.api().isBedrockPlayer(targetIDN);
            boolean isFloodgate = fgVaild && FloodgateApi.getInstance().isFloodgatePlayer(targetIDN);
            //调试
            //String cbt = "";
            //if (event.getClickedBlock() != null) cbt = " " + event.getClickedBlock().getType();
            //getLogger().info(Bukkit.getWorld("world").getFullTime() + " " + event.getAction() + cbt);
            boolean blockFunction = false;
            //右键方块检测
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Material targetMat = event.getClickedBlock().getType();
                String targetStr = targetMat.toString();
                //取消冒险玩家的食用蛋糕、破坏花盆操作
                if ((targetStr.startsWith("POTTED_") || targetMat == Material.CAKE) && targetPlayer.getGameMode() == GameMode.ADVENTURE) event.setCancelled(true);
                //检查方块是否可操作
                else if (!targetPlayer.isSneaking() && !targetMat.isAir()) {
                    if (
                        targetStr.endsWith("CHEST") ||
                        targetStr.endsWith("BUTTON")||
                        targetStr.endsWith("DOOR") ||
                        targetStr.endsWith("GATE") ||
                        targetStr.endsWith("SIGN") ||
                        targetMat == Material.DISPENSER ||
                        targetMat == Material.LEVER ||
                        targetMat == Material.NOTE_BLOCK ||
                        targetMat == Material.DROPPER ||
                        targetMat == Material.JUKEBOX
                    ) blockFunction = true;
                    //开启漏斗（基岩版）
                    else if (targetMat == Material.HOPPER) {
                        blockFunction = true;
                        if (isFloodgate || isGeyser) plSetTempScore("inventory_opened", targetID, 1);
                    }
                    //开启钱庄箱子
                    if (targetMat == Material.ENDER_CHEST) plSetTempScore("inventory_opened", targetID, 2);
                    if (blockFunction) new BukkitRunnable() {
                        @Override
                        public void run(){
                            plSetTempScore("inventory_opened", targetID, 0);
                        }
                    }.runTaskLaterAsynchronously(myPlugin,0L);
                }
            }
            if (event.getAction() == Action.RIGHT_CLICK_AIR || !blockFunction && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                ItemStack usedItem = event.getItem();
                ItemMeta usedMeta = null;
                if (usedItem != null) usedMeta = usedItem.getItemMeta();
                Material usedType = null;
                if (usedItem != null) usedType = usedItem.getType();
                //雪球、丹药投掷限制
                if (usedType == Material.SNOWBALL || usedType == Material.SPLASH_POTION) {
                    boolean needCancel = getOperationLimit("dp" + targetID, 1);
                    //投掷速度单位为每投一个间隔刻数
                    int enableContinuous = plGetScore("pcub_enable_continuous", targetName),dropSpeed = 7;
                    if(needCancel) event.setCancelled(true);
                    if (
                        //始终禁用连续投掷
                        enableContinuous == 0 ||
                        //仅基岩版禁用连续投掷
                        enableContinuous == 2 && (isFloodgate || isGeyser) ||
                        //仅移动端禁用连续投掷
                        enableContinuous == 3 && plGetTempScore("is_touch", targetID) == 1
                    ) needCancel = false;
                    else dropSpeed = plGetScore("pcub_drop_interval", targetName);
                    if (!needCancel) setOperationLimit("dp" + targetID, dropSpeed);
                }
                //基岩版功能
                if ((isFloodgate || isGeyser) && usedMeta != null) {
                    boolean openMenu = usedType == Material.BOOK && bedrockMenu(usedItem, targetPlayer);
                    //副手功能
                    if (!openMenu && plGetScore("pcub_player_interact", targetName) == 0) {
                        //发出执行请求
                        plSetScore("pcub_player_interact", targetName, 1);
                        //0.5秒CD
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                plSetScore("pcub_player_interact", targetName, 0);
                            }
                        }.runTaskLaterAsynchronously(myPlugin, 10L);
                    }
                }
            }
        }

        //潜行记录
        ArrayList<BukkitRunnable> sneakSkill = new ArrayList<>();
        ArrayList<Player> sneakSkillPlayer = new ArrayList<>();

        //玩家切换潜行
        @EventHandler
        public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
            Player targetPlayer = event.getPlayer();
            String targetName = targetPlayer.getName();
            UUID targetIDN = targetPlayer.getUniqueId();
            String targetID = targetIDN.toString();
            boolean isGeyser = geyserVaild && GeyserApi.api().isBedrockPlayer(targetIDN);
            boolean isFloodgate = fgVaild && FloodgateApi.getInstance().isFloodgatePlayer(targetIDN);
            ItemStack currentItem = targetPlayer.getInventory().getItemInMainHand();
            int enableFastskill = plGetScore("pcub_enable_fastskill", targetName);
            if (
                plGetScore("job", targetName) == 0 && (
                    enableFastskill == 1 ||
                    enableFastskill == 2 && (isFloodgate || isGeyser) ||
                    enableFastskill == 3 && plGetTempScore("is_touch", targetID) == 1
                )
            ) {
                if (event.isSneaking()) {
                    ItemMeta currentMeta = currentItem.getItemMeta();
                    if (currentItem.getType() == Material.CARROT_ON_A_STICK && currentMeta != null) {
                        sneakSkillPlayer.add(targetPlayer);
                        BukkitRunnable runnable = new BukkitRunnable() {
                            @Override
                            public void run() {
                                ItemMeta afterMeta = targetPlayer.getInventory().getItemInMainHand().getItemMeta();
                                if (afterMeta != null && afterMeta.getAsString().equals(currentMeta.getAsString())) {
                                    plSetScore("right_click_check", targetName, 1);
                                    if (plGetScore("sneak_check", targetName) < 1) plSetScore("sneak_check", targetName, 1);
                                }
                            }
                        };
                        sneakSkill.add(runnable);
                        runnable.runTaskLaterAsynchronously(myPlugin, plGetScore("pcub_fastskill_duration", targetName));
                    }
                } else cancelSneak(targetPlayer);
            }
        }

        //玩家钓鱼
        //@EventHandler
        //public void onPlayerFish(PlayerFishEvent event) {
            //Player targetPlayer = event.getPlayer();
            //UUID targetIDN = targetPlayer.getUniqueId();
            //FloodgateApi fgInstance = FloodgateApi.getInstance();
            //boolean isBedrock = fgInstance.isFloodgatePlayer(targetIDN);
            //基岩鱼竿菜单（实验）
            //该方案暂不能应用于副手功能，因为将物品转化为鱼竿会改变原有的物品标签
            //if(isBedrock) event.setCancelled(bedrockMenu(targetPlayer.getInventory().getItemInMainHand(), targetPlayer));
        //}

        //用户命令
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("该命令只能在玩家的聊天栏中执行！");
                return true;
            }
            Player targetPlayer = (Player) sender;
            String targetName = targetPlayer.getName();
            UUID targetIDN = targetPlayer.getUniqueId();
            FloodgateApi fgInstance = (!fgVaild) ? null : FloodgateApi.getInstance();
            boolean isGeyser = geyserVaild && GeyserApi.api().isBedrockPlayer(targetIDN);
            boolean isFloodgate = fgVaild && fgInstance.isFloodgatePlayer(targetIDN);
            String locale = targetPlayer.getLocale();
            boolean isCN = locale.equalsIgnoreCase("zh_cn");
            if (args.length >= 1 && args[0].equalsIgnoreCase("stack")) new BukkitRunnable(){
                //强制叠放物品
                @Override
                public void run(){
                    sender.sendMessage("§a" + ((isCN) ? "已强制叠放" : "已強制疊放") + " §b" + forceStack(targetPlayer, 64) + ((isCN) ? " 种" : " 種") + "物品");
                }
            }.runTaskAsynchronously(myPlugin);
            else if (args.length >= 1 && args[0].equalsIgnoreCase("dropContinuous")) new BukkitRunnable(){
                //连续投掷
                @Override
                public void run(){
                    String[] setResult = (isCN) ? new String[]{"始终禁用", "始终启用", "仅基岩版禁用", "仅 Android/iOS/WP 禁用"} : new String[]{"始終禁用", "始終啟用", "僅 Bedrock 版禁用", "僅 Android/iOS/WP 禁用"};
                    if (args.length >= 2) {
                        int toInt;
                        if (args[1].equalsIgnoreCase("mobileOnly")) toInt = 3;
                        else if (args[1].equalsIgnoreCase("bedrockOnly")) toInt = 2;
                        else if (args[1].equalsIgnoreCase("enable")) toInt = 1;
                        else if (args[1].equalsIgnoreCase("disable")) toInt = 0;
                        else toInt = -1;
                        if (toInt >= 0) {
                            plSetScore("pcub_enable_continuous", targetName, toInt);
                            sender.sendMessage("§a" + ((isCN) ? "连续投掷 已设置为" : "連續投擲 已設定為") + ": §b" + setResult[toInt]);
                        } else sender.sendMessage((isCN) ? "§c错误: 不正确的参数。§r\n去掉所有参数以获得帮助。\n(如需获取当前值请去掉最后一个参数)" : "§c錯誤: 不正確的引數。§r\n去掉所有引數以獲得幫助。\n(如需獲取當前值請去掉最後一個引數)");
                    } else sender.sendMessage("§a" + ((isCN) ? "当前 连续投掷 为" : "當前 連續投擲 為") + ": §b" + setResult[plGetScore("pcub_enable_continuous", targetName)]);
                }
            }.runTaskAsynchronously(myPlugin);
            else if (args.length >= 1 && args[0].equalsIgnoreCase("dropInterval")) new BukkitRunnable(){
                //每次投掷间隔
                @Override
                public void run(){
                    if (args.length >= 2) {
                        if (args[1].matches("[0-9]*")) {
                            int toInt = Integer.parseInt(args[1]);
                            if (toInt <= 20) {
                                plSetScore("pcub_drop_interval", targetName, toInt);
                                sender.sendMessage("§a" + ((isCN) ? "连续投掷间隔 已设置为" : "連續投擲間隔 已設定為") + ": §b" + args[1] + " 刻");
                            } else sender.sendMessage((isCN) ? "§c错误: 此值不能超过 20。" : "§c錯誤: 此值不能超過 20。");
                        } else sender.sendMessage((isCN) ? "§c错误: 需要整数。§r\n去掉所有参数以获得帮助。\n(如需获取当前值请去掉最后一个参数)" : "§c錯誤: 需要整數。§r\n去掉所有引數以獲得幫助。\n(如需獲取當前值請去掉最後一個引數)");
                    } else sender.sendMessage("§a" + ((isCN) ? "当前 连续投掷间隔 为" : "當前 連續投擲間隔 為") + ": §b" + plGetScore("pcub_drop_interval", targetName) + " 刻");
                }
            }.runTaskAsynchronously(myPlugin);
            else if (args.length >= 1 && args[0].equalsIgnoreCase("fastSkill")) new BukkitRunnable(){
                //按住潜行发动技能
                @Override
                public void run(){
                    if (plGetScore("job", targetName) == 0) {
                        String[] setResult = (isCN) ? new String[]{"始终禁用", "始终启用", "仅基岩版启用", "仅 Android/iOS/WP 启用"} : new String[]{"始終禁用", "始終啟用", "僅 Bedrock 版啟用", "僅 Android/iOS/WP 啟用"};
                        if (args.length >= 2) {
                            int toInt;
                            if (args[1].equalsIgnoreCase("mobileOnly")) toInt = 3;
                            else if (args[1].equalsIgnoreCase("bedrockOnly")) toInt = 2;
                            else if (args[1].equalsIgnoreCase("enable")) toInt = 1;
                            else if (args[1].equalsIgnoreCase("disable")) toInt = 0;
                            else toInt = -1;
                            if (toInt >= 0) {
                                plSetScore("pcub_enable_fastskill", targetName, toInt);
                                sender.sendMessage("§a" + ((isCN) ? "按住潜行发动技能 已设置为" : "按住潛行發動技能 已設定為") + ": §b" + setResult[toInt]);
                            } else sender.sendMessage((isCN) ? "§c错误: 不正确的参数。§r\n去掉所有参数以获得帮助。\n(如需获取当前值请去掉最后一个参数)" : "§c錯誤: 不正確的引數。§r\n去掉所有引數以獲得幫助。\n(如需獲取當前值請去掉最後一個引數)");
                        } else sender.sendMessage("§a" + ((isCN) ? "当前 按住潜行发动技能 为" : "當前 按住潛行發動技能 為") + ": §b" + setResult[plGetScore("pcub_enable_fastskill", targetName)]);
                    } else sender.sendMessage((isCN) ? "§c错误: 本功能仅限战士使用。" : "§c錯誤: 本功能僅限戰士使用。");
                }
            }.runTaskAsynchronously(myPlugin);
            else if (args.length >= 1 && args[0].equalsIgnoreCase("fastSkillDuration")) new BukkitRunnable(){
                //发动技能所需时长
                @Override
                public void run(){
                    if (plGetScore("job", targetName) == 0) {
                        if (args.length >= 2) {
                            if (args[1].matches("[0-9]*")) {
                                int toInt = Integer.parseInt(args[1]);
                                if (toInt <= 20) {
                                    plSetScore("pcub_fastskill_duration", targetName, toInt);
                                    sender.sendMessage("§a" + ((isCN) ? "潜行技能所需时长 已设置为" : "潛行技能所需時長 已設定為") + ": §b" + args[1] + " 刻");
                                } else sender.sendMessage((isCN) ? "§c错误: 此值不能超过 20。" : "§c錯誤: 此值不能超過 20。");
                            } else sender.sendMessage((isCN) ? "§c错误: 需要整数。§r\n去掉所有参数以获得帮助。\n(如需获取当前值请去掉最后一个参数)" : "§c錯誤: 需要整數。§r\n去掉所有引數以獲得幫助。\n(如需獲取當前值請去掉最後一個引數)");
                        } else sender.sendMessage("§a" + ((isCN) ? "当前 潜行技能所需时长 为" : "當前 潛行技能所需時長 為") + ": §b" + plGetScore("pcub_fastskill_duration", targetName) + " 刻");
                    } else sender.sendMessage((isCN) ? "§c错误: 本功能仅限战士使用。" : "§c錯誤: 本功能僅限戰士使用。");
                }
            }.runTaskAsynchronously(myPlugin);
            else if (args.length >= 1 && args[0].equalsIgnoreCase("option")) {
                //基岩Forms菜单
                if (!fgVaild && !geyserVaild) {
                    sender.sendMessage("§c" + ((isCN) ? "至少需要安装 Floodgate 和 Geyser-Spigot 插件中的任意一个，才能使用此功能。" : "至少需要安裝 Floodgate 和 Geyser-Spigot 插件中的任意一個，才能使用此功能。"));
                    return true;
                } else if (!isFloodgate && !isGeyser) {
                    sender.sendMessage("§c" + ((isCN) ? "该命令只能由基岩版的玩家执行！" : "這個指令只能由基岩版玩家執行！"));
                    return true;
                } else if (args.length < 2) return true;
                //基岩版设置界面
                if (args[1].equalsIgnoreCase("combat")) new BukkitRunnable() {
                    @Override
                    public void run(){
                        int job = plGetScore("job", targetName);
                        CustomForm.Builder optForm = CustomForm.builder()
                                .title("pcub.title.combat_option")
                                .label("pcub.content.combat_option");
                        //武器激活位置
                        if (job != 2) {
                            strIs = 0;
                            int currentSlot = plGetScore("WeaponSlot", targetName) - 1;
                            if (currentSlot == -2) currentSlot = 9;
                            String name = "武器激活位置";
                            String[] s = {" 主手", "", "一", "二", "三", "四", "五", "六", "七", "八", "九", ""};
                            s[1] = (isCN) ? "号位" :"號位";
                            if (job == 1 || currentSlot == 9) {
                                s[11] = (isCN) ? ((job == 0) ? " 副手 （战士不可使用）" : " 副手 （基岩版暂不支持， 炼丹炉除外）") : ((job == 0) ? " 副手 （戰士不可使用）" : "副手 （Bedrock 版暫不支援， 煉丹爐除外）");
                                optForm.stepSlider(name, currentSlot, mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s));
                            } else
                                optForm.stepSlider(name, currentSlot, mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s));
                        }
                        //连续投掷&每次投掷间隔
                        String dropTitle = (isCN) ? "退火符/击退符 连续投掷" : "退火符/擊退符 連續投擲";
                        if (job == 2) dropTitle = ((isCN) ? "丹药/" : "丹藥/") + dropTitle;
                        int currentDropInterval = plGetScore("pcub_drop_interval", targetName);
                        int currentEnCont = plGetScore("pcub_enable_continuous", targetName);
                        optForm
                            .dropdown(dropTitle + ((isCN) ? " （可能受网络延迟影响）" : " （可能受網路延遲影響）"), currentEnCont, "pcub.combat_option.disable", "pcub.combat_option.enable", "pcub.combat_option.disable_bedrock", "pcub.combat_option.disable_mobile")
                            .slider(dropTitle + ((isCN) ? "间隔 （秒）" : "間隔 （秒）"), (float) 0, (float) 1, (float) 0.05, (float) currentDropInterval / 20);
                        int currentFastSkill,currentSkillDuration;
                        //战士专项
                        if (job == 0) {
                            //按住潜行发动技能&发动技能所需时长
                            currentFastSkill = plGetScore("pcub_enable_fastskill", targetName);
                            String skillTitle = (isCN) ? "按住潜行发动技能" : "按住潛行發動技能";
                            currentSkillDuration = plGetScore("pcub_fastskill_duration", targetName);
                            optForm
                                .dropdown(skillTitle, currentFastSkill, "pcub.combat_option.disable", "pcub.combat_option.enable", "pcub.combat_option.enable_bedrock", "pcub.combat_option.enable_mobile")
                                .slider(skillTitle + ((isCN) ? " 所需时长 （秒）" : " 所需時長 （秒）"), (float) 0, (float) 1, (float) 0.05, (float) currentSkillDuration / 20);
                        } else currentFastSkill = currentSkillDuration = -1;
                        optForm
                            .closedResultHandler(() -> new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        targetPlayer.performCommand("forms open menubook-be");
                                    }
                                }.runTask(myPlugin)
                            )
                            .validResultHandler(response -> new BukkitRunnable(){
                                @Override
                                public void run() {
                                    //武器激活位置
                                    if (job != 2) {
                                        int targetSlot = Integer.parseInt(response.next().toString()) + 1;
                                        if (targetSlot == 10) targetSlot = -1;
                                        if (plGetScore("WeaponSlot", targetName) != targetSlot) plSetScore("SlotSet", targetName, targetSlot);
                                    }
                                    //连续投掷
                                    int enCont = Integer.parseInt(response.next().toString());
                                    if (enCont != currentEnCont) {
                                        plSetScore("pcub_enable_continuous", targetName, enCont);
                                        String[] setResult = (isCN) ? new String[]{"始终禁用", "始终启用", "仅基岩版禁用", "仅 Android/iOS/WP 禁用"} : new String[]{"始終禁用", "始終啟用", "僅 Bedrock 版禁用", "僅 Android/iOS/WP 禁用"};
                                        sender.sendMessage("§a" + ((isCN) ? "连续投掷 已设置为" : "連續投擲 已設定為") + ": §b" + setResult[enCont]);
                                    }
                                    //每次投掷间隔
                                    String strDropInterval = response.next().toString();
                                    if (strDropInterval.length() > 4)
                                        strDropInterval = strDropInterval.substring(0, 4);
                                    int dropSpeed = (int) (Float.parseFloat(strDropInterval) * 20);
                                    if (dropSpeed != currentDropInterval) {
                                        plSetScore("pcub_drop_interval", targetName, dropSpeed);
                                        sender.sendMessage("§a" + ((isCN) ? "连续投掷间隔 已设置为" : "連續投擲間隔 已設定為") + ": §b" + strDropInterval + " 秒");
                                    }
                                    //战士专项
                                    if (job == 0) {
                                        //按住潜行发动技能
                                        int fastSkill = Integer.parseInt(response.next().toString());
                                        if (fastSkill != currentFastSkill) {
                                            plSetScore("pcub_enable_fastskill", targetName, fastSkill);
                                            String[] setResult = (isCN) ? new String[]{"始终禁用", "始终启用", "仅基岩版启用", "仅 Android/iOS/WP 启用"} : new String[]{"始終禁用", "始終啟用", "僅 Bedrock 版啟用", "僅 Android/iOS/WP 啟用"};
                                            sender.sendMessage("§a" + ((isCN) ? "按住潜行发动技能 已设置为" : "按住潛行發動技能 已設定為") + ": §b" + setResult[fastSkill]);
                                        }
                                        //发动技能所需时长
                                        String strSkillDuration = response.next().toString();
                                        if (strSkillDuration.length() > 4)
                                            strSkillDuration = strSkillDuration.substring(0, 4);
                                        int skillDuration = (int) (Float.parseFloat(strSkillDuration) * 20);
                                        if (skillDuration != currentSkillDuration) {
                                            plSetScore("pcub_fastskill_duration", targetName, skillDuration);
                                            sender.sendMessage("§a" + ((isCN) ? "潜行技能所需时长 已设置为" : "潛行技能所需時長 已設定為") + ": §b" + strSkillDuration + " 秒");
                                        }
                                    }
                                }
                            }.runTaskAsynchronously(myPlugin));
                        if(fgVaild) fgInstance.getPlayer(targetIDN).sendForm(optForm);
                        else if(geyserVaild) GeyserApi.api().sendForm(targetIDN, optForm);
                    }
                }.runTaskAsynchronously(myPlugin);
            }
            else {
                String msg = "§6§l" + ((isCN) ? "盘灵无界功能专用命令" : "盤靈無界功能專用命令") + "§r\n所有用法：";
                if (args.length >= 1) msg = ((isCN) ? "§c错误: 不正确的参数" : "§c錯誤: 不正確的引數") + "。§r\n" + msg;
                String drop = (isCN) ? "丹药/退火符/击退符 " : "丹藥/退火符/擊退符 ";
                String tick = (isCN) ? " (单位为刻，20刻=1秒)" : " (單位為刻，20刻=1秒)";
                String fastSkill = (isCN) ? "[战] 按住潜行发动技能" : "[戰] 按住潛行發動技能";
                sender.sendMessage(msg + "\n/pcub dropContinuous §7<§rdisable§7|§renable§7|§rbedrockOnly§7|§rmobileOnly§7> — " + drop + ((isCN) ? "连续投掷" : "連續投擲") + "§r\n/pcub dropInterval §7<§r0§7-§r20§7> — " + drop + ((isCN) ? "连续投掷间隔" : "連續投擲間隔") + tick + "§r\n/pcub fastSkill §7<§rdisable§7|§renable§7|§rbedrockOnly§7|§rmobileOnly§7> — " + fastSkill + "§r\n/pcub fastSkillDuration §7<§r0§7-§r20§7> — " + fastSkill + ((isCN) ? " 所需时长" : " 所需時長") + tick + "§r\n/pcub stack§7 — " + ((isCN) ? "强制叠放背包中的所有物品，上限为 64" : "強制疊放揹包中的所有物品，上限為 64") + "§r\n(" + ((isCN) ? "所有命令都不区分大小写，留空最后一个参数可获取当前值，可以Tab补全)" : "所有命令都不區分大小寫，留空最後一個引數可獲取當前值，可以Tab補全)"));
            }
            return true;
        }

        //命令补全
        @Override
        public ArrayList<String> onTabComplete(CommandSender sender, Command command, String string, String[] args) {
            //Player targetPlayer = (Player) sender;
            //String targetName = targetPlayer.getName();
            ArrayList<String> content = new ArrayList<>();
            if (args.length == 1) {
                String[] enTab = {"stack", "dropContinuous", "dropInterval", "fastSkill", "fastSkillDuration"};
                for (String tab : enTab) if (tab.toLowerCase().startsWith(args[0].toLowerCase())) content.add(tab);
                return content;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("dropContinuous") || args[0].equalsIgnoreCase("fastSkill")) {
                    String[] enTab = {"disable", "enable", "bedrockOnly", "mobileOnly"};
                    for (String tab : enTab) if (tab.toLowerCase().startsWith(args[1].toLowerCase())) content.add(tab);
                    return content;
                }
                if (args[0].equalsIgnoreCase("dropInterval")) {
                    if (args[1].isEmpty()) for (int i = 4; i <= 20; i += 4) content.add(i + "");
                    else for (int i = 0; i <= 20; i ++) if ((i + "").startsWith(args[1])) content.add(i + " ");
                    return content;
                }
                if (args[0].equalsIgnoreCase("fastSkillDuration")) {
                    if (args[1].isEmpty()) for (int i = 0; i <= 20; i += 5) content.add(i + "");
                    else for (int i = 0; i <= 20; i ++) if ((i + "").startsWith(args[1])) content.add(i + " ");
                    return content;
                }
            }
            return null;
        }

        //提供武器激活位的文本名称
        int strIs;
        public String mst(String[] str){
            strIs ++;
            if (strIs == 10) return str[11];
            else return str[0] + str[strIs + 1] + str[1];
        }

        //

        //基岩版菜单书
        public boolean bedrockMenu(ItemStack item, Player player){
            boolean opened = false;
            String targetID = player.getUniqueId().toString();
            String targetName = player.getName();
            //获取自定义标签
            ItemMeta itemMeta = item.getItemMeta();
            String metaString = (itemMeta == null) ? "{}" : itemMeta.getAsString();
            String[] metaList = metaString.substring(1, metaString.length() - 1).split(",");
            for (String tag : metaList) {
                if (tag.equals("id:\"pcub:menubook\"")) {
                    //打开菜单书
                    plSetScore("pcub_open_bedrock_menu", targetName, 1);
                    opened = true;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!getOperationLimit("mb" + targetID, 1)) {
                                //隐藏物品》隐藏对话》菜单
                                //获取记分项
                                boolean hideItem = plGetScore("pcub_hide_item_enable", targetName) == 1;
                                boolean hideTalk = plGetScore("pcub_hide_talk_enable", targetName) == 1;
                                if (hideItem && hideTalk) player.performCommand("forms open hide-task-be");
                                else if (hideItem) player.performCommand("forms open hide-item-be");
                                else if (hideTalk) player.performCommand("forms open hide-talk-be");
                                else player.performCommand("forms open menubook-be");
                            }
                            setOperationLimit("mb" + targetID, 10L);
                        }
                    }.runTaskLater(myPlugin, 2L);
                    break;
                } else if (tag.startsWith("id:\"shortcut:")) {
                    //命令捷径
                    opened = true;
                    if (!getOperationLimit("mb" + targetID, 1)) player.performCommand(tag.substring(13, tag.length() - 1));
                    setOperationLimit("mb" + targetID, 10L);
                    break;
                }
            }
            return opened;
        }

        //取消潜行
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

        /*
        ==================================
        ==========物品强制叠放部分==========
        ==================================
        */

        //判断是否为强制叠放物品
        public boolean isForceStack(Material type) {
            if(type == null) return false;
            String typeStr = type.toString();
            return type == Material.SNOWBALL || typeStr.endsWith("POTION") || typeStr.endsWith("STEW");
        }

        //两组物品合并
        public int margeItem(ItemStack origin, ItemStack target, Material originType, String originMetaStr, Material targetType, String targetMetaStr) {
            if (origin == null || target == null) return 0;
            if (originType == null) originType = target.getType();
            if (originMetaStr == null) {
                ItemMeta meta = origin.getItemMeta();
                if (meta != null) originMetaStr = meta.getAsString();
                else originMetaStr = "{}";
            }
            if (targetType == null) targetType = target.getType();
            if (targetMetaStr == null) {
                ItemMeta meta = target.getItemMeta();
                if (meta != null) targetMetaStr = meta.getAsString();
                else targetMetaStr = "{}";
            }
            int originAmount = origin.getAmount(),targetAmount = target.getAmount();
            if (originAmount < 64 && targetAmount < 64 && originType == targetType && originMetaStr.equals(targetMetaStr)) {
                int margeAmount = originAmount + targetAmount;
                if (margeAmount > 64) {
                    origin.setAmount(margeAmount - 64);
                    target.setAmount(64);
                } else {
                    origin.setAmount(0);
                    target.setAmount(margeAmount);
                }
                return margeAmount;
            }
            return 0;
        }

        //交易物品归位
        public void afterTrade(ItemStack origin, Player player) {
            int air = -1;
            int originAmount = origin.getAmount();
            for (int i = 0; i < 36; i ++) {
                ItemStack target = player.getInventory().getItem(i);
                if (target == null) {
                    if (air == -1) air = i;
                } else {
                    Material targetType = target.getType();
                    ItemMeta originMeta = origin.getItemMeta();
                    ItemMeta targetMeta = target.getItemMeta();
                    int targetAmount = target.getAmount();
                    if (originMeta != null && targetMeta != null && origin.getType() == targetType && originMeta.getAsString().equals(targetMeta.getAsString())) {
                        int readyAmount = 64 - targetAmount;
                        if (readyAmount < originAmount) {
                            target.setAmount(64);
                            originAmount -= readyAmount;
                        } else {
                            target.setAmount(targetAmount + originAmount);
                            originAmount = 0;
                            break;
                        }
                    }
                }
            }
            if (air != -1 && originAmount > 0) {
                origin.setAmount(originAmount);
                player.getInventory().setItem(air, origin);
                originAmount = 0;
            }
            origin.setAmount(originAmount);
            getLogger().info(player.getName() + "：已强制交易归位。");
        }

        //交易丹药模型数据转对应数量
        public boolean potionModelToStack(ItemStack origin, Material originType, ItemMeta originMeta){
            if (originMeta != null && originMeta.hasCustomModelData()) {
                int originMD = originMeta.getCustomModelData();
                int amountIndex = (originMD > 10) ? originMD % 10 : 0;
                if (amountIndex > 0) {
                    int[] targetAmount = (originType == Material.POTION) ? new int[]{2,3,4,5,8,10,12,15} : new int[]{5,8,10,16,24,32,48,64};
                    origin.setAmount(targetAmount[amountIndex - 1] * origin.getAmount());
                    originMeta.setCustomModelData(originMD - amountIndex);
                    origin.setItemMeta(originMeta);
                    //getLogger().info("已根据模型数据设置叠放数量：" + originMD + " → " + (originMD - amountIndex) + "x" + targetAmount[amountIndex - 1]);
                    return true;
                }
            }
            return false;
        }

        //强制叠放物品
        public int forceStack(Player player, int amount){
            ArrayList<String> metaStrList = new ArrayList<>();
            ArrayList<Material> typeList = new ArrayList<>();
            ArrayList<ArrayList<ItemStack>> itemList = new ArrayList<>();
            //获取全背包物品分类
            for (int i = 0; i < 36; i ++) {
                ItemStack item = player.getInventory().getItem(i);
                if(item != null) {
                    int notFound = 0;
                    Material currentType = item.getType();
                    String currentMeta = "";
                    if (item.getItemMeta() != null) currentMeta = item.getItemMeta().getAsString();
                    for (int m = 0; m < metaStrList.size(); m ++) {
                        if(typeList.get(m) == currentType && metaStrList.get(m).equals(currentMeta)) {
                            itemList.get(m).add(item);
                            break;
                        } else notFound ++;
                    }
                    if (notFound == metaStrList.size()) {
                        metaStrList.add(currentMeta);
                        typeList.add(currentType);
                        itemList.add(new ArrayList<>());
                        itemList.get(itemList.size() - 1).add(item);
                    }
                }
            }
            int stackedAmount = 0;
            //开始整理
            for (ArrayList<ItemStack> itemSubList : itemList) {
                int slotAmount = itemSubList.size();
                int itemAmount = 0;
                if (!(slotAmount == 1 && itemSubList.get(0).getAmount() <= amount)) {
                    stackedAmount ++;
                    for (ItemStack itemStack : itemSubList) itemAmount += itemStack.getAmount();
                    //sender.sendMessage(itemSubList.get(0).getType() + " 不足64个的多出部分：" + (itemAmount % 64) + " 个");
                    int remainderAmount = itemAmount % amount;
                    int fullSlotAmount = (itemAmount - remainderAmount) / amount;
                    if (
                        remainderAmount > 0 && slotAmount > fullSlotAmount ||
                        remainderAmount == 0 && slotAmount >= fullSlotAmount
                    ) for (int a = 0; a < slotAmount; a++) {
                        if (a < fullSlotAmount) itemSubList.get(a).setAmount(amount);
                        else if (remainderAmount > 0) {
                            itemSubList.get(a).setAmount(remainderAmount);
                            remainderAmount = 0;
                        } else itemSubList.get(a).setAmount(0);
                    }
                }
            }
            return stackedAmount;
        }
    }
}

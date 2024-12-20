package org.pcub.extension;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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

    //调试日志输出开关
    boolean showLog = false;
    public void plLogger(String info) {
        int worldTime = (int) Bukkit.getWorlds().get(0).getFullTime() % 1000;
        console.sendMessage("PCUB调试" + new char[]{'/','\\'}[worldTime % 2] + worldTime + " " + info);
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
            if(target == null || target.getItemMeta() == null) return false;
            String itemMeta = target.getItemMeta().getAsString();
            //检查是否有按钮标签
            return itemMeta.substring(1, itemMeta.length() - 1).contains("clickable:1");
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

        //获取物品栏操作类型
        public boolean isPickup(InventoryAction a) {
            return
                a == InventoryAction.PICKUP_ALL ||
                a == InventoryAction.PICKUP_SOME ||
                a == InventoryAction.PICKUP_HALF ||
                a == InventoryAction.PICKUP_ONE;
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
                    isFloodgate && Arrays.asList(
                        DeviceOs.GOOGLE,
                        DeviceOs.IOS,
                        DeviceOs.WINDOWS_PHONE
                    ).contains(fgPlayer.getDeviceOs()) ||
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
                    }
                }.runTaskLater(myPlugin, 0L);
            }
        }

        //容器打开事件
        @EventHandler
        public void onInventoryOpen(InventoryOpenEvent event) {
            Player targetPlayer = (Player) event.getPlayer();
            String targetName = targetPlayer.getName();
            String targetID = targetPlayer.getUniqueId().toString();
            if (plGetTempScore("inventory_opened", targetID) == 2) {
                plConsoleExec("execute as " + targetID + " run function pld:system/chest_menu/open");
                if (showLog) plLogger(targetName + " 已打开末影箱");
            }
        }

        //快速移动变量
        Player moveUser = null;
        ItemStack moveFromCopy = null;
        Inventory moveFromInv = null;
        InventoryType moveFromInvType = null;
        int moveFromSlot = 0;

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
            String currentMetaStr = "{}",cursorMetaStr = "{}";
            //获取槽位物品
            ItemStack currentItem = event.getCurrentItem();
            if(currentItem != null) {
                currentType = currentItem.getType();
                currentMeta = currentItem.getItemMeta();
                if(currentMeta != null) currentMetaStr = currentMeta.getAsString();
            }
            //获取指针物品
            ItemStack cursorItem = event.getCursor();
            if(cursorItem != null) {
                cursorType = cursorItem.getType();
                cursorMeta = cursorItem.getItemMeta();
                if(cursorMeta != null) cursorMetaStr = cursorMeta.getAsString();
            }

            //调试信息
            if (showLog) plLogger(targetName + " " + currentClick + " " + currentInvType + " " + currentSlot + " " + currentAction + " 指针:" + cursorType + " 槽位:" + currentType);

            //将丹药的模型叠放转换为真实叠放
            if (
                (
                    cursorType == Material.POTION ||
                    cursorType == Material.SPLASH_POTION
                ) &&
                currentInvType == InventoryType.PLAYER &&
                potionModelToStack(cursorItem, cursorType, cursorMeta)
            ) {
                if (showLog) plLogger(targetName + " 伪叠放丹药转换");
                cursorMeta = cursorItem.getItemMeta();
                cursorMetaStr = cursorMeta.getAsString();
            }

            //判断点击类型
            if (currentAction == InventoryAction.SWAP_WITH_CURSOR) {
                //强制合并（常规交换）
                int margeResult = margeItem(currentItem, cursorItem, currentType, currentMetaStr, cursorType, cursorMetaStr);
                if (margeResult != 0 && showLog)  plLogger(targetName + " 强制交换合并 x" + margeResult);
            } else if (
                Arrays.asList(
                    InventoryAction.HOTBAR_SWAP,
                    InventoryAction.HOTBAR_MOVE_AND_READD
                ).contains(currentAction)
            ) {
                //强制合并&刷新（HOTBAR_SWAP）
                int hotbarSlot = event.getHotbarButton();
                //目标槽位与指针槽位不同，且目标槽位不是副手
                if (currentSlot != hotbarSlot && hotbarSlot != -1) {
                    ItemStack targetItem = targetPlayer.getInventory().getItem(hotbarSlot);
                    int margeResult = margeItem(currentItem, targetItem, currentType, currentMetaStr, null, null);
                    if (margeResult != 0){
                        event.setCancelled(true);
                        if (showLog) plLogger(targetName + " 取消并强制快捷栏合并 x" + margeResult);
                    }
                    if (
                        targetItem != null && (
                            isForceStack(targetItem.getType()) ||
                            isForceStack(currentType)
                        )
                    ) new BukkitRunnable() {
                        @Override
                        public void run() {
                            targetPlayer.updateInventory();
                        }
                    }.runTaskLaterAsynchronously(myPlugin,0L);
                }
            } else if (isPickup(currentAction) && isForceStack(currentType)) {
                //当变量被其他玩家占用、或同一刻下再次拿起，则取消动作
                if (moveUser != null) {
                    event.setCancelled(true);
                    if (showLog) plLogger("取消 - 非标准叠放物同一刻再次拿起");
                }
                //除交易操作以外执行
                else if (currentInvType != InventoryType.MERCHANT || currentSlot != 2) {
                    //为处理移动/合并的同一刻内多次触发，在第一次触发时设置变量
                    moveUser = targetPlayer;
                    if (currentItem != null) moveFromCopy = new ItemStack(currentItem);
                    moveFromInv = currentInv;
                    moveFromInvType = currentInvType;
                    moveFromSlot = currentSlot;
                    //在2刻后清除变量，之后的触发视作下一刻
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            moveUser = null;
                            moveFromCopy = null;
                        }
                    }.runTaskLater(myPlugin, 2L);
                }
            }

            //叠放丹药交易修复
/*          以下是交易非标准叠放物时，不同客户端及用户行为在同一刻下所产生的不同动作，以及所采取的措施 (注：时间顺序从上往下)
            Java 单击
                LEFT MERCHANT 2 PICKUP_ALL 指针:AIR 槽位:POTION - 无措施
            Java Shift 键
                SHIFT_LEFT MERCHANT 2 MOVE_TO_OTHER_INVENTORY 指针:AIR 槽位:POTION - 取消
            Java F 键
                SWAP_OFFHAND MERCHANT 2 HOTBAR_SWAP 指针:AIR 槽位:POTION - 取消
            Java 1~9 数字键
                NUMBER_KEY MERCHANT 2 HOTBAR_SWAP 指针:AIR 槽位:POTION - 取消
            (在 Geyser 3be9b8a 后失效) 基岩键鼠单击
                LEFT MERCHANT 2 PICKUP_ALL 指针:AIR 槽位:POTION - 无措施
                RIGHT MERCHANT 2 PICKUP_ALL 指针:POTION 槽位:POTION - 取消并对交易所得物进行归位
                RIGHT MERCHANT 2 PICKUP_ALL 指针:AIR 槽位:POTION - 取消 (该动作可能会重复数次)
            基岩触屏/Shift 键 (会导致基岩不能 Shift 批量交易)
                RIGHT MERCHANT 2 PICKUP_HALF 指针:AIR 槽位:POTION - 无措施
                RIGHT PLAYER 0 PLACE_ONE 指针:POTION 槽位:AIR - 取消并对交易所得物进行归位
                LEFT MERCHANT 2 PICKUP_ALL 指针:AIR 槽位:POTION - 取消 (该动作可能会重复数次)
*/
            //在间隔2刻前，始终取消拿起或交换
            boolean notPickup = getOperationLimit("pu" + targetID, 1);
            if (notPickup) {
                if (
                    isPickup(currentAction) ||
                    currentAction == InventoryAction.SWAP_WITH_CURSOR
                ) {
                    event.setCancelled(true);
                    if (showLog) plLogger(targetName + " 取消 - 拿起和交换受限");
                }
                setOperationLimit("pu" + targetID, 2L);
            }

            boolean buttonClicked = plGetTempScore("clicked", targetID) == 1;
            if (currentInvType == InventoryType.MERCHANT && currentSlot == 2) {
                //交易目标拿起
                //JE Shift/F/1~9键 当丹药存在模型数据时取消动作
                if (
                    (
                        currentAction == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
                        currentAction == InventoryAction.HOTBAR_SWAP
                    ) && (
                        currentType == Material.POTION ||
                        currentType == Material.SPLASH_POTION
                    ) &&
                    currentMeta.hasCustomModelData()
                ) {
                    event.setCancelled(true);
                    if (showLog) plLogger(targetName + " 取消 - 伪叠放丹药Shift/F/1~9键交易");
                }
                //基岩 键鼠左键 (在 Geyser 3be9b8a 后失效) 同一刻内多次触发，且动作为右键拿起全部，则开始自动归位
                else if (
                    notPickup &&
                    currentAction == InventoryAction.PICKUP_ALL &&
                    currentClick == ClickType.RIGHT &&
                    cursorItem != null &&
                    cursorType != Material.AIR
                ) {
                    afterTrade(cursorItem, targetPlayer);
                    if (showLog) plLogger(targetName + " 强制交易归位 - 同一刻内多次触发且类型为右键拿起全部");
                }
                //第一次点击，则限制2刻内不能拿起
                else if (!notPickup) setOperationLimit("pu" + targetID, 2L);
            } else if (plGetScore("screen", targetName) >= 0) {
                //钱庄末影箱按钮点击
                boolean buttonOnCursor = isButton(cursorItem);
                boolean buttonOnCurrent = isButton(currentItem);
                //过滤操作
                if (
                    //玩家频繁操作
                    buttonOnCurrent && getOperationLimit("ep" + targetID, 5) ||
                    //箱子整理模组、GeyserMC 导致同一刻内的多次点击
                    buttonClicked ||
                    //将其他物品与按钮互换
                    cursorType != Material.AIR && !buttonOnCursor && buttonOnCurrent
                ) {
                    event.setCancelled(true);
                    if (showLog) plLogger(targetName + " 取消 - 频繁操作/同一刻内多次点击/将其他物品与按钮互换");
                }
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
                currentAction != InventoryAction.MOVE_TO_OTHER_INVENTORY &&
                currentAction != InventoryAction.HOTBAR_SWAP &&
                currentAction != InventoryAction.HOTBAR_MOVE_AND_READD &&
                !isPickup(currentAction) && (
                    isForceStack(cursorType) ||
                    isForceStack(currentType)
                ) &&
                //未触发末影箱按钮
                !buttonClicked
            ) {
                if (moveUser != null && moveUser != targetPlayer && !notPickup) {
                    event.setCancelled(true);
                    if (showLog) plLogger(targetName + " 取消 - 不同数据");
                }
                //基岩版移动/合并的同一刻内多次触发
                if (
                    moveUser == targetPlayer &&
                    (moveFromSlot != currentSlot || moveFromInvType != currentInvType)
                ) {
                    //原槽位物品
                    Material srcType = moveFromCopy.getType();
                    ItemMeta srcMeta = moveFromCopy.getItemMeta();
                    int srcAmount = moveFromCopy.getAmount();
                    String srcMetaStr = (srcMeta != null) ? srcMeta.getAsString() : "";
                    //双击/Shift快速移动
                    //目标槽位没有任何物品，则触发此项
                    if (
                        (
                            //当物品在原版堆叠范围内则不使用
                            srcType == Material.SNOWBALL && srcAmount > 16 ||
                            srcType != Material.SNOWBALL && srcAmount > 1
                        ) &&
                        (currentType == Material.AIR || currentType == null)
                    ) {
                        event.setCancelled(true);
                        //是否为玩家背包快捷栏内的移动
                        boolean moveInPlayerInv = moveFromInvType == currentInvType && currentInvType == InventoryType.PLAYER;
                        boolean moveFromHotBar = moveInPlayerInv && moveFromSlot < 9;
                        boolean moveToHotBar = moveInPlayerInv && currentSlot < 9;
                        //从目标容器中获取同物品
                        ArrayList<ItemStack> itemList = new ArrayList<>();
                        for (int i = (moveFromHotBar) ? 9 : 0; i < ((moveToHotBar) ? 9 : currentInv.getSize()); i ++) {
                            ItemStack item = currentInv.getItem(i);
                            if (item == null) continue;
                            ItemMeta tempMeta = item.getItemMeta();
                            if (
                                (
                                    i != moveFromSlot ||
                                    moveFromInvType != currentInvType
                                ) &&
                                item.getType() == srcType && //相同物品ID
                                srcMetaStr.equals((tempMeta != null) ? tempMeta.getAsString() : "") //相同物品标签
                            ) {
                                itemList.add(item);
                            }
                        }
                        //将这些物品尽可能填满
                        for (ItemStack itemStack : itemList) {
                            //跳过已满的槽位
                            if (itemStack.getAmount() >= 64) continue;
                            //未满的槽位则利用原物品填满，直到其用尽
                            int targetAmount = itemStack.getAmount();
                            srcAmount -= 64 - targetAmount;
                            itemStack.setAmount(64 + Math.min(srcAmount, 0)); //如果原物品透支，则在填满的基础上扣回
                            //原物品耗尽或透支，则停止遍历
                            if (srcAmount <= 0) break;
                        }
                        //如果原物品还有剩，则将其放入目标槽位
                        if (srcAmount > 0) {
                            moveFromCopy.setAmount(srcAmount);
                            event.setCurrentItem(moveFromCopy);
                        }
                        //清除指针
                        if (cursorItem != null) cursorItem.setAmount(0);
                        //清除原槽位
                        ItemStack srcSlotItem = moveFromInv.getItem(moveFromSlot);
                        if (srcSlotItem != null) srcSlotItem.setAmount(0);
                        if (showLog) plLogger(targetName + " 取消并强制移动");
                        //限制2刻内不能拿起
                        setOperationLimit("pu" + targetID, 2L);
                    }
                    //双击合并
                    //目标已有物品与源物品相同，则触发此项
                    else if (currentType == srcType && srcMetaStr.equals(currentMetaStr)) {
                        event.setCancelled(true);
                        ArrayList<ItemStack> itemList = new ArrayList<>();
                        int itemOriginAmount = currentItem.getAmount() + ((cursorItem != null) ? cursorItem.getAmount() : 0);
                        int itemAmount = itemOriginAmount;
                        //从当前容器获取同类物品
                        for (int i = 0; i < currentInv.getSize(); i ++) {
                            ItemStack item = currentInv.getItem(i);
                            if (item == null) continue;
                            ItemMeta tempMeta = item.getItemMeta();
                            if (
                                i != currentSlot &&
                                item.getType() == srcType &&
                                srcMetaStr.equals((tempMeta != null) ? tempMeta.getAsString() : "")
                            ) {
                                itemList.add(item);
                                itemAmount += item.getAmount();
                            }
                        }
                        //如果当前容器不是玩家背包，则另外从背包获取一遍
                        if (currentInvType != InventoryType.PLAYER) {
                            Inventory playerInv = targetPlayer.getInventory();
                            for (int i = 0; i < 36; i ++) {
                                ItemStack item = playerInv.getItem(i);
                                if (item == null) continue;
                                ItemMeta tempMeta = item.getItemMeta();
                                if (
                                    item.getType() == srcType &&
                                    srcMetaStr.equals((tempMeta != null) ? tempMeta.getAsString() : "")
                                ) {
                                    itemList.add(item);
                                    itemAmount += item.getAmount();
                                }
                            }
                        }
                        //将双击处的物品设置成同类物品数量的总和，超出64的部分保持原样
                        if (itemAmount > 64) {
                            //补满目标槽位，需要在同类物品中扣除的数量
                            //指针物品+目标槽位物品数量>64，则会导致此值为负数
                            //通常基岩版不会在这种情况下触发这一操作，但不代表不会发生
                            int remainderAmount = 64 - itemOriginAmount;
                            //填满目标槽位
                            currentItem.setAmount(64);
                            //从其它槽位倒扣
                            for (ItemStack itemStack : itemList) {
                                if (itemStack.getAmount() < remainderAmount) {
                                    remainderAmount -= itemStack.getAmount();
                                    itemStack.setAmount(0);
                                } else {
                                    //遍历物品被扣除后的剩余数量
                                    //指针物品+目标槽位物品数量>64，则会导致其不减反增，甚至可能超过64
                                    int finalAmount = itemStack.getAmount() - remainderAmount;
                                    if(finalAmount > 64) { //如果遍历物品数量被反增超过了64
                                        itemStack.setAmount(64);
                                        remainderAmount = 64 - finalAmount;
                                    } else {
                                        itemStack.setAmount(finalAmount);
                                        remainderAmount = 0;
                                    }
                                    break;
                                }
                            }
                            //指针物品+目标槽位物品数量>64
                            if (remainderAmount < 0) {
                                //多出的部分放回原拿起槽位
                                moveFromCopy.setAmount(remainderAmount * -1);
                                moveFromInv.setItem(moveFromSlot,moveFromCopy);
                            }
                        } else {
                            currentItem.setAmount(itemAmount);
                            for (ItemStack itemStack : itemList) itemStack.setAmount(0);
                        }
                        //清空指针上的多余
                        if (cursorItem != null) cursorItem.setAmount(0);
                        if (showLog) plLogger(targetName + " 取消并强制双击合并");
                        //限制2刻内不能拿起
                        setOperationLimit("pu" + targetID, 2L);
                    }
                }
                //交易（触屏单击/Shift）同一刻内任意触发，且指针不为空，则开始自动归位
                else if (
                    notPickup &&
                    cursorItem != null &&
                    cursorType != Material.AIR
                ) {
                    afterTrade(cursorItem, targetPlayer);
                    event.setCancelled(true);
                    if (showLog) plLogger(targetName + " 取消并强制交易归位 - 同一刻内任意触发，且指针不为空");
                }
                //放置物品后强制刷新物品栏
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        targetPlayer.updateInventory();
                    }
                }.runTaskLaterAsynchronously(myPlugin,0L);
            }
        }

        //容器关闭事件
        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            Player targetPlayer = (Player) event.getPlayer();
            String targetName = targetPlayer.getName();
            if (plGetScore("screen", targetName) >= 0) {
                plConsoleExec("execute as " + targetName + " run function #pcub:chest_menu_leave");
            }
        }

        //连续投掷变量
        int dropLastAmount = -1;

        //玩家交互事件
        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event) {
            Player targetPlayer = event.getPlayer();
            String targetName = targetPlayer.getName();
            UUID targetIDN = targetPlayer.getUniqueId();
            String targetID = targetIDN.toString();
            boolean isGeyser = geyserVaild && GeyserApi.api().isBedrockPlayer(targetIDN);
            boolean isFloodgate = fgVaild && FloodgateApi.getInstance().isFloodgatePlayer(targetIDN);
            ItemStack usedItem = event.getItem();
            Action action = event.getAction();
            Block clickedBlock = event.getClickedBlock();
            //调试
            if (showLog) plLogger(targetName + " " + action + " " + ((usedItem != null) ? usedItem.getType() : "") + " " + ((clickedBlock != null) ? clickedBlock.getType() : ""));
            boolean blockFunction = false;
            //右键方块检测
            if (action == Action.RIGHT_CLICK_BLOCK) {
                Material targetMat = clickedBlock.getType();
                String targetStr = targetMat.toString();
                //取消冒险玩家的食用蛋糕、破坏花盆操作
                if ((targetStr.startsWith("POTTED_") || targetMat == Material.CAKE) && targetPlayer.getGameMode() == GameMode.ADVENTURE) event.setCancelled(true);
                //检查方块是否可操作
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
                    //开启钱庄箱子
                    if (targetMat == Material.ENDER_CHEST) {
                        plSetTempScore("inventory_opened", targetID, 2);
                        new BukkitRunnable() {
                            @Override
                            public void run(){
                                plSetTempScore("inventory_opened", targetID, 0);
                            }
                        }.runTaskLaterAsynchronously(myPlugin,0L);
                        if (showLog) plLogger(targetName + " 准备打开末影箱");
                    }
                }
            }
            if (action == Action.RIGHT_CLICK_AIR || !blockFunction && action == Action.RIGHT_CLICK_BLOCK) {
                ItemMeta usedMeta = null;
                if (usedItem != null) usedMeta = usedItem.getItemMeta();
                Material usedType = null;
                if (usedItem != null) usedType = usedItem.getType();
                //雪球、丹药投掷限制
                if (usedType == Material.SNOWBALL || usedType == Material.SPLASH_POTION) {
                    boolean needCancel = getOperationLimit("dp" + targetID, 1);
                    int dropAmount = usedItem.getAmount();
                    //当在限制范围，且相比同一刻上一次事件使用物品的叠放量不同（投出了）则取消
                    //这可能会使投掷限制在高延迟的创造模式下失效
                    if(needCancel && dropLastAmount != dropAmount) {
                        event.setCancelled(true);
                        if (showLog) plLogger(targetName + " 取消 - 投掷限制");
                    }
                    //更新当前使用物品叠放量
                    //避免基岩版投出时同一刻触发多次事件，导致真正投出的触发被取消
                    dropLastAmount = dropAmount;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            dropLastAmount = -1;
                        }
                    }.runTaskLaterAsynchronously(myPlugin, 0L);
                    //连续投掷
                    //如果每次触发都会设置限制，而不等待上一限制结束，就能达到禁用连续投掷的效果
                    int enableContinuous = plGetScore("pcub_enable_continuous", targetName),dropSpeed = 7;
                    if (
                        //始终禁用
                        enableContinuous == 0 ||
                        //仅基岩版禁用
                        enableContinuous == 2 && (isFloodgate || isGeyser) ||
                        //仅移动端禁用
                        enableContinuous == 3 && plGetTempScore("is_touch", targetID) == 1
                    ) needCancel = false;
                    //投掷速度单位为刻数/次
                    else dropSpeed = plGetScore("pcub_drop_interval", targetName);
                    //如果当前未限制，且投掷间隔大于0刻（MC原版投掷间隔约4刻），则设置限制
                    if (!needCancel && dropSpeed > 0) setOperationLimit("dp" + targetID, dropSpeed);
                }
                //基岩版功能
                if (isFloodgate || isGeyser) {
                    boolean openMenu = usedType == Material.BOOK && bedrockMenu(usedItem, targetPlayer);
                    //副手功能
                    if (
                        !openMenu &&
                        targetPlayer.getInventory().getItemInOffHand().getType() == Material.CARROT_ON_A_STICK &&
                        usedType != Material.CARROT_ON_A_STICK &&
                        usedType != Material.WARPED_FUNGUS_ON_A_STICK &&
                        usedType != Material.BOW &&
                        usedType != Material.CROSSBOW &&
                        !isForceStack(usedType) &&
                        plGetScore("pcub_player_interact", targetName) == 0
                    ) {
                        //发出执行请求
                        plSetScore("pcub_player_interact", targetName, 1);
                        //0.1秒CD
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                plSetScore("pcub_player_interact", targetName, 0);
                            }
                        }.runTaskLaterAsynchronously(myPlugin, 2L);
                        if (showLog) plLogger(targetName + " 通过计分板向数据包请求");
                    } else if (showLog) plLogger(targetName + ((openMenu) ? " 右键执行/" : " ") + "主副手物品不满足条件/请求频率过高");
                }
            }
        }


        @EventHandler
        public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
            Entity targetEntity = event.getRightClicked();
            Player targetPlayer = event.getPlayer();
            //村民被玩家点击后，在打开交易界面前先执行函数
            if (targetEntity.getType() == EntityType.VILLAGER) {
                //给玩家赋予 Tag 以便数据包函数检测到触发者
                targetPlayer.addScoreboardTag("interact_villager");
                //以村民身份执行函数
                plConsoleExec("execute as " + targetEntity.getUniqueId().toString() + " run function pcub:interact_villager");
                //如果玩家被赋予 Tag “ignoreTradeUI” 则将其删除并屏蔽交易界面
                for (String tag : targetPlayer.getScoreboardTags()) if (tag.equals("ignoreTradeUI")) {
                    targetPlayer.removeScoreboardTag("ignoreTradeUI");
                    event.setCancelled(true);
                    if (showLog) plLogger(targetPlayer.getName() + " 取消打开 " + targetEntity.getName() + " 的交易界面");
                    break;
                }
                //删除玩家 Tag
                targetPlayer.removeScoreboardTag("interact_villager");
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
                //控制台
                if (
                    args.length >= 2 &&
                    args[0].equalsIgnoreCase("showDebugLog")
                ) {
                    if (args[1].equalsIgnoreCase("true")) {
                        if (!showLog) {
                            showLog = true;
                            getLogger().info("已开启调试日志");
                        } else getLogger().info("调试日志已被开启");
                        return true;
                    } else if (args[1].equalsIgnoreCase("false")) {
                        if (showLog) {
                            showLog = false;
                            getLogger().info("已关闭调试日志");
                        } else getLogger().info("调试日志已被关闭");
                        return true;
                    }
                }
                getLogger().info("支持的命令：/pcub showDebugLog <true|false>");
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
            // pcub stack
            if (args.length >= 1 && args[0].equalsIgnoreCase("stack")) new BukkitRunnable(){
                //强制叠放物品
                @Override
                public void run(){
                    sender.sendMessage("§a" + ((isCN) ? "已强制叠放" : "已強制疊放") + " §b" + forceStack(targetPlayer, 64) + ((isCN) ? " 种" : " 種") + "物品");
                }
            }.runTaskAsynchronously(myPlugin);
            // pcub dropContinuous
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
            // pcub dropInterval
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
            // pcub fastSkill
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
            // pcub fastSkillDuration
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
            // pcub option
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
                            .slider(dropTitle + ((isCN) ? "间隔 （秒）" : "間隔 （秒）"), 0F, 1F, 0.05F, (float) currentDropInterval / 20);
                        int currentFastSkill,currentSkillDuration;
                        //战士专项
                        if (job == 0) {
                            //按住潜行发动技能&发动技能所需时长
                            currentFastSkill = plGetScore("pcub_enable_fastskill", targetName);
                            String skillTitle = (isCN) ? "按住潜行发动技能" : "按住潛行發動技能";
                            currentSkillDuration = plGetScore("pcub_fastskill_duration", targetName);
                            optForm
                                .dropdown(skillTitle, currentFastSkill, "pcub.combat_option.disable", "pcub.combat_option.enable", "pcub.combat_option.enable_bedrock", "pcub.combat_option.enable_mobile")
                                .slider(skillTitle + ((isCN) ? " 所需时长 （秒）" : " 所需時長 （秒）"), 0F, 1F, 0.05F, (float) currentSkillDuration / 20);
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
            if(!(sender instanceof Player)) {
                //控制台
                for (String tab : (
                    (args.length == 1) ? new String[]{"showDebugLog"} :
                    (args.length == 2) ? new String[]{"true", "false"} :
                    new String[]{}
                )) if (tab.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) content.add(tab);
                return content;
            } else if (args.length == 1) {
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
                    if (args[1].isEmpty()) for (int i = 0; i <= 20; i += 4) content.add(i + "");
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
        //键名定义
        private final NamespacedKey shortcutKey = new NamespacedKey(myPlugin, "run_command");
        private final NamespacedKey menuKey = new NamespacedKey(myPlugin, "menubook");
        public boolean bedrockMenu(ItemStack item, Player player){
            String targetID = player.getUniqueId().toString();
            String targetName = player.getName();
            //获取自定义标签
            ItemMeta itemMeta = item.getItemMeta();
            if(itemMeta == null) return false;
            PersistentDataContainer dataCont = itemMeta.getPersistentDataContainer();
            String shortcut = dataCont.get(shortcutKey, PersistentDataType.STRING);
            if (Boolean.TRUE.equals(dataCont.get(menuKey, PersistentDataType.BOOLEAN))) {
                //打开菜单书
                plSetScore("pcub_open_bedrock_menu", targetName, 1);
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
                return true;
            } if (shortcut != null) {
                //命令捷径
                if (!getOperationLimit("mb" + targetID, 1)) player.performCommand(shortcut);
                setOperationLimit("mb" + targetID, 10L);
                return true;
            }
            return false;
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
            return
                type == Material.SNOWBALL ||
                type == Material.POTION ||
                type == Material.SPLASH_POTION ||
                type == Material.LINGERING_POTION||
                type == Material.MUSHROOM_STEW ||
                type == Material.RABBIT_STEW ||
                type == Material.SUSPICIOUS_STEW;
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

package org.pcub.extension.feature;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.pcub.extension.Common;
import org.pcub.extension.Main;
import org.pcub.extension.common.OperationLimiter;

import java.util.ArrayList;
import java.util.List;

public class Stacker {
    // 物品通用静态数据
    public static class StackerCommon {
        public final ItemStack item;
        public boolean isNull;
        @Nullable
        public Material type;
        @Nullable
        public ItemMeta meta;
        public String metaStr;

        public void load(){
            type = item.getType();
            meta = item.getItemMeta();
            metaStr = (meta != null) ? meta.getAsString() : "{}";
        }

        public StackerCommon(ItemStack item) {
            this.item = item;
            isNull = (item == null);
            if (isNull) {
                type = null;
                meta = null;
                metaStr = "{}";
            } else load();
        }
    }




    //获取物品栏操作类型
    public final static List<InventoryAction> pickupActions = List.of(
            InventoryAction.PICKUP_ALL,
            InventoryAction.PICKUP_SOME,
            InventoryAction.PICKUP_HALF,
            InventoryAction.PICKUP_ONE
    );



    // 判断是否为强制叠放物品
    public static boolean isForceStack(Material type) {
        return type == Material.SNOWBALL ||
                type == Material.POTION ||
                type == Material.SPLASH_POTION ||
                type == Material.LINGERING_POTION||
                type == Material.MUSHROOM_STEW ||
                type == Material.RABBIT_STEW ||
                type == Material.SUSPICIOUS_STEW;
    }



    // 两组物品合并
    public static class MergeItem{
        private final StackerCommon origin;
        private final StackerCommon target;
        private int stackSize = 64;
        public static int work(ItemStack origin, ItemStack target, int stackSize){
            int originAmount = origin.getAmount(), targetAmount = target.getAmount();
            if (originAmount < stackSize && targetAmount < stackSize) {
                int margeAmount = originAmount + targetAmount;
                if (margeAmount > stackSize) {
                    origin.setAmount(margeAmount - stackSize);
                    target.setAmount(stackSize);
                } else {
                    origin.setAmount(0);
                    target.setAmount(margeAmount);
                }
                return margeAmount;
            }
            return 0;
        }
        public int checkWork() {
            if (origin.isNull || target.isNull) return -1;
            if (origin.type == target.type && origin.metaStr.equals(target.metaStr)) return work(origin.item, target.item, stackSize);
            return -1;
        }

        public MergeItem setStackSize(int stackSize) {
            this.stackSize = stackSize;
            return this;
        }

        public MergeItem(StackerCommon origin, StackerCommon target){
            this.origin = origin;
            this.target = target;
        }
    }



    // 交易物品归位
    public static void afterTrade(ItemStack origin, Player player) {
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



    // 交易丹药模型数据转对应数量
    public static boolean potionModelToStack(StackerCommon origin) {
        if (origin.meta != null && origin.meta.hasCustomModelData()) {
            int originMD = origin.meta.getCustomModelData();
            int amountIndex = (originMD > 10) ? originMD % 10 : 0;
            if (amountIndex > 0) {
                int[] targetAmount = (origin.type == Material.POTION) ? new int[]{2,3,4,5,8,10,12,15} : new int[]{5,8,10,16,24,32,48,64};
                origin.item.setAmount(targetAmount[amountIndex - 1] * origin.item.getAmount());
                origin.meta.setCustomModelData(originMD - amountIndex);
                origin.item.setItemMeta(origin.meta);
                //getLogger().info("已根据模型数据设置叠放数量：" + originMD + " → " + (originMD - amountIndex) + "x" + targetAmount[amountIndex - 1]);
                return true;
            }
        }
        return false;
    }



    // 强制叠放物品
    public static int forceStack(Player player, int amount){
        ArrayList<String> metaStrList = new ArrayList<>();
        ArrayList<Material> typeList = new ArrayList<>();
        ArrayList<ArrayList<ItemStack>> itemList = new ArrayList<>();
        // 获取全背包物品分类
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
        // 开始整理
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



    private final Common common;
    private final Main main;
    private final ChestMenu chestMenu;
    // 快速移动变量
    private Player moveUser = null;
    private ItemStack moveFromCopy = null;
    private Inventory moveFromInv = null;
    private InventoryType moveFromInvType = null;
    private int moveFromSlot = 0;
    private final OperationLimiter legacyPickupLimit;



    public void legacyStack(
            InventoryClickEvent event,
            Player targetPlayer,
            String targetName,
            String targetUUIDStr,
            InventoryAction currentAction,
            ClickType currentClick,
            Inventory currentInv,
            InventoryType currentInvType,
            int currentSlot,
            StackerCommon current,
            StackerCommon cursor
    ){
        // 将丹药的模型叠放转换为真实叠放
        if (
                (
                        cursor.type == Material.POTION ||
                        cursor.type == Material.SPLASH_POTION
                ) &&
                        currentInvType == InventoryType.PLAYER &&
                        Stacker.potionModelToStack(cursor)
        ) {
            if (common.debug) common.debugLogger(targetName + " 伪叠放丹药转换");
            // 刷新转换后的静态数据
            cursor.load();
        }

        //判断点击类型
        int margeResult = 0;
        if (currentAction == InventoryAction.SWAP_WITH_CURSOR) {
            //强制合并（常规交换）
            margeResult = new MergeItem(current, cursor).checkWork();
            if (margeResult > 0 && common.debug) common.debugLogger(targetName + " 强制交换合并 x" + margeResult);
        }
        // 快捷栏移动
        else if (List.of(InventoryAction.HOTBAR_SWAP, InventoryAction.HOTBAR_MOVE_AND_READD).contains(currentAction)) {
            //强制合并&刷新（HOTBAR_SWAP）
            int hotbarSlot = event.getHotbarButton();
            //目标槽位与指针槽位不同，且目标槽位不是副手
            if (currentSlot != hotbarSlot && hotbarSlot != -1) {
                StackerCommon target = new StackerCommon(targetPlayer.getInventory().getItem(hotbarSlot));
                if (!target.isNull) {
                    margeResult = new MergeItem(current, target).checkWork();
                    if (margeResult > 0) {
                        event.setCancelled(true);
                        if (common.debug) common.debugLogger(targetName + " 取消并强制快捷栏合并 x" + margeResult);
                    }
                    if (
                            isForceStack(target.type) ||
                            isForceStack(current.type)
                    ) new BukkitRunnable() {
                        @Override
                        public void run() {
                            targetPlayer.updateInventory();
                        }
                    }.runTaskLaterAsynchronously(main,0L);
                }
            }
        }
        // 拿起缓存记录
        else if (pickupActions.contains(currentAction) && isForceStack(current.type)) {
            //当变量被其他玩家占用、或同一刻下再次拿起，则取消动作
            if (moveUser != null) {
                event.setCancelled(true);
                if (common.debug) common.debugLogger("取消 - 非标准叠放物同一刻再次拿起");
            }
            //除交易操作以外执行
            else if (currentInvType != InventoryType.MERCHANT || currentSlot != 2) {
                //为处理移动/合并的同一刻内多次触发，在第一次触发时设置变量
                moveUser = targetPlayer;
                if (current.item != null) moveFromCopy = new ItemStack(current.item);
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
                }.runTaskLater(main, 2L);
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
        boolean notPickup = legacyPickupLimit.get(targetPlayer) > 0;
        if (notPickup) {
            if (
                    pickupActions.contains(currentAction) ||
                    currentAction == InventoryAction.SWAP_WITH_CURSOR
            ) {
                event.setCancelled(true);
                if (common.debug) common.debugLogger(targetName + " 取消 - 拿起和交换受限");
            }
            legacyPickupLimit.put(targetPlayer, 2L);
        }

        boolean buttonClicked = common.getTempScore("clicked", targetUUIDStr) == 1;
        // 交易目标拿起
        if (currentInvType == InventoryType.MERCHANT && currentSlot == 2) {
            //JE Shift/F/1~9键 当丹药存在模型数据时取消动作
            if (
                    (
                            currentAction == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
                            currentAction == InventoryAction.HOTBAR_SWAP
                    ) && (
                            current.type == Material.POTION ||
                            current.type == Material.SPLASH_POTION
                    ) &&
                            current.meta.hasCustomModelData()
            ) {
                event.setCancelled(true);
                if (common.debug) common.debugLogger(targetName + " 取消 - 伪叠放丹药Shift/F/1~9键交易");
            }
            //基岩 键鼠左键 (在 Geyser 3be9b8a 后失效) 同一刻内多次触发，且动作为右键拿起全部，则开始自动归位
            else if (
                    notPickup &&
                    currentAction == InventoryAction.PICKUP_ALL &&
                    currentClick == ClickType.RIGHT &&
                    !cursor.isNull &&
                    cursor.type != Material.AIR
            ) {
                afterTrade(cursor.item, targetPlayer);
                if (common.debug) common.debugLogger(targetName + " 强制交易归位 - 同一刻内多次触发且类型为右键拿起全部");
            }
            //第一次点击，则限制2刻内不能拿起
            else if (!notPickup) legacyPickupLimit.put(targetPlayer, 2L);
        }
        // 钱庄末影箱按钮点击
        else if (
                common.getScore("screen", targetName) >= 0 &&
                chestMenu.checkWork(cursor, current, targetPlayer).limit
        ) event.setCancelled(true);

        // 放置/交换非常规叠放物品
        if(
                currentClick != ClickType.CREATIVE &&
                currentAction != InventoryAction.NOTHING &&
                // 非Java版快速移动
                currentAction != InventoryAction.MOVE_TO_OTHER_INVENTORY &&
                currentAction != InventoryAction.HOTBAR_SWAP &&
                currentAction != InventoryAction.HOTBAR_MOVE_AND_READD &&
                // 叠放物品非拿起
                !pickupActions.contains(currentAction) && (
                    isForceStack(cursor.type) ||
                    isForceStack(current.type)
                ) &&
                // 未触发末影箱按钮
                !buttonClicked
        ) {
            if (moveUser != null && moveUser != targetPlayer && !notPickup) {
                event.setCancelled(true);
                if (common.debug) common.debugLogger(targetName + " 取消 - 不同数据");
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
                // 双击/Shift快速移动
                // 目标槽位没有任何物品，则触发此项
                if (
                        (
                                //当物品在原版堆叠范围内则不使用
                                srcType == Material.SNOWBALL && srcAmount > 16 ||
                                srcType != Material.SNOWBALL && srcAmount > 1
                        ) &&
                                (current.type == Material.AIR || current.type == null)
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
                    if (cursor.item != null) cursor.item.setAmount(0);
                    //清除原槽位
                    ItemStack srcSlotItem = moveFromInv.getItem(moveFromSlot);
                    if (srcSlotItem != null) srcSlotItem.setAmount(0);
                    if (common.debug) common.debugLogger(targetName + " 取消并强制移动");
                    //限制2刻内不能拿起
                    legacyPickupLimit.put(targetPlayer, 2L);
                }
                // 双击合并
                // 目标已有物品与源物品相同，且未触发合并两组，则触发此项
                else if (margeResult <= 0 && current.type == srcType && srcMetaStr.equals(current.metaStr)) {
                    event.setCancelled(true);
                    ArrayList<ItemStack> itemList = new ArrayList<>();
                    int itemOriginAmount = current.item.getAmount() + ((cursor.item != null) ? cursor.item.getAmount() : 0);
                    int itemAmount = itemOriginAmount;
                    // 从当前容器获取同类物品
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
                    // 如果当前容器不是玩家背包，则另外从背包获取一遍
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
                    // 将双击处的物品设置成同类物品数量的总和，超出64的部分保持原样
                    if (itemAmount > 64) {
                        //补满目标槽位，需要在同类物品中扣除的数量
                        //指针物品+目标槽位物品数量>64，则会导致此值为负数
                        //通常基岩版不会在这种情况下触发这一操作，但不代表不会发生
                        int remainderAmount = 64 - itemOriginAmount;
                        //填满目标槽位
                        current.item.setAmount(64);
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
                        current.item.setAmount(itemAmount);
                        for (ItemStack itemStack : itemList) itemStack.setAmount(0);
                    }
                    // 清空指针上的多余
                    if (cursor.item != null) cursor.item.setAmount(0);
                    if (common.debug) common.debugLogger(targetName + " 取消并强制双击合并");
                    // 限制2刻内不能拿起
                    legacyPickupLimit.put(targetPlayer, 2L);
                }
            }
            //交易（触屏单击/Shift）同一刻内任意触发，且指针不为空，则开始自动归位
            else if (
                    notPickup &&
                    // 指针上有物品
                    cursor.item != null &&
                    cursor.type != Material.AIR
            ) {
                afterTrade(cursor.item, targetPlayer);
                event.setCancelled(true);
                if (common.debug) common.debugLogger(targetName + " 取消并强制交易归位 - 同一刻内任意触发，且指针不为空");
            }
            //放置物品后强制刷新物品栏
            new BukkitRunnable() {
                @Override
                public void run() {
                    targetPlayer.updateInventory();
                }
            }.runTaskLaterAsynchronously(main,0L);
        }
    }



    public Stacker(Common common, ChestMenu chestMenu){
        this.common = common;
        this.main = common.main;
        this.chestMenu = chestMenu;
        this.legacyPickupLimit = new OperationLimiter(common.main);
    }
}
#储存容器 槽位[2]
#从玩家主手获取命令捷径，用于获取物品数量
item replace block 1 -1 2 container.0 from entity @s weapon.mainhand
#为原版书设置物品数量
data modify block 1 -1 2 Items[2].Count set from block 1 -1 2 Items[0].Count
#替换主手物品为原版书
item replace entity @s weapon.mainhand from block 1 -1 2 container.2
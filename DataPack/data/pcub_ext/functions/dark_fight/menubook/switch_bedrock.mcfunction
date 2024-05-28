#储存容器 槽位[2]
#从玩家主手获取原版书，用于获取物品数量并储存，以供从命令捷径切换回原版
item replace block 1 -1 2 container.2 from entity @s weapon.mainhand
#设置缓存槽位为命令捷径
item replace block 1 -1 2 container.0 with minecraft:book{id:"shortcut:dark_fight_menu",CustomModelData:1,display:{Name:'[{"text":"诡秘挑战说明/辅助工具","color":"gold","italic":false}]',Lore:['{"translate":"book.byAuthor","with":["§6天道"],"color":"gray","italic":false}','{"translate":"book.generation.0","color":"gray","italic":false}']}}
#为命令捷径设置物品数量
data modify block 1 -1 2 Items[0].Count set from block 1 -1 2 Items[2].Count
#替换主手物品为命令捷径
item replace entity @s weapon.mainhand from block 1 -1 2 container.0
#储存容器 槽位[1]
#从玩家主手获取原版书，用于获取物品数量并储存，以供从命令捷径切换回原版
item replace block 1 -1 2 container.1 from entity @s weapon.mainhand
#设置缓存槽位为命令捷径
item replace block 1 -1 2 container.0 with minecraft:book{id:"shortcut:element_menubook",CustomModelData:1,display:{Name:'{"text":"§6便捷元素银行"}',Lore:['{"translate":"book.byAuthor","with":["§6天道"],"color":"gray","italic":false}','{"translate":"book.generation.0","color":"gray","italic":false}']}}
#为命令捷径设置物品数量
data modify block 1 -1 2 Items[0].Count set from block 1 -1 2 Items[1].Count
#替换主手物品为命令捷径
item replace entity @s weapon.mainhand from block 1 -1 2 container.0
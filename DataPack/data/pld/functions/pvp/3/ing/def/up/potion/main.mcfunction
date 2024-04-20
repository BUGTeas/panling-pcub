#扣分
scoreboard players remove @s pvp_point 1
#标记
scoreboard players set @s pvp3_defpotion 1
#更新物品

give @s minecraft:potion{id:"panling:pvp3_def_pill",CustomPotionColor:8227,CustomModelData:8,HideFlags:63,CustomPotionEffects:[{Amplifier:4b,Duration:600,Id:22b},{Amplifier:0b,Duration:600,Id:10b}],display:{Name:'{"translate":"pl.pvp3.item.name.def_pill"}',Lore:['{"translate":"pl.pvp.item.limit"}','{"translate":"pl.pvp3.item.lore.def_pill1"}','{"translate":"pl.pvp3.item.lore.def_pill2"}']}}

#重置按钮
clear @s potion{id:"panling:pvp3_buy_def3_potion"}
item replace entity @s inventory.10 with potion{CustomPotionColor:16426,CustomModelData:9,id:"panling:pvp3_bought_def_potion",display:{Name:'{"translate":"pl.pvp.button.name.buy_def3_potion"}',Lore:['{"translate":"pl.pvp.button.lore.bought_def3_potion"}',]},HideFlags:63}

#信息
tellraw @s {"translate":"pl.info.item_enough_point"}

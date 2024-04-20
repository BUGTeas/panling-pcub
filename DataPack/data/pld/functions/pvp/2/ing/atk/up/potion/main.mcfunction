#扣分
scoreboard players remove @s pvp_point 1
#标记
scoreboard players set @s pvp2_atkpotion 1
#更新物品

give @s minecraft:potion{id:"panling:pvp2_atk_pill",CustomPotionColor:16426,CustomModelData:9,HideFlags:63,CustomPotionEffects:[{Amplifier:2b,Duration:200,Id:1b},{Amplifier:0b,Duration:200,Id:5b},{Amplifier:1b,Duration:200,Id:8b}],display:{Name:'{"translate":"pl.pvp2.item.name.atk_pill"}',Lore:['{"translate":"pl.pvp.item.limit"}','{"translate":"pl.pvp2.item.lore.atk_pill"}',]}}

#重置按钮
clear @s potion{id:"panling:pvp2_buy_atk_potion"}
item replace entity @s inventory.10 with potion{CustomPotionColor:16426,CustomModelData:9,id:"panling:pvp2_bought_atk_potion",display:{Name:'{"translate":"pl.pvp.button.name.buy_atk_potion"}',Lore:['{"translate":"pl.pvp.button.lore.bought_atk_potion"}',]},HideFlags:63}

#信息
tellraw @s {"translate":"pl.info.item_enough_point"}

#设置出生点
spawnpoint @s 1871 52 -1777
#重置背包
function pld:pvp/2/ing/atk/equipment_reset

#关闭原有属性
scoreboard players set @s raceeffect -1
effect clear @s
function pld:system/attribute/clear
#系统激活标记
scoreboard players set @s inpvp_area 1

#给予初始分数
scoreboard players set @s pvp_point 4

#开启购买资格
scoreboard players set @s inbuy_area 1

#进图赠送：冲锋丸
give @s minecraft:potion{id:"panling:pvp2_atk_pill",CustomPotionColor:16426,CustomModelData:9,HideFlags:63,CustomPotionEffects:[{Amplifier:2b,Duration:200,Id:1b},{Amplifier:0b,Duration:200,Id:5b},{Amplifier:1b,Duration:200,Id:8b}],display:{Name:'{"translate":"pl.pvp2.item.name.atk_pill"}',Lore:['{"translate":"pl.pvp.item.limit"}','{"translate":"pl.pvp2.item.lore.atk_pill"}',]}}


tp @s 1872 111 -1747 -180 0
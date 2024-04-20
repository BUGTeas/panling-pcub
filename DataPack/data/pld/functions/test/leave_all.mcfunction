#试炼冷却
#scoreboard players set @s test_cool 12000
#异步
scoreboard players set @s test_cool 3000

tellraw @s {"translate":"pl.info.test_cool"}
function pld:test/raceeffect_check
scoreboard players set @s feather_mainland 1
scoreboard players set @s test_effectlock 0
spawnpoint @s 205 54 -1771

clear @s minecraft:potion{id:"panling:killpotion"}
give @s minecraft:potion{id:"panling:killpotion",CustomPotionColor:4393481,CustomModelData:30,CustomPotionEffects:[{Id:7b,Amplifier:19b,Duration:20}],display:{Lore:['{"translate":"pl.item.lore.killpotiona"}','{"translate":"pl.item.lore.killpotionb"}'],Name:'{"translate":"pl.item.name.killpotion"}'}}


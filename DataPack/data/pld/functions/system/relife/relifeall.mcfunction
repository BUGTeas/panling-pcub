#鹤顶丹补充
clear @s potion{id:"panling:killpotion"}
give @s minecraft:potion{id:"panling:killpotion",CustomPotionColor:4393481,CustomModelData:30,CustomPotionEffects:[{Id:7b,Amplifier:19b,Duration:20}],display:{Lore:['{"translate":"pl.item.lore.killpotiona"}','{"translate":"pl.item.lore.killpotionb"}'],Name:'{"translate":"pl.item.name.killpotion"}'}}
#回血回饥饿
effect give @s minecraft:instant_health 1 19
effect give @s minecraft:saturation 1 127
tellraw @a[scores={job=0..}] {"text":"","extra":[{"selector":"@s","color":"green"},{"translate":"pl.info.relife","color":"green"}]}

scoreboard players set @s feather_mainland 1
#友好检定
scoreboard players set @s[scores={race=0,racefriend0=..0}] raceeffect 0
scoreboard players set @s[scores={race=1,racefriend1=..0}] raceeffect 0
scoreboard players set @s[scores={race=2,racefriend2=..0}] raceeffect 0
scoreboard players set @s[scores={race=3,racefriend3=..0}] raceeffect 0
scoreboard players set @s[scores={race=4,racefriend4=..0}] raceeffect 0

scoreboard players set @s[scores={race=0,racefriend0=1..}] raceeffect 1
scoreboard players set @s[scores={race=1,racefriend1=1..}] raceeffect 1
scoreboard players set @s[scores={race=2,racefriend2=1..}] raceeffect 1
scoreboard players set @s[scores={race=3,racefriend3=1..}] raceeffect 1
scoreboard players set @s[scores={race=4,racefriend4=1..}] raceeffect 1

tellraw @s[scores={raceeffect=0}] {"translate":"pl.info.relifefriendless","color":"gray"}

effect give @s[scores={race=0,raceeffect=1}] health_boost 1000000 2 false
effect give @s[scores={race=1,raceeffect=1}] speed 20 0 false
effect give @s[scores={race=2,raceeffect=1}] jump_boost 20 1 false
effect give @s[scores={race=3,raceeffect=1}] fire_resistance 20 0 false
effect give @s[scores={race=4,raceeffect=1}] resistance 20 0 false

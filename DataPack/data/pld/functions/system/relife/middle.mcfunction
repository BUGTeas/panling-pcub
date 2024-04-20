#中间-惩罚
#鹤顶丹补充
clear @s potion{id:"panling:killpotion"}
give @s minecraft:potion{id:"panling:killpotion",CustomPotionColor:4393481,CustomModelData:30,CustomPotionEffects:[{Id:7b,Amplifier:19b,Duration:20}],display:{Lore:['{"translate":"pl.item.lore.killpotiona"}','{"translate":"pl.item.lore.killpotionb"}'],Name:'{"translate":"pl.item.name.killpotion"}'}}
#等级惩罚
xp set @s 0 points
tellraw @s {"translate":"pl.info.relifemiddle","color":"gray"}

scoreboard players set @s feather_mainland 1
#传送
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
tp @s[scores={raceeffect=0}] 179 43 63
tp @s[scores={race=0,raceeffect=1}] 3208 73 381 90 0
tp @s[scores={race=1,raceeffect=1}] 2845 48 899 180 -20
tp @s[scores={race=2,raceeffect=1}] 3179 127 783 -90 0
tp @s[scores={race=3,raceeffect=1}] 3299 22 -138 90 0
tp @s[scores={race=4,raceeffect=1}] 1689 140 138 90 0
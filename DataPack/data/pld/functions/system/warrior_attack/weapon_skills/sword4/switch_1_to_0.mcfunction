
execute unless score @s info_pos_weapon_skill matches 1 run title @s actionbar {"translate":"pl.info.weapon_skill_sword4_0"}
execute if score @s info_pos_weapon_skill matches 1 run tellraw @s {"translate":"pl.info.weapon_skill_sword4_0"}
playsound minecraft:pl.zf.cooldown player @s ~ ~ ~
tag @s remove sword4_0
attribute @s generic.max_health modifier remove 11-5-5-5-5


scoreboard players set @s weapon_skill_sword4_switch 0

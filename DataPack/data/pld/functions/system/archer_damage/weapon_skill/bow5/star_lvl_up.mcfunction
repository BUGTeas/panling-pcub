#星等级提升
scoreboard players add @s[scores={weapon_skill_bow5_lvl=..6}] weapon_skill_bow5_lvl 1
execute as @s[scores={weapon_skill_bow5_lvl=..6}] run function pld:system/archer_damage/weapon_skill/bow5/cool


execute at @s run playsound minecraft:pl.zf.cooldown player @s ~ ~ ~
title @s actionbar [{"translate":"pl.info.weapon_skill_bow5_star_lvl_up.a"},{"score":{"name": "@s","objective": "weapon_skill_bow5_lvl"}},{"translate":"pl.info.weapon_skill_bow5_star_lvl_up.b"}]
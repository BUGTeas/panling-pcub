#触发了武器技

#粒子效果&文本提示&音效
#particle minecraft:wax_on ~ ~1 ~ 0.5 0.5 0.5 0 20 force @a
playsound minecraft:pl.zf.cooldown player @p[tag=shooter] ~ ~ ~
execute as @p[tag=shooter] unless score @s info_pos_weapon_skill matches 1 run title @s actionbar {"translate":"pl.info.weapon_skill_bow4"}
execute as @p[tag=shooter] if score @s info_pos_weapon_skill matches 1 run tellraw @s {"translate":"pl.info.weapon_skill_bow4"}


#武器技进入冷却
scoreboard players set @p[tag=shooter] weapon_skill_cool_5ticks 50

#应用冷却缩减
execute as @p[tag=shooter] run function pld:system/weapon_skill_cool/reduce_cool

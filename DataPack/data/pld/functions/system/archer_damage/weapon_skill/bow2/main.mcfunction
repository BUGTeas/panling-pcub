#为箭矢提供强度增幅
execute store result score #temp temp run data get entity @s damage 100
scoreboard players set #temp temp2 12
scoreboard players operation #temp temp *= #temp temp2
scoreboard players set #temp temp2 10
scoreboard players operation #temp temp /= #temp temp2
execute store result entity @s damage double 0.01 run scoreboard players get #temp temp

#粒子效果&文本提示&音效
#particle minecraft:wax_on ~ ~1 ~ 0.5 0.5 0.5 0 20 force @a
playsound minecraft:pl.zf.cooldown player @p[tag=shooter] ~ ~ ~
execute as @p[tag=shooter] unless score @s info_pos_weapon_skill matches 1 run title @s actionbar {"translate":"pl.info.weapon_skill_bow2"}
execute as @p[tag=shooter] if score @s info_pos_weapon_skill matches 1 run tellraw @s {"translate":"pl.info.weapon_skill_bow2"}


#武器技进入冷却
scoreboard players set @p[tag=shooter] weapon_skill_cool_5ticks 25

#应用冷却缩减
execute as @p[tag=shooter] run function pld:system/weapon_skill_cool/reduce_cool

scoreboard players set @p[tag=shooter] skill_success 1
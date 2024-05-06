#根据星数追加伤害
#语境
# @s 被命中目标
# @p[tag=hit_player] 攻击的玩家

#计算追加伤害 archer_damage 一位小数
scoreboard players operation #temp archer_damage = @p[tag=hit_player] archer_damage
scoreboard players operation #temp temp = #system weapon_skill_bow5_multiplier
#scoreboard players operation #temp temp *= @p[tag=hit_player] weapon_skill_bow5_lvl
scoreboard players operation #temp temp *= #temp archer_damage
scoreboard players set #temp temp2 10
scoreboard players operation #temp temp /= #temp temp2

#计算追加伤害 二位小数
scoreboard players operation #temp entity_hurt_temp = #temp temp

#追加伤害给予
tag @p[tag=hit_player] add if_death_count
function pld:system/warrior_attack/hurt/hurt_armor_enchant_effect
tag @p[tag=hit_player] remove if_death_count

#消耗一枚星
scoreboard players remove @p[tag=hit_player] weapon_skill_bow5_lvl 1
execute as @p[tag=hit_player,scores={weapon_skill_bow5_5ticks=-1}] run function pld:system/archer_damage/weapon_skill/bow5/cool

#粒子效果
execute at @s run particle end_rod ~ ~1 ~ 0.5 0.5 0.5 0.2 20

execute as @p[tag=hit_player] at @s run playsound minecraft:pl.zf.cooldown player @s ~ ~ ~
execute as @p[tag=hit_player] unless score @s info_pos_weapon_skill matches 1 run title @s actionbar [{"translate":"pl.info.weapon_skill_bow5"},{"score":{"name": "@p[tag=hit_player]","objective": "weapon_skill_bow5_lvl"}},{"translate":"pl.info.weapon_skill_bow5.b"}]
execute as @p[tag=hit_player] if score @s info_pos_weapon_skill matches 1 run tellraw @s [{"translate":"pl.info.weapon_skill_bow5"},{"score":{"name": "@p[tag=hit_player]","objective": "weapon_skill_bow5_lvl"}},{"translate":"pl.info.weapon_skill_bow5.b"}]

execute as @p[tag=hit_player] run function pld:equipment_lock/equipment/armor/set_bonus/when_weapon_skill_enable

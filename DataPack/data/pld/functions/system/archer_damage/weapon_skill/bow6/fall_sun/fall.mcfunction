#落日启动 当前指令坐标为落地点 @s是触发落日的marker @s projectile_owner_uuid0是玩家的uuid0
tag @s add the_sun_marker
#根据uuid找到玩家
execute as @a[scores={job=1}] if score @s player_uuid3 = @e[type=marker,tag=the_sun_marker,limit=1] projectile_owner_uuid3 run tag @s add sun_owner
tag @s remove the_sun_marker

#落日本体初始化
execute unless score @p[tag=sun_owner] weapon_skill_bow6_cool_5ticks matches 0.. run function pld:system/archer_damage/weapon_skill/bow6/fall_sun/sun_owner_when_fall
#execute unless score @p[tag=sun_owner] weapon_skill_bow6_cool_5ticks matches 0.. run say 114
tag @a[tag=sun_owner] remove sun_owner
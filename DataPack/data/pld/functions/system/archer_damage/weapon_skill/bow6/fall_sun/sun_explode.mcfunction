tag @s add the_sun
#根据uuid 锁定player
execute as @a[scores={job=1}] if score @s player_uuid3 = @e[type=armor_stand,tag=the_sun,limit=1] projectile_owner_uuid3 run tag @s add hit_player

#爆炸伤害 半径3以内 weapon_skill_bow6_sun_2damage

execute positioned ~-5 ~-2 ~-5 run tag @e[tag=monster,dx=9,dy=6,dz=9] add to_damage
execute as @e[tag=to_damage] run function pld:system/archer_damage/weapon_skill/bow6/fall_sun/sun_damage
tag @e[tag=to_damage] remove to_damage

tag @a[tag=hit_player] remove hit_player
tag @s remove the_sun

#爆炸效果
particle flame ~ ~1.5 ~ 0 0 0 0.05 10 force
particle minecraft:explosion ~ ~1.5 ~ 0.2 0.2 0.2 0 10 force

playsound entity.generic.explode neutral @a[distance=..20]

#清理自己
kill @s
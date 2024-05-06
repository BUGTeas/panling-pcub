#同步owneruuid
scoreboard players operation @e[type=armor_stand,tag=no_init,limit=1] projectile_owner_uuid3 = @s projectile_owner_uuid3

#移除marker

#同步伤害值 weapon_skill_bow6_sun_2damage
execute as @e[type=armor_stand,tag=no_init,limit=1] run function pld:system/archer_damage/weapon_skill/bow6/fall_sun/sun_damage_set

tag @e[type=armor_stand,tag=no_init] remove no_init

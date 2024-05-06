# @s 箭本身 
# @e[type=marker,tag=no_owner,limit=1]

#将箭的uuid[0] 写进marker的marker_owner_uuid0
execute store result score @s projectile_uuid0 store result score @e[type=marker,tag=no_owner,limit=1] marker_owner_uuid0 run data get entity @s UUID[0]
#将箭owneruuid 同步给marker
scoreboard players operation @e[type=marker,tag=no_owner,limit=1] projectile_owner_uuid3 = @s projectile_owner_uuid3

tag @e[type=marker,tag=no_owner,limit=1] remove no_owner
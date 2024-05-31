execute if score @s fcub_teleport_test matches 0 run tp @s 523 33 54
execute if score @s fcub_teleport_test matches 60 run tp @s -770 126 388
execute if score @s fcub_teleport_test matches 120 run tp @s 12 34 -907
execute if score @s fcub_teleport_test matches 180 run tp @s -198 65 -179
scoreboard players add @s fcub_teleport_test 1
execute if score @s fcub_teleport_test matches 240.. run scoreboard players set @s fcub_teleport_test 0
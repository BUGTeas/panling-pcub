#3 zhan
execute if score @s process_zhan_hide matches 6 unless entity @s[nbt={Inventory:[{tag:{id:"panling:zhan_hide_key"}}]}] run scoreboard players set @s pcub_hide_item 1
#1
execute if entity @s[x=3264,y=20,z=-148,distance=..5] run scoreboard players set @s pcub_hide_talk 1
#2
execute if entity @s[x=3259,y=115,z=375,distance=..5] run scoreboard players set @s pcub_hide_talk 2
#3
execute if entity @s[x=-678,y=139,z=354,distance=..5] run scoreboard players set @s pcub_hide_talk 3
#4
execute if entity @s[x=-678,y=139,z=346,distance=..5] run scoreboard players set @s pcub_hide_talk 4
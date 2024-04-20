#2 xian
execute if score @s process_xian_hide matches 2 unless entity @s[nbt={Inventory:[{tag:{id:"panling:xian_hide_item1"}}]}] run scoreboard players set @s pcub_hide_item 1
execute if score @s process_xian_hide matches 8 unless entity @s[nbt={Inventory:[{tag:{id:"panling:xian_hide_key"}}]}] run scoreboard players set @s pcub_hide_item 2
#1
execute if entity @s[x=3180,y=95,z=889,distance=..5] run scoreboard players set @s pcub_hide_talk 1
#2
execute if entity @s[x=3241,y=67,z=881,distance=..5] run scoreboard players set @s pcub_hide_talk 2
#3
execute if entity @s[x=3225,y=60,z=941,distance=..5] run scoreboard players set @s pcub_hide_talk 3
#4
execute if entity @s[x=3337,y=109,z=941,distance=..5] run scoreboard players set @s pcub_hide_talk 4
#5
execute if entity @s[x=-335,y=97,z=-446,distance=..5] run scoreboard players set @s pcub_hide_talk 5
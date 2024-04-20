#4 ren
execute if score @s process_ren_hide matches 6 unless entity @s[nbt={Inventory:[{tag:{id:"panling:ren_hide_key"}}]}] run scoreboard players set @s pcub_hide_item 1
#1
execute if entity @s[x=1713,y=49,z=159,distance=..5] run scoreboard players set @s pcub_hide_talk 1
#2
execute if entity @s[x=3337,y=109,z=941,distance=..5] run scoreboard players set @s pcub_hide_talk 2
#3
execute if entity @s[x=-119,y=46,z=139,distance=..5] run scoreboard players set @s pcub_hide_talk 3
#4
execute if entity @s[x=-112,y=33,z=133,distance=..5] run scoreboard players set @s pcub_hide_talk 4
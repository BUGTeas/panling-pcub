#0 shen
execute if score @s process_shen_hide matches 6 unless entity @s[nbt={Inventory:[{tag:{id:"panling:shen_hide_key"}}]}] run scoreboard players set @s pcub_hide_item 1
#1
execute if entity @s[x=3318,y=115,z=354,distance=..5] run scoreboard players set @s pcub_hide_talk 1
#2
execute if entity @s[x=2742,y=53,z=867,distance=..5] run scoreboard players set @s pcub_hide_talk 2
#3
execute if entity @s[x=121,y=49,z=805,distance=..5] run scoreboard players set @s pcub_hide_talk 3
#4
execute if entity @s[x=123,y=32,z=814,distance=..5] run scoreboard players set @s pcub_hide_talk 4
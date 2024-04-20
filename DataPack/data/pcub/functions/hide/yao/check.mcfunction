#1 yao
execute if score @s process_yao_hide matches 8 unless entity @s[nbt={Inventory:[{tag:{id:"panling:yao_hide_key"}}]}] run scoreboard players set @s pcub_hide_item 1
#1
execute if entity @s[x=2656,y=89,z=895,distance=..5] run scoreboard players set @s pcub_hide_talk 1
#2
execute if entity @s[x=253,y=49,z=26,distance=..5] run scoreboard players set @s pcub_hide_talk 2
#3
execute if entity @s[x=172,y=66,z=-158,distance=..5] run scoreboard players set @s pcub_hide_talk 3
#4
execute if entity @s[x=339,y=33,z=-716,distance=..5] run scoreboard players set @s pcub_hide_talk 4
#5
execute if entity @s[x=336,y=16,z=-716,distance=..5] run scoreboard players set @s pcub_hide_talk 5
#execute if entity @s[nbt={SelectedItem:{tag:{id:"pcub:menubook"}}}] run scoreboard players set @s pcub_open_bedrock_menu 1
#execute if entity @s[scores={pcub_showitem_work=1}] run function pcub:showitem
execute if entity @s[scores={pcub_showitem=1}] unless entity @s[nbt={SelectedItem:{tag:{id:"pcub:menubook"}}}] run function pcub:bedrock/showitem
#实验：漏斗投放后先走动后传送
#奈何桥
#execute if entity @s[x=208,y=51,z=-1768,dx=-6,dy=7,dz=-20,scores={pcub_relife=1..}] run function pld:system/relife/bedrock_check
#雨竹
#execute if entity @s[x=-319,y=114,z=-424,distance=..10,scores={pcub_te17_target=0..}] run function pld:system/te17/in_tp
#execute if entity @s[x=-319,y=114,z=-424,distance=..10,scores={pcub_te32_target=0..}] run function pld:system/te32/in_tp
#execute if entity @s[x=-319,y=114,z=-424,distance=..10,scores={pcub_truth_xian5_target=0..}] run function pld:system/truth/xian/5/in_tp
#人族支线
#execute if entity @s[x=805,y=25,z=-70,distance=..5,scores={pcub_exren_finish=0..}] run function pld:system/ex_ren/out_tp
#副手修复
#execute if score @s pcub_event_cool matches 1.. run scoreboard players remove @s pcub_event_cool 1
execute if score @s pcub_player_interact matches 1 run function pcub:bedrock/event/click_work
#菜单书隐藏任务
execute if score @s pcub_open_bedrock_menu matches 1 run function pcub:hide/check
execute as @a[scores={pcub_is_bedrock=1}] run function pcub:bedrock/tick
#钱庄箱修复（已插件化）
#错乱还原延迟
execute as @a if score @s pcub_inventory_fix_delay matches 0.. run scoreboard players remove @s pcub_inventory_fix_delay 1
execute as @a[scores={pcub_inventory_fix_delay=1}] run function pcub:stack_fix/main
#execute as @a[scores={pcub_inventory_fix_delay=0}] if score @s screen matches 0.. run function pcub:chest_menu_fix/work
#挡板还原延迟
#execute as @a if score @s pcub_inventory_fix_delay2 matches 0.. run scoreboard players remove @s pcub_inventory_fix_delay2 1
#execute as @a[scores={pcub_inventory_fix_delay2=0}] if score @s screen matches 2 run function pld:system/chest_menu/screen/click_events_left
#execute as @a[scores={pcub_inventory_fix_delay2=0}] if score @s screen matches 200..225 run function pld:system/chest_menu/screen/click_events_left
#缩小基岩版苹果贴图
#execute as @a[nbt={SelectedItem:{id:"minecraft:apple"}}] unless entity @s[nbt={SelectedItem:{tag:{display:{Name:"{\"translate\":\"item.minecraft.apple\"}"}}}}] run item modify entity @s weapon.mainhand pcub:apple
#缩小基岩版箭支贴图
#execute as @a[nbt={SelectedItem:{id:"minecraft:arrow"}}] unless entity @s[nbt={SelectedItem:{tag:{display:{Name:"{\"translate\":\"item.minecraft.arrow\"}"}}}}] run item modify entity @s weapon.mainhand pcub:arrow
#兑泽1
#execute if score #system pcub_instance5_spawn1 matches 1 run function pld:instances/instance5/swamp/fill_position1
#execute as @a[x=3092,y=129,z=-1799,dx=-10,dy=4,dz=10] run tellraw @s {"score":{"name": "#aiod_timer","objective": "aiod_timer"}}
#兑泽4
#execute if score #system pcub_instance5_spawn4 matches 1 run function pld:instances/instance5/swamp/fill_position4
#execute as @a[x=3150,y=129,z=-1821,dx=-10,dy=4,dz=10] run tellraw @s {"score":{"name": "#aiod_timer","objective": "aiod_timer"}}
#兑泽3
#execute if score #system pcub_instance5_spawn3 matches 1 run function pld:instances/instance5/swamp/fill_position3
#execute as @a[x=3178,y=129,z=-1839,dx=-10,dy=4,dz=10] run tellraw @s {"score":{"name": "#aiod_timer","objective": "aiod_timer"}}
#兑泽2
#execute if score #system pcub_instance5_spawn2 matches 1 run function pld:instances/instance5/swamp/fill_position2
#execute as @a[x=3151,y=129,z=-1862,dx=-10,dy=4,dz=10] run tellraw @s {"score":{"name": "#aiod_timer","objective": "aiod_timer"}}
#execute as @a run title @s actionbar [{"score":{"name": "@s","objective": "moving"}},{"text":" "},{"score":{"name": "@s","objective": "moving2"}},{"text":" "},{"score":{"name": "@s","objective": "moving3"}}]
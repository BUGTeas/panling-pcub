#execute if score #aiod_timer aiod_timer matches 0 as @a run scoreboard players reset @s fcub_element_store_enable
#元素银行
execute if score #aiod_timer aiod_timer matches 0 as @a unless score @s fcub_element_store_enable matches 1.. run function fcub:element/check
execute as @a[scores={fcub_teleport_test=0..}] run function fcub:teleport_test
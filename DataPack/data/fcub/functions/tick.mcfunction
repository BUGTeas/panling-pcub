#execute if score #aiod_timer aiod_timer matches 0 as @a run scoreboard players reset @s fcub_element_store_enable
execute as @a[scores={pcub_is_bedrock=1}] run function fcub:tick_bedrock
#元素银行
execute as @a[scores={pcub_is_bedrock=0}] if entity @s[nbt={Inventory:[{tag:{id:"fcub:element_menubook"}}]}] run function fcub:element/switch_java
execute if score #aiod_timer aiod_timer matches 0 as @a unless score @s fcub_element_store_enable matches 1.. run function fcub:element/check
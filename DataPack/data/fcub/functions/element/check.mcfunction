execute if entity @s[nbt={Inventory:[{id:"minecraft:emerald",Count:64b}]}] run scoreboard players set @s fcub_element_store_enable 1
execute if entity @s[nbt={Inventory:[{id:"minecraft:bone",Count:64b}]}] run scoreboard players set @s fcub_element_store_enable 1
execute if entity @s[nbt={Inventory:[{id:"minecraft:string",Count:64b}]}] run scoreboard players set @s fcub_element_store_enable 1
execute if entity @s[nbt={Inventory:[{id:"minecraft:blaze_rod",Count:64b}]}] run scoreboard players set @s fcub_element_store_enable 1
execute if entity @s[nbt={Inventory:[{id:"minecraft:magma_cream",Count:64b}]}] run scoreboard players set @s fcub_element_store_enable 1
execute if entity @s[scores={fcub_element_store_enable=1}] run function fcub:element/active
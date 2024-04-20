#清空storage
execute if score @s screen matches 0.. run data modify storage pld:system Temp set value {}
execute if score @s screen matches 0.. run data modify storage pld:system Temp_ender_data set value {}
execute if score @s screen matches 0.. run data modify storage pld:system Temp_chest set value []
#抛出玩家数据
execute if score @s screen matches 0.. run data modify storage pld:system Temp set from entity @s {}
#抛出储存数据到Temp_ender_data
execute if score @s screen matches 0.. run data modify storage pld:system Temp_ender_data set from storage pld:system Temp.EnderItems[{Slot:0b}].tag.data
#execute if score @s screen matches 0.. run tellraw yl_jiu_qiu {"nbt":"Temp.EnderItems[{Slot:0b}]","storage":"pld:system"}
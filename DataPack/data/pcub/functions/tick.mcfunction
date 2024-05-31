execute as @a[scores={pcub_is_bedrock=1}] run function #pcub:tick_bedrock
execute as @a[scores={pcub_is_bedrock=0}] run function #pcub:tick_java

#基岩版落日
#主手放置
execute as @e[type=minecraft:armor_stand,nbt={Tags:["fall_sun"]}] unless entity @s[nbt={Tags:["pcub_patched"]}] run item replace entity @s weapon.mainhand with minecraft:blue_ice{CustomModelData:1}
#设置数据
execute as @e[type=minecraft:armor_stand,nbt={Tags:["fall_sun"]}] unless entity @s[nbt={Tags:["pcub_patched"]}] run data modify entity @s Tags append value "pcub_patched"
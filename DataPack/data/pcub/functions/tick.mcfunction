execute as @a[tag=player_bedrock] run function #pcub:tick_bedrock
execute as @a[tag=player_java] run function #pcub:tick_java

#基岩版落日
execute as @e[type=minecraft:armor_stand,nbt={Tags:["fall_sun"]}] unless entity @s[nbt={Tags:["pcub_patched"]}] run function pcub:bedrock/fall_sun

#用于在基础必要组件加载后执行
function #pcub:tick
execute as @a[tag=player_bedrock] run function #pcub:player_tick/bedrock
execute as @a[tag=player_java] run function #pcub:player_tick/java

# 背包物品变动/主手槽位切换检测
execute as @a[tag=player_bedrock, tag=pcub_inventory_check] run function #pcub:inventory_check/bedrock
execute as @a[tag=player_java, tag=pcub_inventory_check] run function #pcub:inventory_check/java

# 向下兼容
function #pcub:tick
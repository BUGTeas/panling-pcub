# 检测玩家类型用
tag @s remove player_java
tag @s add player_bedrock
# 当玩家使用副手作为武器槽位时发出警告
tellraw @s[scores={WeaponSlot=-1}] [{"translate":"pcub.not_support.offhand"}]

# 向下兼容
function #pcub:join_bedrock
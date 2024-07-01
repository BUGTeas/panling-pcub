#检测玩家类型用
tag @s remove player_java
tag @s add player_bedrock
#插件事件重置
scoreboard players reset @s pcub_player_interact
#当玩家使用副手作为武器槽位时发出警告
tellraw @s[scores={WeaponSlot=-1}] [{"translate":"pcub.not_support.offhand"}]
#重置漏斗打开状态
scoreboard players reset @s pcub_hopper_opened
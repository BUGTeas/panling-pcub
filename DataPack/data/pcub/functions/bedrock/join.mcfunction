#检测玩家类型用
scoreboard players set @s pcub_is_bedrock 1
#插件事件重置
scoreboard players reset @s pcub_player_interact
tellraw @s[scores={WeaponSlot=-1}] [{"translate":"pcub.not_support.offhand"}]
#重置漏斗打开状态
scoreboard players reset @s pcub_hopper_opened
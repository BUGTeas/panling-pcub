#检测玩家类型用
scoreboard players set @s pcub_is_bedrock 1
#插件事件重置
scoreboard players reset @s pcub_player_interact
#进服提示附加
tellraw @s [{"translate":"plbe.notice.bug"}]
tellraw @s[scores={WeaponSlot=-1}] [{"translate":"plbe.notice.notsupport_offhand"}]
#服务器识别客户端不正确时，会弹出文字提示
title @s subtitle {"color":"#ffff00","translate":"please.download.and.add_it"}
title @s title {"color":"#ff5555","translate":"res.pack.040.requested"}
#重置漏斗打开状态
scoreboard players reset @s pcub_hopper_opened
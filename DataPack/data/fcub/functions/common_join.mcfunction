#execute if score @s fcub_final_state matches 1 run function pld:system/online/if_in_dungeon/leave
execute if score #system fcub_final_state matches 2 run kick @s[scores={fcub_final_state=3}] 您已参加过最终剧情，在本轮结束前将无法进入服务器。
#execute if score #system fcub_final_state matches 2 unless entity @s[scores={fcub_final_state=2..}] run kick @s 由于您错过了圣山之战，本轮真盘古您无法参加。
#欢迎提示
tellraw @s [{"color":"#ffaa00","bold":true,"text":"欢迎游玩幻域无界 · 盘灵古域 (梦回盘灵) 互通服务器！\n"},{"color":"#ffffff","bold":false,"text":"请关注官方群聊：490989498，了解服务器玩法和最新状况！"}]
gamemode survival @s[scores={fcub_gamemode=0}]
gamemode creative @s[scores={fcub_gamemode=1}]
gamemode adventure @s[scores={fcub_gamemode=2}]
gamemode spectator @s[scores={fcub_gamemode=3}]
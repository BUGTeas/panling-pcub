#对话标记初始化
scoreboard players set #system conversation_final_story 0
#下一句对话开始延迟
scoreboard players set #system tick_final_story 2

#因为激活了真盘 取消假盘的传送出本
schedule clear pld:instances/instance5/end

scoreboard players set #system fcub_final_state 2
kick @a[scores={fcub_final_state=3}] 您已参加过最终剧情，在本轮结束前将无法进入服务器。
execute as @a unless score @s fcub_final_state matches 3 run scoreboard players set @s fcub_final_state 2

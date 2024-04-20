#判断基岩玩家
scoreboard objectives add pcub_is_bedrock dummy
#基岩菜单书、成就
scoreboard objectives add pcub_open_bedrock_menu dummy
#基岩版主手物品展示优化
scoreboard objectives add pcub_showitem dummy
#scoreboard objectives add pcub_showitem_work trigger
#手持物品交互事件
scoreboard objectives add pcub_player_interact dummy
#插件事件冷却
#scoreboard objectives add pcub_event_cool dummy
scoreboard objectives remove pcub_event_cool
#容器交互事件
scoreboard objectives add pcub_inventory_fix_delay dummy
scoreboard objectives add pcub_inventory_fix_delay2 dummy

#投掷速度限制
scoreboard objectives add pcub_drop_interval dummy
#允许连续投掷
scoreboard objectives add pcub_enable_continuous dummy
#按住潜行发动技能
scoreboard objectives add pcub_enable_fastskill dummy
#发动技能所需时长
scoreboard objectives add pcub_fastskill_duration dummy


#强制玩家进服游戏模式
scoreboard objectives add fcub_gamemode dummy
#漏斗投放后先关闭再传送
scoreboard objectives add pcub_hopper_opened dummy
#兑泽
scoreboard objectives add pcub_instance5_spawn1 dummy
scoreboard objectives add pcub_instance5_spawn2 dummy
scoreboard objectives add pcub_instance5_spawn3 dummy
scoreboard objectives add pcub_instance5_spawn4 dummy
#奈何桥
#scoreboard objectives add pcub_relife dummy
#实验：漏斗投放后先走动后传送
#监测移动
scoreboard objectives add pcub_moving dummy
#奈何桥
#scoreboard objectives add pcub_relife_left dummy
scoreboard objectives remove pcub_relife_left
#scoreboard objectives add pcub_relife_right dummy
scoreboard objectives remove pcub_relife_right
#雨竹
#scoreboard objectives add pcub_te17_target dummy
#scoreboard objectives add pcub_te32_target dummy
#scoreboard objectives add pcub_truth_xian5_target dummy
#人族支线
#scoreboard objectives add pcub_exren_finish dummy

#隐藏任务
scoreboard objectives add pcub_hide_item dummy
scoreboard objectives add pcub_hide_talk dummy
scoreboard objectives add pcub_hide_item_enable dummy
scoreboard objectives add pcub_hide_talk_enable dummy
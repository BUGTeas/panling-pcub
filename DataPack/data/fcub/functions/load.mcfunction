#元素银行激活
scoreboard objectives add fcub_element_store_enable dummy
#反连点次数
#scoreboard objectives add fcub_auto_click_count dummy
scoreboard objectives remove fcub_auto_click_count
#隐藏提示
scoreboard objectives add fcub_hidden dummy
#禁用玩家碰撞
team modify normal collisionRule pushOwnTeam
team modify attack collisionRule pushOwnTeam
team modify defence collisionRule pushOwnTeam
#昆仑bgm时间倒数
scoreboard players set #5ticks_bgm_shen_all time_trigger 350
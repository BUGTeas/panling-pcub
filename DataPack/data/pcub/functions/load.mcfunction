#基岩菜单书、成就
scoreboard objectives add pcub_open_bedrock_menu dummy
#基岩版主手物品展示优化
scoreboard objectives add pcub_showitem dummy
#手持物品交互事件
scoreboard objectives add pcub_player_interact dummy

#投掷速度限制
scoreboard objectives add pcub_drop_interval dummy
#允许连续投掷
scoreboard objectives add pcub_enable_continuous dummy
#按住潜行发动技能
scoreboard objectives add pcub_enable_fastskill dummy
#发动技能所需时长
scoreboard objectives add pcub_fastskill_duration dummy

#API版本
scoreboard objectives add pcub_api_version dummy
scoreboard players set #system pcub_api_version 1

#（暂无作用）漏斗投放后先关闭再传送
scoreboard objectives add pcub_hopper_opened dummy

#隐藏任务
scoreboard objectives add pcub_hide_item dummy
scoreboard objectives add pcub_hide_talk dummy
scoreboard objectives add pcub_hide_item_enable dummy
scoreboard objectives add pcub_hide_talk_enable dummy

#用于在基础必要组件加载后执行
function #pcub:load
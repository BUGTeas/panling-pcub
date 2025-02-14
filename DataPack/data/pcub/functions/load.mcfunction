# 弃用的记分项
scoreboard objectives remove pcub_open_bedrock_menu
scoreboard objectives remove pcub_showitem
scoreboard objectives remove pcub_player_interact

# 投掷速度限制
scoreboard objectives add pcub_drop_interval dummy
# 允许连续投掷
scoreboard objectives add pcub_enable_continuous dummy
# 按住潜行发动技能
scoreboard objectives add pcub_enable_fastskill dummy
# 发动技能所需时长
scoreboard objectives add pcub_fastskill_duration dummy

# 当前API版本
scoreboard objectives add pcub_api_version dummy
scoreboard players set #system pcub_api_version 6

# 最低兼容API版本
scoreboard objectives add pcub_api_minVersion dummy
scoreboard players set #system pcub_api_minVersion 6

# 隐藏任务
scoreboard objectives add pcub_hide_item dummy
scoreboard objectives add pcub_hide_talk dummy
scoreboard objectives add pcub_hide_item_enable dummy
scoreboard objectives add pcub_hide_talk_enable dummy

# 修改部分交易项，以修复基岩版 1.20.30+ 的交易 Bug
scoreboard objectives add pcub_villagerFix_temp1 dummy
scoreboard objectives add pcub_villagerFix_temp2 dummy

# 头饰修复计数器
scoreboard objectives add pcub_honorHeadFix_count dummy
scoreboard objectives add pcub_honorHeadFix_countCMD dummy

# 用于在梦盘互通套件加载后执行
function #pcub:load

# 检测并禁用优化修改数据包，恢复相关 NPC
schedule function pcub:uninst_pcub_mod/check 1s
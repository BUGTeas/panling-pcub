# 用于在梦盘互通套件卸载前执行
function #pcub:uninstall

# 投掷速度限制
scoreboard objectives remove pcub_drop_interval
# 允许连续投掷
scoreboard objectives remove pcub_enable_continuous
# 按住潜行发动技能
scoreboard objectives remove pcub_enable_fastskill
# 发动技能所需时长
scoreboard objectives remove pcub_fastskill_duration

# 当前API版本
scoreboard objectives remove pcub_api_version

# 最低兼容API版本
scoreboard objectives remove pcub_api_minVersion

# 隐藏任务
scoreboard objectives remove pcub_hide_item
scoreboard objectives remove pcub_hide_talk
scoreboard objectives remove pcub_hide_item_enable
scoreboard objectives remove pcub_hide_talk_enable

# 修改部分交易项，以修复基岩版 1.20.30+ 的交易 Bug
scoreboard objectives remove pcub_villagerFix_temp1
scoreboard objectives remove pcub_villagerFix_temp2

# 头饰修复计数器
scoreboard objectives remove pcub_honorHeadFix_count
scoreboard objectives remove pcub_honorHeadFix_countCMD
#副手修复
execute if entity @s[tag=pcub_player_interact] run function pcub:bedrock/event/click_work

# 向下兼容
function #pcub:tick_bedrock
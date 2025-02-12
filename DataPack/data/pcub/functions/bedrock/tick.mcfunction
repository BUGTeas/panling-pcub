#副手修复
execute if score @s pcub_player_interact matches 1 run function pcub:bedrock/event/click_work

# 向下兼容
function #pcub:tick_bedrock
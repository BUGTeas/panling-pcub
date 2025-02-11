#展示主手物品
execute if score @s pcub_showitem matches 1 unless entity @s[nbt={SelectedItem:{tag:{id:"pcub:menubook"}}}] run function pcub:bedrock/showitem
#副手修复
execute if score @s pcub_player_interact matches 1 run function pcub:bedrock/event/click_work
#菜单书隐藏任务
execute if score @s pcub_open_bedrock_menu matches 1 run function pcub:hide/check

# 向下兼容
function #pcub:tick_bedrock
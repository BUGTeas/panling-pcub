#菜单书切换
execute if entity @s[nbt={Inventory:[{tag:{title:"§6菜单", author:"§6天道"}}]}] run function pcub:menubook/panling/switch_bedrock
#展示主手物品
execute if entity @s[scores={pcub_showitem=1}] unless entity @s[nbt={SelectedItem:{tag:{id:"pcub:menubook"}}}] run function pcub:bedrock/showitem
#副手修复
execute if score @s pcub_player_interact matches 1 run function pcub:bedrock/event/click_work
#菜单书隐藏任务
execute if score @s pcub_open_bedrock_menu matches 1 run function pcub:hide/check
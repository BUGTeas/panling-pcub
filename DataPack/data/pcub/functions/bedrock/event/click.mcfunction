#如果冷却完毕就执行
execute unless score @s pcub_event_cool matches 1.. run function pcub:bedrock/event/click_work
#开始冷却
scoreboard players set @s pcub_event_cool 20
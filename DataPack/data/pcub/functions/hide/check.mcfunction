scoreboard players reset @s pcub_hide_talk
scoreboard players reset @s pcub_hide_item
scoreboard players reset @s pcub_hide_talk_enable
scoreboard players reset @s pcub_hide_item_enable
execute if entity @s[scores={race=0}] run function pcub:hide/shen/check
execute if entity @s[scores={race=1}] run function pcub:hide/yao/check
execute if entity @s[scores={race=2}] run function pcub:hide/xian/check
execute if entity @s[scores={race=3}] run function pcub:hide/zhan/check
execute if entity @s[scores={race=4}] run function pcub:hide/ren/check
execute if score @s pcub_hide_talk matches 1.. run scoreboard players set @s pcub_hide_talk_enable 1
execute if score @s pcub_hide_item matches 1.. run scoreboard players set @s pcub_hide_item_enable 1
#tellraw @s [{"text":"隐藏任务检测中！ 对话范围："},{"score":{"name": "@s","objective": "pcub_hide_talk"}},{"text":" 物品领取："},{"score":{"name": "@s","objective": "pcub_hide_item"}}]
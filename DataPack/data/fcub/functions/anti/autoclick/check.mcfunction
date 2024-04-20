scoreboard players add @s fcub_auto_click_count 1
execute unless score @s fcub_auto_click_count matches 2.. run title @s subtitle {"text":"下一次将受到惩罚并广而告之"}
execute unless score @s fcub_auto_click_count matches 2.. run title @s title {"text":"§e§l您已被检测到使用连点器！"}
execute if score @s fcub_auto_click_count matches 2 run function fcub:anti/autoclick/kill
execute if score @s fcub_auto_click_count matches 3.. run function fcub:anti/autoclick/kick
#execute if score @s fcub_auto_click_count matches 4 run ban @p 因使用连点器，您受到了天道的制裁。
#scoreboard players reset @s[scores={fcub_auto_click_count=4}] fcub_auto_click_count
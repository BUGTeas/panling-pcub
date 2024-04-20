kill @s
tellraw @s [{"text":"§e§l您已经第 "},{"score":{"name": "@s","objective": "fcub_auto_click_count"}},{"text":" §e§l次被检测到使用连点器！ §r因此受到了致命的惩罚。"}]
tellraw @a[distance=0.1..] [{"selector":"@s"},{"text":" §r因 使用连点器 受到了死亡惩罚。"}]
execute as @a[tag=!honorHeadFix_disable] if score @s pcub_honorHeadFix_countCMD < @s pcub_honorHeadFix_count run function pcub:honor_head_fix/delay_player
scoreboard players reset @a pcub_honorHeadFix_count
scoreboard players reset @a pcub_honorHeadFix_countCMD
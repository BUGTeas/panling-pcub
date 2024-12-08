advancement revoke @s only pcub:honor_head_fix/all
#计数
scoreboard players add @s pcub_honorHeadFix_count 1
#初始化CMD计数，以免判断异常
scoreboard players add @s pcub_honorHeadFix_countCMD 0
#下一刻开始检测
schedule function pcub:honor_head_fix/delay 1t
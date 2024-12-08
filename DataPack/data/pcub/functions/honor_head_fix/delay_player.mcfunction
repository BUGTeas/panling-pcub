#调试
	tellraw @s[tag=honorHeadFix_debug] [{"text":"[头颅修复调试] 带CMD触发×"},{"score": {"name": "@s", "objective": "pcub_honorHeadFix_countCMD"}},{"text":" 通用触发×"},{"score": {"name": "@s", "objective": "pcub_honorHeadFix_count"}}]
#提前清零计数器，以免进度触发死循环
	scoreboard players reset @s pcub_honorHeadFix_count
	scoreboard players reset @s pcub_honorHeadFix_countCMD
#遍历背包槽位
	function pcub:honor_head_fix/slot
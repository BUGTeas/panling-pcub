const fs = require("fs");
const headText = "此文件由脚本自动生成";
const headLine = "## " + headText + "\n";

//村民修复
const villagerFixLength = 24;
for (i = 0; i < villagerFixLength; i ++) fs.writeFileSync(
	"./DataPack/data/pcub/functions/bedrock_villager_fix/recipe/" + i + ".mcfunction",
	[
		headLine,
		`execute store result score @s pcub_villagerFix_temp1 run data get entity @s Offers.Recipes[${i}].uses`,
		`execute store result score @s pcub_villagerFix_temp2 run data get entity @s Offers.Recipes[${i}].maxUses`,
		`scoreboard players remove @s pcub_villagerFix_temp2 2147483647`,
		`execute if score @s pcub_villagerFix_temp1 < @s pcub_villagerFix_temp2 run data modify entity @s Offers.Recipes[${i}].uses set value -2147483647`,
		`execute if score @s pcub_villagerFix_temp1 < @s pcub_villagerFix_temp2 run data modify entity @s Offers.Recipes[${i}].maxUses set value 0`,
		`execute if data entity @s Offers.Recipes[${i + 1}] run function pcub:bedrock_villager_fix/recipe/${i + 1}`
	].join("\n")
)
fs.writeFileSync(
	"./DataPack/data/pcub/functions/bedrock_villager_fix/recipe/" + villagerFixLength + ".mcfunction",
	headLine + "\nsay 交易项数目超过 " + villagerFixLength + " 个, 这部分将不会被自动修复。"
)
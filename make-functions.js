const fs = require("fs");
const maxLength = 64;
for (i = 0; i < maxLength; i ++) fs.writeFileSync("./DataPack/data/pcub/functions/bedrock_villager_fix/recipe/" + i + ".mcfunction",
"execute store result score @s pcub_villagerFix_temp1 run data get entity @s Offers.Recipes[" + i + "].uses\n\
execute store result score @s pcub_villagerFix_temp2 run data get entity @s Offers.Recipes[" + i + "].maxUses\n\
scoreboard players remove @s pcub_villagerFix_temp2 2147483647\n\
execute if score @s pcub_villagerFix_temp1 < @s pcub_villagerFix_temp2 run data modify entity @s Offers.Recipes[" + i + "].uses set value -2147483647\n\
execute if score @s pcub_villagerFix_temp1 < @s pcub_villagerFix_temp2 run data modify entity @s Offers.Recipes[" + i + "].maxUses set value 0\n\
execute if data entity @s Offers.Recipes[" + (i + 1) + "] run function pcub:bedrock_villager_fix/recipe/" + (i + 1)
)
fs.writeFileSync("./DataPack/data/pcub/functions/bedrock_villager_fix/recipe/" + maxLength + ".mcfunction", "say 交易项数目超过 " + maxLength + " 个, 这部分将不会被自动修复。")
execute if data entity @s Offers.Recipes[0] run function pcub:bedrock_villager_fix/recipe/0
#清除缓存
scoreboard players reset @s pcub_villagerFix_temp1
scoreboard players reset @s pcub_villagerFix_temp2
#设置状态
tag @s add bedrock_fixed_134
# say 已对部分交易项进行修改，以修复基岩版 1.20.30+ 的交易 Bug。
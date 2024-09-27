scoreboard objectives add pcub_villagerFix_temp1 dummy
scoreboard objectives add pcub_villagerFix_temp2 dummy
execute if data entity @s Offers.Recipes[0] run function pcub:bedrock_villager_fix/recipe/0
scoreboard players reset @s pcub_villagerFix_temp1
scoreboard players reset @s pcub_villagerFix_temp2
tag @s add bedrock_fixed_134
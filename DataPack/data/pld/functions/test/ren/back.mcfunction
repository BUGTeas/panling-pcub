execute if block 1662 24 -225 hopper{Items:[{tag:{id:"panling:relive_stone"}}]} run tellraw @s {"translate":"pl.info.test.relife"}
execute if block 1662 24 -225 hopper{Items:[{tag:{id:"panling:relive_stone"}}]} run effect give @s instant_health 1 100
execute if block 1662 24 -225 hopper{Items:[{tag:{id:"panling:relive_stone"}}]} run tp @s 1685 26 -232 -90 ~
execute if block 1662 24 -225 hopper{Items:[{tag:{id:"panling:relive_stone"}}]} run clear @s minecraft:potion{id:"panling:killpotion"}
execute if block 1662 24 -225 hopper{Items:[{tag:{id:"panling:relive_stone"}}]} run give @s minecraft:potion{id:"panling:killpotion",CustomPotionColor:4393481,CustomModelData:30,CustomPotionEffects:[{Id:7b,Amplifier:19b,Duration:20}],display:{Lore:['{"translate":"pl.item.lore.killpotiona"}','{"translate":"pl.item.lore.killpotionb"}'],Name:'{"translate":"pl.item.name.killpotion"}'}}

data merge block 1662 24 -225 {Items:[]}
data merge block 1662 25 -225 {Items:[]}
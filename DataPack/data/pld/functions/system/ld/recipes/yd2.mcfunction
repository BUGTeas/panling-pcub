#洛神丹 -yd2
execute if score @p[tag=ld_tag] ld_count matches 2 if block ~ ~ ~ minecraft:hopper{Items:[{Slot:0b,id:"minecraft:poisonous_potato",Count:1b,tag:{id:"panling:yy1"}},{Slot:1b,id:"minecraft:string",Count:10b,tag:{id:"panling:water"}}]} run data merge block ~ ~ ~ {Items:[{Slot:2b,id:"minecraft:potion",Count:10b,tag:{id:"panling:yd2",CustomPotionColor:2039713,CustomModelData:10,CustomPotionEffects:[{Id:29b,Amplifier:0b,Duration:12000},{Id:30b,Amplifier:0b,Duration:12000}],display:{Name:'{"translate":"pl.item.potion.yd2"}',Lore:['[{"text":"§9"},{"translate":"plbe.potion.effect.conduit_power"},{"translate":"plbe.potion.time.10min"},{"translate":"plbe.potion.comma"},{"translate":"plbe.potion.effect.dolphins_grace"},{"translate":"plbe.potion.time.10min"}]']}}}]}
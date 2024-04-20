#菜单书主手
#execute if entity @s[nbt={SelectedItem:{tag:{id:"pcub:menubook"}}}] run function pcub:bedrock/menubook
#副手胡萝卜钓竿全功能
execute unless score @s pcub_open_bedrock_menu matches 1 if entity @s[nbt={Inventory:[{Slot:-106b,id:"minecraft:carrot_on_a_stick"}]}] unless entity @s[nbt={SelectedItem:{id:"minecraft:carrot_on_a_stick"}}] unless entity @s[nbt={SelectedItem:{id:"minecraft:bow"}}] unless entity @s[nbt={SelectedItem:{id:"minecraft:warped_fungus_on_a_stick"}}] unless entity @s[nbt={SelectedItem:{id:"minecraft:potion"}}] unless entity @s[nbt={SelectedItem:{id:"minecraft:splash_potion"}}] unless entity @s[nbt={SelectedItem:{id:"minecraft:magenta_dye"}}] unless entity @s[nbt={SelectedItem:{id:"minecraft:light_blue_dye"}}] unless entity @s[nbt={SelectedItem:{id:"minecraft:yellow_dye"}}] run function pld:system/right_click
#清除请求
scoreboard players set @s pcub_player_interact 2
#设置冷却
#scoreboard players set @s pcub_event_cool 10
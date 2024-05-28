#元素银行切换
#遗留
execute if entity @s[nbt={Inventory:[{tag:{id:"fcub:element_menubook"}}]}] run function fcub:element/active
execute if entity @s[nbt={SelectedItem:{tag:{id:"dlc:element_menubook"}}}] run function pcub_ext:element/menubook/switch_bedrock

#诡秘菜单切换
execute if entity @s[nbt={SelectedItem:{tag:{id:"panling:book_te25",display:{Name:"[{\"text\":\"诡秘挑战说明/辅助工具\",\"color\":\"gold\",\"italic\":false}]"}}}}] run function pcub_ext:dark_fight/menubook/switch_bedrock
#遗留
execute if entity @s[nbt={SelectedItem:{tag:{id:"shortcut:book_te25"}}}] run item replace entity @s weapon.mainhand with minecraft:book{id:"shortcut:dark_fight_menu",CustomModelData:1,display:{Name:'[{"text":"诡秘挑战说明/辅助工具","color":"gold","italic":false}]',Lore:['{"translate":"book.byAuthor","with":["§6天道"],"color":"gray","italic":false}','{"translate":"book.generation.0","color":"gray","italic":false}']}}
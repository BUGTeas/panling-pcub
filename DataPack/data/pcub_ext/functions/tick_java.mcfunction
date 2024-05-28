#元素银行切换
execute if entity @s[nbt={SelectedItem:{tag:{id:"shortcut:element_menubook"}}}] run function pcub_ext:element/menubook/switch_java
#遗留
execute if entity @s[nbt={SelectedItem:{tag:{id:"pcub_ext:element_menubook"}}}] run function pcub_ext:element/menubook/switch_java

#诡秘菜单切换
execute if entity @s[nbt={SelectedItem:{tag:{id:"shortcut:dark_fight_menu"}}}] run function pcub_ext:dark_fight/menubook/switch_java
#遗留
execute if entity @s[nbt={SelectedItem:{tag:{id:"shortcut:book_te25"}}}] run function pcub_ext:dark_fight/menubook/switch_java
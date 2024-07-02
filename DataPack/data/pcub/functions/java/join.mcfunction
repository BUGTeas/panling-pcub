#检测玩家类型用
tag @s remove player_bedrock
tag @s add player_java
#菜单书切换
execute as @s[nbt={Inventory:[{tag:{id:"pcub:menubook"}}]}] run function pcub:menubook/panling/switch_java
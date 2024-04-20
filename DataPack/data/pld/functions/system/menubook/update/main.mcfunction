clear @s written_book{title:"§6菜单", author:"§6天道"}
clear @s carrot_on_a_stick{id:"pcub:menubook"}
clear @s minecraft:fishing_rod{id:"pcub:menubook"}
execute if score @s pcub_is_bedrock matches 0 run function pld:system/menubook/update/java
execute if score @s pcub_is_bedrock matches 1 run give @s carrot_on_a_stick{id:"pcub:menubook",CustomModelData:90,display:{Name:'{"translate":"pcub.item.menu"}',Lore:['{"translate":"plbe.item.menu.content1"}','{"translate":"plbe.item.menu.content2"}']}}
#execute if score @s pcub_is_bedrock matches 1 run give @s minecraft:fishing_rod{id:"pcub:menubook",CustomModelData:90,display:{Name:'{"translate":"pcub.item.menu"}',Lore:['{"translate":"plbe.item.menu.content1"}','{"translate":"plbe.item.menu.content2"}']}}
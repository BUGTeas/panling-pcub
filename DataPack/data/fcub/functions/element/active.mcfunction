tellraw @s {"text":"§a便携元素银行已启用, 您可以将收集到的元素存放至此, 从而腾出更多背包空间! 在菜单书中即可找到它。"}
execute as @s[scores={pcub_is_bedrock=0}] run function fcub:element/switch_java
execute as @s[nbt={Inventory:[{tag:{title:"§6菜单", author:"§6天道"}}]}] run function pld:system/menubook/update/main
execute at @s run playsound minecraft:pl.zf.cooldown player @s ^ ^ ^
#检测背包空格与获取元素数量
function pld:system/test_inv/invmain
#失败1:背包空间不足
#信息
tellraw @s[scores={inv_remain=0}] {"translate":"pl.info.zf_cook_fault1","color":"red"}
#音效-失败
playsound minecraft:block.fire.extinguish ambient @s[scores={inv_remain=0}] ~ ~ ~ 1000

#扣除肉
execute store success score @s zfsuccess run clear @s[scores={inv_remain=1..}] minecraft:rabbit{id:"panling:rabbit"} 1
#给予烤肉
give @s[scores={zfsuccess=1}] minecraft:cooked_rabbit{HideFlags:63,Enchantments:[{id:"minecraft:protection",lvl:1s}],id:"panling:job1food1",display:{Name:'{"translate":"pl.item.name.job1food1"}',Lore:['{"translate":"pl.item.lore.job1food1a"}','{"translate":"pl.item.lore.food_buff_confliction"}','{"translate":"pl.item.lore.job1food1b"}','{"translate":"pl.item.lore.job1food1c"}','{"translate":"pl.item.lore.job1food1d"}','{"translate":"pl.item.lore.job1food1e"}']}}

#信息-成功
tellraw @s[scores={zfsuccess=1}] {"translate":"pl.info.zf_cook_success","color":"green"}
playsound minecraft:pl.zf.cooldown player @a[scores={zfsuccess=1}] ~ ~ ~ 1000

advancement grant @s[scores={zfsuccess=1}] only pld:other/ld_cook

#重置阵法成功判定
scoreboard players set @s zfsuccess 0
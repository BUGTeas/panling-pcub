#scoreboard players set #system instance5_intick 742
#异步
execute if score #system final_battle_stage matches 6 run scoreboard players set #system instance5_intick 186

execute unless score #system final_battle_stage matches 6 unless score #system pvpevent matches 1.. if entity @e[x=-184,y=98,z=-821,distance=..40,type=player,scores={race=0,finish_shen_hide=1}] if entity @e[x=-184,y=98,z=-821,distance=..40,type=player,scores={race=1,finish_yao_hide=1}] if entity @e[x=-184,y=98,z=-821,distance=..40,type=player,scores={race=2,finish_xian_hide=1}] if entity @e[x=-184,y=98,z=-821,distance=..40,type=player,scores={race=3,finish_zhan_hide=1}] if entity @e[x=-184,y=98,z=-821,distance=..40,type=player,scores={race=4,finish_ren_hide=1}] run scoreboard players set #system instance5_intick 186

execute unless score #system final_battle_stage matches 6 if score #system instance5_intick matches 186 run tellraw @a [{"text":"§a如果不出意外的话, 在本轮圣山之战后, 所有的真相都将浮出水面。"}]

execute unless score #system final_battle_stage matches 6 unless score #system instance5_intick matches 186 run tellraw @a[x=-184,y=98,z=-821,distance=..40] [{"text":"这颗大茧毫无反应。","bold":true,"color":"red"},{"translate": "pl.info.instance5.end3"}]

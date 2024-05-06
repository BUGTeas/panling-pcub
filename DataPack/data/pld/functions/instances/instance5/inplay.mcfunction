#入场动画函数 758
#execute if score #system instance5_intick matches 741 run setblock -184 99 -832 minecraft:air
#execute if score #system instance5_intick matches 725 run tellraw @a[x=-184,y=98,z=-821,distance=..40] {"translate": "pl.info.instance5.inplay1"}
#execute if score #system instance5_intick matches 685 run particle happy_villager -189 112 -832 5 5 5 1 500 force
#execute if score #system instance5_intick matches 685 run playsound pl.ding ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
#execute if score #system instance5_intick matches 645 run particle happy_villager -189 112 -832 1 1 1 1 100 force
#execute if score #system instance5_intick matches 645 run playsound pl.ding ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
#execute if score #system instance5_intick matches 621 run playsound pl.instance5.teleport ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
#execute if score #system instance5_intick matches 619 run summon armor_stand -189 112 -832 {ArmorItems:[{id:"minecraft:chainmail_boots",Count:1},{id:"minecraft:chainmail_leggings",Count:1},{id:"minecraft:chainmail_chestplate",Count:1},{id:"minecraft:player_head",Count:1,tag:{SkullOwner:{Id:[I;260497508,-338736586,-1333673891,-1422241753],Properties:{textures:[{Value:"eyJ0aW1lc3RhbXAiOjE0MTEyODYxMjYzMjIsInByb2ZpbGVJZCI6IjViYjE5ZjBjNDBjNjQzMmZhMGY0NTQyZDAzY2YzZGNjIiwicHJvZmlsZU5hbWUiOiJBdWRpYWNlMDgwOSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMTlmYzlhZTM3YmZkMTNlY2I5ZmNkMmJlNWYxOWRhZjkzOWI2OTA2ZjU5NjU3NmFlYWU1M2QxYjE1NGRlYiJ9fX0="}]}}}}],CustomName:"",CustomNameVisible:0,Invulnerable:1,DisabledSlots:2039552,Invisible:0,NoBasePlate:1,NoGravity:1,ShowArms:1,Small:0,Pose:{Body:[0.0F,0.0F,0.0F],LeftArm:[20F,0.0F,30F],RightArm:[20F,0.0F,330F],LeftLeg:[0.0F,0.0F,0.0F],RightLeg:[0.0F,0.0F,0.0F],Head:[25.0F,0.0F,0.0F]},Rotation:[-30F,0.0F]}

#execute if score #system instance5_intick matches 559 run tellraw @a[x=-184,y=98,z=-821,distance=..40,scores={race=1..}] {"translate": "pl.info.instance5.inplay2"}
#execute if score #system instance5_intick matches 559 run tellraw @a[x=-184,y=98,z=-821,distance=..40,scores={race=0}] {"translate": "pl.info.instance5.inplay2b"}
#execute if score #system instance5_intick matches 480 run tellraw @a[x=-184,y=98,z=-821,distance=..40] {"translate": "pl.info.instance5.inplay3"}
#execute if score #system instance5_intick matches 400 run effect clear @a[x=-184,y=98,z=-821,distance=..40] minecraft:night_vision
#execute if score #system instance5_intick matches 400 run summon lightning_bolt -184 98 -826
#execute if score #system instance5_intick matches 400 run tellraw @a[x=-184,y=98,z=-821,distance=..40] {"translate": "pl.info.instance5.inplay4"}
#execute if score #system instance5_intick matches 329 run effect give @a[x=-184,y=98,z=-821,distance=..40] minecraft:blindness 25 0
#execute if score #system instance5_intick matches 329 run kill @e[x=-184,y=98,z=-821,distance=..40,type=armor_stand,limit=1]
#execute if score #system instance5_intick matches 209 run playsound pl.instance5_pangu1 ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
#execute if score #system instance5_intick matches 209 run tellraw @a[x=-184,y=98,z=-821,distance=..40] {"translate": "pl.info.instance5.inplay5"}
#execute if score #system instance5_intick matches 129 run tellraw @a[x=-184,y=98,z=-821,distance=..40] {"translate": "pl.info.instance5.inplay6"}
#execute if score #system instance5_intick matches 33 run tellraw @a[x=-184,y=98,z=-821,distance=..40] {"translate": "pl.info.instance5.inplay7"}
#execute if score #system instance5_intick matches 33 run playsound pl.instance5_pangu2 ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
#execute if score #system instance5_intick matches 25 run playsound entity.generic.explode ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
#execute if score #system instance5_intick matches 25 run particle explosion -184 98 -821 5 5 5 1 300 force
#execute if score #system instance5_intick matches 17 run playsound entity.generic.explode ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
#execute if score #system instance5_intick matches 17 run particle explosion -184 98 -821 5 5 5 1 300 force
#execute if score #system instance5_intick matches 9 run particle explosion -184 98 -821 5 5 5 1 300 force
#execute if score #system instance5_intick matches 9 run playsound entity.generic.explode ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
#execute if score #system instance5_intick matches 1 run playsound entity.generic.explode ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
#execute if score #system instance5_intick matches 1 run particle explosion -184 98 -821 5 5 5 1 300 force
#execute if score #system instance5_intick matches 1 run forceload add -184 -832
#execute if score #system instance5_intick matches 1 run setblock -184 99 -832 minecraft:stone_button
#execute if score #system instance5_intick matches 1 run forceload remove -184 -832
#execute if score #system instance5_intick matches 1 run function pld:instances/instance5/begin

#异步 149
execute if score #system instance5_intick matches 185 run setblock -184 99 -832 minecraft:air
execute if score #system instance5_intick matches 181 run tellraw @a[x=-184,y=98,z=-821,distance=..40] {"translate": "pl.info.instance5.inplay1"}
execute if score #system instance5_intick matches 171 run particle happy_villager -189 112 -832 5 5 5 1 500 force
execute if score #system instance5_intick matches 171 run playsound pl.ding ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
execute if score #system instance5_intick matches 161 run particle happy_villager -189 112 -832 1 1 1 1 100 force
execute if score #system instance5_intick matches 161 run playsound pl.ding ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
execute if score #system instance5_intick matches 155 run playsound pl.instance5.teleport ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
execute if score #system instance5_intick matches 155 run summon armor_stand -189 112 -832 {ArmorItems:[{id:"minecraft:chainmail_boots",Count:1},{id:"minecraft:chainmail_leggings",Count:1},{id:"minecraft:chainmail_chestplate",Count:1},{id:"minecraft:player_head",Count:1,tag:{SkullOwner:{Id:[I;260497508,-338736586,-1333673891,-1422241753],Properties:{textures:[{Value:"eyJ0aW1lc3RhbXAiOjE0MTEyODYxMjYzMjIsInByb2ZpbGVJZCI6IjViYjE5ZjBjNDBjNjQzMmZhMGY0NTQyZDAzY2YzZGNjIiwicHJvZmlsZU5hbWUiOiJBdWRpYWNlMDgwOSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMTlmYzlhZTM3YmZkMTNlY2I5ZmNkMmJlNWYxOWRhZjkzOWI2OTA2ZjU5NjU3NmFlYWU1M2QxYjE1NGRlYiJ9fX0="}]}}}}],CustomName:"",CustomNameVisible:0,Invulnerable:1,DisabledSlots:2039552,Invisible:0,NoBasePlate:1,NoGravity:1,ShowArms:1,Small:0,Pose:{Body:[0.0F,0.0F,0.0F],LeftArm:[20F,0.0F,30F],RightArm:[20F,0.0F,330F],LeftLeg:[0.0F,0.0F,0.0F],RightLeg:[0.0F,0.0F,0.0F],Head:[25.0F,0.0F,0.0F]},Rotation:[-30F,0.0F]}
execute if score #system instance5_intick matches 140 run tellraw @a[x=-184,y=98,z=-821,distance=..40,scores={race=1..}] {"translate": "pl.info.instance5.inplay2"}
execute if score #system instance5_intick matches 140 run tellraw @a[x=-184,y=98,z=-821,distance=..40,scores={race=0}] {"translate": "pl.info.instance5.inplay2b"}
execute if score #system instance5_intick matches 120 run tellraw @a[x=-184,y=98,z=-821,distance=..40] {"translate": "pl.info.instance5.inplay3"}
execute if score #system instance5_intick matches 100 run effect clear @a[x=-184,y=98,z=-821,distance=..40] minecraft:night_vision
execute if score #system instance5_intick matches 100 run summon lightning_bolt -184 98 -826
execute if score #system instance5_intick matches 100 run tellraw @a[x=-184,y=98,z=-821,distance=..40] {"translate": "pl.info.instance5.inplay4"}
execute if score #system instance5_intick matches 82 run effect give @a[x=-184,y=98,z=-821,distance=..40] minecraft:blindness 25 0
execute if score #system instance5_intick matches 82 run kill @e[x=-184,y=98,z=-821,distance=..40,type=armor_stand,limit=1]
execute if score #system instance5_intick matches 52 run playsound pl.instance5_pangu1 ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
execute if score #system instance5_intick matches 52 run tellraw @a[x=-184,y=98,z=-821,distance=..40] {"translate": "pl.info.instance5.inplay5"}
execute if score #system instance5_intick matches 32 run tellraw @a[x=-184,y=98,z=-821,distance=..40] {"translate": "pl.info.instance5.inplay6"}
execute if score #system instance5_intick matches 8 run tellraw @a[x=-184,y=98,z=-821,distance=..40] {"translate": "pl.info.instance5.inplay7"}
execute if score #system instance5_intick matches 8 run playsound pl.instance5_pangu2 ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
execute if score #system instance5_intick matches 6 run playsound entity.generic.explode ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
execute if score #system instance5_intick matches 6 run particle explosion -184 98 -821 5 5 5 1 300 force
execute if score #system instance5_intick matches 4 run playsound entity.generic.explode ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
execute if score #system instance5_intick matches 4 run particle explosion -184 98 -821 5 5 5 1 300 force
execute if score #system instance5_intick matches 2 run particle explosion -184 98 -821 5 5 5 1 300 force
execute if score #system instance5_intick matches 2 run playsound entity.generic.explode ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
execute if score #system instance5_intick matches 1 run playsound entity.generic.explode ambient @a[x=-184,y=98,z=-821,distance=..40] -189 112 -832 10 1 1
execute if score #system instance5_intick matches 1 run particle explosion -184 98 -821 5 5 5 1 300 force
execute if score #system instance5_intick matches 1 run forceload add -184 -832
execute if score #system instance5_intick matches 1 run setblock -184 99 -832 minecraft:stone_button
execute if score #system instance5_intick matches 1 run forceload remove -184 -832

execute if score #system instance5_intick matches 1 unless score #system final_battle_stage matches 6 unless score #system pvpevent matches 1.. if entity @e[x=-184,y=98,z=-821,distance=..40,type=player,scores={race=0,finish_shen_hide=1}] if entity @e[x=-184,y=98,z=-821,distance=..40,type=player,scores={race=1,finish_yao_hide=1}] if entity @e[x=-184,y=98,z=-821,distance=..40,type=player,scores={race=2,finish_xian_hide=1}] if entity @e[x=-184,y=98,z=-821,distance=..40,type=player,scores={race=3,finish_zhan_hide=1}] if entity @e[x=-184,y=98,z=-821,distance=..40,type=player,scores={race=4,finish_ren_hide=1}] run scoreboard players set #system fcub_final_state 1

execute if score #system instance5_intick matches 1 if score #system fcub_final_state matches 1 run scoreboard players set @a[x=-184,y=98,z=-821,distance=..40] fcub_final_state 1

execute if score #system instance5_intick matches 1 if score #system fcub_final_state matches 1 run function pld:instances/instance5/begin

execute if score #system instance5_intick matches 1 if score #system final_battle_stage matches 6 run function pld:instances/instance5/begin


scoreboard players remove #system instance5_intick 1



 

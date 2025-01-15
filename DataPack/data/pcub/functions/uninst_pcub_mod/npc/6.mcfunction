execute positioned 122 44 805 run function pld:npcs/south/texian
execute positioned 11 43 810 run function pld:npcs/south/tong4
execute positioned -119 41 138 run function pld:npcs/west/teshen
execute positioned -397 107 152 run function pld:npcs/west/travelling_trader

forceload remove 1653 174
forceload remove 3348 360
forceload remove 375 767
forceload remove -283 395
forceload add 3207 860
forceload add 2656 897
forceload add 3237 -248
schedule function pcub:uninst_pcub_mod/npc/7 1s
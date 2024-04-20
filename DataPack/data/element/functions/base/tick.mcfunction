scoreboard players enable @a[scores={fcub_element_store_enable=1,feather_mainland=1}] element_menu_trigger1

execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=1}] run function element:base/metal_get_5
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=2}] run function element:base/metal_get_64
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=3}] run function element:base/metal_store_5
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=4}] run function element:base/metal_store_all

execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=5}] run function element:base/wood_get_5
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=6}] run function element:base/wood_get_64
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=7}] run function element:base/wood_store_5
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=8}] run function element:base/wood_store_all

execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=9}] run function element:base/water_get_5
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=10}] run function element:base/water_get_64
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=11}] run function element:base/water_store_5
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=12}] run function element:base/water_store_all

execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=13}] run function element:base/fire_get_5
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=14}] run function element:base/fire_get_64
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=15}] run function element:base/fire_store_5
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=16}] run function element:base/fire_store_all

execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=17}] run function element:base/earth_get_5
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=18}] run function element:base/earth_get_64
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=19}] run function element:base/earth_store_5
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=20}] run function element:base/earth_store_all

execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=21}] run function element:base/metal_info
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=22}] run function element:base/wood_info
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=23}] run function element:base/water_info
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=24}] run function element:base/fire_info
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=25}] run function element:base/earth_info

execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=26}] run function element:base/metal_store_refined
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=27}] run function element:base/metal_get_refined
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=28}] run function element:base/metal_get_refined_10

execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=29}] run function element:base/wood_store_refined
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=30}] run function element:base/wood_get_refined
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=31}] run function element:base/wood_get_refined_10

execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=32}] run function element:base/water_store_refined
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=33}] run function element:base/water_get_refined
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=34}] run function element:base/water_get_refined_10

execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=35}] run function element:base/fire_store_refined
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=36}] run function element:base/fire_get_refined
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=37}] run function element:base/fire_get_refined_10

execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=38}] run function element:base/earth_store_refined
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=39}] run function element:base/earth_get_refined
execute as @a[scores={fcub_element_store_enable=1,feather_mainland=1,element_menu_trigger1=40}] run function element:base/earth_get_refined_10

scoreboard players reset @a[scores={element_menu_trigger1=1..}] element_menu_trigger1
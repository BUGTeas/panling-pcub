scoreboard players set #system element_dlc_installed 1

scoreboard players reset @a element_dlc_install_setting

scoreboard objectives add element_number_metal dummy "金元素储存量"
scoreboard objectives add element_number_wood dummy "木元素储存量"
scoreboard objectives add element_number_water dummy "水元素储存量"
scoreboard objectives add element_number_fire dummy "火元素储存量"
scoreboard objectives add element_number_earth dummy "土元素储存量"
scoreboard objectives add element_middle_number dummy "计算中间用变量"
scoreboard objectives add element_menu_trigger1 trigger "元素银行-基础功能"

execute if score #system element_dlc_optional1 matches 1 run function element:install_optional1

#forceload add 184 67 186 69
#setblock 186 46 69 minecraft:structure_block[mode=load]{author:"Akiyama_mikagami",ignoreEntities:1b,#integrity:1.0f,metadata:"",mirror:"NONE",mode:"LOAD",name:"element:menubook",posX:-2,posY:-6,posZ:-2,#powered:0b,rotation:"NONE",seed:0L,showair:1b,showboundingbox:1b,sizeX:3,sizeY:6,sizeZ:3}
#setblock 186 47 69 redstone_block
#setblock 186 46 69 air
#setblock 186 47 69 air
#forceload remove 184 67 186 69

schedule function element:installmsg 1s
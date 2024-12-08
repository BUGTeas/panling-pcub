#读取索引值
execute store result score @s temp run data get block 0 -1 0 Items[0].tag.type
#CMD从100开始
scoreboard players add @s temp 100
#写入CMD
execute store result block 0 -1 0 Items[0].tag.CustomModelData int 1 run scoreboard players get @s temp
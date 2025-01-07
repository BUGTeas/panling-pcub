#初始化连续投掷
execute unless score @s pcub_enable_continuous matches 0.. run scoreboard players set @s pcub_enable_continuous 3
execute unless score @s pcub_drop_interval matches 0.. run scoreboard players set @s pcub_drop_interval 4
#初始化快速被动
execute unless score @s job matches 1.. unless score @s pcub_enable_fastskill matches 0.. run scoreboard players set @s pcub_enable_fastskill 3
execute unless score @s job matches 1.. unless score @s pcub_fastskill_duration matches 0.. run scoreboard players set @s pcub_fastskill_duration 0
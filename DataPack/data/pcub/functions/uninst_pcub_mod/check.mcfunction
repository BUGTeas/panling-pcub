execute store success score #system temp run datapack disable "file/pcub_mod.zip"

execute if score #system temp matches 1 run say [梦盘互通套件] 检测到过时的优化修改组件，现已将其数据包禁用。

execute if score #system temp matches 1 run function pcub:uninst_pcub_mod/npc
# 主手物品展示优化（解决主手手持菜单书时，展示不了不支持副手的物品）
execute if entity @s[tag=pcub_showitem,nbt=!{SelectedItem: {tag: {title: "§6菜单", author: "§6天道"}}}] run function pcub:bedrock/showitem/work
# 梦盘菜单书补丁
execute if entity @s[nbt={SelectedItem: {tag: {title: "§6菜单", author: "§6天道"}}}] unless entity @s[nbt={SelectedItem: {tag: {pcub_patch_ver: 0}}}] run item modify entity @s weapon.mainhand pcub:menubook_patch/panling
const fs = require("fs");
const headText = "此文件由脚本自动生成";
const headLine = "## " + headText + "\n";

//村民修复
const villagerFixLength = 24;
for (i = 0; i < villagerFixLength; i ++) fs.writeFileSync(
	"./DataPack/data/pcub/functions/bedrock_villager_fix/recipe/" + i + ".mcfunction",
	[
		headLine,
		`execute store result score @s pcub_villagerFix_temp1 run data get entity @s Offers.Recipes[${i}].uses`,
		`execute store result score @s pcub_villagerFix_temp2 run data get entity @s Offers.Recipes[${i}].maxUses`,
		`scoreboard players remove @s pcub_villagerFix_temp2 2147483647`,
		`execute if score @s pcub_villagerFix_temp1 < @s pcub_villagerFix_temp2 run data modify entity @s Offers.Recipes[${i}].uses set value -2147483647`,
		`execute if score @s pcub_villagerFix_temp1 < @s pcub_villagerFix_temp2 run data modify entity @s Offers.Recipes[${i}].maxUses set value 0`,
		`execute if data entity @s Offers.Recipes[${i + 1}] run function pcub:bedrock_villager_fix/recipe/${i + 1}`
	].join("\n")
)
fs.writeFileSync(
	"./DataPack/data/pcub/functions/bedrock_villager_fix/recipe/" + villagerFixLength + ".mcfunction",
	headLine + "\nsay 交易项数目超过 " + villagerFixLength + " 个, 这部分将不会被自动修复。"
)

//忠烈祠头颅佩戴修复
const getSlotName = function(slot) {
	if (slot == 103) return "armor.head";
	if (slot == -106) return "weapon.offhand";
	if (slot < 9) return "hotbar." + slot;
	return "inventory." + (slot - 9);
}
const getFileName = function(slot) {
	if (slot == 103) return "head";
	if (slot == -106) return "offhand";
	return slot;
}
var slotChkContent = [ headLine ];
const createSlotElement = function(slot){
	const slotName = getSlotName(slot);
	const fileName = getFileName(slot);
	slotChkContent.push([
		`execute`,
		`if data entity @s Inventory[{Slot: ${slot}b, id: "minecraft:player_head", tag: {id: "panling:honor_head"}}]`,
		`unless data entity @s Inventory[{Slot: ${slot}b}].tag.CustomModelData`,
		`run function pcub:honor_head_fix/slot/${fileName}`
	].join(" "))
	fs.writeFileSync(
		"./DataPack/data/pcub/functions/honor_head_fix/slot/" + fileName + ".mcfunction",
		[
			headLine,
			`item replace block 0 -1 0 container.0 from entity @s ${slotName}`,
			`function pcub:honor_head_fix/set_cmd`,
			`item replace entity @s ${slotName} from block 0 -1 0 container.0`
		].join("\n")
	)
}
for (i = 0; i < 36; i ++) createSlotElement(i);
createSlotElement(-106);
createSlotElement(103);
fs.writeFileSync(
	"./DataPack/data/pcub/functions/honor_head_fix/slot.mcfunction",
	slotChkContent.join("\n")
)
const typeList = [
	1, 2, 3, 4, 5, 6,
	8, 9, 10, 11, 12, 13
]
for (i in typeList) {
	const advObject = {
		注意: headText,
		parent: "pcub:honor_head_fix/with_cmd",
		criteria: {},
		rewards: {
			function: "pcub:honor_head_fix/with_cmd"
		}
	}
	const advCriteria = {
		trigger: "minecraft:inventory_changed",
		conditions: {
			items: [
				{
					items: [ "minecraft:player_head" ],
					nbt: `{id:"panling:honor_head",CustomModelData:${100 + typeList[i]}}`
				}
			]
		}
	}
	advObject.criteria["pcub_honorHeadFix_withCMD_" + typeList[i]] = advCriteria;
	fs.writeFileSync(
		"./DataPack/data/pcub/advancements/honor_head_fix/with_cmd/" + typeList[i] + ".json",
		JSON.stringify(advObject)
	)
}
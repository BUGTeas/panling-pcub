# 面向有技术经验的腐竹的说明
阅读此技术性说明前请先阅读使用说明。强烈建议使用支持 Markdown 的阅读器查看。

## 基本原理

本方案默认使用 Spigot 1.20.1 插件服务端核心，并使用以下插件：
1. [Geyser-Spigot](https://geysermc.org/)：  
	间歇泉，互通实现插件
2. [ViaVersion](https://www.spigotmc.org/resources/viaversion.19254/)：  
	由于 Geyser 紧随最新 Minecraft 原版更新，而当前梦回盘灵版本仍然在 1.20/1.20.1，故需要此插件向上兼容
3. [CrossPlatForms](https://www.spigotmc.org/resources/crossplatforms.101043/)：  
	原先在基岩版不支持的菜单书，通过此插件的 Form 表单界面重新制作
4. [PlaceHolderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)：  
	为 Form 表单界面的动态内容提供重要支持，  
	此外还需要安装子组件 `player`、`scoreboardobjectives`

**除非使用服务端部署包**，上述插件均需自行下载安装。

此外，本方案自带一个特制专用插件，即 `PCUB.jar`（“PCUB”为盘灵无界英文“Pan Gu Continent Unbounded”的缩写），提供更多针对性优化，其中包括：  
1. 修复基岩版叠放丹药移动，可以像 Java 版一样按 Shift 或触屏双击移动，雪球（退火符、击退符）、蘑菇煲（佛跳墙、万春羹）及兔肉煲也同样支持
2. 修复因 Geyser 诡异 Bug 导致交易点一下会连续交易多次（牺牲了基岩版按 Shift 一次性交易全部的功能，不影响 Java 版）
3. 可以禁用丹药/雪球的连续投掷，或者设置其间隔，以避免误触造成的损失。
4. 战士可以设置潜行一键发动技能，无需右键/长按，从而简化武器技能的触发方式
6. 允许玩家通过手持物品打开菜单书等表单界面，  
	内容创作者也可以创建带有标签 `{PublicBukkitValues: {"pcub:run_command": "<命令>"}}` 的空书（minecraft:book）实现基岩版玩家右键执行特定命令，  
	比如手持物品 `{id: "minecraft:book", tags: {PublicBukkitValues: {"pcub:run_command": "help"}}}` 右键，玩家就会执行命令 `/help`，  
	该功能仍在完善，请慎用（Java 版也即将支持敬请期待）
7. 玩家点击村民后，会先以村民身份执行标签 `#pcub:interact_villager/villager` 中的所有函数，然后以玩家身份执行标签 `#pcub:interact_villager/player` 中的所有函数，最后再加载交易界面。  
	内容创作者可以通过数据包在这些标签中附加一些需要在交易界面加载前执行的函数。
8. 内容创作者可以在标签 `#pcub:bedrock_right_click` 中附加需要在基岩版胡萝卜钓竿副手右键触发时执行的函数
9. 为优化修改选装组件提供伪叠放转换功能
10. 强制叠放背包所有物品：不同于常见的叠放插件，它通过读取NBT标签字符串整理物品，完美兼容盘灵
11. 防止玩家在冒险模式下摘花盆里的花或食用地图上的蛋糕

未来该插件会被进一步拆分以提高其实用性。自带命令 `/pcub`，更多功能请自行探索。

在本压缩档中已经存在服务器目录结构，以 `PanGuContinentUnbounded-server` 作为服务器根目录（名称可自行更改，安装其他组件时能够理解文档的意思就行），里面除了专用数据包、插件以及用于基岩版的资源包外，还有一些已经调好的必要配置文件。



## 有关叠放插件

由于特制专用插件对叠放的修复仅限于基岩版 Shift/双击移动/交易，丢出捡回等操作仍可能会被打散，强烈建议搭配叠放插件使用！
以下为经过测试的叠放插件：
1. [PotionStacker](https://www.spigotmc.org/resources/potion-stacker.66168/)：
	最简单易用，是快速部署包中默认启用的叠放插件，但只支持药水的叠放
2. [SimpleStack](https://github.com/Mikedeejay2/SimpleStackPlugin)：
	该插件支持任意物品的超量叠放，可自由配置白名单/黑名单模式。
	注意：请不要在 spigotmc.org 中下载它，提供的版本非常旧，不支持 1.20，
	不过上方的 GitHub 仓库现仍保持更新，提供了最新的 Dev 快照版本，可以在 Action 中找到。且部分版本存在无法加载的Bug，建议从服务端部署包中拿出经过我测试过的版本。
	该插件需要使用完整的 Java JDK 开服才能正常运行，不支持 JRE！

建议为以下物品设置叠放（不建议超过 64 个）：
- 药水：`POTION`
- 喷溅药水：`SPLASH_POTION`
- 雪球：`SNOWBALL`
- 蘑菇煲：`MUSHROOM_STEW`
- 兔肉煲：`RABBIT_STEW`

其他的插件我暂未测试，可以自行尝试，若出现Bug请向我反馈。

如果未通过插件配置叠放蘑菇煲（佛跳墙、万春羹）、兔肉煲，请不要将这些食物强制叠放后食用， **会一次性吃光！且只有食用一个的饱和度！**



## 使用非 Spigot 服务端

如果您不能独立分析一些 Bug 的根源所在，请不要使用非 Spigot 核心，**尤其是 Paper 及其分支**。

如果您希望将互通方案用在非插件平台上（例如 Fabric），则只能使用 `plugins/Geyser-Spigot` 文件夹中的内容，这里包含了 Geyser 所有版本通用的配置文件及资源包，除此之外的其它功能（包括数据包）均只为插件端设计，也就是说在这些平台上无法实现菜单书、基岩版丹药叠放（仅依靠JE叠放模组在基岩版下不能完美生效）等部分针对性优化。除此之外，所有的选装组件除非特殊说明外，均不支持非插件平台。



## 在携带版UI档案下强制使用经典箱子界面，防止布局错乱

该方案已经在末影箱界面中实装，此外对副本保底便捷钱庄（插件 DLC）做了特别适配，当容器标题中带有 `掌上钱庄` 或 `元素锻炉` 字样时就会生效。

而如果您也有这样的需求，只需要给容器的标题后面加上 `:force_desktop_ui:` 字段即可，比如 `测试容器:force_desktop_ui:`。该字段也可以用在 Geyser 自定义语言文件中，在容器标题中通过其本地化键名调用（注意是 Geyser 的语言文件，而不是客户端资源包的）。除非与个别第三方UI冲突，否则都能生效，且参数字段不会在界面中显示。



## 本组件对游戏内容所造成的影响

为了确保基岩版玩家的正常游戏，本组件目前会对存档中以下数据进行修改，这在将来可能会对某些 DLC 内容造成影响：

### 钱庄末影箱系统

基岩版触屏移动按钮的拿起、放下操作都是在同一游戏刻下请求，这导致原有的刻循环系统无法及时地检测到末影箱中的这些按钮被拿起，而使其可能被任意移动，由于部分按钮中储存有箱子中的所有数据（包括信件、仓库），一旦被挪动则可能因为数据读取异常导致其全被清空！

本组件通过插件监听玩家事件实现了更敏捷的检测，并覆盖弃用掉原有的循环检测函数 `pld:system/chest_menu/tick_players`，从而避免此类问题的发生。

### 所有村民 NPC

由于数据满足条件 `uses < maxUses - 2147483647` 的村民交易项（`Offers.Recipes[x]`）无法在基岩版 1.21.30+ 中使用触屏或 Shift 键交易，现在会对所有村民的前 24 个交易项进行自动检查修复。

修复原理是将数据 `Offers.Recipes[x].uses` 设为 -2147483647，`Offers.Recipes[x].maxUses` 设为 0。当村民被玩家交互时，由 PCUB 插件在交易界面加载前通过标签 `#pcub:interact_villager/villager` 执行 pcub 数据包中的修复函数。该修复不会覆盖原数据包文件。

### 忠烈祠头饰

由于经过 Geyser 映射后的自定义头颅作为自定义方块，受制于基岩版特性无法直接装备（佩戴），现通过为这些头饰附加模型数据值（CustomModelData），并基于此值在 Geyser 中二次映射，使其成为正常物品可装备。

修复仅限于带有值为 `panling:honor_head` 的自定义标签 `id` 的玩家头颅 `minecraft:player_head`，附加的数据值为 100 + 自定义标签 `type` 的数值。

比如物品 `{id: "minecraft:player_head", tag: {id: "panling:honor_head", type: 4}}` 经过处理后就会变成 `{id: "minecraft:player_head", tag: {id: "panling:honor_head", type: 4, CustomModelData: 104}}`。该修复不会覆盖原数据包文件。
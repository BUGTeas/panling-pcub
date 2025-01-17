# 技术性说明（梦回盘灵 Java - 基岩双端互通套件）
请先阅读使用说明。
<!--强烈建议使用支持 Markdown 的阅读器查看。-->

## ⛏️ 服务端环境搭建

### 核心配置

使用任意插件端（Bukkit 系）核心，游戏版本为 1.20.1。

**推荐使用 Spigot 作为服务端核心，能最大程度减少对原版特性的影响**。

如果您需要使用 Paper 插件或者在 Android 平台上开服，建议使用 Leaves 核心，在 Paper 的基础上尽可能地修复被破坏的原版特性。（稳定性及兼容性比 Spigot 略高，但原版特性的完整性不及 Spigot）

### 插件配置

以下插件为实现互通的基本条件：

- [Geyser-Spigot](https://geysermc.org/)：间歇泉，互通实现插件，在当前版本中已内置魔改版本，无需自行下载
- [ViaVersion](https://www.spigotmc.org/resources/viaversion.19254/)：由于 Geyser 紧随最新 Minecraft 原版更新，而当前梦回盘灵版本仍然在 1.20/1.20.1，故需要此插件向上兼容

以下插件为必需：

- [CrossPlatForms](https://www.spigotmc.org/resources/crossplatforms.101043/)：原先在基岩版不支持的菜单书，通过此插件的 Form 表单界面重新制作
- [PlaceHolderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)：为 Form 表单界面的动态内容提供重要支持，
  - 此外还需要安装子组件 `player`、`scoreboardobjectives`

为了使基岩版正常叠放丹药，你还需要准备一个自定义物品叠放插件，并设置药水、喷溅药水的叠放上限为 64。以下为经过测试的插件：

- [PotionStacker](https://www.spigotmc.org/resources/potion-stacker.66168/)：最简单易用，但只支持药水、喷溅药水和滞留药水的叠放
- [StackableItems](https://dev.bukkit.org/projects/stackableitems)：支持配置任意物品叠放
  - 在 Spigot 下使用漏斗可能会出现刷物品，甚至影响红石系统等 Bug，建议在 Leaves 下使用
  - 物品移动过程不太完美，小概率会出现回弹等情况
- [SimpleStack（兼容性差）](https://github.com/Mikedeejay2/SimpleStackPlugin)：同样支持任意物品，但上限 64，可自由配置白名单/黑名单模式。
	- 请不要在 spigotmc.org 中下载它，提供的版本非常旧，不过上方的 GitHub 仓库现仍保持更新，提供了最新的 Dev 快照版本，可以在 Action 中找到。但多数版本存在无法加载的Bug，建议从服务端部署包中拿出经过我测试过的版本。
	- 对于 Java 环境要求苛刻，需要使用 JDK 开服才能正常运行，不支持 JRE！

建议为以下物品设置叠放（不建议超过 64 个）：
- 药水：`POTION`
- 喷溅药水：`SPLASH_POTION`
- 雪球：`SNOWBALL`
- 蘑菇煲：`MUSHROOM_STEW`
- 兔肉煲：`RABBIT_STEW`

如果未通过插件配置叠放蘑菇煲（佛跳墙、万春羹）、兔肉煲，请不要将这些食物强制叠放后食用， **会一次性吃光！且只有食用一个的饱和度！**

其他的插件我暂未测试，可以自行尝试，若出现Bug请在交流社区反馈。

### 配置文件

（所有提到的文件均位于服务端根目录）

请到 `server.properties` 作如下修改，使梦回盘灵数据包正常运作：

```properties
allow-nether=false
difficulty=normal
enable-command-block=true
gamemode=adventure
pvp=true
```
（非必需）由于插件端默认配置的掉落物/经验值合并机制与原版略有不同，可能会影响游戏体验。建议到 `spigot.yml` 中作如下修改，使其恢复原版机制：
```yml
world-settings:
  default:
    merge-radius:
      item: -1.0
      exp: -1.0
```

### 启动参数

如果您使用 Spigot 核心且未安装 Floodgate 插件，基岩版玩家可能无法打开菜单书。这是 CrossPlatForms 的兼容性问题，可以在启动命令上添加此参数以解决：`-Djdk.util.jar.enableMultiRelease=force`


## 💿 将本套件安装到服务端中

本套件的所有组成部分（包括但不限于专用的插件、数据包及配置文件），在压缩档中已按标准服务端根目录结构存放到 `PanGuContinentUnbounded-server` 文件夹中。

将该根目录结构**合并到现有服务端根目录中**即可，根目录名称不限。



## ☢️ 本套件对游戏内容所造成的影响

为了确保基岩版玩家的正常游戏，本套件目前会对存档中以下数据进行修改，这在将来可能会对某些 DLC 内容造成影响：

### 所有村民 NPC

由于数据满足条件 `uses < maxUses - 2147483647` 的村民交易项（`Offers.Recipes[x]`）无法在基岩版 1.21.30+ 中使用触屏或 Shift 键交易，现在会对所有村民的前 24 个交易项进行自动检查修复。

修复原理是将数据 `Offers.Recipes[x].uses` 设为 -2147483647，`Offers.Recipes[x].maxUses` 设为 0。当村民被玩家交互时，由 PCUB 插件在交易界面加载前通过标签 `#pcub:interact_villager/villager` 执行 pcub 数据包中的修复函数。该修复不会覆盖原数据包文件。

### 忠烈祠头饰

由于经过 Geyser 映射后的自定义头颅作为自定义方块，受制于基岩版特性无法直接装备（佩戴），现通过为这些头饰附加模型数据值（CustomModelData），并基于此值在 Geyser 中二次映射，使其成为正常物品可装备。

修复仅限于带有值为 `panling:honor_head` 的自定义标签 `id` 的玩家头颅 `minecraft:player_head`，附加的数据值为 100 + 自定义标签 `type` 的数值。

比如物品 `{id: "minecraft:player_head", tag: {id: "panling:honor_head", type: 4}}` 经过处理后就会变成 `{id: "minecraft:player_head", tag: {id: "panling:honor_head", type: 4, CustomModelData: 104}}`。该修复不会覆盖原数据包文件。

## ☢️ 选装包对游戏内容所造成的影响

如果以下功能对某些 DLC 内容造成影响，请到交流社区反馈，或禁用选装包：`/datapack disable "file/pcub_mod.zip"`

### 钱庄末影箱系统

基岩版触屏移动按钮的拿起、放下操作都是在同一游戏刻下请求，这导致原有的刻循环系统无法及时地检测到末影箱中的这些按钮被拿起，而使其可能被任意移动，由于部分按钮中储存有箱子中的所有数据（包括信件、仓库），一旦被挪动则可能因为数据读取异常导致其全被清空！

本组件通过插件监听玩家事件实现了更敏捷的检测，并覆盖弃用掉原有的循环检测函数 `pld:system/chest_menu/tick_players`，从而避免此类问题的发生。

### 针对 Floodgate 插件的修复

如果您安装了 Floodgate（基岩版登录优化）插件，则会导致基岩版玩家的 UUID 格式与 Java 版玩家不同，通过该插件登录的基岩版玩家其 UUID 通常为`00000000-0000-0000-xxxx-xxxxxxxxxxxx`（x 代表玩家的 Xbox ID，即 XUID）

由于前段值都为 0，而梦回盘灵数据包中弓箭手武器技能【日落九天·落日】的运行正是通过检测前段 UUID，这就意味着，所有通过 Floodgate 登录且未绑定 Java 版账户的基岩版玩家，都将无法正常使用这个技能。

选装包通过覆盖数据包相关函数，将检测 UUID 改为末端以修复此问题。



## ⚙️ 使用非 Spigot 服务端

如果您不能独立分析一些 Bug 的根源所在，请不要使用非 Spigot 核心，**尤其是 Paper 及其分支**。

如果您希望将此套件用在非插件平台上（例如 Fabric 等模组端），则只能使用 `plugins/Geyser-Spigot` 文件夹中的内容，这里包含了 Geyser 所有版本通用的配置文件及资源包，除此之外的其它功能（包括数据包）均只为插件端设计，也就是说在这些平台上无法实现菜单书、基岩版丹药叠放（仅依靠JE叠放模组在基岩版下不能完美生效）等部分针对性优化。除此之外，所有的选装组件除非特殊说明外，均不支持非插件平台。



## 🧩 接口支持

本套件对外开放了以下接口，以便开发者适配：

### 获取接口版本号（当前接口版本为 4，最低向下兼容到 1，即 v1.1.0）
- 接口类型：记分板（`<记分对象或玩家> <记分项>`）

当前接口版本号：`#system pcub_api_version`

向下兼容的最低接口版本号：`#system pcub_api_minVersion`

### 区分 Java 版 / 基岩版玩家
- 接口类型：实体标签

本套件为玩家添加了以下实体标签：

- `player_java`：Java 版玩家
- `player_bedrock`：基岩版玩家

标签在玩家每次进服时，都会根据玩家客户端类型重新设置。

### 副手触发
- 接口类型：函数标签

基岩版副手持胡萝卜钓竿，通过键鼠右键或触屏短长按使用不能正常触发，本套件通过事件监听实现了折中的解决方法。可以在标签 `#pcub:bedrock_right_click` 中附加需要在基岩版胡萝卜钓竿副手使用触发时执行的函数。

建议使用梦回盘灵自带的接口 `#pld:right_click/check` 或 `#pld:right_click/do` 实现胡萝卜钓竿使用时触发，无需专门为互通适配。

### 手持物品执行命令（实验性）
- 接口类型：函数标签

可以创建带有标签 `{PublicBukkitValues: {"pcub:run_command": "<命令>"}}` 的空书（minecraft:book）实现基岩版玩家使用触发时，执行特定命令。

（例）给予自己一本空书，使用时执行 `/help` 命令：`give @s minecraft:book{PublicBukkitValues: {"pcub:run_command": "help"}}`

该功能仍在完善，请慎用（Java 版也即将支持敬请期待）

### 村民交互事件
- 接口类型：函数标签

玩家点击村民后，在打开交易界面前会先以村民身份执行标签 `#pcub:interact_villager/villager` 中的所有函数，然后再以玩家身份执行标签 `#pcub:interact_villager/player` 中的所有函数，如果玩家被添加实体标签 `ignoreTradeUI`，则阻止其打开交易界面，并删除该标签。

该功能目前应用于 1.21.30+ 交易修复，您也可以通过数据包在这些标签中附加一些需要在交易界面加载前执行的函数。

### 末影箱交互事件（打开/点击/关闭）
- 接口类型：函数标签

打开末影箱（玩家交互末影箱并打开界面后）：`#pcub:chest_menu/open`

按下按钮（玩家记分项 `screen`>=0 且拿起/移动/丢出带有标签 `{clickable:1}` 的物品）：`#pcub:chest_menu/click`

关闭末影箱：`#pcub:chest_menu/leave`

注：如果只是单纯需要适配选装包，请将 `pcub` 命名空间改为 `pcub_mod` 

### 在携带版 UI 档案下强制使用经典箱子界面，防止布局错乱
- 接口类型：容器（>=9 个槽位）标题

该方案已经在末影箱界面中实装，此外对副本保底便捷钱庄（插件 DLC）做了特别适配，当容器标题中带有 `掌上钱庄` 或 `元素锻炉` 字样时就会生效。

而如果您也有这样的需求，只需要给容器的标题后面加上 `:force_desktop_ui:` 字段即可。该字段也可以用在 Geyser 自定义语言文件中，在容器标题中通过其本地化键名调用（注意是 Geyser 的语言文件，而不是客户端资源包的）。除非与个别第三方UI冲突，否则都能生效，且参数字段不会在界面中显示。

比如标题为 `测试容器:force_desktop_ui:` 的容器界面，在基岩版上将强制以经典 UI 布局呈现，标题显示为 `测试容器`。

此外，发射器界面（装备锻造）也同样强制经典界面，暂不支持更改。
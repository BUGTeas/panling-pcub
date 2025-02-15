# 技术性说明（梦回盘灵专用 Java - 基岩双端互通套件）
请先阅读使用说明。  
<!-- 强烈建议使用支持 Markdown 的阅读器查看此说明 -->  
<!-- 以下内容中所有命令均不包括`反引号 -->

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
  - 物品移动过程不太完美，小概率会出现回弹等情况，在创造模式背包下操作甚至还可能导致物品被误吞
  - 在 Spigot 下使用漏斗可能会出现刷物品，甚至影响红石系统等 Bug，经测试在 Leaves 下不会发生
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

如果您使用 Spigot 核心且未安装 Floodgate 插件，基岩版玩家可能无法打开菜单书。这是 CrossPlatForms 的兼容性问题，可以在启动命令上添加此参数以解决：  
`-Djdk.util.jar.enableMultiRelease=force`



## 💿 安装/更新本套件

本套件的所有组成部分（包括但不限于专用的插件、数据包及配置文件），在压缩档中已按标准服务端根目录结构存放到 `PanGuContinentUnbounded-server` 文件夹中。

将该根目录结构**合并到现有服务端根目录中**即可，根目录名称不限。



## ☢️ 本套件对游戏内容所造成的影响

为了确保基岩版玩家的正常游戏，本套件目前会对存档中以下数据进行修改，这在将来可能会对某些 DLC 内容造成影响：

### 所有村民 NPC

自基岩版 1.21.30 开始，当前可交易超过 2147483647 次的村民交易项，都将无法使用触屏或 Shift 键交易。在梦回盘灵中几乎所有的 NPC 都存在这种情况，故本套件会对所有村民的前 24 个交易项进行自动检查修复。

修复原理是将符合条件的数据 `Offers.Recipes[x].uses` 设为 -2147483647，`Offers.Recipes[x].maxUses` 设为 0。当村民被玩家交互时，由 PCUB 插件在交易界面加载前通过标签 `#pcub:interact_villager/villager` 执行 pcub 数据包中的修复函数。该修复不会覆盖原数据包文件。

### 忠烈祠头饰

由于经过 Geyser 映射后的自定义头颅作为自定义方块，受制于基岩版特性无法直接装备（佩戴），现通过为这些头饰附加模型数据值（CustomModelData），并基于此值在 Geyser 中二次映射，使其成为可装备的正常物品。

修复仅限于带有值为 `panling:honor_head` 的自定义标签 `id` 的玩家头颅 `minecraft:player_head`，附加的数据值为 100 + 自定义标签 `type` 的数值。

比如物品 `{id: "minecraft:player_head", tag: {id: "panling:honor_head", type: 4}}` 经过处理后就会变成 `{id: "minecraft:player_head", tag: {id: "panling:honor_head", type: 4, CustomModelData: 104}}`。该修复不会覆盖原数据包文件。



## ☢️ 选装数据包对游戏内容所造成的影响

如果以下变动对某些 DLC 内容造成影响，请到交流社区反馈，或禁用选装包：  
`/datapack disable "file/pcub_add.zip"`

### 钱庄末影箱系统

选装包基于插件事件监听实现了更敏捷高效的按钮操作检测，覆盖取代掉原有的循环检测函数，从而优化游戏性能。

如果您的游戏内容（DLC）对末影箱菜单功能有所修改，请不要使用选装包，或者将其中的以下文件或目录删除：  
- `data/pcub/tags/functions/chest_menu/`
- `data/pld/functions/system/chest_menu/tick_players.mcfunction`

再或者，若您希望适配选装包，请在您自己的数据包中添加函数标签 `#pcub_add:chest_menu/<open|click|leave>` 并在其中定义相关函数。

### 针对 Floodgate 插件的修复

如果您安装了 Floodgate（基岩版登录优化）插件，则会导致基岩版玩家的 UUID 格式与 Java 版玩家不同，通过该插件登录的基岩版玩家其 UUID 通常为`00000000-0000-0000-xxxx-xxxxxxxxxxxx`（x 代表玩家的 Xbox ID，即 XUID）

由于前段值都为 0，而梦回盘灵数据包中弓箭手武器技能【日落九天·落日】的运行正是通过检测前段 UUID，这就意味着，所有通过 Floodgate 登录且未绑定 Java 版账户的基岩版玩家，都将无法正常使用这个技能。

选装包通过覆盖数据包相关函数，将检测 UUID 改为末端以修复此问题。



## ⚙️ 使用非 Spigot 服务端

如果您不能独立分析一些 Bug 的根源所在，请不要使用非 Spigot 核心，**尤其是 Paper 及其分支**。

如果您希望将此套件用在非插件平台上（例如 Fabric 等模组端），则只能使用 `plugins/Geyser-Spigot` 文件夹中的内容，这里包含了 Geyser 所有版本通用的配置文件及资源包，除此之外的其它功能（包括数据包）均只为插件端设计，也就是说在这些平台上无法实现菜单书、基岩版丹药叠放（仅依靠 Java 版叠放模组在基岩版下无效）等部分针对性优化。



## 🧩 接口支持

本套件对外开放了以下接口，以便开发者适配：
### 获取接口版本号（当前接口和最低向下兼容版本为 6）
- 接口类型：记分板（`<记分对象或玩家> <记分项>`）

当前接口版本号：`#system pcub_api_version`

向下兼容的最低接口版本号：`#system pcub_api_minVersion`

### 本套件加载完毕
- 接口类型：函数标签

在 `#pcub:load` 中附加的函数，可在本套件的数据包加载完毕后执行。

### 区分 Java 版 / 基岩版玩家
- 接口类型：实体标签

本套件为玩家添加了以下实体标签：

- `player_java`：Java 版玩家
- `player_bedrock`：基岩版玩家

标签在玩家每次进服时，都会根据玩家客户端类型重新设置。

### 以玩家身份执行
- 接口类型：函数标签

- Java 版玩家进入：`#pcub:player_join/java`
- 基岩版玩家进入：`#pcub:player_join/bedrock`
- Java 版玩家每刻：`#pcub:player_tick/java`
- 基岩版玩家每刻：`#pcub:player_tick/bedrock`

- 原先旧版的标签路径现仍然兼容，在不久后将失效

注：进入执行由插件实现，比梦盘数据包提供的接口反应更快。每刻执行可能会在将来的更新中被弃用。

### 背包物品变动 / 主手槽位切换检测
- 接口类型：函数标签

在主手槽位切换、背包物品发生变化（包括但不限于移动位置、增减物品）以及打开容器界面（Bug）时触发，避免通过循环检测物品，减少占用。

- Java 版玩家：`#pcub:inventory_check/java`
- 基岩版玩家：`#pcub:inventory_check/bedrock`

注：这是一个异步执行接口，具有一定的延迟（目前内部实现方式是通过进度和插件事件监听，给予玩家实体标签，最后由每刻循环检测到此标签后触发）

### 使用物品执行命令 / 禁用原物品功能
- 接口类型：自定义物品 NBT

**示例**：给予自己一张纸，基岩版玩家使用时执行 `/help` 命令：  
`give @s minecraft:paper{PublicBukkitValues: {"pcub:run_command": "help"}}`

以下为标签详细用法，若无特殊说明均为可选项：
```
tag
  └─PublicBukkitValues
      ├─"pcub:run_command" 以玩家身份执行命令，所有权限和在聊天栏输入命令一致（类似书本/聊天栏中的 ClickEvent，但开头的斜杠 “/” 不是必需的）
      ├─"pcub:use_placeholder" 默认为 false，当值为 true 时，通过 PlaceholderAPI 插件实时解析执行命令中的占位符
      ├─"pcub:block_usage" 禁用物品原先的功能（比如书本、食物、钓竿），当提供执行命令后，默认为 true，否则默认为 false
      └─"pcub:bedrock_only" 默认为 true，当值为 false 时，以上功能在 Java 版上同样生效
```
注意：基岩版无法阻止书本打开，且会覆盖掉通过命令打开的其它界面！将其映射为 Geyser 自定义物品，即可避免此问题发生。

### 村民交互事件
- 接口类型：函数标签

玩家点击村民后，在打开交易界面前会先以村民身份执行标签 `#pcub:interact_villager/villager` 中的所有函数，然后再以玩家身份执行标签 `#pcub:interact_villager/player` 中的所有函数，如果玩家被添加实体标签 `ignoreTradeUI`，则阻止其打开交易界面，并删除该标签。

该功能目前应用于 1.21.30+ 交易修复，您也可以通过数据包在这些标签中附加一些需要在交易界面加载前执行的函数。

### 末影箱交互事件（打开/点击/关闭）
- 接口类型：函数标签

打开末影箱（玩家交互末影箱并打开界面后）时触发：`#pcub:chest_menu/open`

按下按钮（玩家记分项 `screen`>=0 且拿起/移动/丢出带有标签 `{clickable:1}` 的物品）时触发：`#pcub:chest_menu/click`

关闭末影箱时触发：`#pcub:chest_menu/leave`

注：如果只是单纯需要适配选装包，请将 `pcub` 命名空间改为 `pcub_add` 

### 在携带版 UI 档案下强制使用经典箱子界面，防止布局错乱
- 接口类型：容器（>=9 个槽位）标题

该方案已经在末影箱界面中实装，此外对副本保底便捷钱庄（插件 DLC）做了特别适配，当容器标题中带有 `掌上钱庄` 或 `元素锻炉` 字样（仅在显示为大箱的情况下），再或者带有 `Ender Chest` （仅在显示为小箱的情况下）时就会生效。

而如果您也有这样的需求，只需要给容器的标题后面加上 `:force_desktop_ui:` 字段即可。除非与个别第三方UI冲突，否则都能生效，且参数字段不会在界面中显示。

比如标题为 `测试容器:force_desktop_ui:` 的容器界面，在基岩版上将强制以经典 UI 布局呈现，标题显示为 `测试容器`。

该字段也可以用在 Geyser 自定义语言文件中，在容器标题中通过其本地化键名调用（注意是 Geyser 的语言文件，而不是客户端资源包的）。

已知 Bug：当容器标题以等号 `=` 开头时，即使没有参数字段也会强制经典布局，若有参数字段还会直接显示。

此外，发射器界面（装备锻造）也同样强制经典界面，不过暂不支持自定义。

### 本套件数据包卸载
- 接口类型：函数标签

执行命令 `/function pcub:uninstall` 后，本套件所增加的所有记分板项将会被清除。在此之前还会先执行 `#pcub:uninstall` 标签中附加的所有函数。
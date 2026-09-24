# 技术性说明（梦回盘灵专用 Java - 基岩双端互通套件）
作为套件使用说明的技术性问题补充，请先阅读使用说明。  
<!-- 强烈建议使用支持 Markdown 的阅读器查看此说明 -->  
<!-- 以下内容中所有命令均不包括`反引号 -->

## ⛏️ 配置服务端环境

### 核心配置

使用任意插件端（Bukkit 系）核心，游戏版本为 1.20.1。

**推荐使用 Spigot 作为服务端核心，能最大程度减少对原版特性的影响**。

如果您需要使用 Paper 插件或者在 Android 平台上开服，建议使用 Leaves 核心，在 Paper 的基础上尽可能地修复被破坏的原版特性。（稳定性及兼容性比 Spigot 略高，但原版特性的完整性不及 Spigot）

### 插件配置

以下插件为实现互通的基本条件：

- [Geyser-Spigot](https://geysermc.org/)：间歇泉，互通实现插件
- [ViaVersion](https://www.spigotmc.org/resources/viaversion.19254/)：由于 Geyser 紧随最新 Minecraft 原版更新，而当前梦回盘灵版本仍然在 1.20/1.20.1，故需要此插件向上兼容

以下插件为必需：

- [CrossPlatForms](https://www.spigotmc.org/resources/crossplatforms.101043/)：原先在基岩版不支持的交互书（如菜单书），通过此插件的 Form 表单界面重新制作
- [PlaceHolderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)：为 Form 表单界面的动态内容提供重要支持
  - 此外还需要安装子组件 `player`、`scoreboardobjectives`

为了使基岩版正常叠放丹药，至少需要能将药水 `potion`、喷溅药水 `splash_potion` 的叠放上限提高至 64 的自定义物品叠放插件。以下**仅为推荐插件**，而非指定要求。

经过测试的药水叠放插件：[PotionStacker v0.1.0](https://www.spigotmc.org/resources/potion-stacker.66168/)

如需自定义其它物品的叠放，也同样需要服务端的叠放插件支持。经过测试的，支持自定义叠放任何物品的插件：

[SimpleStack v1.3.8](https://www.spigotmc.org/resources/simple-stack-stack-any-items-to-64.83044/)
- 设置叠放数量后的物品在 Java 版无法双击合并
- 基岩版或 Java 版数字键，在快捷栏中移动物品，自定义叠放会失效
- 在 Spigot 下使用漏斗可能会出现刷物品，甚至影响红石系统等 Bug，经测试在 Paper / Leaves 下不会发生

SimpleStack v2.0.0#91 / 8d7d6dd 测试版
- 相比 v1.3.8，物品移动暂未发现有任何问题
- 请从本套件和服务端部署包的下载链接（见使用说明）中获取，或自行构建，因为在官方唯一下载途径 —— [仓库的 Action](https://github.com/Mikedeejay2/SimpleStackPlugin/actions) 中只能下载近期的版本
- 对于 Java 环境较为挑剔，部分 Java 环境下无法加载。且需要使用 JDK 开服才能正常运行，不支持 JRE！

[StackableItems v1.3.0](https://dev.bukkit.org/projects/stackableitems)：
- 和 SimpleStack v1.3.8 存在同样问题，但 Java 版可以双击合并物品
- 小概率会出现回弹等情况 (如交换槽位)，在创造模式背包下操作甚至还可能导致物品被误吞

本套件默认支持基岩版将以下物品最高叠放至 64 个：
- 药水：`POTION`
- 喷溅药水：`SPLASH_POTION`
- 雪球：`SNOWBALL`
- 蘑菇煲：`MUSHROOM_STEW`
- 兔肉煲：`RABBIT_STEW`

如果未通过插件配置叠放蘑菇煲（佛跳墙、万春羹）、兔肉煲，请不要将这些食物强制叠放后食用， **会一次性吃光！且只有食用一个的饱和度！**

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
### 启动参数

如果您使用 Spigot 核心且未安装 Floodgate 插件，基岩版玩家可能无法打开菜单书。这是 CrossPlatForms 的兼容性问题，可以在启动命令上添加此参数以解决：  
`-Djdk.util.jar.enableMultiRelease=force`

### 额外优化 - 掉落物 / 经验值合并（可选）

由于插件端默认配置的掉落物 / 经验值合并机制与原版略有不同，可能会影响游戏体验。建议到 `spigot.yml` 中作如下修改，使其恢复原版机制：
```yml
world-settings:
  default:
    merge-radius:
      item: -1.0
      exp: -1.0
```
### 额外优化 - 玩家碰撞（可选）

因 Geyser 的远古 Bug，基岩版玩家间无碰撞，**但可以推开 Java 版玩家，反之则不行**，这可能影响了 Java 版玩家的游戏体验。

可以通过以下命令，禁用**所有玩家**之间的碰撞（仅进入盘古大陆后）：  
- 禁用：`/team modify normal collisionRule pushOwnTeam`
- 恢复默认：`/team modify normal collisionRule always`

需要注意，上述命令并不能解决战役场景下的玩家碰撞问题。因为战役使用另外的两个队伍 `attack` 和 `defence` 有特殊的碰撞规则，直接修改会影响玩法。若使用 Paper 或在其基础上再开发的服务端核心，可以在服务端根目录下的 `config/paper-global.yml` 中作如下修改：
```yaml
collisions:
  enable-player-collisions: false
```
这是 Paper 提供的禁用玩家间碰撞功能，不影响现有队伍原有的设置，以及队伍中其它生物碰撞



## 💿 安装/更新本套件

本套件的所有组成部分（包括但不限于专用的插件、数据包及配置文件），在压缩档中已按标准服务端根目录结构存放到 `PanGuContinentUnbounded-server` 文件夹中。

将该根目录结构**合并到现有服务端根目录中**即可，根目录名称不限。



## ☢️ 选装数据包对游戏内容所造成的影响

为了进一步确保基岩版玩家的游戏体验，本套件的选装数据包会对存档中以下数据进行修改，这在将来可能会对某些 DLC 内容造成影响：

如果以下变动对某些 DLC 内容造成影响，请到交流社区反馈，或禁用选装包：  
`/datapack disable "file/pcub_add.zip"`

### 弓箭手武器技能

如果您安装了 Floodgate（基岩版登录优化）插件，则会导致基岩版玩家的 UUID 格式与 Java 版玩家不同，通过该插件登录的基岩版玩家其 UUID 通常为`00000000-0000-0000-xxxx-xxxxxxxxxxxx`（x 代表玩家的 Xbox ID，即 XUID）

由于前段值都为 0，而梦回盘灵数据包中弓箭手武器技能【日落九天·落日】的运行正是通过检测前段 UUID，这就意味着，所有通过 Floodgate 登录且未绑定 Java 版账户的基岩版玩家，都将无法正常使用这个技能。

选装包通过覆盖数据包相关函数，将检测 UUID 改为末端以修复此问题。具体修改详见选装包中的 `data/pcub/system/archer_damage/weapon_skill/bow6/fall_sun` 目录。

### 钱庄末影箱系统

选装包基于插件事件监听实现了更敏捷高效的按钮操作检测，覆盖取代掉原有的循环检测函数，从而优化游戏性能。这并不属于 Bug 修复。

如果您的游戏内容（DLC）对末影箱菜单功能有所修改，请不要使用选装包，或者将其中的以下文件或目录删除：  
- `data/pcub/tags/functions/chest_menu/`
- `data/pld/functions/system/chest_menu/tick_players.mcfunction`

再或者，若您希望适配选装包，请在您自己的数据包中添加函数标签 `#pcub_add:chest_menu/<open|click|leave>` 并在其中定义相关函数。



## ⚙️ 使用非插件服务端（如模组端、代理端）

目前只能使用 `plugins/Geyser-Spigot` 文件夹中的内容，这里包含了 Geyser 所有版本通用的配置文件及资源包，除此之外的其它功能（包括数据包）暂时只考虑到插件端，也就是说在这些平台上无法实现菜单书、投掷限制等部分针对基岩版的修复或优化。（CrossPlatforms 菜单支持其它平台，但失去本套件专用插件和数据包后只能通过命令打开）

此外还有部分功能（如基岩版颜色映射、交易次数溢出修复）依靠核心组件 (PCUB-Core.jar) 实现，其不依托于插件生态，但部分环境可能需要修改其[源代码](https://github.com/BUGTeas/pcub-core)自行适配。



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
- 接口类型：物品 NBT

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
- 接口类型：投掷/发射器（始终启用）；箱子类容器（标题）

该方案已经在末影箱界面中实装，此外对副本保底便捷钱庄（插件 DLC）做了特别适配，当容器标题中带有 `掌上钱庄` 或 `元素锻炉` 字样（仅在显示为大箱的情况下），再或者带有 `Ender Chest` （仅在显示为小箱的情况下）时就会生效。

而如果您也有这样的需求，只需要给容器的标题后面加上 `:force_desktop_ui:` 字段即可。除非与个别第三方UI冲突，否则都能生效，且参数字段不会在界面中显示。

比如标题为 `测试容器:force_desktop_ui:` 的容器界面，在基岩版上将强制以经典 UI 布局呈现，标题显示为 `测试容器`。

该字段也可以用在 Geyser 自定义语言文件中，在容器标题中通过其本地化键名调用（注意是 Geyser 的语言文件，而不是客户端资源包的）。

已知 Bug：当容器标题以等号 `=` 开头时，即使没有参数字段也会强制经典布局，若有参数字段还会直接显示。

此外，发射器界面（装备锻造）也同样强制经典界面，不过暂不支持自定义。

### 本套件数据包卸载
- 接口类型：函数标签

执行命令 `/function pcub:uninstall` 后，本套件所增加的所有记分板项将会被清除。在此之前还会先执行 `#pcub:uninstall` 标签中附加的所有函数。
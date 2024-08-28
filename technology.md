# 面向有技术经验的腐竹的说明
阅读此技术性说明前请先阅读使用说明。

## 基本原理

本方案默认使用 Spigot 1.20.1 插件服务端核心，并使用以下插件：
1. Geyser-Spigot (https://geysermc.org/)：  
	间歇泉，互通实现插件
2. ViaVersion (https://www.spigotmc.org/resources/viaversion.19254/)：  
	由于 Geyser 紧随最新 Minecraft 原版更新，而当前梦回盘灵版本仍然在 1.20/1.20.1，故需要此插件向上兼容
3. CrossPlatForms (https://www.spigotmc.org/resources/crossplatforms.101043/)：  
	原先在基岩版不支持的菜单书，将通过此插件做成 Form 表单界面
4. PlaceHolderAPI (https://www.spigotmc.org/resources/placeholderapi.6245/)：  
	为 Form 表单界面的动态内容提供重要支持，  
	此外还需要安装子组件 `player`、`scoreboardobjectives`

除非使用服务端部署包，上述插件均需自行下载安装！

此外，本方案自带一个特制专用插件，即“PCUB.jar”（“PCUB”为盘灵无界英文“Pan Gu Continent Unbounded”的缩写），提供以下功能：  
1. 修复基岩版叠放丹药移动，可以像 Java 版一样按 Shift 或触屏双击移动，雪球（退火符、击退符）、蘑菇煲（佛跳墙、万春羹）及兔肉煲同样支持
2. 修复因 Geyser 诡异 Bug 导致交易点一下会连续交易多次（牺牲了基岩版按 Shift 一次性交易全部的功能，不影响 Java 版）
3. 允许玩家通过手持物品打开菜单书等表单界面，  
	内容创作者也可以创建带有标签 `{PublicBukkitValues: {"pcub:run_command": "<命令>"}}` 的空书（minecraft:book）实现基岩版玩家右键执行特定命令，  
	比如手持物品 `{id: "minecraft:book", tags: {PublicBukkitValues: {"pcub:run_command": "help"}}}` 右键，玩家就会执行命令 `/help`，  
	该功能仍在完善，请慎用（Java 版也即将支持敬请期待）
4. 为优化修改项提供伪叠放转换功能
5. 强制叠放背包所有物品：不同于常见的叠放插件，它通过读取NBT标签字符串整理物品，完美兼容盘灵

自带命令 `/pcub`，更多功能请自行探索，计划未来开源

在本压缩档中已经存在服务器目录结构，以“PanGuContinentUnbounded-server”作为服务器根目录（名称可自行更改，安装其他组件时能够理解文档的意思就行），里面除了专用数据包、插件以及用于基岩版的资源包外，还有一些已经调好的必要配置文件。

## 有关叠放插件

由于特制专用插件对叠放的修复仅限于基岩版 Shift/双击移动/交易，丢出捡回等操作仍可能会被打散，强烈建议搭配叠放插件使用！
以下为经过测试的叠放插件：
1. PotionStacker (https://www.spigotmc.org/resources/potion-stacker.66168/)：
	最简单易用，是快速部署包中默认启用的叠放插件，但只支持药水的叠放
2. SimpleStack (https://github.com/Mikedeejay2/SimpleStackPlugin)：
	该插件支持任意物品的超量叠放，可自由配置白名单/黑名单模式。
	注意：请不要在 spigotmc.org 中下载它，提供的版本非常旧，不支持 1.20，
	上方的 GitHub 仓库现仍保持更新，提供了最新的 Dev 快照版本，可以在 Action 中找到。且部分版本存在无法加载的Bug，建议从服务端部署包中拿出经过我测试过的版本。
	该插件需要使用完整的 Java JDK 开服才能正常运行，不支持 JRE！

建议为以下物品设置叠放（不建议超过 64 个）：
- 药水：`POTION`
- 喷溅药水：`SPLASH_POTION`
- 雪球：`SNOWBALL`
- 蘑菇煲：`MUSHROOM_STEW`
- 兔肉煲：`RABBIT_STEW`

其他的插件我暂未测试，可以自行尝试，若出现Bug请向我反馈。

如果未通过插件配置叠放蘑菇煲（佛跳墙、万春羹）、兔肉煲，请不要将这些食物强制叠放后食用， **会一次性吃光！且只有食用一个的饱和度！**

## 在携带版UI档案下强制使用经典箱子界面，防止布局错乱

该方案已经在末影箱界面中实装，此外对副本保底便捷钱庄（插件 DLC）做了特别适配。而如果您也有这样的需求，只需要给容器的标题后面加上`:force_desktop_ui:`字段即可，比如`测试容器:force_desktop_ui:`。该字段也可以用在 Geyser 自定义语言文件中，在容器标题中通过其本地化键名调用（注意是 Geyser 的语言文件，而不是客户端资源包的）。除非与个别第三方UI冲突，否则都能生效，且参数字段不会在界面中显示。

## 使用非 Spigot 服务端

如果您不能独立分析一些 Bug 的根源所在，请不要使用非 Spigot 核心，**尤其是 Paper 及其分支**。

如果您希望将互通方案用在非插件平台上（例如 Fabric），则只能使用 `plugins/Geyser-Spigot` 文件夹中的内容，这里包含了 Geyser 所有版本通用的配置文件及资源包，除此之外的其它功能（包括数据包）均只为插件端设计，也就是说在这些平台上无法实现菜单书、基岩版丹药叠放（仅依靠JE叠放模组在基岩版下不能完美生效）等部分针对性优化。除此之外，所有的选装组件除非特殊说明外，均不支持非插件平台。
# 梦回盘灵 Java - 基岩双端互通套件
v1.5.0 先行版（适用于梦回盘灵扩展包 v1.1.0b）
<!-- 强烈建议使用支持 Markdown 的阅读器查看此说明。 -->
<!-- 以下内容中所有命令均不包括`反引号 -->

## 📖 简介

本套件隶属于盘灵无界项目，原名“盘灵无界-基础必要组件”。以 GeyserMC（间歇泉）为基础，集成基岩版专用资源包以及一系列深度优化适配，为希望体验「盘灵古域」地图及其一系列内容的基岩版玩家开启一道大门。

本套件不会修改或增添游戏内容，但你也可以添加绝大多数基于数据包/插件的 DLC 游戏内容扩展，部分 DLC 也受盘灵无界项目的官方支持（如便捷元素银行、罪如歌），以确保基岩版上也能完美游玩。



## ⚙️ 基本功能

- 为梦回盘灵扩展包适配 Geyser，并移植基岩端资源包
- 为基岩版重制了菜单书，避免因基岩版不支持书本按钮交互而无法正常游戏
- 装备锻造、末影箱菜单不受基岩版设置影响，强制经典 UI 布局，避免布局错乱
- 防止玩家在冒险模式下摘花盆里的花或食用地图上的蛋糕

以下功能可通过基岩版菜单书或 `/pcub` 命令自由配置：

- 可以禁用丹药/雪球的连续投掷，或者设置其间隔，以避免误触造成的损失 ¹
- 战士可以设置潜行一键发动技能，无需右键/长按，从而简化武器技能的触发方式 ¹
- 强制叠放背包所有物品：不同于常见的叠放插件，它通过读取NBT标签字符串整理物品，完美兼容盘灵

¹：Java 版需手动配置，若安装有 Floodgate 插件，则默认在 Android/iOS/WP 平台的基岩版上启用，否则默认所有基岩版启用。



## ⚠️ 注意事项

- 本套件为**免费获取**，通过付费获取到的则说明您已上当！您可以使用本方案进行任何自由创作，但严禁作为任何商业用途，以及在**与「盘灵古域」地图无关的场景**使用本压缩档中的资源包
- 请尊重原作者，本方案所有组件均不集成**盘灵古域地图**及**梦回盘灵扩展包**，请自行下载



## ♻️ 前置 / 依赖关系

（需要依赖的部分 -> 被依赖的部分）

- 梦回盘灵 Java - 基岩双端互通套件 -> 梦回盘灵扩展包 v1.1.0b
- 梦回盘灵扩展包 v1.1.0b -> 适用于 Minecraft 1.8.3 的盘灵古域地图 v1.2.x  



## 🚫 不兼容的扩展

### 原盘灵无界-优化修改选装组件

本套件此次更新已经在 Geyser 底层上实现了不少修复，原优化修改组件至此被弃用。仍有意义的功能**已作为选装数据包整合到本套件**。

若您正在现有服务端上更新，此前安装过优化修改组件，请在**更新后先启动一次服务端，等待 NPC 刷新复原完毕后关闭**，之后删除以下文件以将其卸载：

- pcub_mod_merge
- world/datapacks/pcub_mod.zip
- plugins/Geyser-Spigot/custom_mappings/pcub_mod.json
- plugins/Geyser-Spigot/packs/PanlingModificationPack.zip

注意是**先启动一遍再删除，否则将不会自动刷新 NPC**。此时可以使用命令 `/function pcub:uninst_pcub_mod/npc` 或者在游戏内挨个推出 NPC 以完成刷新。



## 💿 安装使用

本套件依赖于插件服务端环境运行。供本套件使用的服务端环境搭建**详见同一目录下的技术性说明**。若您不会配置插件，也可以下载预制的服务端部署包，**配合本套件使用**：

- [123 云盘](https://www.123pan.com/s/0nHvjv-5TjHh.html)
- [百度网盘](https://pan.baidu.com/s/1gK1qgdgUyXBuYXvRCRV2ug?pwd=m6i7)



## 🧩 选装功能

为了进一步优化基岩版玩家体验，本套件**附带选装数据包并默认启用**，通过叠加在梦回盘灵数据包之上，覆盖部分程序代码，实现以下功能：

- 末影箱菜单系统改为纯事件监听，彻底优化提升其性能
- 解决安装 Floodgate（基岩版登录优化）插件后，弓箭手武器技能【日落九天·落日】在基岩版使用异常

如需禁用，请使用命令禁用选装包：`/datapack disable "file/pcub_mod.zip"`



## ⚠️ 特殊说明

**本套件暂时内置魔改版 Geyser**。版本为 2.6.0-SNAPSHOT (git-pcub-temp-feature-da681f0)，魔改版本克隆自官方仓库，独立分支并同样开源：[BUGTeas/Geyser](https://github.com/BUGTeas/Geyser/tree/pcub-temp-feature)

盘灵无界现正将部分修复和优化结合到 Geyser 底层上，我最近常通过 Issues 和 Pull Request 积极向 Geyser 官方反馈问题并提供帮助，助力改善所有互通服的游戏体验。例如部分音效修复以及药水效果显示，且

在此前一直是通过**优化修改选装套件**，对梦盘数据包内容修改实现修复，然而这些问题的根源在于 Geyser，这样的做法不仅治标不治本，还影响了兼容性。近期盛行的“万通货斋” DLC 内容扩展也正是因此，迟迟未能支持互通套件。

计划摆脱对优化修改选装套件的依赖的，还有药水颜色和药水叠放实现。然而，此时 Geyser 官方正在开发新的[自定义物品映射接口 V2](https://github.com/GeyserMC/Geyser/pull/5189)，以跟进 Java 版 1.21.4 更高级的物品模型实现，因此由我提供的自定义叠放解决方案暂未被 Geyser 官方采纳：[GeyserMC/Geyser#5241](https://github.com/GeyserMC/Geyser/pull/5241)

但盘灵无界无界项目不能因此而停滞。在 Geyser 官方版本实现相关功能前，此套件中将内置魔改版 Geyser，为之后的更新提供更好的条件。属于临时过渡方案，并不代表最终效果，且不少东西仍待完善，这也是为什么当前版本会被划分为先行版的主要原因。



## 💡 官方交流社区

反馈问题、DLC 支持、茶水闲聊：

- [腾讯频道](https://pd.qq.com/s/v8t170qb)
- [KOOK](https://kook.vip/KJ7Zlx)



## 🔗 友情链接

- 梦回盘灵官方 KOOK
  - [扩展包及相关 DLC 下载，可能满员，但正常浏览](https://www.kookapp.cn/app/channels/5787377656427081)
- 梦回盘灵扩展包 v1.1.0b 下载
  - [蓝奏云](https://wwrc.lanzoub.com/iZYK8149zyod)
  - [百度网盘](https://pan.baidu.com/s/1y5NuvDD6APhsDcx10VkDBQ?pwd=plgy)
- 梦回盘灵安装教程
  - [B 站视频](https://www.bilibili.com/video/bv14Q4y127wv)
- 1.8.3 盘灵古域地图下载
  - [官方网站（魔法访问）](http://pan-gu-continent.blogspot.tw/)
  - [贴吧补档](https://tieba.baidu.com/p/6132497097)



## ❓ 常见问题

### Wiki 文档

目前尚未完成，但您可以参考幻域无界服务器的游玩指南的部分内容（多数内容已过时），或许能够解决您的疑惑：(两个地址都指向同一页面，部分网络环境打不开可尝试另一个)
- (https://bugtea.of.kg/item.html?article=fantasy-continent-unbounded/documents/bedrock.md)
- (https://bugteas.github.io/item.html?article=fantasy-continent-unbounded/documents/bedrock.md)

### 玩家间碰撞修复

因 Geyser 的远古 Bug，基岩版玩家间无碰撞，**但可以推开 Java 版玩家，反之则不行**，这可能影响了 Java 版玩家的游戏体验。

可以通过以下命令，禁用**所有玩家**之间的碰撞（仅进入盘古大陆后）：`/team modify normal collisionRule pushOwnTeam`

恢复默认碰撞：`/team modify normal collisionRule always`



## ❇️ 开源

为了促进盘灵无界项目的发展，本套件的源代码对外公开，您可以从 [GitHub](https://github.com/BUGTeas/panling-pcub) 或 [Gitee](https://gitee.com/BugTeaON/panling-pcub) 同步更新的仓库上获取到最新的源代码，其中带有构建说明，可自行构建测试。

当然如果您有不错的想法，也可发起 Issues 或 PR 或者在官方交流社区中直接反馈
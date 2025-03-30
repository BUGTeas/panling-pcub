## 📖 简介

本套件隶属于盘灵无界项目，以 GeyserMC（间歇泉）为基础，集成基岩版专用资源包以及一系列深度优化适配，为希望在基岩版体验「盘灵古域」地图及其一系列内容的玩家开启一道大门。

本套件不会修改或增添游戏内容，但你也可以添加绝大多数基于数据包/插件的 DLC 游戏内容扩展，部分 DLC 也受到盘灵无界项目的额外支持（如：罪如歌、万通货斋、便捷元素银行），以确保基岩版上也能完美游玩。（加入交流社区了解更多）



## ⚙️ 基本功能

- 为梦回盘灵扩展包适配 Geyser，并移植基岩端资源包
- 为基岩版重制了菜单书，避免因基岩版不支持书本按钮交互而无法正常游戏
- 实现基岩版所有基于胡萝卜钓竿和记分板的可触发物品（如箭袋、炼丹炉）的副手使用
- 装备锻造、末影箱菜单不受基岩版设置影响，强制经典 UI 布局，避免布局错乱
- 防止玩家在冒险模式下摘花盆里的花或食用地图上的蛋糕
- 防止基岩版触控或 Java 版一键整理模组在 1 Tick 内快速移动末影箱菜单按钮，导致其数据读取错误而被意外清空

以下功能可通过基岩版菜单书或 `/pcub` 命令自由使用或调整：

- 可以禁用丹药/雪球的连续投掷，或者设置其间隔，以避免误触造成的损失 ¹
- 战士可以设置潜行一键发动技能，无需右键/长按，从而简化武器技能的触发方式 ¹
- 强制叠放背包所有物品：不同于常见的叠放插件，它通过读取NBT标签字符串整理物品，完美兼容盘灵

¹：Java 版需手动配置，若安装有 Floodgate 插件，则默认在 Android/iOS/WP 平台的基岩版上启用，否则默认所有基岩版启用。



## ⚠️ 注意事项

- 本套件为**免费获取**，通过付费获取到的则说明您已上当！您可以使用本套件进行任何自由创作，但严禁作为任何商业用途，以及在**与「盘灵古域」地图无关的场景**使用本压缩档中的资源包
- 请尊重原作者，本套件不集成**盘灵古域地图**及**梦回盘灵扩展包**，请自行下载



## ♻️ 前置 / 依赖关系

- 梦回盘灵：灵域破晓 1.1.0b
  - 适用于 Minecraft 1.8.3 的「盘灵古域」地图 v1.2.x  



## 🚫 不再兼容原“盘灵无界-优化修改选装组件”

本套件此次更新已经在 Geyser 底层上实现了不少修复，原优化修改组件至此被弃用。仍有用处的功能**已作为选装数据包整合到本套件**。

若您正在现有服务端上更新，此前安装过优化修改组件，请在**更新后先启动一次服务端，等待 NPC 刷新复原完毕后关闭**，之后删除以下文件以将其卸载：

- pcub_mod_merge
- world/datapacks/pcub_mod.zip
- plugins/Geyser-Spigot/custom_mappings/pcub_mod.json
- plugins/Geyser-Spigot/packs/PanlingModificationPack.zip

注意是**先启动一遍再删除**，否则将需要在游戏内手动推出 NPC，或使用此命令才能完成刷新：  
`/function pcub:uninst_pcub_mod/npc`



## 💿 安装使用

本套件依赖于插件服务端环境运行。最简单的方法便是使用现成的服务端部署包，**配合本套件使用**，它和本套件放在了同一个下载链接中，很容易被找到。
若您希望自行搭建服务端环境，**详见同一目录下的技术性说明**。

### 通用视频教程

<iframe src="//player.bilibili.com/player.html?bvid=BV1V7QVY8E4t&page=1" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"> </iframe>



## 🧩 选装功能

为了进一步优化基岩版玩家体验，本套件**附带选装数据包并默认启用**，通过叠加在梦回盘灵数据包之上，覆盖部分程序代码，实现以下功能：

- 末影箱菜单系统改为纯事件监听，彻底优化提升其性能
- 解决安装 Floodgate（基岩版登录优化）插件后，弓箭手武器技能【日落九天·落日】在基岩版使用异常

如需禁用，请使用命令禁用选装包：`/datapack disable "file/pcub_add.zip"`



## ⚠️ 本套件暂时内置魔改版 Geyser

**支持 1.21.40 - 1.21.7x 的基岩版客户端**，版本为 2.6.2-SNAPSHOT (git-pcub-temp-feature-cc1a6d4)。

魔改版本克隆自官方仓库，独立分支并同样开源：[BUGTeas/Geyser](https://github.com/BUGTeas/Geyser/tree/pcub-temp-feature)

盘灵无界现正将部分修复和优化结合到 Geyser 底层上，我最近常通过 Issues 和 Pull Request 积极向 Geyser 官方反馈问题并提供帮助，助力改善所有互通服的游戏体验。例如部分音效修复以及药水效果显示。

这些问题在此前一直是通过**原优化修改选装组件**对梦盘数据包内容修改实现修复，然而这些问题的根源在于 Geyser，这样的做法不仅治标不治本，还影响了兼容性。也正因如此直到近期才实现了对 “万通货斋” DLC 的兼容。

计划摆脱对优化修改选装套件的依赖的，还有药水颜色和药水叠放实现。然而，此时 Geyser 官方正在开发新的[自定义物品映射接口 V2](https://github.com/GeyserMC/Geyser/pull/5189)，以跟进 Java 版 1.21.4 更高级的物品模型实现，因此由我提供的自定义叠放解决方案暂未被 Geyser 官方采纳：[GeyserMC/Geyser#5241](https://github.com/GeyserMC/Geyser/pull/5241)

但盘灵无界项目不能因此而停滞。在 Geyser 官方版本实现相关功能前，此套件中将内置魔改版 Geyser，为之后的更新提供更好的条件。属于临时过渡方案，并不代表最终效果，且不少东西仍待完善，这也是为什么当前版本会被划分为先行版的主要原因。



## 💡 盘灵无界交流社区

反馈问题、DLC 支持、茶水闲聊：

- [腾讯频道](https://pd.qq.com/s/v8t170qb)
- [KOOK](https://kook.vip/KJ7Zlx)



## 🔗 友情链接

梦回盘灵官方 KOOK
- [扩展包及相关 DLC 下载，可能满员，但正常浏览](https://www.kookapp.cn/app/channels/5787377656427081)

梦回盘灵扩展包 v1.1.0b 下载
- [蓝奏云](https://wwrc.lanzoub.com/iZYK8149zyod)
- [百度网盘](https://pan.baidu.com/s/1y5NuvDD6APhsDcx10VkDBQ?pwd=plgy)

梦回盘灵安装教程
- [B 站视频](https://www.bilibili.com/video/bv14Q4y127wv)

1.8.3 盘灵古域地图下载
- [官方网站（魔法访问）](http://pan-gu-continent.blogspot.tw/)
- [贴吧补档](https://tieba.baidu.com/p/6132497097)



## ❓ 疑难解答

### 基岩版常见问题解决方法

已经整理为视频并发布至 B 站，涵盖了 UI 错乱、交易项查看、乱跳等常见的一系列问题及解决方法：
<iframe src="//player.bilibili.com/player.html?bvid=BV16WQWYKE6V&page=1" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"> </iframe>

### 玩家间碰撞修复

因 Geyser 的远古 Bug，基岩版玩家间无碰撞，**但可以推开 Java 版玩家，反之则不行**，这可能影响了 Java 版玩家的游戏体验。

可以通过以下命令，禁用**所有玩家**之间的碰撞（仅进入盘古大陆后）：  
`/team modify normal collisionRule pushOwnTeam`

恢复默认碰撞：`/team modify normal collisionRule always`



## 🎆 特别鸣谢

盘灵无界项目的发展，离不开他们的大力支持和帮助！

- **xzseerl / xz_seer**：为基岩端资源包提供了物品模型、武器动画方面的帮助和技术支持 [bilibili](https://space.bilibili.com/449714964)

这个名单并非一成不变，不知到何时，这里会多出新的几行 )



## ❇️ 开源

为了促进盘灵无界项目的发展，本套件的源代码对外公开，您可以从 [GitHub](https://github.com/BUGTeas/panling-pcub) 或 [Gitee](https://gitee.com/BugTeaON/panling-pcub) 同步更新的仓库上获取到最新的源代码，其中带有构建说明，可自行构建测试。

当然如果您有不错的想法，也可发起 Issues 或 PR 或者在官方交流社区中直接反馈
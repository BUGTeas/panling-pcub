# 盘灵无界（梦回盘灵基岩互通开服方案）基础必要组件
v1.1 正式版（适用于梦回盘灵扩展包 v1.1.0b）

## 注意事项
1. 您可以使用本方案进行任何自由创作，但严禁作为任何商业用途，以及在**与「盘灵古域」地图无关的场景**使用本压缩档中的任何文件
2. 请尊重原作者，本方案所有组件均不集成**盘灵古域地图**及**梦回盘灵扩展包**，请自行下载
3. 因使用此开服方案造成的任何 Bug，「梦回盘灵」原作者概不处理，请通过交流社区反馈
4. 请先使用原版 1.20/1.20.1 客户端将地图与数据包内容加载完毕后，再放入服务端
5. 解决基岩版中，丹药属性及颜色不显示，以及武器技能音效异常，需要安装**优化修改选装组件**
6. 若想使用 Floodgate 插件接管基岩版玩家登录，请安装 **Floodgate 修复选装组件**，否则弓箭手六阶武器技能在基岩版上会出现异常。
7. 部分 DLC 内容扩展（如便捷元素银行、罪如歌）需要搭配对应的兼容选装组件，您可在交流社区中找到并下载
8.  如果所需的 DLC 需要专用的资源包，且没有对应的选装兼容组件，则暂不支持基岩版，但仍可加载供 Java 版玩家游玩（例如「万通货斋」，到时候就有兼容了等等吧？）

**旧版本升级过来的服主请注意，原 DLC 适配可选项现已不再兼容!** 如果您安装过，请删除以下文件将其卸载：（均在服务端根目录下进行）

- pcub_ext_merge
- plugins/Geyser-Spigot/custom_mappings/pcub_ext.json
- plugins/Geyser-Spigot/packs/PanlingExtensionPack.zip
- world/datapacks/pcub_ext.zip

请根据实际需求安装针对扩展内容的互通兼容选装组件（适用于便捷元素银行、罪如歌的兼容组件可直接在网盘下载）

## 依赖关系（需要依赖的部分 -> 被依赖的部分）
盘灵无界-基础必要组件 v1.1 -> 梦回盘灵扩展包 v1.1.0b  
梦回盘灵扩展包 v1.1.0b -> 适用于 Minecraft 1.8.3 的盘灵古域地图 v1.2.x  
不兼容：盘灵无界 (DLC 适配) (已过时)  

## 友情链接
- 梦回盘灵官方 KOOK  
	(https://kook.top/9Mr5ZG)
- 梦回盘灵扩展包 v1.1.0b 下载  
	(https://wwrc.lanzoub.com/iZYK8149zyod)  
	(https://pan.baidu.com/s/1y5NuvDD6APhsDcx10VkDBQ?pwd=plgy)
- 梦回盘灵安装教程  
	(https://www.bilibili.com/video/bv14Q4y127wv)
- 1.8.3 盘灵古域地图下载  
	官方网站，魔法访问：(http://pan-gu-continent.blogspot.tw/)  
	授权补档：(https://tieba.baidu.com/p/6132497097)

--------------------------------------------------------------------------------

## 如果打算使用服务端部署包，请前往阅读部署包的使用说明，而可无视以下内容

--------------------------------------------------------------------------------

## 面向有技术经验的腐竹的说明

本方案默认使用 Spigot 1.20.1 插件服务端核心，并使用以下插件：
1. Geyser-Spigot (https://geysermc.org/)：  
	间歇泉，互通实现插件
2. ViaVersion (https://www.spigotmc.org/resources/viaversion.19254/)：  
	由于 Geyser 紧随最新 Minecraft 原版更新，而当前梦回盘灵版本仍然在 1.20/1.20.1，故需要此插件向上兼容
3. CrossPlatForms (https://www.spigotmc.org/resources/crossplatforms.101043/)：  
	原先在基岩版不支持的菜单书，将通过此插件做成 Form 表单界面
4. PlaceHolderAPI (https://www.spigotmc.org/resources/placeholderapi.6245/)：  
	为 Form 表单界面的动态内容提供重要支持，  
	此外还需要安装子组件 player、scoreboardobjectives

除非使用服务端部署包，上述插件均需自行下载安装！

此外，本方案自带一个特制专用插件，即“PCUB.jar”（“PCUB”为盘灵无界英文“Pan Gu Continent Unbounded”的缩写），提供以下功能：  
1. 修复基岩版叠放丹药移动，可以像 Java 版一样按 Shift 或触屏双击移动，雪球（退火符、击退符）、蘑菇煲（佛跳墙、万春羹）及兔肉煲同样支持
2. 修复因 Geyser 诡异 Bug 导致交易点一下会连续交易多次（牺牲了基岩版按 Shift 一次性交易全部的功能，不影响 Java 版）
3. 允许玩家通过手持物品打开菜单书等表单界面，  
	内容开发者也可以创建带有标签 {PublicBukkitValues: {"pcub:run_command": "<命令>"}} 的空书（minecraft:book）实现基岩版玩家右键执行特定命令，  
	比如手持物品 {id: "minecraft:book", tags: {PublicBukkitValues: {"pcub:run_command": "help"}}} 右键，玩家就会执行命令“/help”，  
	该功能仍在完善，请慎用（Java 版也即将支持敬请期待）
4. 为优化修改项提供伪叠放转换功能
5. 强制叠放背包所有物品：不同于常见的叠放插件，它通过读取NBT标签字符串整理物品，完美兼容盘灵

自带命令“/pcub”，更多功能请自行探索，计划未来开源

在本压缩档中已经存在服务器目录结构，以“PanGuContinentUnbounded-server”作为服务器根目录（名称可自行更改，安装其他组件时能够理解文档的意思就行），里面除了专用数据包、插件以及用于基岩版的资源包外，还有一些已经调好的必要配置文件。

## 有关叠放插件

由于特制专用插件对叠放的修复仅限于基岩版 Shift/双击移动/交易，丢出捡回等操作仍可能会被打散，强烈建议搭配叠放插件使用！
以下为经过测试的叠放插件：
1. PotionStacker (https://www.spigotmc.org/resources/potion-stacker.66168/)：
	最简单易用，是快速部署包中默认启用的叠放插件，但只支持药水的叠放
2. SimpleStack (https://github.com/Mikedeejay2/SimpleStackPlugin)：
	该插件支持任意物品的超量叠放，可自由配置白名单/黑名单模式。
	注意：请不要在 spigotmc.org 中下载它，提供的版本非常旧，不支持 1.20，
	上方的 GitHub 仓库现仍保持更新，提供了最新的 Dev 快照版本，可以在 Action 中找到。
	该插件需要使用完整的 Java JDK 开服才能正常运行，不支持 JRE！

建议为以下物品设置叠放（不建议超过 64 个）：
- 药水：POTION
- 喷溅药水：SPLASH_POTION
- 雪球：SNOWBALL
- 蘑菇煲：MUSHROOM_STEW
- 兔肉煲：RABBIT_STEW

其他的插件我暂未测试，可以自行尝试，若出现Bug请向我反馈。

如果未通过插件配置叠放蘑菇煲（佛跳墙、万春羹）、兔肉煲，请不要将这些食物强制叠放后食用， **会一次性吃光！且只有食用一个的饱和度！**

--------------------------------------------------------------------------------

上述所有第三方插件在服务端部署包中都有，若不方便下载可直接从其中获取  
如果您看不懂这部分内容，请直接使用服务端部署包  
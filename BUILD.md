# 自行构建盘灵无界基础必要组件

## 构建插件

**环境要求:** Java 17 JDK、Maven

1. 进入“Plugin”目录
2. 通过命令 `mvn package` 构建（或者使用 IDEA）
3. 成品 jar 文件位于：`target/PCUB-x.x.x.jar`

注意：目前插件仍未定型，变动可能较大，且未来将会被拆分。



## 打包数据包

### 生成交易修复所用到的函数文件

**环境要求:** Node.js

由于数据满足条件 `uses < maxUses - 2147483647` 的村民交易项（`Offers.Recipes[x]`）无法在基岩版 1.21.30+ 中使用触屏或 Shift 键交易，需要对其进行修改以实现修复，修复原理是将数据 `Offers.Recipes[x].uses` 设为 -2147483647，`Offers.Recipes[x].maxUses` 设为 0。这部分的数据包代码结构高度重复 (但凡梦盘支持 1.20.2，直接上宏函数那叫一个方便)，故使用 JS 脚本批量生成。在当前目录下执行命令 `node make-functions.js` 即可，当村民被玩家交互时，由 PCUB 插件在交易界面加载前执行数据包中的函数，对其前 24 个交易项进行自动检查修复，当然也可更改此脚本中的变量 `maxLength` 以更改修复数量。

脚本生成的函数位于 `DataPack/data/pcub/functions/bedrock_villager_fix/recipe` 目录下，如果不想麻烦也可以直接将成品包中的复制进去。

### 最后打包

将“DataPack”目录中的数据包打包，命名为“pcub.zip”



## 生成 Geyser 的语言文件：

**环境要求:** Node.js

1. 进入“LangFile”目录，将梦回盘灵资源包解压到“resources/panling”目录下
2. 安装依赖项：
   - (必要) `npm install git+https://gitee.com/BugTeaON/pcub-locale#v1.0.1` 或 `npm install git+https://github.com/BUGTeas/pcub-locale#v1.0.1`
   - (可选) 如果需要繁体转换，还需安装 [OpenCC](https://github.com/BYVoid/OpenCC) 转换器，否则输出的繁体文件（“zh_tw”及“zh_hk”）都将是简体内容：`npm install opencc`（由于对 `node-gyp` 的使用，安装条件较为苛刻，若无经验建议跳过）
3. 执行命令 `node index.js` 开始生成
4. 在“output/panling”目录下会出现以下文件夹：
   - assets（为 Java 客户端资源包语言文件，“.json”格式，可为 Java 版提供繁体支持）
   - overrides（**重要!** 为 Geyser 自定义语言文件，“.json”格式，放至 Geyser 配置目录的“locales”目录下，否则游戏内容乱码）
   - texts（为基岩客户端资源包语言文件，虽较少涉及游戏内容，但不可或缺）



## 修改资源包

资源包属于人工手动移植，且涉及到盘灵古域用户条款，和资源包动画框架作者 [xz_seer](https://space.bilibili.com/449714964) 的权利，故不以仓库形式提供源代码，可以直接从发布了的成品中薅过来。您可以在 `PanGuContinentUnbounded-server/plugins/Geyser-Spigot/packs` 中找到它们，虽然是 zip 格式，但其内部是标准的基岩版资源包结构。



## 配置文件

在源码文件夹中还包含有已经调整好的一些插件配置文件，它们都在“Config”目录下，且相对于标准服务端目录结构存放：
```
Config
└─plugins
    ├─CrossplatForms (CrossPlatForm 配置文件目录)
    │      bedrock-forms.yml (基岩版 Form 界面内容)
    │      config.yml (基础配置，以及命令映射，可以额外配置实现普通玩家执行高权命令)
    │
    └─Geyser-Spigot (Geyser 配置文件目录)
        │  custom-skulls.yml (为基岩版提供自定义头颅支持，包含盘灵地图中绝大多数的头颅信息)
        │
        └─custom_mappings (将部分物品/方块映射为Addon类型而非原版，从而在基岩版客户端中自定义模型、纹理)
                pcub.json (原版梦回盘灵中的映射内容)
```
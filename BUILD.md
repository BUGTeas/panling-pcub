# 自行构建盘灵无界基础必要组件

## 环境要求
1. Java 17 JDK（构建插件需要）
2. Node.js（生成语言文件）
3. 一个 ZIP 压缩工具（打包数据包）

## 构建步骤
1. 构建插件：编译“Plugin”目录下的插件，将输出 jar 文件命名为“PCUB.jar”放至服务端“plugins”目录下
2. 打包数据包：将“DataPack”目录中的数据包打包，命名为“pcub.zip”放至服务端“world/datapacks”目录下
3. 生成 Geyser 的语言文件：
   1. 进入“LangFile”目录，将梦回盘灵资源包解压到“resources/panling”目录下
   2. 使用 NPM 安装必要的依赖项，使用 Node.js 执行 langtool.js
   3. 将生成器目录下“output/panling/overrides”文件夹放至服务端“plugins/Geyser-Spigot/locales”目录下（细心的您可能会发现它还生成了 Java 以及基岩版的资源包语言文件，它们分别存放在了“assets”以及“texts”目录中，且带有繁体转换）
4. 将“Config”目录中的配置文件放至服务端对应目录下
5. 资源包属于人工手动移植，所以不存在源代码，可以直接从发布了的成品中薅过来：
   - PanGuContinentUnbounded-server/plugins/Geyser-Spigot/packs 中的所有 zip 文件就是资源包本体
6. 安装必要插件：详见使用说明
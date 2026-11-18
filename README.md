# Blue Skies — 1.20.4 (1.3.32) 反编译源码

<p align="center">Decompiled Java sources of <b>Blue Skies</b> for Minecraft 1.20.4 / NeoForge</p>

> **分支说明**：当前 `1.21.1` 分支是 NeoForge 1.21.1 的迁移分支，进度与待办见 [MIGRATION.md](MIGRATION.md)。`main` 分支保留 1.20.4 的忠实反编译存档。

> **这不是官方源码仓库。** 本仓库是对已发布构建产物 `blue_skies-1.20.4-1.3.32.jar` 的反编译存档，用于学习、逆向研究与移植参考。

## 版权与用途（重要）

| 项目 | 内容 |
| --- | --- |
| 模组名称 | Blue Skies |
| 作者 | KingPhygieBoo, Silver_David, Lachney |
| 官方仓库 | https://gitlab.com/modding-legacy/blue-skies （Modding Legacy） |
| 官方站点 | https://moddinglegacy.com/ |
| 许可证 | **All Rights Reserved**（原始 `mods.toml` 中的声明） |

本仓库全部代码的版权归原作者（Modding Legacy 及其作者）所有。此处存档仅供**个人学习、逆向分析与技术研究**，不用于再分发、不用于商业用途，也不代表原作者的任何立场。如果你是权利人并希望移除本仓库，请提交 issue 说明，我会立即处理。

## 存档对象

| 字段 | 值 |
| --- | --- |
| 文件名 | `blue_skies-1.20.4-1.3.32.jar` |
| 大小 | 83,428,216 字节 |
| SHA-256 | `2b26ab481791e9276322c7e9f3bb401150049840a61cb0ce7f8e84dc97dfe412` |
| 目标环境 | Minecraft 1.20.4 / NeoForge `[20.4.111-beta,)` |
| 必需依赖 | `structure_gel >= 2.17` |

## 反编译方法

- 运行时：Temurin JDK 17.0.21
- 反编译器：Vineflower 1.11.1（Fernflower 的维护分支）

```bash
# 1. 只解出字节码
unzip -q blue_skies-1.20.4-1.3.32.jar '*.class' -d classes
# 2. 反编译（-dgs=1 输出泛型签名）
java -jar vineflower.jar -dgs=1 classes out
```

结果：1046 个 `.class`（含内部类）→ **706 个 `.java`** 文件。

## 目录结构

```
src/main/java/com/legacy/blue_skies/     # 反编译出的 Java 源码
├── asm_hooks/                           # ASM 钩子（被 coremod 注入使用）
├── BlueSkies.java 等入口类
src/main/resources/
├── META-INF/mods.toml                   # 模组元数据（保留原样）
├── META-INF/accesstransformer.cfg       # 访问转换器
├── META-INF/coremods.json               # coremod 注册表
├── blue_skies.mixins.json               # Mixin 配置
├── pack.mcmeta
└── coremods/*.js                        # jar 内原始的 JS coremod（非反编译产物）
```

## 如何编译

原 jar 不包含构建脚本，因此本仓库只提供 `src`。需要自行准备 NeoForge 1.20.4 的 MDK：

1. 用 NeoForge 1.20.4 MDK（NeoGradle 7.x + Java 17）初始化工程；
2. 把本仓库的 `src/main/java`、`src/main/resources` 覆盖进去；
3. 依赖 `net.neoforged:neoforge:20.4.111-beta` 以上，并加入 `structure_gel`（见 [structure_gel 仓库](https://github.com/Klisuaiji/structure_gel)）；
4. `mods.toml` 里的 `modId` / `version` 改成自己的标识。

## 已知局限

反编译不能恢复全部原始信息，阅读时请注意：

- 局部变量名、参数名多为 `var` 系列推断名；lambda / 内部类的命名带 `lambda$...`、`$1` 等编译器痕迹；
- 部分泛型签名、注解参数、record/enum 结构、编译期常量折叠的结果与原码不同；
- `switch` 表达式、字符串拼接、try-with-resources 等会被还原成等价但结构不同的形式；
- 资源文件（`assets/`、`data/`、世界生成 JSON、贴图与音效）**未**包含在本仓库；
- `asm_hooks/` 与 `coremods/*.js` 的配合关系需自行对照运行行为验证。

## 致谢

所有设计与实现均属于 Blue Skies 原作者及 Modding Legacy 团队，本仓库仅为一份可读性存档。

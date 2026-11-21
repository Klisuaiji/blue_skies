> **重要法律声明**：本仓库为教育性质的反编译 + 移植研究产物，**绝非** Blue Skies™ 的官方仓库或分支。所有原始代码与资源的知识产权归 Modding Legacy 及原作者所有。本仓库以**保留所有权益（All Rights Reserved）**方式公开，仅供个人学习与研究。详见 [LICENSE](LICENSE)。

# Blue Skies — 1.21.1 NeoForge 移植版

[English README](README_EN.md)

> 这不是官方仓库。官方 Blue Skies 由 Modding Legacy 开发并发布于 [GitLab](https://gitlab.com/modding-legacy/blue-skies)。本仓库将 1.20.4 版（`blue_skies-1.20.4-1.3.32.jar`）反编译源码真实迁移到 Minecraft 1.21.1 / NeoForge，供技术研究与个人学习。

Blue Skies 是一个内容型模组，新增 **Everbright** 与 **Everdawn** 两个维度，包含完整的方块、物品、生物、结构、世界生成与进度体系。

## 移植状态

| 项目 | 内容 |
| --- | --- |
| 上游版本 | 1.20.4 / 1.3.32（反编译自发布 jar） |
| 目标环境 | Minecraft 1.21.1 / NeoForge 21.1.234+ |
| Java | 21 |
| 移植方式 | 逐模块真实 API 重写（非占位 shim） |
| 构建状态 | 迁移进行中，见仓库提交记录与 Issue |

## 环境要求

| 依赖 | 版本 |
| --- | --- |
| Minecraft | 1.21.1 |
| NeoForge | 21.1.234+ |
| Java | 21 |

## 构建

本项目使用 Gradle（NeoForge ModDev 插件）。仓库已配置 NeoForge 官方 CDN 镜像以加速依赖解析。

```bash
# 使用包装器构建（会自动下载 Gradle 8.11.1 与依赖）
./gradlew build

# Windows
gradlew.bat build

# 产物：build/libs/blue_skies-1.3.32-port0.1.0.jar
```

> 该 jar 内嵌 Structure Gel API（见 [structure_gel 仓库](https://github.com/Klisuaiji/structure_gel)），对应 `neoforge.mods.toml` 中的双 `[[mods]]` 声明。

## 目录结构

```
src/main/java/com/legacy/blue_skies/       # Blue Skies 源码（入口、方块、物品、实体、世界生成等）
src/main/java/com/legacy/structure_gel/    # 内嵌的 Structure Gel API 源码
src/main/resources/
├── assets/blue_skies/                     # 贴图、模型、音效、语言文件
├── data/blue_skies/                       # 配方、战利品、标签、世界生成 JSON
├── blue_skies.mixins.json                 # Mixin 配置
└── META-INF/neoforge.mods.toml            # 模组元数据（由模板生成）
src/main/templates/META-INF/neoforge.mods.toml
```

## 已知局限

- 源码由反编译还原，局部变量名、lambda / 内部类命名带有编译器痕迹；
- 泛型签名、注解参数、record/enum 结构与原始源码可能存在差异；
- 移植尚未完成，可能存在编译错误或未验证的运行时行为。

## 版权声明 (Copyright)

本仓库所有原始代码与资源的版权归 Modding Legacy 及原作者所有。移植工作 © 2026 Kulisaiji。
本仓库**不授予任何形式的开源许可（All Rights Reserved）**，仅供个人学习与研究。详见 [LICENSE](LICENSE)。
若您是版权持有人并希望移除本仓库，请提交 Issue，将立即处理。

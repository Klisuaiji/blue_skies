# 1.21.1 迁移状态（Blue Skies）

分支：`1.21.1`　目标：Minecraft 1.21.1 / NeoForge 21.1.x / Java 21

## 结论先说

本分支只完成了**工程层迁移**：Gradle 骨架、`neoforge.mods.toml`、Java 21、依赖版本、资源/数据包格式。`src/main/java`（706 个文件）**仍是 1.20.4 形态，没有做函数级迁移**。

原因有两条，都不是靠改名字能绕过的：

1. 本机无法访问 `maven.neoforged.net`（DNS 解析到不可达的 IPv6 地址），拿不到 NeoGradle 和 NeoForge 21.1 依赖，**无法编译验证**；
2. Blue Skies 的体量（两套维度、4 座地牢、自定义世界生成、13 个 JS coremod、10 个 ASM 钩子类）意味着 1.21.1 迁移必须是“边编译边修”的工程，盲改产出的是不可维护的代码。

逐处待办见 [`docs/migration-inventory.md`](docs/migration-inventory.md)。

## 已完成的改动

| 项 | 1.20.4 | 1.21.1 | 说明 |
| --- | --- | --- | --- |
| 元数据文件 | `META-INF/mods.toml` | `META-INF/neoforge.mods.toml` | NeoForge 1.20.5+ 改名 |
| loaderVersion | `[1,)` | `[4,)` | |
| NeoForge 依赖 | `[20.4.111-beta,)` | `[21.1,)` | |
| Minecraft 依赖 | `[1.20.4,1.21)` | `[1.21.1,1.21.2)` | |
| structure_gel 依赖 | `[2.17,)` | `[3.0,)` | 对应 structure_gel 分支里的 3.0.0 |
| 模组版本 | `1.3.32` | `2.0.0` | |
| Java | 17 | 21 | |
| `pack_format` | 10 | 48（兼容 34） | |
| 构建脚本 | 无 | `build.gradle` / `settings.gradle` / `gradle.properties` | |

## 代码层必须处理的问题

| 类别 | 命中 | 1.21.1 的替代方案 |
| --- | --- | --- |
| `getTag()` / `getOrCreateTag()` | 24 个文件 | 数据组件：`DataComponents.CUSTOM_DATA` / 自定义 `DataComponentType` |
| `Capability` / `LazyOptional` / `ICapabilityProvider` | 9 个文件 | `AttachmentType` + `IAttachmentHolder` |
| `Level.random` | 81 个文件 | `level.getRandom()` |
| `MobEffectInstance` | 23 个文件 | 传 `Holder<MobEffect>` |
| `CreativeModeTab` | 8 个文件 | `Output` + `DeferredItem` 注册 |
| `StructureType` | 4 个文件 | 1.21 的构造与 `Codec` 用法需比对源码 |
| `BiomeModifier` | 2 个文件 | NeoForge 21 的 biome modifier 注册与 codec 结构有调整 |
| `Registries` / `BuiltInRegistries` 取用 | 62 个文件 | 部分键改名或合并，需逐个核对 |

## 高风险项（比“改名”难一个量级）

1. **13 个 JS coremod + 10 个 ASM 钩子**：`coremods/*.js` 配合 `asm_hooks/*.java` 做字节码注入。NeoForge 在 1.20.5 之后不再支持 JS coremod，这批注入必须重写为 Mixin（`blue_skies.mixins.json` 已有配置，但覆盖范围与 coremod 不同）。这是本次迁移最大的工作量来源。
2. **两套维度**：`LevelStem` / `DimensionType` 的注册与 codec 在 1.21 有调整，且维度数据在 `data/` 下的 JSON，本仓库不含资源文件。
3. **4 座地牢**：拼图（jigsaw）结构与 `StructurePoolElement`、`JigsawPlacement` 的 API 变动会直接影响地牢生成。
4. **`accesstransformer.cfg`**：条目按 1.20.4 成员名写的，1.21.1 里失效会导致运行期 `IllegalAccessError`，必须逐条验证。
5. **Mixin 目标**：`livingentity`、`levelrenderer`、`player` 等注入点在 1.21 的方法签名有变动（渲染管线在 1.20.5 之后重写较多）。

## 建议的推进方式

1. 先迁 structure_gel 到能编译（它是 Blue Skies 的依赖，且只有 208 个文件）；
2. 拉本分支，用 NeoForge 1.21.1 MDK 对齐 `neo_version` / 插件版本，`gradle build` 拿首批错误；
3. 按 `docs/migration-inventory.md` 分类清：NBT → Capability → `Level.random` → 渲染/网络 → 世界生成；
4. coremod 那 13 个文件单独排期，按 Mixin 重写，不要试图“修”它们。

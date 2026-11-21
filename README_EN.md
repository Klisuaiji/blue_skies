> **Important legal notice**: This repository is an educational decompilation + porting study artifact and is **in no way** an official repository or fork of Blue Skies™. All original code and resources are the intellectual property of Modding Legacy and the original authors. This repository is published **All Rights Reserved** for private study and research only. See [LICENSE](LICENSE).

# Blue Skies — 1.21.1 NeoForge Port

[中文说明](README.md)

> This is not an official repository. Official Blue Skies is developed by Modding Legacy and published on [GitLab](https://gitlab.com/modding-legacy/blue-skies). This repository decompiles the 1.20.4 build (`blue_skies-1.20.4-1.3.32.jar`) and performs a genuine migration to Minecraft 1.21.1 / NeoForge for technical research and personal study.

Blue Skies is a content mod that adds the **Everbright** and **Everdawn** dimensions, with a full set of blocks, items, entities, structures, world generation and progression.

## Port Status

| Item | Value |
| --- | --- |
| Upstream version | 1.20.4 / 1.3.32 (decompiled from release jar) |
| Target | Minecraft 1.21.1 / NeoForge 21.1.234+ |
| Java | 21 |
| Approach | Genuine module-by-module API rewrite (no placeholder shims) |
| Build status | Migration in progress — see commit history and issues |

## Requirements

| Dependency | Version |
| --- | --- |
| Minecraft | 1.21.1 |
| NeoForge | 21.1.234+ |
| Java | 21 |

## Building

This project uses Gradle (NeoForge ModDev plugin). The repository is configured with the official NeoForge CDN mirror for faster dependency resolution.

```bash
# Build with the wrapper (downloads Gradle 8.11.1 and dependencies automatically)
./gradlew build

# Windows
gradlew.bat build

# Output: build/libs/blue_skies-1.3.32-port0.1.0.jar
```

> The jar embeds Structure Gel API (see the [structure_gel repository](https://github.com/Klisuaiji/structure_gel)), matching the dual `[[mods]]` declarations in `neoforge.mods.toml`.

## Structure

```
src/main/java/com/legacy/blue_skies/       # Blue Skies sources (entry, blocks, items, entities, worldgen, ...)
src/main/java/com/legacy/structure_gel/    # Embedded Structure Gel API sources
src/main/resources/
├── assets/blue_skies/                     # Textures, models, sounds, lang
├── data/blue_skies/                       # Recipes, loot, tags, worldgen JSON
├── blue_skies.mixins.json                 # Mixin config
└── META-INF/neoforge.mods.toml            # Mod metadata (generated from template)
src/main/templates/META-INF/neoforge.mods.toml
```

## Known Limitations

- Sources are decompiled; local variable names and lambda/inner-class names retain compiler artifacts.
- Generic signatures, annotation parameters, record/enum structures may differ from the original source.
- The port is a work in progress and may contain compile errors or unverified runtime behavior.

## Copyright

All original code and resources are copyrighted to Modding Legacy and the original authors. Porting work (c) 2026 Kulisaiji.
This repository grants **no open-source license of any kind (All Rights Reserved)** and is for private study and research only. See [LICENSE](LICENSE).
If you are a copyright holder and wish this repository removed, please open an issue.

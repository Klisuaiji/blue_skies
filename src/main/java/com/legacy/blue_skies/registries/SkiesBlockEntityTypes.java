package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.block_entity.BagOfSpoilsBlockEntity;
import com.legacy.blue_skies.block_entity.HorizoniteForgeBlockEntity;
import com.legacy.blue_skies.block_entity.KeystoneBlockEntity;
import com.legacy.blue_skies.block_entity.SkyChestBlockEntity;
import com.legacy.blue_skies.block_entity.SkyHangingSignBlockEntity;
import com.legacy.blue_skies.block_entity.SkySignBlockEntity;
import com.legacy.blue_skies.block_entity.SnowcapOvenBlockEntity;
import com.legacy.blue_skies.block_entity.TrophyBlockEntity;
import com.legacy.blue_skies.events.SkiesMappingChanges;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesBlockEntityTypes {
   public static final BlockEntityType<SkyChestBlockEntity.BluebrightChestBlockEntity> BLUEBRIGHT_CHEST = Builder.of(
         SkyChestBlockEntity.BluebrightChestBlockEntity::new, new Block[]{SkiesBlocks.bluebright_chest}
      )
      .build(null);
   public static final BlockEntityType<SkyChestBlockEntity.StarlitChestBlockEntity> STARLIT_CHEST = Builder.of(
         SkyChestBlockEntity.StarlitChestBlockEntity::new, new Block[]{SkiesBlocks.starlit_chest}
      )
      .build(null);
   public static final BlockEntityType<SkyChestBlockEntity.FrostbrightChestBlockEntity> FROSTBRIGHT_CHEST = Builder.of(
         SkyChestBlockEntity.FrostbrightChestBlockEntity::new, new Block[]{SkiesBlocks.frostbright_chest}
      )
      .build(null);
   public static final BlockEntityType<SkyChestBlockEntity.LunarChestBlockEntity> LUNAR_CHEST = Builder.of(
         SkyChestBlockEntity.LunarChestBlockEntity::new, new Block[]{SkiesBlocks.lunar_chest}
      )
      .build(null);
   public static final BlockEntityType<SkyChestBlockEntity.DuskChestBlockEntity> DUSK_CHEST = Builder.of(
         SkyChestBlockEntity.DuskChestBlockEntity::new, new Block[]{SkiesBlocks.dusk_chest}
      )
      .build(null);
   public static final BlockEntityType<SkyChestBlockEntity.MapleChestBlockEntity> MAPLE_CHEST = Builder.of(
         SkyChestBlockEntity.MapleChestBlockEntity::new, new Block[]{SkiesBlocks.maple_chest}
      )
      .build(null);
   public static final BlockEntityType<SkyChestBlockEntity.CometChestBlockEntity> COMET_CHEST = Builder.of(
         SkyChestBlockEntity.CometChestBlockEntity::new, new Block[]{SkiesBlocks.comet_chest}
      )
      .build(null);
   public static final BlockEntityType<KeystoneBlockEntity.BrightBlindingKeystoneBlockEntity> BRIGHT_BLINDING_KEYSTONE = Builder.of(
         KeystoneBlockEntity.BrightBlindingKeystoneBlockEntity::new, new Block[]{SkiesBlocks.bright_blinding_keystone}
      )
      .build(null);
   public static final BlockEntityType<KeystoneBlockEntity.DawnBlindingKeystoneBlockEntity> DAWN_BLINDING_KEYSTONE = Builder.of(
         KeystoneBlockEntity.DawnBlindingKeystoneBlockEntity::new, new Block[]{SkiesBlocks.dawn_blinding_keystone}
      )
      .build(null);
   public static final BlockEntityType<KeystoneBlockEntity.NatureKeystoneBlockEntity> NATURE_KEYSTONE = Builder.of(
         KeystoneBlockEntity.NatureKeystoneBlockEntity::new, new Block[]{SkiesBlocks.nature_keystone}
      )
      .build(null);
   public static final BlockEntityType<KeystoneBlockEntity.PoisonKeystoneBlockEntity> POISON_KEYSTONE = Builder.of(
         KeystoneBlockEntity.PoisonKeystoneBlockEntity::new, new Block[]{SkiesBlocks.poison_keystone}
      )
      .build(null);
   public static final BlockEntityType<TrophyBlockEntity> TROPHY = Builder.of(
         TrophyBlockEntity::new,
         new Block[]{SkiesBlocks.summoner_trophy, SkiesBlocks.alchemist_trophy, SkiesBlocks.starlit_crusher_trophy, SkiesBlocks.arachnarch_trophy}
      )
      .build(null);
   public static final BlockEntityType<SkySignBlockEntity> SKY_SIGN = Builder.of(
         SkySignBlockEntity::new,
         new Block[]{
            SkiesBlocks.bluebright_wall_sign,
            SkiesBlocks.bluebright_sign,
            SkiesBlocks.lunar_wall_sign,
            SkiesBlocks.lunar_sign,
            SkiesBlocks.starlit_wall_sign,
            SkiesBlocks.starlit_sign,
            SkiesBlocks.dusk_wall_sign,
            SkiesBlocks.dusk_sign,
            SkiesBlocks.frostbright_wall_sign,
            SkiesBlocks.frostbright_sign,
            SkiesBlocks.maple_wall_sign,
            SkiesBlocks.maple_sign,
            SkiesBlocks.comet_wall_sign,
            SkiesBlocks.comet_sign
         }
      )
      .build(null);
   public static final BlockEntityType<SkyHangingSignBlockEntity> SKY_HANGING_SIGN = Builder.of(
         SkyHangingSignBlockEntity::new,
         new Block[]{
            SkiesBlocks.bluebright_wall_hanging_sign,
            SkiesBlocks.bluebright_hanging_sign,
            SkiesBlocks.lunar_wall_hanging_sign,
            SkiesBlocks.lunar_hanging_sign,
            SkiesBlocks.starlit_wall_hanging_sign,
            SkiesBlocks.starlit_hanging_sign,
            SkiesBlocks.dusk_wall_hanging_sign,
            SkiesBlocks.dusk_hanging_sign,
            SkiesBlocks.frostbright_wall_hanging_sign,
            SkiesBlocks.frostbright_hanging_sign,
            SkiesBlocks.maple_wall_hanging_sign,
            SkiesBlocks.maple_hanging_sign,
            SkiesBlocks.comet_wall_hanging_sign,
            SkiesBlocks.comet_hanging_sign
         }
      )
      .build(null);
   public static final BlockEntityType<SnowcapOvenBlockEntity> SNOWCAP_OVEN = Builder.<SnowcapOvenBlockEntity>of(SnowcapOvenBlockEntity::new, new Block[]{SkiesBlocks.snowcap_oven})
      .build(null);
   public static final BlockEntityType<HorizoniteForgeBlockEntity> HORIZONITE_FORGE = Builder.of(
         HorizoniteForgeBlockEntity::new, new Block[]{SkiesBlocks.horizonite_forge}
      )
      .build(null);
   public static final BlockEntityType<BagOfSpoilsBlockEntity> BAG_OF_SPOILS = Builder.<BagOfSpoilsBlockEntity>of(BagOfSpoilsBlockEntity::new, new Block[]{SkiesBlocks.bag_of_spoils})
      .build(null);

   public static void init(RegisterEvent event) {
      register(event, "bluebright_chest", BLUEBRIGHT_CHEST);
      register(event, "starlit_chest", STARLIT_CHEST);
      register(event, "frostbright_chest", FROSTBRIGHT_CHEST);
      register(event, "lunar_chest", LUNAR_CHEST);
      register(event, "dusk_chest", DUSK_CHEST);
      register(event, "maple_chest", MAPLE_CHEST);
      register(event, "comet_chest", COMET_CHEST);
      register(event, "bright_blinding_keystone", BRIGHT_BLINDING_KEYSTONE);
      register(event, "dawn_blinding_keystone", DAWN_BLINDING_KEYSTONE);
      register(event, "nature_keystone", NATURE_KEYSTONE);
      register(event, "poison_keystone", POISON_KEYSTONE);
      register(event, "trophy", TROPHY);
      register(event, "sky_sign", SKY_SIGN);
      register(event, "sky_hanging_sign", SKY_HANGING_SIGN);
      register(event, "snowcap_oven", SNOWCAP_OVEN);
      register(event, "horizonite_forge", HORIZONITE_FORGE);
      register(event, "bag_of_spoils", BAG_OF_SPOILS);
      SkiesMappingChanges.addBlockEntityAliases(event.getRegistry(Registries.BLOCK_ENTITY_TYPE));
   }

   private static void register(RegisterEvent event, String name, BlockEntityType<?> type) {
      event.register(Registries.BLOCK_ENTITY_TYPE, BlueSkies.locate(name), () -> type);
   }
}

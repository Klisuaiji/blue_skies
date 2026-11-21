package com.legacy.blue_skies.block_entity;

import com.legacy.blue_skies.registries.SkiesBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkyChestBlockEntity extends ChestBlockEntity {
   private final Component localizedName;

   public SkyChestBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
      super(typeIn, pos, state);
      this.localizedName = Component.translatable("block.blue_skies." + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(typeIn).getPath());
   }

   protected Component getDefaultName() {
      return this.localizedName;
   }

   public static class BluebrightChestBlockEntity extends SkyChestBlockEntity {
      public BluebrightChestBlockEntity(BlockPos pos, BlockState state) {
         super(SkiesBlockEntityTypes.BLUEBRIGHT_CHEST, pos, state);
      }
   }

   public static class CometChestBlockEntity extends SkyChestBlockEntity {
      public CometChestBlockEntity(BlockPos pos, BlockState state) {
         super(SkiesBlockEntityTypes.COMET_CHEST, pos, state);
      }
   }

   public static class DuskChestBlockEntity extends SkyChestBlockEntity {
      public DuskChestBlockEntity(BlockPos pos, BlockState state) {
         super(SkiesBlockEntityTypes.DUSK_CHEST, pos, state);
      }
   }

   public static class FrostbrightChestBlockEntity extends SkyChestBlockEntity {
      public FrostbrightChestBlockEntity(BlockPos pos, BlockState state) {
         super(SkiesBlockEntityTypes.FROSTBRIGHT_CHEST, pos, state);
      }
   }

   public static class LunarChestBlockEntity extends SkyChestBlockEntity {
      public LunarChestBlockEntity(BlockPos pos, BlockState state) {
         super(SkiesBlockEntityTypes.LUNAR_CHEST, pos, state);
      }
   }

   public static class MapleChestBlockEntity extends SkyChestBlockEntity {
      public MapleChestBlockEntity(BlockPos pos, BlockState state) {
         super(SkiesBlockEntityTypes.MAPLE_CHEST, pos, state);
      }
   }

   public static class StarlitChestBlockEntity extends SkyChestBlockEntity {
      public StarlitChestBlockEntity(BlockPos pos, BlockState state) {
         super(SkiesBlockEntityTypes.STARLIT_CHEST, pos, state);
      }
   }
}

package com.legacy.blue_skies.world.general_features.structures;

import com.legacy.blue_skies.data.providers.SkiesLootProv;
import com.legacy.blue_skies.entities.passive.ShadeMonitorEntity;
import com.legacy.blue_skies.entities.villager.GatekeeperEntity;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesJigsawTypes;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.blue_skies.util.StructureChestUtil;
import com.legacy.structure_gel.api.structure.jigsaw.ExtendedJigsawStructurePiece;
import com.legacy.structure_gel.api.structure.jigsaw.IPieceFactory;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapabilityType;
import com.legacy.structure_gel.api.structure.jigsaw.IPieceFactory.Context;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class GatekeeperHouseStructure {
   public static class Capability implements JigsawCapability {
      public static final GatekeeperHouseStructure.Capability INSTANCE = new GatekeeperHouseStructure.Capability();
      public static final Codec<GatekeeperHouseStructure.Capability> CODEC = Codec.unit(INSTANCE);

      public JigsawCapabilityType<?> getType() {
         return SkiesJigsawTypes.GATEKEEPER_HOUSE;
      }

      public IPieceFactory getPieceFactory() {
         return GatekeeperHouseStructure.Piece::new;
      }
   }

   public static class Piece extends ExtendedJigsawStructurePiece {
      public Piece(Context context) {
         super(context);
      }

      public Piece(StructurePieceSerializationContext context, CompoundTag nbt) {
         super(context, nbt);
      }

      public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, RandomSource rand, BoundingBox bounds) {
         this.setAir(level, pos);
         if (key.equals("gatekeeper")) {
            GatekeeperEntity entity = (GatekeeperEntity)this.createEntity(SkiesEntityTypes.GATEKEEPER, level, pos, this.rotation);
            entity.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);
            entity.homePos = pos;
            level.addFreshEntity(entity);
         } else if (key.equals("shade_monitor")) {
            ShadeMonitorEntity entity = (ShadeMonitorEntity)this.createEntity(SkiesEntityTypes.SHADE_MONITOR, level, pos, this.rotation);
            entity.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);
            level.addFreshEntity(entity);
         }

         if (key.startsWith("barrel")) {
            String[] data = key.split("-");
            if (data.length > 2) {
               level.setBlock(
                  pos,
                  ((BlockState)Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, Direction.byName(data[1]))).rotate(level, pos, this.rotation),
                  2
               );
               RandomizableContainer.setBlockEntityLootTable(level, rand, pos, SkiesLootProv.GATEKEEPER_LOOT[Integer.valueOf(data[2])]);
            }
         } else if (key.startsWith("chest")) {
            String[] data = key.split("-");
            if (data.length > 4) {
               StructureChestUtil.createChest(
                  pos, level, rand, this.rotation, data[1], data[2], data[3], SkiesLootProv.GATEKEEPER_LOOT[Integer.valueOf(data[4])]
               );
            }
         }
      }

      public StructurePieceType getType() {
         return (StructurePieceType)SkiesStructures.GATEKEEPER_HOUSE.getPieceType().get();
      }
   }
}

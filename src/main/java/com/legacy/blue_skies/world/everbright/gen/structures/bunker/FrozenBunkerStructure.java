package com.legacy.blue_skies.world.everbright.gen.structures.bunker;

import com.legacy.blue_skies.data.providers.SkiesLootProv;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesJigsawTypes;
import com.legacy.blue_skies.registries.SkiesStructures;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class FrozenBunkerStructure {
   public static class Capability implements JigsawCapability {
      public static final FrozenBunkerStructure.Capability INSTANCE = new FrozenBunkerStructure.Capability();
      public static final Codec<FrozenBunkerStructure.Capability> CODEC = Codec.unit(INSTANCE);

      public JigsawCapabilityType<?> getType() {
         return SkiesJigsawTypes.FROZEN_BUNKER;
      }

      public IPieceFactory getPieceFactory() {
         return FrozenBunkerStructure.Piece::new;
      }
   }

   public static class Piece extends ExtendedJigsawStructurePiece {
      public Piece(Context context) {
         super(context);
      }

      public Piece(StructurePieceSerializationContext context, CompoundTag nbt) {
         super(context, nbt);
      }

      public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor worldIn, RandomSource rand, BoundingBox bounds) {
         if (key.equals("spawner")) {
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            worldIn.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 3);
            if (worldIn.getBlockEntity(pos) instanceof SpawnerBlockEntity tile) {
               tile.setEntityId(SkiesEntityTypes.ARMORED_FROST_SPIRIT, rand);
            }
         } else if (key.startsWith("chest") && key.contains("-")) {
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            String facing = key.split("-")[1];
            boolean isRare = rand.nextFloat() < 0.25;
            this.createChest(
               worldIn,
               bounds,
               rand,
               pos,
               isRare ? SkiesLootProv.BUNKER_RARE : SkiesLootProv.BUNKER,
               ((BlockState)(isRare ? SkiesBlocks.starlit_chest : SkiesBlocks.bluebright_chest)
                     .defaultBlockState()
                     .setValue(ChestBlock.FACING, Direction.byName(facing)))
                  .rotate(worldIn, pos, this.rotation)
            );
         }
      }

      public StructurePieceType getType() {
         return (StructurePieceType)SkiesStructures.FROZEN_BUNKER.getPieceType().get();
      }
   }
}

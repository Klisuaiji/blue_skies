package com.legacy.blue_skies.util;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class StructureChestUtil {
   private static final Map<String, ChestType> CHEST_TYPE_BY_ID = (Map<String, ChestType>)Util.make(new HashMap(), map -> {
      for (ChestType type : ChestType.values()) {
         map.put(type.getSerializedName(), type);
      }
   });

   public static void createChest(
      BlockPos pos, ServerLevelAccessor level, RandomSource rand, Rotation rotation, Block block, Direction facing, ChestType chestType, ResourceLocation loot
   ) {
      if (block instanceof ChestBlock) {
         level.setBlock(
            pos,
            ((BlockState)((BlockState)block.defaultBlockState().setValue(ChestBlock.FACING, facing)).setValue(ChestBlock.TYPE, chestType))
               .rotate(level, pos, rotation),
            2
         );
         RandomizableContainer.setBlockEntityLootTable(level, rand, pos, loot);
      }
   }

   public static void createChest(
      BlockPos pos,
      ServerLevelAccessor level,
      RandomSource rand,
      Rotation rotation,
      String blockKey,
      String facingKey,
      String chestTypeKey,
      ResourceLocation loot
   ) {
      createChest(
         pos,
         level,
         rand,
         rotation,
         (Block)BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockKey)),
         Direction.byName(facingKey),
         getChestType(chestTypeKey),
         loot
      );
   }

   public static void createChest(
      BlockPos pos, ServerLevelAccessor level, RandomSource rand, Rotation rotation, String blockKey, String facingKey, ResourceLocation loot
   ) {
      createChest(
         pos, level, rand, rotation, (Block)BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockKey)), Direction.byName(facingKey), ChestType.SINGLE, loot
      );
   }

   public static ChestType getChestType(String name) {
      return CHEST_TYPE_BY_ID.getOrDefault(name, ChestType.SINGLE);
   }
}

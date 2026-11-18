package com.legacy.blue_skies.blocks.construction;

import com.google.common.base.Supplier;
import com.legacy.blue_skies.blocks.natural.SnowcapMushroomBlock;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SkySnowcapPotBlock extends SkyFlowerPotBlock {
   public static final MapCodec<FlowerPotBlock> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(flowerCodec(), propertiesCodec()).apply(instance, (flower, prop) -> new SkySnowcapPotBlock(() -> flower, prop))
   );

   public SkySnowcapPotBlock(Supplier<? extends Block> flower, Properties properties) {
      super(flower, properties);
   }

   public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
      if (!world.isClientSide) {
         SnowcapMushroomBlock.freezeBlocks(world, pos, random);
      }
   }

   @Override
   public MapCodec<FlowerPotBlock> codec() {
      return CODEC;
   }
}

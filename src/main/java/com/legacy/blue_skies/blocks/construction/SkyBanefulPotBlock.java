package com.legacy.blue_skies.blocks.construction;

import com.google.common.base.Supplier;
import com.legacy.blue_skies.blocks.natural.BanefulMushroomBlock;
import com.legacy.blue_skies.client.particle.data.PoisonData;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SkyBanefulPotBlock extends SkyFlowerPotBlock {
   public static final MapCodec<FlowerPotBlock> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(flowerCodec(), propertiesCodec()).apply(instance, (flower, prop) -> new SkyBanefulPotBlock(() -> flower, prop))
   );
   public static final BooleanProperty OPEN = BooleanProperty.create("open");

   public SkyBanefulPotBlock(Supplier<? extends Block> flower, Properties properties) {
      super(flower, properties);
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(OPEN, false));
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{OPEN});
   }

   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      if ((Boolean)state.getValue(OPEN)) {
         level.setBlock(pos, this.defaultBlockState(), 3);
      }
   }

   public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
      BanefulMushroomBlock.poisonTarget(state, level, pos, entity, this, OPEN);
      super.stepOn(level, pos, state, entity);
   }

   @OnlyIn(Dist.CLIENT)
   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
      if ((Boolean)state.getValue(OPEN) && rand.nextBoolean()) {
         for (int i = 0; i < rand.nextInt(4) + 3; i++) {
            float dx = (rand.nextFloat() - 0.5F) / 7.0F;
            float dy = rand.nextFloat() / 10.0F;
            float dz = (rand.nextFloat() - 0.5F) / 7.0F;
            level.addParticle(new PoisonData(false), pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, dx, dy, dz);
         }
      }
   }

   @Override
   public MapCodec<FlowerPotBlock> codec() {
      return CODEC;
   }
}

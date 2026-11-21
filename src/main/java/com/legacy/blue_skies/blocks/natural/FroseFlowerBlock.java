package com.legacy.blue_skies.blocks.natural;

import net.minecraft.core.Holder;
import com.legacy.blue_skies.registries.SkiesParticles;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.Tags.Blocks;

public class FroseFlowerBlock extends FlowerBlock {
   public FroseFlowerBlock(Holder<MobEffect> effect, float effectDuration, Properties properties) {
      super(effect, effectDuration, properties);
   }

   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      return super.mayPlaceOn(state, level, pos) || Block.canSupportRigidBlock(level, pos) && (state.is(Blocks.STONE) || state.is(BlockTags.ICE));
   }

   @OnlyIn(Dist.CLIENT)
   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
      Vec3 offset = state.getOffset(level, pos);
      int count = rand.nextInt(2);

      for (int i = 0; i <= count; i++) {
         level.addParticle(SkiesParticles.FROSE_SNOW, pos.getX() + 0.5 + offset.x, pos.getY() + 0.5, pos.getZ() + 0.5 + offset.z, 0.0, 0.0, 0.0);
      }
   }
}

package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.registries.SkiesBlocks;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LargeFlowerBlock extends FlowerBlock {
   private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);
   private final boolean allowedOnCometGrass;

   public LargeFlowerBlock(Supplier<MobEffect> effect, int effectDuration, Properties properties) {
      this(effect, effectDuration, properties, false);
   }

   public LargeFlowerBlock(Supplier<MobEffect> effect, int effectDuration, Properties properties, boolean allowedOnComet) {
      super(effect, effectDuration, properties);
      this.allowedOnCometGrass = allowedOnComet;
   }

   public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
      return (
               worldIn.getBlockState(pos.below()).getBlock() == SkiesBlocks.turquoise_comet_grass_block
                  || worldIn.getBlockState(pos.below()).getBlock() == SkiesBlocks.lunar_comet_grass_block
            )
            && !this.allowedOnCometGrass
         ? false
         : super.canSurvive(state, worldIn, pos);
   }

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      Vec3 vec3d = state.getOffset(worldIn, pos);
      return SHAPE.move(vec3d.x, vec3d.y, vec3d.z);
   }
}

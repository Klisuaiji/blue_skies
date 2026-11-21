package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.registries.SkiesEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class SkyLeavesBlock extends LeavesBlock {
   public static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;

   public SkyLeavesBlock(Properties props) {
      super(props);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(DISTANCE, 7)).setValue(PERSISTENT, false))
               .setValue(SNOWY, false))
            .setValue(WATERLOGGED, false)
      );
   }

   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
      Block topBlock = level.getBlockState(pos.above()).getBlock();
      return (BlockState)super.updateShape(state, facing, facingState, level, pos, facingPos)
         .setValue(SNOWY, topBlock == Blocks.SNOW || topBlock == Blocks.SNOW_BLOCK);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(new Property[]{SNOWY});
   }

   public static Boolean allowsSpawnOnLeaves(BlockState state, BlockGetter level, BlockPos pos, EntityType<?> entityType) {
      return entityType == EntityType.OCELOT || entityType == EntityType.PARROT || entityType == SkiesEntityTypes.SNOW_OWL;
   }

   public static boolean isntSolid(BlockState state, BlockGetter level, BlockPos pos) {
      return false;
   }
}

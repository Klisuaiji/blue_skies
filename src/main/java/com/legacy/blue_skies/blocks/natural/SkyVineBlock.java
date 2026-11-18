package com.legacy.blue_skies.blocks.natural;

import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class SkyVineBlock extends VineBlock {
   public static final BooleanProperty CAN_GROW = BooleanProperty.create("can_grow");
   public static final Map<Direction, BooleanProperty> HORIZONTAL_FACING_MAP = PipeBlock.PROPERTY_BY_DIRECTION
      .entrySet()
      .stream()
      .filter(prop -> ((Direction)prop.getKey()).getAxis() != Axis.Y)
      .collect(Util.toMap());

   public SkyVineBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(UP, false))
                        .setValue(NORTH, false))
                     .setValue(EAST, false))
                  .setValue(SOUTH, false))
               .setValue(WEST, false))
            .setValue(CAN_GROW, true)
      );
   }

   public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
      if ((Boolean)state.getValue(CAN_GROW)) {
         super.randomTick(state, world, pos, random);
      } else if (!this.canStay(state, world, pos)) {
         world.destroyBlock(pos, false);
      }
   }

   private boolean canStay(BlockState state, Level world, BlockPos pos) {
      BlockState aboveState = world.getBlockState(pos.above());

      for (Entry<Direction, BooleanProperty> property : HORIZONTAL_FACING_MAP.entrySet()) {
         if (aboveState.getBlock() == this
            && (Boolean)state.getValue((Property)property.getValue())
            && (Boolean)aboveState.getValue((Property)property.getValue())) {
            return true;
         }

         BlockPos offsetPos = pos.relative(property.getKey());
         BlockState sideState = world.getBlockState(offsetPos);
         if (sideState.isFaceSturdy(world, offsetPos, property.getKey().getOpposite())) {
            return true;
         }
      }

      return false;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(new Property[]{CAN_GROW});
   }
}

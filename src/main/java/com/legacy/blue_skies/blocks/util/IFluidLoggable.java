package com.legacy.blue_skies.blocks.util;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public interface IFluidLoggable extends BucketPickup, LiquidBlockContainer {
   EnumProperty<IFluidLoggable.FluidLogging> FLUIDLOGGED = EnumProperty.create("fluidlogged", IFluidLoggable.FluidLogging.class);

   default boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
      return state.getValue(FLUIDLOGGED) == IFluidLoggable.FluidLogging.EMPTY && IFluidLoggable.FluidLogging.isValid(fluid);
   }

   default boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
      Fluid fluid = fluidState.getType();
      if (state.getValue(FLUIDLOGGED) == IFluidLoggable.FluidLogging.EMPTY && IFluidLoggable.FluidLogging.isValid(fluid)) {
         if (!level.isClientSide()) {
            level.setBlock(pos, (BlockState)state.setValue(FLUIDLOGGED, IFluidLoggable.FluidLogging.from(fluid)), 3);
            level.scheduleTick(pos, fluid, fluid.getTickDelay(level));
         }

         return true;
      } else {
         return false;
      }
   }

   default ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state) {
      IFluidLoggable.FluidLogging fluid = (IFluidLoggable.FluidLogging)state.getValue(FLUIDLOGGED);
      if (fluid != IFluidLoggable.FluidLogging.EMPTY) {
         level.setBlock(pos, (BlockState)state.setValue(FLUIDLOGGED, IFluidLoggable.FluidLogging.EMPTY), 3);
         if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
         }

         return new ItemStack(fluid.fluid.getBucket());
      } else {
         return ItemStack.EMPTY;
      }
   }

   default Optional<SoundEvent> getPickupSound() {
      return Fluids.WATER.getPickupSound();
   }

   public static enum FluidLogging implements StringRepresentable {
      EMPTY(Fluids.EMPTY.defaultFluidState(), "empty"),
      WATER(Fluids.WATER.getSource(false), "water"),
      LAVA(Fluids.LAVA.getSource(false), "lava");

      public final Fluid fluid;
      public final FluidState fluidState;
      private final String name;

      private FluidLogging(FluidState fluidState, String name) {
         this.fluid = fluidState.getType();
         this.fluidState = fluidState;
         this.name = name;
      }

      public static IFluidLoggable.FluidLogging from(Fluid fluid) {
         for (IFluidLoggable.FluidLogging f : values()) {
            if (f.fluid.equals(fluid)) {
               return f;
            }
         }

         return EMPTY;
      }

      public static IFluidLoggable.FluidLogging from(Level level, BlockPos pos) {
         return from(level.getFluidState(pos).getType());
      }

      public static boolean isValid(Fluid fluid) {
         for (IFluidLoggable.FluidLogging f : values()) {
            if (f.fluid.equals(fluid)) {
               return true;
            }
         }

         return false;
      }

      public String getSerializedName() {
         return this.name;
      }
   }
}

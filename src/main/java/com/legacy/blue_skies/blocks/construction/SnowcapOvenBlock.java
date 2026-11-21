package com.legacy.blue_skies.blocks.construction;

import com.legacy.blue_skies.block_entity.SnowcapOvenBlockEntity;
import com.legacy.blue_skies.registries.SkiesBlockEntityTypes;
import com.legacy.blue_skies.registries.SkiesParticles;
import com.legacy.blue_skies.registries.SkiesStats;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SnowcapOvenBlock extends AbstractFurnaceBlock {
   public static final MapCodec<SnowcapOvenBlock> CODEC = simpleCodec(SnowcapOvenBlock::new);

   public SnowcapOvenBlock(Properties properties) {
      super(properties);
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new SnowcapOvenBlockEntity(pos, state);
   }

   protected void openContainer(Level world, BlockPos pos, Player player) {
      BlockEntity tile = world.getBlockEntity(pos);
      if (tile instanceof SnowcapOvenBlockEntity) {
         player.openMenu((MenuProvider)tile);
         player.awardStat(SkiesStats.INTERACT_WITH_SNOWCAP_OVEN);
      }
   }

   public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
      if (state.getBlock() != newState.getBlock()) {
         if (world.getBlockEntity(pos) instanceof SnowcapOvenBlockEntity oven) {
            if (world instanceof ServerLevel serverLevel) {
               Containers.dropContents(serverLevel, pos, oven);
               oven.getRecipesToAwardAndPopExperience(serverLevel, Vec3.atCenterOf(pos));
            }

            world.updateNeighbourForOutputSignal(pos, this);
         }

         if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            world.removeBlockEntity(pos);
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
      if ((Boolean)state.getValue(LIT)) {
         double x = pos.getX() + 0.5;
         double y = pos.getY();
         double z = pos.getZ() + 0.5;
         if (rand.nextDouble() < 0.1) {
            world.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
         }

         Direction direction = (Direction)state.getValue(FACING);
         Axis axis = direction.getAxis();
         double particleOffset = rand.nextDouble() * 0.6 - 0.3;
         double xOffset = axis == Axis.X ? direction.getStepX() * 0.52 : particleOffset;
         double yOffset = rand.nextDouble() * 7.0 / 16.0;
         double zOffset = axis == Axis.Z ? direction.getStepZ() * 0.52 : particleOffset;
         if (rand.nextDouble() < 0.4) {
            world.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
         }

         world.addParticle(SkiesParticles.BLUE_FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
         world.addParticle(ParticleTypes.SMOKE, x, y + 1.1, z, 0.0, 0.0, 0.0);
      }
   }

   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
      return level.isClientSide ? null : createTickerHelper(blockEntityType, SkiesBlockEntityTypes.SNOWCAP_OVEN, SnowcapOvenBlockEntity::ovenServerTick);
   }

   protected MapCodec<? extends AbstractFurnaceBlock> codec() {
      return CODEC;
   }
}

package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.registries.SkiesConfiguredFeatures;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.Event.Result;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.SaplingGrowTreeEvent;

public class SnowcapMushroomBlock extends DoublePlantBlock implements BonemealableBlock {
   private static final VoxelShape STEM_BOUNDS = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
   private static final VoxelShape CAP_COLLISION = Block.box(0.0, 12.0, 0.0, 16.0, 13.0, 16.0);
   private static final VoxelShape CAP_BOUNDS = Shapes.or(CAP_COLLISION, Block.box(6.0, 0.0, 6.0, 10.0, 12.0, 10.0));

   public SnowcapMushroomBlock(Properties properties) {
      super(properties);
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return this.getShape(state, level, pos, CAP_BOUNDS, STEM_BOUNDS);
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return this.getShape(state, level, pos, CAP_COLLISION, Shapes.empty());
   }

   public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
      return Shapes.empty();
   }

   private VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, VoxelShape upperShape, VoxelShape lowerShape) {
      boolean isUpper = state.getValue(HALF) == DoubleBlockHalf.UPPER;
      Vec3 offset = isUpper ? state.getOffset(level, pos) : level.getBlockState(pos.above()).getOffset(level, pos.above());
      return (isUpper ? upperShape : lowerShape).move(offset.x, offset.y, offset.z);
   }

   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
         return super.canSurvive(state, level, pos) || level.getBlockState(pos.below()).is(BlockTags.MUSHROOM_GROW_BLOCK);
      } else {
         BlockState lowerState = level.getBlockState(pos.below());
         return state.getBlock() != this
            ? super.canSurvive(state, level, pos)
            : lowerState.getBlock() == this && lowerState.getValue(HALF) == DoubleBlockHalf.LOWER;
      }
   }

   public boolean isRandomlyTicking(BlockState state) {
      return state.getValue(HALF) == DoubleBlockHalf.LOWER;
   }

   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      if (!level.isClientSide) {
         freezeBlocks(level, pos, random);
      }
   }

   public static void freezeBlocks(Level level, BlockPos pos, RandomSource rand) {
      BlockPos randPos = new BlockPos(pos.getX() + rand.nextInt(3) - 1, pos.getY() - rand.nextInt(2), pos.getZ() + rand.nextInt(3) - 1);
      BlockState stateAtPos = level.getBlockState(randPos);
      BlockState frozenState = BlueSkiesData.SNOWCAP_MUSHROOM_FREEZING.getState(stateAtPos, rand);
      if (frozenState != null) {
         level.setBlockAndUpdate(randPos, frozenState);
         if (level instanceof ServerLevel) {
            ((ServerLevel)level).sendParticles(ParticleTypes.CLOUD, randPos.getX() + 0.5, randPos.getY() + 0.5, randPos.getZ() + 0.5, 8, 0.3, 0.3, 0.3, 0.03);
            level.playSound(null, randPos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.15F, 0.9F + rand.nextFloat() * 0.2F);
         }
      }
   }

   public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
      return false;
   }

   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
      return true;
   }

   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
      return true;
   }

   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      if (rand.nextFloat() < 0.75F) {
         Optional<Reference<ConfiguredFeature<?, ?>>> mushroomFeature = level.registryAccess()
            .registryOrThrow(Registries.CONFIGURED_FEATURE)
            .getHolder(SkiesConfiguredFeatures.GIANT_SNOWCAP_MUSHROOM);
         if (mushroomFeature.isPresent()) {
            SaplingGrowTreeEvent event = EventHooks.blockGrowFeature(level, rand, pos, (Holder)mushroomFeature.get());
            if (event.getResult().equals(Result.DENY)) {
               return;
            }

            ((ConfiguredFeature)event.getFeature().value())
               .place(level, level.getChunkSource().getGenerator(), rand, state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below());
         }
      }
   }
}

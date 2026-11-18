package com.legacy.blue_skies.blocks.misc;

import com.legacy.blue_skies.blocks.portal.SkyPortalBlock;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBossMob;
import com.legacy.blue_skies.events.SkiesPlayerEvents;
import com.legacy.structure_gel.api.block.GelPortalBlock;
import com.legacy.structure_gel.api.dimension.portal.GelPortalLogic;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SkyFireBlock extends FireBlock {
   private final Supplier<MobEffectInstance> effect;
   public final Supplier<GelPortalBlock> portalBlock;
   public final TagKey<Block> baseBlocks;

   public SkyFireBlock(Properties prop, Supplier<MobEffectInstance> effect, Supplier<GelPortalBlock> portalBlock, TagKey<Block> baseBlocks) {
      super(prop);
      this.effect = effect;
      this.portalBlock = portalBlock;
      this.baseBlocks = baseBlocks;
   }

   public void entityInside(BlockState state, Level world, BlockPos pos, Entity entityIn) {
      if (entityIn instanceof LivingEntity && !(entityIn instanceof ISkyBossMob)) {
         ((LivingEntity)entityIn).addEffect(this.effect.get());
      }
   }

   public BlockState updateShape(BlockState fireState, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
      return this.canSurvive(fireState, world, currentPos)
         ? this.getFireWithAge(world, currentPos, (Integer)fireState.getValue(AGE))
         : Blocks.AIR.defaultBlockState();
   }

   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      return super.canSurvive(state, level, pos) || level.getBlockState(pos.below()).is(this.baseBlocks);
   }

   public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
      if (oldState.getBlock() != state.getBlock() && !GelPortalLogic.fillPortal(world, pos, this.portalBlock.get(), SkyPortalBlock.ALLOWED_INSIDE_BLOCKS)) {
         if (!state.canSurvive(world, pos)) {
            world.removeBlock(pos, false);
         } else {
            world.scheduleTick(pos, this, getTickCooldown(world.random));
         }
      }
   }

   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      level.scheduleTick(pos, this, getTickCooldown(level.random));
      if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
         if (!state.canSurvive(level, pos)) {
            level.removeBlock(pos, false);
         }

         BlockState baseBlock = level.getBlockState(pos.below());
         boolean infiniburn = baseBlock.isFireSource(level, pos, Direction.UP) || baseBlock.is(this.baseBlocks);
         int age = (Integer)state.getValue(AGE);
         if (!infiniburn && level.isRaining() && this.isNearRain(level, pos) && rand.nextFloat() < 0.2F + age * 0.03F) {
            level.removeBlock(pos, false);
         } else {
            int j = Math.min(15, age + rand.nextInt(3) / 2);
            if (age != j) {
               state = (BlockState)state.setValue(AGE, j);
               level.setBlock(pos, state, 4);
            }

            if (!infiniburn) {
               if (!this.areNeighborsFlammable(level, pos)) {
                  BlockPos blockpos = pos.below();
                  if (!level.getBlockState(blockpos).isFaceSturdy(level, blockpos, Direction.UP) || age > 3) {
                     level.removeBlock(pos, false);
                  }

                  return;
               }

               if (age == 15 && rand.nextInt(4) == 0 && !this.canCatchFire(level, pos.below(), Direction.UP)) {
                  level.removeBlock(pos, false);
                  return;
               }
            }

            if (!SkiesPlayerEvents.DungeonBalancingChecks.isPosInBossRoom(level, pos)) {
               boolean flag1 = level.getBiome(pos).is(BiomeTags.INCREASED_FIRE_BURNOUT);
               int k = flag1 ? -50 : 0;
               this.tryCatchFire(level, pos.east(), 300 + k, rand, age, Direction.WEST);
               this.tryCatchFire(level, pos.west(), 300 + k, rand, age, Direction.EAST);
               this.tryCatchFire(level, pos.below(), 250 + k, rand, age, Direction.UP);
               this.tryCatchFire(level, pos.above(), 250 + k, rand, age, Direction.DOWN);
               this.tryCatchFire(level, pos.north(), 300 + k, rand, age, Direction.SOUTH);
               this.tryCatchFire(level, pos.south(), 300 + k, rand, age, Direction.NORTH);
               MutableBlockPos blockpos$mutable = new MutableBlockPos();

               for (int l = -1; l <= 1; l++) {
                  for (int i1 = -1; i1 <= 1; i1++) {
                     for (int j1 = -1; j1 <= 4; j1++) {
                        if (l != 0 || j1 != 0 || i1 != 0) {
                           int k1 = 100;
                           if (j1 > 1) {
                              k1 += (j1 - 1) * 100;
                           }

                           blockpos$mutable.setWithOffset(pos, l, j1, i1);
                           int l1 = this.getNeighborEncouragement(level, blockpos$mutable);
                           if (l1 > 0) {
                              int i2 = (l1 + 40 + level.getDifficulty().getId() * 7) / (age + 30);
                              if (flag1) {
                                 i2 /= 2;
                              }

                              if (i2 > 0 && rand.nextInt(k1) <= i2 && (!level.isRaining() || !this.isNearRain(level, blockpos$mutable))) {
                                 int j2 = Math.min(15, age + rand.nextInt(5) / 4);
                                 level.setBlock(blockpos$mutable, this.getFireWithAge(level, blockpos$mutable, j2), 3);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void tryCatchFire(Level world, BlockPos pos, int chance, RandomSource rand, int age, Direction face) {
      if (rand.nextInt(chance) < world.getBlockState(pos).getFlammability(world, pos, face)) {
         BlockState blockstate = world.getBlockState(pos);
         if (rand.nextInt(age + 10) < 5 && !world.isRainingAt(pos)) {
            world.setBlock(pos, this.getFireWithAge(world, pos, Math.min(age + rand.nextInt(5) / 4, 15)), 3);
         } else {
            world.removeBlock(pos, false);
         }

         blockstate.onCaughtFire(world, pos, face, null);
      }
   }

   private BlockState getFireWithAge(LevelAccessor world, BlockPos pos, int age) {
      BlockState state = this.getStateForPlacement(world, pos);
      return state.is(this) ? (BlockState)state.setValue(AGE, age) : state;
   }

   private boolean areNeighborsFlammable(BlockGetter world, BlockPos pos) {
      for (Direction direction : Direction.values()) {
         if (this.canCatchFire(world, pos.relative(direction), direction.getOpposite())) {
            return true;
         }
      }

      return false;
   }

   private int getNeighborEncouragement(LevelReader world, BlockPos pos) {
      if (!world.isEmptyBlock(pos)) {
         return 0;
      } else {
         int i = 0;

         for (Direction direction : Direction.values()) {
            i = Math.max(world.getBlockState(pos.relative(direction)).getFireSpreadSpeed(world, pos.relative(direction), direction.getOpposite()), i);
         }

         return i;
      }
   }

   private static int getTickCooldown(RandomSource rand) {
      return 30 + rand.nextInt(10);
   }
}

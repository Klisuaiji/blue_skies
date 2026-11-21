package com.legacy.blue_skies.entities.passive;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.EventHooks;

public class StardustRamEntity extends Sheep {
   private StardustRamEntity.EatSkyGrassGoal eatGrassGoal;

   public StardustRamEntity(EntityType<? extends StardustRamEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   protected void registerGoals() {
      super.registerGoals();
      this.eatGrassGoal = new StardustRamEntity.EatSkyGrassGoal(this);
      this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(new ItemLike[]{SkiesItems.pine_fruit}), false));
      this.goalSelector.addGoal(5, this.eatGrassGoal);
   }

   protected void customServerAiStep() {
      this.eatAnimationTick = this.eatGrassGoal.getEatAnimationTick();
      super.customServerAiStep();
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.23);
   }

   @OnlyIn(Dist.CLIENT)
   public float getHeadEatPositionScale(float partialTicks) {
      return super.getHeadEatPositionScale(partialTicks);
   }

   public int getSheepTimer() {
      return this.eatAnimationTick;
   }

   public boolean isFood(ItemStack stack) {
      return stack.getItem() == SkiesItems.pine_fruit;
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_STARDUST_RAM_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_STARDUST_RAM_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_STARDUST_RAM_DEATH;
   }

   public float getVoicePitch() {
      return this.isBaby()
         ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.3F
         : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.8F;
   }

   public StardustRamEntity getBreedOffspring(ServerLevel worldIn, AgeableMob ageable) {
      return (StardustRamEntity)SkiesEntityTypes.STARDUST_RAM.create(this.level());
   }

   public ResourceKey<LootTable> getDefaultLootTable() {
      return this.isSheared() ? this.getType().getDefaultLootTable() : BlueSkies.locate("entities/stardust_ram/" + this.getColor().toString());
   }

   static class EatSkyGrassGoal extends Goal {
      private static final Predicate<BlockState> IS_TALL_GRASS = BlockStatePredicate.forBlock(SkiesBlocks.short_turquoise_grass);
      private final Mob mob;
      private final Level level;
      private int eatAnimationTick;

      public EatSkyGrassGoal(Mob ram) {
         this.mob = ram;
         this.level = ram.level();
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
      }

      public boolean canUse() {
         if (this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 1000) != 0) {
            return false;
         }

         BlockPos blockpos = this.mob.blockPosition();
         return IS_TALL_GRASS.test(this.level.getBlockState(blockpos))
            ? true
            : this.level.getBlockState(blockpos.below()).is(SkiesBlocks.turquoise_grass_block);
      }

      public void start() {
         this.eatAnimationTick = 40;
         this.level.broadcastEntityEvent(this.mob, (byte)10);
         this.mob.getNavigation().stop();
      }

      public void stop() {
         this.eatAnimationTick = 0;
      }

      public boolean canContinueToUse() {
         return this.eatAnimationTick > 0;
      }

      public int getEatAnimationTick() {
         return this.eatAnimationTick;
      }

      public void tick() {
         this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
         if (this.eatAnimationTick == 4) {
            BlockPos blockpos = this.mob.blockPosition();
            if (IS_TALL_GRASS.test(this.level.getBlockState(blockpos))) {
               if (EventHooks.getMobGriefingEvent(this.level, this.mob)) {
                  this.level.destroyBlock(blockpos, false);
               }

               this.mob.ate();
            } else {
               BlockPos blockpos1 = blockpos.below();
               if (this.level.getBlockState(blockpos1).is(SkiesBlocks.turquoise_grass_block)) {
                  if (EventHooks.getMobGriefingEvent(this.level, this.mob)) {
                     this.level.levelEvent(2001, blockpos1, Block.getId(SkiesBlocks.turquoise_grass_block.defaultBlockState()));
                     this.level.setBlock(blockpos1, SkiesBlocks.turquoise_dirt.defaultBlockState(), 2);
                  }

                  this.mob.ate();
               }
            }
         }
      }

      public boolean requiresUpdateEveryTick() {
         return true;
      }
   }
}

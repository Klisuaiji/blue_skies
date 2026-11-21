package com.legacy.blue_skies.entities.hostile.boss.summons;

import com.legacy.blue_skies.entities.hostile.boss.SummonerEntity;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesDamageSources;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class StrangeLightningEntity extends Entity {
   private int lightningState;
   public long boltVertex;
   private int boltLivingTime;
   private final boolean effectOnly;
   @Nullable
   private ServerPlayer caster;

   public StrangeLightningEntity(Level level) {
      this(SkiesEntityTypes.STRANGE_LIGHTNING, level);
   }

   public StrangeLightningEntity(EntityType<?> type, Level level) {
      super(type, level);
      this.noCulling = true;
      this.lightningState = 2;
      this.boltVertex = this.random.nextLong();
      this.boltLivingTime = this.random.nextInt(3) + 1;
      this.effectOnly = false;
      Difficulty difficulty = level.getDifficulty();
      if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
         this.igniteBlocks(4);
      }
   }

   public SoundSource getSoundSource() {
      return SoundSource.WEATHER;
   }

   public void setCaster(ServerPlayer p_204809_1_) {
      this.caster = p_204809_1_;
   }

   public void tick() {
      super.tick();
      if (this.lightningState == 2) {
         this.level()
            .playSound(
               (Player)null,
               this.getX(),
               this.getY(),
               this.getZ(),
               SoundEvents.LIGHTNING_BOLT_THUNDER,
               SoundSource.WEATHER,
               5.0F,
               0.8F + this.random.nextFloat() * 0.2F
            );
         this.level()
            .playSound(
               (Player)null,
               this.getX(),
               this.getY(),
               this.getZ(),
               SoundEvents.LIGHTNING_BOLT_IMPACT,
               SoundSource.WEATHER,
               2.0F,
               0.5F + this.random.nextFloat() * 0.2F
            );
      }

      this.lightningState--;
      if (this.lightningState < 0) {
         if (this.boltLivingTime == 0) {
            this.discard();
         } else if (this.lightningState < -this.random.nextInt(10)) {
            this.boltLivingTime--;
            this.lightningState = 1;
            this.boltVertex = this.random.nextLong();
            this.igniteBlocks(0);
         }
      }

      if (this.lightningState >= 0 && !this.effectOnly) {
         List<Entity> list = this.level()
            .getEntities(this, new AABB(this.getX() - 3.0, this.getY() - 3.0, this.getZ() - 3.0, this.getX() + 3.0, this.getY() + 6.0 + 3.0, this.getZ() + 3.0));

         for (int i = 0; i < list.size(); i++) {
            Entity entity = list.get(i);
            if (!(entity instanceof SummonerEntity) && !(entity instanceof ArtificialGolemEntity)) {
               entity.hurt(SkiesDamageSources.get(entity.level()).strangeLightning(this, null), 2.0F);
            }
         }
      }
   }

   private void igniteBlocks(int extraIgnitions) {
      if (!this.effectOnly && !this.level().isClientSide) {
         BlockState blockstate = SkiesBlocks.blue_fire.defaultBlockState();
         BlockPos blockpos = this.blockPosition();
         if (this.level().getBlockState(blockpos).isAir() && blockstate.canSurvive(this.level(), blockpos)) {
            this.level().setBlockAndUpdate(blockpos, blockstate);
         }

         for (int i = 0; i < extraIgnitions; i++) {
            BlockPos blockpos1 = blockpos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
            if (this.level().getBlockState(blockpos1).isAir() && blockstate.canSurvive(this.level(), blockpos1)) {
               this.level().setBlockAndUpdate(blockpos1, blockstate);
            }
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public boolean shouldRenderAtSqrDistance(double distance) {
      double d0 = 64.0 * getViewScale();
      return distance < d0 * d0;
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
   }

   protected void readAdditionalSaveData(CompoundTag compound) {
   }

   protected void addAdditionalSaveData(CompoundTag compound) {
   }
}

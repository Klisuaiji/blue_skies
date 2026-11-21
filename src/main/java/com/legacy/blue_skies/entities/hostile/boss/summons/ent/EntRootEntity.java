package com.legacy.blue_skies.entities.hostile.boss.summons.ent;

import com.legacy.blue_skies.entities.hostile.SpewterEntity;
import com.legacy.blue_skies.entities.hostile.boss.StarlitCrusherEntity;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBoss;
import com.legacy.blue_skies.registries.SkiesDamageSources;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.ItemAbilities;

public class EntRootEntity extends Entity {
   protected static final EntityDataAccessor<Boolean> LARGE = SynchedEntityData.defineId(EntRootEntity.class, EntityDataSerializers.BOOLEAN);
   public int warmupDelayTicks;
   public int lifeTicks = this.isLarge() ? 80 : 50;
   public int downwardTranslateAmount = 0;
   private boolean sentSpikeEvent;
   private LivingEntity caster;
   private UUID casterUuid;

   public EntRootEntity(EntityType<? extends EntRootEntity> typeIn, Level worldIn) {
      super(typeIn, worldIn);
   }

   public EntRootEntity(Level worldIn, double x, double y, double z, float rotation, int warmupDelay, LivingEntity casterIn, boolean isRootLarge) {
      this(SkiesEntityTypes.ENT_ROOT, worldIn);
      this.warmupDelayTicks = warmupDelay;
      this.setCaster(casterIn);
      this.setYRot(rotation * (180.0F / (float)Math.PI));
      this.setPos(x, y, z);
      this.lifeTicks = this.isLarge() ? 80 : 50;
      this.setRootIsLarge(isRootLarge);
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      builder.define(LARGE, false);
   }

   public void move(MoverType type, Vec3 vec) {
      super.move(type, Vec3.ZERO);
   }

   public void setCaster(LivingEntity livingIn) {
      this.caster = livingIn;
      this.casterUuid = livingIn == null ? null : livingIn.getUUID();
   }

   @Nullable
   public LivingEntity getCaster() {
      if (this.caster == null && this.casterUuid != null && this.level() instanceof ServerLevel) {
         Entity entity = ((ServerLevel)this.level()).getEntity(this.casterUuid);
         if (entity instanceof LivingEntity) {
            this.caster = (LivingEntity)entity;
         }
      }

      return this.caster;
   }

   public void readAdditionalSaveData(CompoundTag compound) {
      this.setRootIsLarge(compound.getBoolean("Large"));
      this.warmupDelayTicks = compound.getInt("Warmup");
      if (compound.hasUUID("OwnerUUID")) {
         this.casterUuid = compound.getUUID("OwnerUUID");
      }
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      compound.putBoolean("Large", this.isLarge());
      compound.putInt("Warmup", this.warmupDelayTicks);
      if (this.casterUuid != null) {
         compound.putUUID("OwnerUUID", this.casterUuid);
      }
   }

   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         if (this.lifeTicks <= 5) {
            this.downwardTranslateAmount--;
         }

         this.lifeTicks--;
      } else if (--this.warmupDelayTicks < 0) {
         if (this.warmupDelayTicks <= -2 && this.lifeTicks > 5) {
            for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2, 0.0, 0.2))) {
               if (!(livingentity instanceof EntWallEntity) && !(livingentity instanceof SpewterEntity)) {
                  this.damage(livingentity);
               }
            }
         }

         if (!this.sentSpikeEvent) {
            this.level().broadcastEntityEvent(this, (byte)4);
            this.sentSpikeEvent = true;
         }

         if (--this.lifeTicks < 0) {
            this.discard();
         }
      }

      if (!this.level().getBlockState(this.blockPosition()).getCollisionShape(this.level(), this.blockPosition()).isEmpty()
         || this.getCaster() != null && !this.getCaster().isAlive()) {
         this.discard();
      }

      if (this.tickCount == 1) {
         this.playSound(SoundEvents.CROP_BREAK, 1.0F, this.random.nextFloat() * 0.2F + 0.85F);
      }
   }

   private void damage(LivingEntity target) {
      LivingEntity caster = this.getCaster();
      int timeUntilHit = 0;
      if (caster instanceof StarlitCrusherEntity crusher) {
         timeUntilHit = crusher.getDifficultyID() == ISkyBoss.BossLevel.SILVER.getId()
            ? 8
            : (crusher.getDifficultyID() == ISkyBoss.BossLevel.GOLD.getId() ? 10 : (crusher.getDifficultyID() >= ISkyBoss.BossLevel.PLATINUM.getId() ? 14 : 0));
      }

      if (target.isAlive() && !target.isInvulnerable() && target != caster && target.invulnerableTime <= timeUntilHit) {
         float damage = this.isLarge() ? 5.0F : 3.0F;
         if (caster != null && caster.isAlliedTo(target)) {
            return;
         }

         if (target.isUsingItem() && target.getUseItem().canPerformAction(ItemAbilities.SHIELD_BLOCK)) {
            target.stopUsingItem();
         }

         target.hurt(SkiesDamageSources.get(target.level()).crusherRoot(this, caster), damage);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      super.handleEntityEvent(id);
      if (id == 4) {
         this.playSound(SoundEvents.CHORUS_FLOWER_GROW, 1.0F, this.random.nextFloat() * 0.2F + 0.85F);
      }
   }

   public boolean hurt(DamageSource source, float amount) {
      return source == this.damageSources().inWall() ? false : super.hurt(source, amount);
   }

   @OnlyIn(Dist.CLIENT)
   public float getLifeTicks() {
      return this.lifeTicks;
   }

   public boolean isLarge() {
      return (Boolean)this.entityData.get(LARGE);
   }

   public void setRootIsLarge(boolean staticIn) {
      this.entityData.set(LARGE, staticIn);
   }
}

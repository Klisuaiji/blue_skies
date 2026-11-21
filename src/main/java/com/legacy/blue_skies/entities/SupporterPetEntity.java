package com.legacy.blue_skies.entities;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.entities.util.MovingEntitySound;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.blue_skies.util.StringUtil;
import com.legacy.structure_gel.api.util.Positions;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.MoveControl;

import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SupporterPetEntity extends TamableAnimal {
   private static final EntityDataAccessor<Byte> TYPE_ID = SynchedEntityData.defineId(SupporterPetEntity.class, EntityDataSerializers.BYTE);
   @OnlyIn(Dist.CLIENT)
   private SoundInstance flapSound;

   public SupporterPetEntity(Level worldIn, byte typeId) {
      this(SkiesEntityTypes.SUPPORTER_PET, worldIn);
      this.setTypeID(typeId);
   }

   public SupporterPetEntity(EntityType<? extends SupporterPetEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this) {
         public boolean canContinueToUse() {
            return !SupporterPetEntity.this.getMoveControl().hasWanted() && super.canContinueToUse();
         }
      });
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(TYPE_ID, (byte)0);
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.3F);
   }

   public void tick() {
      super.tick();
      if (!this.level().isClientSide()) {
         Player owner = this.getPlayerOwner();
         if (owner != null) {
            if (!BlueSkies.ML_SUPPORTER.getRank(owner).hasPerks()) {
               BlueSkies.LOGGER.info(owner.getDisplayName().getString() + " is no longer ranked. Despawning their pet.");
               this.discard();
            }

            SkiesPlayer skyPlayer = SkiesPlayer.get(owner);
            if (skyPlayer != null) {
               if (skyPlayer.getSupporterPet() == null) {
                  skyPlayer.setSupporterPet(this);
               }

               if (this.tickCount > 1 && (skyPlayer.getSupporterPet().getUUID() != this.getUUID() || !skyPlayer.isSupporterPetEnabled())) {
                  this.discard();
               }

               if (this.tickCount > 1 && !owner.level().dimension().location().equals(this.level().dimension().location())) {
                  skyPlayer.setSupporterPet(null);
                  this.discard();
               }

               float distFromOwner = this.distanceTo(owner);
               if (this.getTypeID() != 0) {
                  if (distFromOwner > 25.0F) {
                     this.teleportTo(owner.getX(), owner.getY(), owner.getZ());
                  } else if (distFromOwner > 7.0F) {
                     this.getNavigation().moveTo(owner, 1.0);
                     this.getLookControl().setLookAt(owner, 40.0F, 40.0F);
                  }
               } else {
                  this.setNoGravity(true);
                  if (!(distFromOwner > 25.0F) && (this.getSensing().hasLineOfSight(owner) || !(distFromOwner > 15.0F))) {
                     if (distFromOwner > 7.0F) {
                        Vec3 targetEyes = owner.getEyePosition(1.0F);
                        this.moveControl.setWantedPosition(targetEyes.x, targetEyes.y - 0.3, targetEyes.z, 1.0);
                        this.getLookControl().setLookAt(owner, 40.0F, 40.0F);
                     } else if (this.level().getBlockState(this.blockPosition().below()).canOcclude()) {
                        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.001, 0.0));
                     }
                  } else {
                     this.teleportTo(owner.getX(), owner.getY(), owner.getZ());
                  }

                  if (!this.moveControl.hasWanted()) {
                     this.setDeltaMovement(this.getDeltaMovement().multiply(0.95F, 0.9F, 0.95F));
                  }
               }
            } else {
               this.discard();
            }

            boolean playerInDim = owner.level().dimension().location().equals(this.level().dimension().location());
            if (this.tickCount > 1 && !playerInDim) {
               this.discard();
            }

            if (this.tickCount == 1) {
               this.level().broadcastEntityEvent(this, (byte)4);
            }
         } else if (this.tickCount > 20 && owner == null) {
            this.discard();
         }
      }

      if (this.getTypeID() == 0 && this.level().isClientSide()) {
         this.playFlightSound();
      }

      this.refreshDimensions();
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag
   ) {
      Player owner = this.getPlayerOwner();
      if (owner != null) {
         SkiesPlayer.ifPresent(owner, skyPlayer -> skyPlayer.setSupporterPet(this));
      }

      return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   public void remove(RemovalReason reason) {
      this.level().broadcastEntityEvent(this, (byte)4);
      super.remove(reason);
   }

   public boolean isIgnoringBlockTriggers() {
      return true;
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putByte("GetTypeId", this.getTypeID());
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.setTypeID(compound.getByte("GetTypeId"));
   }

   public void setTypeID(byte id) {
      if (id == 0) {
         this.moveControl = new SupporterPetEntity.FlyingMovementController(this);
      } else {
         this.goalSelector.addGoal(0, new FloatGoal(this));
         this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
      }

      this.entityData.set(TYPE_ID, id);
   }

   public byte getTypeID() {
      return (Byte)this.entityData.get(TYPE_ID);
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      switch (id) {
         case 4:
            for (int i = 0; i < 20; i++) {
               double dx = this.random.nextGaussian() * 0.02;
               double dy = this.random.nextGaussian() * 0.02;
               double dz = this.random.nextGaussian() * 0.02;
               this.level()
                  .addParticle(
                     ParticleTypes.POOF,
                     this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
                     this.getY() + this.random.nextFloat() * this.getBbHeight(),
                     this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
                     dx,
                     dy,
                     dz
                  );
            }
            break;
         default:
            super.handleEntityEvent(id);
      }
   }

   protected void handleNetherPortal() {
   }

   public boolean isPushable() {
      return false;
   }

   public boolean canBeCollidedWith() {
      return false;
   }

   public boolean isPickable() {
      return false;
   }

   protected void doPush(Entity entityIn) {
   }

   protected void pushEntities() {
   }

   protected SoundEvent getAmbientSound() {
      if (this.shouldGangster()) {
         return SkiesSounds.GANGSTER;
      }

      switch (this.getTypeID()) {
         case 1:
            return SkiesSounds.ENTITY_STONELET_IDLE;
         default:
            return null;
      }
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      switch (this.getTypeID()) {
         case 0:
            return SkiesSounds.ENTITY_NYCTOFLY_HURT;
         case 1:
            return SkiesSounds.ENTITY_STONELET_HURT;
         case 2:
            return SkiesSounds.ENTITY_ARTIFICIAL_GOLEM_HURT;
         default:
            return super.getHurtSound(damageSourceIn);
      }
   }

   protected SoundEvent getDeathSound() {
      switch (this.getTypeID()) {
         case 0:
            return SkiesSounds.ENTITY_NYCTOFLY_DEATH;
         case 1:
            return SkiesSounds.ENTITY_STONELET_DEATH;
         case 2:
            return SkiesSounds.ENTITY_ARTIFICIAL_GOLEM_DEATH;
         default:
            return super.getDeathSound();
      }
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      switch (this.getTypeID()) {
         case 0:
            return;
         case 1:
            this.playSound(SoundEvents.COW_STEP, 0.1F, 1.4F);
            break;
         case 2:
            this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.5F, 1.4F);
            break;
         default:
            super.playStepSound(pos, blockIn);
      }
   }

   public boolean shouldGangster() {
      return this.hasCustomName() && StringUtil.toLower(this.getCustomName().getString()).equals("gangster");
   }

   public float getVoicePitch() {
      return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + (this.shouldGangster() ? 1.0F : 1.5F);
   }

   public int getAmbientSoundInterval() {
      return this.shouldGangster() ? 360 : super.getAmbientSoundInterval();
   }

   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      switch (this.getTypeID()) {
         case 0:
            return sizeIn.height / 2.0F;
         case 2:
            return 0.7F;
         default:
            return super.getStandingEyeHeight(poseIn, sizeIn);
      }
   }

   public boolean hurt(DamageSource source, float amount) {
      return source == this.damageSources().fellOutOfWorld() ? super.hurt(source, amount) : false;
   }

   @Nullable
   public Player getPlayerOwner() {
      LivingEntity owner = this.getOwner();
      return owner != null && owner instanceof Player ? (Player)owner : null;
   }

   public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob ageable) {
      return null;
   }

   @OnlyIn(Dist.CLIENT)
   public void playFlightSound() {
      if (!this.isSilent() && this.level().isClientSide) {
         SoundManager soundHandler = Minecraft.getInstance().getSoundManager();
         if (this.flapSound == null && !soundHandler.isActive(this.flapSound) || this.flapSound != null && !soundHandler.isActive(this.flapSound)) {
            soundHandler.play(this.flapSound = new MovingEntitySound(this, SkiesSounds.ENTITY_NYCTOFLY_FLYING, 0.3F, 1.5F));
         }
      }
   }

   class FlyingMovementController extends MoveControl {
      private SupporterPetEntity nycto = SupporterPetEntity.this;

      public FlyingMovementController(SupporterPetEntity bug) {
         super(bug);
      }

      public void tick() {
         if (this.operation == MoveControl.Operation.MOVE_TO) {
            Vec3 difference = new Vec3(this.wantedX - this.nycto.getX(), this.wantedY - this.nycto.getY(), this.wantedZ - this.nycto.getZ());
            double d0 = difference.length();
            if (d0 < this.nycto.getBoundingBox().getSize()) {
               this.operation = MoveControl.Operation.WAIT;
               this.nycto.setDeltaMovement(this.nycto.getDeltaMovement().scale(0.5));
            } else {
               this.nycto.setDeltaMovement(this.nycto.getDeltaMovement().add(difference.scale(this.getSpeedModifier() * 0.05 / d0)));
               double d4 = this.wantedX - this.nycto.getX();
               double d5 = this.wantedZ - this.nycto.getZ();
               this.nycto.setYRot(-((float)Mth.atan2(d4, d5)) * (180.0F / (float)Math.PI));
               this.nycto.setYBodyRot(this.nycto.getYRot());
            }

            if (EntityUtil.getDistanceToPos(this.nycto.blockPosition(), Positions.blockPos(this.wantedX, this.wantedY, this.wantedZ)) < 3.0F) {
               this.operation = MoveControl.Operation.WAIT;
               this.nycto.setDeltaMovement(this.nycto.getDeltaMovement().scale(0.5));
            }
         }
      }

      public double getSpeedModifier() {
         return 0.6;
      }
   }
}

package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.entities.util.base.SkiesMonsterEntity;
import com.legacy.structure_gel.api.util.Positions;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

public class WhistleshellCrabEntity extends SkiesMonsterEntity {
   public static final EntityDataAccessor<Byte> TYPE = SynchedEntityData.defineId(WhistleshellCrabEntity.class, EntityDataSerializers.BYTE);
   private float prevSidwaysAnim;
   private float sidewaysAnim;
   private float swingAnim;
   private float prevSwingAnim;
   private boolean playSidewaysAnim;
   private boolean playAttackAnim;
   private int attackTimer;

   public WhistleshellCrabEntity(EntityType<? extends WhistleshellCrabEntity> type, Level worldIn) {
      super(type, worldIn);
      this.setPathfindingMalus(PathType.WATER, 0.0F);
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 26.0)
         .add(Attributes.MOVEMENT_SPEED, 0.3)
         .add(Attributes.ARMOR, 2.0)
         .add(Attributes.STEP_HEIGHT, 1.0);
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      this.getEntityData().define(TYPE, (byte)0);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.goalSelector.addGoal(3, new WhistleshellCrabEntity.CrabMeleeAttackGoal(this, 1.2F, true));
      this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn
   ) {
      spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
      this.setCrabType((byte)worldIn.getRandom().nextInt(2));
      int height = worldIn.getHeight(Types.OCEAN_FLOOR, (int)this.getX(), (int)this.getZ());
      BlockPos newPos = Positions.blockPos(this.getX(), height, this.getZ());
      if (reason == MobSpawnType.NATURAL && worldIn.isWaterAt(this.blockPosition().below()) && !worldIn.isWaterAt(this.blockPosition())) {
         this.teleportTo(newPos.getX(), worldIn.noCollision(this.getBoundingBox()) ? newPos.getY() : newPos.getY() + 5, newPos.getZ());
      }

      return spawnDataIn;
   }

   public void tick() {
      super.tick();
      if (this.level().isClientSide()) {
         this.prevSidwaysAnim = this.sidewaysAnim;
         this.prevSwingAnim = this.swingAnim;
         if (this.playSidewaysAnim) {
            this.sidewaysAnim = Mth.clamp(this.sidewaysAnim + 1.0F, 0.0F, 6.0F);
         } else {
            this.sidewaysAnim = Mth.clamp(this.sidewaysAnim - 1.0F, 0.0F, 6.0F);
         }

         if (this.playAttackAnim) {
            this.swingAnim = Mth.clamp(this.swingAnim + 1.0F, 0.0F, 6.0F);
         } else {
            this.swingAnim = Mth.clamp(this.swingAnim - 1.0F, 0.0F, 6.0F);
         }
      } else {
         if (this.attackTimer > 0) {
            this.attackTimer--;
         }

         if (this.attackTimer == 0) {
            this.setAttackAnim(false);
         }
      }

      boolean walking = this.getMoveForward() > 0.0F;
      if ((!walking || this.getTarget() != null)
         && (!walking || this.getTarget() == null || !(this.distanceTo(this.getTarget()) > 3.0F + this.getTarget().getBbWidth()))) {
         this.level().broadcastEntityEvent(this, (byte)5);
      } else {
         this.level().broadcastEntityEvent(this, (byte)4);
      }

      if (this.isInWaterOrBubble() && this.tickCount % 40 == 0 && this.getTarget() != null && this.distanceTo(this.getTarget()) <= 10.0F && this.onGround()) {
         Vec3 vector3d = this.getDeltaMovement();
         Vec3 vector3d1 = new Vec3(this.getTarget().getX() - this.getX(), this.getTarget().getY() - this.getY(), this.getTarget().getZ() - this.getZ());
         if (vector3d1.lengthSqr() > 1.0E-7) {
            vector3d1 = vector3d1.normalize().scale(0.4).add(vector3d.scale(0.2));
         }

         this.setDeltaMovement(vector3d1.x, vector3d1.y + 0.1, vector3d1.z);
      }
   }

   public void setCrabType(byte type) {
      this.entityData.set(TYPE, type);
   }

   public byte getCrabType() {
      return (Byte)this.entityData.get(TYPE);
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putByte("CrabType", this.getCrabType());
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.setCrabType(compound.getByte("CrabType"));
   }

   public boolean doHurtTarget(Entity entityIn) {
      if (!this.level().isClientSide()) {
         this.setAttackAnim(true);
      }

      return super.doHurtTarget(entityIn);
   }

   protected float getWaterSlowDown() {
      return 0.95F;
   }

   public boolean canDrownInFluidType(FluidType type) {
      return super.canDrownInFluidType(type) && type != NeoForgeMod.WATER_TYPE.value();
   }


   public boolean isPushedByFluid() {
      return false;
   }

   protected AABB getAttackBoundingBox() {
      return super.getAttackBoundingBox().inflate(0.5);
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   public boolean hurt(DamageSource source, float amount) {
      return super.hurt(source, amount);
   }

   protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
      return 0.7F;
   }

   public int getMaxSpawnClusterSize() {
      return 1;
   }

   @OnlyIn(Dist.CLIENT)
   public float getSidewaysAnimationScale(float pctIn) {
      return Mth.lerp(pctIn, this.prevSidwaysAnim, this.sidewaysAnim) / 6.0F;
   }

   @OnlyIn(Dist.CLIENT)
   public float getAttackAnimationScale(float pctIn) {
      return Mth.lerp(pctIn, this.prevSwingAnim, this.swingAnim) / 6.0F;
   }

   protected void setAttackAnim(boolean attacking) {
      if (attacking) {
         this.level().broadcastEntityEvent(this, (byte)6);
         this.attackTimer = 10;
         this.playAttackAnim = true;
      } else {
         this.level().broadcastEntityEvent(this, (byte)7);
         this.playAttackAnim = false;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      switch (id) {
         case 4:
            this.playSidewaysAnim = true;
            break;
         case 5:
            this.playSidewaysAnim = false;
            break;
         case 6:
            this.playAttackAnim = true;
            break;
         case 7:
            this.playAttackAnim = false;
            break;
         default:
            super.handleEntityEvent(id);
      }
   }

   class CrabMeleeAttackGoal extends MeleeAttackGoal {
      public CrabMeleeAttackGoal(PathfinderMob creature, double speedIn, boolean useLongMemory) {
         super(creature, speedIn, useLongMemory);
      }

      public void stop() {
         super.stop();
      }
   }
}

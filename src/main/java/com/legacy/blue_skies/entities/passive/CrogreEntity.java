package com.legacy.blue_skies.entities.passive;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.entities.util.base.SkiesMountEntity;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.structure_gel.api.util.Positions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.MoveControl;

import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Vector3f;

public class CrogreEntity extends SkiesMountEntity {
   private float prevJumpAnim;
   private float jumpAnim;
   private float prevSwimAnim;
   private float swimAnim;
   private boolean playSwimAnim;

   public CrogreEntity(EntityType<? extends CrogreEntity> type, Level worldIn) {
      super(type, worldIn);
      this.moveControl = new CrogreEntity.MoveHelperController(this);
      this.setPathfindingMalus(PathType.WATER, 0.0F);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
      this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(new ItemLike[]{this.getTameItem()}), false));
      this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0) {
         protected Vec3 getPosition() {
            return LandRandomPos.getPos(this.mob, this.mob.isInWaterOrBubble() ? 60 : 40, 10);
         }
      });
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.3F);
   }

   public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
      return worldIn.getBiome(pos) != null && ((Biome)worldIn.getBiome(pos).value()).getPrecipitationAt(pos) == Precipitation.RAIN ? 0.5F : 0.0F;
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return distance > 10.0F ? super.causeFallDamage(distance - 10.0F, damageMultiplier, source) : false;
   }

   protected Vector3f getPassengerAttachmentPoint(Entity passenger, EntityDimensions size, float scale) {
      return new Vector3f(0.0F, size.height - 0.2F * scale, 0.0F);
   }

   public void tick() {
      super.tick();
      if (this.level().isClientSide()) {
         this.prevJumpAnim = this.jumpAnim;
         this.prevSwimAnim = this.swimAnim;
         if (!this.isNoAi() && !this.isInWaterOrBubble() && this.jumping && this.getDeltaMovement().y() > 0.0) {
            this.jumpAnim = Mth.clamp(this.jumpAnim + 1.0F, 0.0F, 6.0F);
         } else {
            this.jumpAnim = Mth.clamp(this.jumpAnim - 1.0F, 0.0F, 6.0F);
         }

         if (this.playSwimAnim) {
            this.swimAnim = Mth.clamp(this.swimAnim + 1.0F, 0.0F, 6.0F);
         } else {
            this.swimAnim = Mth.clamp(this.swimAnim - 1.0F, 0.0F, 6.0F);
         }
      } else if (!this.getNavigation().isDone()) {
         Node path = this.getNavigation().getPath().getEndNode();
         double d1 = path.x - this.getX();
         double d2 = path.z - this.getZ();
         this.setYRot(-((float)Mth.atan2(d1, d2)) * (180.0F / (float)Math.PI));
      }

      if (!this.isInWaterOrBubble()) {
         if (!this.onGround() && (!this.isVehicle() || !(this.getDeltaMovement().y() < 0.0))) {
            this.level().broadcastEntityEvent(this, (byte)4);
         } else {
            this.setJumping(false);
            this.level().broadcastEntityEvent(this, (byte)5);
         }
      }

      if (this.tickCount % 20 == 10) {
         if (this.jumping && this.onGround()) {
            this.setJumping(false);
            this.level().broadcastEntityEvent(this, (byte)5);
         }

         if (!this.isInWaterOrBubble() || this.swimAnim >= 6.0F && this.prevSwimAnim >= 6.0F) {
            this.setCrogreSwimming(false);
         }
      }
   }

   public void setJumping(boolean jumping) {
      super.setJumping(jumping);
      if (jumping && !this.isInWaterOrBubble() && !this.level().isClientSide) {
         this.level().broadcastEntityEvent(this, (byte)10);
      }
   }

   public void setCrogreSwimming(boolean swimming) {
      if (swimming) {
         this.playSwimAnim = true;
         this.level().broadcastEntityEvent(this, (byte)8);
      } else {
         this.playSwimAnim = false;
         this.level().broadcastEntityEvent(this, (byte)9);
      }
   }

   protected SoundEvent getAmbientSound() {
      return this.level().isRainingAt(this.blockPosition().above()) ? SkiesSounds.ENTITY_CROGRE_IDLE_HAPPY : SkiesSounds.ENTITY_CROGRE_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_CROGRE_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_CROGRE_DEATH;
   }

   @Override
   public AgeableMob createChild(AgeableMob ageable) {
      return (AgeableMob)SkiesEntityTypes.CROGRE.create(this.level());
   }

   @OnlyIn(Dist.CLIENT)
   public float getJumpingAnimationScale(float pctIn) {
      return Mth.lerp(pctIn, this.prevJumpAnim, this.jumpAnim) / 4.0F;
   }

   @OnlyIn(Dist.CLIENT)
   public float getSwimmingAnimationScale(float pctIn) {
      return Mth.lerp(pctIn, this.prevSwimAnim, this.swimAnim) / 5.0F;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void handleEntityEvent(byte id) {
      switch (id) {
         case 4:
            this.setJumping(true);
            break;
         case 5:
            this.setJumping(false);
            break;
         case 6:
         case 7:
         default:
            super.handleEntityEvent(id);
            break;
         case 8:
            this.playSwimAnim = true;
            break;
         case 9:
            this.playSwimAnim = false;
            break;
         case 10:
            if (this.level().isClientSide()) {
               EntityUtil.playMovingSound(SkiesSounds.ENTITY_CROGRE_JUMP, this, this.getSoundVolume(), this.getVoicePitch());
            }
            break;
         case 11:
            if (this.level().isClientSide()) {
               EntityUtil.playMovingSound(this.getSwimSound(), this, 0.1F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
            }
      }
   }

   public boolean onClimbable() {
      return false;
   }

   public void onMove(CrogreEntity.MoveHelperController helper, double distanceIn) {
      if (!this.getNavigation().isDone() && this.getPassengers().isEmpty()) {
         Node path = this.getNavigation().getPath().getEndNode();
         BlockPos finalPos = Positions.blockPos(path.x, path.y, path.z);
         Vec3 vector3d = this.getViewVector(1.0F);
         double distanceToFinal = EntityUtil.getDistanceToPos(this.blockPosition(), finalPos);
         if (distanceToFinal < 0.5) {
            return;
         }

         double newMotion = 1.5 * distanceToFinal / 8.5;
         double motion = this.isBaby() ? Math.min(newMotion, 0.5) : Math.min(newMotion, 1.5);
         if (!this.isInWaterOrBubble() && this.onGround()) {
            this.level().broadcastEntityEvent(this, (byte)4);
            this.setJumping(true);
            helper.setWantedPosition(path.x, path.y, path.z, 1.0);
            Vec3 currentMotion = this.getDeltaMovement();
            Vec3 motionTowardsTarget = new Vec3(this.getX() - path.x, 0.0, this.getZ() - path.z);
            if (motionTowardsTarget.lengthSqr() > 1.0E-7) {
               motionTowardsTarget = motionTowardsTarget.normalize().scale(-motion).add(currentMotion);
            }

            this.setDeltaMovement(new Vec3(motionTowardsTarget.x, this.isBaby() ? 0.5 : Math.min(1.0 * distanceToFinal / 5.0, 1.0), motionTowardsTarget.z));
            if (distanceToFinal <= 3.0) {
               this.navigation.stop();
            }
         }

         if (this.isInWaterOrBubble() && this.getPassengers().isEmpty()) {
            if (distanceToFinal <= 2.0) {
               this.navigation.stop();
            }

            if (this.tickCount % 20 == 0) {
               this.setCrogreSwimming(true);
               this.setDeltaMovement(
                  new Vec3(vector3d.x * 0.5, !this.level().noCollision(this.getBoundingBox().inflate(0.5, 0.0, 0.5)) ? 0.2F : 0.0, vector3d.z * 0.5)
               );
            } else if (this.tickCount % 20 == 10) {
               this.setCrogreSwimming(false);
            }
         }
      }
   }

   public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
      return type == NeoForgeMod.WATER_TYPE.value();
   }

   @Override
   public void travel(Vec3 travelVectorIn) {
      if (this.isAlive()) {
         Entity entity = this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);
         if (this.isSaddled() && entity instanceof Player player) {
            this.setYRot(entity.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(entity.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
            float forward = player.zza;
            float backward = 0.0F;
            if (forward <= 0.0F) {
               backward = -forward;
               forward = 0.0F;
            }

            boolean local = this.isControlledByLocalInstance();
            if (local) {
               float f = (float)this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 0.225F;
               this.setSpeed(f);
            }

            Vec3 vector3d = this.getViewVector(1.0F);
            if (forward > 0.0F) {
               if (!this.isInWaterOrBubble() && this.onGround()) {
                  if (local) {
                     if (this.level().isClientSide()) {
                        EntityUtil.playMovingSound(SkiesSounds.ENTITY_CROGRE_JUMP, this, this.getSoundVolume(), this.getVoicePitch());
                     }

                     this.setJumping(true);
                     this.setDeltaMovement(new Vec3(vector3d.x * 1.0, 1.0, vector3d.z * 1.0));
                  } else {
                     if (this.level() instanceof ServerLevel sl) {
                        sl.playSound(player, this, SkiesSounds.ENTITY_CROGRE_JUMP, SoundSource.NEUTRAL, this.getSoundVolume(), this.getVoicePitch());
                     }

                     this.level().broadcastEntityEvent(this, (byte)4);
                  }
               } else if (this.isInWaterOrBubble()) {
                  if (this.tickCount % 20 == 0) {
                     if (local) {
                        this.setCrogreSwimming(true);
                        if (this.level().isClientSide()) {
                           EntityUtil.playMovingSound(this.getSwimSound(), this, 0.1F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                        }

                        float strength = 0.8F;
                        this.setDeltaMovement(
                           new Vec3(
                              vector3d.x * strength,
                              !this.level().noCollision(this.getBoundingBox().inflate(0.5, 0.0, 0.5)) ? 0.2F : 0.0,
                              vector3d.z * strength
                           )
                        );
                     }

                     if (this.level() instanceof ServerLevel sl) {
                        sl.playSound(
                           player, this, this.getSwimSound(), SoundSource.NEUTRAL, 0.1F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F
                        );
                     }
                  } else if (this.tickCount % 20 == 10 && local) {
                     this.setCrogreSwimming(false);
                  }
               }
            } else if (local) {
               float stopMul = backward > 0.0F ? 0.85F : 0.95F;
               this.setDeltaMovement(this.getDeltaMovement().multiply(stopMul, 1.0, stopMul));
            }

            if (local) {
               super.travel(new Vec3(0.0, 0.0, forward));
            } else {
               this.setDeltaMovement(Vec3.ZERO);
            }
         } else {
            super.travel(travelVectorIn);
         }
      }
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      if (!super.mobInteract(player, hand).consumesAction() && itemstack.getItem() == this.getTameItem()) {
         if (!player.isCreative()) {
            itemstack.shrink(1);
         }

         if (!this.level().isClientSide()) {
            if (!this.isTamed()) {
               if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {
                  this.navigation.stop();
                  this.setTarget((LivingEntity)null);
                  this.playTameEffect(true);
                  this.level().broadcastEntityEvent(this, (byte)7);
                  this.setTamed(true);
               } else {
                  this.playTameEffect(false);
                  this.level().broadcastEntityEvent(this, (byte)6);
               }
            } else if (this.getHealth() < this.getMaxHealth()) {
               this.heal(2.0F);
            }

            this.playSound(SoundEvents.GENERIC_EAT, this.getSoundVolume(), this.getVoicePitch());
         }

         return InteractionResult.SUCCESS;
      } else {
         return super.mobInteract(player, hand);
      }
   }

   public void jumpInFluid(FluidType type) {
      if (this.isControlledByLocalInstance()) {
         this.setDeltaMovement(
            this.getDeltaMovement()
               .add(
                  0.0, this.isUnderWater() ? 0.05F : (this.level().isWaterAt(Positions.blockPos(this.getEyePosition().add(0.0, -0.1F, 0.0))) ? 0.02 : 0.0), 0.0
               )
         );
      } else {
         super.jumpInFluid(type);
      }
   }

   @Override
   public Item getSaddleItem() {
      return Items.SADDLE;
   }

   @Override
   public Item getTameItem() {
      return SkiesItems.bug_guts;
   }

   @Override
   public boolean useTraditionalTravel() {
      return true;
   }

   @Override
   public int getMaxInventorySlots() {
      return 1;
   }

   class MoveHelperController extends MoveControl {
      private final CrogreEntity frog;

      public MoveHelperController(CrogreEntity frogIn) {
         super(frogIn);
         this.frog = frogIn;
      }

      public void tick() {
         if (!this.frog.getPassengers().isEmpty()) {
            this.operation = MoveControl.Operation.WAIT;
         } else {
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedZ - this.mob.getZ();
            double d3 = d0 * d0 + d1 * d1;
            this.frog.onMove(this, Mth.sqrt((float)d3));
            if (!this.frog.isPathFinding()) {
               this.operation = MoveControl.Operation.WAIT;
            }
         }
      }

      public Operation getAction() {
         return this.operation;
      }
   }
}

package com.legacy.blue_skies.entities.passive;

import com.legacy.blue_skies.entities.ai.FollowDirectlyTemptGoal;
import com.legacy.blue_skies.entities.util.base.SkiesAnimalEntity;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.EntityUtil;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.AgeableMob.AgeableMobGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForgeMod;

public class AzulfoEntity extends SkiesAnimalEntity {
   private AzulfoEntity herdLeader;
   private int herdSize = 1;
   public int attackTimer = 11;

   public AzulfoEntity(EntityType<? extends AzulfoEntity> type, Level world) {
      super(type, world);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
      this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
      this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.7F, false) {
         public boolean canUse() {
            return !AzulfoEntity.this.isBaby() && super.canUse();
         }
      });
      this.goalSelector.addGoal(3, new FollowDirectlyTemptGoal(this, 1.4, Ingredient.of(new ItemLike[]{SkiesItems.winter_leaves})));
      this.goalSelector.addGoal(4, new AzulfoEntity.FollowHerdLeaderGoal(this));
      this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.3));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.25, 0.01F) {
         public boolean canUse() {
            return (!AzulfoEntity.this.hasHerdLeader() || AzulfoEntity.this.isHerdLeader()) && super.canUse();
         }
      });
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 20.0)
         .add(Attributes.MOVEMENT_SPEED, 0.15F)
         .add(Attributes.ARMOR, 4.0)
         .add(Attributes.ATTACK_DAMAGE, 4.0)
         .add(Attributes.ATTACK_KNOCKBACK)
         .add((Attribute)NeoForgeMod.STEP_HEIGHT.value(), 1.0);
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag
   ) {
      super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
      if (!this.isBaby()) {
         if (spawnDataIn == null) {
            spawnDataIn = new AzulfoEntity.HerdData(this);
         } else {
            this.joinHerd(((AzulfoEntity.HerdData)spawnDataIn).herdLeader);
         }
      }

      return spawnDataIn;
   }

   public void aiStep() {
      super.aiStep();
      if (this.attackTimer < 11) {
         this.attackTimer++;
      }

      if (this.isHerdLeader() && this.level().random.nextInt(200) == 1) {
         List<AzulfoEntity> list = this.level().getEntitiesOfClass(AzulfoEntity.class, this.getBoundingBox().inflate(20.0, 20.0, 20.0));
         if (list.size() <= 1) {
            this.herdSize = 1;
         }
      }
   }

   protected AABB getAttackBoundingBox() {
      return super.getAttackBoundingBox().inflate(0.6F);
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_AZULFO_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_AZULFO_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_AZULFO_DEATH;
   }

   @Override
   public Item getMainBreedingItem() {
      return SkiesItems.winter_leaves;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   public AzulfoEntity createChild(AgeableMob ageable) {
      return (AzulfoEntity)SkiesEntityTypes.AZULFO.create(this.level());
   }

   protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
      return this.isBaby() ? 0.8F : 1.8F;
   }

   public boolean doHurtTarget(Entity entityIn) {
      if (this.attackTimer >= 11) {
         this.attackTimer = 0;
         this.level().broadcastEntityEvent(this, (byte)4);
      }

      return super.doHurtTarget(entityIn);
   }

   public void handleEntityEvent(byte id) {
      if (id == 4) {
         this.attackTimer = 0;
      } else {
         super.handleEntityEvent(id);
      }
   }

   public AzulfoEntity joinHerd(AzulfoEntity herdLeaderIn) {
      this.herdLeader = herdLeaderIn;
      herdLeaderIn.increaseHerdSize();
      return herdLeaderIn;
   }

   public void leaveHerd() {
      this.herdLeader.decreaseHerdSize();
      this.herdLeader = null;
   }

   private void increaseHerdSize() {
      this.herdSize++;
   }

   private void decreaseHerdSize() {
      this.herdSize--;
   }

   public boolean canHerdGrow() {
      return this.isHerdLeader() && this.herdSize < this.getMaxHerdSize();
   }

   public int getMaxSpawnedInChunk() {
      return this.getMaxHerdSize();
   }

   public int getMaxHerdSize() {
      return super.getMaxSpawnClusterSize();
   }

   public boolean isHerdLeader() {
      return this.herdSize > 1;
   }

   public boolean inRangeOfHerdLeader() {
      return this.distanceTo(this.herdLeader) <= 20.0;
   }

   public void moveToHerdLeader() {
      if (this.isPosTooFarFromLeader(this.blockPosition())) {
         this.getNavigation().moveTo(this.herdLeader, 1.45);
      }
   }

   public boolean hasHerdLeader() {
      return this.herdLeader != null && this.herdLeader.isAlive();
   }

   public boolean isPosTooFarFromLeader(BlockPos pos) {
      return this.hasHerdLeader() && EntityUtil.getDistanceToPos(pos, this.herdLeader.blockPosition()) > 12.0;
   }

   public void getHerdMembers(Stream<AzulfoEntity> membersIn) {
      membersIn.limit(this.getMaxHerdSize() - this.herdSize).filter(p_212801_1_ -> p_212801_1_ != this).forEach(member -> {
         if (!member.isBaby()) {
            member.joinHerd(this);
         }
      });
   }

   public class FollowHerdLeaderGoal extends Goal {
      private final AzulfoEntity taskOwner;
      private int navigateTimer;
      private int cooldown;

      public FollowHerdLeaderGoal(AzulfoEntity taskOwnerIn) {
         this.taskOwner = taskOwnerIn;
         this.cooldown = this.getNewCooldown(taskOwnerIn);
      }

      protected int getNewCooldown(AzulfoEntity taskOwnerIn) {
         return 200 + taskOwnerIn.getRandom().nextInt(200) % 20;
      }

      public boolean canUse() {
         if (this.taskOwner.isHerdLeader() || this.taskOwner.isBaby()) {
            return false;
         } else if (this.taskOwner.hasHerdLeader()) {
            return true;
         } else if (this.cooldown > 0) {
            this.cooldown--;
            return false;
         } else {
            this.cooldown = this.getNewCooldown(this.taskOwner);
            Predicate<AzulfoEntity> canJoinHerd = azulfo -> azulfo.canHerdGrow() || !azulfo.hasHerdLeader();
            List<AzulfoEntity> list = this.taskOwner
               .level()
               .getEntitiesOfClass(AzulfoEntity.class, this.taskOwner.getBoundingBox().inflate(20.0, 20.0, 20.0), canJoinHerd);
            AzulfoEntity azulfos = list.stream().filter(AzulfoEntity::canHerdGrow).findAny().orElse(this.taskOwner);
            azulfos.getHerdMembers(list.stream().filter(azulfo -> !azulfo.hasHerdLeader()));
            return this.taskOwner.hasHerdLeader();
         }
      }

      public boolean canContinueToUse() {
         return this.taskOwner.hasHerdLeader() && this.taskOwner.inRangeOfHerdLeader();
      }

      public void start() {
         this.navigateTimer = 0;
      }

      public void stop() {
         this.taskOwner.leaveHerd();
      }

      public void tick() {
         if (--this.navigateTimer <= 0) {
            this.navigateTimer = 10;
            this.taskOwner.moveToHerdLeader();
         }
      }
   }

   public static class HerdData extends AgeableMobGroupData implements SpawnGroupData {
      public final AzulfoEntity herdLeader;

      public HerdData(AzulfoEntity herdLeaderIn) {
         super(true);
         this.herdLeader = herdLeaderIn;
      }
   }
}

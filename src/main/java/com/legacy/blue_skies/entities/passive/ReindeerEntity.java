package com.legacy.blue_skies.entities.passive;

import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.entities.util.base.SkiesMountEntity;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;
import org.joml.Vector3f;

public class ReindeerEntity extends SkiesMountEntity {
   private static final float NATURAL_FESTIVE_CHANCE = 0.4F;
   private static final EntityDataAccessor<Integer> FESTIVE_STATUS = SynchedEntityData.defineId(ReindeerEntity.class, EntityDataSerializers.INT);

   public ReindeerEntity(EntityType<? extends ReindeerEntity> type, Level world) {
      super(SkiesEntityTypes.REINDEER, world);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
      this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
      this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(new ItemLike[]{this.getTameItem()}), false));
      this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.15F);
   }

   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData group, CompoundTag nbt) {
      this.setFestiveStatus(this.random.nextFloat() < 0.4F ? 1 : 0);
      return super.finalizeSpawn(level, difficulty, reason, group, nbt);
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_REINDEER_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_REINDEER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_REINDEER_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.HORSE_STEP, 0.15F, 1.0F);
   }

   public int getAmbientSoundInterval() {
      return 460;
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   @Override
   public boolean isFood(ItemStack stack) {
      return this.isTamed() ? stack.getItem() == this.getTameItem() : false;
   }

   public ReindeerEntity createChild(AgeableMob ageable) {
      ReindeerEntity child = new ReindeerEntity(SkiesEntityTypes.REINDEER, this.level());
      if (BlueSkiesConfig.COMMON.isChristmas() && this.getFestiveStatus() > 0 && ((ReindeerEntity)ageable).getFestiveStatus() > 0) {
         child.setFestiveStatus(2);
      }

      return child;
   }

   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return this.isBaby() ? this.getBbHeight() : 1.5F;
   }

   protected Vector3f getPassengerAttachmentPoint(Entity passenger, EntityDimensions size, float scale) {
      return new Vector3f(0.0F, size.height - 0.2F * scale, 0.0F);
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      if (!super.mobInteract(player, hand).consumesAction() && itemstack.getItem() == this.getTameItem() && !this.isTamed()) {
         if (!player.isCreative()) {
            itemstack.shrink(1);
         }

         if (!this.level().isClientSide()) {
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
         }

         if (player.level().isClientSide()) {
            player.level().playSound(player, player.blockPosition(), SoundEvents.HORSE_EAT, SoundSource.AMBIENT, 1.0F, 1.0F);
         }

         return InteractionResult.SUCCESS;
      } else {
         return super.mobInteract(player, hand);
      }
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return distance > 5.0F ? super.causeFallDamage(distance - 5.0F, damageMultiplier, source) : false;
   }

   @Override
   public Item getSaddleItem() {
      return Items.SADDLE;
   }

   @Override
   public Item getTameItem() {
      return SkiesItems.pine_fruit;
   }

   @Override
   public int getMaxInventorySlots() {
      return 1;
   }

   @Override
   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(FESTIVE_STATUS, 0);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("FestiveStatus", this.getFestiveStatus());
   }

   @Override
   public void load(CompoundTag compound) {
      super.load(compound);
      this.setFestiveStatus(!compound.contains("FestiveStatus") ? (this.random.nextFloat() < 0.4F ? 1 : 0) : compound.getInt("FestiveStatus"));
   }

   public int getFestiveStatus() {
      return (Integer)this.entityData.get(FESTIVE_STATUS);
   }

   public void setFestiveStatus(int flag) {
      this.entityData.set(FESTIVE_STATUS, flag);
   }
}

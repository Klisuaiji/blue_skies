package com.legacy.blue_skies.entities.passive.fish;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.entities.util.interfaces.IVentiumCatchable;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.StringUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.MoveControl;

import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class JellyDrifterEntity extends WaterAnimal implements IVentiumCatchable {
   private static final EntityDataAccessor<Byte> COLOR = SynchedEntityData.defineId(JellyDrifterEntity.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(JellyDrifterEntity.class, EntityDataSerializers.BOOLEAN);
   public float jellyPitch;
   public float prevJellyPitch;
   public float jellyYaw;
   public float prevJellyYaw;

   public JellyDrifterEntity(EntityType<? extends JellyDrifterEntity> type, Level world) {
      super(type, world);
      this.moveControl = new JellyDrifterEntity.MoveHelperController(this);
   }

   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0, 80));
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0);
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(COLOR, (byte)0);
      builder.define(FROM_BUCKET, false);
   }

   public boolean requiresCustomPersistence() {
      return this.isFromBucket();
   }

   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return !this.isFromBucket() && !this.hasCustomName();
   }

   public void aiStep() {
      super.aiStep();
      this.prevJellyPitch = this.jellyPitch;
      this.prevJellyYaw = this.jellyYaw;
      if (this.isInWaterOrBubble()) {
         Vec3 vector3d = this.getDeltaMovement();
         double f1 = vector3d.horizontalDistance();
         this.yBodyRot = this.yBodyRot + (-((float)Mth.atan2(vector3d.x, vector3d.z)) * (180.0F / (float)Math.PI) - this.yBodyRot) * 0.1F;
         this.setYRot(this.yBodyRot);
         this.jellyYaw = (float)(this.jellyYaw + (Math.PI * 3.0 / 4.0));
         this.jellyPitch = this.jellyPitch + (-((float)Mth.atan2(f1, vector3d.y)) * (180.0F / (float)Math.PI) - this.jellyPitch) * 0.1F;
      } else {
         this.jellyPitch = 0.0F;
      }
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn
   ) {
      spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
      if (dataTag != null && dataTag.contains("Color", 99)) {
         this.setColor(dataTag.getByte("Color"));
      } else {
         this.setColor(JellyDrifterEntity.Colors.getRandom(this.random).getId());
      }

      return spawnDataIn;
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putByte("Color", this.getColor());
      compound.putBoolean("FromBucket", this.isFromBucket());
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.setColor(compound.getByte("Color"));
      this.setFromVentiumBucket(compound.getBoolean("FromBucket"));
   }

   public void setColor(byte color) {
      this.entityData.set(COLOR, color);
   }

   public byte getColor() {
      return (Byte)this.entityData.get(COLOR);
   }

   public boolean isFromBucket() {
      return (Boolean)this.entityData.get(FROM_BUCKET);
   }

   @Override
   public void setFromVentiumBucket(boolean fromBucket) {
      this.entityData.set(FROM_BUCKET, fromBucket);
   }

   protected PathNavigation createNavigation(Level worldIn) {
      return new WaterBoundPathNavigation(this, worldIn);
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_JELLY_DRIFTER_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_JELLY_DRIFTER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_JELLY_DRIFTER_DEATH;
   }

   protected float getSoundVolume() {
      return 0.7F;
   }

   @Override
   public ItemStack getVentiumBucket() {
      return new ItemStack(SkiesItems.ventium_drifter_bucket);
   }

   protected void doPush(Entity entityIn) {
      super.doPush(entityIn);
      if (entityIn instanceof LivingEntity
         && !(entityIn instanceof JellyDrifterEntity)
         && !((LivingEntity)entityIn).hasEffect(MobEffects.POISON)
         && !entityIn.isInvulnerable()) {
         if (entityIn instanceof Player && ((Player)entityIn).isCreative()) {
            return;
         }

         if (!this.level().isClientSide) {
            ((LivingEntity)entityIn).addEffect(new MobEffectInstance(MobEffects.POISON, 60));
            this.playSound(SkiesSounds.ENTITY_JELLY_DRIFTER_POISON, 1.0F, this.getVoicePitch());
         }
      }
   }

   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack waterBucket = player.getItemInHand(hand);
      if (waterBucket.getItem() == SkiesItems.ventium_water_bucket && this.isAlive()) {
         this.playSound(SoundEvents.BUCKET_FILL_FISH, 1.0F, 1.0F);
         waterBucket.shrink(1);
         ItemStack bucket = this.getVentiumBucket();
         this.setBucketData(bucket);
         if (!this.level().isClientSide) {
            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, bucket);
         }

         if (waterBucket.isEmpty()) {
            player.setItemInHand(hand, bucket);
         } else if (!player.getInventory().add(bucket)) {
            player.drop(bucket, false);
         }

         this.discard();
         return InteractionResult.SUCCESS;
      } else {
         return super.mobInteract(player, hand);
      }
   }

   protected void setBucketData(ItemStack bucket) {
      if (this.hasCustomName()) {
         bucket.setHoverName(this.getCustomName());
      }

      CompoundTag compoundnbt = LegacyNbt.getOrCreate(bucket);
      compoundnbt.putByte("Color", this.getColor());
   }

   public static boolean spawnConditions(
      EntityType<? extends JellyDrifterEntity> type, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223363_3_, RandomSource randomIn
   ) {
      return worldIn.getBlockState(p_223363_3_).getBlock() == Blocks.WATER && worldIn.getBlockState(p_223363_3_.above()).getBlock() == Blocks.WATER;
   }

   public enum Colors {
      SNOW,
      ICY,
      ROSE,
      TEAL,
      AMBER,
      CRIMSON;

      private final ResourceLocation texture = BlueSkies.locate(
         String.format("textures/entity/jelly_drifter/jelly_drifter_%s.png", StringUtil.toLower(this.name()))
      );

      public byte getId() {
         return (byte)this.ordinal();
      }

      public ResourceLocation getTexture() {
         return this.texture;
      }

      public static JellyDrifterEntity.Colors getRandom(RandomSource rand) {
         return rand.nextFloat() < 0.002F ? CRIMSON : values()[rand.nextInt(values().length - 1)];
      }

      public static JellyDrifterEntity.Colors byId(int id) {
         if (id < 0 || id >= values().length) {
            id = 0;
         }

         return values()[id];
      }
   }

   static class MoveHelperController extends MoveControl {
      private final JellyDrifterEntity jellyfish;

      MoveHelperController(JellyDrifterEntity fish) {
         super(fish);
         this.jellyfish = fish;
      }

      public void tick() {
         if (this.jellyfish.isEyeInFluid(FluidTags.WATER)) {
            this.jellyfish.setDeltaMovement(this.jellyfish.getDeltaMovement().add(0.0, 0.005, 0.0));
         }

         if (this.operation == MoveControl.Operation.MOVE_TO && !this.jellyfish.getNavigation().isDone()) {
            double d0 = this.wantedX - this.jellyfish.getX();
            double d1 = this.wantedY - this.jellyfish.getY();
            double d2 = this.wantedZ - this.jellyfish.getZ();
            double d3 = Mth.sqrt((float)(d0 * d0 + d1 * d1 + d2 * d2));
            d1 /= d3;
            float f = (float)(Mth.atan2(d2, d0) * 180.0F / (float)Math.PI) - 90.0F;
            this.jellyfish.setYRot(this.rotlerp(this.jellyfish.getYRot(), f, 90.0F));
            this.jellyfish.yBodyRot = this.jellyfish.getYRot();
            float f1 = (float)(this.speedModifier * this.jellyfish.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
            this.jellyfish.setSpeed(Mth.lerp(0.125F, this.jellyfish.getSpeed(), f1));
            this.jellyfish.setDeltaMovement(this.jellyfish.getDeltaMovement().add(0.0, this.jellyfish.getSpeed() * d1 * 0.1, 0.0));
         } else {
            this.jellyfish.setSpeed(0.0F);
         }
      }
   }
}

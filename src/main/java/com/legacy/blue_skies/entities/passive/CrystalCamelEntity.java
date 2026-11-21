package com.legacy.blue_skies.entities.passive;

import com.legacy.blue_skies.entities.util.base.SkiesMountEntity;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.MoveFunction;
import net.minecraft.world.entity.ai.attributes.Attribute;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags.Items;
import net.neoforged.neoforge.event.EventHooks;
import org.joml.Vector3f;

public class CrystalCamelEntity extends SkiesMountEntity {
   private static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(CrystalCamelEntity.class, EntityDataSerializers.BOOLEAN);

   public CrystalCamelEntity(EntityType<? extends CrystalCamelEntity> type, Level world) {
      super(type, world);
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
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 30.0)
         .add(Attributes.MOVEMENT_SPEED, 0.15F)
         .add(Attributes.STEP_HEIGHT, 1.0);
   }

   @Override
   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(CHESTED, false);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("Chested", this.hasChest());
      if (this.hasChest()) {
         ListTag listnbt = new ListTag();

         for (int i = 2; i < this.getMountInventory().getContainerSize(); i++) {
            ItemStack itemstack = this.getMountInventory().getItem(i);
            if (!itemstack.isEmpty()) {
               CompoundTag compoundnbt = new CompoundTag();
               compoundnbt.putByte("Slot", (byte)i);
               itemstack.save(compoundnbt);
               listnbt.add(compoundnbt);
            }
         }

         compound.put("Items", listnbt);
      }
   }

   @Override
   public void load(CompoundTag compound) {
      super.load(compound);
      this.setChested(compound.getBoolean("Chested"));
      if (this.hasChest()) {
         ListTag listnbt = compound.getList("Items", 10);
         this.initMountInventory();

         for (int i = 0; i < listnbt.size(); i++) {
            CompoundTag compoundnbt = listnbt.getCompound(i);
            int j = compoundnbt.getByte("Slot") & 255;
            if (j >= 2 && j < this.getMountInventory().getContainerSize()) {
               this.getMountInventory().setItem(j, ItemStack.of(compoundnbt));
            }
         }
      }
   }

   public boolean hasChest() {
      return (Boolean)this.entityData.get(CHESTED);
   }

   public void setChested(boolean chested) {
      this.entityData.set(CHESTED, chested);
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_CRYSTAL_CAMEL_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_CRYSTAL_CAMEL_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_CRYSTAL_CAMEL_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SkiesSounds.ENTITY_CRYSTAL_CAMEL_STEP, 0.15F, 1.0F);
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   @Override
   public boolean isFood(ItemStack stack) {
      return stack.getItem() == this.getTameItem();
   }

   public CrystalCamelEntity createChild(AgeableMob ageable) {
      CrystalCamelEntity entitycamel = (CrystalCamelEntity)SkiesEntityTypes.CRYSTAL_CAMEL.create(this.level());
      entitycamel.setTamed(this.isTamed());
      return entitycamel;
   }

   public float getEyeHeight(Pose pose) {
      return this.isBaby() ? 1.0F : 2.3F;
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      if (!this.hasChest() && itemstack.is(Items.CHESTS) && !this.isBaby() && this.isTamed()) {
         itemstack.interactLivingEntity(player, this, hand);
         this.setChested(true);
         itemstack.shrink(1);
         if (player.level().isClientSide()) {
            player.level().playSound(player, player.blockPosition(), SoundEvents.LLAMA_CHEST, SoundSource.AMBIENT, 1.0F, 1.0F);
         }

         return InteractionResult.SUCCESS;
      } else if (itemstack.getItem() == this.getTameItem() && !this.isTamed()) {
         if (!player.isCreative()) {
            itemstack.shrink(1);
         }

         if (!this.level().isClientSide()) {
            if (this.random.nextInt(5) == 0 && !EventHooks.onAnimalTame(this, player)) {
               this.navigation.stop();
               this.setTarget((LivingEntity)null);
               this.playTameEffect(true);
               this.level().broadcastEntityEvent(this, (byte)7);
               this.setTamed(true);
            } else {
               this.playTameEffect(false);
               this.level().broadcastEntityEvent(this, (byte)6);
            }
         } else if (player.level().isClientSide()) {
            player.level().playSound(player, player.blockPosition(), SoundEvents.LLAMA_EAT, SoundSource.AMBIENT, 1.0F, 1.0F);
         }

         return InteractionResult.SUCCESS;
      } else if (!player.isCrouching() && this.isSaddled() && this.getPassengers().size() < 2) {
         if (!this.level().isClientSide()) {
            player.startRiding(this, true);
         }

         return InteractionResult.SUCCESS;
      } else {
         return super.mobInteract(player, hand);
      }
   }

   protected Vector3f getPassengerAttachmentPoint(Entity passenger, EntityDimensions size, float scale) {
      float f = 0.0F;
      if (this.getPassengers().size() > 1) {
         int i = this.getPassengers().indexOf(passenger);
         if (i == 0) {
            f = 0.2F;
         } else {
            f = -0.6F;
         }

         if (passenger instanceof Animal) {
            f += 0.2F;
         }
      }

      return new Vector3f(0.0F, size.height - 0.3F * scale, f);
   }

   public void positionRider(Entity passenger, MoveFunction callback) {
      super.positionRider(passenger, callback);
   }

   @Override
   public float getSpeed() {
      return this.isVehicle() ? (this.hasChest() ? 0.17F : 0.2F) : 0.3F;
   }

   @Override
   protected float getJumpPower() {
      return this.isVehicle() ? 0.3F : super.getJumpPower();
   }

   @Override
   public Item getSaddleItem() {
      return SkiesItems.camel_saddle;
   }

   @Override
   public Item getTameItem() {
      return SkiesItems.scalefruit;
   }

   @Override
   public int getMaxInventorySlots() {
      return 17;
   }
}

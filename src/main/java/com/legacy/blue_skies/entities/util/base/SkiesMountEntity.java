package com.legacy.blue_skies.entities.util.base;

import com.legacy.blue_skies.client.gui.menu.MountInventoryMenu;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class SkiesMountEntity extends SkiesAnimalEntity implements ContainerListener {
   private static final EntityDataAccessor<Boolean> TAMED = SynchedEntityData.defineId(SkiesMountEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(SkiesMountEntity.class, EntityDataSerializers.BOOLEAN);
   protected boolean mountJumping;
   protected SimpleContainer mountInventory;

   public SkiesMountEntity(EntityType<? extends SkiesMountEntity> type, Level worldIn) {
      super(type, worldIn);
      this.initMountInventory();
   }

   public void move(MoverType typeIn, Vec3 pos) {
      super.move(typeIn, this.isNoAi() ? Vec3.ZERO : pos);
   }

   public boolean useTraditionalTravel() {
      return false;
   }

   public abstract int getMaxInventorySlots();

   public abstract Item getSaddleItem();

   public abstract Item getTameItem();

   public SimpleContainer getMountInventory() {
      return this.mountInventory;
   }

   protected void initMountInventory() {
      SimpleContainer inventory = this.mountInventory;
      this.mountInventory = new SimpleContainer(this.getMaxInventorySlots());
      if (inventory != null) {
         inventory.removeListener(this);
         int i = Math.min(inventory.getContainerSize(), this.mountInventory.getContainerSize());

         for (int j = 0; j < i; j++) {
            ItemStack itemstack = inventory.getItem(j);
            if (!itemstack.isEmpty()) {
               this.mountInventory.setItem(j, itemstack.copy());
            }
         }
      }

      this.mountInventory.addListener(this);
   }

   public void containerChanged(Container invBasic) {
      boolean prevSaddled = this.isSaddled();
      if (invBasic.getItem(0).getItem() == this.getSaddleItem() && !prevSaddled) {
         this.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
         this.setSaddled(true);
      } else if (invBasic.getItem(0).getItem() != this.getSaddleItem()) {
         this.setSaddled(false);
      }
   }

   public void travel(Vec3 travelVectorIn) {
      if (this.useTraditionalTravel()) {
         super.travel(travelVectorIn);
      } else {
         if (this.isSaddled() && this.isAlive()) {
            Entity entity = this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);
            if (entity instanceof Player player) {
               this.setYRot(entity.getYRot());
               this.yRotO = this.getYRot();
               this.setXRot(entity.getXRot() * 0.5F);
               this.setRot(this.getYRot(), this.getXRot());
               this.yBodyRot = this.getYRot();
               this.yHeadRot = this.getYRot();
               float forward = player.zza;
               float strafe = player.xxa;
               if (forward <= 0.0F) {
                  forward *= 0.25F;
               }

               if (this.isControlledByLocalInstance()) {
                  float f = (float)this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 0.225F;
                  this.setSpeed(f);
                  if (player.jumping && this.onGround()) {
                     Vec3 look = this.getViewVector(1.0F);
                     float jumpTravel = (this.getSpeed() <= 2.0F ? 0.1F : 0.4F) * forward;
                     this.setDeltaMovement(this.getDeltaMovement().add(look.x * jumpTravel, this.getJumpPower(), look.z * jumpTravel));
                  }

                  super.travel(new Vec3(strafe * 0.4F, 0.0, forward));
               } else {
                  this.setDeltaMovement(Vec3.ZERO);
               }
            } else {
               super.travel(travelVectorIn);
            }
         } else {
            super.travel(travelVectorIn);
         }
      }
   }

   public LivingEntity getControllingPassenger() {
      return this.getFirstPassenger() instanceof LivingEntity living && this.isSaddled() ? living : null;
   }

   protected float getJumpPower() {
      return this.isVehicle() ? 0.7F : super.getJumpPower();
   }

   public float getSpeed() {
      return this.isVehicle() ? 0.25F : 0.3F;
   }

   @Override
   public Item getMainBreedingItem() {
      return this.getTameItem();
   }

   @Override
   public AgeableMob createChild(AgeableMob ageable) {
      return null;
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(TAMED, false);
      builder.define(SADDLED, false);
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("Saddle", this.isSaddled());
      compound.putBoolean("Tame", this.isTamed());
      if (!this.getMountInventory().getItem(0).isEmpty()) {
         compound.put("SaddleItem", this.getMountInventory().getItem(0).save(new CompoundTag()));
      }
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.setSaddled(compound.getBoolean("Saddle"));
      this.setTamed(compound.getBoolean("Tame"));
      if (this.isSaddled()) {
         ItemStack itemstack = ItemStack.of(compound.getCompound("SaddleItem"));
         if (itemstack.getItem() == this.getSaddleItem()) {
            this.getMountInventory().setItem(0, itemstack);
         }
      }
   }

   public boolean isSaddled() {
      return (Boolean)this.entityData.get(SADDLED);
   }

   public void setSaddled(boolean saddled) {
      this.entityData.set(SADDLED, saddled);
   }

   public boolean isTamed() {
      return (Boolean)this.entityData.get(TAMED);
   }

   public void setTamed(boolean tamed) {
      this.entityData.set(TAMED, tamed);
   }

   protected void playTameEffect(boolean play) {
      SimpleParticleType enumparticletypes = !play ? ParticleTypes.SMOKE : ParticleTypes.HEART;

      for (int i = 0; i < 7; i++) {
         double d0 = this.random.nextGaussian() * 0.02;
         double d1 = this.random.nextGaussian() * 0.02;
         double d2 = this.random.nextGaussian() * 0.02;
         this.level()
            .addParticle(
               enumparticletypes,
               this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
               this.getY() + 0.5 + this.random.nextFloat() * this.getBbHeight(),
               this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
               d0,
               d1,
               d2
            );
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      if (id == 7) {
         this.playTameEffect(true);
      } else if (id == 6) {
         this.playTameEffect(false);
      } else {
         super.handleEntityEvent(id);
      }
   }

   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      if (super.mobInteract(player, hand).consumesAction()) {
         return InteractionResult.FAIL;
      }

      ItemStack itemstack = player.getItemInHand(hand);
      if (itemstack.getItem() == Items.NAME_TAG) {
         itemstack.interactLivingEntity(player, this, hand);
         return InteractionResult.SUCCESS;
      }

      if (!player.isCrouching() && !this.isVehicle() && this.isTamed()) {
         if (!this.level().isClientSide()) {
            player.startRiding(this);
         }

         return InteractionResult.SUCCESS;
      } else if (!this.isBaby() && this.isTamed() && (itemstack.getItem() == this.getSaddleItem() || player.isCrouching())) {
         if (player instanceof ServerPlayer) {
            openMountContainer((ServerPlayer)player, this);
         }

         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.FAIL;
      }
   }

   protected void dropEquipment() {
      super.dropEquipment();
      if (this.getMountInventory() != null) {
         for (int i = 0; i < this.getMountInventory().getContainerSize(); i++) {
            ItemStack itemstack = this.getMountInventory().getItem(i);
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
               this.spawnAtLocation(itemstack);
            }
         }
      }
   }

   public static void openMountContainer(ServerPlayer player, SkiesMountEntity enderHorse) {
      player.openMenu(
         new SimpleMenuProvider((id, inventory, playerIn) -> new MountInventoryMenu(id, inventory, enderHorse), enderHorse.getName()),
         buffer -> buffer.writeInt(enderHorse.getId())
      );
   }
}

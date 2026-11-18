package com.legacy.blue_skies.entities.projectile;

import com.legacy.blue_skies.items.tools.weapons.SpearItem;
import com.legacy.blue_skies.items.util.ToolUtils;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SpearEntity extends AbstractArrow {
   private static final ItemStack DEFAULT_SPEAR_STACK = new ItemStack(SkiesItems.bluebright_spear);
   private static final EntityDataAccessor<Byte> LOYALTY_LEVEL = SynchedEntityData.defineId(SpearEntity.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Boolean> HAS_EFFECT = SynchedEntityData.defineId(SpearEntity.class, EntityDataSerializers.BOOLEAN);
   public static final float BASE_DAMAGE = 8.0F;
   private ItemStack thrownStack = DEFAULT_SPEAR_STACK;
   private boolean dealtDamage;
   public int returningTicks;
   protected IntOpenHashSet spearPiercedEntities = null;

   public SpearEntity(EntityType<? extends SpearEntity> type, Level worldIn) {
      super(type, worldIn, DEFAULT_SPEAR_STACK);
      this.setState();
   }

   public SpearEntity(Level worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
      super(SkiesEntityTypes.SPEAR, thrower, worldIn, thrownStackIn);
      this.thrownStack = thrownStackIn.copy();
      this.entityData.set(LOYALTY_LEVEL, this.getLoyalty(thrownStackIn));
      this.entityData.set(HAS_EFFECT, thrownStackIn.hasFoil());
      this.setState();
   }

   @OnlyIn(Dist.CLIENT)
   public SpearEntity(Level worldIn, ItemStack stack, double x, double y, double z) {
      super(SkiesEntityTypes.SPEAR, x, y, z, worldIn, stack);
   }

   public void setState() {
      this.level().broadcastEntityEvent(this, SpearEntity.Type.getValue(this.thrownStack.getItem()));
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      this.thrownStack = new ItemStack(SpearEntity.Type.getSpear(id));
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(LOYALTY_LEVEL, (byte)0);
      this.entityData.define(HAS_EFFECT, false);
   }

   public void tick() {
      if (this.tickCount == 1) {
         this.setState();
      }

      if (this.piercingIgnoreEntityIds != this.spearPiercedEntities) {
         this.piercingIgnoreEntityIds = this.spearPiercedEntities;
      }

      if (this.inGroundTime > 4) {
         this.dealtDamage = true;
      }

      Entity entity = this.getOwner();
      if ((this.dealtDamage || this.isNoPhysics()) && entity != null) {
         int i = (Byte)this.entityData.get(LOYALTY_LEVEL);
         if (i > 0 && !this.shouldReturnToThrower()) {
            if (!this.level().isClientSide() && this.pickup == Pickup.ALLOWED) {
               this.spawnAtLocation(this.getPickupItem(), 0.1F);
            }

            this.discard();
         } else if (i > 0) {
            this.setNoPhysics(true);
            Vec3 vec3d = new Vec3(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
            this.setPosRaw(this.getX(), this.getY() + vec3d.y * 0.015 * i, this.getZ());
            if (this.level().isClientSide()) {
               this.yOld = this.getY();
            }

            double d0 = 0.05 * i;
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3d.normalize().scale(d0)));
            if (this.returningTicks == 0) {
               this.playSound(SkiesSounds.ITEM_SPEAR_RETURN, 10.0F, 1.0F);
            }

            this.returningTicks++;
         }
      }

      super.tick();
   }

   private boolean shouldReturnToThrower() {
      Entity entity = this.getOwner();
      return entity != null && entity.isAlive() ? !(entity instanceof ServerPlayer) || !entity.isSpectator() : false;
   }

   public ItemStack getPickupItem() {
      return this.thrownStack.copy();
   }

   @OnlyIn(Dist.CLIENT)
   public boolean hasEffect() {
      return (Boolean)this.entityData.get(HAS_EFFECT);
   }

   protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
      return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
   }

   protected void onHitEntity(EntityHitResult result) {
      Entity entity = result.getEntity();
      float damage = ToolUtils.getProjectileDamage(
         8.0F, this.thrownStack.getItem() instanceof SpearItem ? ((SpearItem)this.thrownStack.getItem()).getStick() : Items.STICK
      );
      if (entity instanceof LivingEntity livingentity) {
         damage += EnchantmentHelper.getDamageBonus(this.thrownStack, livingentity.getMobType());
      }

      if (this.getPierceLevel() > 0 && !entity.canBeCollidedWith()) {
         if (this.spearPiercedEntities == null) {
            this.spearPiercedEntities = new IntOpenHashSet(5);
         }

         if (this.spearPiercedEntities.size() >= this.getPierceLevel() + 1) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
            this.dealtDamage = true;
            return;
         }

         this.spearPiercedEntities.add(entity.getId());
      } else {
         this.dealtDamage = true;
         this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
      }

      Entity entity1 = this.getOwner();
      DamageSource damagesource = this.damageSources().trident(this, (Entity)(entity1 == null ? this : entity1));
      SoundEvent soundevent = SkiesSounds.ITEM_SPEAR_HIT;
      if (entity.hurt(damagesource, damage) && entity instanceof LivingEntity livingentity1) {
         if (entity1 instanceof LivingEntity) {
            EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
            EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity1);
         }

         this.doPostHurtEffects(livingentity1);
      }

      float f1 = 1.0F;
      this.playSound(soundevent, f1, 1.0F);
      if (this.piercingIgnoreEntityIds != this.spearPiercedEntities) {
         this.piercingIgnoreEntityIds = this.spearPiercedEntities;
      }
   }

   protected SoundEvent getDefaultHitGroundSoundEvent() {
      return SkiesSounds.ITEM_SPEAR_HIT_GROUND;
   }

   protected void onHit(HitResult raytraceResultIn) {
      net.minecraft.world.phys.HitResult.Type raytraceresult$type = raytraceResultIn.getType();
      if (raytraceresult$type == net.minecraft.world.phys.HitResult.Type.BLOCK) {
         this.playSound(SkiesSounds.ITEM_SPEAR_HIT_GROUND, 1.0F, 1.0F + this.random.nextFloat() * 0.2F);
      }

      super.onHit(raytraceResultIn);
   }

   public void playerTouch(Player entityIn) {
      Entity entity = this.getOwner();
      if (entity == null || entity.getUUID() == entityIn.getUUID()) {
         super.playerTouch(entityIn);
      }
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      if (compound.contains("Spear", 10)) {
         this.thrownStack = ItemStack.of(compound.getCompound("Spear"));
      }

      this.dealtDamage = compound.getBoolean("DealtDamage");
      this.entityData.set(LOYALTY_LEVEL, this.getLoyalty(this.thrownStack));
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.put("Spear", this.thrownStack.save(new CompoundTag()));
      compound.putBoolean("DealtDamage", this.dealtDamage);
   }

   protected void tickDespawn() {
      int i = (Byte)this.entityData.get(LOYALTY_LEVEL);
      if (this.pickup != Pickup.ALLOWED || i <= 0) {
         super.tickDespawn();
      }
   }

   protected float getWaterInertia() {
      return 0.99F;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean shouldRender(double x, double y, double z) {
      return true;
   }

   public byte getLoyalty(ItemStack stack) {
      int loyaltyLevel = EnchantmentHelper.getLoyalty(stack);
      return (byte)(stack.getItem() == SkiesItems.soulbound_spear ? Math.max(1, loyaltyLevel) : loyaltyLevel);
   }

   public static enum Type {
      BLUEBRIGHT(0, SkiesItems.bluebright_spear),
      STARLIT(1, SkiesItems.starlit_spear),
      FROSTBRIGHT(2, SkiesItems.frostbright_spear),
      LUNAR(3, SkiesItems.lunar_spear),
      DUSK(4, SkiesItems.dusk_spear),
      MAPLE(5, SkiesItems.maple_spear),
      COMET(6, SkiesItems.comet_spear),
      SOULBOUND(7, SkiesItems.soulbound_spear);

      final byte value;
      final Item item;

      private Type(int value, Item item) {
         this.value = (byte)value;
         this.item = item;
      }

      public static Item getSpear(byte value) {
         for (SpearEntity.Type type : values()) {
            if (type.value == value) {
               return type.item;
            }
         }

         return BLUEBRIGHT.item;
      }

      public static byte getValue(Item item) {
         for (SpearEntity.Type type : values()) {
            if (type.item == item) {
               return type.value;
            }
         }

         return BLUEBRIGHT.value;
      }
   }
}

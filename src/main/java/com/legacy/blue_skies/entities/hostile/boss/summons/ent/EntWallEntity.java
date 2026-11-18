package com.legacy.blue_skies.entities.hostile.boss.summons.ent;

import com.legacy.blue_skies.data.objects.tags.SkiesEntityTags;
import com.legacy.blue_skies.entities.hostile.boss.StarlitCrusherEntity;
import com.legacy.blue_skies.entities.passive.SnowOwlEntity;
import com.legacy.blue_skies.entities.util.WallExtensionEntity;
import com.legacy.blue_skies.items.tools.SkyAxeItem;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.entity.PartEntity;

public class EntWallEntity extends LivingEntity {
   protected static final EntityDataAccessor<Boolean> FACING_Z = SynchedEntityData.defineId(EntWallEntity.class, EntityDataSerializers.BOOLEAN);
   private LivingEntity caster;
   private UUID casterUuid;
   public int translateAmount = 0;
   private final WallExtensionEntity leftSection = new WallExtensionEntity(this, "left_section", 1.0F, 3.0F);
   private final WallExtensionEntity rightSection = new WallExtensionEntity(this, "right_section", 1.0F, 3.0F);
   private final WallExtensionEntity[] parts = new WallExtensionEntity[]{this.leftSection, this.rightSection};

   public EntWallEntity(EntityType<? extends EntWallEntity> typeIn, Level worldIn) {
      super(typeIn, worldIn);
      this.setId(ENTITY_COUNTER.getAndAdd(this.parts.length + 1) + 1);
   }

   public void setId(int id) {
      super.setId(id);

      for (int i = 0; i < this.parts.length; i++) {
         this.parts[i].setId(id + i + 1);
      }
   }

   public EntWallEntity(Level worldIn, double x, double y, double z, LivingEntity casterIn) {
      this(SkiesEntityTypes.ENT_WALL, worldIn);
      this.setCaster(casterIn);
      this.setXRot(0.0F);
      this.setYRot(0.0F);
      this.yHeadRot = this.xRotO = this.yRotO = this.yHeadRot = 0.0F;
      this.setPos(x + 0.5, y, z + 0.5);
      if (casterIn != null && casterIn instanceof StarlitCrusherEntity) {
         this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0 + ((StarlitCrusherEntity)casterIn).getExtraPlayers() * 10.0);
      }
   }

   public void move(MoverType typeIn, Vec3 pos) {
      super.move(typeIn, Vec3.ZERO);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(FACING_Z, false);
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.KNOCKBACK_RESISTANCE, 100.0);
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

   public void load(CompoundTag compound) {
      super.load(compound);
      this.setFacingZ(compound.getBoolean("IsFacingZ"));
      if (compound.hasUUID("OwnerUUID")) {
         this.casterUuid = compound.getUUID("OwnerUUID");
      }
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("IsFacingZ", this.isFacingZ());
      if (this.casterUuid != null) {
         compound.putUUID("OwnerUUID", this.casterUuid);
      }
   }

   public void tick() {
      super.tick();
      this.setXRot(0.0F);
      this.setYRot(0.0F);
      this.yHeadRot = this.xRotO = this.yRotO = this.yHeadRot = 0.0F;
   }

   public void aiStep() {
      super.aiStep();
      int x = !this.isFacingZ() ? 0 : 1;
      int z = !this.isFacingZ() ? 1 : 0;
      this.rightSection.setPos(this.position().add(x, 0.0, z));
      this.leftSection.setPos(this.position().add(-x, 0.0, -z));

      for (WallExtensionEntity ex : this.parts) {
         ex.xo = ex.getX();
         ex.yo = ex.getY();
         ex.zo = ex.getZ();
         ex.xOld = ex.getX();
         ex.yOld = ex.getY();
         ex.zOld = ex.getZ();
      }
   }

   public void die(DamageSource cause) {
      super.die(cause);
      if (!this.level().isClientSide) {
         this.remove(RemovalReason.KILLED);
         this.playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, this.getSoundVolume(), super.getVoicePitch());
         if (this.level() instanceof ServerLevel) {
            for (int i = 0; i < 6; i++) {
               this.level().broadcastEntityEvent(this, (byte)4);
               ((ServerLevel)this.level())
                  .sendParticles(
                     new BlockParticleOption(ParticleTypes.BLOCK, SkiesBlocks.starlit_log.defaultBlockState()),
                     this.getX(),
                     this.getY() + this.getBbHeight() / 1.5,
                     this.getZ(),
                     20,
                     this.getBoundingBox().getXsize() / 4.0,
                     this.getBoundingBox().getYsize() / 4.0,
                     this.getBoundingBox().getZsize() / 4.0,
                     0.05
                  );
            }
         }
      }
   }

   public boolean hurt(DamageSource source, float amount) {
      if (source == this.damageSources().fellOutOfWorld()) {
         return super.hurt(source, amount);
      } else {
         if (source.getDirectEntity() instanceof LivingEntity) {
            ItemStack stack = ((LivingEntity)source.getDirectEntity()).getMainHandItem();
            if (stack.getItem() instanceof SkyAxeItem) {
               this.playDamageEffect();
               return super.hurt(source, amount);
            }

            if (source.getDirectEntity() instanceof Player) {
               ((Player)source.getDirectEntity()).displayClientMessage(Component.translatable("gui.blue_skies.tooltip.invalid_ent_weapon"), true);
            }
         }

         return false;
      }
   }

   protected void doPush(Entity entityIn) {
      if (!(entityIn instanceof SnowOwlEntity)) {
         super.doPush(entityIn);
      }
   }

   public boolean isAlliedTo(Entity entityIn) {
      if (entityIn instanceof EntRootEntity) {
         return this.isAlliedTo(((EntRootEntity)entityIn).getCaster());
      } else {
         return entityIn.getType().is(SkiesEntityTags.NATURE_ALLIES)
            ? this.getTeam() == null && entityIn.getTeam() == null
            : super.isAlliedTo(entityIn) && entityIn != null && entityIn != this;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      if (id == 4) {
         if (!this.isSilent()) {
            this.level()
               .playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.WOOD_BREAK, this.getSoundSource(), this.getSoundVolume(), 0.8F, false);
         }
      } else {
         super.handleEntityEvent(id);
      }
   }

   public void playDamageEffect() {
      if (this.level() instanceof ServerLevel sl) {
         for (int i = 0; i < 4; i++) {
            this.level().broadcastEntityEvent(this, (byte)4);
            float amount = 0.7F;
            float x = !this.isFacingZ() ? 0.2F : amount;
            float z = !this.isFacingZ() ? amount : 0.2F;
            sl.sendParticles(
               new BlockParticleOption(ParticleTypes.BLOCK, SkiesBlocks.starlit_log.defaultBlockState()),
               this.getX(),
               this.getY() + 1.5,
               this.getZ(),
               80,
               x,
               amount,
               z,
               0.05
            );
         }
      }
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return null;
   }

   protected SoundEvent getDeathSound() {
      return null;
   }

   protected float getSoundVolume() {
      return 2.0F;
   }

   public boolean canBeCollidedWith() {
      return true;
   }

   public boolean isPickable() {
      return true;
   }

   public boolean isFacingZ() {
      return (Boolean)this.entityData.get(FACING_Z);
   }

   public void setFacingZ(boolean facingz) {
      this.entityData.set(FACING_Z, facingz);
   }

   public Iterable<ItemStack> getArmorSlots() {
      return NonNullList.withSize(4, ItemStack.EMPTY);
   }

   public ItemStack getItemBySlot(EquipmentSlot slotIn) {
      return ItemStack.EMPTY;
   }

   public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {
   }

   public HumanoidArm getMainArm() {
      return HumanoidArm.RIGHT;
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   public void knockback(double strength, double x, double z) {
   }

   public void push(double x, double y, double z) {
   }

   public boolean isMultipartEntity() {
      return true;
   }

   public PartEntity<?>[] getParts() {
      return this.parts;
   }
}

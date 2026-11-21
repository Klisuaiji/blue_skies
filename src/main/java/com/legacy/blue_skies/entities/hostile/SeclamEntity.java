package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.entities.util.base.SkiesMonsterEntity;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.structure_gel.api.util.Positions;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

public class SeclamEntity extends SkiesMonsterEntity {
   private static final UUID SHELL_ARMOR_BONUS_ID = UUID.fromString("bdf8660c-ccee-43fd-bbf2-40e7d4ddbea2");
   private static final AttributeModifier SHELL_ARMOR_BONUS_MODIFIER = LegacyAttrs.modifier(
      SHELL_ARMOR_BONUS_ID, "Shell armor bonus", 20.0, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE
   );
   private static final EntityDataAccessor<Boolean> OPEN = SynchedEntityData.defineId(SeclamEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Byte> TIMES_HIT_SINCE_OPEN = SynchedEntityData.defineId(SeclamEntity.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Boolean> HAS_PEARL = SynchedEntityData.defineId(SeclamEntity.class, EntityDataSerializers.BOOLEAN);
   private float prevOpenAnim;
   private float openAnim;
   public int timeSinceOpened;

   public SeclamEntity(EntityType<? extends SeclamEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 30.0)
         .add(Attributes.MOVEMENT_SPEED, 0.0)
         .add(Attributes.ARMOR, 6.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 0.9F)
         .add(Attributes.ATTACK_DAMAGE, 8.0);
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(OPEN, true);
      builder.define(TIMES_HIT_SINCE_OPEN, (byte)0);
      builder.define(HAS_PEARL, true);
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag
   ) {
      spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
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
         this.prevOpenAnim = this.openAnim;
         if (this.isOpen()) {
            this.openAnim = Mth.clamp(this.openAnim + (this.getTimesHitSinceOpen() < 3 ? 0.3F : 1.5F), 0.0F, 6.0F);
         } else {
            this.openAnim = Mth.clamp(this.openAnim - 1.5F, 0.0F, 6.0F);
         }

         if (this.isEyeInFluidType((FluidType)NeoForgeMod.WATER_TYPE.value())
            && (this.isOpen() && this.openAnim >= 2.0F && this.openAnim <= 4.0F || this.isOpen() && this.openAnim >= 6.0F && this.random.nextInt(10) == 0)) {
            for (int i = 0; i < (this.openAnim >= 6.0F ? 2 : 10); i++) {
               double dx = this.random.nextGaussian() * 0.02;
               double dy = this.random.nextFloat() * 0.5F + this.random.nextGaussian() * 0.04;
               double dz = this.random.nextGaussian() * 0.02;
               this.level()
                  .addParticle(
                     ParticleTypes.BUBBLE,
                     0.8F + this.getX() + this.random.nextFloat() * this.getBbWidth() - this.getBbWidth(),
                     this.getY() + 0.5,
                     0.9F + this.getZ() + this.random.nextFloat() * this.getBbWidth() - this.getBbWidth(),
                     dx,
                     dy,
                     dz
                  );
            }
         }
      } else {
         if (this.timeSinceOpened > 0) {
            this.timeSinceOpened--;
         }

         if (this.level().getNearestPlayer(this, 8.0) != null && this.getTimesHitSinceOpen() < 3 && this.timeSinceOpened <= 0) {
            if (this.isOpen()) {
               this.hurtSurroundingMobs();
            }

            this.setOpen(false);
         } else {
            this.setOpen(true);
         }

         if (this.getTimesHitSinceOpen() >= 3 && this.timeSinceOpened <= 0 && this.isOpen()) {
            this.hurtSurroundingMobs();
            this.setOpen(false);
         }
      }
   }

   public boolean hurt(DamageSource source, float amount) {
      if (this.hurtTime <= 0 && this.getTimesHitSinceOpen() < 3) {
         this.setTimesHitSinceOpen((byte)(this.getTimesHitSinceOpen() + 1));
         if (this.getTimesHitSinceOpen() == 3) {
            this.timeSinceOpened = 80;
         }
      }

      return super.hurt(source, amount);
   }

   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      if (this.hasPearl() && this.isOpen()) {
         if (!this.level().isClientSide()) {
            this.setHasPearl(false);
            this.spawnAtLocation(new ItemStack(SkiesItems.pearl));
         }

         return InteractionResult.SUCCESS;
      } else {
         return super.mobInteract(player, hand);
      }
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("Open", this.isOpen());
      compound.putByte("TimeHitSinceOpen", this.getTimesHitSinceOpen());
      compound.putBoolean("HasPearl", this.hasPearl());
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.setOpen(compound.getBoolean("Open"));
      this.setTimesHitSinceOpen(compound.getByte("TimeHitSinceOpen"));
      this.setHasPearl(compound.getBoolean("HasPearl"));
   }

   public boolean isOpen() {
      return (Boolean)this.entityData.get(OPEN);
   }

   public void setOpen(boolean open) {
      if (this.isOpen() != open) {
         if (open) {
            if (this.getAttribute(Attributes.ARMOR).hasModifier(SHELL_ARMOR_BONUS_MODIFIER.id())) {
               this.getAttribute(Attributes.ARMOR).removeModifier(SHELL_ARMOR_BONUS_MODIFIER.getId());
            }
         } else {
            this.playSound(SoundEvents.ARMOR_EQUIP_TURTLE.value(), 1.0F, this.getVoicePitch());
            this.setTimesHitSinceOpen((byte)0);
            if (!this.getAttribute(Attributes.ARMOR).hasModifier(SHELL_ARMOR_BONUS_MODIFIER.id())) {
               this.getAttribute(Attributes.ARMOR).addPermanentModifier(SHELL_ARMOR_BONUS_MODIFIER);
            }
         }

         this.entityData.set(OPEN, open);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public float getOpenAnimationScale(float pctIn) {
      return Mth.lerp(pctIn, this.prevOpenAnim, this.openAnim) / 6.0F;
   }

   public byte getTimesHitSinceOpen() {
      return (Byte)this.entityData.get(TIMES_HIT_SINCE_OPEN);
   }

   public void setTimesHitSinceOpen(byte times) {
      this.entityData.set(TIMES_HIT_SINCE_OPEN, times);
   }

   public boolean hasPearl() {
      return (Boolean)this.entityData.get(HAS_PEARL);
   }

   public void setHasPearl(boolean pearl) {
      this.entityData.set(HAS_PEARL, pearl);
   }

   private void hurtSurroundingMobs() {
      List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.0));
      if (!entities.isEmpty()) {
         entities.forEach(entity -> {
            if (entity != this) {
               this.doHurtTarget(entity);
            }
         });
      }
   }

   public boolean canDrownInFluidType(FluidType type) {
      return super.canDrownInFluidType(type) && type != NeoForgeMod.WATER_TYPE.value();
   }


   public boolean isPushedByFluid() {
      return false;
   }

   protected void dropEquipment() {
      super.dropEquipment();
      if (this.hasPearl()) {
         this.spawnAtLocation(SkiesItems.pearl);
      }
   }
}

package com.legacy.blue_skies.entities.hostile;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.entities.util.base.SkiesMonsterEntity;
import com.legacy.blue_skies.items.tools.SkyPickaxeItem;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class ArmoredFrostSpiritEntity extends SkiesMonsterEntity {
   public static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(ArmoredFrostSpiritEntity.class, EntityDataSerializers.INT);

   public ArmoredFrostSpiritEntity(EntityType<? extends ArmoredFrostSpiritEntity> type, Level worldIn) {
      super(type, worldIn);
      this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
      this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, -1.0F);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.5, false));
      this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[]{ZombifiedPiglin.class}));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
      this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.17F).add(Attributes.ATTACK_DAMAGE, 3.0);
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(TYPE, 0);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag
   ) {
      spawnDataIn = super.finalizeSpawn(worldIn, difficulty, reason, spawnDataIn, dataTag);
      this.determineTypeByDifficulty();
      float f = difficulty.getSpecialMultiplier();
      this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * f);
      this.getAttribute(Attributes.KNOCKBACK_RESISTANCE)
         .addPermanentModifier(LegacyAttrs.modifier("Random spawn bonus", this.random.nextDouble() * 0.05F, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE));
      if (this.getIceType() == 1) {
         this.getAttribute(Attributes.ARMOR).addPermanentModifier(LegacyAttrs.modifier("armor boost", 8.0, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE));
      }

      if (this.level().isRaining()) {
         double armorAmount = this.getIceType() == 1 ? 3.0 : 7.0;
         this.getAttribute(Attributes.ARMOR).addPermanentModifier(LegacyAttrs.modifier("weather armor boost", armorAmount, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE));
         if (this.level().getDifficulty() == Difficulty.HARD) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(LegacyAttrs.modifier("speed boost", 0.04, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE));
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(LegacyAttrs.modifier("attack nerf", -1.0, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE));
         }
      }

      return spawnDataIn;
   }

   public void setIceType(int type) {
      this.entityData.set(TYPE, type);
   }

   public int getIceType() {
      return (Integer)this.entityData.get(TYPE);
   }

   @Override
   public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
      return 0.0F;
   }

   public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
      boolean randomBoolean = this.level().isRaining() ? true : this.random.nextBoolean();
      return randomBoolean && super.checkSpawnRules(worldIn, spawnReasonIn);
   }

   public boolean hurt(DamageSource ds, float amount) {
      if (!ds.is(DamageTypeTags.IS_FIRE) && !ds.is(DamageTypeTags.IS_DROWNING)) {
         if (ds.getEntity() instanceof LivingEntity && this.hurtTime <= 0) {
            if (this.level() instanceof ServerLevel) {
               for (int i = 0; i < 2; i++) {
                  if (this.getIceType() == 1) {
                     this.spawnBlockParticles(Blocks.PACKED_ICE.defaultBlockState(), 20);
                  } else {
                     this.spawnBlockParticles(Blocks.ICE.defaultBlockState(), 20);
                  }
               }
            }

            if (!this.isSilent()) {
               this.playSound(SoundEvents.GLASS_BREAK, 0.5F, 1.0F);
            }

            if (ds.getDirectEntity() instanceof Player) {
               Player player = (Player)ds.getDirectEntity();
               ItemStack stack = player.getInventory().getSelected();
               if (stack.getItem() instanceof SkyPickaxeItem) {
                  return super.hurt(ds, amount * 2.0F);
               }
            }
         }

         return super.hurt(ds, amount);
      } else {
         if (ds.is(DamageTypeTags.IS_FIRE) && this.hurtTime <= 0) {
            this.playSound(SoundEvents.GENERIC_EXTINGUISH_FIRE, 0.3F, 1.0F);
         } else {
            this.playSound(SoundEvents.WATER_AMBIENT, 0.8F, 1.0F);
         }

         if (this.level() instanceof ServerLevel) {
            ((ServerLevel)this.level())
               .sendParticles(
                  ParticleTypes.SPLASH,
                  this.getX(),
                  this.getY() + this.getBbHeight() / 1.5,
                  this.getZ(),
                  20,
                  this.getBbWidth() / 4.0F,
                  this.getBbHeight() / 4.0F,
                  this.getBbWidth() / 4.0F,
                  0.05
               );
         }

         return super.hurt(ds, amount * 2.0F);
      }
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_ARMORED_FROST_SPIRIT_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_ARMORED_FROST_SPIRIT_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_ARMORED_FROST_SPIRIT_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      super.playStepSound(pos, blockIn);
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   public int getAmbientSoundInterval() {
      return 120;
   }

   public float getVoicePitch() {
      return (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.0F;
   }

   public void determineTypeByDifficulty() {
      switch (this.level().getDifficulty()) {
         case EASY:
            this.setIceType(this.random.nextInt(4) == 0 ? 1 : 0);
            break;
         case NORMAL:
            this.setIceType(this.random.nextInt(2));
            break;
         case HARD:
            this.setIceType(this.random.nextInt(4) == 0 ? 0 : 1);
      }
   }

   public static Item getSkyArmorByChance(EquipmentSlot slotIn, int chance) {
      switch (slotIn) {
         case HEAD:
            switch (chance) {
               case 0:
                  return Items.LEATHER_HELMET;
               case 1:
                  return SkiesItems.pyrope_helmet;
               case 2:
                  return SkiesItems.aquite_helmet;
               case 3:
                  return SkiesItems.diopside_helmet;
               case 4:
                  return SkiesItems.charoite_helmet;
               default:
                  return null;
            }
         case CHEST:
            switch (chance) {
               case 0:
                  return Items.LEATHER_CHESTPLATE;
               case 1:
                  return SkiesItems.pyrope_chestplate;
               case 2:
                  return SkiesItems.aquite_chestplate;
               case 3:
                  return SkiesItems.diopside_chestplate;
               case 4:
                  return SkiesItems.charoite_chestplate;
               default:
                  return null;
            }
         case LEGS:
            switch (chance) {
               case 0:
                  return Items.LEATHER_LEGGINGS;
               case 1:
                  return SkiesItems.pyrope_leggings;
               case 2:
                  return SkiesItems.aquite_leggings;
               case 3:
                  return SkiesItems.diopside_leggings;
               case 4:
                  return SkiesItems.charoite_leggings;
               default:
                  return null;
            }
         case FEET:
            switch (chance) {
               case 0:
                  return Items.LEATHER_BOOTS;
               case 1:
                  return SkiesItems.pyrope_boots;
               case 2:
                  return SkiesItems.aquite_boots;
               case 3:
                  return SkiesItems.diopside_boots;
               case 4:
                  return SkiesItems.charoite_boots;
               default:
                  return null;
            }
         default:
            return null;
      }
   }

   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
      if (random.nextFloat() < 0.15F * difficulty.getSpecialMultiplier()) {
         int i = random.nextInt(2);
         float f = this.level().getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F;
         if (random.nextFloat() < 0.095F) {
            i++;
         }

         if (random.nextFloat() < 0.095F) {
            i++;
         }

         if (random.nextFloat() < 0.095F) {
            i++;
         }

         boolean flag = true;

         for (EquipmentSlot entityequipmentslot : EquipmentSlot.values()) {
            if (entityequipmentslot.getType() == Type.ARMOR) {
               ItemStack itemstack = this.getItemBySlot(entityequipmentslot);
               if (!flag && random.nextFloat() < f) {
                  break;
               }

               flag = false;
               if (itemstack.isEmpty()) {
                  Item item = getSkyArmorByChance(entityequipmentslot, i);
                  if (item != null) {
                     this.setItemSlot(entityequipmentslot, new ItemStack(item));
                  }
               }
            }
         }
      }

      if (random.nextFloat() < (this.level().getDifficulty() == Difficulty.HARD ? 0.05F : 0.01F)) {
         int i = random.nextInt(3);
         if (i == 0) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(SkiesItems.aquite_sword));
         } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(SkiesItems.pyrope_sword));
         }
      }
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("IceType", this.getIceType());
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.setIceType(compound.getInt("IceType"));
   }

   public float getEyeHeight(Pose pose) {
      float f = 1.74F;
      if (this.isBaby()) {
         f = (float)(f - 0.81);
      }

      return f;
   }

   protected void customServerAiStep() {
      if (this.tickCount % 20 == 0) {
         if (this.isInWaterOrBubble()) {
            this.hurt(this.damageSources().drown(), 1.0F);
         } else if (this.isInWaterOrRain() && this.level() instanceof ServerLevel) {
            ((ServerLevel)this.level())
               .sendParticles(
                  ParticleTypes.SPLASH,
                  this.getX(),
                  this.getY() + this.getBbHeight() / 1.5,
                  this.getZ(),
                  20,
                  this.getBbWidth() / 4.0F,
                  this.getBbHeight() / 4.0F,
                  this.getBbWidth() / 4.0F,
                  0.05
               );
         }
      }
   }

   public void die(DamageSource cause) {
      super.die(cause);
      if (!this.level().isClientSide) {
         Vec3 pos = this.position().add(0.0, 1.5, 0.0);
         FrostSpiritEntity spirit = (FrostSpiritEntity)SkiesEntityTypes.FROST_SPIRIT.create(this.level());
         spirit.moveTo(pos.x, pos.y, pos.z, this.yRotO, this.xRotO);
         spirit.setDeltaMovement(0.0, 1.0, 0.0);
         spirit.setSilent(this.isSilent());
         spirit.setCustomName(this.getCustomName());
         spirit.setCustomNameVisible(this.isCustomNameVisible());
         this.level().addFreshEntity(spirit);
         this.spawnAnim();
         this.discard();
         spirit.playSound(SkiesSounds.ENTITY_FROST_SPIRIT_FREE, spirit.getSoundVolume(), super.getVoicePitch());
      }
   }

   public int getMaxSpawnClusterSize() {
      return 1;
   }

   public ResourceKey<LootTable> getDefaultLootTable() {
      return this.getIceType() == 1
         ? BlueSkies.locate("entities/" + BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()).getPath() + "_packed")
         : this.getType().getDefaultLootTable();
   }
}

package com.legacy.blue_skies.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.data.objects.tags.SkiesBiomeTags;
import com.legacy.blue_skies.entities.util.base.SkiesAnimalEntity;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesSounds;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;

public class ShadeMonitorEntity extends SkiesAnimalEntity {
   private static final float NATURAL_FESTIVE_CHANCE = 0.4F;
   private static final EntityDataAccessor<Byte> BIOME = SynchedEntityData.defineId(ShadeMonitorEntity.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Integer> FESTIVE_STATUS = SynchedEntityData.defineId(ShadeMonitorEntity.class, EntityDataSerializers.INT);

   public ShadeMonitorEntity(EntityType<? extends ShadeMonitorEntity> type, Level world) {
      super(type, world);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.goalSelector.addGoal(1, new LeapAtTargetGoal(this, 0.4F));
      this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
      this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.5, true));
      this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(ItemTags.FISHES), false));
      this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 16.0)
         .add(Attributes.MOVEMENT_SPEED, 0.3F)
         .add(Attributes.ATTACK_DAMAGE)
         .add(Attributes.ATTACK_KNOCKBACK);
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(BIOME, (byte)0);
      builder.define(FESTIVE_STATUS, 0);
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag
   ) {
      spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
      this.setBiome(
         ShadeMonitorEntity.Biomes.get(this.level().registryAccess().registryOrThrow(Registries.BIOME), this.level().getBiome(this.blockPosition())).getId()
      );
      this.setFestiveStatus(this.random.nextFloat() < 0.4F ? 1 : 0);
      return spawnDataIn;
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putByte("Biome", this.getBiome());
      compound.putInt("FestiveStatus", this.getFestiveStatus());
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.setBiome(compound.getByte("Biome"));
      this.setFestiveStatus(!compound.contains("FestiveStatus") ? (this.random.nextFloat() < 0.4F ? 1 : 0) : compound.getInt("FestiveStatus"));
   }

   public byte getBiome() {
      return (Byte)this.entityData.get(BIOME);
   }

   public void setBiome(byte biome) {
      this.entityData.set(BIOME, biome);
   }

   public int getFestiveStatus() {
      return (Integer)this.entityData.get(FESTIVE_STATUS);
   }

   public void setFestiveStatus(int flag) {
      this.entityData.set(FESTIVE_STATUS, flag);
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_SHADE_MONITOR_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_SHADE_MONITOR_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_SHADE_MONITOR_DEATH;
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   @Override
   public boolean isFood(ItemStack stack) {
      return stack.isEdible() && stack.getFoodProperties(this).isMeat() || stack.is(ItemTags.FISHES);
   }

   public ShadeMonitorEntity createChild(AgeableMob ageable) {
      byte biome;
      if (this.random.nextFloat() < 0.05) {
         biome = ShadeMonitorEntity.Biomes.get(this.level().registryAccess().registryOrThrow(Registries.BIOME), this.level().getBiome(this.blockPosition()))
            .getId();
      } else {
         biome = this.random.nextBoolean() ? ((ShadeMonitorEntity)ageable).getBiome() : this.getBiome();
      }

      ShadeMonitorEntity child = (ShadeMonitorEntity)SkiesEntityTypes.SHADE_MONITOR.create(this.level());
      child.setBiome(biome);
      if (BlueSkiesConfig.COMMON.isHalloween() && this.getFestiveStatus() > 0 && ((ShadeMonitorEntity)ageable).getFestiveStatus() > 0) {
         child.setFestiveStatus(2);
      }

      return child;
   }

   protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
      return this.isBaby() ? 0.3F : 0.6F;
   }

   public boolean doHurtTarget(Entity entity) {
      return super.doHurtTarget(entity);
   }

   public enum Biomes {
      OVERWORLD(null, "shade_monitor/overworld"),
      SNOWY_BLUE(SkiesBiomeTags.SHADE_MONITOR_SNOWY_BLUE),
      PURPLE(SkiesBiomeTags.SHADE_MONITOR_PURPLE),
      WHITE(SkiesBiomeTags.SHADE_MONITOR_WHITE),
      WHITE_AND_PURPLE(SkiesBiomeTags.SHADE_MONITOR_WHITE_AND_PURPLE),
      RED(SkiesBiomeTags.SHADE_MONITOR_RED),
      PINK(SkiesBiomeTags.SHADE_MONITOR_PINK),
      MUDDY_PURPLE(SkiesBiomeTags.SHADE_MONITOR_MUDDY_PURPLE);

      @Nullable
      private final TagKey<Biome> tagKey;
      private final ResourceLocation texture;

      Biomes(TagKey<Biome> tagKey, String name) {
         this.tagKey = tagKey;
         this.texture = BlueSkies.locate("textures/entity/" + name + ".png");
      }

      Biomes(TagKey<Biome> tagKey) {
         this(tagKey, tagKey.location().getPath());
      }

      public byte getId() {
         return (byte)this.ordinal();
      }

      public ResourceLocation getTexture() {
         return this.texture;
      }

      public static ShadeMonitorEntity.Biomes get(Registry<Biome> registry, Holder<Biome> biome) {
         for (ShadeMonitorEntity.Biomes b : values()) {
            if (b.tagKey != null && registry.getOrCreateTag(b.tagKey).contains(biome)) {
               return b;
            }
         }

         return registry.getOrCreateTag(SkiesBiomeTags.EVERDAWN).contains(biome) ? PURPLE : OVERWORLD;
      }

      public static ShadeMonitorEntity.Biomes byId(int id) {
         return id >= 0 && id < values().length ? values()[id] : OVERWORLD;
      }
   }
}

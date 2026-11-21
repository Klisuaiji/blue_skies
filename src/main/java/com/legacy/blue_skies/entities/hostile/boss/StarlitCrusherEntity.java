package com.legacy.blue_skies.entities.hostile.boss;

import com.google.common.collect.ImmutableSet;
import com.legacy.blue_skies.blocks.misc.TrophyBlock;
import com.legacy.blue_skies.data.objects.tags.SkiesEntityTags;
import com.legacy.blue_skies.entities.hostile.SpewterEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntRootEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntWallEntity;
import com.legacy.blue_skies.entities.passive.SnowOwlEntity;
import com.legacy.blue_skies.entities.projectile.EntLeafEntity;
import com.legacy.blue_skies.entities.projectile.SpearEntity;
import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import com.legacy.blue_skies.entities.util.StarlitCrusherSpinSound;
import com.legacy.blue_skies.entities.util.StunnableLookController;
import com.legacy.blue_skies.entities.util.StunnedEntitySound;
import com.legacy.blue_skies.entities.util.base.SkiesBossEntity;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBoss;
import com.legacy.blue_skies.entities.util.interfaces.IStunnableMob;
import com.legacy.blue_skies.items.LootBagItem;
import com.legacy.blue_skies.items.tools.SkyAxeItem;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesDamageSources;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesParticles;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.DirectionUtil;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.structure_gel.api.util.Positions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.ItemAbilities;

public class StarlitCrusherEntity extends SkiesBossEntity implements IStunnableMob {
   public static final Predicate<SnowOwlEntity> OWL_HOSTILE = owl -> owl.isOwlHostile();
   protected static final EntityDataAccessor<Boolean> ROOTED = SynchedEntityData.defineId(StarlitCrusherEntity.class, EntityDataSerializers.BOOLEAN);
   protected static final EntityDataAccessor<Integer> TIME_STUNNED = SynchedEntityData.defineId(StarlitCrusherEntity.class, EntityDataSerializers.INT);
   protected static final EntityDataAccessor<Integer> SPIN_COOLDOWN = SynchedEntityData.defineId(StarlitCrusherEntity.class, EntityDataSerializers.INT);
   protected static final EntityDataAccessor<Boolean> SPINNING = SynchedEntityData.defineId(StarlitCrusherEntity.class, EntityDataSerializers.BOOLEAN);
   public int attackTimer;
   public int spinTimer;
   public int smashTimer;
   private int rootWaveCooldown;
   private int regrowWallsTimer;
   private int owlSpawnCooldown = 400;
   private int forceHomeTimer = 200;
   private int deathTicks;
   private int meleeCooldown;
   private int spearImmuneTime = 60;
   public byte rootingTime;
   public boolean canShake = false;
   public static Set<Item> SPEARS = ImmutableSet.of(
      SkiesItems.bluebright_spear,
      SkiesItems.lunar_spear,
      SkiesItems.starlit_spear,
      SkiesItems.dusk_spear,
      SkiesItems.frostbright_spear,
      SkiesItems.maple_spear,
      new Item[]{SkiesItems.comet_spear, SkiesItems.soulbound_spear}
   );
   @OnlyIn(Dist.CLIENT)
   private SoundInstance stunSound;
   @OnlyIn(Dist.CLIENT)
   private SoundInstance spinSound;

   public StarlitCrusherEntity(EntityType<? extends StarlitCrusherEntity> type, Level worldIn) {
      super(type, worldIn);
      this.lookControl = new StunnableLookController(this);
      this.xpReward = 40;
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector2.addSkyGoal(1, new FloatGoal(this));
      this.goalSelector2.addSkyGoal(2, new NearestAttackableTargetGoal(this, Player.class, false).setUnseenMemoryTicks(6000));
      this.goalSelector2.addSkyGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector2.addSkyGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector2.addSkyGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.goalSelector2.addSkyGoal(2, new StarlitCrusherEntity.GrowWoodenWallsGoal());
      this.goalSelector2.addSkyGoal(3, new StarlitCrusherEntity.DirectRootAttackGoal());
      this.goalSelector2.addSkyGoal(3, new StarlitCrusherEntity.SurroundingRootAttackGoal());
      this.goalSelector2.addSkyGoal(3, new StarlitCrusherEntity.SmashGroundAttackGoal());
      this.goalSelector2.addSkyGoal(3, new StarlitCrusherEntity.SpinAttackGoal());
      this.goalSelector2.addSkyGoal(3, new StarlitCrusherEntity.SpewLeavesAttackGoal());
      this.goalSelector2.addSkyGoal(3, new StarlitCrusherEntity.OwlSpawnAttackGoal());
      this.goalSelector2.addSkyGoal(3, new StarlitCrusherEntity.SpewterSpawnAttackGoal());
   }

   @Override
   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(ROOTED, false);
      builder.define(TIME_STUNNED, 0);
      builder.define(SPIN_COOLDOWN, 400);
      builder.define(SPINNING, false);
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 500.0)
         .add(Attributes.FOLLOW_RANGE, 30.0)
         .add(Attributes.MOVEMENT_SPEED, 0.2F)
         .add(Attributes.ATTACK_DAMAGE, 10.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 100.0);
   }

   @Override
   public double getBossHealth() {
      return 500.0 + this.getExtraPlayers() * 150.0;
   }

   @Override
   public float getHealAmount() {
      return 30.0F;
   }

   @Override
   public LootBagItem getLootBag() {
      return SkiesItems.loot_bag_starlit_crusher;
   }

   @Override
   public TrophyBlock getTrophy() {
      return SkiesBlocks.starlit_crusher_trophy;
   }

   @Override
   public float getMaxReach() {
      return 4.15F;
   }

   @Override
   public void move(MoverType type, Vec3 vec) {
      if (!this.isBossInvul() && !this.isStunned() && !this.isRooted() && this.isAlive()) {
         super.move(type, vec);
      } else {
         super.move(type, new Vec3(0.0, vec.y, 0.0));
      }
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn
   ) {
      if (reason == MobSpawnType.TRIGGERED && this.getHome() != null) {
         this.level().destroyBlock(this.getHome(), false);
      }

      if (!this.level().isClientSide) {
         this.getAttribute(Attributes.ATTACK_DAMAGE)
            .setBaseValue(
               this.getDifficultyID() >= ISkyBoss.BossLevel.GOLD.getId() ? 12.0 : (this.getDifficultyID() == ISkyBoss.BossLevel.SILVER.getId() ? 10.0 : 8.0)
            );
      }

      return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
   }

   @Override
   public void load(CompoundTag compound) {
      super.load(compound);
      this.setRooted(compound.getBoolean("IsRooted"));
      this.setStunTime(compound.getInt("StunTime"));
      this.setSpinCooldown(compound.getInt("SpinCooldown"));
      this.rootWaveCooldown = compound.getInt("RootWaveCooldown");
      this.regrowWallsTimer = compound.getInt("WallRegrowthTime");
      this.owlSpawnCooldown = compound.getInt("OwlSpawnCooldown");
      this.deathTicks = compound.getInt("BossDeathTicks");
      this.forceHomeTimer = compound.getInt("ForceHomeTimer");
      this.spearImmuneTime = compound.getInt("SpearImmuneTime");
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("IsRooted", this.isRooted());
      compound.putInt("StunTime", this.getStunTime());
      compound.putInt("SpinCooldown", this.getSpinCooldown());
      compound.putInt("RootWaveCooldown", this.rootWaveCooldown);
      compound.putInt("WallRegrowthTime", this.regrowWallsTimer);
      compound.putInt("OwlSpawnCooldown", this.owlSpawnCooldown);
      compound.putInt("BossDeathTicks", this.deathTicks);
      compound.putInt("ForceHomeTimer", this.forceHomeTimer);
      compound.putInt("SpearImmuneTime", this.spearImmuneTime);
   }

   public boolean canBeAffected(MobEffectInstance effect) {
      return effect.getEffect() != MobEffects.LEVITATION && effect.getEffect() != MobEffects.MOVEMENT_SLOWDOWN ? super.canBeAffected(effect) : false;
   }

   public void setTarget(LivingEntity living) {
      if (living == null) {
         this.level().broadcastEntityEvent(this, (byte)6);
         this.canShake = false;
      }

      super.setTarget(living);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.attackTimer > 0) {
         this.attackTimer--;
      }

      if (this.smashTimer > 0) {
         this.smashTimer--;
      }

      if (this.meleeCooldown > 0) {
         this.meleeCooldown--;
      }

      if (this.rootWaveCooldown > 0) {
         this.rootWaveCooldown--;
      }

      if (this.owlSpawnCooldown > 0 && this.getOwlsInDungeonArea().isEmpty()) {
         this.owlSpawnCooldown--;
      }

      if (!this.isBossInvul() && !this.isStunned() && this.spearImmuneTime > 0) {
         this.spearImmuneTime--;
      }

      if (this.isSpinning()) {
         this.spinTimer++;
      } else {
         this.spinTimer = 0;
      }

      double distanceToHome = this.getHome() != null ? this.getDistanceToPos(this.getHome()) : 0.0;
      if (this.getTarget() != null && this.getSpinCooldown() > 0 && !this.isStunned() && !this.isSpinning() && distanceToHome <= 5.0) {
         this.setSpinCooldown(this.getSpinCooldown() - 1);
      }

      if (this.getWallsInDungeonArea(5.0).size() < 4 && this.regrowWallsTimer > 0) {
         this.regrowWallsTimer--;
      }

      if (!this.isNoAi() && this.isAlive()) {
         if (this.isStunned()) {
            this.setStunTime(this.getStunTime() - 1);
            if (this.getStunTime() == 1) {
               this.regrowWallsTimer = 0;
            }
         } else if (!this.isBossInvul()) {
            double distanceForPlayer = this.getWallsInDungeonArea(5.0).size() < 4 ? 2.75 : 2.1;
            if (!this.level().isClientSide) {
               if (this.meleeCooldown <= 0 && this.tickCount > 20) {
                  for (LivingEntity living : this.level()
                     .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(distanceForPlayer), this.canSeeAndAttackPredicate())) {
                     this.doHurtTarget(living);
                     if (!this.isSpinning()) {
                        this.meleeCooldown = 20;
                     }

                     if (this.getTarget() == null) {
                        this.setTarget(living);
                     }
                  }
               }

               if (this.isDungeonSpawned()) {
                  int walls = this.getWallsInDungeonArea(5.0).size();
                  if ((!(distanceToHome > 0.1) || this.getSpinCooldown() <= 0) && walls < 4) {
                     if (walls < 4) {
                        this.forceHomeTimer = 140;
                     }
                  } else if (this.forceHomeTimer > 0) {
                     if (distanceToHome > 5.0) {
                        this.getNavigation().moveTo(this.getHome().getX() + 0.5F, this.getHome().getY(), this.getHome().getZ() + 0.5F, 1.5);
                     } else if (distanceToHome > 0.1) {
                        this.getNavigation().stop();
                        this.setPos(
                           Mth.approach((float)this.getX(), this.getHome().getX() + 0.5F, 0.01F),
                           this.getY(),
                           Mth.approach((float)this.getZ(), this.getHome().getZ() + 0.5F, 0.01F)
                        );
                        this.getMoveControl().setWantedPosition(this.getHome().getX() + 0.5F, this.getHome().getY(), this.getHome().getZ() + 0.5F, 1.0);
                     }

                     this.forceHomeTimer = this.lowerCooldown(this.forceHomeTimer);
                  } else if (distanceToHome > 0.5) {
                     this.teleportTo(this.getHome().getX() + 0.5F, this.getHome().getY(), this.getHome().getZ() + 0.5F);
                  }
               }
            }
         }

         if (!this.level().isClientSide && this.getTarget() == null) {
            this.setSpinCooldown(400);
            this.setSpinning(false);
         }

         if (this.isRooted() && this.tickCount % 40 == 0) {
            if (this.getHealth() >= this.getMaxHealth() && this.owlSpawnCooldown > 0) {
               this.setRooted(false);
            }

            if (this.getDifficultyID() != ISkyBoss.BossLevel.PLATINUM.getId() && this.random.nextFloat() < 0.3F
               || this.getDifficultyID() == ISkyBoss.BossLevel.PLATINUM.getId()) {
               this.heal(this.random.nextInt(2) + 1);
               this.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_HEAL, 0.8F, 1.0F);
            }
         }

         if (!this.level().isClientSide && this.getTarget() != null && (!EntityUtil.CAN_ATTACK.test(this.getTarget()) || !this.getTarget().isAlive())) {
            this.setTarget(null);
         }

         if (this.isSpinning() && this.level() instanceof ServerLevel) {
            for (int i = 0; i < 2; i++) {
               ((ServerLevel)this.level())
                  .sendParticles(
                     ParticleTypes.POOF,
                     this.getX(),
                     this.getY(),
                     this.getZ(),
                     1,
                     this.getBoundingBox().getXsize() / 4.0,
                     0.1,
                     this.getBoundingBox().getZsize() / 4.0,
                     0.05
                  );
               ((ServerLevel)this.level())
                  .sendParticles(
                     new BlockParticleOption(ParticleTypes.BLOCK, this.level().getBlockState(this.blockPosition().below())),
                     this.getX(),
                     this.getY(),
                     this.getZ(),
                     3,
                     this.getBoundingBox().getXsize() / 4.0,
                     0.1,
                     this.getBoundingBox().getZsize() / 4.0,
                     0.05
                  );
            }
         }
      }
   }

   @Override
   public void invulnerableTick() {
      super.invulnerableTick();
      if (this.level() instanceof ServerLevel) {
         ((ServerLevel)this.level())
            .sendParticles(
               SkiesParticles.FALLING_LEAF,
               this.getX(),
               this.getY() + this.getBbHeight() + 0.1F,
               this.getZ(),
               1,
               this.getBoundingBox().getXsize() / 2.0,
               0.1F,
               this.getBoundingBox().getZsize() / 2.0,
               0.05
            );
      }
   }

   @Override
   public void peacefulTick() {
      this.setRooted(false);
      this.setSpinning(false);
      this.setStunned(false);
      this.rootWaveCooldown = 0;
      this.regrowWallsTimer = 0;
      this.owlSpawnCooldown = 400;
      this.forceHomeTimer = 200;
      this.spearImmuneTime = 60;
      this.discardAllies();
   }

   private void discardAllies() {
      for (EntWallEntity walls : this.getWallsInDungeonArea(5.0)) {
         if (walls.isMultipartEntity()) {
            walls.playDamageEffect();
         }

         walls.discard();
      }

      for (SpewterEntity spewters : this.getSpewtersInDungeonArea()) {
         spewters.spawnAnim();
         spewters.discard();
      }

      for (SnowOwlEntity owls : this.getOwlsInDungeonArea()) {
         owls.spawnAnim();
         owls.discard();
      }
   }

   protected void tickDeath() {
      if (this.isStunned()) {
         this.setStunned(false);
      }

      boolean flag = this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
      this.discardAllies();
      this.deathTicks++;
      if (this.deathTicks < 19) {
         super.tickDeath();
      }

      float money = this.xpReward;
      if (this.deathTicks > 30 && this.deathTicks % 5 == 0 && flag) {
         this.dropExperience(Mth.floor(money * 0.08F));
      }

      if (this.deathTicks >= 100 && !this.level().isClientSide) {
         if (this.deathTicks >= 120) {
            if (this.isDungeonSpawned() && this.getHome() != null && this.getHome() != BlockPos.ZERO) {
               this.level().setBlockAndUpdate(this.getHome(), SkiesBlocks.potted_starlit_sapling.defaultBlockState());
            } else {
               this.level().setBlockAndUpdate(this.blockPosition(), SkiesBlocks.potted_starlit_sapling.defaultBlockState());
            }

            this.discard();
         }

         if (this.deathTicks % 2 == 0) {
            for (int i = 0; i < 20; i++) {
               this.level().broadcastEntityEvent(this, (byte)9);
            }
         }
      }

      if (this.deathTicks % 5 == 0 && this.level() instanceof ServerLevel) {
         for (int i = 0; i < 4; i++) {
            ((ServerLevel)this.level())
               .sendParticles(
                  new BlockParticleOption(ParticleTypes.BLOCK, SkiesBlocks.starlit_log.defaultBlockState()),
                  this.getX(),
                  this.getY() + 0.1F,
                  this.getZ(),
                  40,
                  this.getBoundingBox().getXsize() / 4.0,
                  this.getBoundingBox().getYsize() / 4.0,
                  this.getBoundingBox().getZsize() / 4.0,
                  0.05
               );
         }
      }
   }

   private void dropExperience(int amount) {
      while (amount > 0) {
         int i = ExperienceOrb.getExperienceValue(amount);
         amount -= i;
         this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY(), this.getZ(), i));
      }
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   public void kill() {
      for (EntWallEntity walls : this.getWallsInDungeonArea(5.0)) {
         walls.playDamageEffect();
         walls.discard();
      }

      for (SpewterEntity spewters : this.getSpewtersInDungeonArea()) {
         spewters.spawnAnim();
         spewters.discard();
      }

      for (SnowOwlEntity owls : this.getOwlsInDungeonArea()) {
         owls.spawnAnim();
         owls.discard();
      }

      this.discard();
   }

   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return !this.isRooted() && !this.isStunned() ? 2.3F : 1.3F;
   }

   public boolean doHurtTarget(Entity entityIn) {
      if (entityIn instanceof Player playerentity) {
         ItemStack playerItem = playerentity.isUsingItem() ? playerentity.getUseItem() : ItemStack.EMPTY;
         if (!playerItem.isEmpty() && playerItem.canPerformAction(ItemAbilities.SHIELD_BLOCK) && !this.isStunned()) {
            if (playerentity instanceof ServerPlayer) {
               playerItem.hurt(this.random.nextInt(5) + 5, this.random, (ServerPlayer)playerentity);
            }

            playerentity.disableShield(true);
            playerentity.getCooldowns().addCooldown(Items.SHIELD, 800);
            this.level().broadcastEntityEvent(playerentity, (byte)30);
         }
      }

      if (!this.isSpinning()) {
         this.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_ATTACK, 1.0F, 1.0F);
         this.attackTimer = 10;
         this.level().broadcastEntityEvent(this, (byte)4);
         entityIn.setDeltaMovement(this.getViewVector(1.0F).x() * 3.0, 0.5, this.getViewVector(1.0F).z() * 3.0);
      } else {
         entityIn.setDeltaMovement(this.getViewVector(1.0F).x() * 5.0, 0.5, this.getViewVector(1.0F).z() * 5.0);
      }

      return super.doHurtTarget(entityIn);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (source == this.damageSources().fellOutOfWorld()) {
         return super.hurt(source, amount);
      }

      if (this.getInvulTime() <= 0
         && !this.isSpinning()
         && (source.isCreativePlayer() || this.getWallsInDungeonArea(5.0).size() < 4)
         && source != this.damageSources().inWall()) {
         if (this.hurtTime <= 0 && this.level() instanceof ServerLevel) {
            ((ServerLevel)this.level())
               .sendParticles(
                  SkiesParticles.FALLING_LEAF,
                  this.getX(),
                  this.getY() + this.getBbHeight() + 0.1F,
                  this.getZ(),
                  60,
                  this.getBoundingBox().getXsize() / 2.0,
                  0.1F,
                  this.getBoundingBox().getZsize() / 2.0,
                  0.05
               );
         }

         if (!this.level().isClientSide()
            && this.getWallsInDungeonArea(5.0).size() < 4
            && this.spearImmuneTime <= 0
            && source.is(DamageTypeTags.IS_PROJECTILE)
            && source.getDirectEntity() instanceof SpearEntity) {
            if (!this.isStunned()) {
               this.setStunned(true);
               this.setRooted(false);
               return super.hurt(source, Math.min(this.getHealth(), amount / 2.0F));
            } else {
               return super.hurt(source, amount * 0.3F);
            }
         } else if (source.isCreativePlayer()) {
            if (source.getEntity() instanceof Player) {
               ItemStack stack = ((LivingEntity)source.getEntity()).getMainHandItem();
               if (stack.getItem() == Items.DEBUG_STICK) {
                  return false;
               }
            }

            return super.hurt(source, amount);
         } else {
            if (source.getDirectEntity() instanceof LivingEntity) {
               ItemStack stack = ((LivingEntity)source.getDirectEntity()).getMainHandItem();
               if (stack.getItem() instanceof SkyAxeItem) {
                  if (this.isStunned()) {
                     return super.hurt(source, amount);
                  }

                  return super.hurt(source, amount * 0.3F);
               }
            }

            if (source.getDirectEntity() instanceof Player) {
               ((Player)source.getDirectEntity()).displayClientMessage(Component.translatable("gui.blue_skies.tooltip.invalid_ent_weapon"), true);
            }

            return super.hurt(source, Math.min(5.0F, amount * 0.1F));
         }
      } else {
         return false;
      }
   }

   protected SoundEvent getAmbientSound() {
      return this.isStunned() ? null : SkiesSounds.ENTITY_STARLIT_CRUSHER_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_STARLIT_CRUSHER_HURT;
   }

   protected SoundEvent getDeathSound() {
      this.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_DEATH, this.getSoundVolume(), 1.0F);
      return null;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      if (!this.isStunned() && !this.isSpinning() && !this.isRooted()) {
         this.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_STEP, 0.25F, 1.0F);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      switch (id) {
         case 4:
            if (!this.isSpinning()) {
               this.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_ATTACK, 1.0F, 1.0F);
            }

            this.attackTimer = 10;
            break;
         case 5:
            this.smashTimer = 21;
            break;
         case 6:
            this.owlSpawnCooldown = 400;
            this.canShake = false;
            break;
         case 7:
            this.canShake = true;
            break;
         case 8:
            this.canShake = false;
            break;
         case 9:
            this.spawnParticles(ParticleTypes.POOF);
            break;
         case 10:
            this.playStunSound();
         case 11:
            this.playSpinSound();
         default:
            super.handleEntityEvent(id);
      }
   }

   public void knockback(double strength, double x, double z) {
   }

   public void push(double x, double y, double z) {
   }

   public boolean isRooted() {
      return (Boolean)this.entityData.get(ROOTED);
   }

   public void setRooted(boolean rooted) {
      if (rooted) {
         this.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_ROOT, this.getSoundVolume(), this.getVoicePitch());
      }

      this.entityData.set(ROOTED, rooted);
   }

   @Override
   public boolean isStunned() {
      return this.getStunTime() > 0;
   }

   public void setStunned(boolean stunned) {
      this.setStunned(stunned, this.getDifficultyID() >= ISkyBoss.BossLevel.GOLD.getId() ? 120 : 130);
   }

   public void setStunned(boolean stunned, int time) {
      if (stunned) {
         this.setSpinning(false);
         this.setStunTime(time);
         this.level().broadcastEntityEvent(this, (byte)10);

         for (Player players : this.getPlayersInDungeonArea()) {
            players.stopUsingItem();
            SPEARS.forEach(spear -> players.getCooldowns().addCooldown(spear, time * 3));
         }
      } else {
         this.setStunTime(0);
      }
   }

   @OnlyIn(Dist.CLIENT)
   private void playStunSound() {
      if (!this.isSilent()) {
         SoundManager soundHandler = Minecraft.getInstance().getSoundManager();
         if (this.stunSound == null || this.stunSound != null && !soundHandler.isActive(this.stunSound)) {
            soundHandler.play(this.stunSound = new StunnedEntitySound(this, SkiesSounds.ENTITY_STARLIT_CRUSHER_STUNNED, this.getSoundVolume()));
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   private void playSpinSound() {
      if (!this.isSilent()) {
         SoundManager soundHandler = Minecraft.getInstance().getSoundManager();
         if (this.spinSound == null || this.spinSound != null && !soundHandler.isActive(this.spinSound)) {
            soundHandler.play(this.spinSound = new StarlitCrusherSpinSound(this, SkiesSounds.ENTITY_STARLIT_CRUSHER_SPIN, this.getSoundVolume()));
         }
      }
   }

   public int getStunTime() {
      return (Integer)this.entityData.get(TIME_STUNNED);
   }

   public void setStunTime(int time) {
      if (time <= 0) {
         this.spearImmuneTime = 60;
         this.meleeCooldown = 15;
         if (this.owlSpawnCooldown <= 200) {
            this.owlSpawnCooldown = 200;
         }
      }

      this.entityData.set(TIME_STUNNED, time);
   }

   public int getSpinCooldown() {
      return (Integer)this.entityData.get(SPIN_COOLDOWN);
   }

   public void setSpinCooldown(int time) {
      this.entityData.set(SPIN_COOLDOWN, time);
   }

   public boolean isSpinning() {
      return (Boolean)this.entityData.get(SPINNING);
   }

   public void setSpinning(boolean spin) {
      if (spin != this.isSpinning() && spin && !this.level().isClientSide) {
         this.level().broadcastEntityEvent(this, (byte)11);
      }

      this.spinTimer = 0;
      this.entityData.set(SPINNING, spin);
   }

   public int getDeathTicks() {
      return this.deathTicks;
   }

   public boolean canWallsGoUp() {
      double distance = this.getWallsInDungeonArea(5.0).size() < 4 ? 4.1 : 3.6;
      Player closePlayer = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), distance, true);
      return !this.isSpinning()
         && this.getHome() != null
         && this.getDistanceToPos(this.getHome()) <= 1.5
         && (
            closePlayer != null && !this.getSensing().hasLineOfSight(closePlayer)
               || closePlayer == null
               || closePlayer != null && this.getSensing().hasLineOfSight(closePlayer)
         );
   }

   public boolean canCollideWith(Entity entityIn) {
      return canCollideWithEntity(this, entityIn);
   }

   public static boolean canCollideWithEntity(Entity currentEntity, Entity collidingEntity) {
      return (collidingEntity.canBeCollidedWith() || collidingEntity.isPushable())
         && !currentEntity.isPassengerOfSameVehicle(collidingEntity)
         && !collidingEntity.isAlliedTo(currentEntity);
   }

   public boolean canBeCollidedWith() {
      return this.isAlive();
   }

   public List<EntWallEntity> getWallsInDungeonArea(double distance) {
      return !this.level().isClientSide && this.getHome() != null && this.getHome() != BlockPos.ZERO
         ? this.level()
            .getEntitiesOfClass(
               EntWallEntity.class,
               new AABB(
                  this.getHome().getX() - distance,
                  this.getHome().getY() - 1.0,
                  this.getHome().getZ() - distance,
                  this.getHome().getX() + distance,
                  this.getHome().getY() + 10.0,
                  this.getHome().getZ() + distance
               )
            )
         : List.of();
   }

   public List<EntRootEntity> getRootsInDungeonArea(double distance) {
      return !this.level().isClientSide && this.getHome() != null && this.getHome() != BlockPos.ZERO
         ? this.level()
            .getEntitiesOfClass(
               EntRootEntity.class,
               new AABB(
                  this.getHome().getX() - distance,
                  this.getHome().getY() - 1.0,
                  this.getHome().getZ() - distance,
                  this.getHome().getX() + distance,
                  this.getHome().getY() + 10.0,
                  this.getHome().getZ() + distance
               )
            )
         : List.of();
   }

   public List<SnowOwlEntity> getOwlsInDungeonArea() {
      double distance = 20.0;
      return !this.level().isClientSide && this.getHome() != null && this.getHome() != BlockPos.ZERO
         ? this.level()
            .getEntitiesOfClass(
               SnowOwlEntity.class,
               new AABB(
                  this.getHome().getX() - distance,
                  this.getHome().getY() - 1.0,
                  this.getHome().getZ() - distance,
                  this.getHome().getX() + distance,
                  this.getHome().getY() + 15.0,
                  this.getHome().getZ() + distance
               ),
               OWL_HOSTILE
            )
         : List.of();
   }

   public List<SpewterEntity> getSpewtersInDungeonArea() {
      double distance = 10.0;
      return !this.level().isClientSide && this.getHome() != null && this.getHome() != BlockPos.ZERO
         ? this.level()
            .getEntitiesOfClass(
               SpewterEntity.class,
               new AABB(
                  this.getHome().getX() - distance,
                  this.getHome().getY() - 1.0,
                  this.getHome().getZ() - distance,
                  this.getHome().getX() + distance,
                  this.getHome().getY() + 10.0,
                  this.getHome().getZ() + distance
               )
            )
         : List.of();
   }

   @Override
   public BossBarColor getBossBarColor() {
      return BossBarColor.GREEN;
   }

   @Override
   public SkiesDungeonType getBossType() {
      return SkiesDungeonType.NATURE;
   }

   @Override
   public int getProgression() {
      return 2;
   }

   @Override
   public boolean isEverbrightBoss() {
      return true;
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

   public boolean isPushedByFluid() {
      return false;
   }

   public boolean updateFluidHeightAndDoFluidPushing(TagKey<Fluid> fluidTag, double p_210500_2_) {
      return this.isDungeonSpawned() ? false : super.updateFluidHeightAndDoFluidPushing(fluidTag, p_210500_2_);
   }

   public void spawnRoots(double xPos, double zPos, double minimumY, double yPos, float rotationIn, int warmupDelay) {
      BlockPos blockpos = Positions.blockPos(xPos, yPos, zPos);
      boolean flag = false;
      double d0 = 0.0;

      do {
         BlockPos blockpos1 = blockpos.below();
         BlockState blockstate = this.level().getBlockState(blockpos1);
         if (blockstate.isFaceSturdy(this.level(), blockpos1, Direction.UP)) {
            if (this.level().isEmptyBlock(blockpos)) {
               BlockState blockstate1 = this.level().getBlockState(blockpos);
               VoxelShape voxelshape = blockstate1.getCollisionShape(this.level(), blockpos);
               if (!voxelshape.isEmpty()) {
                  d0 = voxelshape.max(Axis.Y);
               }
            }

            flag = true;
            break;
         }

         blockpos = blockpos.below();
      } while (blockpos.getY() >= Mth.floor(minimumY) - 1);

      if (flag
         && this.level().noCollision(new AABB(blockpos))
         && (
            this.getDifficultyID() != ISkyBoss.BossLevel.BRONZE.getId()
               || this.getDifficultyID() == ISkyBoss.BossLevel.BRONZE.getId() && this.level().random.nextFloat() < 0.7F
         )) {
         this.level().addFreshEntity(new EntRootEntity(this.level(), xPos, blockpos.getY() + d0, zPos, rotationIn, warmupDelay, this, this.isSecondPhase()));
      }
   }

   public void spawnLeaf(LivingEntity target) {
      double d0 = target.getX();
      double d1 = target.getBoundingBox().minY + target.getBbHeight() / 2.0F;
      double d2 = target.getZ();
      this.spawnLeafAtPos(Positions.blockPos(d0, d1, d2));
   }

   public void spawnLeafAtPos(Vec3i pos) {
      EntLeafEntity leaf = new EntLeafEntity(SkiesEntityTypes.ENT_LEAF, this, this.level());
      leaf.setPos(
         this.getX() - (this.getBbWidth() + 1.0F) * 0.5 * Mth.sin(this.yBodyRot * (float) (Math.PI / 180.0)),
         this.getY() + this.getEyeHeight() + 1.0,
         this.getZ() + (this.getBbWidth() + 1.0F) * 0.5 * Mth.cos(this.yBodyRot * (float) (Math.PI / 180.0))
      );
      double d0 = pos.getX() - this.getX();
      double d1 = pos.getY() - leaf.getY();
      double d2 = pos.getZ() - this.getZ();
      float f = Mth.sqrt((float)(d0 * d0 + d2 * d2)) * 0.2F;
      leaf.shoot(d0, d1 + f, d2, 1.0F, 10.0F);
      this.level()
         .playSound(
            (Player)null,
            this.getX(),
            this.getY(),
            this.getZ(),
            SkiesSounds.ENTITY_STARLIT_CRUSHER_FLING_LEAF,
            this.getSoundSource(),
            1.0F,
            1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F
         );
      this.level().addFreshEntity(leaf);
   }

   public void checkAndSpawnSpewters() {
      if (!this.level().isClientSide) {
         for (BlockPos pos : EntityUtil.createSquareOffsetsFromPos(this.getHome(), 8.0)) {
            if (this.getSpewtersInSpot(pos).isEmpty()) {
               SpewterEntity spewter = new SpewterEntity(SkiesEntityTypes.SPEWTER, this.level());
               spewter.setPos(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F);
               spewter.setGrown(true);
               double maxHealth = this.getDifficultyID() >= ISkyBoss.BossLevel.PLATINUM.getId()
                  ? 40.0
                  : (
                     this.getDifficultyID() == ISkyBoss.BossLevel.GOLD.getId()
                        ? 32.0
                        : (this.getDifficultyID() == ISkyBoss.BossLevel.SILVER.getId() ? 28.0 : 26.0)
                  );
               spewter.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);
               spewter.setHealth((float)maxHealth);
               this.level().addFreshEntity(spewter);
               spewter.playSound(SkiesSounds.ENTITY_SPEWTER_GROW, 1.0F, this.getVoicePitch());
            }
         }
      }
   }

   public List<SpewterEntity> getSpewtersInSpot(BlockPos pos) {
      return this.level().getEntitiesOfClass(SpewterEntity.class, new AABB(pos).inflate(1.0));
   }

   class DirectRootAttackGoal extends SkiesBossEntity.AbstractBossGoal {
      private final StarlitCrusherEntity boss = StarlitCrusherEntity.this;
      private int chargeTime;

      public DirectRootAttackGoal() {
      }

      public boolean canUse() {
         return this.boss.owlSpawnCooldown > 0
            && this.boss.getOwlsInDungeonArea().isEmpty()
            && !this.boss.isSpinning()
            && this.boss.canWallsGoUp()
            && !this.boss.isStunned()
            && this.boss.getTarget() != null
            && !this.boss.isBossInvul()
            && this.boss.getWallsInDungeonArea(5.0).size() >= 4
            && this.boss.regrowWallsTimer > 0;
      }

      public boolean canContinueToUse() {
         return this.boss.owlSpawnCooldown > 0
            && this.boss.getOwlsInDungeonArea().isEmpty()
            && this.boss.getTarget() != null
            && this.chargeTime > 0
            && !this.boss.isStunned()
            && this.boss.getWallsInDungeonArea(5.0).size() >= 4
            && this.boss.regrowWallsTimer > 0;
      }

      public void start() {
         List<Player> playersInDungeon = this.boss.getPlayersInDungeonArea();

         for (Player players : playersInDungeon) {
            if (players != this.boss.getTarget()
               && this.boss.random.nextBoolean()
               && playersInDungeon.size() > 1
               && !players.isCreative()
               && !players.isSpectator()) {
               this.boss.setTarget(players);
            }
         }

         this.chargeTime = 160;
         this.boss.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_PREPARE_SMALL_GROWTH, 2.0F, this.boss.getVoicePitch());
      }

      public void tick() {
         if (this.boss.hasAttackTarget()) {
            this.boss.getLookControl().setLookAt(this.boss.getTarget(), 30.0F, 30.0F);
            if (this.chargeTime > 0) {
               this.chargeTime--;
            }

            if (this.chargeTime >= 70 && this.chargeTime % 10 == 0) {
               if (!(this.boss.getTarget() instanceof Player)) {
                  LivingEntity livingentity = this.boss.getTarget();
                  double d0 = Math.min(livingentity.getY(), this.boss.getY());
                  double d1 = Math.max(livingentity.getY(), this.boss.getY()) + 1.0;
                  float f = (float)Mth.atan2(livingentity.getZ() - this.boss.getZ(), livingentity.getX() - this.boss.getX());

                  for (int l = 0; l < 16; l++) {
                     double d2 = 1.25 * (l + 3);
                     int j = 1 * l;
                     this.boss.spawnRoots(this.boss.getX() + Mth.cos(f) * d2, this.boss.getZ() + Mth.sin(f) * d2, d0, d1, f, j);
                  }
               }

               if (!this.boss.getPlayersInDungeonArea().isEmpty()) {
                  for (Player players : this.boss.getPlayersInDungeonArea()) {
                     double d0 = Math.min(players.getY(), this.boss.getY());
                     double d1 = Math.max(players.getY(), this.boss.getY()) + 1.0;
                     float f = (float)Mth.atan2(players.getZ() - this.boss.getZ(), players.getX() - this.boss.getX());

                     for (int l = 0; l < 16; l++) {
                        double d2 = 1.25 * (l + 3);
                        int j = 1 * l;
                        this.boss.spawnRoots(this.boss.getX() + Mth.cos(f) * d2, this.boss.getZ() + Mth.sin(f) * d2, d0, d1, f, j);
                     }
                  }
               }
            }
         }
      }
   }

   public enum EntLegPosition {
      NONE,
      SPINNING,
      ROOTED,
      STUNNED,
      DYING;

      public static StarlitCrusherEntity.EntLegPosition getEntPosition(StarlitCrusherEntity ent) {
         if (ent.getDeathTicks() > 0) {
            return DYING;
         } else if (ent.isSpinning()) {
            return SPINNING;
         } else if (ent.isRooted()) {
            return ROOTED;
         } else {
            return ent.isStunned() ? STUNNED : NONE;
         }
      }
   }

   class GrowWoodenWallsGoal extends SkiesBossEntity.AbstractBossGoal {
      private final StarlitCrusherEntity boss = StarlitCrusherEntity.this;
      private int chargeTime;

      public GrowWoodenWallsGoal() {
      }

      public boolean canUse() {
         return this.boss.getSpinCooldown() <= 25
            ? false
            : this.boss.canWallsGoUp()
               && !this.boss.isStunned()
               && this.boss.getTarget() != null
               && !this.boss.isBossInvul()
               && (this.boss.regrowWallsTimer <= 0 || this.boss.getWallsInDungeonArea(5.0).isEmpty());
      }

      public boolean canContinueToUse() {
         return this.boss.isSecondPhase() && this.boss.getSpinCooldown() <= 0
            ? false
            : this.boss.canWallsGoUp() && this.boss.getTarget() != null && this.chargeTime > 0 && !this.boss.isStunned();
      }

      public void start() {
         this.boss.randomizeAttackTarget(this.boss.random.nextBoolean());

         for (SnowOwlEntity owl : this.boss.getOwlsInDungeonArea()) {
            owl.spawnAnim();
            owl.discard();
         }

         this.chargeTime = 20;
         this.boss.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_PREPARE_LARGE_GROWTH, this.boss.getSoundVolume(), this.boss.getVoicePitch());
      }

      public void tick() {
         Level world = this.boss.level();
         if (this.boss.hasAttackTarget()) {
            this.boss.getLookControl().setLookAt(this.boss.getTarget(), 30.0F, 30.0F);
         }

         if (this.chargeTime > 0) {
            this.chargeTime--;
         }

         if (!world.isClientSide && this.chargeTime == 1) {
            BlockPos home = this.boss.getHome();
            if (!this.boss.getWallsInDungeonArea(5.0).isEmpty()) {
               for (EntWallEntity walls : this.boss.getWallsInDungeonArea(5.0)) {
                  walls.setHealth(walls.getMaxHealth());
               }
            }

            if (this.getWallsInSpot(home.offset(3, 0, 0)).isEmpty()) {
               EntWallEntity wallx = new EntWallEntity(world, home.getX() + 3, home.getY(), home.getZ(), this.boss);
               wallx.setFacingZ(false);
               wallx.setHealth(wallx.getMaxHealth());
               world.addFreshEntity(wallx);
               wallx.playSound(SkiesSounds.ENTITY_STARLIT_WALL_GROW, 1.0F, this.boss.getVoicePitch());
            }

            if (this.getWallsInSpot(home.offset(-3, 0, 0)).isEmpty()) {
               EntWallEntity wallx2 = new EntWallEntity(world, home.getX() - 3, home.getY(), home.getZ(), this.boss);
               wallx2.setFacingZ(false);
               wallx2.setHealth(wallx2.getMaxHealth());
               world.addFreshEntity(wallx2);
               wallx2.playSound(SkiesSounds.ENTITY_STARLIT_WALL_GROW, 1.0F, this.boss.getVoicePitch());
            }

            if (this.getWallsInSpot(home.offset(0, 0, 3)).isEmpty()) {
               EntWallEntity wallz = new EntWallEntity(world, home.getX(), home.getY(), home.getZ() + 3, this.boss);
               wallz.setFacingZ(true);
               wallz.setHealth(wallz.getMaxHealth());
               world.addFreshEntity(wallz);
               wallz.playSound(SkiesSounds.ENTITY_STARLIT_WALL_GROW, 1.0F, this.boss.getVoicePitch());
            }

            if (this.getWallsInSpot(home.offset(0, 0, -3)).isEmpty()) {
               EntWallEntity wallz2 = new EntWallEntity(world, home.getX(), home.getY(), home.getZ() - 3, this.boss);
               wallz2.setFacingZ(true);
               wallz2.setHealth(wallz2.getMaxHealth());
               world.addFreshEntity(wallz2);
               wallz2.playSound(SkiesSounds.ENTITY_STARLIT_WALL_GROW, 1.0F, this.boss.getVoicePitch());
            }

            this.boss.regrowWallsTimer = 100;
         }
      }

      public List<EntWallEntity> getWallsInSpot(BlockPos pos) {
         return this.boss.level().getEntitiesOfClass(EntWallEntity.class, new AABB(pos).inflate(1.0));
      }
   }

   class OwlSpawnAttackGoal extends SkiesBossEntity.AbstractBossGoal {
      private final StarlitCrusherEntity boss = StarlitCrusherEntity.this;
      private int chargeTime;
      protected List<BlockPos> outsidePos = new ArrayList<>();
      private BlockPos posToExitAt;

      public OwlSpawnAttackGoal() {
      }

      public boolean canUse() {
         return this.boss.getOwlsInDungeonArea().isEmpty()
            && this.boss.owlSpawnCooldown <= 0
            && !this.boss.isSpinning()
            && this.boss.canWallsGoUp()
            && !this.boss.isStunned()
            && this.boss.getTarget() != null
            && !this.boss.isStunned()
            && !this.boss.isBossInvul()
            && this.boss.getWallsInDungeonArea(5.0).size() >= 4
            && this.boss.regrowWallsTimer > 0;
      }

      public boolean canContinueToUse() {
         return this.boss.getTarget() != null
            && this.chargeTime > 0
            && !this.boss.isStunned()
            && this.boss.getWallsInDungeonArea(5.0).size() >= 4
            && this.boss.regrowWallsTimer > 0
            && this.boss.isRooted();
      }

      public void start() {
         this.outsidePos.clear();

         for (int i = 0; i < 4; i++) {
            this.outsidePos.add(this.boss.getHome().relative(Direction.from2DDataValue(i), 7).above(8));
         }

         this.boss.randomizeAttackTarget(this.boss.random.nextBoolean());
         this.chargeTime = 80;
         this.boss.setRooted(true);
         this.boss.level().broadcastEntityEvent(this.boss, (byte)7);
         this.boss.canShake = true;
         this.boss.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_PREPARE_OWLS, 2.0F, 1.0F);
      }

      public void tick() {
         Level world = this.boss.level();
         if (this.boss.hasAttackTarget()) {
            this.boss.getLookControl().setLookAt(this.boss.getTarget(), 30.0F, 30.0F);
         }

         if (this.chargeTime > 0) {
            this.chargeTime--;
         }

         if (this.chargeTime == 20) {
            Player closestPlayer = this.boss.level().getNearestPlayer(this.boss, 30.0);
            if (closestPlayer != null && !closestPlayer.isCreative() && !closestPlayer.isSpectator()) {
               this.posToExitAt = EntityUtil.getClosestPosInList(closestPlayer.blockPosition(), this.outsidePos);
            }

            List<Player> targets = new ArrayList<>();
            targets.addAll(this.boss.getPlayersInDungeonArea());

            for (int i = 0; i < this.getMaxOwlsBasedOnPlayerCount(); i++) {
               SnowOwlEntity owl = (SnowOwlEntity)SkiesEntityTypes.SNOW_OWL.create(world);
               owl.setOwlHostile(true);
               owl.setPos(
                  this.boss.getX() + this.boss.getBbWidth() / 2.0F,
                  this.boss.getY() + this.boss.getBbHeight() + 0.5,
                  this.boss.getZ() + this.boss.getBbWidth() / 2.0F
               );
               owl.setDeltaMovement(owl.getDeltaMovement().add(0.0, 0.4F, 0.0));
               owl.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(25.0);
               owl.getAttribute(Attributes.MAX_HEALTH).setBaseValue(12.0);
               if (!targets.isEmpty()) {
                  Player player = targets.get(0);
                  owl.setTarget(player);
                  targets.remove(0);
               } else if (!this.boss.getPlayersInDungeonArea().isEmpty()) {
                  owl.setTarget((LivingEntity)this.boss.getPlayersInDungeonArea().get(world.random.nextInt(this.boss.getPlayersInDungeonArea().size())));
               } else if (this.boss.getTarget() != null) {
                  owl.setTarget(this.boss.getTarget());
               }

               owl.exitPos = this.posToExitAt != null ? this.posToExitAt : null;
               world.addFreshEntity(owl);
            }

            this.boss.playSound(SoundEvents.ITEM_PICKUP, this.boss.getSoundVolume(), EntityUtil.getSoundPitchWithStart(this.boss.level().random, 0.6F));
            this.boss.checkAndSpawnSpewters();
         } else if (this.chargeTime == 1) {
            world.broadcastEntityEvent(this.boss, (byte)6);
            this.boss.canShake = false;
            this.boss.owlSpawnCooldown = 400;
         }
      }

      public void stop() {
         if (!this.boss.isStunned() && this.boss.owlSpawnCooldown <= 200) {
            this.boss.owlSpawnCooldown = 200;
         }
      }

      public int getMaxOwlsBasedOnPlayerCount() {
         int difficultyAmp = this.boss.getDifficultyID() == ISkyBoss.BossLevel.SILVER.getId()
            ? 4
            : (this.boss.getDifficultyID() >= ISkyBoss.BossLevel.GOLD.getId() ? 5 : 3);
         int extraPlayers = Math.max(0, this.boss.getPlayersInDungeonArea().size() - 1);
         return difficultyAmp + extraPlayers;
      }
   }

   class SmashGroundAttackGoal extends SkiesBossEntity.AbstractBossGoal {
      private final StarlitCrusherEntity boss = StarlitCrusherEntity.this;
      private int chargeTime;

      public SmashGroundAttackGoal() {
      }

      public boolean canUse() {
         return !this.boss.isStunned()
            && this.boss.getTarget() != null
            && !this.boss.isStunned()
            && !this.boss.isBossInvul()
            && this.boss.regrowWallsTimer > 0
            && this.boss.getWallsInDungeonArea(5.0).size() < 4
            && !this.boss.getWallsInDungeonArea(5.0).isEmpty();
      }

      public boolean canContinueToUse() {
         return this.boss.canWallsGoUp() && this.boss.getTarget() != null && this.chargeTime > 0 && !this.boss.isStunned() && this.boss.regrowWallsTimer > 0;
      }

      public void start() {
         this.boss.randomizeAttackTarget(this.boss.random.nextBoolean());
         this.chargeTime = 110;
         if (this.boss.isRooted()) {
            this.boss.setRooted(false);
         }
      }

      public void tick() {
         Level world = this.boss.level();
         if (this.boss.hasAttackTarget()) {
            this.boss.getLookControl().setLookAt(this.boss.getTarget(), 30.0F, 30.0F);
         }

         if (this.chargeTime > 0) {
            this.chargeTime--;
         }

         if (this.chargeTime >= 20) {
            if (this.chargeTime < 100 && this.chargeTime % 20 == 0) {
               AABB hitbox = this.boss.getBoundingBox().inflate(15.0, 0.0, 15.0);
               this.boss.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_HAMMER_SMASH, 1.0F, this.boss.getVoicePitch());
               if (world instanceof ServerLevel) {
                  for (int i = 0; i < 20; i++) {
                     ((ServerLevel)world)
                        .sendParticles(SkiesParticles.CRUSHER_DUST, this.boss.getX(), this.boss.getY(), this.boss.getZ(), 10, 0.0, 0.0, 0.0, 0.05);
                     ((ServerLevel)world)
                        .sendParticles(
                           new BlockParticleOption(ParticleTypes.BLOCK, SkiesBlocks.nature_stone.defaultBlockState()),
                           this.boss.getX(),
                           this.boss.getY(),
                           this.boss.getZ(),
                           20,
                           hitbox.getXsize() / 4.0,
                           0.1,
                           hitbox.getZsize() / 4.0,
                           0.05
                        );
                  }
               }

               for (Entity entity : world.getEntities(this.boss, hitbox)) {
                  if (entity instanceof LivingEntity living
                     && living.onGround()
                     && living.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue() < 1.0
                     && living.hurt(SkiesDamageSources.get(living.level()).slam(this.boss), 3.0F)) {
                     living.setDeltaMovement(living.getDeltaMovement().x(), 0.7, living.getDeltaMovement().z());
                     if (living instanceof Player) {
                        ((Player)living).hurtMarked = true;
                     }
                  }
               }
            }

            if (this.chargeTime % 20 == 13) {
               this.boss.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_PREPARE_HAMMER, 1.0F, 1.0F);
               this.boss.smashTimer = 21;
               world.broadcastEntityEvent(this.boss, (byte)5);
            }
         }
      }
   }

   class SpewLeavesAttackGoal extends SkiesBossEntity.AbstractBossGoal {
      private final StarlitCrusherEntity boss = StarlitCrusherEntity.this;
      private int chargeTime;

      public SpewLeavesAttackGoal() {
      }

      public boolean canUse() {
         return this.boss.getSpinCooldown() <= 0
            && !this.boss.isSecondPhase()
            && this.boss.getTarget() != null
            && !this.boss.isStunned()
            && !this.boss.isBossInvul()
            && this.boss.regrowWallsTimer <= 0
            && !this.boss.getWallsInDungeonArea(15.0).isEmpty();
      }

      public boolean canContinueToUse() {
         return this.boss.getSpinCooldown() <= 0 && this.boss.getTarget() != null && !this.boss.isStunned() && this.chargeTime > 0;
      }

      public void start() {
         for (EntWallEntity walls : this.boss.getWallsInDungeonArea(5.0)) {
            walls.playDamageEffect();
            walls.discard();
         }

         this.chargeTime = 260;
         this.boss.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_PREPARE_SPIN, this.boss.getSoundVolume(), this.boss.getVoicePitch());
      }

      public void tick() {
         if (this.boss.hasAttackTarget()) {
            this.boss.getLookControl().setLookAt(this.boss.getTarget(), 30.0F, 30.0F);
            if (!this.boss.isSpinning()) {
               this.boss.setSpinning(true);
            } else {
               if (this.boss.tickCount % 2 == 0) {
                  LivingEntity target = this.boss.getTarget();
                  this.boss.spawnLeaf(target);

                  for (Direction dir : DirectionUtil.HORIZONTAL) {
                     this.boss.spawnLeafAtPos(this.boss.getHome().relative(dir, 10).above(2));
                  }
               }

               if (this.chargeTime > 0) {
                  this.chargeTime--;
               }
            }

            if (this.chargeTime == 1) {
               this.boss.setSpinCooldown(400);
               this.boss.setStunned(true, 50);
            }
         }
      }
   }

   class SpewterSpawnAttackGoal extends SkiesBossEntity.AbstractBossGoal {
      private final StarlitCrusherEntity boss = StarlitCrusherEntity.this;
      private int chargeTime;

      public SpewterSpawnAttackGoal() {
      }

      public boolean canUse() {
         return this.boss.getSpewtersInDungeonArea().size() <= 2
            && this.boss.isRooted()
            && !this.boss.isSpinning()
            && this.boss.canWallsGoUp()
            && !this.boss.isStunned()
            && this.boss.hasAttackTarget()
            && !this.boss.isBossInvul()
            && this.boss.getWallsInDungeonArea(5.0).size() >= 4
            && this.boss.regrowWallsTimer > 0;
      }

      public boolean canContinueToUse() {
         return this.boss.getTarget() != null
            && this.chargeTime > 0
            && !this.boss.isStunned()
            && this.boss.getWallsInDungeonArea(5.0).size() >= 4
            && this.boss.regrowWallsTimer > 0;
      }

      public void start() {
         this.boss.randomizeAttackTarget(this.boss.random.nextBoolean());
         this.chargeTime = 60;
         this.boss.level().broadcastEntityEvent(this.boss, (byte)7);
         this.boss.canShake = true;
      }

      public void tick() {
         Level world = this.boss.level();
         if (this.boss.hasAttackTarget()) {
            this.boss.getLookControl().setLookAt(this.boss.getTarget(), 30.0F, 30.0F);
         }

         if (this.chargeTime > 0) {
            this.chargeTime--;
         }

         if (this.chargeTime == 20) {
            this.boss.checkAndSpawnSpewters();
         } else if (this.chargeTime == 1) {
            world.broadcastEntityEvent(this.boss, (byte)8);
            this.boss.canShake = false;
         }
      }
   }

   class SpinAttackGoal extends SkiesBossEntity.AbstractBossGoal {
      private final StarlitCrusherEntity boss = StarlitCrusherEntity.this;
      protected List<BlockPos> outsidePos = new ArrayList<>();
      private int chargeTime;
      private BlockPos posToExitAt;

      public SpinAttackGoal() {
      }

      public boolean canUse() {
         return this.boss.getDifficultyID() != ISkyBoss.BossLevel.PLATINUM.getId()
               && this.boss.regrowWallsTimer > 0
               && !this.boss.getWallsInDungeonArea(15.0).isEmpty()
            ? false
            : this.boss.getSpinCooldown() <= 0
               && this.boss.isSecondPhase()
               && this.boss.getTarget() != null
               && !this.boss.isStunned()
               && !this.boss.isBossInvul();
      }

      public boolean canContinueToUse() {
         return this.boss.getSpinCooldown() <= 0 && this.boss.getTarget() != null && !this.boss.isStunned() && this.chargeTime > 0;
      }

      public void start() {
         this.outsidePos.clear();
         this.outsidePos.addAll(EntityUtil.createDiamondOffsetsFromPos(this.boss.getHome(), 7));
         Player closestPlayer = this.boss.level().getNearestPlayer(this.boss, 30.0);
         if (closestPlayer != null && !closestPlayer.isCreative() && !closestPlayer.isSpectator()) {
            this.posToExitAt = EntityUtil.getClosestPosInList(closestPlayer.blockPosition(), this.outsidePos);
            this.boss.setTarget(closestPlayer);
         }

         this.chargeTime = 300;
         if (this.boss.isRooted()) {
            this.boss.setRooted(false);
         }

         if (this.boss.owlSpawnCooldown < 100) {
            this.boss.owlSpawnCooldown = 100;
         }
      }

      public void tick() {
         if (this.boss.hasAttackTarget()) {
            BlockPos spinStartPos = this.boss.getHome() != null && this.posToExitAt != null ? this.posToExitAt : this.boss.blockPosition();
            this.boss.getLookControl().setLookAt(this.boss.getTarget(), 30.0F, 30.0F);
            double distance = this.boss.getDistanceToPos(spinStartPos);
            if (this.chargeTime >= 270) {
               if (this.chargeTime == 300) {
                  this.boss.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_PREPARE_HAMMER, 1.0F, 1.0F);
                  this.boss.smashTimer = 21;
                  this.boss.level().broadcastEntityEvent(this.boss, (byte)5);
               }

               if (this.chargeTime == 285) {
                  this.boss.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_HAMMER_SMASH, this.boss.getSoundVolume(), 1.0F);

                  for (EntWallEntity walls : this.boss.getWallsInDungeonArea(5.0)) {
                     walls.playDamageEffect();
                     walls.discard();
                  }
               }

               this.chargeTime--;
            } else {
               if (!this.boss.isSpinning() && this.chargeTime > 10) {
                  if (distance > 3.0) {
                     this.boss.getNavigation().moveTo(spinStartPos.getX(), spinStartPos.getY(), spinStartPos.getZ(), 2.0);
                  }

                  if (distance <= 3.0) {
                     this.boss.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_PREPARE_SPIN, this.boss.getSoundVolume(), this.boss.getVoicePitch());
                     this.boss.setSpinning(true);
                  }
               } else {
                  float spinSpeed = this.boss.getDifficultyID() == ISkyBoss.BossLevel.PLATINUM.getId()
                     ? 1.65F
                     : (this.boss.getDifficultyID() == ISkyBoss.BossLevel.GOLD.getId() ? 1.55F : 1.45F);
                  if (this.boss.getSensing().hasLineOfSight(this.boss.getTarget())) {
                     this.boss
                        .getMoveControl()
                        .setWantedPosition(this.boss.getTarget().getX(), this.boss.getTarget().getY(), this.boss.getTarget().getZ(), spinSpeed);
                  } else {
                     this.boss.getNavigation().moveTo(this.boss.getTarget().getX(), this.boss.getTarget().getY(), this.boss.getTarget().getZ(), spinSpeed);
                  }

                  if (this.chargeTime > 0) {
                     this.chargeTime--;
                  }
               }

               if (this.chargeTime == 1) {
                  this.boss.setSpinCooldown(800);
                  this.boss.setStunned(true, 80);
                  this.posToExitAt = null;
               }
            }
         }
      }
   }

   class SurroundingRootAttackGoal extends SkiesBossEntity.AbstractBossGoal {
      private final StarlitCrusherEntity boss = StarlitCrusherEntity.this;
      private int chargeTime;

      public SurroundingRootAttackGoal() {
      }

      public boolean canUse() {
         return !this.boss.isSpinning()
            && this.boss.canWallsGoUp()
            && !this.boss.isStunned()
            && this.boss.getTarget() != null
            && !this.boss.isStunned()
            && !this.boss.isBossInvul()
            && this.boss.getWallsInDungeonArea(5.0).size() >= 4
            && this.boss.rootWaveCooldown <= 0
            && this.boss.regrowWallsTimer > 0;
      }

      public boolean canContinueToUse() {
         return this.boss.canWallsGoUp()
            && this.boss.getTarget() != null
            && this.chargeTime > 0
            && !this.boss.isStunned()
            && this.boss.getWallsInDungeonArea(5.0).size() >= 4
            && this.boss.regrowWallsTimer > 0;
      }

      public void start() {
         List<Player> playersInDungeon = this.boss.getPlayersInDungeonArea();

         for (Player players : playersInDungeon) {
            if (players != this.boss.getTarget()
               && this.boss.random.nextBoolean()
               && playersInDungeon.size() > 1
               && !players.isCreative()
               && !players.isSpectator()) {
               this.boss.setTarget(players);
            }
         }

         this.chargeTime = 100;
      }

      public void tick() {
         if (this.boss.hasAttackTarget()) {
            Level world = this.boss.level();
            this.boss.getLookControl().setLookAt(this.boss.getTarget(), 30.0F, 30.0F);
            if (this.chargeTime > 0) {
               this.chargeTime--;
            }

            LivingEntity livingentity = this.boss.getTarget();
            double d0 = Math.min(livingentity.getY(), this.boss.getY());
            double d1 = Math.max(livingentity.getY(), this.boss.getY()) + 1.0;
            float f = (float)Mth.atan2(livingentity.getZ() - this.boss.getZ(), livingentity.getX() - this.boss.getX());
            switch (this.chargeTime) {
               case 20:
                  double distance = 11.5;
                  int amount = 28;

                  for (int k = 0; k < amount; k++) {
                     float f2 = f + k * (float) Math.PI * 2.0F / amount + 9.05F;
                     this.boss
                        .spawnRoots(
                           this.boss.getX() + Mth.cos(f2) * distance + (world.random.nextInt(2) - 1),
                           this.boss.getZ() + Mth.sin(f2) * distance + (world.random.nextInt(2) - 1),
                           d0,
                           d1,
                           f2,
                           3
                        );
                  }
                  break;
               case 40:
                  double distance = 9.5;
                  int amount = 24;

                  for (int k = 0; k < amount; k++) {
                     float f2 = f + k * (float) Math.PI * 2.0F / amount + 9.05F;
                     this.boss
                        .spawnRoots(
                           this.boss.getX() + Mth.cos(f2) * distance + (world.random.nextInt(2) - 1),
                           this.boss.getZ() + Mth.sin(f2) * distance + (world.random.nextInt(2) - 1),
                           d0,
                           d1,
                           f2,
                           3
                        );
                  }
                  break;
               case 60:
                  double distance = 7.5;
                  int amount = 20;

                  for (int k = 0; k < amount; k++) {
                     float f2 = f + k * (float) Math.PI * 2.0F / amount + 9.05F;
                     this.boss
                        .spawnRoots(
                           this.boss.getX() + Mth.cos(f2) * distance + (world.random.nextInt(2) - 1),
                           this.boss.getZ() + Mth.sin(f2) * distance + (world.random.nextInt(2) - 1),
                           d0,
                           d1,
                           f2,
                           3
                        );
                  }
                  break;
               case 80:
                  double distance = 5.5;
                  int amount = 16;

                  for (int k = 0; k < amount; k++) {
                     float f2 = f + k * (float) Math.PI * 2.0F / amount + 9.05F;
                     this.boss
                        .spawnRoots(
                           this.boss.getX() + Mth.cos(f2) * distance + (world.random.nextInt(2) - 1),
                           this.boss.getZ() + Mth.sin(f2) * distance + (world.random.nextInt(2) - 1),
                           d0,
                           d1,
                           f2,
                           3
                        );
                  }
            }

            if (this.chargeTime == 1) {
               this.boss.rootWaveCooldown = 40;
            }
         }
      }
   }
}

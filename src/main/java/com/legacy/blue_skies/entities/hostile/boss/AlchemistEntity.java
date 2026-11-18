package com.legacy.blue_skies.entities.hostile.boss;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.legacy.blue_skies.blocks.dungeon.SkyDecayingSpikeBlock;
import com.legacy.blue_skies.blocks.misc.TrophyBlock;
import com.legacy.blue_skies.entities.hostile.boss.summons.DecayingSpikeEntity;
import com.legacy.blue_skies.entities.projectile.SpearEntity;
import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import com.legacy.blue_skies.entities.util.base.SkiesIllagerBossEntity;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBoss;
import com.legacy.blue_skies.items.LootBagItem;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesParticles;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.structure_gel.api.util.Positions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;

public class AlchemistEntity extends SkiesIllagerBossEntity {
   private static final Set<MobEffect> IMMUNE_EFFECTS = ImmutableSet.of(
      MobEffects.POISON, MobEffects.HARM, MobEffects.WITHER, MobEffects.LEVITATION, MobEffects.MOVEMENT_SLOWDOWN
   );
   protected List<BlockPos> fluidConversionList = new ArrayList<>();
   protected boolean isConvertingToWater = true;

   public AlchemistEntity(EntityType<? extends AlchemistEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector2.addSkyGoal(0, new FloatGoal(this));
      this.goalSelector2.addSkyGoal(2, new AvoidEntityGoal(this, Player.class, 8.0F, 0.6, 1.0));
      this.goalSelector2.addSkyGoal(4, new AlchemistEntity.RegenGoal());
      this.goalSelector2.addSkyGoal(4, new AlchemistEntity.LiquidConversionGoal());
      this.goalSelector2.addSkyGoal(5, new AlchemistEntity.FireArrowBundleGoal());
      this.goalSelector2.addSkyGoal(5, new AlchemistEntity.DecayingSpikeGoal());
      this.goalSelector2.addSkyGoal(5, new AlchemistEntity.PotionCloudGoal());
      this.goalSelector2.addSkyGoal(5, new AlchemistEntity.BlindlessGoal());
      this.goalSelector2.addSkyGoal(8, new RandomStrollGoal(this, 0.6));
      this.goalSelector2.addSkyGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
      this.goalSelector2.addSkyGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
      this.goalSelector2.addSkyGoal(1, new HurtByTargetGoal(this, new Class[]{Raider.class}).setAlertOthers(new Class[0]));
      this.goalSelector2.addSkyGoal(2, new NearestAttackableTargetGoal(this, Player.class, true).setUnseenMemoryTicks(300));
      this.goalSelector2.addSkyGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, false).setUnseenMemoryTicks(300));
      this.goalSelector2.addSkyGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, false));
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 500.0).add(Attributes.FOLLOW_RANGE, 30.0).add(Attributes.MOVEMENT_SPEED, 0.5);
   }

   @Override
   public double getBossHealth() {
      return 500.0 + this.getExtraPlayers() * 150.0;
   }

   @Override
   public float getHealAmount() {
      return 40.0F;
   }

   @Override
   public LootBagItem getLootBag() {
      return SkiesItems.loot_bag_alchemist;
   }

   @Override
   public TrophyBlock getTrophy() {
      return SkiesBlocks.alchemist_trophy;
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag
   ) {
      return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
   }

   @Override
   public Pair<DyeColor, DyeColor> getFireworkColors() {
      return Pair.of(DyeColor.RED, DyeColor.YELLOW);
   }

   public boolean canBeAffected(MobEffectInstance effect) {
      return IMMUNE_EFFECTS.contains(effect.getEffect()) ? false : super.canBeAffected(effect);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (!(source.getDirectEntity() instanceof AreaEffectCloud) && !(source.getDirectEntity() instanceof ThrownPotion)) {
         return source.getDirectEntity() instanceof AbstractArrow arrow
               && !(source.getDirectEntity() instanceof ThrownTrident)
               && !(source.getDirectEntity() instanceof SpearEntity)
            ? false
            : super.hurt(source, amount);
      } else {
         return false;
      }
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void healthParticles() {
      if (this.secondPhaseHealth()) {
         double dx = this.random.nextGaussian() * 0.02;
         double dy = this.random.nextGaussian() * 0.02;
         double dz = this.random.nextGaussian() * 0.02;
         this.level()
            .addParticle(
               SkiesParticles.BLACK_FLAME,
               this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
               this.getY() + this.random.nextFloat() * this.getBbHeight(),
               this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
               dx,
               dy,
               dz
            );
      } else {
         this.spawnParticles(ParticleTypes.END_ROD);
      }
   }

   @Override
   public void tick() {
      super.tick();
   }

   @Override
   public void invulnerableTick() {
      if (this.level().isClientSide()) {
         double d0 = 0.05;
         this.level().addParticle(ParticleTypes.END_ROD, true, this.getX(), this.getY(), this.getZ(), 0.0, 0.5, 0.0);
         this.level().addParticle(ParticleTypes.END_ROD, true, this.getX(), this.getY() + 4.0, this.getZ(), d0, -0.4, d0);
         this.level().addParticle(ParticleTypes.END_ROD, true, this.getX(), this.getY() + 4.0, this.getZ(), d0, -0.4, -d0);
         this.level().addParticle(ParticleTypes.END_ROD, true, this.getX(), this.getY() + 4.0, this.getZ(), -d0, -0.4, d0);
         this.level().addParticle(ParticleTypes.END_ROD, true, this.getX(), this.getY() + 4.0, this.getZ(), -d0, -0.4, -d0);
      }
   }

   public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
      for (int i = 0; i < 3; i++) {
         Arrow entityarrow = this.createArrowEntity(distanceFactor);
         double d0 = target.getX() - this.getX();
         double d1 = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - entityarrow.getY();
         double d2 = target.getZ() - this.getZ();
         double d3 = Mth.sqrt((float)(d0 * d0 + d2 * d2));
         entityarrow.shoot(d0, d1 + d3 * 0.2F, d2 + (4 * i - 4), 1.6F, 14 - this.getDifficultyID() * 4);
         this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
         this.level().addFreshEntity(entityarrow);
      }

      if (this.secondPhaseHealth()) {
         for (int i = 0; i < 3; i++) {
            Arrow entityarrow = this.createArrowEntity(distanceFactor);
            entityarrow.setSecondsOnFire(7);
            double d0 = target.getX() - this.getX();
            double d1 = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - entityarrow.getY() - 1.0;
            double d2 = target.getZ() - this.getZ();
            double d3 = Mth.sqrt((float)(d0 * d0 + d2 * d2));
            entityarrow.shoot(d0, d1 + d3 * 0.2F, d2 + (4 * i - 4), 1.6F, 14 - this.getDifficultyID() * 4);
            this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(entityarrow);
         }
      }
   }

   protected Arrow createArrowEntity(float distanceFactor) {
      Arrow arrow = new Arrow(this.level(), this, Items.ARROW.getDefaultInstance());
      arrow.setEnchantmentEffectsFromEntity(this, distanceFactor);
      arrow.setBaseDamage(3.0);
      if (this.secondPhaseHealth()) {
         if (this.getDifficultyID() >= ISkyBoss.BossLevel.GOLD.getId() && this.random.nextInt(5) == 0) {
            arrow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
         }

         arrow.setBaseDamage(5.0);
      } else {
         arrow.setBaseDamage(3.0);
      }

      arrow.setKnockback(1);
      return arrow;
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_ALCHEMIST_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_ALCHEMIST_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_ALCHEMIST_DEATH;
   }

   protected float getSoundVolume() {
      return 1.5F;
   }

   public float getVoicePitch() {
      return (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.0F;
   }

   @Override
   public BlockState getFloorState() {
      return SkiesBlocks.lunar_stonebrick.defaultBlockState();
   }

   public SoundEvent getCelebrateSound() {
      return SkiesSounds.ENTITY_ALCHEMIST_CELEBRATE;
   }

   protected SoundEvent getCastingSoundEvent() {
      return SkiesSounds.ENTITY_ALCHEMIST_CAST_SPELL;
   }

   @Override
   public SkiesDungeonType getBossType() {
      return SkiesDungeonType.BLINDING;
   }

   @Override
   public int getProgression() {
      return 1;
   }

   @Override
   public boolean isEverbrightBoss() {
      return false;
   }

   @Override
   public BossBarColor getBossBarColor() {
      return BossBarColor.RED;
   }

   public class BlindlessGoal extends SkiesIllagerBossEntity.UseBossSpellGoal {
      private AlchemistEntity boss = AlchemistEntity.this;

      @Override
      public boolean canUse() {
         return super.canUse() && this.boss.isAwayFromPlayers() && !this.boss.isBossInvul() && this.boss.random.nextBoolean();
      }

      public void stop() {
         super.stop();
      }

      protected void performSpellCasting() {
         for (Player players : this.boss.getPlayersInDungeonArea()) {
            if (!players.isCreative() && !players.isSpectator()) {
               players.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
            }
         }
      }

      protected int getCastWarmupTime() {
         return 20;
      }

      protected int getCastingTime() {
         return 60;
      }

      protected int getCastingInterval() {
         return 300;
      }

      protected SoundEvent getSpellPrepareSound() {
         return SkiesSounds.ENTITY_ALCHEMIST_PREPARE_BLINDNESS;
      }

      @Override
      protected SkiesIllagerBossEntity.BossSpellColor getSpellColor() {
         return SkiesIllagerBossEntity.BossSpellColor.BLINDNESS;
      }
   }

   class DecayingSpikeGoal extends SkiesIllagerBossEntity.UseBossSpellGoal {
      private AlchemistEntity boss = AlchemistEntity.this;

      private DecayingSpikeGoal() {
      }

      protected int getCastWarmupTime() {
         return this.boss.getDifficultyID() == ISkyBoss.BossLevel.GOLD.getId()
            ? 15
            : (this.boss.getDifficultyID() >= ISkyBoss.BossLevel.PLATINUM.getId() ? 10 : 20);
      }

      protected int getCastingTime() {
         return 60;
      }

      protected int getCastingInterval() {
         return 270;
      }

      @Override
      public boolean canUse() {
         return super.canUse() && this.boss.isAwayFromPlayers() && !this.boss.isBossInvul();
      }

      @Override
      public void start() {
         super.start();
      }

      protected void performSpellCasting() {
         LivingEntity target = this.boss.getTarget();
         double d0 = Math.min(target.getY(), this.boss.getY());
         double d1 = Math.max(target.getY(), this.boss.getY()) + 1.0;
         float f = (float)Mth.atan2(target.getZ() - this.boss.getZ(), target.getX() - this.boss.getX());
         if (this.boss.distanceTo(target) < 5.0
            || this.boss.level().getNearestPlayer(this.boss.getX(), this.boss.getY(), this.boss.getZ(), 5.0, true) != null
               && this.boss.distanceTo(this.boss.level().getNearestPlayer(this.boss.getX(), this.boss.getY(), this.boss.getZ(), 5.0, true)) < 5.0) {
            for (int k = 0; k < 8; k++) {
               float f2 = f + k * (float) Math.PI * 2.0F / 8.0F + (float) (Math.PI * 2.0 / 5.0);
               this.playSpikeParticles(
                  this.boss.getX() + Mth.cos(f2) * 2.0,
                  this.boss.getY() + 0.2,
                  this.boss.getZ() + Mth.sin(f2) * 2.0,
                  this.boss.getBbWidth() / 4.0F,
                  this.boss.getBbHeight() / 4.0F,
                  this.boss.getBbWidth() / 4.0F
               );
               this.spawnSpikeCircle(this.boss.getX() + Mth.cos(f2) * 2.0, this.boss.getZ() + Mth.sin(f2) * 2.0, d0, d1);
            }
         } else {
            for (int l = 0; l < 16; l++) {
               double d2 = 1.25 * (l + 1);
               this.playSpikeParticles(
                  this.boss.getX() + Mth.cos(f) * d2,
                  this.boss.getY() + 5.0,
                  this.boss.getZ() + Mth.sin(f) * d2,
                  this.boss.getBbWidth() / 4.0F,
                  this.boss.getBbHeight() / 4.0F,
                  this.boss.getBbWidth() / 4.0F
               );
               this.spawnSpikeLine(this.boss.getX() + Mth.cos(f) * d2, this.boss.getZ() + Mth.sin(f) * d2, d0, d1);
            }
         }
      }

      private void spawnSpikeLine(double posX, double posZ, double maxY, double posY) {
         BlockPos blockpos = Positions.blockPos(posX, posY, posZ);
         boolean flag = false;
         double d0 = 0.0;

         do {
            if (this.boss.level().isEmptyBlock(blockpos) && !this.boss.level().isEmptyBlock(blockpos.below())) {
               if (!this.boss.level().isEmptyBlock(blockpos)) {
                  BlockState iblockstate = this.boss.level().getBlockState(blockpos);
                  VoxelShape voxelshape = iblockstate.getCollisionShape(this.boss.level(), blockpos);
                  if (voxelshape != null) {
                     d0 = voxelshape.max(Axis.Y);
                  }
               }

               flag = true;
               break;
            }

            blockpos = blockpos.below();
         } while (blockpos.getY() >= Mth.floor(maxY) - 1);

         if (flag) {
            BlockPos blockpos2 = Positions.blockPos(posX, blockpos.getY() + d0 + 5.0, posZ);
            DecayingSpikeEntity block = new DecayingSpikeEntity(this.boss.level(), blockpos2, SkiesBlocks.decaying_spike.defaultBlockState(), this.boss);
            block.setDownwards(true);
            block.time = 200;
            block.dropItem = false;
            this.boss.level().addFreshEntity(block);
         }
      }

      private void spawnSpikeCircle(double posX, double posZ, double maxY, double posY) {
         BlockPos blockpos = Positions.blockPos(posX, posY, posZ);
         boolean flag = false;
         double d0 = 0.0;

         do {
            if (this.boss.level().isEmptyBlock(blockpos) && !this.boss.level().isEmptyBlock(blockpos.below())) {
               if (!this.boss.level().isEmptyBlock(blockpos)) {
                  BlockState iblockstate = this.boss.level().getBlockState(blockpos);
                  VoxelShape voxelshape = iblockstate.getCollisionShape(this.boss.level(), blockpos);
                  if (voxelshape != null) {
                     d0 = voxelshape.max(Axis.Y);
                  }
               }

               flag = true;
               break;
            }

            blockpos = blockpos.below();
         } while (blockpos.getY() >= Mth.floor(maxY) - 1);

         if (flag) {
            BlockPos blockpos2 = Positions.blockPos(blockpos.getX() + d0, blockpos.getY() + d0, blockpos.getZ() + d0);
            DecayingSpikeEntity block = new DecayingSpikeEntity(
               this.boss.level(),
               blockpos2,
               (BlockState)SkiesBlocks.decaying_spike.defaultBlockState().setValue(SkyDecayingSpikeBlock.DOWNWARD, false),
               this.boss
            );
            block.setDownwards(false);
            block.setPos(blockpos2.getX() + 0.5, blockpos2.getY(), blockpos2.getZ() + 0.5);
            block.time = 200;
            block.setDeltaMovement(0.0, 0.7, 0.0);
            block.dropItem = false;
            this.boss.level().addFreshEntity(block);
         }
      }

      @Override
      public void tick() {
         super.tick();
         LivingEntity target = this.boss.getTarget();
         if (target != null && this.boss.tickCount % 5 == 0) {
            float f = (float)Mth.atan2(target.getZ() - this.boss.getZ(), target.getX() - this.boss.getX());
            if (this.boss.distanceTo(target) < 5.0
               || this.boss.level().getNearestPlayer(this.boss.getX(), this.boss.getY(), this.boss.getZ(), 5.0, true) != null
                  && this.boss.distanceTo(this.boss.level().getNearestPlayer(this.boss.getX(), this.boss.getY(), this.boss.getZ(), 5.0, true)) < 5.0) {
               for (int k = 0; k < 8; k++) {
                  float f2 = f + k * (float) Math.PI * 2.0F / 8.0F + (float) (Math.PI * 2.0 / 5.0);
                  this.playSpikeParticles(
                     this.boss.getX() + Mth.cos(f2) * 2.0,
                     this.boss.getY() + 0.2,
                     this.boss.getZ() + Mth.sin(f2) * 2.0,
                     this.boss.getBbWidth() / 4.0F,
                     this.boss.getBbHeight() / 4.0F,
                     this.boss.getBbWidth() / 4.0F
                  );
               }
            } else {
               for (int l = 0; l < 16; l++) {
                  double d2 = 1.25 * (l + 1);
                  this.playSpikeParticles(
                     this.boss.getX() + Mth.cos(f) * d2,
                     this.boss.getY() + 5.0,
                     this.boss.getZ() + Mth.sin(f) * d2,
                     this.boss.getBbWidth() / 4.0F,
                     this.boss.getBbHeight() / 4.0F,
                     this.boss.getBbWidth() / 4.0F
                  );
               }
            }
         }
      }

      private void playSpikeParticles(double posX, double posY, double posZ, double offsetX, double offsetY, double offsetZ) {
         int count = 20;
         if (this.boss.level() instanceof ServerLevel) {
            ((ServerLevel)this.boss.level())
               .sendParticles(
                  new BlockParticleOption(ParticleTypes.BLOCK, SkiesBlocks.decaying_spike.defaultBlockState()),
                  posX,
                  posY,
                  posZ,
                  count,
                  offsetX,
                  offsetY,
                  offsetZ,
                  0.05
               );
         }
      }

      protected SoundEvent getSpellPrepareSound() {
         return SkiesSounds.ENTITY_ALCHEMIST_PREPARE_ROCKS;
      }

      @Override
      protected SkiesIllagerBossEntity.BossSpellColor getSpellColor() {
         return SkiesIllagerBossEntity.BossSpellColor.ROCKS;
      }
   }

   class FireArrowBundleGoal extends SkiesIllagerBossEntity.UseBossSpellGoal {
      private AlchemistEntity boss = AlchemistEntity.this;

      private FireArrowBundleGoal() {
      }

      protected int getCastingTime() {
         return this.boss.getDifficultyID() == ISkyBoss.BossLevel.BRONZE.getId() ? 50 : 40;
      }

      protected int getCastingInterval() {
         return 200;
      }

      protected int getCastWarmupTime() {
         return this.boss.getDifficultyID() == ISkyBoss.BossLevel.BRONZE.getId() ? 25 : 20;
      }

      @Override
      public boolean canUse() {
         return super.canUse() && this.boss.isAwayFromPlayers() && !this.boss.isBossInvul();
      }

      protected void performSpellCasting() {
         LivingEntity entitylivingbase = this.boss.getTarget();
         if (entitylivingbase != null) {
            this.boss.attackEntityWithRangedAttack(entitylivingbase, BowItem.getPowerForTime(40));
         }
      }

      protected SoundEvent getSpellPrepareSound() {
         return SkiesSounds.ENTITY_ALCHEMIST_PREPARE_ATTACK;
      }

      @Override
      protected SkiesIllagerBossEntity.BossSpellColor getSpellColor() {
         return SkiesIllagerBossEntity.BossSpellColor.ARROW_BUNDLE;
      }
   }

   public class LiquidConversionGoal extends SkiesIllagerBossEntity.UseBossSpellGoal {
      private AlchemistEntity boss = AlchemistEntity.this;
      public boolean shouldPlaceWater;
      private int waterAmount = 0;
      private int lavaAmount = 0;

      @Override
      public boolean canUse() {
         return this.boss.secondPhaseHealth()
            && this.boss.isAwayFromPlayers()
            && !this.boss.isBossInvul()
            && super.canUse()
            && (this.boss.random.nextBoolean() || this.boss.getTarget() != null && this.boss.getTarget().getRemainingFireTicks() > 0);
      }

      @Override
      public void start() {
         super.start();
         byte radius = 15;
         BlockPos pos = this.boss.getHome();
         BlockPos min = pos.offset(-radius, -radius, -radius);
         BlockPos max = pos.offset(radius, radius, radius);

         for (BlockPos posAround : BlockPos.betweenClosed(min, max)) {
            BlockState state = this.boss.level().getBlockState(posAround);
            if (state.getBlock() == Blocks.OBSIDIAN
               || (state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.WATER) && (Integer)state.getValue(LiquidBlock.LEVEL) == 0) {
               this.boss.fluidConversionList.add(posAround.immutable());
               if (state.getBlock() == Blocks.LAVA) {
                  this.lavaAmount++;
               } else if (state.getBlock() == Blocks.WATER) {
                  this.waterAmount++;
               }
            }
         }

         if (this.waterAmount > this.lavaAmount) {
            this.boss.setBossSpellType(SkiesIllagerBossEntity.BossSpellColor.LAVA);
         } else {
            this.boss.setBossSpellType(SkiesIllagerBossEntity.BossSpellColor.WATER);
         }
      }

      public void stop() {
         super.stop();
         this.waterAmount = 0;
         this.lavaAmount = 0;
         this.boss.fluidConversionList.clear();
      }

      protected void performSpellCasting() {
         if (!this.boss.fluidConversionList.isEmpty()) {
            this.boss.fluidConversionList.forEach(fluidPos -> {
               if (this.waterAmount > this.lavaAmount) {
                  this.boss.level().setBlockAndUpdate(fluidPos, Blocks.LAVA.defaultBlockState());
               } else {
                  this.boss.level().setBlockAndUpdate(fluidPos, Blocks.WATER.defaultBlockState());
               }
            });
            this.waterAmount = 0;
            this.lavaAmount = 0;
            this.boss.fluidConversionList.clear();
         }
      }

      @Override
      public void tick() {
         if (this.attackWarmupDelay == 1) {
            byte radius = 15;
            BlockPos pos = this.boss.getHome();
            BlockPos min = pos.offset(-radius, -radius, -radius);
            BlockPos max = pos.offset(radius, radius, radius);

            for (BlockPos posAround : MutableBlockPos.betweenClosed(min, max)) {
               BlockState state = this.boss.level().getBlockState(posAround);
               if (state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.WATER) {
                  this.boss.level().setBlockAndUpdate(posAround, Blocks.AIR.defaultBlockState());
               }
            }
         }

         super.tick();
      }

      protected int getCastWarmupTime() {
         return 30;
      }

      protected int getCastingTime() {
         return this.boss.getDifficultyID() == ISkyBoss.BossLevel.BRONZE.getId() ? 70 : 60;
      }

      protected int getCastingInterval() {
         return 90;
      }

      protected SoundEvent getSpellPrepareSound() {
         return SkiesSounds.ENTITY_ALCHEMIST_PREPARE_CONVERSION;
      }

      @Override
      protected SkiesIllagerBossEntity.BossSpellColor getSpellColor() {
         return this.waterAmount > this.lavaAmount ? SkiesIllagerBossEntity.BossSpellColor.LAVA : SkiesIllagerBossEntity.BossSpellColor.WATER;
      }
   }

   class PotionCloudGoal extends SkiesIllagerBossEntity.UseBossSpellGoal {
      private AlchemistEntity boss = AlchemistEntity.this;

      private PotionCloudGoal() {
      }

      @Override
      public boolean canUse() {
         return super.canUse() && AlchemistEntity.this.isAwayFromPlayers() && AlchemistEntity.this.getInvulTime() <= 0 && this.boss.random.nextBoolean();
      }

      protected int getCastingTime() {
         return this.boss.getDifficultyID() == ISkyBoss.BossLevel.BRONZE.getId() ? 85 : 70;
      }

      protected int getCastingInterval() {
         return 300;
      }

      protected void performSpellCasting() {
         if (this.boss.getTarget() != null) {
            this.spawnClouds();
         }
      }

      private void spawnClouds() {
         BlockPos pos = this.boss.isDungeonSpawned() ? this.boss.getHome() : this.boss.blockPosition();
         AreaEffectCloud potionCloudPX = new AreaEffectCloud(this.boss.level(), pos.getX() + 5, pos.getY(), pos.getZ() + 5);
         AreaEffectCloud potionCloudPZ = new AreaEffectCloud(this.boss.level(), pos.getX() + 5, pos.getY(), pos.getZ() - 5);
         AreaEffectCloud potionCloudNX = new AreaEffectCloud(this.boss.level(), pos.getX() - 5, pos.getY(), pos.getZ() + 5);
         AreaEffectCloud potionCloudNZ = new AreaEffectCloud(this.boss.level(), pos.getX() - 5, pos.getY(), pos.getZ() - 5);
         Potion potiontype = this.boss.secondPhaseHealth() ? Potions.HARMING : Potions.POISON;
         List<AreaEffectCloud> clouds = ImmutableList.of(potionCloudPX, potionCloudPZ, potionCloudNX, potionCloudNZ);
         clouds.forEach(cloud -> {
            cloud.setPotion(potiontype);
            cloud.setDuration(200 + (this.boss.getDifficultyID() > ISkyBoss.BossLevel.SILVER.getId() ? this.boss.getDifficultyID() * 40 : 0));
            cloud.setOwner(this.boss);
            this.boss.level().addFreshEntity(cloud);
         });
      }

      protected SoundEvent getSpellPrepareSound() {
         return SkiesSounds.ENTITY_ALCHEMIST_PREPARE_POTIONS;
      }

      @Override
      protected SkiesIllagerBossEntity.BossSpellColor getSpellColor() {
         return this.boss.secondPhaseHealth() ? SkiesIllagerBossEntity.BossSpellColor.HARMING_CLOUD : SkiesIllagerBossEntity.BossSpellColor.POISON_CLOUD;
      }
   }

   public class RegenGoal extends SkiesIllagerBossEntity.UseBossSpellGoal {
      private AlchemistEntity boss = AlchemistEntity.this;

      @Override
      public boolean canUse() {
         return super.canUse() && this.boss.isAwayFromPlayers() && this.boss.secondPhaseHealth() && !this.boss.isBossInvul();
      }

      public void stop() {
         super.stop();
      }

      protected void performSpellCasting() {
         int extraPlayers = Math.min(4, this.boss.getExtraPlayers());
         this.boss
            .addEffect(
               new MobEffectInstance(
                  MobEffects.REGENERATION, (this.boss.getDifficultyID() == ISkyBoss.BossLevel.BRONZE.getId() ? 18 : 20) + extraPlayers * 5, 5, false, false
               )
            );
      }

      protected int getCastWarmupTime() {
         return 20;
      }

      protected int getCastingTime() {
         return this.boss.getDifficultyID() >= ISkyBoss.BossLevel.GOLD.getId() ? 60 : 70;
      }

      protected int getCastingInterval() {
         int extraPlayers = Math.min(7, this.boss.getExtraPlayers());
         return 360 + extraPlayers * 20;
      }

      protected SoundEvent getSpellPrepareSound() {
         return SkiesSounds.ENTITY_ALCHEMIST_PREPARE_REGEN;
      }

      @Override
      protected SkiesIllagerBossEntity.BossSpellColor getSpellColor() {
         return SkiesIllagerBossEntity.BossSpellColor.REGENERATION;
      }
   }
}

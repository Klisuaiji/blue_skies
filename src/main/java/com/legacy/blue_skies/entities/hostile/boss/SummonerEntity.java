package com.legacy.blue_skies.entities.hostile.boss;

import com.google.common.collect.Lists;
import com.legacy.blue_skies.blocks.misc.TrophyBlock;
import com.legacy.blue_skies.entities.hostile.boss.summons.ArtificialGolemEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.StrangeLightningEntity;
import com.legacy.blue_skies.entities.projectile.FluctuantSphereEntity;
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
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;

public class SummonerEntity extends SkiesIllagerBossEntity {
   public static final Predicate<ArtificialGolemEntity> NOT_RANGED = golem -> !golem.isRanged() && !golem.playerSpawned();
   public static final Predicate<ArtificialGolemEntity> IS_RANGED = golem -> golem.isRanged() && !golem.playerSpawned();
   public boolean golemsChanged = false;

   public SummonerEntity(EntityType<? extends SummonerEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector2.addSkyGoal(0, new FloatGoal(this));
      this.goalSelector2.addSkyGoal(2, new AvoidEntityGoal(this, Player.class, 8.0F, 0.6, 1.0));
      this.goalSelector2.addSkyGoal(4, new SummonerEntity.SummonRangerGoal());
      this.goalSelector2.addSkyGoal(5, new SummonerEntity.SummonGolemGoal());
      this.goalSelector2.addSkyGoal(5, new SummonerEntity.SphereAttackGoal());
      this.goalSelector2.addSkyGoal(6, new SummonerEntity.RegenGoal());
      this.goalSelector2.addSkyGoal(8, new RandomStrollGoal(this, 0.6));
      this.goalSelector2.addSkyGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
      this.goalSelector2.addSkyGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
      this.goalSelector2.addSkyGoal(1, new HurtByTargetGoal(this, new Class[]{Raider.class}).setAlertOthers(new Class[0]));
      this.goalSelector2.addSkyGoal(2, new NearestAttackableTargetGoal(this, Player.class, true).setUnseenMemoryTicks(300));
      this.goalSelector2.addSkyGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, false).setUnseenMemoryTicks(300));
      this.goalSelector2.addSkyGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, false));
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 350.0).add(Attributes.FOLLOW_RANGE, 30.0).add(Attributes.MOVEMENT_SPEED, 0.5);
   }

   @Override
   public double getBossHealth() {
      return 350.0 + this.getExtraPlayers() * 200.0;
   }

   @Override
   public float getHealAmount() {
      return 40.0F;
   }

   @Override
   public int getDamageCap() {
      return 15;
   }

   @Override
   public LootBagItem getLootBag() {
      return SkiesItems.loot_bag_summoner;
   }

   @Override
   public TrophyBlock getTrophy() {
      return SkiesBlocks.summoner_trophy;
   }

   public boolean canBeAffected(MobEffectInstance effect) {
      return effect.getEffect() != MobEffects.LEVITATION && effect.getEffect() != MobEffects.MOVEMENT_SLOWDOWN ? super.canBeAffected(effect) : false;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void healthParticles() {
      if (this.secondPhaseHealth()) {
         this.spawnParticles(SkiesParticles.BLUE_FLAME);
      } else {
         this.spawnParticles(ParticleTypes.FIREWORK);
      }
   }

   @Override
   public void tick() {
      super.tick();
      if (this.secondPhaseHealth() && !this.golemsChanged) {
         for (ArtificialGolemEntity golems : this.getGolemsInDungeonArea()) {
            golems.playSound(SkiesSounds.ENTITY_ARTIFICIAL_GOLEM_ACTIVATE, 0.7F, 1.0F);
            golems.setEnraged(true);
         }

         this.golemsChanged = true;
      } else if (!this.secondPhaseHealth() && this.golemsChanged) {
         for (ArtificialGolemEntity golems : this.getGolemsInDungeonArea()) {
            golems.playSound(SkiesSounds.ENTITY_ARTIFICIAL_GOLEM_DEACTIVATE, 0.7F, 1.0F);
            golems.setEnraged(false);
         }

         this.golemsChanged = false;
      }
   }

   @Override
   public Pair<DyeColor, DyeColor> getFireworkColors() {
      return Pair.of(DyeColor.PURPLE, DyeColor.LIME);
   }

   @Override
   public void invulnerableTick() {
      if (this.level().isClientSide) {
         double d0 = 0.05;
         this.level().addParticle(ParticleTypes.FIREWORK, true, this.getX(), this.getY(), this.getZ(), 0.0, 0.5, 0.0);
         this.level().addParticle(ParticleTypes.FIREWORK, true, this.getX(), this.getY() + 4.0, this.getZ(), d0, -0.4, d0);
         this.level().addParticle(ParticleTypes.FIREWORK, true, this.getX(), this.getY() + 4.0, this.getZ(), d0, -0.4, -d0);
         this.level().addParticle(ParticleTypes.FIREWORK, true, this.getX(), this.getY() + 4.0, this.getZ(), -d0, -0.4, d0);
         this.level().addParticle(ParticleTypes.FIREWORK, true, this.getX(), this.getY() + 4.0, this.getZ(), -d0, -0.4, -d0);
      }
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_SUMMONER_IDLE;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_SUMMONER_DEATH;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_SUMMONER_HURT;
   }

   public float getVoicePitch() {
      return (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 0.9F;
   }

   protected float getSoundVolume() {
      return 1.5F;
   }

   @Override
   public BlockState getFloorState() {
      return SkiesBlocks.turquoise_stonebrick.defaultBlockState();
   }

   @Override
   public BossBarColor getBossBarColor() {
      return BossBarColor.PURPLE;
   }

   public SoundEvent getCelebrateSound() {
      return SkiesSounds.ENTITY_SUMMONER_CELEBRATE;
   }

   protected SoundEvent getCastingSoundEvent() {
      return SkiesSounds.ENTITY_SUMMONER_CAST_SPELL;
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
      return true;
   }

   public List<ArtificialGolemEntity> getGolemsInDungeonArea() {
      return this.getHome() == null
         ? Lists.newArrayList()
         : this.level().getEntitiesOfClass(ArtificialGolemEntity.class, new AABB(this.getHome()).inflate(35.0, 15.0, 35.0), NOT_RANGED);
   }

   public List<ArtificialGolemEntity> getRangedGolemsInDungeonArea() {
      return this.getHome() == null
         ? Lists.newArrayList()
         : this.level().getEntitiesOfClass(ArtificialGolemEntity.class, new AABB(this.getHome()).inflate(35.0, 15.0, 35.0), IS_RANGED);
   }

   public int getMaxGolemsBasedOnPlayerCount() {
      return (this.getDifficultyID() >= ISkyBoss.BossLevel.GOLD.getId() ? 4 : 3) + Math.min(4, this.getExtraPlayers());
   }

   public class RegenGoal extends SkiesIllagerBossEntity.UseBossSpellGoal {
      private SummonerEntity boss = SummonerEntity.this;

      @Override
      public boolean canUse() {
         return super.canUse() && this.boss.isAwayFromPlayers() && !this.boss.isBossInvul() && this.boss.secondPhaseHealth();
      }

      public void stop() {
         super.stop();
      }

      protected void performSpellCasting() {
         int extraPlayers = Math.min(4, this.boss.getExtraPlayers());
         this.boss
            .addEffect(
               new MobEffectInstance(
                  MobEffects.REGENERATION,
                  (
                        this.boss.getDifficultyID() == ISkyBoss.BossLevel.BRONZE.getId()
                           ? 10
                           : (
                              this.boss.getDifficultyID() >= ISkyBoss.BossLevel.PLATINUM.getId()
                                 ? 23
                                 : (this.boss.getDifficultyID() >= ISkyBoss.BossLevel.GOLD.getId() ? 21 : 20)
                           )
                     )
                     + extraPlayers * 5,
                  5,
                  false,
                  false
               )
            );
      }

      protected int getCastWarmupTime() {
         return 20;
      }

      protected int getCastingTime() {
         return 70;
      }

      protected int getCastingInterval() {
         int extraPlayers = Math.min(7, this.boss.getExtraPlayers());
         return 340 + extraPlayers * 10;
      }

      protected SoundEvent getSpellPrepareSound() {
         return SkiesSounds.ENTITY_SUMMONER_PREPARE_REGEN;
      }

      @Override
      protected SkiesIllagerBossEntity.BossSpellColor getSpellColor() {
         return SkiesIllagerBossEntity.BossSpellColor.REGENERATION;
      }
   }

   class SphereAttackGoal extends SkiesIllagerBossEntity.UseBossSpellGoal {
      private SummonerEntity boss = SummonerEntity.this;

      private SphereAttackGoal() {
      }

      @Override
      public boolean canUse() {
         return super.canUse() && this.boss.isAwayFromPlayers() && !this.boss.isBossInvul();
      }

      protected int getCastingTime() {
         return this.boss.getDifficultyID() >= ISkyBoss.BossLevel.GOLD.getId() ? 50 : 55;
      }

      protected int getCastingInterval() {
         return 100;
      }

      protected void performSpellCasting() {
         if (this.boss.getTarget() != null && this.boss.getTarget() instanceof LivingEntity) {
            LivingEntity target = this.boss.getTarget();
            double d0 = Math.min(target.getY(), this.boss.getY());
            double d1 = Math.max(target.getY(), this.boss.getY()) + 0.3;
            float f = (float)Mth.atan2(target.getZ() - this.boss.getZ(), target.getX() - this.boss.getX());
            if (this.boss.distanceTo(target) < 5.0
               || this.boss.level().getNearestPlayer(this.boss.getX(), this.boss.getY(), this.boss.getZ(), 5.0, true) != null
                  && this.boss.distanceTo(this.boss.level().getNearestPlayer(this.boss.getX(), this.boss.getY(), this.boss.getZ(), 5.0, true)) < 5.0) {
               for (int k = 0; k < 8; k++) {
                  float f2 = f + k * (float) Math.PI * 2.0F / 8.0F + (float) (Math.PI * 2.0 / 5.0);
                  this.spawnLightning(this.boss.getX() + Mth.cos(f2) * 4.5, this.boss.getZ() + Mth.sin(f2) * 4.5, d0, d1, f2, 3);
               }
            } else {
               double x = this.boss.getTarget().getX() - this.boss.getX();
               double y = this.boss.getTarget().getBoundingBox().minY
                  + this.boss.getTarget().getBbHeight() / 2.0F
                  - (this.boss.getY() + this.boss.getBbHeight() / 2.0F);
               double z = this.boss.getTarget().getZ() - this.boss.getZ();
               this.spawnSphere(x, y, z);
            }
         }
      }

      private void spawnSphere(double posX, double posY, double posZ) {
         LivingEntity target = this.boss.getTarget();
         if (target != null) {
            Vec3 lookVector = this.boss.getViewVector(1.0F);
            FluctuantSphereEntity projectile = new FluctuantSphereEntity(this.boss.level(), this.boss, posX, posY, posZ);
            projectile.setPos(
               this.boss.getX() + lookVector.x * 1.0, this.boss.getY() + this.boss.getBbHeight() / 2.0F + 0.2F, this.boss.getZ() + lookVector.z * 1.0
            );
            projectile.setYRot(this.boss.yHeadRot);
            this.boss.level().addFreshEntity(projectile);
         }
      }

      private void spawnLightning(double posX, double posZ, double p_190876_5_, double posY, float p_190876_9_, int p_190876_10_) {
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
         } while (blockpos.getY() >= Mth.floor(p_190876_5_) - 1);

         if (flag) {
            StrangeLightningEntity bolt = new StrangeLightningEntity(this.boss.level());
            bolt.moveTo(posX, blockpos.getY() + d0, posZ, 0.0F, 0.0F);
            this.boss.level().addFreshEntity(bolt);
         }
      }

      protected SoundEvent getSpellPrepareSound() {
         return SkiesSounds.ENTITY_SUMMONER_PREPARE_ATTACK;
      }

      @Override
      protected SkiesIllagerBossEntity.BossSpellColor getSpellColor() {
         return SkiesIllagerBossEntity.BossSpellColor.SPHERE;
      }
   }

   class SummonGolemGoal extends SkiesIllagerBossEntity.UseBossSpellGoal {
      private SummonerEntity boss = SummonerEntity.this;

      private SummonGolemGoal() {
      }

      @Override
      public boolean canUse() {
         int i = this.boss.getGolemsInDungeonArea().size();
         return i >= this.boss.getMaxGolemsBasedOnPlayerCount()
            ? false
            : super.canUse() && this.boss.isAwayFromPlayers() && !this.boss.isBossInvul() && this.boss.random.nextInt(8) + 1 > i;
      }

      protected int getCastingTime() {
         return 100;
      }

      protected int getCastingInterval() {
         return 340;
      }

      protected void performSpellCasting() {
         if (this.boss.level() instanceof ServerLevelAccessor) {
            List<Player> targets = new ArrayList<>();
            targets.addAll(this.boss.getPlayersInDungeonArea());
            int maxGolems = this.boss.getMaxGolemsBasedOnPlayerCount() - this.boss.getGolemsInDungeonArea().size();

            for (int i = 0; i < maxGolems; i++) {
               BlockPos pos = this.boss.blockPosition();
               if (this.boss.isDungeonSpawned()) {
                  pos = this.boss.getHome();
               }

               BlockPos spawnPos = Positions.blockPos(
                  pos.getX() + -6 + this.boss.random.nextInt(12), pos.getY(), pos.getZ() + -6 + this.boss.random.nextInt(12)
               );
               ArtificialGolemEntity golem = new ArtificialGolemEntity(this.boss.level(), this.boss);
               golem.moveTo(spawnPos, 0.0F, 0.0F);
               golem.finalizeSpawn(
                  (ServerLevelAccessor)this.boss.level(),
                  this.boss.level().getCurrentDifficultyAt(spawnPos),
                  MobSpawnType.MOB_SUMMONED,
                  (SpawnGroupData)null,
                  (CompoundTag)null
               );
               if (this.boss.getDifficultyID() >= ISkyBoss.BossLevel.GOLD.getId()) {
                  golem.getAttribute(Attributes.ARMOR).setBaseValue(4.0);
                  double health = this.boss.getDifficultyID() >= ISkyBoss.BossLevel.PLATINUM.getId() ? 18.0 : 14.0;
                  golem.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
                  golem.setHealth((float)health);
               }

               if (!targets.isEmpty()) {
                  Player player = targets.get(0);
                  golem.setTarget(player);
                  targets.remove(0);
               } else if (!this.boss.getPlayersInDungeonArea().isEmpty()) {
                  golem.setTarget(
                     (LivingEntity)this.boss.getPlayersInDungeonArea().get(this.boss.level().random.nextInt(this.boss.getPlayersInDungeonArea().size()))
                  );
               } else if (this.boss.getTarget() != null) {
                  golem.setTarget(this.boss.getTarget());
               }

               golem.setEnraged(this.boss.secondPhaseHealth());
               this.boss.level().addFreshEntity(golem);
               if (this.boss.level() instanceof ServerLevel) {
                  for (int h = 0; h < 20; h++) {
                     double dx = this.boss.random.nextGaussian() * 0.02;
                     double dy = this.boss.random.nextGaussian() * 0.02;
                     double dz = this.boss.random.nextGaussian() * 0.02;
                     ((ServerLevel)this.boss.level())
                        .sendParticles(
                           ParticleTypes.POOF,
                           golem.getX() + this.boss.random.nextFloat() * golem.getBbWidth() * 2.0F - golem.getBbWidth(),
                           golem.getY() + this.boss.random.nextFloat() * golem.getBbHeight(),
                           golem.getZ() + this.boss.random.nextFloat() * golem.getBbWidth() * 2.0F - golem.getBbWidth(),
                           1,
                           dx,
                           dy,
                           dz,
                           0.0
                        );
                  }
               }
            }
         }
      }

      protected SoundEvent getSpellPrepareSound() {
         return SkiesSounds.ENTITY_SUMMONER_PREPARE_SUMMONING;
      }

      @Override
      protected SkiesIllagerBossEntity.BossSpellColor getSpellColor() {
         return SkiesIllagerBossEntity.BossSpellColor.SUMMON_GOLEM;
      }
   }

   class SummonRangerGoal extends SkiesIllagerBossEntity.UseBossSpellGoal {
      private SummonerEntity boss = SummonerEntity.this;

      private SummonRangerGoal() {
      }

      @Override
      public boolean canUse() {
         List<ArtificialGolemEntity> golems = this.boss.getGolemsInDungeonArea();
         List<ArtificialGolemEntity> archers = this.boss.getRangedGolemsInDungeonArea();
         return super.canUse() && this.boss.isAwayFromPlayers() && !this.boss.isBossInvul() && archers.isEmpty() && golems.size() <= 1;
      }

      protected int getCastingTime() {
         return 100;
      }

      protected int getCastingInterval() {
         return 340;
      }

      protected void performSpellCasting() {
         if (this.boss.level() instanceof ServerLevelAccessor) {
            List<Player> targets = new ArrayList<>();
            targets.addAll(this.boss.getPlayersInDungeonArea());

            for (int i = 0; i < 2; i++) {
               BlockPos pos = this.boss.blockPosition();
               if (this.boss.isDungeonSpawned()) {
                  pos = this.boss.getHome();
               }

               BlockPos spawnPos = Positions.blockPos(
                  pos.getX() + -6 + this.boss.random.nextInt(12), pos.getY(), pos.getZ() + -6 + this.boss.random.nextInt(12)
               );
               ArtificialGolemEntity golem = new ArtificialGolemEntity(this.boss.level(), this.boss);
               golem.moveTo(spawnPos, 0.0F, 0.0F);
               golem.setRanged(true);
               golem.finalizeSpawn(
                  (ServerLevelAccessor)this.boss.level(),
                  this.boss.level().getCurrentDifficultyAt(spawnPos),
                  MobSpawnType.MOB_SUMMONED,
                  (SpawnGroupData)null,
                  (CompoundTag)null
               );
               golem.getAttribute(Attributes.MAX_HEALTH)
                  .setBaseValue(
                     this.boss.getDifficultyID() >= ISkyBoss.BossLevel.PLATINUM.getId()
                        ? 25.0
                        : (this.boss.getDifficultyID() == ISkyBoss.BossLevel.GOLD.getId() ? 20.0 : 10.0)
                  );
               golem.setHealth(golem.getMaxHealth());
               if (this.boss.getTarget() != null) {
                  golem.setTarget(this.boss.getTarget());
               }

               if (!targets.isEmpty()) {
                  Player player = targets.get(0);
                  golem.setTarget(player);
                  targets.remove(0);
               } else if (!this.boss.getPlayersInDungeonArea().isEmpty()) {
                  golem.setTarget(
                     (LivingEntity)this.boss.getPlayersInDungeonArea().get(this.boss.level().random.nextInt(this.boss.getPlayersInDungeonArea().size()))
                  );
               } else if (this.boss.getTarget() != null) {
                  golem.setTarget(this.boss.getTarget());
               }

               golem.setTarget(this.boss.getTarget());
               this.boss.level().addFreshEntity(golem);
               if (this.boss.level() instanceof ServerLevel) {
                  for (int h = 0; h < 20; h++) {
                     double dx = this.boss.random.nextGaussian() * 0.02;
                     double dy = this.boss.random.nextGaussian() * 0.02;
                     double dz = this.boss.random.nextGaussian() * 0.02;
                     ((ServerLevel)this.boss.level())
                        .sendParticles(
                           ParticleTypes.POOF,
                           golem.getX() + this.boss.random.nextFloat() * golem.getBbWidth() * 2.0F - golem.getBbWidth(),
                           golem.getY() + this.boss.random.nextFloat() * golem.getBbHeight(),
                           golem.getZ() + this.boss.random.nextFloat() * golem.getBbWidth() * 2.0F - golem.getBbWidth(),
                           1,
                           dx,
                           dy,
                           dz,
                           0.0
                        );
                  }
               }
            }
         }
      }

      protected SoundEvent getSpellPrepareSound() {
         return SkiesSounds.ENTITY_SUMMONER_PREPARE_SUMMONING;
      }

      @Override
      protected SkiesIllagerBossEntity.BossSpellColor getSpellColor() {
         return SkiesIllagerBossEntity.BossSpellColor.SUMMON_RANGED_GOLEM;
      }
   }
}

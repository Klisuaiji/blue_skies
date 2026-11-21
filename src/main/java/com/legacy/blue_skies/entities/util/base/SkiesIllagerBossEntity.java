package com.legacy.blue_skies.entities.util.base;

import com.legacy.blue_skies.block_entity.KeystoneBlockEntity;
import com.legacy.blue_skies.data.objects.tags.SkiesBlockTags;
import com.legacy.blue_skies.entities.hostile.boss.summons.ArtificialGolemEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.DecayingSpikeEntity;
import com.legacy.blue_skies.entities.util.SkiesGoalSelector;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBoss;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.s_to_c.BossTypePacket;
import com.legacy.blue_skies.network.s_to_c.IllagerTeleportPacket;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.structure_gel.api.util.Positions;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.SpellcasterIllager.IllagerSpell;
import net.minecraft.world.entity.monster.SpellcasterIllager.SpellcasterCastingSpellGoal;
import net.minecraft.world.entity.monster.SpellcasterIllager.SpellcasterUseSpellGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.IExtensibleEnum;
import org.apache.commons.lang3.tuple.Pair;

public abstract class SkiesIllagerBossEntity extends SpellcasterIllager implements ISkyBoss {
   public static final EntityDataAccessor<Boolean> DUNGEON_SPAWNED = SynchedEntityData.defineId(SkiesIllagerBossEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> INVULNERABILITY_TIME = SynchedEntityData.defineId(SkiesIllagerBossEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> BOSS_SPELL_ID = SynchedEntityData.defineId(SkiesIllagerBossEntity.class, EntityDataSerializers.INT);
   protected static final EntityDataAccessor<Boolean> RESPAWNED = SynchedEntityData.defineId(SkiesIllagerBossEntity.class, EntityDataSerializers.BOOLEAN);
   protected static final EntityDataAccessor<Byte> DIFFICULTY_ID = SynchedEntityData.defineId(SkiesIllagerBossEntity.class, EntityDataSerializers.BYTE);
   private SkiesIllagerBossEntity.BossSpellColor activeBossSpell = SkiesIllagerBossEntity.BossSpellColor.NONE;
   private final ServerBossEvent bossInfo = this.getNewBossInfo();
   protected BlockPos homePos;
   private int teleportCooldown;
   protected int bossInvulTicks;
   public boolean noLerp = false;
   private static final Vec3i RANGED_GOLEM_SPELL_COLOR = new Vec3i(155, 225, 156);
   private static final Vec3i GOLEM_SPELL_COLOR = new Vec3i(146, 244, 244);
   private static final Vec3i SPHERE_SPELL_COLOR = new Vec3i(51, 53, 94);
   private static final Vec3i ROCKS_SPELL_COLOR = new Vec3i(183, 119, 255);
   private static final Vec3i ARROW_SPELL_COLOR = new Vec3i(177, 157, 106);
   private static final Vec3i POISON_SPELL_COLOR = new Vec3i(135, 163, 99);
   private static final Vec3i HARMING_SPELL_COLOR = new Vec3i(169, 101, 106);
   private static final Vec3i WATER_SPELL_COLOR = new Vec3i(88, 123, 176);
   private static final Vec3i LAVA_SPELL_COLOR = new Vec3i(216, 118, 33);
   private static final Vec3i BLINDNESS_SPELL_COLOR = new Vec3i(31, 31, 35);
   public static final Vec3i REGEN_SPELL_COLOR = new Vec3i(205, 92, 171);
   public SkiesGoalSelector goalSelector2;
   public SkiesGoalSelector targetSelector2;

   public SkiesIllagerBossEntity(EntityType<? extends SkiesIllagerBossEntity> type, Level worldIn) {
      super(type, worldIn);
      this.xpReward = 30;
   }

   protected void registerGoals() {
      this.goalSelector2 = (SkiesGoalSelector)(this.goalSelector = new SkiesGoalSelector(this.level().getProfilerSupplier()));
      this.targetSelector2 = (SkiesGoalSelector)(this.targetSelector = new SkiesGoalSelector(this.level().getProfilerSupplier()));
      this.goalSelector2.locked = false;
      this.targetSelector2.locked = false;
      super.registerGoals();
      this.goalSelector2.locked = true;
      this.targetSelector2.locked = true;
      this.goalSelector2.addSkyGoal(0, new SkiesIllagerBossEntity.DoNothingGoal());
      this.goalSelector2.addSkyGoal(1, new SkiesIllagerBossEntity.CastingSpellGoal());
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(DUNGEON_SPAWNED, false);
      builder.define(INVULNERABILITY_TIME, 0);
      builder.define(BOSS_SPELL_ID, 0);
      builder.define(RESPAWNED, false);
      builder.define(DIFFICULTY_ID, (byte)1);
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag
   ) {
      SpawnGroupData flag = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
      this.setBossCanJoinRaid(reason == MobSpawnType.SPAWN_EGG || reason == MobSpawnType.COMMAND);
      if (reason == MobSpawnType.TRIGGERED) {
         this.level().destroyBlock(this.blockPosition(), false);
         this.level().destroyBlock(this.blockPosition().above(), false);
      }

      if (!this.level().isClientSide()) {
         this.defaultSpawningRoutine(difficultyIn);
      }

      return flag;
   }

   @Override
   public ServerBossEvent getBossInfo() {
      return this.bossInfo;
   }

   public abstract BlockState getFloorState();

   @OnlyIn(Dist.CLIENT)
   public abstract void healthParticles();

   @Override
   public float getMaxReach() {
      return 3.5F;
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.readDefaultBossNBT(compound);
      this.setBossSpellID(compound.getInt("BossSpellId"));
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      this.writeDefaultBossNBT(compound);
      compound.putInt("BossSpellId", this.getBossSpellID());
   }

   public void move(MoverType type, Vec3 vec) {
      super.move(type, this.getInvulTime() > 0 ? new Vec3(0.0, vec.y, 0.0) : vec);
   }

   public void tick() {
      super.tick();
      this.bossTick();
      if (this.level().isClientSide()) {
         if (this.random.nextDouble() > 0.7) {
            this.healthParticles();
         }

         if (this.isCastingSpell()) {
            float f = this.yBodyRot * (float) (Math.PI / 180.0) + Mth.cos(this.tickCount * 0.6662F) * 0.25F;
            float f1 = Mth.cos(f);
            float f2 = Mth.sin(f);
            SkiesIllagerBossEntity.BossSpellColor type = this.getBossSpellType();
            double d0 = type.colorVec.getX() / 255.0;
            double d1 = type.colorVec.getY() / 255.0;
            double d2 = type.colorVec.getZ() / 255.0;
            SimpleParticleType particle = ParticleTypes.ENTITY_EFFECT;
            this.level().addParticle(particle, this.getX() + f1 * 0.6, this.getY() + 1.8, this.getZ() + f2 * 0.6, d0, d1, d2);
            this.level().addParticle(particle, this.getX() - f1 * 0.6, this.getY() + 1.8, this.getZ() - f2 * 0.6, d0, d1, d2);
         }
      }

      if (!this.level().isClientSide) {
         if (this.isInWater() || this.isInLava()) {
            this.teleportAround();
            this.teleportCooldown = 0;
         }

         if (this.isDungeonSpawned() && !this.isNoAi()) {
            if (this.getHome() != null) {
               this.checkIfHome();
            }

            if (this.getTarget() instanceof Player) {
               Player player = (Player)this.getTarget();
               if (!player.isAlive()) {
                  this.teleportTo(this.getHome().getX(), this.getHome().getY(), this.getHome().getZ());
               }
            }

            this.teleportCooldown++;
         }

         if (this.getTarget() != null) {
            List<Player> playerCount = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(4.0));
            if (playerCount.size() > 0 && this.getTarget() instanceof Player || this.distanceTo(this.getTarget()) <= 5.0) {
               this.teleportAround();
            }
         }
      }
   }

   public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements) {
      if (this.noLerp) {
         this.lerpSteps = 0;
         this.setPos(pX, pY, pZ);
         this.setRot(pYaw, pPitch);
         this.noLerp = false;
      } else {
         super.lerpTo(pX, pY, pZ, pYaw, pPitch, pPosRotationIncrements);
      }
   }

   public void teleportTo(double pX, double pY, double pZ) {
      super.teleportTo(pX, pY, pZ);
   }

   protected void sendNoLerp() {
      this.getPlayersInDungeonArea().forEach(player -> {
         if (player instanceof ServerPlayer sp) {
            PacketHandler.sendToClient(new IllagerTeleportPacket(this.getId()), sp);
         }
      });
   }

   public void die(DamageSource source) {
      super.die(source);
      this.onBossDeath(source);
      if (!this.level().isClientSide && this.isDungeonSpawned() && this.getHome() != null) {
         this.level()
            .setBlock(
               this.getHome().above(),
               this.isEverbrightBoss() ? SkiesBlocks.bright_blinding_keystone.defaultBlockState() : SkiesBlocks.dawn_blinding_keystone.defaultBlockState(),
               2
            );
         this.level().setBlockAndUpdate(this.getHome(), SkiesBlocks.blinding_stone.defaultBlockState());
         BlockEntity keystone = this.level().getBlockEntity(this.getHome().above());
         if (keystone instanceof KeystoneBlockEntity) {
            ((KeystoneBlockEntity)keystone).setTeleportOffset(Positions.blockPos(0L, -9L, 5L));
            ((KeystoneBlockEntity)keystone).setSpawnsBoss(false);
            ((KeystoneBlockEntity)keystone).setKeysRequired(false);
         }
      }

      FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(
         this.level(),
         this.getX(),
         this.getY(),
         this.getZ(),
         EntityUtil.makeFirework((DyeColor)this.getFireworkColors().getLeft(), 0, (DyeColor)this.getFireworkColors().getRight())
      );
      this.level().addFreshEntity(fireworkrocketentity);
   }

   public abstract Pair<DyeColor, DyeColor> getFireworkColors();

   protected void customServerAiStep() {
      if (this.getInvulTime() <= 0) {
         super.customServerAiStep();
         if (this.getTarget() == null && this.isDungeonSpawned() && this.teleportCooldown >= 40 && this.random.nextFloat() * 120.0F < 4.6F) {
            this.teleportAround();
            this.teleportCooldown = 0;
         }
      }
   }

   public boolean hurt(DamageSource source, float amount) {
      if (!this.isInvulnerableTo(source)
         && (this.getInvulTime() <= 0 || source == this.damageSources().fellOutOfWorld())
         && !source.is(DamageTypeTags.IS_LIGHTNING)
         && (!source.is(DamageTypeTags.IS_PROJECTILE) || source.getEntity() != this)) {
         if (!source.is(DamageTypeTags.IS_PROJECTILE)
            || this.isCastingSpell() && !(source.getEntity() instanceof SpellcasterIllager) && !(source.getEntity() instanceof ArtificialGolemEntity)) {
            if (!(source.getDirectEntity() instanceof Player)) {
               return super.hurt(source, amount);
            } else {
               Player player = (Player)source.getDirectEntity();
               ItemStack stack = player.getInventory().getSelected();
               if (source.isCreativePlayer()) {
                  return super.hurt(source, amount);
               } else if (stack.getItem() == SkiesItems.debug_sword) {
                  return false;
               } else {
                  return (!source.is(DamageTypeTags.IS_PROJECTILE) && !source.is(DamageTypeTags.WITCH_RESISTANT_TO) || source.getEntity() != null)
                        && (source.is(DamageTypeTags.IS_PROJECTILE) || source.getEntity() == null || !(source.getEntity().distanceTo(this) > 3.55F))
                     ? super.hurt(source, Math.min(this.getDamageCap(), amount))
                     : false;
               }
            }
         } else {
            if (this.level().isClientSide) {
               return false;
            }

            this.teleportAround();
            return false;
         }
      } else {
         return false;
      }
   }

   public void teleportAround() {
      if (!this.level().isClientSide && !this.isCastingSpell() && this.getInvulTime() <= 0) {
         double d0 = this.getX();
         double d1 = this.getY();
         double d2 = this.getZ();
         double teleportStartX = this.isDungeonSpawned() ? this.getHome().getX() : this.getX();
         double teleportStartY = this.isDungeonSpawned() ? this.getHome().getY() : this.getY();
         double teleportStartZ = this.isDungeonSpawned() ? this.getHome().getZ() : this.getZ();

         for (int i = 0; i < 24; i++) {
            double d3 = teleportStartX + (this.getRandom().nextDouble() - 0.5) * 15.0;
            double d4 = Mth.clamp(teleportStartY + (this.getRandom().nextInt(16) - 8), 0.0, this.level().getMaxBuildHeight() - 1);
            double d5 = teleportStartZ + (this.getRandom().nextDouble() - 0.5) * 15.0;
            if (this.getVehicle() != null) {
               this.stopRiding();
            }

            boolean canTeleport = this.isDungeonSpawned()
               ? this.attemptDungeonTeleport(d3, Math.floor(d4), d5)
               : this.randomTeleport(d3, Math.floor(d4), d5, true);
            if (canTeleport) {
               this.sendNoLerp();
               this.level().playSound((Player)null, d0, d1, d2, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.HOSTILE, 1.0F, 1.0F);
               this.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
               break;
            }
         }
      }

      this.hurtMarked = true;
   }

   public boolean attemptDungeonTeleport(double x, double y, double z) {
      if (!(this.level() instanceof ServerLevel world)) {
         return false;
      } else {
         double var22 = this.getX();
         double d1 = this.getY();
         double d2 = this.getZ();
         double d3 = y;
         boolean flag = false;
         BlockPos blockpos = Positions.blockPos(x, y, z);
         if (world.isLoaded(blockpos) && blockpos.getY() >= this.getHome().getY()) {
            boolean shouldCompleteTeleport = false;

            while (!shouldCompleteTeleport && blockpos.getY() > 0) {
               BlockPos belowPos = blockpos.below();
               BlockState belowState = world.getBlockState(belowPos);
               AABB bounds = new AABB(blockpos).inflate(0.5, 0.0, 0.5);
               if (d3 >= this.getHome().getY()
                  && belowState.is(SkiesBlockTags.ILLAGER_TELEPORTABLE)
                  && this.level().getNearestPlayer(belowPos.getX(), belowPos.getY(), belowPos.getZ(), 6.0, true) == null
                  && belowState.blocksMotion()
                  && this.level().noCollision(bounds)
                  && !world.containsAnyLiquid(bounds)) {
                  shouldCompleteTeleport = true;
               } else {
                  d3--;
                  blockpos = belowPos;
               }
            }

            if (shouldCompleteTeleport && d3 >= this.getHome().getY()) {
               float width = this.getBbWidth() / 2.0F;
               float height = this.getBbHeight() * 0.3F;
               world.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY() + height, this.getZ(), 40, width, height, width, 0.0);
               BlockPos teleportationPos = Positions.blockPos(x, d3, z);
               this.teleportTo(teleportationPos.getX(), teleportationPos.getY(), teleportationPos.getZ());
               flag = true;
               world.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY() + height, this.getZ(), 40, width, height, width, 0.0);
            }
         }

         if (!flag) {
            this.teleportTo(var22, d1, d2);
            return false;
         } else {
            this.getNavigation().stop();
            return true;
         }
      }
   }

   public boolean isAlliedTo(Entity entityIn) {
      if (entityIn == null) {
         return false;
      } else if (entityIn instanceof Vex) {
         return this.isAlliedTo(((Vex)entityIn).getOwner());
      } else if (entityIn instanceof DecayingSpikeEntity) {
         return this.isAlliedTo(((DecayingSpikeEntity)entityIn).getCaster());
      } else if (entityIn instanceof ArtificialGolemEntity) {
         return this.isAlliedTo(((ArtificialGolemEntity)entityIn).getCaster());
      } else {
         return entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getType().is(net.minecraft.tags.EntityTypeTags.ILLAGER)
            ? this.getTeam() == null && entityIn.getTeam() == null
            : super.isAlliedTo(entityIn) || entityIn == this;
      }
   }

   public void checkIfHome() {
      if (this.getHome() != null && this.isDungeonSpawned()) {
         int distance = 22;
         if (EntityUtil.getDistanceToPos(this.blockPosition(), this.getHome()) >= distance || this.getY() < this.getHome().getY()) {
            System.out.println(this.position());
            this.teleportTo(this.getHome().getX(), this.getHome().getY(), this.getHome().getZ());
            this.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE, 1.0F, 1.0F);
            this.spawnAnim();
            this.level().setBlockAndUpdate(this.getHome().below(), this.getFloorState());
         }
      }
   }

   public void setBossSpellType(SkiesIllagerBossEntity.BossSpellColor spellType) {
      this.activeBossSpell = spellType;
      this.setBossSpellID(spellType.id);
   }

   protected SkiesIllagerBossEntity.BossSpellColor getBossSpellType() {
      return !this.level().isClientSide ? this.activeBossSpell : SkiesIllagerBossEntity.BossSpellColor.getFromId((Integer)this.entityData.get(BOSS_SPELL_ID));
   }

   public void setCanJoinRaid(boolean canJoinIn) {
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   public void applyRaidBuffs(int p_213660_1_, boolean p_213660_2_) {
   }

   public void setBossCanJoinRaid(boolean canJoinIn) {
      super.setCanJoinRaid(canJoinIn);
   }

   public boolean requiresCustomPersistence() {
      return true;
   }

   public boolean secondPhaseHealth() {
      return this.getHealth() <= this.getMaxHealth() / 2.0F;
   }

   @Override
   public int getExtraPlayers() {
      return Math.max(0, this.getPlayersInDungeonArea().size() - 1);
   }

   public void setCustomName(Component name) {
      super.setCustomName(name);
      this.getBossInfo().setName(this.getDisplayName().plainCopy().append(ISkyBoss.getDifficultyDecoration(this.getDifficultyID())));
   }

   @Override
   public BlockPos getHome() {
      return this.homePos != null && !this.homePos.equals(BlockPos.ZERO) ? this.homePos : this.blockPosition();
   }

   @Override
   public boolean isDungeonSpawned() {
      return (Boolean)this.entityData.get(DUNGEON_SPAWNED);
   }

   @Override
   public void setDungeonSpawned(boolean isDungeonSpawned) {
      this.entityData.set(DUNGEON_SPAWNED, isDungeonSpawned);
   }

   @Override
   public void setHome(BlockPos pos) {
      this.homePos = pos;
   }

   @Override
   public int getInvulTime() {
      return (Integer)this.entityData.get(INVULNERABILITY_TIME);
   }

   @Override
   public void setInvulTime(int time) {
      this.entityData.set(INVULNERABILITY_TIME, time);
   }

   public int getBossSpellID() {
      return (Integer)this.entityData.get(BOSS_SPELL_ID);
   }

   public void setBossSpellID(int id) {
      this.entityData.set(BOSS_SPELL_ID, id);
   }

   @Override
   public byte getDifficultyID() {
      return (Byte)this.entityData.get(DIFFICULTY_ID);
   }

   @Override
   public void setDifficultyID(byte id) {
      this.entityData.set(DIFFICULTY_ID, (byte)Math.min(3, id));
   }

   @Override
   public boolean isBossRespawned() {
      return (Boolean)this.entityData.get(RESPAWNED);
   }

   @Override
   public void setBossRespawned(boolean isRespawned) {
      this.entityData.set(RESPAWNED, isRespawned);
   }

   public void startSeenByPlayer(ServerPlayer player) {
      PacketHandler.sendToClient(new BossTypePacket((byte)this.getBossType().getId()), player);
      super.startSeenByPlayer(player);
      this.getBossInfo().addPlayer(player);
   }

   public void stopSeenByPlayer(ServerPlayer player) {
      super.stopSeenByPlayer(player);
      this.getBossInfo().removePlayer(player);
   }

   public boolean isAwayFromPlayers() {
      Player player = this.level().getNearestPlayer(this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ(), 5.0, true);
      return this.getTarget() != null && this.distanceTo(this.getTarget()) >= 5.0 && player == null || player == null;
   }

   public boolean isPatrolLeader() {
      return super.isPatrolLeader() && !this.isDungeonSpawned();
   }

   @Override
   public Monster getBoss() {
      return this;
   }

   protected boolean shouldDespawnInPeaceful() {
      return !this.isDungeonSpawned();
   }

   @Override
   public void peacefulTick() {
      ISkyBoss.super.peacefulTick();
      this.spellCastingTickCount = 0;
   }

   public enum BossSpellColor implements IExtensibleEnum {
      NONE(0, Vec3i.ZERO, false),
      REGENERATION(1, SkiesIllagerBossEntity.REGEN_SPELL_COLOR, false),
      SUMMON_GOLEM(2, SkiesIllagerBossEntity.GOLEM_SPELL_COLOR, false),
      SUMMON_RANGED_GOLEM(3, SkiesIllagerBossEntity.RANGED_GOLEM_SPELL_COLOR, false),
      SPHERE(4, SkiesIllagerBossEntity.SPHERE_SPELL_COLOR, true),
      ROCKS(5, SkiesIllagerBossEntity.ROCKS_SPELL_COLOR, true),
      ARROW_BUNDLE(6, SkiesIllagerBossEntity.ARROW_SPELL_COLOR, true),
      POISON_CLOUD(7, SkiesIllagerBossEntity.POISON_SPELL_COLOR, false),
      HARMING_CLOUD(8, SkiesIllagerBossEntity.HARMING_SPELL_COLOR, false),
      WATER(9, SkiesIllagerBossEntity.WATER_SPELL_COLOR, false),
      LAVA(10, SkiesIllagerBossEntity.LAVA_SPELL_COLOR, false),
      BLINDNESS(11, SkiesIllagerBossEntity.BLINDNESS_SPELL_COLOR, false);

      public final Vec3i colorVec;
      public final int id;
      public final boolean instant;

      BossSpellColor(int id, Vec3i colorVec, boolean instant) {
         this.colorVec = colorVec;
         this.id = id;
         this.instant = instant;
      }

      public static SkiesIllagerBossEntity.BossSpellColor getFromId(int idIn) {
         for (SkiesIllagerBossEntity.BossSpellColor bossTypes : values()) {
            if (idIn == bossTypes.id) {
               return bossTypes;
            }
         }

         return NONE;
      }

      public static SkiesIllagerBossEntity.BossSpellColor create(String enumName, int id, Vec3i colorVec, boolean instant) {
         throw new IllegalStateException("Enum not extended");
      }
   }

   class CastingSpellGoal extends SpellcasterCastingSpellGoal {
      private SkiesIllagerBossEntity boss = SkiesIllagerBossEntity.this;

      private CastingSpellGoal() {
         super(SkiesIllagerBossEntity.this);
      }

      public void start() {
         super.start();
         List<Player> playersInDungeon = this.boss.getPlayersInDungeonArea();

         for (Player players : playersInDungeon) {
            if (players != this.boss.getTarget()
               && SkiesIllagerBossEntity.this.random.nextBoolean()
               && playersInDungeon.size() > 1
               && !players.isCreative()
               && !players.isSpectator()) {
               this.boss.setTarget(players);
               break;
            }
         }
      }

      public void tick() {
         if (this.boss.getTarget() != null) {
            this.boss.getLookControl().setLookAt(this.boss.getTarget(), this.boss.getMaxHeadYRot(), this.boss.getMaxHeadXRot());
         }
      }

      public boolean requiresUpdateEveryTick() {
         return true;
      }
   }

   class DoNothingGoal extends Goal {
      public DoNothingGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
      }

      public boolean canUse() {
         return SkiesIllagerBossEntity.this.getInvulTime() > 0;
      }
   }

   public abstract class UseBossSpellGoal extends SpellcasterUseSpellGoal {
      protected UseBossSpellGoal() {
         super(SkiesIllagerBossEntity.this);
      }

      protected abstract SkiesIllagerBossEntity.BossSpellColor getSpellColor();

      public void start() {
         super.start();
         SkiesIllagerBossEntity.this.setBossSpellType(this.getSpellColor());
      }

      public boolean requiresUpdateEveryTick() {
         return true;
      }

      protected IllagerSpell getSpell() {
         return IllagerSpell.FANGS;
      }

      public boolean canContinueToUse() {
         return !SkiesIllagerBossEntity.this.isOnPeaceful() && super.canContinueToUse();
      }
   }
}

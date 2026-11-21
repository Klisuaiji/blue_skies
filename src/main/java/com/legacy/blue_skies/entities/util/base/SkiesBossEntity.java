package com.legacy.blue_skies.entities.util.base;

import com.legacy.blue_skies.entities.util.SkiesGoalSelector;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBoss;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.s_to_c.BossTypePacket;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.structure_gel.api.util.Positions;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public abstract class SkiesBossEntity extends SkiesMonsterEntity implements ISkyBoss {
   protected static final EntityDataAccessor<Boolean> DUNGEON_SPAWNED = SynchedEntityData.defineId(SkiesBossEntity.class, EntityDataSerializers.BOOLEAN);
   protected static final EntityDataAccessor<Integer> INVULNERABILITY_TIME = SynchedEntityData.defineId(SkiesBossEntity.class, EntityDataSerializers.INT);
   protected static final EntityDataAccessor<Boolean> RESPAWNED = SynchedEntityData.defineId(SkiesBossEntity.class, EntityDataSerializers.BOOLEAN);
   protected static final EntityDataAccessor<Byte> DIFFICULTY_ID = SynchedEntityData.defineId(SkiesBossEntity.class, EntityDataSerializers.BYTE);
   protected BlockPos homePos;
   private final ServerBossEvent bossInfo = this.getNewBossInfo();
   protected int bossInvulTicks;
   public SkiesGoalSelector goalSelector2;
   public SkiesGoalSelector targetSelector2;

   public SkiesBossEntity(EntityType<? extends SkiesBossEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   protected void registerGoals() {
      this.goalSelector2 = (SkiesGoalSelector)(this.goalSelector = new SkiesGoalSelector(this.level().getProfilerSupplier()));
      this.targetSelector2 = (SkiesGoalSelector)(this.targetSelector = new SkiesGoalSelector(this.level().getProfilerSupplier()));
      this.goalSelector2.addSkyGoal(0, new SkiesBossEntity.DoNothingGoal());
   }

   public void move(MoverType type, Vec3 vec) {
      if (this.getInvulTime() <= 0) {
         super.move(type, vec);
      } else {
         super.move(type, new Vec3(0.0, vec.y, 0.0));
      }
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(DUNGEON_SPAWNED, false);
      builder.define(INVULNERABILITY_TIME, 0);
      builder.define(RESPAWNED, false);
      builder.define(DIFFICULTY_ID, (byte)1);
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag
   ) {
      SpawnGroupData flag = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
      if (!this.level().isClientSide()) {
         this.defaultSpawningRoutine(difficultyIn);
      }

      return flag;
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.readDefaultBossNBT(compound);
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      this.writeDefaultBossNBT(compound);
   }

   public void tick() {
      super.tick();
      this.bossTick();
      if (this.bossInvulTicks > 0) {
         this.bossInvulTicks--;
      }
   }

   @Override
   public ServerBossEvent getBossInfo() {
      return this.bossInfo;
   }

   public void setCustomName(Component name) {
      super.setCustomName(name);
      this.getBossInfo().setName(this.getDisplayName().plainCopy().append(ISkyBoss.getDifficultyDecoration(this.getDifficultyID())));
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

   public boolean canChangeDimensions() {
      return false;
   }

   public boolean requiresCustomPersistence() {
      return true;
   }

   protected float getSoundVolume() {
      return 2.0F;
   }

   public boolean hurt(DamageSource source, float amount) {
      if (!this.level().getDifficulty().equals(Difficulty.PEACEFUL) && this.bossInvulTicks <= 0) {
         boolean flag = false;
         if (!this.isInvulnerableTo(source) && (this.getInvulTime() <= 0 || source == this.damageSources().fellOutOfWorld())) {
            if (source.getEntity() instanceof Player) {
               Player player = (Player)source.getEntity();
               ItemStack stack = player.getInventory().getSelected();
               if (source.isCreativePlayer()) {
                  flag = super.hurt(source, amount);
               } else {
                  if (stack.getItem() == SkiesItems.debug_sword) {
                     return false;
                  }

                  flag = super.hurt(source, Math.min(this.getDamageCap(), amount));
               }
            } else {
               flag = super.hurt(source, amount);
            }

            return flag;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void die(DamageSource source) {
      super.die(source);
      this.onBossDeath(source);
   }

   @Override
   public void setHome(BlockPos pos) {
      this.homePos = pos;
   }

   @Override
   public BlockPos getHome() {
      return this.isDungeonSpawned() && this.homePos != null && (this.homePos == null || this.homePos != BlockPos.ZERO) ? this.homePos : this.blockPosition();
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
   public boolean isBossRespawned() {
      return (Boolean)this.entityData.get(RESPAWNED);
   }

   @Override
   public void setBossRespawned(boolean isRespawned) {
      this.entityData.set(RESPAWNED, isRespawned);
   }

   @Override
   public int getInvulTime() {
      return (Integer)this.entityData.get(INVULNERABILITY_TIME);
   }

   @Override
   public void setInvulTime(int time) {
      this.entityData.set(INVULNERABILITY_TIME, time);
   }

   @Override
   public byte getDifficultyID() {
      return (Byte)this.entityData.get(DIFFICULTY_ID);
   }

   @Override
   public void setDifficultyID(byte id) {
      this.entityData.set(DIFFICULTY_ID, (byte)Math.min(3, id));
   }

   public float getDistanceToPos(double x, double y, double z) {
      return EntityUtil.getDistanceToPos(this.blockPosition(), Positions.blockPos(x, y, z));
   }

   public float getDistanceToPos(BlockPos pos) {
      return EntityUtil.getDistanceToPos(this.blockPosition(), pos);
   }

   @Override
   public Monster getBoss() {
      return this;
   }

   protected boolean shouldDespawnInPeaceful() {
      return !this.isDungeonSpawned();
   }

   public abstract class AbstractBossGoal extends Goal {
      public boolean requiresUpdateEveryTick() {
         return true;
      }
   }

   class DoNothingGoal extends Goal {
      public DoNothingGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
      }

      public boolean canUse() {
         return SkiesBossEntity.this.getInvulTime() > 0;
      }
   }
}

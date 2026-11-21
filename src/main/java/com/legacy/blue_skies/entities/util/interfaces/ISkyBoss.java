package com.legacy.blue_skies.entities.util.interfaces;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.blocks.misc.TrophyBlock;
import com.legacy.blue_skies.entities.BossItemEntity;
import com.legacy.blue_skies.entities.SupporterPetEntity;
import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import com.legacy.blue_skies.items.LootBagItem;
import com.legacy.blue_skies.items.util.IRarityItem;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.registries.SkiesStats;
import com.legacy.structure_gel.api.util.Positions;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public interface ISkyBoss extends ISkyBossMob {
   Predicate<Mob> IS_BOSS = entity -> entity instanceof ISkyBoss;

   default Predicate<LivingEntity> canAttackPredicate() {
      return entity -> entity != this.getBoss()
            && (!(entity instanceof Player) || EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity))
            && !(entity instanceof SupporterPetEntity)
         ? !entity.isInvulnerable()
            && !this.getBoss().isAlliedTo(entity)
            && entity.isAlive()
            && entity.invulnerableTime <= 0
            && EntitySelector.NO_SPECTATORS.test(entity)
         : false;
   }

   default Predicate<LivingEntity> canSeeAndAttackPredicate() {
      return entity -> this.getBoss().getSensing().hasLineOfSight(entity) && this.canAttackPredicate().test(entity);
   }

   Monster getBoss();

   SkiesDungeonType getBossType();

   int getProgression();

   boolean isEverbrightBoss();

   float getHealAmount();

   double getBossHealth();

   boolean isBossRespawned();

   default float getMaxReach() {
      return -1.0F;
   }

   default int getDamageCap() {
      return 20;
   }

   default byte getDefaultDifficultyID(Difficulty difficultyIn) {
      return (byte)(difficultyIn == Difficulty.HARD ? 2 : (difficultyIn == Difficulty.NORMAL ? 1 : 0));
   }

   byte getDifficultyID();

   default ISkyBoss.BossLevel getBossDifficulty() {
      return ISkyBoss.BossLevel.getFromId(this.getDifficultyID());
   }

   default BlockPos bossSpawnOffset() {
      return BlockPos.ZERO;
   }

   static Component getDifficultyDecoration(byte diffID) {
      int color = diffID == ISkyBoss.BossLevel.SILVER.getId()
         ? 11974326
         : (diffID == ISkyBoss.BossLevel.GOLD.getId() ? 14463509 : (diffID == ISkyBoss.BossLevel.PLATINUM.getId() ? 10336465 : 13467442));
      return Component.literal(" ⭐").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(color)));
   }

   LootBagItem getLootBag();

   @Nullable
   default TrophyBlock getTrophy() {
      return null;
   }

   BlockPos getHome();

   BossBarColor getBossBarColor();

   boolean isDungeonSpawned();

   default BossItemEntity dropLootBag(Player player) {
      Monster entity = this.getBoss();
      ItemStack stack = new ItemStack(this.getLootBag());
      IRarityItem.SkiesRarity rarity = this.getBossDifficulty().getBagRarity(entity.level().getRandom());
      LootBagItem.setLootTable(stack, BlueSkies.locate("loot_bags/" + BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath() + "/" + rarity));
      this.getLootBag().setRarityTag(stack, rarity);
      if (stack.isEmpty()) {
         return null;
      }

      if (entity.level().isClientSide) {
         return null;
      }

      BossItemEntity itementity = new BossItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), stack);
      itementity.setVisiblePlayerId(player.getUUID());
      itementity.setDefaultPickUpDelay();
      entity.level().addFreshEntity(itementity);
      return itementity;
   }

   default BossItemEntity dropTrophy(Player player) {
      if (this.getTrophy() == null) {
         return null;
      }

      Monster entity = this.getBoss();
      ItemStack stack = ISkyBoss.BossLevel.getTier(this.getBossDifficulty()).getItem(this.getTrophy());
      if (stack.isEmpty()) {
         return null;
      }

      if (entity.level().isClientSide) {
         return null;
      }

      BossItemEntity itementity = new BossItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), stack);
      itementity.setVisiblePlayerId(player.getUUID());
      itementity.setDefaultPickUpDelay();
      entity.level().addFreshEntity(itementity);
      return itementity;
   }

   default List<Player> getAllPlayersInDungeonArea() {
      return this.getPlayersInDungeonArea(15.0, true);
   }

   default List<Player> getPlayersInDungeonArea() {
      return this.getPlayersInDungeonArea(15.0, false);
   }

   default List<Player> getPlayersInDungeonArea(double distance, boolean creative) {
      if (this.getBoss().level().isClientSide) {
         return this.getBoss()
            .level()
            .getEntitiesOfClass(Player.class, this.getBoss().getBoundingBox().inflate(distance), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
      } else {
         return (List<Player>)(!this.getBoss().level().isClientSide && this.getHome() != null && this.getHome() != BlockPos.ZERO
            ? this.getBoss()
               .level()
               .getEntitiesOfClass(
                  Player.class,
                  new AABB(
                     this.getHome().getX() - distance,
                     this.getHome().getY() - 1.0,
                     this.getHome().getZ() - distance,
                     this.getHome().getX() + distance,
                     this.getHome().getY() + 10.0,
                     this.getHome().getZ() + distance
                  ),
                  creative ? EntitySelector.LIVING_ENTITY_STILL_ALIVE.and(EntitySelector.NO_SPECTATORS) : EntitySelector.NO_CREATIVE_OR_SPECTATOR
               )
            : ImmutableList.of());
      }
   }

   default List<Cow> getCowsInDungeonArea(double distance) {
      return this.getBoss().level().isClientSide()
         ? Lists.newArrayList()
         : this.getBoss()
            .level()
            .getEntitiesOfClass(
               Cow.class,
               new AABB(
                  this.getHome().getX() - distance,
                  this.getHome().getY() - 1.0,
                  this.getHome().getZ() - distance,
                  this.getHome().getZ() + distance,
                  this.getHome().getY() + 10.0,
                  this.getHome().getZ() + distance
               )
            );
   }

   default void randomizeAttackTarget(boolean extraChance) {
      List<Player> playersInDungeon = this.getPlayersInDungeonArea();
      playersInDungeon.forEach(players -> {
         if (extraChance && players != this.getBoss().getTarget() && playersInDungeon.size() > 1 && !players.isCreative() && !players.isSpectator()) {
            this.getBoss().setTarget(players);
         }
      });
   }

   default ServerBossEvent getNewBossInfo() {
      return (ServerBossEvent)new ServerBossEvent(
            this.getBoss().getDisplayName().plainCopy().append(getDifficultyDecoration(this.getDifficultyID())),
            this.getBossBarColor(),
            BossBarOverlay.PROGRESS
         )
         .setDarkenScreen(false)
         .setPlayBossMusic(true);
   }

   default void spawnParticles(ParticleOptions particleType) {
      if (this.getBoss().level().isClientSide()) {
         double dx = this.getBoss().level().getRandom().nextGaussian() * 0.02;
         double dy = this.getBoss().level().getRandom().nextGaussian() * 0.02;
         double dz = this.getBoss().level().getRandom().nextGaussian() * 0.02;
         this.getBoss()
            .level()
            .addParticle(
               particleType,
               this.getBoss().getX() + this.getBoss().level().getRandom().nextFloat() * this.getBoss().getBbWidth() * 2.0F - this.getBoss().getBbWidth(),
               this.getBoss().getY() + this.getBoss().level().getRandom().nextFloat() * this.getBoss().getBbHeight(),
               this.getBoss().getZ() + this.getBoss().level().getRandom().nextFloat() * this.getBoss().getBbWidth() * 2.0F - this.getBoss().getBbWidth(),
               dx,
               dy,
               dz
            );
      }
   }

   ServerBossEvent getBossInfo();

   default void bossTick() {
      this.getBossInfo().setProgress(this.getBoss().getHealth() / this.getBoss().getMaxHealth());
      if (this.getInvulTime() > 0) {
         int j1 = this.getInvulTime() - 1;
         this.setInvulTime(j1);
         if (j1 <= 0) {
            this.getBoss()
               .level()
               .playSound(
                  null, this.getBoss().getX(), this.getBoss().getY(), this.getBoss().getZ(), SkiesSounds.ENTITY_BOSS_SPAWNED, SoundSource.HOSTILE, 2.0F, 1.0F
               );
         }

         this.invulnerableTick();
      }

      if (this.isOnPeaceful() && this.isDungeonSpawned()) {
         if (this.getHome() != null) {
            this.getBoss().setPos(Vec3.atBottomCenterOf(this.getHome()));
         }

         this.setInvulTime(20);
         this.getBoss().setHealth(this.getBoss().getMaxHealth());
         this.getBoss().setTarget(null);
         this.peacefulTick();
      }
   }

   default void invulnerableTick() {
   }

   default void peacefulTick() {
   }

   default void readDefaultBossNBT(CompoundTag compound) {
      this.setDungeonSpawned(compound.getBoolean("DungeonSpawned"));
      this.setInvulTime(compound.getInt("Invul"));
      this.setBossRespawned(compound.getBoolean("BossRespawned"));
      this.setDifficultyID(compound.getByte("DifficultyID"));
      this.getBossInfo().setName(this.getBoss().getDisplayName().plainCopy().append(getDifficultyDecoration(compound.getByte("DifficultyID"))));
      if (compound.contains("HomeX") && compound.contains("HomeY") && compound.contains("HomeZ")) {
         this.setHome(Positions.blockPos(compound.getDouble("HomeX"), compound.getDouble("HomeY"), compound.getDouble("HomeZ")));
      }

      if (compound.contains("BossHomePos")) {
         this.setHome(NbtUtils.readBlockPos(compound, "BossHomePos").orElse(this.getHome()));
      }
   }

   default void writeDefaultBossNBT(CompoundTag compound) {
      compound.putInt("Invul", this.getInvulTime());
      compound.putBoolean("DungeonSpawned", this.isDungeonSpawned());
      compound.putBoolean("BossRespawned", this.isBossRespawned());
      compound.putByte("DifficultyID", this.getDifficultyID());
      if (this.getHome() != null) {
         compound.put("BossHomePos", NbtUtils.writeBlockPos(this.getHome()));
      }
   }

   default void defaultSpawningRoutine(DifficultyInstance difficultyIn) {
      byte diffId = (byte)(this.getDefaultDifficultyID(difficultyIn.getDifficulty()) + (this.isBossRespawned() ? 1 : 0));
      this.setDifficultyID(diffId);
      this.getBossInfo().setName(this.getBoss().getDisplayName().plainCopy().append(getDifficultyDecoration(diffId)));
      if (this.getHome() == null || this.getHome() != null && this.getHome().equals(BlockPos.ZERO)) {
         this.setHome(this.getBoss().blockPosition());
      }

      this.getBoss().getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getBossHealth());
      this.getBoss().setHealth(this.getBoss().getMaxHealth());
      BlueSkies.LOGGER
         .info(
            "Spawned a(n) "
               + this.getBoss().getDisplayName().getString()
               + " scaled to "
               + (1 + this.getExtraPlayers())
               + " player(s). The difficulty ID is "
               + ISkyBoss.BossLevel.getFromId(diffId)
               + ". The position is "
               + this.getBoss().blockPosition()
         );
   }

   default void onBossDeath(DamageSource source) {
      if (!this.getAllPlayersInDungeonArea().isEmpty()) {
         this.getAllPlayersInDungeonArea().forEach(players -> {
            if (this.getBoss().level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
               this.dropLootBag(players);
               this.dropTrophy(players);
            }

            if (source.getEntity() instanceof Player && players instanceof ServerPlayer) {
               CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger((ServerPlayer)players, this.getBoss(), source);
            }

            if (!players.isCreative() && !players.isSpectator()) {
               if (!this.isBossRespawned()) {
                  players.awardStat(SkiesStats.DUNGEONS_CONQUERED);
               }

               players.awardStat(SkiesStats.BOSSES_DEFEATED);
            }
         });
      }
   }

   default boolean isSecondPhase() {
      return this.getBoss().getHealth() <= this.getBoss().getMaxHealth() / 2.0F;
   }

   default boolean hasAttackTarget() {
      return this.getBoss().getTarget() != null;
   }

   default int getExtraPlayers() {
      return Math.max(0, this.getPlayersInDungeonArea().size() - 1);
   }

   default boolean isBossInvul() {
      return this.getInvulTime() > 0 || this.isOnPeaceful();
   }

   default boolean isOnPeaceful() {
      return this.getBoss().level().getDifficulty().equals(Difficulty.PEACEFUL);
   }

   void setHome(BlockPos var1);

   void setDungeonSpawned(boolean var1);

   void setBossRespawned(boolean var1);

   int getInvulTime();

   void setInvulTime(int var1);

   void setDifficultyID(byte var1);

   default int lowerCooldown(int cooldown) {
      if (cooldown > 0) {
         cooldown--;
      }

      return cooldown;
   }

   enum BossLevel {
      BRONZE(0, 70, 25, 5),
      SILVER(1, 60, 30, 10),
      GOLD(2, 50, 30, 20),
      PLATINUM(3, 0, 60, 40);

      private final int id;
      private final List<IRarityItem.WeightedSkiesRarity> weights = new ArrayList<>();

      BossLevel(int id, int commonBagChance, int uncommonBagChance, int rareBagChance) {
         this.id = id;
         if (commonBagChance > 0) {
            this.weights.add(new IRarityItem.WeightedSkiesRarity(commonBagChance, IRarityItem.SkiesRarity.COMMON));
         }

         if (uncommonBagChance > 0) {
            this.weights.add(new IRarityItem.WeightedSkiesRarity(uncommonBagChance, IRarityItem.SkiesRarity.UNCOMMON));
         }

         if (rareBagChance > 0) {
            this.weights.add(new IRarityItem.WeightedSkiesRarity(rareBagChance, IRarityItem.SkiesRarity.RARE));
         }
      }

      public IRarityItem.SkiesRarity getBagRarity(RandomSource rand) {
         return ((IRarityItem.WeightedSkiesRarity)WeightedRandomList.create(this.weights).getRandom(rand).get()).getRarity();
      }

      public static ISkyBoss.BossLevel getFromId(int id) {
         for (ISkyBoss.BossLevel type : values()) {
            if (id == type.id) {
               return type;
            }
         }

         return BRONZE;
      }

      public int getId() {
         return this.id;
      }

      public static TrophyBlock.Tier getTier(ISkyBoss.BossLevel level) {
         switch (level) {
            case PLATINUM:
               return TrophyBlock.Tier.PLATINUM;
            case GOLD:
               return TrophyBlock.Tier.GOLD;
            case SILVER:
               return TrophyBlock.Tier.SILVER;
            default:
               return TrophyBlock.Tier.BRONZE;
         }
      }
   }
}

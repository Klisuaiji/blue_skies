package com.legacy.blue_skies.entities.villager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.entities.hostile.ArmoredFrostSpiritEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.ArtificialGolemEntity;
import com.legacy.blue_skies.entities.util.base.SkiesIllagerBossEntity;
import com.legacy.blue_skies.entities.villager.tasks.GatekeeperTasks;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.EntityUtil;
import com.mojang.serialization.Dynamic;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.Brain.Provider;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class GatekeeperEntity extends AbstractVillager implements RangedAttackMob {
   public static final Predicate<LivingEntity> IS_POSSIBLE_DANGER = entity -> entity instanceof Enemy || entity instanceof Player;
   private static final EntityDataAccessor<Byte> SPELL = SynchedEntityData.defineId(GatekeeperEntity.class, EntityDataSerializers.BYTE);
   private SkiesIllagerBossEntity.BossSpellColor activeSpell = SkiesIllagerBossEntity.BossSpellColor.NONE;
   protected int spellTicks;
   public BlockPos homePos;
   private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
      MemoryModuleType.HOME,
      MemoryModuleType.JOB_SITE,
      MemoryModuleType.POTENTIAL_JOB_SITE,
      MemoryModuleType.MEETING_POINT,
      MemoryModuleType.NEAREST_LIVING_ENTITIES,
      MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
      MemoryModuleType.VISIBLE_VILLAGER_BABIES,
      MemoryModuleType.NEAREST_PLAYERS,
      MemoryModuleType.NEAREST_VISIBLE_PLAYER,
      MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
      MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
      MemoryModuleType.WALK_TARGET,
      new MemoryModuleType[]{
         MemoryModuleType.LOOK_TARGET,
         MemoryModuleType.INTERACTION_TARGET,
         MemoryModuleType.BREED_TARGET,
         MemoryModuleType.PATH,
         MemoryModuleType.DOORS_TO_CLOSE,
         MemoryModuleType.NEAREST_BED,
         MemoryModuleType.HURT_BY,
         MemoryModuleType.HURT_BY_ENTITY,
         MemoryModuleType.NEAREST_HOSTILE,
         MemoryModuleType.SECONDARY_JOB_SITE,
         MemoryModuleType.HIDING_PLACE,
         MemoryModuleType.HEARD_BELL_TIME,
         MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
         MemoryModuleType.LAST_SLEPT,
         MemoryModuleType.LAST_WOKEN,
         MemoryModuleType.LAST_WORKED_AT_POI,
         MemoryModuleType.GOLEM_DETECTED_RECENTLY
      }
   );
   private static final ImmutableList<SensorType<? extends Sensor<? super GatekeeperEntity>>> SENSOR_TYPES = ImmutableList.of(
      SensorType.NEAREST_LIVING_ENTITIES,
      SensorType.NEAREST_PLAYERS,
      SensorType.NEAREST_ITEMS,
      SensorType.NEAREST_BED,
      SensorType.HURT_BY,
      SensorType.VILLAGER_HOSTILES
   );
   public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<GatekeeperEntity, Holder<PoiType>>> VALID_POI = ImmutableMap.of(
      MemoryModuleType.HOME,
      (BiPredicate<GatekeeperEntity, Holder>)(p_219625_, p_219626_) -> p_219626_.is(PoiTypes.HOME),
      MemoryModuleType.MEETING_POINT,
      (BiPredicate<GatekeeperEntity, Holder>)(p_219616_, p_219617_) -> p_219617_.is(PoiTypes.MEETING)
   );
   private static final float DEFAULT_MOVEMENT_SPEED = 0.5F;
   private static boolean canGetAttributes = true;

   public GatekeeperEntity(EntityType<? extends GatekeeperEntity> type, Level world) {
      super(type, world);
      ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
      ((GroundPathNavigation)this.getNavigation()).setCanFloat(true);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new GatekeeperEntity.CastingASpellGoal());
      this.goalSelector.addGoal(6, new GatekeeperEntity.RegenGoal());
      this.goalSelector.addGoal(1, new GatekeeperEntity.RangedBowAttackGoal<GatekeeperEntity>(this, 0.65, 10, 15.0F) {
         @Override
         public boolean canUse() {
            return !GatekeeperEntity.this.isCriticalHealth() && !GatekeeperEntity.this.isSpellcasting() && super.canUse();
         }

         @Override
         public boolean canContinueToUse() {
            return !GatekeeperEntity.this.isCriticalHealth() && !GatekeeperEntity.this.isSpellcasting() && super.canContinueToUse();
         }
      });
      this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
      this.goalSelector.addGoal(3, new AvoidEntityGoal(this, SkiesIllagerBossEntity.class, 32.0F, 1.0, 1.2));
      this.goalSelector
         .addGoal(
            3,
            new AvoidEntityGoal<LivingEntity>(
               this, LivingEntity.class, 6.0F, 0.7, 1.0, entity -> this.getTarget() != null && entity == this.getTarget() || entity instanceof Enemy
            ) {
               public boolean canUse() {
                  return GatekeeperEntity.this.isCriticalHealth() && super.canUse();
               }

               public boolean canContinueToUse() {
                  return GatekeeperEntity.this.isCriticalHealth() && super.canContinueToUse();
               }
            }
         );
      this.targetSelector
         .addGoal(
            3,
            new NearestAttackableTargetGoal(
               this,
               Mob.class,
               5,
               false,
               false,
               mob -> (mob instanceof ArmoredFrostSpiritEntity || mob instanceof AbstractIllager || mob instanceof Zombie)
                  && !(mob instanceof ZombifiedPiglin)
                  && !(mob instanceof SkiesIllagerBossEntity)
                  && !(mob instanceof ArtificialGolemEntity)
            )
         );
   }

   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return super.getStandingEyeHeight(poseIn, sizeIn);
   }

   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return false;
   }

   public boolean isCriticalHealth() {
      return this.getHealth() <= this.getMaxHealth() / 3.0F;
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 48.0).add(Attributes.MAX_HEALTH, 80.0);
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn
   ) {
      ItemStack bow = new ItemStack(Items.BOW);
      bow.enchant(Enchantments.POWER_ARROWS, 2);
      this.setItemSlot(EquipmentSlot.MAINHAND, bow);
      return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
   }

   public void setTarget(LivingEntity entitylivingbaseIn) {
      if (entitylivingbaseIn == null || !(this.distanceTo(entitylivingbaseIn) > 20.0F)) {
         if (entitylivingbaseIn == null) {
            super.setTarget(entitylivingbaseIn);
         } else {
            if (this.getTarget() == null && this.hurtTime == 0) {
               this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC.value(), 1.2F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
               this.playSound(SkiesSounds.ENTITY_GATEKEEPER_NO, 1.0F, this.getVoicePitch());
            }

            super.setTarget(entitylivingbaseIn);
         }
      }
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(SPELL, (byte)0);
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("SpellTicks", this.spellTicks);
      if (this.homePos != null) {
         compound.put("GatekeeperHomePos", NbtUtils.writeBlockPos(this.homePos));
      }
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.spellTicks = compound.getInt("SpellTicks");
      if (this.level() instanceof ServerLevel) {
         this.resetBrain((ServerLevel)this.level());
      }

      if (compound.contains("GatekeeperHomePos")) {
         this.homePos = NbtUtils.readBlockPos(compound, "GatekeeperHomePos").orElse(this.homePos);
      }
   }

   public void tick() {
      super.tick();
      if (this.spellTicks > 0) {
         this.spellTicks--;
      }

      if (this.level().isClientSide && this.isSpellcasting()) {
         SkiesIllagerBossEntity.BossSpellColor spellColor = this.getSpellType();
         Vec3i particleSpeed = spellColor.colorVec;
         double d0 = particleSpeed.getX() / 255.0;
         double d1 = particleSpeed.getY() / 255.0;
         double d2 = particleSpeed.getZ() / 255.0;
         float f = this.yBodyRot * (float) (Math.PI / 180.0) + Mth.cos(this.tickCount * 0.6662F) * 0.25F;
         float f1 = Mth.cos(f);
         float f2 = Mth.sin(f);
         this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + f1 * 0.6, this.getY() + 1.8, this.getZ() + f2 * 0.6, d0, d1, d2);
         this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - f1 * 0.6, this.getY() + 1.8, this.getZ() - f2 * 0.6, d0, d1, d2);
      }

      if (this.homePos != null
         && this.homePos != BlockPos.ZERO
         && EntityUtil.getDistanceToPos(this.blockPosition(), this.homePos) < 60.0F
         && (
            EntityUtil.getDistanceToPos(this.blockPosition(), this.homePos) >= 30.0F
               || EntityUtil.getDistanceToPos(this.blockPosition(), this.homePos) >= 10.0F
                  && !this.level().isDay()
                  && this.level().canSeeSky(this.blockPosition())
                  && !this.level().canSeeSky(this.homePos)
         )) {
         this.getNavigation().moveTo(this.homePos.getX(), this.homePos.getY(), this.homePos.getZ(), 0.7F);
      }

      if (!this.level().isClientSide && this.getTarget() != null && !this.getTarget().isAlive()) {
         this.setTarget(null);
      }
   }

   protected void customServerAiStep() {
      this.level().getProfiler().push("brain");
      this.getBrain().tick((ServerLevel)this.level(), this);
      this.level().getProfiler().pop();
      super.customServerAiStep();
   }

   protected SoundEvent getAmbientSound() {
      return this.isSleeping() ? null : (this.isTrading() ? SkiesSounds.ENTITY_GATEKEEPER_TRADE : SkiesSounds.ENTITY_GATEKEEPER_IDLE);
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_GATEKEEPER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_GATEKEEPER_DEATH;
   }

   protected SoundEvent getTradeUpdatedSound(boolean yes) {
      return yes ? this.getNotifyTradeSound() : SkiesSounds.ENTITY_GATEKEEPER_NO;
   }

   public SoundEvent getNotifyTradeSound() {
      return SkiesSounds.ENTITY_GATEKEEPER_YES;
   }

   protected SoundEvent getSpellSound() {
      return SkiesSounds.ENTITY_GATEKEEPER_CAST_SPELL;
   }

   public GatekeeperEntity getBreedOffspring(ServerLevel world, AgeableMob ageable) {
      return null;
   }

   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      boolean flag = itemstack.getItem() == Items.NAME_TAG;
      if (flag) {
         itemstack.interactLivingEntity(player, this, hand);
         return InteractionResult.SUCCESS;
      }

      if (itemstack.getItem() == SkiesItems.gatekeeper_spawn_egg || !this.isAlive() || this.isTrading() || this.isBaby()) {
         return super.mobInteract(player, hand);
      }

      if (this.isAggressive() && !this.level().isClientSide) {
         this.playSound(SkiesSounds.ENTITY_GATEKEEPER_NO, 1.0F, this.getVoicePitch());
         return InteractionResult.PASS;
      }

      if (player.getItemInHand(hand).getItem() == SkiesBlocks.bag_of_spoils.asItem() && !this.level().isClientSide) {
         this.setTarget(player);
         return InteractionResult.PASS;
      }

      if (hand == InteractionHand.MAIN_HAND) {
         player.awardStat(Stats.TALKED_TO_VILLAGER);
      }

      if (this.getOffers().isEmpty()) {
         return super.mobInteract(player, hand);
      }

      if (!this.level().isClientSide) {
         this.setTradingPlayer(player);
         this.openTradingScreen(player, this.getDisplayName(), 1);
      }

      return InteractionResult.SUCCESS;
   }

   protected void rewardTradeXp(MerchantOffer offer) {
      if (offer.shouldRewardExp()) {
         int i = 3 + this.random.nextInt(4);
         this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), i));
      }
   }

   protected void updateTrades() {
      this.addOffersFromItemListings(new MerchantOffers(), new ItemListing[0], 0);
   }

   public MerchantOffers getOffers() {
      if (this.offers == null) {
         this.offers = new MerchantOffers();
      }

      List<ItemListing> trades = SkiesPlayer.getIfPresent(
         this.getTradingPlayer(),
         skiesPlayer -> this.getTradesForProgression(skiesPlayer.getBrightProgression(), skiesPlayer.getDawnProgression()),
         () -> this.getTradesForProgression(0, 0)
      );
      MerchantOffers offers = new MerchantOffers();

      for (ItemListing trade : trades) {
         offers.add(trade.getOffer(this.getTradingPlayer(), this.random));
      }

      return offers;
   }

   public List<ItemListing> getTradesForProgression(int everbrightProgress, int everdawnProgress) {
      List<ItemListing> trades = new ArrayList<>();
      trades.add(
         new SkiesVillagerTrades.SingleTrade.Builder()
            .item(Items.EMERALD, Math.min(64, Math.max(1, BlueSkiesConfig.COMMON.getZealLighterCost())))
            .result(SkiesItems.zeal_lighter, 1)
            .maxUses(5)
            .givenXP(10)
            .build()
      );
      trades.add(new SkiesVillagerTrades.SingleTrade.Builder().item(Items.BOOK, 1).result(SkiesItems.blue_journal, 1).maxUses(50).givenXP(0).build());
      if (!this.level().isClientSide && this.getTradingPlayer() instanceof ServerPlayer) {
         ServerPlayer player = (ServerPlayer)this.getTradingPlayer();
         if (EntityUtil.hasAdvancement(player, BlueSkies.locate("progression/unlock_everbright_blinding_key_trade"))
            || EntityUtil.hasAdvancement(player, BlueSkies.locate("progression/unlock_everdawn_blinding_key_trade"))) {
            trades.add(new SkiesVillagerTrades.KeyTrade(Items.BOOK.asItem(), 6, Items.EMERALD, 1, SkiesItems.blinding_key));
         }

         if (everbrightProgress >= 1 && EntityUtil.hasAdvancement(player, BlueSkies.locate("progression/unlock_nature_key_trade"))) {
            trades.add(new SkiesVillagerTrades.KeyTrade(SkiesBlocks.nature_stone.asItem(), 6, SkiesBlocks.starlit_sapling.asItem(), 16, SkiesItems.nature_key));
         }

         if (everdawnProgress >= 1 && EntityUtil.hasAdvancement(player, BlueSkies.locate("progression/unlock_poison_key_trade"))) {
            trades.add(new SkiesVillagerTrades.KeyTrade(SkiesBlocks.poison_stone.asItem(), 6, Items.SPIDER_EYE, 16, SkiesItems.poison_key));
         }
      }

      return trades;
   }

   public void die(DamageSource cause) {
      this.releasePoi(MemoryModuleType.HOME);
      this.releasePoi(MemoryModuleType.MEETING_POINT);
      super.die(cause);
   }

   public void releasePoi(MemoryModuleType<GlobalPos> p_35429_) {
      if (this.level() instanceof ServerLevel) {
         MinecraftServer minecraftserver = ((ServerLevel)this.level()).getServer();
         this.brain.getMemory(p_35429_).ifPresent(p_186306_ -> {
            ServerLevel serverlevel = minecraftserver.getLevel(p_186306_.dimension());
            if (serverlevel != null) {
               PoiManager poimanager = serverlevel.getPoiManager();
               Optional<Holder<PoiType>> optional = poimanager.getType(p_186306_.pos());
               BiPredicate<GatekeeperEntity, Holder<PoiType>> bipredicate = VALID_POI.get(p_35429_);
               if (optional.isPresent() && bipredicate.test(this, optional.get())) {
                  poimanager.release(p_186306_.pos());
                  DebugPackets.sendPoiTicketCountPacket(serverlevel, p_186306_.pos());
               }
            }
         });
      }
   }

   public boolean isAlliedTo(Entity entityIn) {
      return entityIn instanceof Villager || entityIn instanceof IronGolem || entityIn instanceof GatekeeperEntity || this.isAlliedTo(entityIn.getTeam());
   }

   public boolean showProgressBar() {
      return false;
   }

   public boolean canChangeDimensions() {
      return false;
   }

   public Brain<GatekeeperEntity> getBrain() {
      return super.getBrain();
   }

   protected Provider<GatekeeperEntity> brainProvider() {
      return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
   }

   protected Brain<?> makeBrain(Dynamic<?> nbtOps) {
      Brain<GatekeeperEntity> brain = this.brainProvider().makeBrain(nbtOps);
      this.initBrain(brain);
      return brain;
   }

   public void resetBrain(ServerLevel world) {
      Brain<GatekeeperEntity> brain = this.getBrain();
      brain.stopAll(world, this);
      this.brain = brain.copyWithoutBehaviors();
      this.initBrain(this.getBrain());
   }

   private void initBrain(Brain<GatekeeperEntity> villagerBrain) {
      float f;
      try {
         if (canGetAttributes) {
            f = (float)this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
         } else {
            f = 0.5F;
         }
      } catch (Exception e) {
         f = 0.5F;
         canGetAttributes = false;
      }

      villagerBrain.setSchedule(Schedule.VILLAGER_DEFAULT);
      villagerBrain.addActivity(Activity.CORE, GatekeeperTasks.core(f));
      villagerBrain.addActivity(Activity.REST, GatekeeperTasks.rest(f));
      villagerBrain.addActivity(Activity.IDLE, GatekeeperTasks.idle(f));
      villagerBrain.setCoreActivities(ImmutableSet.of(Activity.CORE));
      villagerBrain.setDefaultActivity(Activity.IDLE);
      villagerBrain.setActiveActivityIfPossible(Activity.IDLE);
      villagerBrain.updateActivityFromSchedule(this.level().getDayTime(), this.level().getGameTime());
   }

   public void startSleeping(BlockPos pos) {
      super.startSleeping(pos);
      this.brain.setMemory(MemoryModuleType.LAST_SLEPT, this.level().getGameTime());
   }

   public void stopSleeping() {
      super.stopSleeping();
      this.brain.setMemory(MemoryModuleType.LAST_WOKEN, this.level().getGameTime());
   }

   @OnlyIn(Dist.CLIENT)
   public GatekeeperEntity.ArmPose getArmPose() {
      if (this.isSpellcasting()) {
         return GatekeeperEntity.ArmPose.SPELLCASTING;
      } else {
         return this.isAggressive() ? GatekeeperEntity.ArmPose.BOW_AND_ARROW : GatekeeperEntity.ArmPose.CROSSED;
      }
   }

   public boolean isSpellcasting() {
      return this.level().isClientSide ? (Byte)this.entityData.get(SPELL) > 0 : this.spellTicks > 0;
   }

   public void setSpellType(SkiesIllagerBossEntity.BossSpellColor spellType) {
      this.activeSpell = spellType;
      this.entityData.set(SPELL, (byte)Math.min(255, spellType.id));
   }

   protected int getSpellTicks() {
      return this.spellTicks;
   }

   protected SkiesIllagerBossEntity.BossSpellColor getSpellType() {
      return !this.level().isClientSide() ? this.activeSpell : SkiesIllagerBossEntity.BossSpellColor.getFromId((Byte)this.entityData.get(SPELL));
   }

   public void performRangedAttack(LivingEntity target, float distanceFactor) {
      ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem)));
      AbstractArrow abstractarrowentity = ProjectileUtil.getMobArrow(this, itemstack, distanceFactor);
      if (this.getMainHandItem().getItem() instanceof BowItem) {
         abstractarrowentity = ((BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrowentity, itemstack);
      }

      double d0 = target.getX() - this.getX();
      double d1 = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - abstractarrowentity.getY();
      double d2 = target.getZ() - this.getZ();
      double d3 = Mth.sqrt((float)(d0 * d0 + d2 * d2));
      abstractarrowentity.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, 14 - this.level().getDifficulty().getId() * 4);
      this.playSound(SoundEvents.ARROW_SHOOT.value(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
      this.level().addFreshEntity(abstractarrowentity);
   }

   @OnlyIn(Dist.CLIENT)
   public enum ArmPose {
      CROSSED,
      ATTACKING,
      BOW_AND_ARROW,
      SPELLCASTING;
   }

   public class CastingASpellGoal extends Goal {
      public CastingASpellGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
      }

      public boolean canUse() {
         return !GatekeeperEntity.this.isSleeping() && GatekeeperEntity.this.getSpellTicks() > 0;
      }

      public void start() {
         super.start();
         GatekeeperEntity.this.navigation.stop();
      }

      public void stop() {
         super.stop();
         GatekeeperEntity.this.setSpellType(SkiesIllagerBossEntity.BossSpellColor.NONE);
      }

      public void tick() {
         if (GatekeeperEntity.this.getTarget() != null) {
            GatekeeperEntity.this.getLookControl()
               .setLookAt(GatekeeperEntity.this.getTarget(), GatekeeperEntity.this.getMaxHeadYRot(), GatekeeperEntity.this.getMaxHeadXRot());
         }
      }
   }

   public abstract class GatekeeperUseSpellGoal extends Goal {
      protected int spellWarmup;
      protected int spellCooldown;

      public boolean shouldExecute() {
         LivingEntity livingentity = GatekeeperEntity.this.getTarget();
         if (livingentity == null || !livingentity.isAlive()) {
            return false;
         } else {
            return GatekeeperEntity.this.isSpellcasting() ? false : GatekeeperEntity.this.tickCount >= this.spellCooldown;
         }
      }

      public boolean canContinueToUse() {
         return this.spellWarmup > 0;
      }

      public void start() {
         this.spellWarmup = this.getCastWarmupTime();
         GatekeeperEntity.this.spellTicks = this.getCastingTime();
         this.spellCooldown = GatekeeperEntity.this.tickCount + this.getCastingInterval();
         SoundEvent soundevent = this.getSpellPrepareSound();
         if (soundevent != null) {
            GatekeeperEntity.this.playSound(soundevent, 1.0F, 1.0F);
         }

         GatekeeperEntity.this.setSpellType(this.getSpellType());
      }

      public void tick() {
         this.spellWarmup--;
         if (this.spellWarmup == 0) {
            this.castSpell();
            GatekeeperEntity.this.playSound(GatekeeperEntity.this.getSpellSound(), 1.0F, 1.0F);
         }
      }

      protected abstract void castSpell();

      protected int getCastWarmupTime() {
         return 20;
      }

      protected abstract int getCastingTime();

      protected abstract int getCastingInterval();

      @Nullable
      protected abstract SoundEvent getSpellPrepareSound();

      protected abstract SkiesIllagerBossEntity.BossSpellColor getSpellType();

      public boolean requiresUpdateEveryTick() {
         return true;
      }
   }

   class RangedBowAttackGoal<T extends GatekeeperEntity & RangedAttackMob> extends Goal {
      private final T entity;
      private final double moveSpeedAmp;
      private int attackCooldown;
      private final float maxAttackDistance;
      private int attackTime = -1;
      private int seeTime;
      private boolean strafingClockwise;
      private boolean strafingBackwards;
      private int strafingTime = -1;

      public RangedBowAttackGoal(T mob, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn) {
         this.entity = mob;
         this.moveSpeedAmp = moveSpeedAmpIn;
         this.attackCooldown = attackCooldownIn;
         this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
      }

      public void setAttackCooldown(int p_189428_1_) {
         this.attackCooldown = p_189428_1_;
      }

      public boolean canUse() {
         return this.entity.getTarget() == null ? false : this.isBowInMainhand();
      }

      protected boolean isBowInMainhand() {
         ItemStack main = this.entity.getMainHandItem();
         ItemStack off = this.entity.getOffhandItem();
         return main.getItem() instanceof BowItem || off.getItem() instanceof BowItem;
      }

      public boolean canContinueToUse() {
         return (this.canUse() || !this.entity.getNavigation().isDone()) && this.isBowInMainhand();
      }

      public void start() {
         super.start();
         this.entity.setAggressive(true);
      }

      public void stop() {
         super.stop();
         this.entity.setAggressive(false);
         this.seeTime = 0;
         this.attackTime = -1;
         this.entity.stopUsingItem();
      }

      public void tick() {
         LivingEntity livingentity = this.entity.getTarget();
         if (livingentity != null) {
            double d0 = this.entity.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ());
            boolean flag = this.entity.getSensing().hasLineOfSight(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
               this.seeTime = 0;
            }

            if (flag) {
               this.seeTime++;
            } else {
               this.seeTime--;
            }

            if (!(d0 > this.maxAttackDistance) && this.seeTime >= 20) {
               this.entity.getNavigation().stop();
               this.strafingTime++;
            } else {
               this.entity.getNavigation().moveTo(livingentity, this.moveSpeedAmp);
               this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
               if (this.entity.getRandom().nextFloat() < 0.3) {
                  this.strafingClockwise = !this.strafingClockwise;
               }

               if (this.entity.getRandom().nextFloat() < 0.3) {
                  this.strafingBackwards = !this.strafingBackwards;
               }

               this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
               if (d0 > this.maxAttackDistance * 0.75F) {
                  this.strafingBackwards = false;
               } else if (d0 < this.maxAttackDistance * 0.25F) {
                  this.strafingBackwards = true;
               }

               this.entity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
               this.entity.lookAt(livingentity, 30.0F, 30.0F);
            } else {
               this.entity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }

            if (this.entity.isUsingItem()) {
               if (!flag && this.seeTime < -60) {
                  this.entity.stopUsingItem();
               } else if (flag) {
                  int i = this.entity.getTicksUsingItem();
                  if (i >= 20) {
                     this.entity.stopUsingItem();
                     this.entity.performRangedAttack(livingentity, BowItem.getPowerForTime(i));
                     this.attackTime = this.attackCooldown;
                  }
               }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
               this.entity.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.entity, item -> item instanceof BowItem));
            }
         }
      }
   }

   public class RegenGoal extends GatekeeperEntity.GatekeeperUseSpellGoal {
      public boolean canUse() {
         if (GatekeeperEntity.this.tickCount < this.spellCooldown) {
            return false;
         }

         GatekeeperEntity keeper = GatekeeperEntity.this;
         Player player = keeper.level().getNearestPlayer(keeper, 30.0);
         boolean criticalHealth = keeper.isCriticalHealth();
         boolean should = !keeper.isSleeping()
            && !keeper.isSpellcasting()
            && keeper.getHealth() < keeper.getMaxHealth()
            && (keeper.getTarget() == null || criticalHealth);
         return !criticalHealth && player != null
            ? should && !keeper.getSensing().hasLineOfSight(player)
            : should && (criticalHealth && (keeper.getTarget() != null || keeper.hurtTime > 0) || player == null);
      }

      @Override
      public boolean canContinueToUse() {
         return super.canContinueToUse();
      }

      public void stop() {
         super.stop();
      }

      @Override
      protected void castSpell() {
         GatekeeperEntity.this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 23, 5, false, false));
      }

      @Override
      protected int getCastWarmupTime() {
         return 20;
      }

      @Override
      protected int getCastingTime() {
         return 70;
      }

      @Override
      protected int getCastingInterval() {
         return 200;
      }

      @Override
      protected SoundEvent getSpellPrepareSound() {
         return SkiesSounds.ENTITY_GATEKEEPER_PREPARE_REGEN;
      }

      @Override
      protected SkiesIllagerBossEntity.BossSpellColor getSpellType() {
         return SkiesIllagerBossEntity.BossSpellColor.REGENERATION;
      }
   }
}

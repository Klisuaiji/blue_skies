# Blue Skies — 1.21.1 迁移待办清单（逐处）

由 1.20.4 源码扫描生成，格式为 `文件:行`。改完一处可自行勾掉。

## 类别：NBT/数据组件
com/legacy/blue_skies/block_entity/SkiesBlockEntity.java:31:      this.load(packet.getTag());
com/legacy/blue_skies/blocks/dungeon/KeystoneBlock.java:81:            EntityType<?> entityType = ((SpawnEggItem)heldItem.getItem()).getType(heldItem.getTag());
com/legacy/blue_skies/blocks/misc/TrophyBlock.java:163:         CompoundTag nbt = stack.getOrCreateTag();
com/legacy/blue_skies/client/audio/ambient/SkiesWeatherAmbientSounds.java:35:            && mc.level.registryAccess().registryOrThrow(Registries.BIOME).getOrCre
com/legacy/blue_skies/client/events/SkiesClientEvents.java:455:                  && world.registryAccess().registryOrThrow(Registries.BIOME).getOrCreateTag(Skie
com/legacy/blue_skies/client/events/SkiesClientEvents.java:459:                     && !world.registryAccess().registryOrThrow(Registries.BIOME).getOrCreateTag(
com/legacy/blue_skies/client/init/SkiesColoring.java:111:         PreparedFood preparedFood = FoodPrepUtils.deserialize(itemStack.getTag());
com/legacy/blue_skies/client/init/SkiesItemModelPredicates.java:100:            CompoundTag tag = stack.getTag();
com/legacy/blue_skies/client/renders/world/DawnWeatherRenderer.java:80:                  || (hasSandstorm = level.registryAccess().registryOrThrow(Registries.BI
com/legacy/blue_skies/client/renders/world/DawnWeatherTick.java:104:            && level.registryAccess().registryOrThrow(Registries.BIOME).getOrCreateTag(Skies
com/legacy/blue_skies/crafting/toolbox/ModifySpearHandleRecipe.java:31:      output.setTag(inputItem.getTag());
com/legacy/blue_skies/entities/passive/fish/JellyDrifterEntity.java:214:      CompoundTag compoundnbt = bucket.getOrCreateTag();
com/legacy/blue_skies/entities/passive/ShadeMonitorEntity.java:206:            if (b.tagKey != null && registry.getOrCreateTag(b.tagKey).contains(biome)) {
com/legacy/blue_skies/entities/passive/ShadeMonitorEntity.java:211:         return registry.getOrCreateTag(SkiesBiomeTags.EVERDAWN).contains(biome) ? PURPLE : O
com/legacy/blue_skies/items/arcs/IArcItem.java:30:      CompoundTag nbt = stack.getOrCreateTag();
com/legacy/blue_skies/items/arcs/IArcItem.java:37:      CompoundTag nbt = stack.getOrCreateTag();
com/legacy/blue_skies/items/buckets/VentiumMobBucketItem.java:59:         bucketable.loadFromBucketTag(bucketStack.getOrCreateTag());
com/legacy/blue_skies/items/buckets/VentiumMobBucketItem.java:67:         CompoundTag compoundnbt = stack.getTag();
com/legacy/blue_skies/items/LootBagItem.java:44:      CompoundTag stackNbt = stack.getTag();
com/legacy/blue_skies/items/LootBagItem.java:87:      CompoundTag nbt = stack.getTag();
com/legacy/blue_skies/items/LootBagItem.java:99:      CompoundTag nbt = stack.getOrCreateTag();
com/legacy/blue_skies/items/PreparedFoodItem.java:118:      PreparedFood preparedFood = FoodPrepUtils.deserialize(stack.getTag());
com/legacy/blue_skies/items/PreparedFoodItem.java:45:      if (player.canEat(FoodPrepUtils.deserialize(heldItem.getTag()).getHungerPerBite() <= 2)) {
com/legacy/blue_skies/items/PreparedFoodItem.java:54:      PreparedFood preparedFood = FoodPrepUtils.deserialize(stack.getTag());
com/legacy/blue_skies/items/PreparedFoodItem.java:84:      CompoundTag tag = stack.getOrCreateTag();
com/legacy/blue_skies/items/tools/AstrolabeItem.java:142:      CompoundTag compoundnbt = stack.getTag();
com/legacy/blue_skies/items/tools/AstrolabeItem.java:147:      CompoundTag compoundnbt = stack.getOrCreateTag();
com/legacy/blue_skies/items/tools/AstrolabeItem.java:153:      CompoundTag compoundnbt = stack.getTag();
com/legacy/blue_skies/items/tools/AstrolabeItem.java:158:      CompoundTag compoundnbt = stack.getOrCreateTag();
com/legacy/blue_skies/items/tools/AstrolabeItem.java:163:      CompoundTag compoundnbt = stack.getTag();
com/legacy/blue_skies/items/tools/AstrolabeItem.java:168:      CompoundTag compoundnbt = stack.getOrCreateTag();
com/legacy/blue_skies/items/tools/weapons/InfusedArcSwordItem.java:41:      CompoundTag compoundnbt = stack.getTag();
com/legacy/blue_skies/items/tools/weapons/InfusedArcSwordItem.java:46:      CompoundTag compoundnbt = stack.getOrCreateTag();
com/legacy/blue_skies/items/tools/weapons/InfusedArcSwordItem.java:51:      CompoundTag compoundnbt = stack.getTag();
com/legacy/blue_skies/items/tools/weapons/InfusedArcSwordItem.java:56:      CompoundTag compoundnbt = stack.getOrCreateTag();
com/legacy/blue_skies/items/tools/weapons/SummoningTomeItem.java:148:      CompoundTag compoundnbt = stack.getTag();
com/legacy/blue_skies/items/tools/weapons/SummoningTomeItem.java:153:      CompoundTag compoundnbt = stack.getOrCreateTag();
com/legacy/blue_skies/items/util/FoodPrepUtils.java:115:      CompoundTag tag = stack.getOrCreateTag();
com/legacy/blue_skies/items/util/FoodPrepUtils.java:120:      CompoundTag tag = stack.getOrCreateTag();
com/legacy/blue_skies/items/util/FoodPrepUtils.java:164:         serialize(stack.getOrCreateTag(), new PreparedFood(ingredients, getEffects(ingredients, maxUses
com/legacy/blue_skies/items/util/FoodPrepUtils.java:256:            PotionUtils.getAllEffects(ingredient.getOrCreateTag()).forEach(e -> addEffect(effectMap, e))
com/legacy/blue_skies/items/util/IFalsiteItem.java:34:      stack.getOrCreateTag().putInt("Falsite", getMaxFalsiteUses(stack));
com/legacy/blue_skies/items/util/IFalsiteItem.java:43:      stack.getOrCreateTag().putInt("Falsite", Math.max(value, 0));
com/legacy/blue_skies/items/util/IFalsiteItem.java:47:      CompoundTag tag = stack.getTag();
com/legacy/blue_skies/items/util/IRarityItem.java:16:      CompoundTag nbt = stack.getOrCreateTag();
com/legacy/blue_skies/items/util/IRarityItem.java:21:      CompoundTag nbt = stack.getOrCreateTag();
com/legacy/blue_skies/items/util/ToolUtils.java:39:      CompoundTag tag = stack.getTag();
com/legacy/blue_skies/items/util/ToolUtils.java:51:      stack.getOrCreateTag().putString("Stick", BlueSkiesData.TOOL_HANDLE_TYPES.getHandle(stick).getName());
com/legacy/blue_skies/util/EntityUtil.java:117:      CompoundTag compoundnbt = itemstack1.getOrCreateTagElement("Explosion");
com/legacy/blue_skies/util/EntityUtil.java:125:      CompoundTag compoundnbt1 = itemstack.getOrCreateTagElement("Fireworks");

## 类别：Capability/LazyOptional
com/legacy/blue_skies/BlueSkiesConfig.java:114:      private final ConfigValue<Boolean> crashOnMissingCapabilityForDungeon;
com/legacy/blue_skies/BlueSkiesConfig.java:136:         this.crashOnMissingCapabilityForDungeon = ConfigBuilder.makeBoolean(
com/legacy/blue_skies/BlueSkiesConfig.java:181:      public boolean crashOnMissingCapabilityForDungeon() {
com/legacy/blue_skies/BlueSkiesConfig.java:182:         return (Boolean)this.crashOnMissingCapabilityForDungeon.get();
com/legacy/blue_skies/capability/SkiesPlayer.java:65:   private static final Marker MARKER = MarkerManager.getMarker("Player Capability");
com/legacy/blue_skies/events/SkiesPlayerEvents.java:715:         if (BlueSkiesConfig.COMMON.crashOnMissingCapabilityForDungeon()) {
com/legacy/blue_skies/mixin/CapabilityMigrateMixin.java:22:public class CapabilityMigrateMixin {
com/legacy/blue_skies/mixin/CapabilityMigrateMixin.java:58:                           BlueSkies.LOGGER.debug("Migrating Forge Capability {} to NeoForge Attachme
com/legacy/blue_skies/registries/SkiesJigsawTypes.java:13:   public static final JigsawCapabilityType<FrozenBunkerStructure.Capability> FROZEN_BUNKER = () -> Fr
com/legacy/blue_skies/registries/SkiesJigsawTypes.java:14:   public static final JigsawCapabilityType<GatekeeperHouseStructure.Capability> GATEKEEPER_HOUSE = ()
com/legacy/blue_skies/registries/SkiesJigsawTypes.java:15:   public static final JigsawCapabilityType<SkiesVillageStructure.Capability> VILLAGE = () -> SkiesVil
com/legacy/blue_skies/registries/SkiesJigsawTypes.java:24:   private static void register(RegisterEvent event, String key, JigsawCapabilityType<?> type) {
com/legacy/blue_skies/registries/SkiesJigsawTypes.java:8:import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapabilityType;
com/legacy/blue_skies/registries/SkiesStructures.java:162:            .capability(GatekeeperHouseStructure.Capability.INSTANCE)
com/legacy/blue_skies/registries/SkiesStructures.java:178:            .capability(GatekeeperHouseStructure.Capability.INSTANCE)
com/legacy/blue_skies/registries/SkiesStructures.java:194:            .capability(GatekeeperHouseStructure.Capability.INSTANCE)
com/legacy/blue_skies/registries/SkiesStructures.java:212:            .capability(SkiesVillageStructure.Capability.INSTANCE)
com/legacy/blue_skies/registries/SkiesStructures.java:224:            .capability(SkiesVillageStructure.Capability.INSTANCE)
com/legacy/blue_skies/registries/SkiesStructures.java:236:            .capability(SkiesVillageStructure.Capability.INSTANCE)
com/legacy/blue_skies/registries/SkiesStructures.java:248:            .capability(SkiesVillageStructure.Capability.INSTANCE)
com/legacy/blue_skies/registries/SkiesStructures.java:260:            .capability(SkiesVillageStructure.Capability.INSTANCE)
com/legacy/blue_skies/registries/SkiesStructures.java:272:            .capability(SkiesVillageStructure.Capability.INSTANCE)
com/legacy/blue_skies/registries/SkiesStructures.java:69:            .capability(FrozenBunkerStructure.Capability.INSTANCE)
com/legacy/blue_skies/world/everbright/gen/structures/bunker/FrozenBunkerStructure.java:10:import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability
com/legacy/blue_skies/world/everbright/gen/structures/bunker/FrozenBunkerStructure.java:11:import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability
com/legacy/blue_skies/world/everbright/gen/structures/bunker/FrozenBunkerStructure.java:28:   public static class Capability implements JigsawCapability {
com/legacy/blue_skies/world/everbright/gen/structures/bunker/FrozenBunkerStructure.java:29:      public static final FrozenBunkerStructure.Capability INSTANCE =
com/legacy/blue_skies/world/everbright/gen/structures/bunker/FrozenBunkerStructure.java:30:      public static final Codec<FrozenBunkerStructure.Capability> COD
com/legacy/blue_skies/world/everbright/gen/structures/bunker/FrozenBunkerStructure.java:32:      public JigsawCapabilityType<?> getType() {
com/legacy/blue_skies/world/general_features/structures/GatekeeperHouseStructure.java:12:import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability;
com/legacy/blue_skies/world/general_features/structures/GatekeeperHouseStructure.java:13:import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapabilityTy
com/legacy/blue_skies/world/general_features/structures/GatekeeperHouseStructure.java:31:   public static class Capability implements JigsawCapability {
com/legacy/blue_skies/world/general_features/structures/GatekeeperHouseStructure.java:32:      public static final GatekeeperHouseStructure.Capability INSTANCE 
com/legacy/blue_skies/world/general_features/structures/GatekeeperHouseStructure.java:33:      public static final Codec<GatekeeperHouseStructure.Capability> CO
com/legacy/blue_skies/world/general_features/structures/GatekeeperHouseStructure.java:35:      public JigsawCapabilityType<?> getType() {
com/legacy/blue_skies/world/general_features/structures/villages/SkiesVillageStructure.java:11:import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapabi
com/legacy/blue_skies/world/general_features/structures/villages/SkiesVillageStructure.java:12:import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapabi
com/legacy/blue_skies/world/general_features/structures/villages/SkiesVillageStructure.java:38:   public static class Capability implements JigsawCapability {
com/legacy/blue_skies/world/general_features/structures/villages/SkiesVillageStructure.java:39:      public static final SkiesVillageStructure.Capability INSTAN
com/legacy/blue_skies/world/general_features/structures/villages/SkiesVillageStructure.java:40:      public static final Codec<SkiesVillageStructure.Capability>
com/legacy/blue_skies/world/general_features/structures/villages/SkiesVillageStructure.java:42:      public JigsawCapabilityType<?> getType() {

## 类别：MobEffectInstance
com/legacy/blue_skies/blocks/misc/SkyFireBlock.java:16:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/blocks/misc/SkyFireBlock.java:31:   private final Supplier<MobEffectInstance> effect;
com/legacy/blue_skies/blocks/misc/SkyFireBlock.java:35:   public SkyFireBlock(Properties prop, Supplier<MobEffectInstance> effect, Supplier<GelPortalBlock> port
com/legacy/blue_skies/blocks/misc/SkyUpsidedownFireBlock.java:19:   private final Supplier<MobEffectInstance> effect;
com/legacy/blue_skies/blocks/misc/SkyUpsidedownFireBlock.java:21:   public SkyUpsidedownFireBlock(Properties prop, Supplier<MobEffectInstance> effect) {
com/legacy/blue_skies/blocks/misc/SkyUpsidedownFireBlock.java:5:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/blocks/natural/BanefulMushroomBlock.java:55:      if (entity instanceof LivingEntity living && living.canBeAffected(new MobEffectInstance(
com/legacy/blue_skies/blocks/natural/BanefulMushroomBlock.java:68:            living.addEffect(new MobEffectInstance(MobEffects.POISON, durration, amplifier));
com/legacy/blue_skies/blocks/natural/BanefulMushroomBlock.java:9:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/capability/SkiesPlayer.java:137:            this.player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80));
com/legacy/blue_skies/capability/SkiesPlayer.java:138:            this.player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
com/legacy/blue_skies/capability/SkiesPlayer.java:139:            this.player.addEffect(new MobEffectInstance(MobEffects.POISON, 50, 1));
com/legacy/blue_skies/capability/SkiesPlayer.java:41:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/client/init/SkiesColoring.java:118:            List<MobEffectInstance> effects = preparedFood.getEffects();
com/legacy/blue_skies/client/init/SkiesColoring.java:15:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:143:   public boolean canBeAffected(MobEffectInstance effect) {
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:232:            arrow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:312:               players.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:36:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:753:               new MobEffectInstance(
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:1009:                  players.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 0, t
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:1128:                  venomBomb.effect = new MobEffectInstance(SkiesEffects.DEADLY_VENOM, 50,
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:290:                  rider.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1));
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:53:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:557:   public boolean canBeAffected(MobEffectInstance potioneffectIn) {
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:792:         venomBomb.effect = new MobEffectInstance(SkiesEffects.DEADLY_VENOM, 100, this.get
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:248:   public boolean canBeAffected(MobEffectInstance effect) {
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:59:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:116:   public boolean canBeAffected(MobEffectInstance effect) {
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:252:               new MobEffectInstance(
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:29:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/entities/hostile/boss/summons/NestedSpiderEntity.java:14:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/entities/hostile/boss/summons/NestedSpiderEntity.java:71:   public boolean canBeAffected(MobEffectInstance potioneffectIn) {
com/legacy/blue_skies/entities/hostile/ShrumptyEntity.java:11:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/entities/hostile/ShrumptyEntity.java:137:         ((LivingEntity)entityIn).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 4
com/legacy/blue_skies/entities/hostile/VenomSpiderEntity.java:119:            this.addEffect(new MobEffectInstance(effect, Integer.MAX_VALUE));
com/legacy/blue_skies/entities/hostile/VenomSpiderEntity.java:193:   public boolean canBeAffected(MobEffectInstance potioneffectIn) {
com/legacy/blue_skies/entities/hostile/VenomSpiderEntity.java:236:            ((LivingEntity)entityIn).addEffect(new MobEffectInstance(MobEffects.POISON, durrat
com/legacy/blue_skies/entities/hostile/VenomSpiderEntity.java:25:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/entities/passive/fish/JellyDrifterEntity.java:179:            ((LivingEntity)entityIn).addEffect(new MobEffectInstance(MobEffects.POISON, 
com/legacy/blue_skies/entities/passive/fish/JellyDrifterEntity.java:25:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/entities/projectile/VenomBombEntity.java:100:                        livingentity.addEffect(new MobEffectInstance(effect.getEffect(), i, e
com/legacy/blue_skies/entities/projectile/VenomBombEntity.java:109:   private void makeAreaOfEffectCloud(MobEffectInstance effect) {
com/legacy/blue_skies/entities/projectile/VenomBombEntity.java:10:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/entities/projectile/VenomBombEntity.java:23:   public MobEffectInstance effect;
com/legacy/blue_skies/entities/projectile/VenomBombEntity.java:84:   private void applyEffectToNearbyMobs(MobEffectInstance effect, @Nullable Entity entityIn) {
com/legacy/blue_skies/entities/projectile/VenomSpitEntity.java:140:               living.addEffect(new MobEffectInstance(SkiesEffects.DEADLY_VENOM, durration, a
com/legacy/blue_skies/entities/projectile/VenomSpitEntity.java:14:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/entities/projectile/VenomSpitEntity.java:156:               living.addEffect(new MobEffectInstance(MobEffects.POISON, durration, amplifier
com/legacy/blue_skies/entities/villager/GatekeeperEntity.java:48:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/entities/villager/GatekeeperEntity.java:811:         GatekeeperEntity.this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 23, 5,
com/legacy/blue_skies/events/SkiesEvents.java:110:         MobEffectInstance effect = entity.getEffect(SkiesEffects.DEADLY_VENOM);
com/legacy/blue_skies/events/SkiesEvents.java:307:            arrow.addEffect(new MobEffectInstance(MobEffects.POISON, 300, 2));
com/legacy/blue_skies/events/SkiesEvents.java:308:            arrow.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200));
com/legacy/blue_skies/events/SkiesEvents.java:40:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/items/arcs/LifeArcItem.java:21:         List<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects());
com/legacy/blue_skies/items/arcs/LifeArcItem.java:7:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/items/tools/weapons/SkySwordItem.java:15:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/items/tools/weapons/SkySwordItem.java:46:         target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 3));
com/legacy/blue_skies/items/util/FoodPrepUtils.java:184:         List<MobEffectInstance> effects = new ArrayList<>();
com/legacy/blue_skies/items/util/FoodPrepUtils.java:186:            effects.add(new MobEffectInstance(MobEffects.POISON, 180, entity.getRandom().nextBoolean() ?
com/legacy/blue_skies/items/util/FoodPrepUtils.java:189:               new MobEffectInstance(
com/legacy/blue_skies/items/util/FoodPrepUtils.java:197:               effects.add(new MobEffectInstance(MobEffects.HARM, 1, entity.getRandom().nextBoolean() ? 
com/legacy/blue_skies/items/util/FoodPrepUtils.java:199:               effects.add(new MobEffectInstance(MobEffects.WITHER, 200, entity.getRandom().nextBoolean(
com/legacy/blue_skies/items/util/FoodPrepUtils.java:22:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/items/util/FoodPrepUtils.java:239:   private static List<MobEffectInstance> getEffects(List<ItemStack> ingredients, int maxUses) {
com/legacy/blue_skies/items/util/FoodPrepUtils.java:240:      Map<MobEffect, MobEffectInstance> effectMap = new LinkedHashMap<>();
com/legacy/blue_skies/items/util/FoodPrepUtils.java:250:            for (MobEffectInstance effect : (List)ingredient.getFoodProperties(null).getEffects().stream
com/legacy/blue_skies/items/util/FoodPrepUtils.java:264:      List<MobEffectInstance> effects = new ArrayList<>();
com/legacy/blue_skies/items/util/FoodPrepUtils.java:266:      for (Entry<MobEffect, MobEffectInstance> entry : effectMap.entrySet()) {
com/legacy/blue_skies/items/util/FoodPrepUtils.java:267:         MobEffectInstance effect = entry.getValue();
com/legacy/blue_skies/items/util/FoodPrepUtils.java:271:            effects.add(new MobEffectInstance(effect.getEffect(), Math.round(time), effect.getAmplifier(
com/legacy/blue_skies/items/util/FoodPrepUtils.java:278:   private static void addEffect(Map<MobEffect, MobEffectInstance> map, MobEffectInstance newEffect) {
com/legacy/blue_skies/items/util/FoodPrepUtils.java:280:         MobEffectInstance oldEffect = map.get(newEffect.getEffect());
com/legacy/blue_skies/items/util/FoodPrepUtils.java:283:            map.put(newEffect.getEffect(), new MobEffectInstance(newEffect.getEffect(), duration, newEff
com/legacy/blue_skies/items/util/FoodPrepUtils.java:286:            map.put(newEffect.getEffect(), new MobEffectInstance(newEffect.getEffect(), duration, oldEff
com/legacy/blue_skies/items/util/FoodPrepUtils.java:289:            map.put(newEffect.getEffect(), new MobEffectInstance(newEffect.getEffect(), duration, newEff
com/legacy/blue_skies/items/util/FoodPrepUtils.java:292:         map.put(newEffect.getEffect(), new MobEffectInstance(newEffect.getEffect(), newEffect.getDurati
com/legacy/blue_skies/items/util/FoodPrepUtils.java:56:      List<MobEffectInstance> effects = new ArrayList<>();
com/legacy/blue_skies/items/util/FoodPrepUtils.java:80:               MobEffectInstance effect = null;
com/legacy/blue_skies/items/util/FoodPrepUtils.java:83:                  effect = MobEffectInstance.load((CompoundTag)nbt);
com/legacy/blue_skies/items/util/PreparedFood.java:15:   public PreparedFood(List<ItemStack> ingredients, List<MobEffectInstance> effects, int totalHunger, int 
com/legacy/blue_skies/items/util/PreparedFood.java:28:   public List<MobEffectInstance> getEffects() {
com/legacy/blue_skies/items/util/PreparedFood.java:4:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/items/util/PreparedFood.java:9:   private final List<MobEffectInstance> effects;
com/legacy/blue_skies/registries/SkiesBlocks.java:1001:            () -> new MobEffectInstance(MobEffects.LEVITATION, 20),
com/legacy/blue_skies/registries/SkiesBlocks.java:1010:            () -> new MobEffectInstance(MobEffects.BLINDNESS, 60),
com/legacy/blue_skies/registries/SkiesBlocks.java:1016:         "upsidedown_blue_fire", new SkyUpsidedownFireBlock(Properties.ofFullCopy(blue_fire), () -> new M
com/legacy/blue_skies/registries/SkiesBlocks.java:1019:         "upsidedown_black_fire", new SkyUpsidedownFireBlock(Properties.ofFullCopy(black_fire), () -> new
com/legacy/blue_skies/registries/SkiesBlocks.java:103:import net.minecraft.world.effect.MobEffectInstance;
com/legacy/blue_skies/registries/SkiesFoods.java:14:      .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 40, 2), 100.0F)
com/legacy/blue_skies/registries/SkiesFoods.java:27:      .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0), 0.05F)
com/legacy/blue_skies/registries/SkiesFoods.java:3:import net.minecraft.world.effect.MobEffectInstance;

## 类别：CreativeModeTab
com/legacy/blue_skies/client/gui/screen/widgets/InventoryTabButton.java:24:import net.minecraft.world.item.CreativeModeTab;
com/legacy/blue_skies/client/gui/screen/widgets/InventoryTabButton.java:25:import net.minecraft.world.item.CreativeModeTabs;
com/legacy/blue_skies/client/gui/screen/widgets/InventoryTabButton.java:63:         Optional<ResourceKey<CreativeModeTab>> key = BuiltInRegistries.CREATIVE_MODE
com/legacy/blue_skies/client/gui/screen/widgets/InventoryTabButton.java:64:         this.shouldDraw = key.isPresent() && key.get().equals(CreativeModeTabs.INVEN
com/legacy/blue_skies/client/gui/screen/widgets/SimpleInventoryTabButton.java:23:import net.minecraft.world.item.CreativeModeTab;
com/legacy/blue_skies/client/gui/screen/widgets/SimpleInventoryTabButton.java:24:import net.minecraft.world.item.CreativeModeTabs;
com/legacy/blue_skies/client/gui/screen/widgets/SimpleInventoryTabButton.java:58:      Optional<ResourceKey<CreativeModeTab>> selectedTab = BuiltInRegistries.CR
com/legacy/blue_skies/client/gui/screen/widgets/SimpleInventoryTabButton.java:66:         this.shouldDraw = selectedTab.isPresent() && selectedTab.get().equals(
com/legacy/blue_skies/items/arcs/ArcItem.java:26:import net.minecraft.world.item.CreativeModeTab.Output;
com/legacy/blue_skies/items/arcs/ArcItem.java:27:import net.minecraft.world.item.CreativeModeTab.TabVisibility;
com/legacy/blue_skies/items/BossLootBagItem.java:10:import net.minecraft.world.item.CreativeModeTab.Output;
com/legacy/blue_skies/items/BossLootBagItem.java:11:import net.minecraft.world.item.CreativeModeTab.TabVisibility;
com/legacy/blue_skies/items/LootBagItem.java:24:import net.minecraft.world.item.CreativeModeTab.Output;
com/legacy/blue_skies/items/LootBagItem.java:25:import net.minecraft.world.item.CreativeModeTab.TabVisibility;
com/legacy/blue_skies/items/util/IRarityItem.java:11:import net.minecraft.world.item.CreativeModeTab.Output;
com/legacy/blue_skies/items/util/IRarityItem.java:12:import net.minecraft.world.item.CreativeModeTab.TabVisibility;
com/legacy/blue_skies/items/util/ISpecialTabItem.java:3:import net.minecraft.world.item.CreativeModeTab.Output;
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:14:import net.minecraft.world.item.CreativeModeTab;
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:15:import net.minecraft.world.item.CreativeModeTabs;
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:19:import net.minecraft.world.item.CreativeModeTab.TabVisibility;
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:26:import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:47:   public static void modifyExisting(BuildCreativeModeTabContentsEvent event) {
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:48:      if (event.getTabKey().equals(CreativeModeTabs.SPAWN_EGGS)) {
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:59:         () -> CreativeModeTab.builder()
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:734:   protected static void printMissingItems(BuildCreativeModeTabContentsEvent event) {

## 类别：Level.random
com/legacy/blue_skies/block_entity/BagOfSpoilsBlockEntity.java:90:               this.level.random.nextFloat() * 0.1F + 1.0F
com/legacy/blue_skies/block_entity/HorizoniteForgeBlockEntity.java:402:      if (f != 0.0F && Math.random() < f) {
com/legacy/blue_skies/blocks/dungeon/KeystoneBlock.java:141:               EntityUtil.getSoundPitchWithStart(worldIn.random, 1.0F),
com/legacy/blue_skies/blocks/dungeon/SkyDecayingSpikeBlock.java:61:         int i = level.random.nextInt(100) + 60;
com/legacy/blue_skies/blocks/misc/SkyFireBlock.java:63:            world.scheduleTick(pos, this, getTickCooldown(world.random));
com/legacy/blue_skies/blocks/misc/SkyFireBlock.java:69:      level.scheduleTick(pos, this, getTickCooldown(level.random));
com/legacy/blue_skies/blocks/misc/StarFlareBlock.java:55:         this.animateTick(state, level, pos, level.random);
com/legacy/blue_skies/blocks/natural/BanefulMushroomBlock.java:63:            block.animateTick(level.getBlockState(pos), level, pos, level.random);
com/legacy/blue_skies/blocks/natural/BrewberryBushBlock.java:78:      int count = level.random.nextInt(2) + 1;
com/legacy/blue_skies/blocks/natural/BrewberryBushBlock.java:80:         count += level.random.nextInt(3);
com/legacy/blue_skies/blocks/util/DispenserBehaviors.java:119:                        if (stack.hurt(1, world.random, null)) {
com/legacy/blue_skies/client/audio/ambient/DungeonAmbientSoundHandler.java:34:               float f = this.player.level().random.nextFloat();
com/legacy/blue_skies/client/audio/ambient/DungeonAmbientSoundHandler.java:37:                  float pitch = (this.player.level().random.nextFloat() - this.pla
com/legacy/blue_skies/client/audio/ambient/DungeonAmbientSoundHandler.java:51:               float f = this.player.level().random.nextFloat();
com/legacy/blue_skies/client/audio/ambient/SnowstormAmbientSoundHandler.java:28:               float f = this.player.level().random.nextFloat();
com/legacy/blue_skies/client/gui/screen/journal/BlueJournalScreen.java:262:      codeMotionX = Math.random() < 0.5 ? 1 : -1;
com/legacy/blue_skies/client/gui/screen/journal/BlueJournalScreen.java:263:      codeMotionY = Math.random() < 0.5 ? 1 : -1;
com/legacy/blue_skies/client/particle/BriskPetalParticle.java:22:      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/BriskPetalParticle.java:23:      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/BriskPetalParticle.java:24:      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/BriskPetalParticle.java:25:      this.lifetime = (int)(16.0 / (Math.random() * 0.3 + 0.2)) + 40;
com/legacy/blue_skies/client/particle/BriskPetalParticle.java:26:      this.roll = (float)(Math.random() * Math.PI * 2.0);
com/legacy/blue_skies/client/particle/BriskPetalParticle.java:27:      this.rotationSpeed = (float)(Math.random() / 20.0);
com/legacy/blue_skies/client/particle/DuskParticle.java:22:      this.lifetime = (int)(7.0 / (Math.random() * 0.8 + 0.2)) + 4;
com/legacy/blue_skies/client/particle/DustParticle.java:25:      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/DustParticle.java:26:      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/DustParticle.java:27:      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/DustParticle.java:28:      this.quadSize = (this.random.nextFloat() + 0.5F) * 0.5F;
com/legacy/blue_skies/client/particle/EverbrightPortalParticle.java:26:      this.quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);
com/legacy/blue_skies/client/particle/EverbrightPortalParticle.java:27:      float f = this.random.nextFloat() + 0.8F;
com/legacy/blue_skies/client/particle/EverbrightPortalParticle.java:31:      this.lifetime = (int)(Math.random() * 10.0) + 60;
com/legacy/blue_skies/client/particle/EverdawnPortalParticle.java:26:      this.quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);
com/legacy/blue_skies/client/particle/EverdawnPortalParticle.java:27:      float f = this.random.nextFloat() + 0.8F;
com/legacy/blue_skies/client/particle/EverdawnPortalParticle.java:31:      this.lifetime = (int)(Math.random() * 10.0) + 60;
com/legacy/blue_skies/client/particle/FallingLeafParticle.java:22:      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/FallingLeafParticle.java:23:      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/FallingLeafParticle.java:24:      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/FallingLeafParticle.java:25:      this.lifetime = (int)(8.0 / (Math.random() * 0.3 + 0.2)) + 14;
com/legacy/blue_skies/client/particle/FallingLeafParticle.java:26:      this.roll = (float)(Math.random() * Math.PI * 2.0);
com/legacy/blue_skies/client/particle/FallingLeafParticle.java:27:      this.rotationSpeed = (float)(Math.random() / 20.0);
com/legacy/blue_skies/client/particle/FluctuantSphereBeamParticle.java:21:      this.x = x + Math.random() / 100.0;
com/legacy/blue_skies/client/particle/FluctuantSphereBeamParticle.java:23:      this.z = z + Math.random() / 100.0;
com/legacy/blue_skies/client/particle/FluctuantSphereBeamParticle.java:28:      this.quadSize = (float)(Math.random() / 10.0) + 0.2F;
com/legacy/blue_skies/client/particle/FluctuantSphereBeamParticle.java:29:      this.roll = (float)(Math.random() * Math.PI * 2.0);
com/legacy/blue_skies/client/particle/FluctuantSphereBeamParticle.java:30:      this.rotationSpeed = (float)(Math.random() / 10.0);
com/legacy/blue_skies/client/particle/FluctuantSphereParticle.java:21:      this.x = x + Math.random() / 100.0;
com/legacy/blue_skies/client/particle/FluctuantSphereParticle.java:23:      this.z = z + Math.random() / 100.0;
com/legacy/blue_skies/client/particle/FluctuantSphereParticle.java:29:      this.roll = (float)(Math.random() * Math.PI * 2.0);
com/legacy/blue_skies/client/particle/FluctuantSphereParticle.java:30:      this.rotationSpeed = (float)(Math.random() / 10.0);
com/legacy/blue_skies/client/particle/FroseSnowParticle.java:18:      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/FroseSnowParticle.java:19:      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/FroseSnowParticle.java:20:      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/FroseSnowParticle.java:21:      this.quadSize = this.random.nextFloat() * 0.04F;
com/legacy/blue_skies/client/particle/PoisonParticle.java:24:      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
com/legacy/blue_skies/client/particle/PoisonParticle.java:25:      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
com/legacy/blue_skies/client/particle/PoisonParticle.java:26:      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
com/legacy/blue_skies/client/particle/PoisonParticle.java:27:      this.lifetime = (int)(7.0 / (Math.random() * 0.8 + 0.2)) + 4;
com/legacy/blue_skies/client/particle/PoisonParticle.java:63:                  LogicUtil.plusOrMinus(range, this.random),
com/legacy/blue_skies/client/particle/PoisonParticle.java:64:                  LogicUtil.plusOrMinus(range, this.random),
com/legacy/blue_skies/client/particle/PoisonParticle.java:65:                  LogicUtil.plusOrMinus(range, this.random)
com/legacy/blue_skies/client/particle/SkyFlameParticle.java:23:      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
com/legacy/blue_skies/client/particle/SkyFlameParticle.java:24:      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
com/legacy/blue_skies/client/particle/SkyFlameParticle.java:25:      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
com/legacy/blue_skies/client/particle/SkyFlameParticle.java:26:      this.lifetime = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
com/legacy/blue_skies/client/particle/StarFlareParticle.java:26:      this.lifetime = (int)(14.0 / (Math.random() * 0.8 + 0.2)) + 4;
com/legacy/blue_skies/client/particle/StarFlareParticle.java:63:      float s = (color == DyeColor.BLACK ? 0.9F : 1.15F) + (float)(Math.random() * 0.1);
com/legacy/blue_skies/client/particle/StarGlimmerParticle.java:15:      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/StarGlimmerParticle.java:16:      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/StarGlimmerParticle.java:17:      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
com/legacy/blue_skies/client/particle/StarGlimmerParticle.java:18:      this.lifetime = (int)(8.0 / (Math.random() * 0.3 + 0.2)) + 14;
com/legacy/blue_skies/client/renders/entities/hostile/boss/StarlitCrusherRenderer.java:36:         ? new Vec3(entityIn.level().random.nextGaussian() * 0.02, -(e
com/legacy/blue_skies/entities/BossItemEntity.java:30:      this.setYRot(this.random.nextFloat() * 360.0F);
com/legacy/blue_skies/entities/BossItemEntity.java:31:      this.setDeltaMovement(this.random.nextDouble() * 0.2 - 0.1, 0.2, this.random.nextDouble() * 0.2 - 0.
com/legacy/blue_skies/entities/hostile/ArmoredFrostSpiritEntity.java:126:      boolean randomBoolean = this.level().isRaining() ? true : this.random.nextBoolean
com/legacy/blue_skies/entities/hostile/ArmoredFrostSpiritEntity.java:208:      return (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.0F;
com/legacy/blue_skies/entities/hostile/ArmoredFrostSpiritEntity.java:214:            this.setIceType(this.random.nextInt(4) == 0 ? 1 : 0);
com/legacy/blue_skies/entities/hostile/ArmoredFrostSpiritEntity.java:217:            this.setIceType(this.random.nextInt(2));
com/legacy/blue_skies/entities/hostile/ArmoredFrostSpiritEntity.java:220:            this.setIceType(this.random.nextInt(4) == 0 ? 0 : 1);
com/legacy/blue_skies/entities/hostile/ArmoredFrostSpiritEntity.java:93:      this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * f);
com/legacy/blue_skies/entities/hostile/ArmoredFrostSpiritEntity.java:95:         .addPermanentModifier(new AttributeModifier("Random spawn bonus", this.random.n
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:164:         double dx = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:165:         double dy = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:166:         double dz = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:170:               this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getB
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:171:               this.getY() + this.random.nextFloat() * this.getBbHeight(),
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:172:               this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getB
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:231:         if (this.getDifficultyID() >= ISkyBoss.BossLevel.GOLD.getId() && this.random.nextI
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:261:      return (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.0F;
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:302:         return super.canUse() && this.boss.isAwayFromPlayers() && !this.boss.isBossInvul()
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:592:            && (this.boss.random.nextBoolean() || this.boss.getTarget() != null && this.bos
com/legacy/blue_skies/entities/hostile/boss/AlchemistEntity.java:694:         return super.canUse() && AlchemistEntity.this.isAwayFromPlayers() && AlchemistEnti
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:1025:                     if (funny.getBlock() instanceof SpiderNestBlock && this.boss.level()
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:356:            if (this.getTimeOnCeiling() == 320 && this.random.nextBoolean()) {
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:382:                           this.getX() + (this.random.nextFloat() - 0.5) * this.getBbWidth
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:384:                           this.getZ() + (this.random.nextFloat() - 0.5) * this.getBbWidth
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:385:                           4.0 * (this.random.nextFloat() - 0.5),
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:387:                           (this.random.nextFloat() - 0.5) * 4.0
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:398:                        this.getX() + this.level().random.nextFloat() * this.getBbWidth() 
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:399:                        this.getY() + this.level().random.nextFloat() * this.getBbHeight()
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:400:                        this.getZ() + this.level().random.nextFloat() * this.getBbWidth() 
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:600:         this.setTimeOnCeiling(this.getTicksUntilNextScreech() == 0 && this.isSecondPhase(
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:771:         this.playSound(SkiesSounds.ENTITY_ARACHNARCH_SPIT, this.getSoundVolume(), 1.0F + 
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:789:         this.playSound(SkiesSounds.ENTITY_ARACHNARCH_SPIT, this.getSoundVolume(), 1.0F + 
com/legacy/blue_skies/entities/hostile/boss/ArachnarchEntity.java:905:            if (players != this.boss.getTarget() && this.boss.random.nextBoolean() && play
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1059:               && this.boss.random.nextBoolean()
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1154:         this.boss.randomizeAttackTarget(this.boss.random.nextBoolean());
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1262:         this.boss.randomizeAttackTarget(this.boss.random.nextBoolean());
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1305:                  owl.setTarget((LivingEntity)this.boss.getPlayersInDungeonArea().get
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1314:            this.boss.playSound(SoundEvents.ITEM_PICKUP, this.boss.getSoundVolume(), 
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1360:         this.boss.randomizeAttackTarget(this.boss.random.nextBoolean());
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1509:         this.boss.randomizeAttackTarget(this.boss.random.nextBoolean());
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1670:               && this.boss.random.nextBoolean()
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1702:                           this.boss.getX() + Mth.cos(f2) * distance + (world.random.
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1703:                           this.boss.getZ() + Mth.sin(f2) * distance + (world.random.
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1719:                           this.boss.getX() + Mth.cos(f2) * distance + (world.random.
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1720:                           this.boss.getZ() + Mth.sin(f2) * distance + (world.random.
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1736:                           this.boss.getX() + Mth.cos(f2) * distance + (world.random.
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1737:                           this.boss.getZ() + Mth.sin(f2) * distance + (world.random.
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1753:                           this.boss.getX() + Mth.cos(f2) * distance + (world.random.
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:1754:                           this.boss.getZ() + Mth.sin(f2) * distance + (world.random.
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:363:            if (this.getDifficultyID() != ISkyBoss.BossLevel.PLATINUM.getId() && this.
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:365:               this.heal(this.random.nextInt(2) + 1);
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:550:               playerItem.hurt(this.random.nextInt(5) + 5, this.random, (ServerPlayer)
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:959:               || this.getDifficultyID() == ISkyBoss.BossLevel.BRONZE.getId() && this.
com/legacy/blue_skies/entities/hostile/boss/StarlitCrusherEntity.java:993:            1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:180:      return (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 0.9F;
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:400:            : super.canUse() && this.boss.isAwayFromPlayers() && !this.boss.isBossInvul() &&
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:424:                  pos.getX() + -6 + this.boss.random.nextInt(12), pos.getY(), pos.getZ() + -
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:448:                     (LivingEntity)this.boss.getPlayersInDungeonArea().get(this.boss.level()
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:458:                     double dx = this.boss.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:459:                     double dy = this.boss.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:460:                     double dz = this.boss.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:464:                           golem.getX() + this.boss.random.nextFloat() * golem.getBbWidth() 
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:465:                           golem.getY() + this.boss.random.nextFloat() * golem.getBbHeight()
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:466:                           golem.getZ() + this.boss.random.nextFloat() * golem.getBbWidth() 
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:522:                  pos.getX() + -6 + this.boss.random.nextInt(12), pos.getY(), pos.getZ() + -
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:551:                     (LivingEntity)this.boss.getPlayersInDungeonArea().get(this.boss.level()
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:561:                     double dx = this.boss.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:562:                     double dy = this.boss.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:563:                     double dz = this.boss.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:567:                           golem.getX() + this.boss.random.nextFloat() * golem.getBbWidth() 
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:568:                           golem.getY() + this.boss.random.nextFloat() * golem.getBbHeight()
com/legacy/blue_skies/entities/hostile/boss/SummonerEntity.java:569:                           golem.getZ() + this.boss.random.nextFloat() * golem.getBbWidth() 
com/legacy/blue_skies/entities/hostile/boss/summons/ArtificialGolemEntity.java:283:      return EntityUtil.getSoundPitchWithStart(this.random, 0.9F, 0.15F);
com/legacy/blue_skies/entities/hostile/boss/summons/ArtificialGolemEntity.java:539:               this.entity.performRangedAttack(livingentity, BowItem.getPower
com/legacy/blue_skies/entities/hostile/boss/summons/ArtificialGolemEntity.java:87:         float chance = this.level().random.nextFloat();
com/legacy/blue_skies/entities/hostile/boss/summons/ent/EntRootEntity.java:124:         this.playSound(SoundEvents.CROP_BREAK, 1.0F, this.random.nextFloat() * 0
com/legacy/blue_skies/entities/hostile/boss/summons/ent/EntRootEntity.java:155:         this.playSound(SoundEvents.CHORUS_FLOWER_GROW, 1.0F, this.random.nextFlo
com/legacy/blue_skies/entities/hostile/boss/summons/NestedSpiderEntity.java:68:      return EntityUtil.getSoundPitchWithStart(this.random, 1.5F, 0.2F);
com/legacy/blue_skies/entities/hostile/boss/summons/StrangeLightningEntity.java:118:            BlockPos blockpos1 = blockpos.offset(this.random.nextInt(3) - 1,
com/legacy/blue_skies/entities/hostile/boss/summons/StrangeLightningEntity.java:40:      this.boltVertex = this.random.nextLong();
com/legacy/blue_skies/entities/hostile/boss/summons/StrangeLightningEntity.java:41:      this.boltLivingTime = this.random.nextInt(3) + 1;
com/legacy/blue_skies/entities/hostile/boss/summons/StrangeLightningEntity.java:69:               0.8F + this.random.nextFloat() * 0.2F
com/legacy/blue_skies/entities/hostile/boss/summons/StrangeLightningEntity.java:80:               0.5F + this.random.nextFloat() * 0.2F
com/legacy/blue_skies/entities/hostile/boss/summons/StrangeLightningEntity.java:88:         } else if (this.lightningState < -this.random.nextInt(10)) {
com/legacy/blue_skies/entities/hostile/boss/summons/StrangeLightningEntity.java:91:            this.boltVertex = this.random.nextLong();
com/legacy/blue_skies/entities/hostile/CrynocerousEntity.java:64:      return EntityUtil.getSoundPitchWithStart(this.random, 1.0F);
com/legacy/blue_skies/entities/hostile/DiophydeProwlerEntity.java:149:         this.chargeTime = 60 + this.attacker.random.nextInt(40);
com/legacy/blue_skies/entities/hostile/EmberbackEntity.java:204:      boolean randomBoolean = this.level().isRaining() ? true : this.random.nextBoolean();
com/legacy/blue_skies/entities/hostile/EmberbackEntity.java:230:               living.setSecondsOnFire(1 + this.level().random.nextInt(5));
com/legacy/blue_skies/entities/hostile/EmberbackEntity.java:291:               double xSpeed = this.random.nextGaussian() * 0.2;
com/legacy/blue_skies/entities/hostile/EmberbackEntity.java:292:               double ySpeed = this.random.nextGaussian() * 0.2;
com/legacy/blue_skies/entities/hostile/EmberbackEntity.java:293:               double zSpeed = this.random.nextGaussian() * 0.2;
com/legacy/blue_skies/entities/hostile/FrostSpiritEntity.java:131:      if (this.level().isClientSide && this.random.nextFloat() < 0.5F) {
com/legacy/blue_skies/entities/hostile/FrostSpiritEntity.java:141:      double x = LogicUtil.plusOrMinus(this.getX(), offset, this.random);
com/legacy/blue_skies/entities/hostile/FrostSpiritEntity.java:142:      double y = LogicUtil.plusOrMinus(this.getY(), offset, this.random);
com/legacy/blue_skies/entities/hostile/FrostSpiritEntity.java:143:      double z = LogicUtil.plusOrMinus(this.getZ(), offset, this.random);
com/legacy/blue_skies/entities/hostile/FrostSpiritEntity.java:145:      double dx = LogicUtil.plusOrMinus(motion, this.random);
com/legacy/blue_skies/entities/hostile/FrostSpiritEntity.java:146:      double dy = LogicUtil.plusOrMinus(motion, this.random);
com/legacy/blue_skies/entities/hostile/FrostSpiritEntity.java:147:      double dz = LogicUtil.plusOrMinus(motion, this.random);
com/legacy/blue_skies/entities/hostile/FrostSpiritEntity.java:88:      if (this.spawnPosition == null || this.random.nextInt(30) == 0 || this.spawnPosition.clos
com/legacy/blue_skies/entities/hostile/FrostSpiritEntity.java:90:            (int)this.getX() + this.random.nextInt(7) - this.random.nextInt(7),
com/legacy/blue_skies/entities/hostile/FrostSpiritEntity.java:91:            (int)this.getY() + this.random.nextInt(6) - 2,
com/legacy/blue_skies/entities/hostile/FrostSpiritEntity.java:92:            (int)this.getZ() + this.random.nextInt(7) - this.random.nextInt(7)
com/legacy/blue_skies/entities/hostile/NyctoflyEntity.java:168:      boolean randomBoolean = this.level().isRaining() ? true : this.random.nextBoolean();
com/legacy/blue_skies/entities/hostile/PolargeistEntity.java:169:      boolean randomBoolean = this.level().isRaining() ? true : this.random.nextBoolean();
com/legacy/blue_skies/entities/hostile/SeclamEntity.java:100:               double dx = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/hostile/SeclamEntity.java:101:               double dy = this.random.nextFloat() * 0.5F + this.random.nextGaussian() * 0.04;
com/legacy/blue_skies/entities/hostile/SeclamEntity.java:102:               double dz = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/hostile/SeclamEntity.java:106:                     0.8F + this.getX() + this.random.nextFloat() * this.getBbWidth() - this.getBbW
com/legacy/blue_skies/entities/hostile/SeclamEntity.java:108:                     0.9F + this.getZ() + this.random.nextFloat() * this.getBbWidth() - this.getBbW
com/legacy/blue_skies/entities/hostile/SeclamEntity.java:98:            && (this.isOpen() && this.openAnim >= 2.0F && this.openAnim <= 4.0F || this.isOpen() && 
com/legacy/blue_skies/entities/hostile/SpewterEntity.java:211:         this.playSound(SkiesSounds.ENTITY_SPEWTER_SPIT, 1.0F, 1.0F + (this.random.nextFloat() - t
com/legacy/blue_skies/entities/hostile/StoneletEntity.java:72:      this.setLeftHanded(this.random.nextFloat() >= 0.05F);
com/legacy/blue_skies/entities/hostile/VenomSpiderEntity.java:165:      boolean randomBoolean = this.level().isRaining() ? true : this.random.nextBoolean();
com/legacy/blue_skies/entities/hostile/VenomSpiderEntity.java:227:      this.playSound(SkiesSounds.ENTITY_VENOM_SPIDER_SPIT, 1.0F, 1.0F + (this.random.nextFloat
com/legacy/blue_skies/entities/passive/AzulfoEntity.java:112:      if (this.isHerdLeader() && this.level().random.nextInt(200) == 1) {
com/legacy/blue_skies/entities/passive/CosmicFoxEntity.java:105:         if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {
com/legacy/blue_skies/entities/passive/CrogreEntity.java:216:               EntityUtil.playMovingSound(this.getSwimSound(), this, 0.1F, 1.0F + (this.random.next
com/legacy/blue_skies/entities/passive/CrogreEntity.java:320:                           EntityUtil.playMovingSound(this.getSwimSound(), this, 0.1F, 1.0F + (this
com/legacy/blue_skies/entities/passive/CrogreEntity.java:335:                           player, this, this.getSwimSound(), SoundSource.NEUTRAL, 0.1F, 1.0F + (th
com/legacy/blue_skies/entities/passive/CrogreEntity.java:368:               if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {
com/legacy/blue_skies/entities/passive/CrystalCamelEntity.java:183:            if (this.random.nextInt(5) == 0 && !EventHooks.onAnimalTame(this, player)) {
com/legacy/blue_skies/entities/passive/fish/JellyDrifterEntity.java:108:         this.setColor(JellyDrifterEntity.Colors.getRandom(this.random).getId());
com/legacy/blue_skies/entities/passive/ReindeerEntity.java:135:            if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {
com/legacy/blue_skies/entities/passive/ReindeerEntity.java:191:      this.setFestiveStatus(!compound.contains("FestiveStatus") ? (this.random.nextFloat() < 0.4F
com/legacy/blue_skies/entities/passive/ReindeerEntity.java:76:      this.setFestiveStatus(this.random.nextFloat() < 0.4F ? 1 : 0);
com/legacy/blue_skies/entities/passive/ShadeMonitorEntity.java:107:      this.setFestiveStatus(!compound.contains("FestiveStatus") ? (this.random.nextFloat() < 
com/legacy/blue_skies/entities/passive/ShadeMonitorEntity.java:149:      if (this.random.nextFloat() < 0.05) {
com/legacy/blue_skies/entities/passive/ShadeMonitorEntity.java:153:         biome = this.random.nextBoolean() ? ((ShadeMonitorEntity)ageable).getBiome() : this.
com/legacy/blue_skies/entities/passive/ShadeMonitorEntity.java:94:      this.setFestiveStatus(this.random.nextFloat() < 0.4F ? 1 : 0);
com/legacy/blue_skies/entities/passive/SnowOwlEntity.java:195:         return this.random.nextFloat() < 0.03F && BlueSkiesConfig.COMMON.isChristmas() ? SkiesSou
com/legacy/blue_skies/entities/passive/SnowOwlEntity.java:212:      return EntityUtil.getSoundPitchWithStart(this.random, 1.0F);
com/legacy/blue_skies/entities/passive/StardustRamEntity.java:85:         ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.3F
com/legacy/blue_skies/entities/passive/StardustRamEntity.java:86:         : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.8F;
com/legacy/blue_skies/entities/projectile/FluctuantSphereEntity.java:158:         double d0 = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/projectile/FluctuantSphereEntity.java:159:         double d1 = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/projectile/FluctuantSphereEntity.java:160:         double d2 = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/projectile/FluctuantSphereEntity.java:164:            this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.get
com/legacy/blue_skies/entities/projectile/FluctuantSphereEntity.java:165:            this.getY() + this.random.nextFloat() * this.getBbHeight() - d1 * d3,
com/legacy/blue_skies/entities/projectile/FluctuantSphereEntity.java:166:            this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.get
com/legacy/blue_skies/entities/projectile/SeedBombEntity.java:52:      float dx = (this.random.nextFloat() - 0.5F) / 7.0F;
com/legacy/blue_skies/entities/projectile/SeedBombEntity.java:53:      float dy = this.random.nextFloat() / 10.0F;
com/legacy/blue_skies/entities/projectile/SeedBombEntity.java:54:      float dz = (this.random.nextFloat() - 0.5F) / 7.0F;
com/legacy/blue_skies/entities/projectile/SeedBombEntity.java:65:            this.random.nextGaussian() * 0.0075F * inaccuracy,
com/legacy/blue_skies/entities/projectile/SeedBombEntity.java:66:            this.random.nextGaussian() * 0.0075F * inaccuracy,
com/legacy/blue_skies/entities/projectile/SeedBombEntity.java:67:            this.random.nextGaussian() * 0.0075F * inaccuracy
com/legacy/blue_skies/entities/projectile/SpearEntity.java:189:         this.playSound(SkiesSounds.ITEM_SPEAR_HIT_GROUND, 1.0F, 1.0F + this.random.nextFloat() *
com/legacy/blue_skies/entities/projectile/VenomBombEntity.java:45:            float dx = (this.random.nextFloat() - 0.5F) / 7.0F;
com/legacy/blue_skies/entities/projectile/VenomBombEntity.java:46:            float dy = this.random.nextFloat() / 10.0F;
com/legacy/blue_skies/entities/projectile/VenomBombEntity.java:47:            float dz = (this.random.nextFloat() - 0.5F) / 7.0F;
com/legacy/blue_skies/entities/projectile/VenomBombEntity.java:62:      RandomSource random = this.random;
com/legacy/blue_skies/entities/projectile/VenomSpitEntity.java:104:         float dx = (this.random.nextFloat() - 0.5F) / 7.0F;
com/legacy/blue_skies/entities/projectile/VenomSpitEntity.java:105:         float dy = this.random.nextFloat() / 10.0F;
com/legacy/blue_skies/entities/projectile/VenomSpitEntity.java:106:         float dz = (this.random.nextFloat() - 0.5F) / 7.0F;
com/legacy/blue_skies/entities/projectile/VenomSpitEntity.java:118:            this.random.nextGaussian() * 0.0075F * inaccuracy,
com/legacy/blue_skies/entities/projectile/VenomSpitEntity.java:119:            this.random.nextGaussian() * 0.0075F * inaccuracy,
com/legacy/blue_skies/entities/projectile/VenomSpitEntity.java:120:            this.random.nextGaussian() * 0.0075F * inaccuracy
com/legacy/blue_skies/entities/SupporterPetEntity.java:215:               double dx = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/SupporterPetEntity.java:216:               double dy = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/SupporterPetEntity.java:217:               double dz = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/SupporterPetEntity.java:221:                     this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWid
com/legacy/blue_skies/entities/SupporterPetEntity.java:222:                     this.getY() + this.random.nextFloat() * this.getBbHeight(),
com/legacy/blue_skies/entities/SupporterPetEntity.java:223:                     this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWid
com/legacy/blue_skies/entities/SupporterPetEntity.java:315:      return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + (this.shouldGangster() ? 1.
com/legacy/blue_skies/entities/util/base/SkiesIllagerBossEntity.java:171:         if (this.random.nextDouble() > 0.7) {
com/legacy/blue_skies/entities/util/base/SkiesIllagerBossEntity.java:276:         if (this.getTarget() == null && this.isDungeonSpawned() && this.teleportCooldo
com/legacy/blue_skies/entities/util/base/SkiesIllagerBossEntity.java:615:               && SkiesIllagerBossEntity.this.random.nextBoolean()
com/legacy/blue_skies/entities/util/base/SkiesMountEntity.java:201:         double d0 = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/util/base/SkiesMountEntity.java:202:         double d1 = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/util/base/SkiesMountEntity.java:203:         double d2 = this.random.nextGaussian() * 0.02;
com/legacy/blue_skies/entities/util/base/SkiesMountEntity.java:207:               this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbW
com/legacy/blue_skies/entities/util/base/SkiesMountEntity.java:208:               this.getY() + 0.5 + this.random.nextFloat() * this.getBbHeight(),
com/legacy/blue_skies/entities/util/base/SkiesMountEntity.java:209:               this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbW
com/legacy/blue_skies/entities/util/interfaces/ISkyBoss.java:32:import net.minecraft.util.random.WeightedRandomList;
com/legacy/blue_skies/entities/villager/GatekeeperEntity.java:383:         int i = 3 + this.random.nextInt(4);
com/legacy/blue_skies/entities/villager/GatekeeperEntity.java:405:         offers.add(trade.getOffer(this.getTradingPlayer(), this.random));
com/legacy/blue_skies/events/SkiesEvents.java:121:         && animal.level().random.nextFloat() < 0.3F
com/legacy/blue_skies/items/LootBagItem.java:62:               level.random.nextFloat() * 0.1F + 1.0F
com/legacy/blue_skies/items/PreparedFoodItem.java:66:            world.random.nextFloat() * 0.1F + 0.9F
com/legacy/blue_skies/items/PreparedFoodItem.java:81:         1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F
com/legacy/blue_skies/items/tools/weapons/SummoningTomeItem.java:103:                  if (worldIn.random.nextFloat() < 0.3F) {
com/legacy/blue_skies/items/tools/weapons/SummoningTomeItem.java:111:                        double dx = worldIn.random.nextGaussian() * 0.02;
com/legacy/blue_skies/items/tools/weapons/SummoningTomeItem.java:112:                        double dy = worldIn.random.nextGaussian() * 0.02;
com/legacy/blue_skies/items/tools/weapons/SummoningTomeItem.java:113:                        double dz = worldIn.random.nextGaussian() * 0.02;
com/legacy/blue_skies/items/tools/weapons/SummoningTomeItem.java:117:                              golem.getX() + worldIn.random.nextFloat() * golem.getBbWidth(
com/legacy/blue_skies/items/tools/weapons/SummoningTomeItem.java:118:                              golem.getY() + worldIn.random.nextFloat() * golem.getBbHeight
com/legacy/blue_skies/items/tools/weapons/SummoningTomeItem.java:119:                              golem.getZ() + worldIn.random.nextFloat() * golem.getBbWidth(
com/legacy/blue_skies/items/tools/weapons/SummoningTomeItem.java:88:               BlockPos spawnPos = new BlockPos(pos.getX() + (worldIn.random.nextInt(6) - 3)
com/legacy/blue_skies/items/tools/weapons/VenomSacItem.java:60:            (world.random.nextFloat() - world.random.nextFloat()) * 0.1F + 1.5F
com/legacy/blue_skies/items/util/IRarityItem.java:7:import net.minecraft.util.random.Weight;
com/legacy/blue_skies/items/util/IRarityItem.java:8:import net.minecraft.util.random.WeightedEntry;
com/legacy/blue_skies/network/s_to_c/SpawnParticlePacket.java:75:                  double x = LogicUtil.plusOrMinus((double)(packet.x + 0.5F), spread, world.ran
com/legacy/blue_skies/network/s_to_c/SpawnParticlePacket.java:76:                  double y = LogicUtil.plusOrMinus((double)(packet.y + 0.5F), spread, world.ran
com/legacy/blue_skies/network/s_to_c/SpawnParticlePacket.java:77:                  double z = LogicUtil.plusOrMinus((double)(packet.z + 0.5F), spread, world.ran
com/legacy/blue_skies/network/s_to_c/SpawnParticlePacket.java:78:                  double dx = LogicUtil.plusOrMinus(speed, world.random);
com/legacy/blue_skies/network/s_to_c/SpawnParticlePacket.java:79:                  double dy = LogicUtil.plusOrMinus(speed, world.random);
com/legacy/blue_skies/network/s_to_c/SpawnParticlePacket.java:80:                  double dz = LogicUtil.plusOrMinus(speed, world.random);
com/legacy/blue_skies/network/s_to_c/SpawnParticlePacket.java:88:                  double x = LogicUtil.plusOrMinus(packet.x, 0.3F, world.random);
com/legacy/blue_skies/network/s_to_c/SpawnParticlePacket.java:89:                  double y = LogicUtil.plusOrMinus(packet.y + 0.25F, 0.5F, world.random);
com/legacy/blue_skies/network/s_to_c/SpawnParticlePacket.java:90:                  double z = LogicUtil.plusOrMinus(packet.z, 0.3F, world.random);
com/legacy/blue_skies/network/s_to_c/SpawnParticlePacket.java:91:                  double dx = LogicUtil.plusOrMinus(speed, world.random);
com/legacy/blue_skies/network/s_to_c/SpawnParticlePacket.java:92:                  double dy = LogicUtil.plusOrMinus(speed, world.random);
com/legacy/blue_skies/network/s_to_c/SpawnParticlePacket.java:93:                  double dz = LogicUtil.plusOrMinus(speed, world.random);
com/legacy/blue_skies/registries/SkiesFeatures.java:63:import net.minecraft.util.random.SimpleWeightedRandomList;
com/legacy/blue_skies/registries/SkiesFeatures.java:64:import net.minecraft.util.random.SimpleWeightedRandomList.Builder;
com/legacy/blue_skies/registries/SkiesStructures.java:40:import net.minecraft.util.random.WeightedRandomList;
com/legacy/blue_skies/util/LogicUtil.java:17:      return Math.random() < 0.5 ? original + value : original - value;
com/legacy/blue_skies/util/LogicUtil.java:26:      return Math.random() < 0.5 ? original + value : original - value;
com/legacy/blue_skies/util/LogicUtil.java:8:      return Math.random() < 0.5 ? original + value : original - value;
com/legacy/blue_skies/util/WeightedObject.java:3:import net.minecraft.util.random.Weight;
com/legacy/blue_skies/util/WeightedObject.java:4:import net.minecraft.util.random.WeightedEntry;
com/legacy/blue_skies/world/everbright/gen/features/FrostbrightTreeFeature.java:33:      int height = rand.nextInt(7) + Math.random() < 0.1 ? 30 : 20;
com/legacy/blue_skies/world/everbright/gen/structures/blinding_dungeon/EverbrightBlindingDungeonStructure.java:27:      Rotation rotation = Rotation.values()[co
com/legacy/blue_skies/world/everbright/gen/structures/blinding_dungeon/EverbrightBlindingDungeonStructure.java:35:            context.random(),
com/legacy/blue_skies/world/everbright/gen/structures/nature_dungeon/NatureDungeonStructure.java:27:      WorldgenRandom random = context.random();
com/legacy/blue_skies/world/everdawn/gen/structures/blinding_dungeon/EverdawnBlindingDungeonStructure.java:27:      Rotation rotation = Rotation.values()[contex
com/legacy/blue_skies/world/everdawn/gen/structures/blinding_dungeon/EverdawnBlindingDungeonStructure.java:35:            context.random(),
com/legacy/blue_skies/world/everdawn/gen/structures/poison_dungeon/PoisonDungeonStructure.java:32:      Rotation rotation = Rotation.getRandom(context.random())
com/legacy/blue_skies/world/everdawn/gen/structures/poison_dungeon/PoisonDungeonStructure.java:36:         PoisonDungeonPieces.assemble(context.structureTemplat

## 类别：StructureType
com/legacy/blue_skies/world/everbright/gen/structures/blinding_dungeon/EverbrightBlindingDungeonStructure.java:12:import net.minecraft.world.level.levelgen.stru
com/legacy/blue_skies/world/everbright/gen/structures/blinding_dungeon/EverbrightBlindingDungeonStructure.java:45:   public StructureType<?> type() {
com/legacy/blue_skies/world/everbright/gen/structures/nature_dungeon/NatureDungeonStructure.java:12:import net.minecraft.world.level.levelgen.structure.Structur
com/legacy/blue_skies/world/everbright/gen/structures/nature_dungeon/NatureDungeonStructure.java:44:   public StructureType<?> type() {
com/legacy/blue_skies/world/everdawn/gen/structures/blinding_dungeon/EverdawnBlindingDungeonStructure.java:12:import net.minecraft.world.level.levelgen.structur
com/legacy/blue_skies/world/everdawn/gen/structures/blinding_dungeon/EverdawnBlindingDungeonStructure.java:45:   public StructureType<?> type() {
com/legacy/blue_skies/world/everdawn/gen/structures/poison_dungeon/PoisonDungeonStructure.java:16:import net.minecraft.world.level.levelgen.structure.StructureT
com/legacy/blue_skies/world/everdawn/gen/structures/poison_dungeon/PoisonDungeonStructure.java:67:   public StructureType<?> type() {

## 类别：BiomeModifier
com/legacy/blue_skies/biome_modifiers/PurifyBiome.java:12:import net.neoforged.neoforge.common.world.BiomeModifier;
com/legacy/blue_skies/biome_modifiers/PurifyBiome.java:13:import net.neoforged.neoforge.common.world.BiomeModifier.Phase;
com/legacy/blue_skies/biome_modifiers/PurifyBiome.java:16:public class PurifyBiome implements BiomeModifier {
com/legacy/blue_skies/biome_modifiers/PurifyBiome.java:44:   public Codec<? extends BiomeModifier> codec() {
com/legacy/blue_skies/registries/SkiesBiomeModifiers.java:12:public class SkiesBiomeModifiers {
com/legacy/blue_skies/registries/SkiesBiomeModifiers.java:13:   public static final RegistrarHandler<Codec<? extends BiomeModifier>> SERIALIZER_HANDLER = Regist
com/legacy/blue_skies/registries/SkiesBiomeModifiers.java:19:      RegistrarHandler<BiomeModifier> handler = RegistrarHandler.getOrCreate(Keys.BIOME_MODIFIERS, 
com/legacy/blue_skies/registries/SkiesBiomeModifiers.java:8:import net.neoforged.neoforge.common.world.BiomeModifier;

## 类别：Registry 取用
com/legacy/blue_skies/asm_hooks/PlayerHooks.java:127:         && !TierSortingRegistry.isCorrectTierForDrops(Tiers.WOOD, state);
com/legacy/blue_skies/asm_hooks/PlayerHooks.java:13:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/asm_hooks/PlayerHooks.java:90:         ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(item);
com/legacy/blue_skies/block_entity/KeystoneBlockEntity.java:110:      this.containedMob = (EntityType<?>)BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(
com/legacy/blue_skies/block_entity/KeystoneBlockEntity.java:111:      this.key = (Item)BuiltInRegistries.ITEM.get(new ResourceLocation(compound.getString("Key")
com/legacy/blue_skies/block_entity/KeystoneBlockEntity.java:112:      this.arc = (Item)BuiltInRegistries.ITEM.get(new ResourceLocation(compound.getString("Arc")
com/legacy/blue_skies/block_entity/KeystoneBlockEntity.java:122:      if ((mobKey = BuiltInRegistries.ENTITY_TYPE.getKey(this.containedMob)) != null) {
com/legacy/blue_skies/block_entity/KeystoneBlockEntity.java:126:      compound.putString("Arc", BuiltInRegistries.ITEM.getKey(this.arc).toString());
com/legacy/blue_skies/block_entity/KeystoneBlockEntity.java:127:      compound.putString("Key", BuiltInRegistries.ITEM.getKey(this.key).toString());
com/legacy/blue_skies/block_entity/KeystoneBlockEntity.java:12:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/block_entity/SkyChestBlockEntity.java:16:      this.localizedName = Component.translatable("block.blue_skies." + BuiltInRegistries.BLOCK_E
com/legacy/blue_skies/block_entity/SkyChestBlockEntity.java:5:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/blocks/construction/SkyFlowerPotBlock.java:20:      ((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(BuiltInRegistries.BLOCK.getKey(flower.get
com/legacy/blue_skies/blocks/construction/SkyFlowerPotBlock.java:32:      return BuiltInRegistries.BLOCK.byNameCodec().fieldOf("potted").forGetter(block -> bloc
com/legacy/blue_skies/blocks/construction/SkyFlowerPotBlock.java:7:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/blocks/natural/FruitBlock.java:7:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/blocks/natural/FruitBlock.java:80:      Item item = (Item)BuiltInRegistries.ITEM.get(BuiltInRegistries.BLOCK.getKey(this));
com/legacy/blue_skies/BlueSkiesConfig.java:11:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/BlueSkiesConfig.java:202:         ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
com/legacy/blue_skies/BlueSkiesConfig.java:210:            .filter(BuiltInRegistries.ENTITY_TYPE::containsKey)
com/legacy/blue_skies/BlueSkiesConfig.java:211:            .<EntityType<?>>map(BuiltInRegistries.ENTITY_TYPE::get)
com/legacy/blue_skies/BlueSkies.java:100:      modBus.register(SkiesRegistry.class);
com/legacy/blue_skies/client/gui/menu/AlchemyTableMenu.java:11:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/client/gui/menu/AlchemyTableMenu.java:206:         allOutputs.sort((a, b) -> BuiltInRegistries.ITEM.getKey(a).compareTo(BuiltInRegistries.
com/legacy/blue_skies/client/gui/screen/journal/JournalEntryRenderers.java:12:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/client/gui/screen/journal/JournalEntryRenderers.java:150:         json.addProperty("item", BuiltInRegistries.ITEM.getKey(this.item.getItem
com/legacy/blue_skies/client/gui/screen/journal/JournalEntryRenderers.java:84:         json.addProperty("entity", BuiltInRegistries.ENTITY_TYPE.getKey(this.enti
com/legacy/blue_skies/client/gui/screen/widgets/InventoryTabButton.java:18:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/client/gui/screen/widgets/InventoryTabButton.java:63:         Optional<ResourceKey<CreativeModeTab>> key = BuiltInRegistries.CREATIVE_MODE
com/legacy/blue_skies/client/gui/screen/widgets/SimpleInventoryTabButton.java:17:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/client/gui/screen/widgets/SimpleInventoryTabButton.java:58:      Optional<ResourceKey<CreativeModeTab>> selectedTab = BuiltInRegistries.CR
com/legacy/blue_skies/client/particle/data/PoisonData.java:12:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/client/particle/data/PoisonData.java:47:      return String.format(Locale.ROOT, "%s small=%s", BuiltInRegistries.PARTICLE_TYPE.getKey(this
com/legacy/blue_skies/client/particle/data/StarFlareData.java:12:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/client/particle/data/StarFlareData.java:58:      return String.format(Locale.ROOT, "%s %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.ge
com/legacy/blue_skies/client/particle/DustParticle.java:12:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/client/particle/DustParticle.java:79:         return (DustParticle)(BuiltInRegistries.PARTICLE_TYPE.getKey(type).equals(BuiltInRegistries.
com/legacy/blue_skies/crafting/toolbox/ModifySpearHandleRecipe.java:25:      return spearStickRegistry.containsKey(inputItem.getItem()) && spearStickRegistry.co
com/legacy/blue_skies/crafting/toolbox/ModifySpearHandleRecipe.java:30:      ItemStack output = new ItemStack((ItemLike)spearStickRegistry.inverse().get(modifie
com/legacy/blue_skies/data/managers/HorizoniteForgeFuelManager.java:30:      keyNbt.putString("0", BuiltInRegistries.ITEM.getKey(key).toString());
com/legacy/blue_skies/data/managers/HorizoniteForgeFuelManager.java:6:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/managers/SkiesDataManager.java:135:      return parseRegistry(json, key, BuiltInRegistries.ITEM);
com/legacy/blue_skies/data/managers/SkiesDataManager.java:139:      return parseRegistry(json, key, BuiltInRegistries.ITEM, defaultVal);
com/legacy/blue_skies/data/managers/SkiesDataManager.java:143:      return parseRegistry(json, key, BuiltInRegistries.BLOCK);
com/legacy/blue_skies/data/managers/SkiesDataManager.java:147:      return parseRegistry(json, key, BuiltInRegistries.BLOCK, defaultVal);
com/legacy/blue_skies/data/managers/SkiesDataManager.java:151:      return parseRegistry(json, key, BuiltInRegistries.ENTITY_TYPE);
com/legacy/blue_skies/data/managers/SkiesDataManager.java:155:      return parseRegistry(json, key, BuiltInRegistries.ENTITY_TYPE, defaultVal);
com/legacy/blue_skies/data/managers/SkiesDataManager.java:23:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/managers/SkiesSyncedDataManager.java:10:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/managers/SkiesSyncedDataManager.java:84:      nbt.putString(key, BuiltInRegistries.ITEM.getKey(item).toString());
com/legacy/blue_skies/data/managers/SkiesSyncedDataManager.java:88:      return (Item)BuiltInRegistries.ITEM.get(new ResourceLocation(nbt.getString(key)));
com/legacy/blue_skies/data/managers/SkiesSyncedDataManager.java:92:      nbt.putString(key, BuiltInRegistries.BLOCK.getKey(block).toString());
com/legacy/blue_skies/data/managers/SkiesSyncedDataManager.java:96:      return (Block)BuiltInRegistries.BLOCK.get(new ResourceLocation(nbt.getString(key)));
com/legacy/blue_skies/data/managers/SnowcapOvenFreezingManager.java:33:      keyNbt.putString("0", BuiltInRegistries.ITEM.getKey(key).toString());
com/legacy/blue_skies/data/managers/SnowcapOvenFreezingManager.java:6:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/managers/ToolHandleTypeManager.java:40:      return BuiltInRegistries.ITEM.containsKey(itemName) ? this.getHandle((Item)BuiltInRegist
com/legacy/blue_skies/data/managers/ToolHandleTypeManager.java:52:      return BuiltInRegistries.ITEM.containsKey(itemName) ? this.hasHandle((Item)BuiltInRegist
com/legacy/blue_skies/data/managers/ToolHandleTypeManager.java:8:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/objects/alchemy/AlchemyRecipe.java:24:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/objects/alchemy/AlchemyRecipe.java:50:            names.stream().map(BuiltInRegistries.ITEM::get).filter(Objects::nonNull),
com/legacy/blue_skies/data/objects/alchemy/AlchemyRecipe.java:52:               .<Optional>map(BuiltInRegistries.ITEM::getTag)
com/legacy/blue_skies/data/objects/alchemy/AlchemyRecipe.java:80:      return resourceLocationSet.stream().filter(BuiltInRegistries.ITEM::containsKey).<Item>map
com/legacy/blue_skies/data/objects/alchemy/CatylistRecipe.java:181:            this.inputs.add(BuiltInRegistries.ITEM.getKey(item.asItem()));
com/legacy/blue_skies/data/objects/alchemy/CatylistRecipe.java:198:            this.catylists.add(BuiltInRegistries.ITEM.getKey(item.asItem()));
com/legacy/blue_skies/data/objects/alchemy/CatylistRecipe.java:19:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/objects/alchemy/CatylistRecipe.java:215:            this.results.add(BuiltInRegistries.ITEM.getKey(item.asItem()));
com/legacy/blue_skies/data/objects/alchemy/CatylistRecipe.java:33:               BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("input").forGetter(r -> r.
com/legacy/blue_skies/data/objects/alchemy/CatylistRecipe.java:36:               BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("catylist").forGetter(r ->
com/legacy/blue_skies/data/objects/alchemy/TransmuteRecipe.java:122:            this.items.add(BuiltInRegistries.ITEM.getKey(item.asItem()));
com/legacy/blue_skies/data/objects/alchemy/TransmuteRecipe.java:139:            this.resultBlacklist.add(BuiltInRegistries.ITEM.getKey(item.asItem()));
com/legacy/blue_skies/data/objects/alchemy/TransmuteRecipe.java:18:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/objects/FreezingRecipe.java:29:      json.addProperty("input", BuiltInRegistries.ITEM.getKey(this.inputItem).toString());
com/legacy/blue_skies/data/objects/FreezingRecipe.java:32:         json.addProperty("output", BuiltInRegistries.ITEM.getKey(this.outputItem).toString());
com/legacy/blue_skies/data/objects/FreezingRecipe.java:4:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/objects/HorizoniteForgeFuel.java:19:      json.addProperty("item", BuiltInRegistries.ITEM.getKey(this.item).toString());
com/legacy/blue_skies/data/objects/HorizoniteForgeFuel.java:4:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/objects/SnowcapMushroomFreezing.java:24:      json.addProperty("block", BuiltInRegistries.BLOCK.getKey(this.block).toString());
com/legacy/blue_skies/data/objects/SnowcapMushroomFreezing.java:26:      json.addProperty("frozen_block", BuiltInRegistries.BLOCK.getKey(this.frozenBlock).toStr
com/legacy/blue_skies/data/objects/SnowcapMushroomFreezing.java:5:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/objects/ToolHandleType.java:27:      json.addProperty("stick", BuiltInRegistries.ITEM.getKey(this.stick).toString());
com/legacy/blue_skies/data/objects/ToolHandleType.java:4:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/objects/ToolHandleType.java:60:      return BuiltInRegistries.ITEM.getKey(this.getStick()).toString();
com/legacy/blue_skies/data/objects/ToolHandleType.java:67:         BuiltInRegistries.ITEM.getKey(this.stick),
com/legacy/blue_skies/data/providers/AlchemyRecipeProv.java:15:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/providers/AlchemyRecipeProv.java:256:         if (BuiltInRegistries.ITEM.containsKey(itemName)) {
com/legacy/blue_skies/data/providers/AlchemyRecipeProv.java:257:            items.add((ItemLike)BuiltInRegistries.ITEM.get(itemName));
com/legacy/blue_skies/data/providers/AlchemyRecipeProv.java:271:         if (BuiltInRegistries.ITEM.containsKey(itemName)) {
com/legacy/blue_skies/data/providers/AlchemyRecipeProv.java:272:            items.add((ItemLike)BuiltInRegistries.ITEM.get(itemName));
com/legacy/blue_skies/data/providers/AlchemyRecipeProv.java:286:         if (BuiltInRegistries.ITEM.containsKey(itemName)) {
com/legacy/blue_skies/data/providers/AlchemyRecipeProv.java:287:            items.add((ItemLike)BuiltInRegistries.ITEM.get(itemName));
com/legacy/blue_skies/data/providers/HorizoniteForgeFuelProv.java:34:      registry.put(BlueSkies.locate(BuiltInRegistries.ITEM.getKey(fuel).getPath()), new Hor
com/legacy/blue_skies/data/providers/HorizoniteForgeFuelProv.java:9:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/providers/SkiesAdvancementProv.java:27:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/providers/SkiesAdvancementProv.java:305:               "kill_" + BuiltInRegistries.ENTITY_TYPE.getKey(e).getPath(),
com/legacy/blue_skies/data/providers/SkiesAdvancementProv.java:315:               "has_" + BuiltInRegistries.ITEM.getKey(item.asItem()).getPath(),
com/legacy/blue_skies/data/providers/SkiesBiomeProv.java:448:                  .ambientLoopSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SkiesSounds.AMBIENT_
com/legacy/blue_skies/data/providers/SkiesBiomeProv.java:571:                  .ambientLoopSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SkiesSounds.AMBIENT_
com/legacy/blue_skies/data/providers/SkiesBiomeProv.java:8:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/providers/SkiesLootProv.java:1798:                  this.ramLootTable((ItemLike)BuiltInRegistries.BLOCK.get(new ResourceLocation(c + 
com/legacy/blue_skies/data/providers/SkiesLootProv.java:1903:         return BuiltInRegistries.ENTITY_TYPE.getKey(entity).getPath();
com/legacy/blue_skies/data/providers/SkiesLootProv.java:1907:         return BuiltInRegistries.ENTITY_TYPE.stream().filter(e -> BuiltInRegistries.ENTITY_TYPE.ge
com/legacy/blue_skies/data/providers/SkiesLootProv.java:39:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/providers/SkiesLootProv.java:771:         return BuiltInRegistries.BLOCK
com/legacy/blue_skies/data/providers/SkiesLootProv.java:773:            .filter(b -> BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals("blue_skies") && !b
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:1057:               BlueSkies.find(BuiltInRegistries.ITEM.getKey(dye.asItem()).getPath() + "_from_" + 
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:1391:      ResourceLocation itemName = BuiltInRegistries.ITEM.getKey(wall.asItem());
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:1435:                  BuiltInRegistries.ITEM.getKey(result.asItem()).getPath() + "_stonecutting_" + B
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:1453:               BuiltInRegistries.ITEM.getKey(result.asItem()).getPath()
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:1455:                  + BuiltInRegistries.RECIPE_SERIALIZER.getKey(type).getPath()
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:1457:                  + BuiltInRegistries.ITEM.getKey(ingredient.asItem()).getPath()
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:1471:               BuiltInRegistries.ITEM.getKey(result.asItem()).getPath()
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:1473:                  + BuiltInRegistries.RECIPE_SERIALIZER.getKey(type).getPath()
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:1475:                  + BuiltInRegistries.ITEM.getKey(ingredient.asItem()).getPath()
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:1509:            BlueSkies.find(BuiltInRegistries.ITEM.getKey(result.asItem()).getPath() + "_from_" + 
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:18:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:841:            .save(this.con, BlueSkies.find(BuiltInRegistries.ITEM.getKey(mat.material.asItem()).ge
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:881:            .save(this.con, BlueSkies.find(BuiltInRegistries.ITEM.getKey(nugget.asItem()).getPath(
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:888:            .save(this.con, BlueSkies.find(BuiltInRegistries.ITEM.getKey(ingot.asItem()).getPath()
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:900:         String rawOreName = BuiltInRegistries.ITEM.getKey(rawOre.asItem()).getPath();
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:911:            .save(this.con, BlueSkies.find(BuiltInRegistries.ITEM.getKey(rawBlock.asItem()).getPat
com/legacy/blue_skies/data/providers/SkiesRecipeProv.java:973:            ToolRecipeBuilder.of(material, shape, ((Item)BuiltInRegistries.ITEM.get(item)).getDefa
com/legacy/blue_skies/data/providers/SkiesTagProv.java:1001:         return BuiltInRegistries.BLOCK
com/legacy/blue_skies/data/providers/SkiesTagProv.java:1003:            .filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals("blue_skies
com/legacy/blue_skies/data/providers/SkiesTagProv.java:1569:         return BuiltInRegistries.ITEM
com/legacy/blue_skies/data/providers/SkiesTagProv.java:1571:            .filter(block -> BuiltInRegistries.ITEM.getKey(block).getNamespace().equals("blue_skies"
com/legacy/blue_skies/data/providers/SkiesTagProv.java:28:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/providers/SnowcapOvenFreezingProv.java:74:      String name = BuiltInRegistries.ITEM.getKey(inputItem.asItem()).getPath();
com/legacy/blue_skies/data/providers/SnowcapOvenFreezingProv.java:76:         name = name + "_to_" + BuiltInRegistries.ITEM.getKey(outputItem.asItem()).getPath(
com/legacy/blue_skies/data/providers/SnowcapOvenFreezingProv.java:9:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/data/providers/ToolHandleTypeProv.java:38:      registry.put(BlueSkies.locate(BuiltInRegistries.ITEM.getKey(toolHandleType.getStick()).get
com/legacy/blue_skies/data/providers/ToolHandleTypeProv.java:9:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/entities/hostile/ArmoredFrostSpiritEntity.java:12:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/entities/hostile/ArmoredFrostSpiritEntity.java:400:         ? BlueSkies.locate("entities/" + BuiltInRegistries.ENTITY_TYPE.getKey(this.get
com/legacy/blue_skies/entities/util/interfaces/ISkyBoss.java:129:      LootBagItem.setLootTable(stack, BlueSkies.locate("loot_bags/" + BuiltInRegistries.ENTITY_
com/legacy/blue_skies/entities/util/interfaces/ISkyBoss.java:22:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/entities/util/SkiesEntityHooks.java:50:         ResourceLocation name = BuiltInRegistries.ENTITY_TYPE.getKey(source.getDirectEntity().getT
com/legacy/blue_skies/entities/util/SkiesEntityHooks.java:8:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/events/SkiesEvents.java:329:      if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("blue_skies")) {
com/legacy/blue_skies/events/SkiesEvents.java:34:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/events/SkiesMappingChanges.java:7:import com.legacy.structure_gel.core.registry.SGRegistry.Blocks;
com/legacy/blue_skies/events/SkiesPlayerEvents.java:39:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/events/SkiesPlayerEvents.java:599:            ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(block);
com/legacy/blue_skies/items/BossLootBagItem.java:24:      String bossName = BuiltInRegistries.ENTITY_TYPE.getKey(this.boss.get()).getPath();
com/legacy/blue_skies/items/BossLootBagItem.java:38:      String bossName = BuiltInRegistries.ENTITY_TYPE.getKey(this.boss.get()).getPath();
com/legacy/blue_skies/items/BossLootBagItem.java:6:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/items/util/FoodPrepUtils.java:16:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/items/util/FoodPrepUtils.java:349:         for (Item item : BuiltInRegistries.ITEM) {
com/legacy/blue_skies/items/util/FoodPrepUtils.java:359:                  COLORS.put(item, values()[new Random(BuiltInRegistries.ITEM.getKey(item).hashCode()).n
com/legacy/blue_skies/network/s_to_c/DisplayToastPacket.java:11:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/network/s_to_c/DisplayToastPacket.java:76:            String keyName = BuiltInRegistries.ITEM.getKey(key).getPath();
com/legacy/blue_skies/registries/SkiesArmorTrims.java:11:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/registries/SkiesArmorTrims.java:133:            BuiltInRegistries.ITEM.wrapAsHolder(templateItem),
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:11:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:35:      () -> BuiltInRegistries.ITEM
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:38:            item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("blue_skies")
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:42:         .sorted((o, n) -> BuiltInRegistries.ITEM.getKey(o).compareTo(BuiltInRegistries.ITEM.getKey(n
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:742:         for (Item e : BuiltInRegistries.ITEM
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:744:            .filter(block -> BuiltInRegistries.ITEM.getKey(block).getNamespace().equals("blue_skies"
com/legacy/blue_skies/registries/SkiesCreativeTabs.java:746:            System.out.println(BuiltInRegistries.ITEM.getKey(e));
com/legacy/blue_skies/registries/SkiesItems.java:52:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/registries/SkiesItems.java:768:         registry.register(Registries.ITEM, BuiltInRegistries.BLOCK.getKey(block), () -> new BlockItem(bloc
com/legacy/blue_skies/registries/SkiesItems.java:775:         registry.register(Registries.ITEM, BuiltInRegistries.BLOCK.getKey(block), () -> new BlockItem(bloc
com/legacy/blue_skies/registries/SkiesItems.java:782:         registry.register(Registries.ITEM, BuiltInRegistries.BLOCK.getKey(block), () -> new DungeonBlockIt
com/legacy/blue_skies/registries/SkiesSounds.java:294:      Registry.register(BuiltInRegistries.SOUND_EVENT, location, sound);
com/legacy/blue_skies/registries/SkiesSounds.java:5:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/registries/SkiesStats.java:29:      Registry.register(BuiltInRegistries.CUSTOM_STAT, key, resourcelocation);
com/legacy/blue_skies/registries/SkiesStats.java:5:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/registries/SkiesStructureProcessors.java:23:      return (StructureProcessorType<P>)Registry.register(BuiltInRegistries.STRUCTURE_PROCESSO
com/legacy/blue_skies/registries/SkiesStructureProcessors.java:9:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/registries/SkiesVillagers.java:142:      VillagerType type = (VillagerType)Registry.register(BuiltInRegistries.VILLAGER_TYPE, BlueSkies.lo
com/legacy/blue_skies/registries/SkiesVillagers.java:14:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/util/StructureChestUtil.java:55:         (Block)BuiltInRegistries.BLOCK.get(new ResourceLocation(blockKey)),
com/legacy/blue_skies/util/StructureChestUtil.java:66:         pos, level, rand, rotation, (Block)BuiltInRegistries.BLOCK.get(new ResourceLocation(blockKey)), D
com/legacy/blue_skies/util/StructureChestUtil.java:8:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/world/everbright/biome/provider/EverbrightBiomeSource.java:28:      this(biomeRegistry, minY, height, HolderSet.direct(r -> biomeRegistry.
com/legacy/blue_skies/world/everbright/EverbrightChunkGenerator.java:22:      super(new EverbrightBiomeSource(biomeRegistry, 0, 320), noiseSettingsRegistry.getO
com/legacy/blue_skies/world/everbright/gen/structures/blinding_dungeon/EverbrightBlindingDungeonPieces.java:20:import net.minecraft.core.registries.BuiltInRegis
com/legacy/blue_skies/world/everbright/gen/structures/blinding_dungeon/EverbrightBlindingDungeonPieces.java:210:            entity.setVariant((CatVariant)((Refe
com/legacy/blue_skies/world/everbright/gen/structures/nature_dungeon/NatureDungeonPieces.java:1282:               Block block = (Block)BuiltInRegistries.BLOCK.g
com/legacy/blue_skies/world/everbright/gen/structures/nature_dungeon/NatureDungeonPieces.java:32:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/world/everdawn/biome/provider/EverdawnBiomeSource.java:28:      this(biomeRegistry, minY, height, HolderSet.direct(r -> biomeRegistry.getO
com/legacy/blue_skies/world/everdawn/EverdawnChunkGenerator.java:22:      super(new EverdawnBiomeSource(biomeRegistry, 0, 320), noiseSettingsRegistry.getOrThrow
com/legacy/blue_skies/world/everdawn/gen/structures/blinding_dungeon/EverdawnBlindingDungeonPieces.java:20:import net.minecraft.core.registries.BuiltInRegistrie
com/legacy/blue_skies/world/everdawn/gen/structures/blinding_dungeon/EverdawnBlindingDungeonPieces.java:210:            entity.setVariant((CatVariant)((Referenc
com/legacy/blue_skies/world/general_features/structures/villages/SkiesVillagePools.java:237:      JigsawPoolBuilder jobSites = houseRegistry.poolBuilder().names
com/legacy/blue_skies/world/general_features/structures/villages/SkiesVillageStructure.java:20:import net.minecraft.core.registries.BuiltInRegistries;
com/legacy/blue_skies/world/general_features/structures/villages/SkiesVillageStructure.java:81:                              BuiltInRegistries.VILLAGER_TYPE.get

## 类别：ItemStack.is
com/legacy/blue_skies/entities/passive/CrystalCamelEntity.java:168:      if (!this.hasChest() && itemstack.is(Items.CHESTS) && !this.isBaby() && this.isTamed())


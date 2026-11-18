package com.legacy.blue_skies.entities.villager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.tags.SkiesStructureTags;
import com.legacy.blue_skies.items.util.IFalsiteItem;
import com.legacy.blue_skies.items.util.ToolUtils;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.registries.SkiesItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.saveddata.maps.MapDecoration.Type;

public class SkiesVillagerTrades {
   private static final List<Item> EVERBRIGHT_STICKS = Lists.newArrayList(
      new Item[]{SkiesItems.bluebright_stick, SkiesItems.starlit_stick, SkiesItems.frostbright_stick, SkiesItems.comet_stick}
   );
   private static final List<Item> EVERDAWN_STICKS = Lists.newArrayList(
      new Item[]{SkiesItems.lunar_stick, SkiesItems.dusk_stick, SkiesItems.maple_stick, SkiesItems.comet_stick}
   );
   private static final Item EMERALD = Items.EMERALD;
   public static final Int2ObjectMap<ItemListing[]> STARGAZER_OFFERS = getAsIntMap(
      ImmutableMap.of(
         1,
         new ItemListing[]{
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(SkiesItems.falsite_ingot, 4)
               .secondItem(SkiesItems.moonstone_shard, 1)
               .result(Items.CLOCK, 1)
               .build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(SkiesBlocks.star_flare, 32).result(EMERALD, 1).maxUses(10).build()
         },
         2,
         new ItemListing[]{
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(SkiesItems.ventium_ingot, 4)
               .secondItem(SkiesItems.moonstone_shard, 1)
               .result(Items.COMPASS, 1)
               .givenXP(10)
               .build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(Blocks.GLASS, 16).result(EMERALD, 1).maxUses(10).givenXP(7).build()
         },
         3,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(Items.IRON_INGOT, 5).result(EMERALD, 1).givenXP(15).build(),
            new SkiesVillagerTrades.EmeraldForMapTrade("frozen_bunker", 7, SkiesStructureTags.ON_BUNKER_MAPS, Type.TARGET_POINT, 12, 20)
         },
         4,
         new ItemListing[]{
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(SkiesBlocks.moonstone, 6)
               .secondItem(Blocks.IRON_BARS, 8)
               .result(SkiesBlocks.moonstone_lantern, 3)
               .maxUses(15)
               .givenXP(25)
               .build()
         },
         5,
         new ItemListing[]{new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 10).result(SkiesItems.astrolabe, 1).givenXP(30).build()}
      )
   );
   public static final Int2ObjectMap<ItemListing[]> WRANGLER_OFFERS = getAsIntMap(
      ImmutableMap.of(
         1,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(SkiesItems.cooked_charscale_moki, 4).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(Items.LEAD, 4).build()
         },
         2,
         new ItemListing[]{
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(EMERALD, 1)
               .secondItem(Items.LEATHER, 4)
               .result(Items.SADDLE, 1)
               .maxUses(15)
               .givenXP(10)
               .build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(SkiesItems.azulfo_horn, 16).result(EMERALD, 1).maxUses(10).givenXP(7).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(SkiesItems.cooked_monitor_tail, 3).build()
         },
         3,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 2).result(SkiesItems.cooked_grittle_flatfish, 6).maxUses(10).givenXP(15).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 2).result(Items.COOKED_MUTTON, 8).maxUses(10).givenXP(13).build()
         },
         4,
         new ItemListing[]{
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(SkiesItems.comet_berries, 6)
               .secondItem(EMERALD, 1)
               .result(SkiesBlocks.comet_berry_pie, 1)
               .givenXP(20)
               .build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(Items.STRING, 26).result(EMERALD, 1).maxUses(10).givenXP(16).build()
         },
         5,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 2).result(SkiesItems.cooked_carabeef, 4).maxUses(10).givenXP(25).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 2).result(SkiesItems.cooked_venison, 4).maxUses(10).givenXP(25).build()
         }
      )
   );
   public static final Int2ObjectMap<ItemListing[]> SHOVELER_OFFERS = getAsIntMap(
      ImmutableMap.of(
         1,
         new ItemListing[]{
            new SkiesVillagerTrades.ToolWithFalsiteTrade(1, SkiesItems.pyrope_gem, SkiesItems.pyrope_shovel, 5, 7),
            new SkiesVillagerTrades.SingleTrade.Builder().item(SkiesItems.falsite_ingot, 8).result(EMERALD, 1).maxUses(8).givenXP(8).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(SkiesItems.falsite_ingot, 4).result(Blocks.SNOW_BLOCK, 16).givenXP(5).build()
         },
         2,
         new ItemListing[]{
            new SkiesVillagerTrades.ToolWithFalsiteTrade(2, SkiesItems.aquite, SkiesItems.aquite_shovel, 5, 10),
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(SkiesItems.falsite_ingot, 3)
               .secondItem(SkiesItems.aquite, 5)
               .result(EMERALD, 1)
               .givenXP(10)
               .build()
         },
         3,
         new ItemListing[]{
            new SkiesVillagerTrades.ToolWithFalsiteTrade(3, SkiesItems.horizonite_ingot, SkiesItems.horizonite_shovel, 15, 15, 5),
            new SkiesVillagerTrades.StickForFalsiteTrade(1, 5, 10)
         },
         4,
         new ItemListing[]{new SkiesVillagerTrades.ToolWithFalsiteTrade(5, SkiesItems.diopside_gem, SkiesItems.diopside_shovel, 5, 20, 12)},
         5,
         new ItemListing[]{new SkiesVillagerTrades.ToolWithFalsiteTrade(10, SkiesItems.charoite, SkiesItems.charoite_shovel, 5, 25, 17)}
      )
   );
   public static final Int2ObjectMap<ItemListing[]> NIGHTWATCHER_OFFERS = getAsIntMap(
      ImmutableMap.of(
         1,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(Items.STRING, 16).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(Items.WHEAT_SEEDS, 26).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(Items.BREAD, 8).givenXP(5).build()
         },
         2,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(SkiesItems.ventium_nugget, 6).givenXP(10).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(Items.GOLD_NUGGET, 6).givenXP(10).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(SkiesItems.falsite_nugget, 6).givenXP(10).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(SkiesItems.cooked_grittle_flatfish, 6).givenXP(10).build()
         },
         3,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 2).result(SkiesItems.ventium_bucket, 1).givenXP(15).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 2).result(SkiesItems.falsite_ingot, 1).givenXP(15).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 2).result(SkiesItems.ventium_ingot, 1).givenXP(15).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 2).result(SkiesItems.aquite, 6).givenXP(15).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 4).result(Items.MAP, 1).givenXP(15).build()
         },
         4,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 8).result(Items.NAME_TAG, 1).givenXP(25).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 6).result(Items.SADDLE, 1).givenXP(20).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 9).result(SkiesItems.charoite, 3).givenXP(30).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 2).result(Items.GOLD_INGOT, 6).givenXP(20).build()
         },
         5,
         new ItemListing[]{new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 6).result(SkiesBlocks.bag_of_spoils, 1).givenXP(35).build()}
      )
   );
   public static final Int2ObjectMap<ItemListing[]> SUMMONER_OFFERS = getAsIntMap(
      ImmutableMap.of(
         1,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(Items.BOOK, 2).givenXP(5).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(SkiesItems.bug_guts, 32).result(EMERALD, 1).build()
         },
         2,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(SkiesItems.pearl, 3).givenXP(13).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(SkiesItems.soul_fragment, 22).result(EMERALD, 1).givenXP(10).build()
         },
         3,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(Items.SPIDER_EYE, 26).result(EMERALD, 1).givenXP(15).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(Items.ROTTEN_FLESH, 46).result(EMERALD, 1).givenXP(15).build()
         },
         4,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(Items.RABBIT_FOOT, 6).result(EMERALD, 1).givenXP(25).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(SkiesItems.fox_pelt, 22).result(EMERALD, 1).givenXP(20).build()
         },
         5,
         new ItemListing[]{new SkiesVillagerTrades.SingleTrade.Builder().item(SkiesItems.summoning_tome, 1).result(EMERALD, 6).givenXP(25).build()}
      )
   );
   public static final Int2ObjectMap<ItemListing[]> ALCHEMIST_OFFERS = getAsIntMap(
      ImmutableMap.of(
         1,
         new ItemListing[]{
            new SkiesVillagerTrades.DoubleTrade.Builder().firstItem(EMERALD, 1).secondItem(Items.MAGMA_CREAM, 3).result(Items.SLIME_BALL, 3).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(Items.GOLD_INGOT, 8).result(EMERALD, 1).givenXP(5).maxUses(5).build(),
            new SkiesVillagerTrades.SingleTrade.Builder().item(EMERALD, 1).result(Items.BLAZE_ROD, 3).givenXP(5).build(),
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(EMERALD, 1)
               .secondItem(SkiesBlocks.midnight_sandstone, 16)
               .result(SkiesBlocks.crystal_sandstone, 16)
               .build(),
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(EMERALD, 1)
               .secondItem(SkiesBlocks.crystal_sandstone, 16)
               .result(SkiesBlocks.midnight_sandstone, 16)
               .build()
         },
         2,
         new ItemListing[]{
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(SkiesBlocks.lunar_sapling, 1)
               .secondItem(SkiesItems.crescent_fruit, 3)
               .result(SkiesBlocks.crescent_fruit_sapling, 1)
               .givenXP(10)
               .build(),
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(SkiesBlocks.lunar_dirt, 6)
               .secondItem(SkiesItems.ventium_water_bucket, 1)
               .result(SkiesBlocks.lunar_mud, 6)
               .givenXP(5)
               .build(),
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(SkiesItems.camel_saddle, 1)
               .secondItem(EMERALD, 1)
               .result(Items.SADDLE, 1)
               .givenXP(10)
               .build(),
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(SkiesItems.horizonite_ingot, 3)
               .secondItem(SkiesItems.ventium_water_bucket, 1)
               .result(Items.IRON_INGOT, 3)
               .givenXP(10)
               .maxUses(15)
               .build()
         },
         3,
         new ItemListing[]{
            new SkiesVillagerTrades.SingleTrade.Builder().item(Items.BUCKET, 1).result(SkiesItems.ventium_bucket, 1).maxUses(3).givenXP(10).build(),
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(SkiesBlocks.lunar_planks, 16)
               .secondItem(SkiesBlocks.moonstone_crystal, 4)
               .result(SkiesBlocks.crystallized_planks, 16)
               .givenXP(17)
               .build()
         },
         4,
         new ItemListing[]{
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(SkiesBlocks.short_lunar_grass, 12)
               .secondItem(SkiesItems.moonstone_shard, 6)
               .result(SkiesBlocks.crystallized_grass, 12)
               .givenXP(15)
               .build(),
            new SkiesVillagerTrades.DoubleTrade.Builder()
               .firstItem(EMERALD, 15)
               .secondItem(SkiesItems.ventium_water_bucket, 1)
               .result(SkiesItems.ventium_lava_bucket, 1)
               .givenXP(15)
               .build()
         },
         5,
         new ItemListing[]{new SkiesVillagerTrades.SingleTrade.Builder().item(SkiesItems.alchemy_scroll, 1).result(EMERALD, 6).givenXP(25).build()}
      )
   );
   public static final ItemListing BLINDING_DUNGEON_OFFER = new SkiesVillagerTrades.EmeraldForMapTrade(
      "blinding_dungeon", 8, SkiesStructureTags.ON_BLINDING_MAPS, Type.RED_X, 15, 5
   );
   public static final ItemListing POPULATION_DUNGEON_OFFER = new SkiesVillagerTrades.EmeraldForMapWithBackupTrade(
      "nature_dungeon", "poison_dungeon", 12, SkiesStructureTags.ON_NATURE_MAPS, SkiesStructureTags.ON_POISON_MAPS, Type.RED_X, 15, 5
   );

   public static Int2ObjectMap<ItemListing[]> getAsIntMap(ImmutableMap<Integer, ItemListing[]> tradeList) {
      return new Int2ObjectOpenHashMap(tradeList);
   }

   public static class DoubleTrade implements ItemListing {
      private final ItemStack itemGiven1;
      private final int itemGiven1Count;
      private final ItemStack itemGiven2;
      private final int itemGiven2Count;
      private final ItemStack itemSold;
      private final int soldItemCount;
      private final int maxUses;
      private final int givenXP;
      private final float priceMultiplier;
      private final ResourceLocation allowedDim;

      public DoubleTrade(
         Item itemGiven1,
         int itemGiven1Count,
         Item itemGiven2,
         int itemGiven2Count,
         Item soldItem,
         int soldItemCount,
         int maxUsesIn,
         int givenXPIn,
         ResourceLocation allowedDim
      ) {
         this(
            new ItemStack(itemGiven1),
            itemGiven1Count,
            new ItemStack(itemGiven2),
            itemGiven2Count,
            new ItemStack(soldItem),
            soldItemCount,
            maxUsesIn,
            givenXPIn,
            allowedDim
         );
      }

      public DoubleTrade(
         ItemStack itemGiven1In,
         int itemGiven1CountIn,
         ItemStack itemGiven2In,
         int itemGiven2CountIn,
         ItemStack itemSoldIn,
         int itemSoldCountIn,
         int maxUsesIn,
         int givenXPIn,
         ResourceLocation allowedDim
      ) {
         this.itemGiven1 = itemGiven1In;
         this.itemGiven1Count = itemGiven1CountIn;
         this.itemGiven2 = itemGiven2In;
         this.itemGiven2Count = itemGiven2CountIn;
         this.itemSold = itemSoldIn;
         this.soldItemCount = itemSoldCountIn;
         this.maxUses = maxUsesIn;
         this.givenXP = givenXPIn;
         this.priceMultiplier = 0.05F;
         this.allowedDim = allowedDim;
      }

      public MerchantOffer getOffer(Entity entity, RandomSource rand) {
         return this.allowedDim != null && !entity.level().dimension().location().equals(this.allowedDim)
            ? null
            : new MerchantOffer(
               new ItemStack(this.itemGiven1.getItem(), this.itemGiven1Count),
               new ItemStack(this.itemGiven2.getItem(), this.itemGiven2Count),
               new ItemStack(this.itemSold.getItem(), this.soldItemCount),
               this.maxUses,
               this.givenXP,
               this.priceMultiplier
            );
      }

      public static class Builder {
         private Item itemGiven1 = Items.WHEAT;
         private int itemGiven1Count = 1;
         private Item itemGiven2 = SkiesVillagerTrades.EMERALD;
         private int itemGiven2Count = 1;
         private Item itemSold = Items.BREAD;
         private int itemSoldCount = 1;
         private int maxUses = 12;
         private int givenXP = 3;
         private ResourceLocation allowedDim = null;

         public SkiesVillagerTrades.DoubleTrade.Builder firstItem(ItemLike item, int count) {
            this.itemGiven1 = item.asItem();
            this.itemGiven1Count = count;
            return this;
         }

         public SkiesVillagerTrades.DoubleTrade.Builder secondItem(ItemLike item, int count) {
            this.itemGiven2 = item.asItem();
            this.itemGiven2Count = count;
            return this;
         }

         public SkiesVillagerTrades.DoubleTrade.Builder result(ItemLike item, int count) {
            this.itemSold = item.asItem();
            this.itemSoldCount = count;
            return this;
         }

         public SkiesVillagerTrades.DoubleTrade.Builder maxUses(int maxUses) {
            this.maxUses = maxUses;
            return this;
         }

         public SkiesVillagerTrades.DoubleTrade.Builder givenXP(int givenXp) {
            this.givenXP = givenXp;
            return this;
         }

         public SkiesVillagerTrades.DoubleTrade.Builder allowedDim(ResourceLocation dim) {
            this.allowedDim = dim;
            return this;
         }

         public SkiesVillagerTrades.DoubleTrade build() {
            return new SkiesVillagerTrades.DoubleTrade(
               this.itemGiven1,
               this.itemGiven1Count,
               this.itemGiven2,
               this.itemGiven2Count,
               this.itemSold,
               this.itemSoldCount,
               this.maxUses,
               this.givenXP,
               this.allowedDim
            );
         }
      }
   }

   static class EmeraldForMapTrade implements ItemListing {
      private final String displayName;
      private final int count;
      private final TagKey<Structure> structureTag;
      private final Type mapDecorationType;
      private final int maxUses;
      private final int xpValue;

      public EmeraldForMapTrade(String displayName, int count, TagKey<Structure> structureTag, Type mapDecorationType, int maxUses, int xpValue) {
         this.displayName = displayName;
         this.count = count;
         this.structureTag = structureTag;
         this.mapDecorationType = mapDecorationType;
         this.maxUses = maxUses;
         this.xpValue = xpValue;
      }

      @Nullable
      public MerchantOffer getOffer(Entity trader, RandomSource rand) {
         if (!(trader.level() instanceof ServerLevel)) {
            return null;
         } else {
            ServerLevel serverlevel = (ServerLevel)trader.level();
            BlockPos blockpos = serverlevel.findNearestMapStructure(this.structureTag, trader.blockPosition(), 100, true);
            if (blockpos != null) {
               ItemStack itemstack = MapItem.create(serverlevel, blockpos.getX(), blockpos.getZ(), (byte)2, true, true);
               MapItem.renderBiomePreviewMap(serverlevel, itemstack);
               MapItemSavedData.addTargetDecoration(itemstack, blockpos, "+", this.mapDecorationType);
               itemstack.setHoverName(Component.translatable("filled_map." + BlueSkies.find(this.displayName)));
               return new MerchantOffer(new ItemStack(Items.EMERALD, this.count), new ItemStack(Items.COMPASS), itemstack, this.maxUses, this.xpValue, 0.2F);
            } else {
               return null;
            }
         }
      }
   }

   static class EmeraldForMapWithBackupTrade implements ItemListing {
      private final String displayName;
      private final String backupName;
      private final int count;
      private final TagKey<Structure> structureTag;
      private final TagKey<Structure> backupTag;
      private final Type mapDecorationType;
      private final int maxUses;
      private final int xpValue;

      public EmeraldForMapWithBackupTrade(
         String displayName,
         String backupName,
         int count,
         TagKey<Structure> structureTag,
         TagKey<Structure> backupTag,
         Type mapDecorationType,
         int maxUses,
         int xpValue
      ) {
         this.displayName = displayName;
         this.backupName = backupName;
         this.count = count;
         this.structureTag = structureTag;
         this.backupTag = backupTag;
         this.mapDecorationType = mapDecorationType;
         this.maxUses = maxUses;
         this.xpValue = xpValue;
      }

      @Nullable
      public MerchantOffer getOffer(Entity trader, RandomSource rand) {
         if (!(trader.level() instanceof ServerLevel)) {
            return null;
         } else {
            boolean isBackup = false;
            ServerLevel serverlevel = (ServerLevel)trader.level();
            BlockPos blockpos = serverlevel.findNearestMapStructure(this.structureTag, trader.blockPosition(), 100, true);
            if (blockpos == null) {
               blockpos = serverlevel.findNearestMapStructure(this.backupTag, trader.blockPosition(), 100, true);
               isBackup = blockpos != null;
            }

            if (blockpos != null) {
               ItemStack itemstack = MapItem.create(serverlevel, blockpos.getX(), blockpos.getZ(), (byte)2, true, true);
               MapItem.renderBiomePreviewMap(serverlevel, itemstack);
               MapItemSavedData.addTargetDecoration(itemstack, blockpos, "+", this.mapDecorationType);
               itemstack.setHoverName(Component.translatable("filled_map." + BlueSkies.find(isBackup ? this.backupName : this.displayName)));
               return new MerchantOffer(new ItemStack(Items.EMERALD, this.count), new ItemStack(Items.COMPASS), itemstack, this.maxUses, this.xpValue, 0.2F);
            } else {
               return null;
            }
         }
      }
   }

   public static class KeyTrade extends SkiesVillagerTrades.DoubleTrade {
      public KeyTrade(Item itemGiven1, int itemGiven1Count, Item itemGiven2, int itemGiven2Count, Item key) {
         super(itemGiven1, itemGiven1Count, itemGiven2, itemGiven2Count, key, 1, 4, 1, null);
      }
   }

   public static class SingleTrade implements ItemListing {
      private final ItemStack itemGiven1;
      private final int itemGiven1Count;
      private final ItemStack itemSold;
      private final int soldItemCount;
      private final int maxUses;
      private final int givenXP;
      private final float priceMultiplier;
      private final ResourceLocation allowedDim;

      public SingleTrade(Item itemGiven1, int itemGiven1Count, Item soldItem, int soldItemCount, int maxUses, int givenXP, ResourceLocation allowedDim) {
         this(new ItemStack(itemGiven1), itemGiven1Count, new ItemStack(soldItem), soldItemCount, maxUses, givenXP, allowedDim);
      }

      public SingleTrade(
         ItemStack itemGiven1In, int itemGiven1CountIn, ItemStack itemSoldIn, int itemSoldCountIn, int maxUsesIn, int givenXPIn, ResourceLocation allowedDim
      ) {
         this.itemGiven1 = itemGiven1In;
         this.itemGiven1Count = itemGiven1CountIn;
         this.itemSold = itemSoldIn;
         this.soldItemCount = itemSoldCountIn;
         this.maxUses = maxUsesIn;
         this.givenXP = givenXPIn;
         this.priceMultiplier = 0.05F;
         this.allowedDim = allowedDim;
      }

      public MerchantOffer getOffer(Entity entity, RandomSource rand) {
         return this.allowedDim != null && !entity.level().dimension().location().equals(this.allowedDim)
            ? null
            : new MerchantOffer(
               new ItemStack(this.itemGiven1.getItem(), this.itemGiven1Count),
               new ItemStack(this.itemSold.getItem(), this.soldItemCount),
               this.maxUses,
               this.givenXP,
               this.priceMultiplier
            );
      }

      public static class Builder {
         private Item itemGiven1 = SkiesVillagerTrades.EMERALD;
         private int itemGiven1Count = 1;
         private Item itemSold = Items.BREAD;
         private int itemSoldCount = 1;
         private int maxUses = 12;
         private int givenXP = 3;
         private ResourceLocation allowedDim = null;

         public SkiesVillagerTrades.SingleTrade.Builder item(ItemLike item, int count) {
            this.itemGiven1 = item.asItem();
            this.itemGiven1Count = count;
            return this;
         }

         public SkiesVillagerTrades.SingleTrade.Builder result(ItemLike item, int count) {
            this.itemSold = item.asItem();
            this.itemSoldCount = count;
            return this;
         }

         public SkiesVillagerTrades.SingleTrade.Builder maxUses(int maxUses) {
            this.maxUses = maxUses;
            return this;
         }

         public SkiesVillagerTrades.SingleTrade.Builder givenXP(int givenXp) {
            this.givenXP = givenXp;
            return this;
         }

         public SkiesVillagerTrades.SingleTrade.Builder allowedDim(ResourceLocation dim) {
            this.allowedDim = dim;
            return this;
         }

         public SkiesVillagerTrades.SingleTrade build() {
            return new SkiesVillagerTrades.SingleTrade(
               this.itemGiven1, this.itemGiven1Count, this.itemSold, this.itemSoldCount, this.maxUses, this.givenXP, this.allowedDim
            );
         }
      }
   }

   static class StickForFalsiteTrade implements ItemListing {
      private final int count;
      private final int maxUses;
      private final int xpValue;

      public StickForFalsiteTrade(int falsiteCount, int maxUses, int xpValue) {
         this.count = falsiteCount;
         this.maxUses = maxUses;
         this.xpValue = xpValue;
      }

      @Nullable
      public MerchantOffer getOffer(Entity trader, RandomSource rand) {
         List<Item> stickList = (List<Item>)(trader.level().dimension().equals(SkiesDimensions.everbrightKey())
            ? SkiesVillagerTrades.EVERBRIGHT_STICKS
            : (
               trader.level().dimension().equals(SkiesDimensions.everdawnKey())
                  ? SkiesVillagerTrades.EVERDAWN_STICKS
                  : Lists.newArrayList(new Item[]{Items.STICK})
            ));
         return new MerchantOffer(
            new ItemStack((ItemLike)stickList.get(rand.nextInt(stickList.size())), 32),
            new ItemStack(SkiesItems.falsite_ingot, this.count),
            this.maxUses,
            this.xpValue,
            0.2F
         );
      }
   }

   static class ToolWithFalsiteTrade implements ItemListing {
      private final int count;
      private final Item toolMaterial;
      private final Item toolItem;
      private final int maxUses;
      private final int xpValue;
      private final int enchantLevel;

      public ToolWithFalsiteTrade(int emeraldCount, Item toolMaterial, Item toolItem, int maxUses, int xpValue, int enchantLevel) {
         this.count = emeraldCount;
         this.toolMaterial = toolMaterial;
         this.toolItem = toolItem;
         this.maxUses = maxUses;
         this.xpValue = xpValue;
         this.enchantLevel = enchantLevel;
      }

      public ToolWithFalsiteTrade(int emeraldCount, Item toolMaterial, Item toolItem, int maxUses, int xpValue) {
         this(emeraldCount, toolMaterial, toolItem, maxUses, xpValue, 0);
      }

      @Nullable
      public MerchantOffer getOffer(Entity trader, RandomSource rand) {
         ItemStack itemstack = new ItemStack(this.toolItem);
         List<Item> stickList = (List<Item>)(trader.level().dimension().equals(SkiesDimensions.everbrightKey())
            ? SkiesVillagerTrades.EVERBRIGHT_STICKS
            : (
               trader.level().dimension().equals(SkiesDimensions.everdawnKey())
                  ? SkiesVillagerTrades.EVERDAWN_STICKS
                  : Lists.newArrayList(new Item[]{Items.STICK})
            ));
         ToolUtils.setStickType(itemstack, stickList.get(rand.nextInt(stickList.size())));
         IFalsiteItem.applyFalsite(itemstack);
         if (this.enchantLevel > 0) {
            EnchantmentHelper.enchantItem(rand, itemstack, this.enchantLevel, false);
         }

         return new MerchantOffer(new ItemStack(Items.EMERALD, this.count), new ItemStack(this.toolMaterial), itemstack, this.maxUses, this.xpValue, 0.2F);
      }
   }
}

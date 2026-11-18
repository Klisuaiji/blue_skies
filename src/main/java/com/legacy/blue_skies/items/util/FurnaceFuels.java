package com.legacy.blue_skies.items.util;

import com.legacy.blue_skies.items.tools.weapons.SpearItem;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.ItemLike;

public class FurnaceFuels {
   private static final Map<Item, Integer> itemBurnTimes = new HashMap<Item, Integer>() {
      private static final long serialVersionUID = 4106468353841L;

      {
         int coalValue = 1600;
         int snowcapValue = 1200;
         this.put(SkiesBlocks.snowcap_mushroom, snowcapValue);
         this.put(SkiesBlocks.snowcap_pinhead, snowcapValue / 4);
         this.put(SkiesBlocks.snowcap_mushroom_block, snowcapValue);
         this.put(SkiesBlocks.snowcap_mushroom_stem, snowcapValue + 400);
         this.put(SkiesBlocks.blaze_bud, 200);
         int horizoniteValue = 1400;
         this.put(SkiesItems.horizonite_ingot, horizoniteValue);
         this.put(SkiesItems.horizonite_nugget, horizoniteValue / 10);
         this.put(SkiesBlocks.horizonite_block, horizoniteValue * 10);
         this.put(SkiesItems.raw_horizonite, (int)(horizoniteValue * 0.8));
         this.put(SkiesBlocks.raw_horizonite_block, (int)(horizoniteValue * 0.8 * 10.0));
         this.put(SkiesItems.horizonite_pickaxe, horizoniteValue * 3);
         this.put(SkiesItems.horizonite_axe, horizoniteValue * 3);
         this.put(SkiesItems.horizonite_shovel, horizoniteValue);
         this.put(SkiesItems.horizonite_hoe, horizoniteValue * 2);
         this.put(SkiesItems.horizonite_sword, horizoniteValue * 2);
         this.put(SkiesItems.horizonite_helmet, horizoniteValue * 5);
         this.put(SkiesItems.horizonite_chestplate, horizoniteValue * 8);
         this.put(SkiesItems.horizonite_leggings, horizoniteValue * 7);
         this.put(SkiesItems.horizonite_boots, horizoniteValue * 4);
         this.put(SkiesItems.ventium_lava_bucket, 20000);
         this.put(SkiesBlocks.sunstone_crystal, coalValue);
         this.put(SkiesBlocks.sunstone_block, coalValue * 10);
         this.putMultiple(
            100,
            SkiesItems.bluebright_stick,
            SkiesItems.starlit_stick,
            SkiesItems.frostbright_stick,
            SkiesItems.lunar_stick,
            SkiesItems.dusk_stick,
            SkiesItems.maple_stick,
            SkiesItems.comet_stick
         );
         this.putMultiple(
            300,
            SkiesBlocks.bluebright_fence_gate,
            SkiesBlocks.starlit_fence_gate,
            SkiesBlocks.frostbright_fence_gate,
            SkiesBlocks.lunar_fence_gate,
            SkiesBlocks.dusk_fence_gate,
            SkiesBlocks.maple_fence_gate,
            SkiesBlocks.comet_fence_gate,
            SkiesBlocks.bluebright_fence,
            SkiesBlocks.starlit_fence,
            SkiesBlocks.frostbright_fence,
            SkiesBlocks.lunar_fence,
            SkiesBlocks.dusk_fence,
            SkiesBlocks.maple_fence,
            SkiesBlocks.comet_fence,
            SkiesBlocks.bluebright_ladder,
            SkiesBlocks.starlit_ladder,
            SkiesBlocks.frostbright_ladder,
            SkiesBlocks.lunar_ladder,
            SkiesBlocks.dusk_ladder,
            SkiesBlocks.maple_ladder,
            SkiesBlocks.comet_ladder,
            SkiesBlocks.bluebright_crafting_table,
            SkiesBlocks.starlit_crafting_table,
            SkiesBlocks.frostbright_crafting_table,
            SkiesBlocks.lunar_crafting_table,
            SkiesBlocks.dusk_crafting_table,
            SkiesBlocks.maple_crafting_table,
            SkiesBlocks.comet_crafting_table,
            SkiesBlocks.bluebright_bookshelf,
            SkiesBlocks.starlit_bookshelf,
            SkiesBlocks.frostbright_bookshelf,
            SkiesBlocks.lunar_bookshelf,
            SkiesBlocks.dusk_bookshelf,
            SkiesBlocks.maple_bookshelf,
            SkiesBlocks.comet_bookshelf,
            SkiesBlocks.bluebright_chest,
            SkiesBlocks.starlit_chest,
            SkiesBlocks.frostbright_chest,
            SkiesBlocks.lunar_chest,
            SkiesBlocks.dusk_chest,
            SkiesBlocks.maple_chest,
            SkiesBlocks.comet_chest
         );
      }

      private void putMultiple(Integer time, ItemLike... providers) {
         for (ItemLike prov : providers) {
            this.put(prov, time);
         }
      }

      public Integer put(ItemLike key, Integer value) {
         return super.put(key.asItem(), value);
      }
   };

   public static int getBurnTime(Item item) {
      Integer burnTime = itemBurnTimes.get(item);
      if (burnTime != null) {
         return burnTime;
      } else {
         if (item instanceof TieredItem) {
            Tier tier = ((TieredItem)item).getTier();
            if (tier == SkiesItemTier.WOOD || tier == SkiesItemTier.COMET || tier == Tiers.WOOD) {
               return 200;
            }
         } else if (item instanceof SpearItem spear) {
            Integer spearBurnTime = itemBurnTimes.get(spear.getStick());
            if (spearBurnTime != null) {
               return spearBurnTime * 2;
            }
         }

         return -1;
      }
   }
}

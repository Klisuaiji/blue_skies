package com.legacy.blue_skies.data.providers;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.HorizoniteForgeFuel;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class HorizoniteForgeFuelProv extends SkiesDataProvider<HorizoniteForgeFuel> {
   public HorizoniteForgeFuelProv(DataGenerator generator) {
      super(generator, HorizoniteForgeFuel.getDirectory());
   }

   @Override
   protected Map<ResourceLocation, HorizoniteForgeFuel> gatherData() {
      Map<ResourceLocation, HorizoniteForgeFuel> registry = new HashMap<>();
      this.register(registry, SkiesItems.horizonite_ingot, 0.2);
      this.register(registry, SkiesItems.horizonite_nugget, 0.05);
      this.register(registry, SkiesBlocks.horizonite_block, 0.8);
      this.register(registry, SkiesItems.raw_horizonite, 0.15);
      this.register(registry, SkiesBlocks.raw_horizonite_block, 0.6);
      this.register(registry, SkiesBlocks.sunstone_crystal, 0.4);
      this.register(registry, SkiesBlocks.sunstone_block, 1.0);
      return registry;
   }

   private void register(Map<ResourceLocation, HorizoniteForgeFuel> registry, Item fuel, double fuelAmount) {
      registry.put(BlueSkies.locate(BuiltInRegistries.ITEM.getKey(fuel).getPath()), new HorizoniteForgeFuel(fuel, (float)fuelAmount));
   }

   private void register(Map<ResourceLocation, HorizoniteForgeFuel> registry, Block fuel, double fuelAmount) {
      this.register(registry, fuel.asItem(), fuelAmount);
   }
}

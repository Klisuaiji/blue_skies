package com.legacy.blue_skies.data.providers;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.data.objects.ToolHandleType;
import com.legacy.blue_skies.registries.SkiesItems;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ToolHandleTypeProv extends SkiesDataProvider<ToolHandleType> {
   public ToolHandleTypeProv(DataGenerator gen) {
      super(gen, ToolHandleType.getDirectory());
   }

   @Override
   protected Map<ResourceLocation, ToolHandleType> gatherData() {
      Map<ResourceLocation, ToolHandleType> registry = new HashMap<>();
      this.register(registry, ToolHandleType.DEFAULT);
      this.register(registry, 1.0F, 1.0F, 1.0F, SkiesItems.bluebright_stick, Integer.parseInt("6171A2", 16));
      this.register(registry, 1.0F, 1.0F, 1.0F, SkiesItems.lunar_stick, Integer.parseInt("9860AC", 16));
      this.register(registry, 0.9F, 0.95F, 1.1F, SkiesItems.dusk_stick, Integer.parseInt("653C6C", 16));
      this.register(registry, 0.9F, 0.95F, 1.1F, SkiesItems.starlit_stick, Integer.parseInt("92ADB2", 16));
      this.register(registry, 1.1F, 1.05F, 0.9F, SkiesItems.frostbright_stick, Integer.parseInt("705045", 16));
      this.register(registry, 1.1F, 1.05F, 0.9F, SkiesItems.maple_stick, Integer.parseInt("AB5745", 16));
      this.register(registry, 1.2F, 1.05F, 0.9F, SkiesItems.comet_stick, Integer.parseInt("B36371", 16));
      return registry;
   }

   public void register(Map<ResourceLocation, ToolHandleType> registry, float durability, float attackSpeed, float projectileDamage, Item stick, int color) {
      this.register(registry, new ToolHandleType(durability, attackSpeed, projectileDamage, stick, color));
   }

   public void register(Map<ResourceLocation, ToolHandleType> registry, ToolHandleType toolHandleType) {
      registry.put(BlueSkies.locate(BuiltInRegistries.ITEM.getKey(toolHandleType.getStick()).getPath()), toolHandleType);
      BlueSkiesData.TOOL_HANDLE_TYPES.getData().put(toolHandleType.getStick(), toolHandleType);
   }
}

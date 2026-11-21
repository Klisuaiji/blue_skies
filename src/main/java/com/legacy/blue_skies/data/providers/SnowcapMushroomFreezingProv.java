package com.legacy.blue_skies.data.providers;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.SnowcapMushroomFreezing;
import com.legacy.blue_skies.registries.SkiesBlocks;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.properties.Property;

public class SnowcapMushroomFreezingProv extends SkiesDataProvider<SnowcapMushroomFreezing> {
   public SnowcapMushroomFreezingProv(DataGenerator gen) {
      super(gen, SnowcapMushroomFreezing.getDirectory());
   }

   @Override
   protected Map<ResourceLocation, SnowcapMushroomFreezing> gatherData() {
      Map<ResourceLocation, SnowcapMushroomFreezing> registry = new HashMap<>();
      this.register(registry, "extinguish_fire", Blocks.FIRE, Blocks.AIR);
      this.register(registry, "extinguish_soul_fire", Blocks.SOUL_FIRE, Blocks.AIR);
      this.register(registry, "extinguish_blue_fire", SkiesBlocks.blue_fire, Blocks.AIR);
      this.register(registry, "extinguish_black_fire", SkiesBlocks.black_fire, Blocks.AIR);
      this.register(registry, "freeze_water", Blocks.WATER, Blocks.ICE);
      this.register(registry, "freeze_lava", Blocks.LAVA, Blocks.OBSIDIAN);
      this.register(registry, "cool_magma_block", Blocks.MAGMA_BLOCK, Blocks.COBBLESTONE);
      Map<Property<?>, Comparable<?>> campfireProp = Map.of(CampfireBlock.LIT, false);
      this.register(registry, "extinguish_campfire", Blocks.CAMPFIRE, Blocks.CAMPFIRE, campfireProp, true);
      this.register(registry, "extinguish_soul_campfire", Blocks.SOUL_CAMPFIRE, Blocks.SOUL_CAMPFIRE, campfireProp, true);
      this.register(registry, "extinguish_jack_o_lantern", Blocks.JACK_O_LANTERN, Blocks.CARVED_PUMPKIN, new HashMap<>(), true);
      this.register(registry, "cool_horizonite_block", SkiesBlocks.horizonite_block, Blocks.IRON_BLOCK);
      this.register(registry, "cool_raw_horizonite_block", SkiesBlocks.raw_horizonite_block, Blocks.RAW_IRON_BLOCK);
      return registry;
   }

   private void register(Map<ResourceLocation, SnowcapMushroomFreezing> registry, String name, Block block, Block frozenBlock) {
      this.register(registry, name, block, frozenBlock, new HashMap<>(), false);
   }

   private void register(
      Map<ResourceLocation, SnowcapMushroomFreezing> registry,
      String name,
      Block block,
      Block frozenBlock,
      Map<Property<?>, Comparable<?>> states,
      boolean preserveState
   ) {
      Map<String, String> statesMap = states.entrySet()
         .stream()
         .collect(
            Collectors.toMap(
               e -> e.getKey().getName(),
               e -> e.getValue() instanceof StringRepresentable ? ((StringRepresentable)e.getValue()).getSerializedName() : e.getValue().toString()
            )
         );
      registry.put(BlueSkies.locate(name), new SnowcapMushroomFreezing(block, frozenBlock, statesMap, preserveState));
   }
}

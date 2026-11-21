package com.legacy.blue_skies.data.objects;

import com.google.gson.JsonObject;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public class SnowcapMushroomFreezing implements IToJson<SnowcapMushroomFreezing> {
   public final Block block;
   public final Block frozenBlock;
   public final Map<String, String> states;
   public final boolean preserveStates;

   public SnowcapMushroomFreezing(Block block, Block frozenBlock, Map<String, String> states, boolean preserveStates) {
      this.block = block;
      this.frozenBlock = frozenBlock;
      this.states = states;
      this.preserveStates = preserveStates;
   }

   @Override
   public JsonObject toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("block", BuiltInRegistries.BLOCK.getKey(this.block).toString());
      json.addProperty("preserve_states", this.preserveStates);
      json.addProperty("frozen_block", BuiltInRegistries.BLOCK.getKey(this.frozenBlock).toString());
      JsonObject statesJson = new JsonObject();
      this.states.forEach((state, value) -> statesJson.addProperty(state, value));
      json.add("states", statesJson);
      return json;
   }

   public static String getDirectory() {
      return "snowcap_mushroom_freezing";
   }
}

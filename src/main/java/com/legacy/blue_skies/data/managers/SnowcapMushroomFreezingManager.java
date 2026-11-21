package com.legacy.blue_skies.data.managers;

import com.google.common.collect.UnmodifiableIterator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.legacy.blue_skies.data.objects.SnowcapMushroomFreezing;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class SnowcapMushroomFreezingManager extends SkiesDataManager<Block, List<SnowcapMushroomFreezing>> {
   public SnowcapMushroomFreezingManager() {
      super(SnowcapMushroomFreezing.getDirectory());
   }

   @Override
   protected Pair<Block, List<SnowcapMushroomFreezing>> parseJson(JsonObject json, ResourceLocation name) throws SkiesDataManager.RegistryObjectNotFoundException {
      Block block = parseBlock(json, "block");
      boolean preserveStates = GsonHelper.getAsBoolean(json, "preserve_states", false);
      Block frozenBlock = parseBlock(json, "frozen_block");
      Map<String, String> states = new HashMap<>();
      JsonObject statesJson = GsonHelper.getAsJsonObject(json, "states", new JsonObject());

      for (Entry<String, JsonElement> stateEntry : statesJson.entrySet()) {
         states.put(stateEntry.getKey(), stateEntry.getValue().getAsString());
      }

      return Pair.of(block, new ArrayList<>(Arrays.asList(new SnowcapMushroomFreezing(block, frozenBlock, states, preserveStates))));
   }

   protected void registerData(Map<Block, List<SnowcapMushroomFreezing>> activeData, Block key, List<SnowcapMushroomFreezing> value) {
      List<SnowcapMushroomFreezing> oldValue = activeData.get(key);
      if (oldValue != null) {
         oldValue.addAll(value);
      } else {
         super.registerData(activeData, key, value);
      }
   }

   @Nullable
   public BlockState getState(BlockState state, RandomSource rand) {
      if (state.hasProperty(LiquidBlock.LEVEL) && (Integer)state.getValue(LiquidBlock.LEVEL) > 0) {
         return null;
      }

      List<SnowcapMushroomFreezing> freezings = this.getValue(state.getBlock());
      return freezings != null && !freezings.isEmpty() ? this.getFrozenState(freezings.get(rand.nextInt(freezings.size())), state) : null;
   }

   @Nullable
   public BlockState getFrozenState(SnowcapMushroomFreezing freezingRecipe, BlockState state) {
      BlockState frozenState = freezingRecipe.frozenBlock.defaultBlockState();
      if (freezingRecipe.preserveStates) {
         UnmodifiableIterator properties = state.getValues().entrySet().iterator();

         while (properties.hasNext()) {
            Entry<Property<?>, Comparable<?>> entry = (Entry<Property<?>, Comparable<?>>)properties.next();
            if (frozenState.hasProperty(entry.getKey())) {
               frozenState = (BlockState)frozenState.setValue(entry.getKey(), entry.getValue());
            }
         }
      }

      Map<String, Property<?>> properties = frozenState.getProperties().stream().collect(Collectors.toMap(propx -> propx.getName(), propx -> propx));

      for (Entry<String, String> entry : freezingRecipe.states.entrySet()) {
         if (properties.containsKey(entry.getKey())) {
            Property prop = properties.get(entry.getKey());
            Optional<Comparable> value = prop.getValue(entry.getValue());
            if (value.isPresent()) {
               frozenState = (BlockState)frozenState.setValue(prop, value.get());
            }
         }
      }

      return state.equals(frozenState) ? null : frozenState;
   }
}

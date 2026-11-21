package com.legacy.blue_skies.data.managers.journal;

import com.google.gson.JsonObject;
import com.legacy.blue_skies.data.managers.SkiesDataManager;
import com.legacy.blue_skies.data.managers.SkiesSyncedDataManager;
import com.legacy.blue_skies.data.objects.journal.JournalRequirement;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class JournalRequirementManager extends SkiesSyncedDataManager<ResourceLocation, JournalRequirement, JournalRequirementManager> {
   public JournalRequirementManager() {
      super(JournalRequirement.getDirectory());
   }

   public JournalRequirementManager newInstance() {
      return new JournalRequirementManager();
   }

   public Pair<CompoundTag, CompoundTag> entryToNbt(ResourceLocation key, JournalRequirement value) {
      CompoundTag keyNbt = new CompoundTag();
      keyNbt.putString("0", key.toString());
      CompoundTag valueNbt = new CompoundTag();
      if (value.everbrightProgression > -1) {
         valueNbt.putInt("0", value.everbrightProgression);
      }

      if (value.everdawnProgression > -1) {
         valueNbt.putInt("1", value.everdawnProgression);
      }

      if (value.advancement != null) {
         valueNbt.putString("2", value.advancement.toString());
      }

      return Pair.of(keyNbt, valueNbt);
   }

   @Override
   public Pair<ResourceLocation, JournalRequirement> entryFromNbt(CompoundTag keyNbt, CompoundTag valueNbt) {
      ResourceLocation name = ResourceLocation.parse(keyNbt.getString("0"));
      int everbrightProgression = valueNbt.contains("0") ? valueNbt.getInt("0") : -1;
      int everdawnProgression = valueNbt.contains("1") ? valueNbt.getInt("1") : -1;
      ResourceLocation advancement = valueNbt.contains("2") ? ResourceLocation.parse(valueNbt.getString("2")) : null;
      return Pair.of(name, new JournalRequirement(name, everbrightProgression, everdawnProgression, advancement));
   }

   @Override
   protected Pair<ResourceLocation, JournalRequirement> parseJson(JsonObject json, ResourceLocation name) throws SkiesDataManager.RegistryObjectNotFoundException {
      int everbrightProgression = GsonHelper.getAsInt(json, "everbright_progression", -1);
      int everdawnProgression = GsonHelper.getAsInt(json, "everdawn_progression", -1);
      ResourceLocation advancement = json.has("advancement") ? ResourceLocation.parse(GsonHelper.getAsString(json, "advancement")) : null;
      return Pair.of(name, new JournalRequirement(name, everbrightProgression, everdawnProgression, advancement));
   }
}

package com.legacy.blue_skies.data.managers.journal;

import com.google.gson.JsonObject;
import com.legacy.blue_skies.data.managers.SkiesDataManager;
import com.legacy.blue_skies.data.managers.SkiesSyncedDataManager;
import com.legacy.blue_skies.data.objects.journal.JournalSection;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class JournalSectionManager extends SkiesSyncedDataManager<ResourceLocation, JournalSection, JournalSectionManager> {
   public JournalSectionManager() {
      super(JournalSection.getDirectory());
   }

   public JournalSectionManager newInstance() {
      return new JournalSectionManager();
   }

   public Pair<CompoundTag, CompoundTag> entryToNbt(ResourceLocation key, JournalSection value) {
      CompoundTag keyNbt = new CompoundTag();
      keyNbt.putString("0", key.toString());
      CompoundTag valueNbt = new CompoundTag();
      valueNbt.putString("0", value.name.toString());
      valueNbt.putString("1", value.displayText);
      valueNbt.putString("2", value.texture.toString());
      return Pair.of(keyNbt, valueNbt);
   }

   @Override
   public Pair<ResourceLocation, JournalSection> entryFromNbt(CompoundTag keyNbt, CompoundTag valueNbt) {
      ResourceLocation key = ResourceLocation.parse(keyNbt.getString("0"));
      ResourceLocation name = ResourceLocation.parse(valueNbt.getString("0"));
      String displayText = valueNbt.getString("1");
      ResourceLocation texture = ResourceLocation.parse(valueNbt.getString("2"));
      return Pair.of(key, new JournalSection(name, displayText, texture));
   }

   @Override
   protected Pair<ResourceLocation, JournalSection> parseJson(JsonObject json, ResourceLocation name) throws SkiesDataManager.RegistryObjectNotFoundException {
      ResourceLocation _name = ResourceLocation.parse(GsonHelper.getAsString(json, "name"));
      String displayText = GsonHelper.getAsString(json, "display_text");
      ResourceLocation texture = ResourceLocation.parse(GsonHelper.getAsString(json, "texture"));
      return Pair.of(_name, new JournalSection(_name, displayText, texture));
   }
}

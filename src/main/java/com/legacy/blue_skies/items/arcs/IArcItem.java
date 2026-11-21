package com.legacy.blue_skies.items.arcs;

import com.legacy.blue_skies.compat.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public interface IArcItem {
   void serverTick(ItemStack var1, ServerPlayer var2);

   @OnlyIn(Dist.CLIENT)
   void clientTick(ItemStack var1, LocalPlayer var2);

   void onEquip(ItemStack var1, Player var2);

   void onUnequip(ItemStack var1, Player var2);

   void refresh(ItemStack var1, Player var2);

   void onHit(ItemStack var1, Player var2, LivingDamageEvent var3);

   int getMaxLevel();

   default int getLevel(ItemStack stack) {
      CompoundTag nbt = LegacyNbt.getOrCreate(stack);
      return nbt.getInt("ArcLevel");
   }

   int getFunctionalLevel(ItemStack var1, Player var2);

   default void setLevel(ItemStack stack, int level) {
      CompoundTag nbt = LegacyNbt.getOrCreate(stack);
      nbt.putInt("ArcLevel", Mth.clamp(level, 0, this.getMaxLevel() > 0 ? this.getMaxLevel() - 1 : 0));
   }
}

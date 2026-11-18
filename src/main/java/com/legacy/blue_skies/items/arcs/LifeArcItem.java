package com.legacy.blue_skies.items.arcs;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class LifeArcItem extends ArcItem {
   public LifeArcItem(int arcId) {
      super(arcId, "life", new Properties().stacksTo(1));
   }

   @Override
   public void serverTick(ItemStack stack, ServerPlayer player) {
      if (player.getRemainingFireTicks() > 0 && this.getFunctionalLevel(stack, player) >= 3 && player.tickCount % 60 == 0) {
         List<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects());
         effects.forEach(effect -> {
            if (effect.getEffect().getCategory().equals(MobEffectCategory.HARMFUL)) {
               player.removeEffect(effect.getEffect());
            }
         });
      }
   }

   @Override
   public void onHit(ItemStack stack, Player player, LivingDamageEvent event) {
      int level = this.getFunctionalLevel(stack, player);
      float reductionHealth = level == 0 ? 4.0F : (level == 1 ? 8.0F : 12.0F);
      if (player.getRemainingFireTicks() > 0 && player.getHealth() <= reductionHealth) {
         event.setAmount(event.getAmount() / 2.0F);
      }
   }
}

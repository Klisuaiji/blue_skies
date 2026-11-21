package com.legacy.blue_skies.entities.util;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.asm_hooks.PlayerHooks;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.util.EntityUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SkiesEntityHooks {
   public static float nerfDamage(DamageSource source, float amount) {
      if (source.getDirectEntity() instanceof Player player && SkiesDimensions.inSkyDimension(player)) {
         float def = Math.min(7.0F, amount * 0.3F);
         ItemStack weapon = player.getMainHandItem();
         return SkiesPlayer.getIfPresent(
            player,
            skyPlayer -> {
               if (!EntityUtil.hasPlayerCompletedProgression(skyPlayer)
                  && player.getAttributeValue(Attributes.ATTACK_DAMAGE) > 1.0
                  && PlayerHooks.isNerfableTool(weapon, null)) {
                  player.displayClientMessage(Component.translatable("gui.blue_skies.tooltip.invalid_weapon"), true);
                  return def;
               } else {
                  return amount;
               }
            },
            () -> {
               BlueSkies.LOGGER
                  .error(
                     "Blue Skies' player capability wasn't present for {} when trying to do damage progression checks. This should be reported if happening consistently!",
                     player.getUUID().toString()
                  );
               return def;
            }
         );
      } else {
         return amount;
      }
   }

   public static float nerfIndirectDamage(DamageSource source, float amount) {
      float def = Math.min(5.0F, amount);
      if (source.getEntity() != null && source.getEntity() instanceof Player player && SkiesDimensions.inSkyDimension(source.getEntity())) {
         ResourceLocation name = BuiltInRegistries.ENTITY_TYPE.getKey(source.getDirectEntity().getType());
         return SkiesPlayer.getIfPresent(
            player,
            skyPlayer -> EntityUtil.hasPlayerCompletedProgression(skyPlayer) || name != null && (name == null || name.getNamespace().equals("blue_skies"))
               ? amount
               : def,
            () -> {
               BlueSkies.LOGGER
                  .error(
                     "Blue Skies' player capability wasn't present for {} when trying to do indirect damage progression checks. This should be reported if happening consistently!",
                     player.getUUID().toString()
                  );
               return def;
            }
         );
      } else {
         return def;
      }
   }
}

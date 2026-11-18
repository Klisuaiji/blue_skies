package com.legacy.blue_skies.asm_hooks;

import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.entities.hostile.boss.ArachnarchEntity;
import com.legacy.blue_skies.items.SkyArmorItem;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.util.EntityUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class LivingEntityHooks {
   public static int getArmorValue(int original, LivingEntity entity) {
      return entity instanceof Player player && player != null && SkiesDimensions.inSkyDimension(entity) ? SkiesPlayer.getIfPresent(player, skyPlayer -> {
         if (!EntityUtil.hasPlayerCompletedProgression(skyPlayer)) {
            int i = 0;

            for (ItemStack stack : player.getArmorSlots()) {
               if (!(stack.getItem() instanceof SkyArmorItem) && stack.getItem() instanceof ArmorItem armor) {
                  ArmorMaterial material = armor.getMaterial();
                  if (material != ArmorMaterials.LEATHER && material != ArmorMaterials.GOLD && material != ArmorMaterials.TURTLE) {
                     i += Math.max(2, Math.min(4, armor.getDefense() / 2));
                  }
               }
            }

            if (original > 1 && i < original) {
               return original - i;
            }
         }

         return original;
      }, () -> original) : original;
   }

   public static boolean shouldRepositionDismount(LivingEntity rider, Entity mount) {
      if (mount instanceof ArachnarchEntity boss) {
         Vec3 vec3d = boss.getViewVector(1.0F);
         rider.teleportTo(
            boss.getX() + vec3d.x * 3.0,
            boss.getY()
               + rider.getMyRidingOffset(mount)
               + (boss.isCeilingLocked() ? -Mth.cos(boss.tickCount * 0.1F) : Mth.cos(boss.tickCount * 0.1F)) * 0.05
               + (boss.isCeilingLocked() ? -1.2F : 1.4),
            boss.getZ() + vec3d.z * 3.0
         );
         return true;
      } else {
         return false;
      }
   }
}

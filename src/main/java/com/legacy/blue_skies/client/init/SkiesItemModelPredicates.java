package com.legacy.blue_skies.client.init;

import com.legacy.blue_skies.compat.*;
import com.google.common.collect.ImmutableSet;
import com.legacy.blue_skies.blocks.misc.TrophyBlock;
import com.legacy.blue_skies.items.tools.AstrolabeItem;
import com.legacy.blue_skies.items.tools.weapons.InfusedArcSwordItem;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import java.util.Set;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class SkiesItemModelPredicates {
   public static void init() {
      Set<Item> spears = ImmutableSet.of(
         SkiesItems.bluebright_spear,
         SkiesItems.starlit_spear,
         SkiesItems.frostbright_spear,
         SkiesItems.lunar_spear,
         SkiesItems.dusk_spear,
         SkiesItems.maple_spear,
         new Item[]{SkiesItems.soulbound_spear}
      );
      spears.forEach(spear -> createActivePredicate(spear, "throwing"));
      createActivePredicate(SkiesItems.moonstone_shield, "blocking");
      createActivePredicate(SkiesItems.spike_shield, "blocking");
      createActivePredicate(SkiesItems.venom_sac, "pulling");
      createUseDurationPredicate(SkiesItems.venom_sac, "pull", 20);
      createChargedPredicate(SkiesItems.crushing_hammer, "charged", 20);
      Set<Item> arcs = ImmutableSet.of(
         SkiesItems.ethereal_arc,
         SkiesItems.dusk_arc,
         SkiesItems.nature_arc,
         SkiesItems.poison_arc,
         SkiesItems.aquatic_arc,
         SkiesItems.life_arc,
         new Item[]{SkiesItems.runic_arc}
      );
      arcs.forEach(arc -> createCooldownPredicate(arc, "drained"));
      createInfusedArcSwordPredicate(SkiesItems.infused_arc_sword, "drained");
      createTrophyPredicate(SkiesBlocks.summoner_trophy, SkiesBlocks.alchemist_trophy, SkiesBlocks.starlit_crusher_trophy, SkiesBlocks.arachnarch_trophy);
      createAstrolabePredicate(SkiesItems.astrolabe, "can_use");
   }

   private static void createChargedPredicate(Item item, String predicateName, int maxUseTime) {
      ItemProperties.register(
         item,
         ResourceLocation.parse(predicateName),
         (stack, world, entity, num) -> entity != null
               && entity.getUseItem() == stack
               && entity.getUseItemRemainingTicks() <= stack.getUseDuration(entity) - maxUseTime
            ? 1.0F
            : 0.0F
      );
   }

   private static void createUseDurationPredicate(Item item, String predicateName, int maxUseTime) {
      ItemProperties.register(
         item,
         ResourceLocation.parse(predicateName),
         (stack, world, entity, num) -> entity != null && entity.getUseItem() == stack
            ? (float)(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / maxUseTime
            : 0.0F
      );
   }

   private static void createActivePredicate(Item item, String predicateName) {
      ItemProperties.register(
         item,
         ResourceLocation.parse(predicateName),
         (stack, world, entity, num) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
      );
   }

   private static void createCooldownPredicate(Item item, String predicateName) {
      ItemProperties.register(
         item,
         ResourceLocation.parse(predicateName),
         (stack, world, entity, num) -> entity != null && entity instanceof Player && ((Player)entity).getCooldowns().isOnCooldown(stack.getItem())
            ? 1.0F
            : 0.0F
      );
   }

   private static void createInfusedArcSwordPredicate(Item item, String predicateName) {
      ItemProperties.register(
         item,
         ResourceLocation.parse(predicateName),
         (stack, world, entity, num) -> stack.getItem() instanceof InfusedArcSwordItem && InfusedArcSwordItem.getInfusedCooldown(stack) > 0 ? 1.0F : 0.0F
      );
   }

   private static void createTrophyPredicate(Block... blocks) {
      for (Block b : blocks) {
         ItemProperties.register(b.asItem(), ResourceLocation.parse("tier"), (stack, world, entity, num) -> {
            CompoundTag tag = LegacyNbt.get(stack);
            if (tag != null) {
               CompoundTag states = tag.getCompound("BlockStateTag");
               String tier = states.getString("tier");
               return TrophyBlock.Tier.from(tier).ordinal();
            } else {
               return 0.0F;
            }
         });
      }
   }

   private static void createAstrolabePredicate(Item item, String predicateName) {
      ItemProperties.register(
         item,
         ResourceLocation.parse(predicateName),
         (stack, world, entity, num) -> AstrolabeItem.getTeleportCooldown(stack) == 0 && AstrolabeItem.isWithinDistance(stack, entity, world) ? 1.0F : 0.0F
      );
   }
}

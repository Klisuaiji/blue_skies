package com.legacy.blue_skies.asm_hooks;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.blocks.misc.SkyWebbingBlock;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.data.objects.tags.SkiesEntityTags;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.util.EntityUtil;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PlayerHooks {
   private static final Set<Item> NERFED_ITEMS_CACHE = new HashSet<>();
   private static final String[] NERFABLE_ITEM_NAMES = new String[]{
      "axe",
      "pickaxe",
      "shovel",
      "hoe",
      "hammer",
      "mattock",
      "pickadze",
      "excavator",
      "kama",
      "scythe",
      "sword",
      "dagger",
      "cleaver",
      "rapier",
      "saber",
      "scabbard",
      "scimitar",
      "shortsword",
      "greatsword",
      "katana",
      "spear"
   };

   public static boolean cannotDismount(Player player) {
      return player.isPassenger() && player.getRootVehicle().getType().is(SkiesEntityTags.BOSSES);
   }

   public static float modifyBreakSpeed(float speed, BlockState state, BlockPos pos, Player player) {
      Block block = state.getBlock();
      Item item = player.getMainHandItem().getItem();
      if (item != null && pos != null) {
         if (webHarvestCheck(block, item)) {
            return 15.0F;
         }

         if (SkiesDimensions.inSkyDimension(player) && isNerfableTool(player.getMainHandItem(), state)) {
            return SkiesPlayer.getIfPresent(player, skyPlayer -> {
               if (!EntityUtil.hasPlayerCompletedProgression(skyPlayer)) {
                  player.displayClientMessage(Component.translatable("gui.blue_skies.tooltip.invalid_tool"), true);
                  return 0.999F;
               } else {
                  return speed;
               }
            }, () -> speed);
         }
      }

      return speed;
   }

   public static synchronized boolean isNerfableTool(ItemStack stack, BlockState state) {
      if (stack == null) {
         return false;
      }

      if (NERFED_ITEMS_CACHE.size() > 100) {
         NERFED_ITEMS_CACHE.clear();
      }

      Item item = stack.getItem();
      ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(item);
      if (registryName != null && "blue_skies".equals(registryName.getNamespace())) {
         return false;
      }

      if (registryName == null) {
         return true;
      }

      if (!NERFED_ITEMS_CACHE.contains(item)) {
         tryAddToNerfedCache(item, registryName);
      }

      return !NERFED_ITEMS_CACHE.contains(item) ? false : state == null || stack.isCorrectToolForDrops(state);
   }

   private static void tryAddToNerfedCache(Item item, ResourceLocation registryName) {
      if (item instanceof TieredItem tiered) {
         Tier tier = tiered.getTier();
         if (tier != Tiers.WOOD && tier != Tiers.STONE && tier != Tiers.GOLD) {
            NERFED_ITEMS_CACHE.add(item);
         }
      } else {
         String name = registryName.getPath();

         for (String nerfedName : NERFABLE_ITEM_NAMES) {
            if (name.equals(nerfedName) || name.endsWith(nerfedName)) {
               NERFED_ITEMS_CACHE.add(item);
               return;
            }
         }
      }
   }

   public static boolean isBreakingNerfed(Item item, Player player, BlockState state) {
      return item != null
         && SkiesDimensions.inSkyDimension(player)
         && isNerfableTool(player.getMainHandItem(), state)
         && !state.is(Tiers.WOOD.getIncorrectBlocksForDrops());
   }

   public static boolean webHarvestCheck(Block block, Item item) {
      return block instanceof WebBlock && item == SkiesItems.ventium_shears
         || block instanceof SkyWebbingBlock && new net.minecraft.world.item.ItemStack(item).isCorrectToolForDrops(Blocks.COBWEB.defaultBlockState());
   }
}

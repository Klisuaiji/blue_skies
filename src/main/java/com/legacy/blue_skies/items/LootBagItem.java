package com.legacy.blue_skies.items;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.items.util.IRarityItem;
import com.legacy.blue_skies.items.util.ISpecialTabItem;
import com.legacy.blue_skies.registries.SkiesSounds;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class LootBagItem extends Item implements IRarityItem, ISpecialTabItem {
   private static final String LOOT_TABLE_KEY = "LootTable";

   public LootBagItem(Properties properties) {
      super(properties);
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      CompoundTag stackNbt = stack.getTag();
      if (!player.getAbilities().instabuild) {
         player.setItemInHand(hand, ItemStack.EMPTY);
      }

      ResourceLocation lootTableLocation = getLootTable(stack);
      if (lootTableLocation != null) {
         if (!level.isClientSide) {
            getRandomItems(level, player, lootTableLocation)
               .forEach(item -> level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), item)));
            level.playSound(
               (Player)null,
               player.getX(),
               player.getY(),
               player.getZ(),
               SkiesSounds.ITEM_BAG_OPEN,
               SoundSource.BLOCKS,
               1.0F,
               level.random.nextFloat() * 0.1F + 1.0F
            );
            player.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
            player.awardStat(Stats.ITEM_USED.get(this));
         }

         return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
      } else {
         BlueSkies.LOGGER.error("Tag not valid: {}", stackNbt);
         return InteractionResultHolder.fail(stack);
      }
   }

   public static List<ItemStack> getRandomItems(Level level, Player player, @Nullable ResourceLocation lootTableLocation) {
      if (lootTableLocation == null) {
         return List.of();
      } else {
         LootTable lootTable = level.getServer().getLootData().getLootTable(lootTableLocation);
         LootParams ctx = new Builder((ServerLevel)level).withLuck(player.getLuck()).create(LootContextParamSets.EMPTY);
         return lootTable.getRandomItems(ctx);
      }
   }

   @Nullable
   public static ResourceLocation getLootTable(ItemStack stack) {
      CompoundTag nbt = stack.getTag();
      if (nbt != null && nbt.contains("LootTable", 8)) {
         String table = nbt.getString("LootTable");
         if (ResourceLocation.isValidResourceLocation(table)) {
            return new ResourceLocation(table);
         }
      }

      return null;
   }

   public static void setLootTable(ItemStack stack, ResourceLocation table) {
      CompoundTag nbt = stack.getOrCreateTag();
      nbt.putString("LootTable", table.toString());
   }

   @Override
   public void fillTabs(Output output) {
      this.fillItemCategoryWithRarity(output, this, TabVisibility.SEARCH_TAB_ONLY);
      ItemStack stack = new ItemStack(this);
      this.setRarityTag(stack, IRarityItem.SkiesRarity.COMMON);
      output.accept(stack, TabVisibility.PARENT_TAB_ONLY);
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> lore, TooltipFlag flag) {
      this.addRarityText(stack, lore);
      if (getLootTable(stack) != null && flag.isAdvanced()) {
         lore.add(Component.literal(getLootTable(stack).toString()).withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}));
      }
   }
}

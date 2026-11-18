package com.legacy.blue_skies.items.block;

import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;

public class DungeonBlockItem extends BlockItem {
   private final SkiesDungeonType dungeonType;

   public DungeonBlockItem(Block blockIn, Properties builder, SkiesDungeonType dungeonTypeIn) {
      super(blockIn, builder);
      this.dungeonType = dungeonTypeIn;
   }

   public Component getName(ItemStack stack) {
      return Component.empty().append(super.getName(stack)).withStyle(this.getColorFromType());
   }

   private ChatFormatting getColorFromType() {
      switch (this.dungeonType) {
         case BLINDING:
            return ChatFormatting.GRAY;
         case NATURE:
            return ChatFormatting.GREEN;
         case POISON:
            return ChatFormatting.LIGHT_PURPLE;
         case AQUATIC:
            return ChatFormatting.BLUE;
         case LIFE:
            return ChatFormatting.RED;
         case RUNIC:
            return ChatFormatting.GOLD;
         default:
            return ChatFormatting.WHITE;
      }
   }
}

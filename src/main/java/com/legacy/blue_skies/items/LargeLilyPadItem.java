package com.legacy.blue_skies.items;

import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LargeLilyPadItem extends PlaceOnWaterBlockItem {
   public LargeLilyPadItem(Block blockIn, Properties builder) {
      super(blockIn, builder);
   }

   protected boolean mustSurvive() {
      return false;
   }

   protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
      return context.getLevel().setBlock(context.getClickedPos(), state, 27);
   }
}

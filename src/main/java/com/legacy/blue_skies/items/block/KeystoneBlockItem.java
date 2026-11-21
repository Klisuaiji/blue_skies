package com.legacy.blue_skies.items.block;

import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class KeystoneBlockItem extends DungeonBlockItem {
   public KeystoneBlockItem(Block blockIn, Properties builder, SkiesDungeonType dungeonTypeIn) {
      super(blockIn, builder, dungeonTypeIn);
   }

   protected BlockState getPlacementState(BlockPlaceContext context) {
      Player playerentity = context.getPlayer();
      return playerentity != null && !playerentity.isCreative() ? null : super.getPlacementState(context);
   }
}

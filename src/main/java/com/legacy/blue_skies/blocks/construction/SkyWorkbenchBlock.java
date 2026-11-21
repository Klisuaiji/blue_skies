package com.legacy.blue_skies.blocks.construction;

import com.legacy.blue_skies.client.gui.menu.SkyWorkbenchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SkyWorkbenchBlock extends CraftingTableBlock {
   private static final Component GUI_TITLE = Component.translatable("container.crafting");

   public SkyWorkbenchBlock() {
      super(Properties.ofFullCopy(Blocks.CRAFTING_TABLE));
   }

   public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
      return new SimpleMenuProvider((id, inventory, entity) -> new SkyWorkbenchMenu(id, inventory, ContainerLevelAccess.create(worldIn, pos), this), GUI_TITLE);
   }
}

package com.legacy.blue_skies.client.gui.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.block.Block;

public class SkyWorkbenchMenu extends CraftingMenu {
   private final Block workbench;
   private ContainerLevelAccess worldPos;

   public SkyWorkbenchMenu(int id, Inventory playerInv, ContainerLevelAccess worldPos, Block workbench) {
      super(id, playerInv, worldPos);
      this.workbench = workbench;
      this.worldPos = worldPos;
   }

   public boolean stillValid(Player playerIn) {
      return isWithinUsableDistance(this.worldPos, playerIn, this.workbench);
   }

   protected static boolean isWithinUsableDistance(ContainerLevelAccess worldPos, Player playerIn, Block targetBlock) {
      return (Boolean)worldPos.evaluate(
         (world, pos) -> world.getBlockState(pos).getBlock() != targetBlock
            ? false
            : playerIn.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0,
         true
      );
   }
}

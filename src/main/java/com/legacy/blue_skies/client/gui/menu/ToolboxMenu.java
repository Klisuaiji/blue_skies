package com.legacy.blue_skies.client.gui.menu;

import com.legacy.blue_skies.crafting.toolbox.ToolboxRecipe;
import com.legacy.blue_skies.data.managers.ToolboxRecipeManager;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesContainers;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.registries.SkiesTriggers;
import java.util.List;
import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ToolboxMenu extends ItemCombinerMenu {
   private ToolboxRecipe recipe;

   public ToolboxMenu(int id, Inventory playerInv) {
      this(id, playerInv, ContainerLevelAccess.NULL);
   }

   public ToolboxMenu(int id, Inventory playerInv, ContainerLevelAccess worldPos) {
      super(SkiesContainers.TOOLBOX, id, playerInv, worldPos);
   }

   public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
      return slotIn.container != this.resultSlots && super.canTakeItemForPickAll(stack, slotIn);
   }

   protected boolean mayPickup(Player player, boolean flag) {
      return this.recipe != null && this.recipe.matches(this.inputSlots.getItem(0), this.inputSlots.getItem(1));
   }

   protected void onTake(Player player, ItemStack outputStack) {
      if (player instanceof ServerPlayer) {
         SkiesTriggers.USED_TOOL_BOX.trigger((ServerPlayer)player, this.inputSlots.getItem(0), this.inputSlots.getItem(1), outputStack);
      }

      this.access
         .execute(
            (world, pos) -> world.playSound(
               (Player)null,
               pos,
               this.inputSlots.getItem(1).getItem() == SkiesItems.falsite_ingot ? SkiesSounds.BLOCK_TOOL_BOX_USE_FALSITE : SkiesSounds.BLOCK_TOOL_BOX_USE,
               SoundSource.BLOCKS,
               1.0F,
               1.0F
            )
         );
      outputStack.onCraftedBy(player.level(), player, outputStack.getCount());
      this.resultSlots.awardUsedRecipes(player, List.of(this.inputSlots.getItem(0), this.inputSlots.getItem(1)));
      this.shrinkSlot(0);
      this.shrinkSlot(1);
   }

   private void shrinkSlot(int slotId) {
      ItemStack stack = this.inputSlots.getItem(slotId);
      stack.shrink(1);
      this.inputSlots.setItem(slotId, stack);
   }

   protected boolean isValidBlock(BlockState state) {
      return state.getBlock() == SkiesBlocks.tool_box;
   }

   public void createResult() {
      ItemStack inputItem = this.inputSlots.getItem(0);
      ItemStack modifierItem = this.inputSlots.getItem(1);
      Optional<ToolboxRecipe> recipe = ToolboxRecipeManager.INSTANCE.getMatching(inputItem, modifierItem);
      if (recipe.isPresent()) {
         this.recipe = recipe.get();
         this.resultSlots.setItem(0, recipe.get().getResult(inputItem, modifierItem));
      } else {
         this.resultSlots.setItem(0, ItemStack.EMPTY);
      }

      this.broadcastChanges();
   }

   protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
      return ItemCombinerMenuSlotDefinition.create()
         .withSlot(0, 27, 47, p_266883_ -> true)
         .withSlot(1, 76, 47, p_267323_ -> true)
         .withResultSlot(2, 134, 47)
         .build();
   }
}

package com.legacy.blue_skies.block_entity;

import com.legacy.blue_skies.client.gui.menu.SnowcapOvenMenu;
import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.data.objects.FreezingRecipe;
import com.legacy.blue_skies.registries.SkiesBlockEntityTypes;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.util.LogicUtil;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SnowcapOvenBlockEntity extends AbstractFurnaceBlockEntity {
   public final RecipeType<SmeltingRecipe> ovenRecipeType;

   public SnowcapOvenBlockEntity(BlockPos pos, BlockState state) {
      super(SkiesBlockEntityTypes.SNOWCAP_OVEN, pos, state, RecipeType.SMELTING);
      this.items = NonNullList.withSize(4, ItemStack.EMPTY);
      this.ovenRecipeType = RecipeType.SMELTING;
   }

   protected Component getDefaultName() {
      return Component.translatable(SkiesBlocks.snowcap_oven.getDescriptionId());
   }

   protected SnowcapOvenMenu createMenu(int id, Inventory playerInv) {
      return new SnowcapOvenMenu(id, playerInv, this, this.dataAccess);
   }

   public static void ovenServerTick(Level level, BlockPos pos, BlockState state, SnowcapOvenBlockEntity oven) {
      boolean isBurning = oven.isBurning();
      boolean shouldUpdate = false;
      if (oven.isBurning()) {
         oven.setBurnTime(oven.getBurnTime() - 1);
      }

      ItemStack fuelStack = (ItemStack)oven.items.get(1);
      if (oven.isBurning() || !fuelStack.isEmpty() && !((ItemStack)oven.items.get(0)).isEmpty()) {
         RecipeHolder<SmeltingRecipe> recipe = (RecipeHolder<SmeltingRecipe>)level.getRecipeManager()
            .getRecipeFor(oven.ovenRecipeType, oven, level)
            .orElse(null);
         int maxStackSize = oven.getMaxStackSize();
         if (!oven.isBurning() && oven.canBurn(recipe, oven.items, maxStackSize)) {
            oven.setBurnTime(oven.getBurnDuration(fuelStack));
            oven.setRecipesUsed(oven.getBurnTime());
            if (oven.isBurning()) {
               shouldUpdate = true;
               if (fuelStack.hasCraftingRemainingItem()) {
                  oven.freezeFuel(fuelStack);
                  oven.items.set(1, fuelStack.getCraftingRemainingItem());
               } else if (!fuelStack.isEmpty()) {
                  oven.freezeFuel(fuelStack);
                  fuelStack.shrink(1);
                  if (fuelStack.isEmpty()) {
                     oven.items.set(1, fuelStack.getCraftingRemainingItem());
                  }
               }
            }
         }

         if (oven.isBurning() && oven.canBurn(recipe, oven.items, maxStackSize)) {
            oven.setCookTimeVar(oven.getCookTimeVar() + 1);
            if (oven.getCookTimeVar() == oven.getCookTimeTotal()) {
               oven.setCookTimeVar(0);
               oven.setCookTimeTotal(getTotalCookTime(level, oven.ovenRecipeType, oven));
               oven.smelt(recipe);
               shouldUpdate = true;
            }
         } else {
            oven.setCookTimeVar(0);
         }
      } else if (!oven.isBurning() && oven.getCookTimeVar() > 0) {
         oven.setCookTimeVar(Mth.clamp(oven.getCookTimeVar() - 2, 0, oven.getCookTimeTotal()));
      }

      if (isBurning != oven.isBurning()) {
         shouldUpdate = true;
         level.setBlock(pos, (BlockState)level.getBlockState(pos).setValue(AbstractFurnaceBlock.LIT, oven.isBurning()), 3);
      }

      if (shouldUpdate) {
         oven.setChanged();
      }
   }

   private static int getTotalCookTime(Level level, RecipeType<SmeltingRecipe> recipeType, Container container) {
      return level.getRecipeManager()
         .getRecipeFor(recipeType, container, level)
         .map(h -> (SmeltingRecipe)h.value())
         .<Integer>map(AbstractCookingRecipe::getCookingTime)
         .orElse(200);
   }

   protected int getBurnDuration(ItemStack fuel) {
      if (isFuel(fuel)) {
         int value = BlueSkiesData.SNOWCAP_OVEN_FREEZING.getValue(fuel.getItem()).fuelAmount;
         int damage = fuel.getDamageValue();
         if (damage > 0) {
            int maxDamage = fuel.getMaxDamage();
            int scaledValue = (int)((double)(maxDamage - damage) / maxDamage * value);
            return LogicUtil.bind(0, scaledValue, value);
         } else {
            return value;
         }
      } else {
         return 0;
      }
   }

   private void freezeFuel(ItemStack fuelStack) {
      if (fuelStack != null && !fuelStack.isEmpty() && isFuel(fuelStack)) {
         FreezingRecipe recipe = BlueSkiesData.SNOWCAP_OVEN_FREEZING.getValue(fuelStack.getItem());
         ItemStack freezingOutput = recipe.getOutput();
         ItemStack frozenStack = (ItemStack)this.items.get(3);
         if (frozenStack.isEmpty()) {
            int damage = fuelStack.getDamageValue();
            int outputMaxDamage = freezingOutput.getMaxDamage();
            if (damage > 0 && outputMaxDamage > 0) {
               int maxDamage = fuelStack.getMaxDamage();
               int scaledOutputDamage = (int)((double)damage / maxDamage * outputMaxDamage);
               freezingOutput.setDamageValue(LogicUtil.bind(0, scaledOutputDamage, outputMaxDamage));
            }

            this.items.set(3, freezingOutput.copy());
         } else if (frozenStack.getItem() == freezingOutput.getItem() && frozenStack.getCount() < frozenStack.getMaxStackSize()) {
            frozenStack.grow(freezingOutput.getCount());
            if (frozenStack.getCount() > frozenStack.getMaxStackSize()) {
               frozenStack.setCount(frozenStack.getMaxStackSize());
            }
         }
      }
   }

   private boolean isBurning() {
      return this.getBurnTime() > 0;
   }

   private void smelt(RecipeHolder<?> recipe) {
      if (recipe != null && this.canBurn(recipe, this.items, this.getMaxStackSize())) {
         ItemStack rawStack = (ItemStack)this.items.get(0);
         ItemStack recipeOutput = recipe.value().getResultItem(this.level.registryAccess());
         ItemStack outputStack = (ItemStack)this.items.get(2);
         if (outputStack.isEmpty()) {
            this.items.set(2, recipeOutput.copy());
         } else if (outputStack.getItem() == recipeOutput.getItem()) {
            outputStack.grow(recipeOutput.getCount());
         }

         if (!this.level.isClientSide) {
            this.setRecipeUsed(recipe);
         }

         if (rawStack.getItem() == Blocks.WET_SPONGE.asItem()
            && !((ItemStack)this.items.get(1)).isEmpty()
            && ((ItemStack)this.items.get(1)).getItem() == Items.BUCKET) {
            this.items.set(1, new ItemStack(Items.WATER_BUCKET));
         }

         rawStack.shrink(1);
      }
   }

   public int getBurnTime() {
      return this.dataAccess.get(0);
   }

   public int getRecipesUsed() {
      return this.dataAccess.get(1);
   }

   public int getCookTimeVar() {
      return this.dataAccess.get(2);
   }

   public int getCookTimeTotal() {
      return this.dataAccess.get(3);
   }

   public void setBurnTime(int value) {
      this.dataAccess.set(0, value);
   }

   public void setRecipesUsed(int value) {
      this.dataAccess.set(1, value);
   }

   public void setCookTimeVar(int value) {
      this.dataAccess.set(2, value);
   }

   public void setCookTimeTotal(int value) {
      this.dataAccess.set(3, value);
   }

   private boolean canBurn(RecipeHolder<?> recipe, NonNullList<ItemStack> items, int maxStackSize) {
      if (!((ItemStack)items.get(0)).isEmpty() && recipe != null) {
         ItemStack itemstack = recipe.value().assemble(this, this.level.registryAccess());
         if (itemstack.isEmpty()) {
            return false;
         } else {
            ItemStack itemstack1 = (ItemStack)items.get(2);
            if (itemstack1.isEmpty()) {
               return true;
            } else if (!itemstack1.is(itemstack.getItem())) {
               return false;
            } else {
               return itemstack1.getCount() + itemstack.getCount() <= maxStackSize
                     && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()
                  ? true
                  : itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
            }
         }
      } else {
         return false;
      }
   }

   public static boolean isFuel(ItemStack stack) {
      return BlueSkiesData.SNOWCAP_OVEN_FREEZING.getData().containsKey(stack.getItem());
   }

   public int[] getSlotsForFace(Direction side) {
      if (side == Direction.DOWN) {
         return new int[]{2, 3, 1};
      } else {
         return side == Direction.UP ? new int[]{0} : new int[]{1};
      }
   }

   public boolean canPlaceItem(int index, ItemStack stack) {
      if (index == 2 || index == 3) {
         return false;
      }

      if (index != 1) {
         return true;
      }

      ItemStack itemstack = (ItemStack)this.items.get(1);
      return isFuel(stack) || stack.getItem() == Items.BUCKET && itemstack.getItem() != Items.BUCKET;
   }

   public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
      return direction != Direction.DOWN || index != 1 || !isFuel(stack);
   }
}

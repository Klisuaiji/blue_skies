package com.legacy.blue_skies.block_entity;

import com.google.common.collect.Lists;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.menu.HorizoniteForgeMenu;
import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.registries.SkiesBlockEntityTypes;
import com.legacy.blue_skies.registries.SkiesBlocks;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class HorizoniteForgeBlockEntity
   extends BaseContainerBlockEntity
   implements WorldlyContainer,
   RecipeCraftingHolder,
   StackedContentsCompatible,
   IRecipeUnlocker {
   private static final int[] SLOTS_UP = new int[]{0};
   private static final int[] SLOTS_DOWN = new int[]{2, 1};
   private static final int[] SLOTS_HORIZONTAL = new int[]{1};
   protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
   private int energy;
   private int cookTime;
   private int maxCookTime;
   private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap();
   protected final RecipeType<SmeltingRecipe> recipeType = RecipeType.SMELTING;
   private final ContainerData data = new ContainerData() {
      public int getCount() {
         return 4;
      }

      public void set(int index, int value) {
         switch (index) {
            case 0:
               HorizoniteForgeBlockEntity.this.energy = value;
               break;
            case 1:
               BlueSkies.LOGGER.warn("Cannot set this value");
               break;
            case 2:
               HorizoniteForgeBlockEntity.this.cookTime = value;
               break;
            case 3:
               HorizoniteForgeBlockEntity.this.maxCookTime = value;
         }
      }

      public int get(int index) {
         switch (index) {
            case 0:
               return HorizoniteForgeBlockEntity.this.energy;
            case 1:
               return HorizoniteForgeBlockEntity.this.getMaxEnergy();
            case 2:
               return HorizoniteForgeBlockEntity.this.cookTime;
            case 3:
               return HorizoniteForgeBlockEntity.this.maxCookTime;
            default:
               IllegalArgumentException ex = new IllegalArgumentException("Value out of range");
               BlueSkies.LOGGER.fatal(ex);
               throw ex;
         }
      }
   };

   public HorizoniteForgeBlockEntity(BlockPos pos, BlockState state) {
      super(SkiesBlockEntityTypes.HORIZONITE_FORGE, pos, state);
   }

   protected Component getDefaultName() {
      return Component.translatable(SkiesBlocks.horizonite_forge.getDescriptionId());
   }

   protected HorizoniteForgeMenu createMenu(int id, Inventory playerInv) {
      return new HorizoniteForgeMenu(id, playerInv, this, this.data);
   }

   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
      ContainerHelper.loadAllItems(nbt, this.items);
      this.energy = nbt.getInt("Energy");
      this.cookTime = nbt.getInt("CookTime");
      this.maxCookTime = nbt.getInt("MaxCookTime");
      CompoundTag recipesUsedNbt = nbt.getCompound("RecipesUsed");

      for (String s : recipesUsedNbt.getAllKeys()) {
         this.recipesUsed.put(new ResourceLocation(s), recipesUsedNbt.getInt(s));
      }
   }

   public void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putInt("Energy", this.energy);
      nbt.putInt("CookTime", this.cookTime);
      nbt.putInt("MaxCookTime", this.maxCookTime);
      ContainerHelper.saveAllItems(nbt, this.items);
      CompoundTag recipesUsedNbt = new CompoundTag();
      this.recipesUsed.forEach((recipeId, craftedAmount) -> recipesUsedNbt.putInt(recipeId.toString(), craftedAmount));
      nbt.put("RecipesUsed", recipesUsedNbt);
   }

   public static void forgeServerTick(Level level, BlockPos pos, BlockState state, HorizoniteForgeBlockEntity forge) {
      boolean isCooking = forge.isCooking();
      boolean shouldUpdate = false;
      if (isCooking) {
         if (forge.energy > 0) {
            forge.energy--;
         }
      } else if (forge.getRawStack().isEmpty() && forge.energy < forge.getMaxEnergy()) {
         forge.energy += 2;
      }

      if (forge.energy > forge.getMaxEnergy()) {
         forge.energy = forge.getMaxEnergy();
      }

      ItemStack fuelStack = forge.getFuelStack();
      if (forge.energy <= 0 && !forge.getFuelStack().isEmpty()) {
         forge.energy = forge.energy + forge.getEnergyFrom(fuelStack);
         fuelStack.shrink(1);
      }

      Optional<RecipeHolder<SmeltingRecipe>> recipeHolder = level.getRecipeManager().getRecipeFor(forge.recipeType, forge, level);
      SmeltingRecipe recipe = recipeHolder.<SmeltingRecipe>map(RecipeHolder::value).orElse(null);
      int maxStackSize = forge.getMaxStackSize();
      if (isCooking) {
         shouldUpdate = true;
         forge.maxCookTime = forge.getRecipeCookTime();
         if (forge.canBurn(recipe, forge.items, maxStackSize)) {
            forge.cookTime++;
            if (forge.cookTime >= forge.maxCookTime) {
               forge.smelt(recipeHolder);
               forge.cookTime = 0;
            }
         } else if (forge.cookTime > 0) {
            forge.cookTime = 0;
         }
      } else if (forge.canBurn(recipe, forge.items, maxStackSize)) {
         shouldUpdate = true;
         forge.cookTime++;
      }

      boolean shouldBeLit = forge.isCooking() || forge.canBurn(recipe, forge.items, maxStackSize);
      if (isCooking != shouldBeLit) {
         shouldUpdate = true;
         level.setBlock(pos, (BlockState)level.getBlockState(pos).setValue(AbstractFurnaceBlock.LIT, shouldBeLit), 3);
      }

      if (shouldUpdate) {
         forge.setChanged();
      }
   }

   public boolean isCooking() {
      return this.cookTime > 0;
   }

   public ItemStack getRawStack() {
      return (ItemStack)this.items.get(0);
   }

   public ItemStack getFuelStack() {
      return (ItemStack)this.items.get(1);
   }

   public ItemStack getOutputStack() {
      return (ItemStack)this.items.get(2);
   }

   public void setRawStack(ItemStack stack) {
      this.items.set(0, stack);
   }

   public void setFuelStack(ItemStack stack) {
      this.items.set(1, stack);
   }

   public void setOutputStack(ItemStack stack) {
      this.items.set(2, stack);
   }

   public int getEnergy() {
      return this.energy;
   }

   public int getMaxEnergy() {
      return 4800;
   }

   public int getEnergyFrom(ItemStack stack) {
      return isFuel(stack) ? (int)(this.getMaxEnergy() * BlueSkiesData.HORIZONITE_FORGE_FUEL.getValue(stack.getItem()).fuelAmount) : 0;
   }

   public int getCookTime() {
      return this.cookTime;
   }

   private boolean canBurn(@Nullable Recipe<?> recipe, NonNullList<ItemStack> items, int maxStackSize) {
      if (!this.getRawStack().isEmpty() && recipe != null && this.energy > 0) {
         ItemStack recipeOutput = recipe.assemble(this, this.level.registryAccess());
         if (recipeOutput.isEmpty()) {
            return false;
         } else {
            ItemStack outputSlotStack = (ItemStack)items.get(2);
            if (outputSlotStack.isEmpty()) {
               return true;
            } else if (!outputSlotStack.is(recipeOutput.getItem())) {
               return false;
            } else {
               return outputSlotStack.getCount() + recipeOutput.getCount() <= maxStackSize
                     && outputSlotStack.getCount() + recipeOutput.getCount() <= outputSlotStack.getMaxStackSize()
                  ? true
                  : outputSlotStack.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize();
            }
         }
      } else {
         return false;
      }
   }

   private void smelt(Optional<RecipeHolder<SmeltingRecipe>> recipeHolder) {
      if (recipeHolder.isPresent()) {
         SmeltingRecipe recipe = (SmeltingRecipe)recipeHolder.get().value();
         if (this.canBurn(recipe, this.items, this.getMaxStackSize())) {
            ItemStack rawStack = this.getRawStack();
            ItemStack recipeOutput = recipe.getResultItem(this.level.registryAccess());
            ItemStack outputStack = this.getOutputStack();
            if (outputStack.isEmpty()) {
               this.setOutputStack(recipeOutput.copy());
            } else if (outputStack.getItem() == recipeOutput.getItem()) {
               outputStack.grow(recipeOutput.getCount());
            }

            if (!this.level.isClientSide) {
               this.setRecipeUsed(recipeHolder.get());
            }

            rawStack.shrink(1);
         }
      }
   }

   public int getRecipeCookTime() {
      return (int)(
         this.level
               .getRecipeManager()
               .getRecipeFor(this.recipeType, this, this.level)
               .map(h -> (SmeltingRecipe)h.value())
               .<Integer>map(AbstractCookingRecipe::getCookingTime)
               .orElse(200)
               .intValue()
            * 1.5
      );
   }

   public static boolean isFuel(ItemStack stack) {
      return BlueSkiesData.HORIZONITE_FORGE_FUEL.getData().containsKey(stack.getItem());
   }

   public int[] getSlotsForFace(Direction side) {
      if (side == Direction.DOWN) {
         return SLOTS_DOWN;
      } else {
         return side == Direction.UP ? SLOTS_UP : SLOTS_HORIZONTAL;
      }
   }

   public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
      return this.canPlaceItem(index, stack);
   }

   public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
      return direction != Direction.DOWN || index != 1 || !isFuel(stack);
   }

   public int getContainerSize() {
      return this.items.size();
   }

   public boolean isEmpty() {
      for (ItemStack itemstack : this.items) {
         if (!itemstack.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public ItemStack getItem(int index) {
      return (ItemStack)this.items.get(index);
   }

   public ItemStack removeItem(int index, int count) {
      return ContainerHelper.removeItem(this.items, index, count);
   }

   public ItemStack removeItemNoUpdate(int index) {
      return ContainerHelper.takeItem(this.items, index);
   }

   public void setItem(int index, ItemStack stack) {
      this.items.set(index, stack);
      if (stack.getCount() > this.getMaxStackSize()) {
         stack.setCount(this.getMaxStackSize());
      }
   }

   public boolean stillValid(Player player) {
      return this.level.getBlockEntity(this.worldPosition) != this
         ? false
         : player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64.0;
   }

   public boolean canPlaceItem(int index, ItemStack stack) {
      switch (index) {
         case 0:
            return true;
         case 1:
            return isFuel(stack);
         case 2:
            return false;
         default:
            return true;
      }
   }

   public void clearContent() {
      this.items.clear();
   }

   public void setRecipeUsed(RecipeHolder<?> recipe) {
      ResourceLocation recipeId = recipe.id();
      this.recipesUsed.addTo(recipeId, 1);
   }

   @Nullable
   public RecipeHolder<?> getRecipeUsed() {
      return null;
   }

   public void awardUsedRecipes(Player player, List<ItemStack> recipeItems) {
   }

   @Override
   public void unlockRecipes(Player player) {
      List<RecipeHolder<?>> list = this.grantStoredRecipeExperience(player.level(), player.position());
      player.awardRecipes(list);
      this.recipesUsed.clear();
   }

   public List<RecipeHolder<?>> grantStoredRecipeExperience(Level world, Vec3 pos) {
      List<RecipeHolder<?>> list = Lists.newArrayList();
      ObjectIterator var4 = this.recipesUsed.object2IntEntrySet().iterator();

      while (var4.hasNext()) {
         Entry<ResourceLocation> entry = (Entry<ResourceLocation>)var4.next();
         world.getRecipeManager().byKey((ResourceLocation)entry.getKey()).ifPresent(recipe -> {
            list.add((RecipeHolder<?>)recipe);
            splitAndSpawnExperience(world, pos, entry.getIntValue(), ((AbstractCookingRecipe)recipe.value()).getExperience());
         });
      }

      return list;
   }

   private static void splitAndSpawnExperience(Level world, Vec3 pos, int craftedAmount, float experience) {
      int i = Mth.floor(craftedAmount * experience);
      float f = Mth.frac(craftedAmount * experience);
      if (f != 0.0F && Math.random() < f) {
         i++;
      }

      while (i > 0) {
         int j = ExperienceOrb.getExperienceValue(i);
         i -= j;
         world.addFreshEntity(new ExperienceOrb(world, pos.x, pos.y, pos.z, j));
      }
   }

   public void fillStackedContents(StackedContents helper) {
      for (ItemStack stack : this.items) {
         helper.accountStack(stack);
      }
   }
}

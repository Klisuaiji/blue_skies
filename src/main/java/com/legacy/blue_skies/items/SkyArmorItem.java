package com.legacy.blue_skies.items;

import com.legacy.blue_skies.items.util.IFalsiteItem;
import com.legacy.blue_skies.items.util.SkiesArmorMaterial;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.util.StringUtil;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SkyArmorItem extends ArmorItem implements IFalsiteItem {
   private final SkiesArmorMaterial skiesMaterial;

   public SkyArmorItem(SkiesArmorMaterial skiesMaterial, Type slot, Properties builder) {
      super(skiesMaterial.holder(), slot, builder);
      this.skiesMaterial = skiesMaterial;
   }

   /** Falsite compatibility is carried on the item, since ArmorMaterial is now a record. */
   public boolean isSkiesFalsiteCompatible() {
      return this.skiesMaterial != null && this.skiesMaterial.isCompatWithFalsite();
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, Level worldIn, List<Component> lore, TooltipFlag flagIn) {
      if (stack.getItem() == SkiesItems.shadow_helmet
         || stack.getItem() == SkiesItems.shadow_chestplate
         || stack.getItem() == SkiesItems.shadow_leggings
         || stack.getItem() == SkiesItems.shadow_boots) {
         lore.add(StringUtil.UNFINISHED);
      }
   }

   public int getBarColor(ItemStack stack) {
      return this.falsiteBarColor(stack, super.getBarColor(stack));
   }

   public boolean isBarVisible(ItemStack stack) {
      return this.falsiteBarVisible(stack, super.isBarVisible(stack));
   }

   public int getBarWidth(ItemStack stack) {
      return this.falsiteBarWidth(stack, super.getBarWidth(stack));
   }
}

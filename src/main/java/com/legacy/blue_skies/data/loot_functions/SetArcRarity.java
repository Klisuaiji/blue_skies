package com.legacy.blue_skies.data.loot_functions;

import com.legacy.blue_skies.items.arcs.IArcItem;
import com.legacy.blue_skies.items.util.IRarityItem;
import com.legacy.blue_skies.registries.SkiesLootFunctions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetArcRarity extends LootItemConditionalFunction {
   public static final Codec<SetArcRarity> CODEC = RecordCodecBuilder.create(
      instance -> commonFields(instance)
         .and(Codec.INT.fieldOf("rarity").orElse(IRarityItem.SkiesRarity.COMMON.getId()).forGetter(p_298133_ -> p_298133_.bagRarity.getId()))
         .apply(instance, SetArcRarity::new)
   );
   public final IRarityItem.SkiesRarity bagRarity;

   private SetArcRarity(List<LootItemCondition> condition, int bagRarity) {
      super(condition);
      this.bagRarity = IRarityItem.SkiesRarity.getFromId(bagRarity);
   }

   public LootItemFunctionType getType() {
      return (LootItemFunctionType)SkiesLootFunctions.SET_ARC_RARITY.get();
   }

   public ItemStack run(ItemStack stack, LootContext context) {
      if (stack.getItem() instanceof IArcItem) {
         IArcItem arc = (IArcItem)stack.getItem();
         int level = this.bagRarity.getId();
         if (context.getRandom().nextFloat() < 0.3333F) {
            if (context.getRandom().nextFloat() < 0.6666F) {
               level--;
            } else {
               level++;
            }
         }

         arc.setLevel(stack, level);
      }

      return stack;
   }

   public static Builder<?> setRarity(IRarityItem.SkiesRarity bagRarity) {
      return simpleBuilder(condition -> new SetArcRarity(condition, bagRarity.getId()));
   }
}

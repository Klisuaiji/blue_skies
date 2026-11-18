package com.legacy.blue_skies.triggers;

import com.legacy.blue_skies.registries.SkiesTriggers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.ItemPredicate.Builder;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger.SimpleInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class UsedToolboxTrigger extends SimpleCriterionTrigger<UsedToolboxTrigger.Instance> {
   public void trigger(ServerPlayer player, ItemStack inputItem, ItemStack modifierItem, ItemStack output) {
      this.trigger(player, instance -> instance.test(inputItem, modifierItem, output));
   }

   public Codec<UsedToolboxTrigger.Instance> codec() {
      return UsedToolboxTrigger.Instance.CODEC;
   }

   public record Instance(
      Optional<ContextAwarePredicate> player, Optional<ItemPredicate> input, Optional<ItemPredicate> modifier, Optional<ItemPredicate> output
   ) implements SimpleInstance {
      public static final Codec<UsedToolboxTrigger.Instance> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(UsedToolboxTrigger.Instance::player),
               ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "input").forGetter(UsedToolboxTrigger.Instance::input),
               ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "modifier").forGetter(UsedToolboxTrigger.Instance::modifier),
               ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "output").forGetter(UsedToolboxTrigger.Instance::output)
            )
            .apply(instance, UsedToolboxTrigger.Instance::new)
      );

      public static Criterion<UsedToolboxTrigger.Instance> withModifier(TagKey<Item> item) {
         return SkiesTriggers.USED_TOOL_BOX
            .createCriterion(
               new UsedToolboxTrigger.Instance(Optional.empty(), Optional.empty(), Optional.of(Builder.item().of(item).build()), Optional.empty())
            );
      }

      public static Criterion<UsedToolboxTrigger.Instance> any() {
         return SkiesTriggers.USED_TOOL_BOX
            .createCriterion(new UsedToolboxTrigger.Instance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
      }

      public boolean test(ItemStack inputItem, ItemStack modifierItem, ItemStack output) {
         return (this.input.isEmpty() || this.input.get().matches(inputItem))
            && (this.modifier.isEmpty() || this.modifier.get().matches(modifierItem))
            && (this.output.isEmpty() || this.output.get().matches(output));
      }
   }
}

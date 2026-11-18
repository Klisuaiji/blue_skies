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
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class UsedAlchemyTableTrigger extends SimpleCriterionTrigger<UsedAlchemyTableTrigger.Instance> {
   public void trigger(ServerPlayer player, ItemStack item) {
      this.trigger(player, instance -> instance.test(item));
   }

   public Codec<UsedAlchemyTableTrigger.Instance> codec() {
      return UsedAlchemyTableTrigger.Instance.CODEC;
   }

   public record Instance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item) implements SimpleInstance {
      public static final Codec<UsedAlchemyTableTrigger.Instance> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(UsedAlchemyTableTrigger.Instance::player),
               ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "item").forGetter(UsedAlchemyTableTrigger.Instance::item)
            )
            .apply(instance, UsedAlchemyTableTrigger.Instance::new)
      );

      public static Criterion<UsedAlchemyTableTrigger.Instance> withItem(ItemLike item) {
         return SkiesTriggers.USED_ALCHEMY_TABLE
            .createCriterion(new UsedAlchemyTableTrigger.Instance(Optional.empty(), Optional.of(Builder.item().of(new ItemLike[]{item}).build())));
      }

      public static Criterion<UsedAlchemyTableTrigger.Instance> any() {
         return SkiesTriggers.USED_ALCHEMY_TABLE.createCriterion(new UsedAlchemyTableTrigger.Instance(Optional.empty(), Optional.empty()));
      }

      public boolean test(ItemStack item) {
         return this.item.isEmpty() || this.item.get().matches(item);
      }
   }
}

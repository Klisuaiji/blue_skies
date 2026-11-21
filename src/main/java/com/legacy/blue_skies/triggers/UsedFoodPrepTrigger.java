package com.legacy.blue_skies.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger.SimpleInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

public class UsedFoodPrepTrigger extends SimpleCriterionTrigger<UsedFoodPrepTrigger.Instance> {
   public void trigger(ServerPlayer player) {
      this.trigger(player, instance -> true);
   }

   public Codec<UsedFoodPrepTrigger.Instance> codec() {
      return UsedFoodPrepTrigger.Instance.CODEC;
   }

   public record Instance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
      public static final Codec<UsedFoodPrepTrigger.Instance> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(UsedFoodPrepTrigger.Instance::player)
            )
            .apply(instance, UsedFoodPrepTrigger.Instance::new)
      );

      public static UsedFoodPrepTrigger.Instance create(ItemStack item) {
         return new UsedFoodPrepTrigger.Instance(Optional.empty());
      }
   }
}

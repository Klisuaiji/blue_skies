package com.legacy.blue_skies.triggers;

import com.legacy.blue_skies.registries.SkiesTriggers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger.SimpleInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

public class SpecialArachnarchTrigger extends SimpleCriterionTrigger<SpecialArachnarchTrigger.Instance> {
   public Codec<SpecialArachnarchTrigger.Instance> codec() {
      return SpecialArachnarchTrigger.Instance.CODEC;
   }

   public void trigger(ServerPlayer player) {
      this.trigger(player, instance -> true);
   }

   public record Instance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
      public static final Codec<SpecialArachnarchTrigger.Instance> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(SpecialArachnarchTrigger.Instance::player)
            )
            .apply(instance, SpecialArachnarchTrigger.Instance::new)
      );

      public static Criterion<SpecialArachnarchTrigger.Instance> any() {
         return SkiesTriggers.SPECIAL_ARACHNARCH_KILL.createCriterion(new SpecialArachnarchTrigger.Instance(Optional.empty()));
      }
   }
}

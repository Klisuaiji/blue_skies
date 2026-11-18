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

public class SpecialCrusherTrigger extends SimpleCriterionTrigger<SpecialCrusherTrigger.Instance> {
   public Codec<SpecialCrusherTrigger.Instance> codec() {
      return SpecialCrusherTrigger.Instance.CODEC;
   }

   public void trigger(ServerPlayer player) {
      this.trigger(player, instance -> true);
   }

   public record Instance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
      public static final Codec<SpecialCrusherTrigger.Instance> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(SpecialCrusherTrigger.Instance::player)
            )
            .apply(instance, SpecialCrusherTrigger.Instance::new)
      );

      public static Criterion<SpecialCrusherTrigger.Instance> any() {
         return SkiesTriggers.SPECIAL_CRUSHER_KILL.createCriterion(new SpecialCrusherTrigger.Instance(Optional.empty()));
      }
   }
}

package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public interface SkiesRefs {
   ResourceKey<ConfiguredFeature<?, ?>> BLUEBRIGHT_TREE = feature("bluebright_tree");
   ResourceKey<ConfiguredFeature<?, ?>> STARLIT_TREE = feature("starlit_tree");
   ResourceKey<ConfiguredFeature<?, ?>> SMALL_FROSTBRIGHT_TREE = feature("small_frostbright_tree");
   ResourceKey<ConfiguredFeature<?, ?>> FROSTBRIGHT_TREE = feature("frostbright_tree");
   ResourceKey<ConfiguredFeature<?, ?>> LUNAR_TREE = feature("lunar_tree");
   ResourceKey<ConfiguredFeature<?, ?>> DUSK_TREE = feature("dusk_tree");
   ResourceKey<ConfiguredFeature<?, ?>> LARGE_DUSK_TREE = feature("large_dusk_tree");
   ResourceKey<ConfiguredFeature<?, ?>> MAPLE_TREE = feature("maple_tree");
   ResourceKey<ConfiguredFeature<?, ?>> CRESCENT_FRUIT_TREE = feature("crescent_fruit_tree");
   ResourceKey<ConfiguredFeature<?, ?>> COMET_TREE = feature("bluebright_tree");

   private static <T> ResourceKey<T> key(ResourceKey<Registry<T>> registry, String id) {
      return ResourceKey.create(registry, BlueSkies.locate(id));
   }

   private static ResourceKey<ConfiguredFeature<?, ?>> feature(String id) {
      return key(Registries.CONFIGURED_FEATURE, id);
   }
}

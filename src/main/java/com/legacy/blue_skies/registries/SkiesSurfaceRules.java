package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.Condition;
import net.minecraft.world.level.levelgen.SurfaceRules.ConditionSource;
import net.minecraft.world.level.levelgen.SurfaceRules.Context;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import net.minecraft.world.level.levelgen.SurfaceRules.SurfaceRule;

public class SkiesSurfaceRules extends SurfaceRules {
   public static SkiesSurfaceRules.RandomConditionSource randomBoolean(String name) {
      return new SkiesSurfaceRules.RandomConditionSource(BlueSkies.locate(name));
   }

   public static enum CrystalRoughness implements RuleSource {
      INSTANCE;

      public static final KeyDispatchDataCodec<SkiesSurfaceRules.CrystalRoughness> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));

      public KeyDispatchDataCodec<SkiesSurfaceRules.CrystalRoughness> codec() {
         return CODEC;
      }

      public SurfaceRule apply(Context context) {
         return (x, y, z) -> this.getBlocks(x, y, z, context);
      }

      protected BlockState getBlocks(int x, int y, int z, Context context) {
         double noise = context.system.surfaceNoise.getValue(x, 0.0, z);
         RandomSource rand = context.randomState.getOrCreateRandomFactory(BlueSkies.locate("roughs_crystallization")).at(x, 0, z);
         return !(noise > (1.0 + rand.nextInt(2)) / 8.25) && (!(noise < 0.0) || !(noise > -0.18181818181818182) || !rand.nextBoolean())
            ? SkiesBlocks.lunar_grass_block.defaultBlockState()
            : SkiesBlocks.crystal_sand.defaultBlockState();
      }
   }

   public record RandomConditionSource(ResourceLocation randomName) implements ConditionSource {
      public static final KeyDispatchDataCodec<SkiesSurfaceRules.RandomConditionSource> CODEC = KeyDispatchDataCodec.of(
         RecordCodecBuilder.mapCodec(
            instance -> instance.group(ResourceLocation.CODEC.fieldOf("random_name").forGetter(SkiesSurfaceRules.RandomConditionSource::randomName))
               .apply(instance, SkiesSurfaceRules.RandomConditionSource::new)
         )
      );

      public Condition apply(Context context) {
         PositionalRandomFactory positionalrandomfactory = context.randomState.getOrCreateRandomFactory(this.randomName());
         RandomSource randomsource = positionalrandomfactory.at(context.blockX, 0, context.blockZ);
         return new SkiesSurfaceRules.RandomConditionSource.RandomCondition(randomsource);
      }

      public KeyDispatchDataCodec<? extends ConditionSource> codec() {
         return CODEC;
      }

      class RandomCondition implements Condition {
         private final RandomSource source;

         public RandomCondition(RandomSource randomSource) {
            this.source = randomSource;
         }

         public boolean test() {
            return this.source.nextBoolean();
         }
      }
   }

   public static enum ReservoirMuddiness implements RuleSource {
      INSTANCE;

      public static final KeyDispatchDataCodec<SkiesSurfaceRules.ReservoirMuddiness> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));

      public KeyDispatchDataCodec<? extends RuleSource> codec() {
         return CODEC;
      }

      public SurfaceRule apply(Context context) {
         return (x, y, z) -> this.getBlocks(x, y, z, context);
      }

      protected BlockState getBlocks(int x, int y, int z, Context context) {
         double noise = context.system.surfaceNoise.getValue(x, 0.0, z);
         RandomSource rand = context.randomState.getOrCreateRandomFactory(BlueSkies.locate("reservoir_mud")).at(x, 0, z);
         double mudRange = 0.55;
         if (noise < mudRange / 8.25 && noise > -mudRange / 8.25 && rand.nextFloat() < 0.8F) {
            return SkiesBlocks.lunar_mud.defaultBlockState();
         } else {
            return noise < (mudRange + 0.7F) / 8.25 && noise > -(mudRange + 0.7F) / 8.25 && rand.nextBoolean()
               ? SkiesBlocks.coarse_lunar_dirt.defaultBlockState()
               : SkiesBlocks.lunar_grass_block.defaultBlockState();
         }
      }
   }
}

package com.legacy.blue_skies.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.legacy.blue_skies.registries.SkiesBiomes;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesSurfaceRules;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.SurfaceRules.ConditionSource;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;

public class SkiesSurfaceRuleData {
   private static final RuleSource BEDROCK = stateRule(Blocks.BEDROCK);
   private static final RuleSource TURQUOISE_GRASS = stateRule(SkiesBlocks.turquoise_grass_block);
   private static final RuleSource TURQUOISE_DIRT = stateRule(SkiesBlocks.turquoise_dirt);
   private static final RuleSource TURQUOISE_COARSE_DIRT = stateRule(SkiesBlocks.coarse_turquoise_dirt);
   private static final RuleSource MIDNIGHT_SAND = stateRule(SkiesBlocks.midnight_sand);
   private static final RuleSource MIDNIGHT_SANDSTONE = stateRule(SkiesBlocks.midnight_sandstone);
   private static final RuleSource LUNAR_GRASS = stateRule(SkiesBlocks.lunar_grass_block);
   private static final RuleSource LUNAR_DIRT = stateRule(SkiesBlocks.lunar_dirt);
   private static final RuleSource LUNAR_COARSE_DIRT = stateRule(SkiesBlocks.coarse_lunar_dirt);
   private static final RuleSource CRYSTAL_SAND = stateRule(SkiesBlocks.crystal_sand);
   private static final RuleSource CRYSTAL_SANDSTONE = stateRule(SkiesBlocks.crystal_sandstone);

   public static RuleSource everbright(boolean preliminarySurface, boolean bedrockRoof, boolean bedrockFloor) {
      ConditionSource aboveWater = SurfaceRules.waterBlockCheck(-1, 0);
      RuleSource sandSource = SurfaceRules.sequence(new RuleSource[]{SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, MIDNIGHT_SANDSTONE), MIDNIGHT_SAND});
      ConditionSource isOcean = SurfaceRules.isBiome(
         new ResourceKey[]{
            SkiesBiomes.MIDDAY_SHORE.getKey(), SkiesBiomes.PEEKING_OCEAN.getKey(), SkiesBiomes.DEEP_PEEKING_OCEAN.getKey(), SkiesBiomes.BRUMBLE_FOREST.getKey()
         }
      );
      RuleSource coarseMixSource = SurfaceRules.sequence(
         new RuleSource[]{
            SurfaceRules.ifTrue(
               aboveWater,
               SurfaceRules.ifTrue(
                  SurfaceRules.isBiome(new ResourceKey[]{SkiesBiomes.SLUSHLANDS.getKey(), SkiesBiomes.POLAR_HIGHLAND.getKey()}),
                  SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SURFACE, -0.23636363636363636, 0.11515151515151514), TURQUOISE_COARSE_DIRT)
               )
            )
         }
      );
      RuleSource belowSurfaceSource = SurfaceRules.sequence(
         new RuleSource[]{
            isLowForMidnightSand(MIDNIGHT_SANDSTONE),
            SurfaceRules.ifTrue(
               SurfaceRules.isBiome(new ResourceKey[]{SkiesBiomes.MIDDAY_SHORE.getKey()}),
               upwardGradient("midnight_sandstone_gradient", MIDNIGHT_SANDSTONE, 70)
            ),
            SurfaceRules.ifTrue(isOcean, SurfaceRules.ifTrue(SurfaceRules.not(aboveWater), MIDNIGHT_SANDSTONE)),
            TURQUOISE_DIRT
         }
      );
      RuleSource onSurfaceSource = SurfaceRules.sequence(
         new RuleSource[]{
            isLowForMidnightSand(MIDNIGHT_SAND),
            SurfaceRules.ifTrue(
               SurfaceRules.isBiome(new ResourceKey[]{SkiesBiomes.MIDDAY_SHORE.getKey()}), upwardGradient("midnight_sand_gradient", MIDNIGHT_SAND, 70)
            ),
            coarseMixSource,
            SurfaceRules.ifTrue(aboveWater, TURQUOISE_GRASS),
            SurfaceRules.ifTrue(isOcean, sandSource),
            belowSurfaceSource
         }
      );
      RuleSource floorModifiers = SurfaceRules.sequence(
         new RuleSource[]{SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, onSurfaceSource), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, belowSurfaceSource)}
      );
      Builder<RuleSource> builder = ImmutableList.builder();
      if (bedrockRoof) {
         builder.add(
            SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK)
         );
      }

      if (bedrockFloor) {
         builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
      }

      builder.add(preliminarySurface ? SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), floorModifiers) : floorModifiers);
      return SurfaceRules.sequence((RuleSource[])builder.build().toArray(RuleSource[]::new));
   }

   public static RuleSource everdawn(boolean preliminarySurface, boolean bedrockRoof, boolean bedrockFloor) {
      ConditionSource aboveWater = SurfaceRules.waterBlockCheck(-1, 0);
      ConditionSource fullySandy = SurfaceRules.isBiome(new ResourceKey[]{SkiesBiomes.CRYSTAL_DUNES.getKey(), SkiesBiomes.CRYSTAL_DUNES_SPIKES.getKey()});
      RuleSource sandSource = SurfaceRules.ifTrue(
         aboveWater, SurfaceRules.sequence(new RuleSource[]{SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, CRYSTAL_SANDSTONE), CRYSTAL_SAND})
      );
      RuleSource crystalRoughsMix = SurfaceRules.sequence(
         new RuleSource[]{
            SurfaceRules.ifTrue(
               aboveWater,
               SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[]{SkiesBiomes.CRYSTAL_ROUGHS.getKey()}), SkiesSurfaceRules.CrystalRoughness.INSTANCE)
            )
         }
      );
      RuleSource moonlitReservoirMix = SurfaceRules.sequence(
         new RuleSource[]{
            SurfaceRules.ifTrue(
               aboveWater,
               SurfaceRules.ifTrue(
                  SurfaceRules.isBiome(new ResourceKey[]{SkiesBiomes.MOONLIT_RESERVOIR.getKey()}), SkiesSurfaceRules.ReservoirMuddiness.INSTANCE
               )
            )
         }
      );
      RuleSource belowSurfaceSource = SurfaceRules.sequence(
         new RuleSource[]{SurfaceRules.ifTrue(fullySandy, canPlaceCrystalSand(CRYSTAL_SANDSTONE)), LUNAR_DIRT}
      );
      RuleSource onSurfaceSource = SurfaceRules.sequence(
         new RuleSource[]{
            moonlitReservoirMix,
            crystalRoughsMix,
            isLowForCreekDirt(LUNAR_COARSE_DIRT),
            SurfaceRules.ifTrue(fullySandy, canPlaceCrystalSand(sandSource)),
            SurfaceRules.ifTrue(aboveWater, LUNAR_GRASS),
            belowSurfaceSource
         }
      );
      RuleSource floorModifiers = SurfaceRules.sequence(
         new RuleSource[]{SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, onSurfaceSource), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, belowSurfaceSource)}
      );
      Builder<RuleSource> builder = ImmutableList.builder();
      if (bedrockRoof) {
         builder.add(
            SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK)
         );
      }

      if (bedrockFloor) {
         builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
      }

      builder.add(preliminarySurface ? SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), floorModifiers) : floorModifiers);
      return SurfaceRules.sequence((RuleSource[])builder.build().toArray(RuleSource[]::new));
   }

   private static RuleSource stateRule(Block block) {
      return stateRule(block.defaultBlockState());
   }

   private static RuleSource stateRule(BlockState state) {
      return SurfaceRules.state(state);
   }

   private static RuleSource isLowForMidnightSand(RuleSource rule) {
      return SurfaceRules.ifTrue(
         SurfaceRules.isBiome(new ResourceKey[]{SkiesBiomes.MIDDAY_SHORE.getKey()}),
         SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.yStartCheck(VerticalAnchor.absolute(69), 0)), rule)
      );
   }

   private static RuleSource upwardGradient(String name, RuleSource rule, int height) {
      return SurfaceRules.ifTrue(SurfaceRules.verticalGradient(name, VerticalAnchor.absolute(height), VerticalAnchor.absolute(height + 3)), rule);
   }

   private static RuleSource canPlaceCrystalSand(RuleSource rule) {
      ConditionSource aboveWater = SurfaceRules.waterBlockCheck(-1, 0);
      return SurfaceRules.ifTrue(aboveWater, SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.yStartCheck(VerticalAnchor.absolute(85), 0)), rule));
   }

   private static RuleSource isLowForCreekDirt(RuleSource rule) {
      return SurfaceRules.ifTrue(
         SurfaceRules.isBiome(new ResourceKey[]{SkiesBiomes.RISING_CREEK.getKey()}),
         SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.yStartCheck(VerticalAnchor.absolute(67), 0)), rule)
      );
   }
}

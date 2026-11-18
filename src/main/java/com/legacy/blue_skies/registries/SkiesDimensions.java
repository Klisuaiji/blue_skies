package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.world.SkiesSurfaceRuleData;
import com.legacy.blue_skies.world.everbright.EverbrightChunkGenerator;
import com.legacy.blue_skies.world.everdawn.EverdawnChunkGenerator;
import com.legacy.structure_gel.api.dimension.DimensionAccessHelper;
import com.legacy.structure_gel.api.dimension.DimensionTypeBuilder;
import com.legacy.structure_gel.api.registry.RegistrarHolder;
import com.legacy.structure_gel.api.registry.registrar.DimensionRegistrar;
import java.util.OptionalLong;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;

@RegistrarHolder
public class SkiesDimensions {
   public static final ResourceLocation EVERBRIGHT_ID = BlueSkies.locate("everbright");
   public static final ResourceLocation EVERDAWN_ID = BlueSkies.locate("everdawn");
   public static final DimensionRegistrar EVERBRIGHT = createEverbright();
   public static final DimensionRegistrar EVERDAWN = createEverdawn();

   private static DimensionRegistrar createEverbright() {
      Function<BootstapContext<?>, DimensionType> dimType = c -> DimensionTypeBuilder.of()
         .fixedTime(OptionalLong.of(6000L))
         .minY(0)
         .effects(EVERBRIGHT_ID)
         .build();
      Function<BootstapContext<?>, NoiseGeneratorSettings> noiseSettings = c -> DimensionAccessHelper.newDimensionSettings(
         dummyNoise(),
         SkiesBlocks.turquoise_stone.defaultBlockState(),
         Blocks.WATER.defaultBlockState(),
         DimensionAccessHelper.noneNoiseRouter(),
         SkiesSurfaceRuleData.everbright(false, false, true),
         63,
         false
      );
      Function<BootstapContext<?>, ChunkGenerator> chunkGen = c -> new EverbrightChunkGenerator(c.lookup(Registries.BIOME), c.lookup(Registries.NOISE_SETTINGS));
      return new DimensionRegistrar(EVERBRIGHT_ID, dimType, noiseSettings, chunkGen);
   }

   private static DimensionRegistrar createEverdawn() {
      Function<BootstapContext<?>, DimensionType> dimType = c -> DimensionTypeBuilder.of()
         .fixedTime(OptionalLong.of(23200L))
         .minY(0)
         .effects(EVERDAWN_ID)
         .build();
      Function<BootstapContext<?>, NoiseGeneratorSettings> noiseSettings = c -> DimensionAccessHelper.newDimensionSettings(
         dummyNoise(),
         SkiesBlocks.lunar_stone.defaultBlockState(),
         Blocks.WATER.defaultBlockState(),
         DimensionAccessHelper.noneNoiseRouter(),
         SkiesSurfaceRuleData.everdawn(false, false, true),
         63,
         false
      );
      Function<BootstapContext<?>, ChunkGenerator> chunkGen = c -> new EverdawnChunkGenerator(c.lookup(Registries.BIOME), c.lookup(Registries.NOISE_SETTINGS));
      return new DimensionRegistrar(EVERDAWN_ID, dimType, noiseSettings, chunkGen);
   }

   private static NoiseSettings dummyNoise() {
      return NoiseSettings.create(0, 384, 1, 2);
   }

   public static ResourceKey<Level> everbrightKey() {
      return EVERBRIGHT.getLevelKey();
   }

   public static ResourceKey<Level> everdawnKey() {
      return EVERDAWN.getLevelKey();
   }

   public static boolean inSkyDimension(@Nullable Entity entity) {
      return entity != null && inSkyDimension(entity.level().dimension());
   }

   public static boolean inSkyDimension(ResourceKey<Level> levelKey) {
      return levelKey.equals(everbrightKey()) || levelKey.equals(everdawnKey());
   }
}

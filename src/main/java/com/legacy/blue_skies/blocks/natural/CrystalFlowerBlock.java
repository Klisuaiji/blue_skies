package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.registries.SkiesConfiguredFeatures;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.neoforged.bus.api.Event.Result;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.SaplingGrowTreeEvent;

public class CrystalFlowerBlock extends FlowerBlock implements BonemealableBlock {
   public CrystalFlowerBlock(Holder<MobEffect> effect, float effectDuration, Properties properties) {
      super(effect, effectDuration, properties);
   }

   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      return super.mayPlaceOn(state, level, pos) || state.is(BlockTags.SAND);
   }

   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
      return true;
   }

   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
      return true;
   }

   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      if (rand.nextFloat() < 0.75) {
         Optional<Reference<ConfiguredFeature<?, ?>>> treeFeature = level.registryAccess()
            .registryOrThrow(Registries.CONFIGURED_FEATURE)
            .getHolder(SkiesConfiguredFeatures.GROWN_CRYSTALIZED_TREE);
         if (treeFeature.isPresent()) {
            SaplingGrowTreeEvent event = EventHooks.blockGrowFeature(level, rand, pos, (Holder)treeFeature.get());
            if (event.getResult().equals(Result.DENY)) {
               return;
            }

            level.removeBlock(pos, false);
            if (!((ConfiguredFeature)event.getFeature().value()).place(level, level.getChunkSource().getGenerator(), rand, pos)) {
               level.setBlock(pos, state, 3);
            }
         }
      }
   }
}

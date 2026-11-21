package com.legacy.blue_skies.client.init;

import com.google.common.collect.UnmodifiableIterator;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.blocks.natural.GlimmerReedBlock;
import com.legacy.blue_skies.blocks.natural.SkyLeavesBlock;
import com.legacy.blue_skies.registries.SkiesBlocks;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.ModelEvent.ModifyBakingResult;
import net.neoforged.neoforge.client.event.ModelEvent.RegisterAdditional;

public class SkiesBlockModels {
   private static final ModelResourceLocation HALLOWEEN_GLIMMER_REED = new ModelResourceLocation(BlueSkies.locate("glimmer_reed_halloween"), "");
   private static final ModelResourceLocation XMAS_STARLIT_LEAVES = new ModelResourceLocation(BlueSkies.locate("starlit_leaves_xmas"), "");
   private static final ModelResourceLocation XMAS_STARLIT_LEAVES_SNOWY = new ModelResourceLocation(BlueSkies.locate("starlit_leaves_snowy_xmas"), "");

   public static void modelRegistry(RegisterAdditional event) {
      for (ModelResourceLocation location : new ModelResourceLocation[]{HALLOWEEN_GLIMMER_REED, XMAS_STARLIT_LEAVES, XMAS_STARLIT_LEAVES_SNOWY}) {
         event.register(location);
      }
   }

   public static void onModelBake(ModifyBakingResult event) {
      if (BlueSkiesConfig.COMMON.isHalloween()) {
         UnmodifiableIterator var1 = SkiesBlocks.glimmer_reed.getStateDefinition().getPossibleStates().iterator();

         while (var1.hasNext()) {
            BlockState state = (BlockState)var1.next();
            if ((Boolean)state.getValue(GlimmerReedBlock.TOP)) {
               swapModel(event, state, HALLOWEEN_GLIMMER_REED);
            }
         }
      }

      if (BlueSkiesConfig.COMMON.isChristmas()) {
         UnmodifiableIterator var3 = SkiesBlocks.starlit_leaves.getStateDefinition().getPossibleStates().iterator();

         while (var3.hasNext()) {
            BlockState state = (BlockState)var3.next();
            if ((Boolean)state.getValue(SkyLeavesBlock.SNOWY)) {
               swapModel(event, state, XMAS_STARLIT_LEAVES_SNOWY);
            } else {
               swapModel(event, state, XMAS_STARLIT_LEAVES);
            }
         }
      }
   }

   private static void swapModel(ModifyBakingResult event, BlockState state, ModelResourceLocation model) {
      event.getModels().put(BlockModelShaper.stateToModelLocation(state), (BakedModel)event.getModels().get(model));
   }
}

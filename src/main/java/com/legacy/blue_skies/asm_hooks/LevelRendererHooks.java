package com.legacy.blue_skies.asm_hooks;

import com.legacy.blue_skies.registries.SkiesEmissives;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

public class LevelRendererHooks {
   public static final ThreadLocal<RenderType> RENDER_TYPE = new ThreadLocal<>();
   private static final Direction[] SEARCH_ORDER = new Direction[]{
      Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.DOWN
   };

   public static boolean isEmissive(BlockState state) {
      return SkiesEmissives.EMISSIVES.containsKey(state.getBlock());
   }

   public static int getLight(BlockAndTintGetter level, BlockState state, BlockPos pos) {
      RenderType renderType = RENDER_TYPE.get();
      if (renderType != null && renderType != RenderType.cutout()) {
         int skyLight = level.getBrightness(LightLayer.SKY, pos);
         int blockLight = level.getBrightness(LightLayer.BLOCK, pos);

         for (Direction dir : SEARCH_ORDER) {
            BlockPos offsetPos = pos.relative(dir);
            skyLight = Math.max(skyLight, level.getBrightness(LightLayer.SKY, offsetPos));
            blockLight = Math.max(blockLight, level.getBrightness(LightLayer.BLOCK, offsetPos) - 1);
         }

         int stateLight = state.getLightEmission(level, pos);
         if (blockLight < stateLight) {
            blockLight = stateLight;
         }

         return skyLight << 20 | blockLight << 4;
      } else {
         return 15728880;
      }
   }
}

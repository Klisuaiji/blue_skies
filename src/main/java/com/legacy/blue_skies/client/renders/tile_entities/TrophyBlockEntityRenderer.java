package com.legacy.blue_skies.client.renders.tile_entities;

import com.legacy.blue_skies.block_entity.TrophyBlockEntity;
import com.legacy.blue_skies.blocks.misc.TrophyBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class TrophyBlockEntityRenderer implements BlockEntityRenderer<TrophyBlockEntity> {
   private final Font font;

   public TrophyBlockEntityRenderer(Context context) {
      this.font = context.getFont();
   }

   public void render(TrophyBlockEntity tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
      BlockState state = tile.getBlockState();
      Component label = tile.getLabel();
      matrixStack.pushPose();
      matrixStack.translate(0.5, 0.17, 0.5);
      float f4 = -((Direction)state.getValue(TrophyBlock.FACING)).toYRot();
      matrixStack.mulPose(Axis.YP.rotationDegrees(f4));
      matrixStack.translate(0.0, 0.0, 0.4378);
      float size = 0.010416667F;
      matrixStack.scale(size, -size, size);
      Font fontRender = this.font;
      int width = fontRender.width(label.getString());
      int maxWidth = 70;
      if (width > maxWidth) {
         float shrink = 0.8F;
         matrixStack.scale(shrink, shrink, shrink);
         int maxShrunkWidth = 92;
         if (width > maxShrunkWidth) {
            label = Component.literal(fontRender.substrByWidth(label, maxShrunkWidth).getString());
            width = fontRender.width(label.getString());
         }
      }

      fontRender.drawInBatch(label, 0.0F - width / 2.0F, 0.0F, 0, false, matrixStack.last().pose(), buffer, DisplayMode.NORMAL, 0, combinedLight);
      matrixStack.popPose();
   }
}

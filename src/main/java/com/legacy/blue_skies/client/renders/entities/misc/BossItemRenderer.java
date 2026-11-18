package com.legacy.blue_skies.client.renders.entities.misc;

import com.legacy.blue_skies.entities.BossItemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.entity.item.ItemEntity;

public class BossItemRenderer extends ItemEntityRenderer {
   public BossItemRenderer(Context context) {
      super(context);
   }

   public void render(ItemEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
      if (!(
         entityIn instanceof BossItemEntity bossItem
            && bossItem.getVisiblePlayerId() != null
            && !bossItem.getVisiblePlayerId().equals(Minecraft.getInstance().player.getUUID())
      )) {
         super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
      }
   }
}

package com.legacy.blue_skies.client.renders.entities.misc;

import com.legacy.blue_skies.blocks.dungeon.SkyDecayingSpikeBlock;
import com.legacy.blue_skies.entities.hostile.boss.summons.DecayingSpikeEntity;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.structure_gel.api.util.Positions;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;

@OnlyIn(Dist.CLIENT)
public class DecayingSpikeRenderer<T extends DecayingSpikeEntity> extends EntityRenderer<T> {
   private final BlockRenderDispatcher dispatcher;
   protected boolean upwardModel;

   public DecayingSpikeRenderer(Context context) {
      super(context);
      this.shadowRadius = 0.5F;
      this.dispatcher = context.getBlockRenderDispatcher();
   }

   public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
      BlockState blockstate = !entityIn.getDownwards()
         ? (BlockState)SkiesBlocks.decaying_spike.defaultBlockState().setValue(SkyDecayingSpikeBlock.DOWNWARD, false)
         : SkiesBlocks.decaying_spike.defaultBlockState();
      if (blockstate.getRenderShape() == RenderShape.MODEL) {
         Level world = entityIn.level();
         if (blockstate != world.getBlockState(entityIn.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
            matrixStackIn.pushPose();
            BlockPos blockpos = Positions.blockPos(entityIn.getX(), entityIn.getBoundingBox().maxY, entityIn.getZ());
            matrixStackIn.translate(-0.5, 0.0, -0.5);
            BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
            BakedModel model = this.dispatcher.getBlockModel(blockstate);

            for (RenderType type : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(entityIn.getStartPos())), ModelData.EMPTY)) {
               blockrendererdispatcher.getModelRenderer()
                  .tesselateBlock(
                     world,
                     blockrendererdispatcher.getBlockModel(blockstate),
                     blockstate,
                     blockpos,
                     matrixStackIn,
                     bufferIn.getBuffer(type),
                     this.upwardModel,
                     RandomSource.create(),
                     blockstate.getSeed(entityIn.getStartPos()),
                     OverlayTexture.NO_OVERLAY,
                     ModelData.EMPTY,
                     type
                  );
            }

            matrixStackIn.popPose();
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
         }
      }
   }

   public ResourceLocation getTextureLocation(T entity) {
      return InventoryMenu.BLOCK_ATLAS;
   }
}

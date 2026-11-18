package com.legacy.blue_skies.client.renders;

import com.legacy.blue_skies.block_entity.SkyChestBlockEntity;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkiesBEWLR extends BlockEntityWithoutLevelRenderer {
   public static final SkiesBEWLR INSTANCE = new SkiesBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
   private final BlockEntityRenderDispatcher renderer;

   private SkiesBEWLR(BlockEntityRenderDispatcher renderer, EntityModelSet models) {
      super(renderer, models);
      this.renderer = renderer;
   }

   public void renderByItem(
      ItemStack stack, ItemDisplayContext displayContext, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay
   ) {
      if (stack.getItem() instanceof BlockItem blockItem) {
         this.renderer.renderItem(this.getTile(blockItem.getBlock()), matrixStack, buffer, combinedLight, combinedOverlay);
      }
   }

   public BlockEntity getTile(Block block) {
      if (block instanceof ChestBlock) {
         if (block == SkiesBlocks.bluebright_chest) {
            return new SkyChestBlockEntity.BluebrightChestBlockEntity(BlockPos.ZERO, SkiesBlocks.bluebright_chest.defaultBlockState());
         }

         if (block == SkiesBlocks.starlit_chest) {
            return new SkyChestBlockEntity.StarlitChestBlockEntity(BlockPos.ZERO, SkiesBlocks.starlit_chest.defaultBlockState());
         }

         if (block == SkiesBlocks.frostbright_chest) {
            return new SkyChestBlockEntity.FrostbrightChestBlockEntity(BlockPos.ZERO, SkiesBlocks.frostbright_chest.defaultBlockState());
         }

         if (block == SkiesBlocks.lunar_chest) {
            return new SkyChestBlockEntity.LunarChestBlockEntity(BlockPos.ZERO, SkiesBlocks.lunar_chest.defaultBlockState());
         }

         if (block == SkiesBlocks.dusk_chest) {
            return new SkyChestBlockEntity.DuskChestBlockEntity(BlockPos.ZERO, SkiesBlocks.dusk_chest.defaultBlockState());
         }

         if (block == SkiesBlocks.maple_chest) {
            return new SkyChestBlockEntity.MapleChestBlockEntity(BlockPos.ZERO, SkiesBlocks.maple_chest.defaultBlockState());
         }

         if (block == SkiesBlocks.comet_chest) {
            return new SkyChestBlockEntity.CometChestBlockEntity(BlockPos.ZERO, SkiesBlocks.comet_chest.defaultBlockState());
         }
      }

      return new ChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState());
   }
}

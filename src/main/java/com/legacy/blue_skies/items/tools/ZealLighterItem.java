package com.legacy.blue_skies.items.tools;

import com.legacy.blue_skies.blocks.misc.SkyFireBlock;
import com.legacy.blue_skies.blocks.portal.SkyPortalBlock;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.structure_gel.api.dimension.portal.GelPortalLogic;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.portal.PortalShape;

public class ZealLighterItem extends FlintAndSteelItem {
   public ZealLighterItem(Properties builder) {
      super(builder);
   }

   public InteractionResult useOn(UseOnContext context) {
      Player player = context.getPlayer();
      Level level = context.getLevel();
      BlockPos pos = context.getClickedPos();
      BlockState placePos = level.getBlockState(pos);
      if (CampfireBlock.canLight(placePos)) {
         level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
         level.setBlock(pos, (BlockState)placePos.setValue(BlockStateProperties.LIT, true), 11);
         if (player != null) {
            context.getItemInHand().hurtAndBreak(1, player, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
         }

         return InteractionResult.sidedSuccess(level.isClientSide());
      } else {
         BlockPos offsetPos = pos.relative(context.getClickedFace());
         if (canLightBlock(level, offsetPos, context.getHorizontalDirection())) {
            level.playSound(player, offsetPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            BlockState fireState = getFire(level, offsetPos).getBlock().getStateForPlacement(new BlockPlaceContext(context));
            level.setBlock(offsetPos, fireState, 11);
            ItemStack stack = context.getItemInHand();
            if (player instanceof ServerPlayer serverPlayer) {
               CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, offsetPos, stack);
               stack.hurtAndBreak(1, serverPlayer, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
         } else {
            return InteractionResult.FAIL;
         }
      }
   }

   public static boolean canLightBlock(Level world, BlockPos pos, Direction facing) {
      BlockState state = world.getBlockState(pos);
      if (!state.isAir()) {
         return false;
      }

      BlockState fire = getFire(world, pos);
      return fire.canSurvive(world, pos)
         || shouldLightNetherPortal(world, pos, facing)
         || fire.getBlock() instanceof SkyFireBlock
            && GelPortalLogic.getPortalFrame(world, pos, ((SkyFireBlock)fire.getBlock()).portalBlock.get(), SkyPortalBlock.ALLOWED_INSIDE_BLOCKS) != null;
   }

   public static BlockState getFire(Level level, BlockPos pos) {
      BlockState blockBelow = level.getBlockState(pos.below());
      ResourceKey<Level> dimension = level.dimension();
      BlockState fire = BaseFireBlock.getState(level, pos);
      if (fire.is(Blocks.FIRE)) {
         if (blockBelow.is(SkiesBlocks.blue_fire.baseBlocks)
            || dimension.equals(Level.OVERWORLD)
               && GelPortalLogic.getPortalFrame(level, pos, SkiesBlocks.everbright_portal, SkyPortalBlock.ALLOWED_INSIDE_BLOCKS) != null) {
            return SkiesBlocks.blue_fire.defaultBlockState();
         }

         if (blockBelow.is(SkiesBlocks.black_fire.baseBlocks)
            || dimension.equals(Level.OVERWORLD)
               && GelPortalLogic.getPortalFrame(level, pos, SkiesBlocks.everdawn_portal, SkyPortalBlock.ALLOWED_INSIDE_BLOCKS) != null) {
            return SkiesBlocks.black_fire.defaultBlockState();
         }

         if (dimension.equals(SkiesDimensions.everbrightKey())) {
            return SkiesBlocks.blue_fire.defaultBlockState();
         }

         if (dimension.equals(SkiesDimensions.everdawnKey())) {
            return SkiesBlocks.black_fire.defaultBlockState();
         }
      }

      return fire;
   }

   private static boolean shouldLightNetherPortal(Level world, BlockPos pos, Direction facing) {
      if (world.dimension() != Level.OVERWORLD && world.dimension() != Level.NETHER) {
         return false;
      }

      MutableBlockPos mutablePos = pos.mutable();
      boolean flag = false;

      for (Direction direction : Direction.values()) {
         if (world.getBlockState(mutablePos.set(pos).move(direction)).is(Blocks.OBSIDIAN)) {
            flag = true;
            break;
         }
      }

      return flag && PortalShape.findEmptyPortalShape(world, pos, facing.getCounterClockWise().getAxis()).isPresent();
   }
}

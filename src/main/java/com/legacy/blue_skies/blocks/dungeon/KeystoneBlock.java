package com.legacy.blue_skies.blocks.dungeon;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.block_entity.KeystoneBlockEntity;
import com.legacy.blue_skies.events.SkiesPlayerEvents;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.s_to_c.DisplayToastPacket;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.registries.SkiesStats;
import com.legacy.blue_skies.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

public abstract class KeystoneBlock extends BaseEntityBlock {
   public KeystoneBlock() {
      super(Properties.ofFullCopy(Blocks.BEDROCK));
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
      player.awardStat(SkiesStats.INTERACT_WITH_KEYSTONE);
      if (player instanceof ServerPlayer) {
         int start = 15;
         ServerPlayer serverPlayer = (ServerPlayer)player;
         if ((state.getBlock() == SkiesBlocks.bright_blinding_keystone || state.getBlock() == SkiesBlocks.dawn_blinding_keystone)
            && !EntityUtil.hasAdvancement(serverPlayer, BlueSkies.locate("progression/unlock_everbright_blinding_key_trade"))
            && !EntityUtil.hasAdvancement(serverPlayer, BlueSkies.locate("progression/unlock_everdawn_blinding_key_trade"))) {
            PacketHandler.sendToClient(new DisplayToastPacket((byte)start), serverPlayer);
         }

         if (state.getBlock() == SkiesBlocks.nature_keystone
            && !EntityUtil.hasAdvancement(serverPlayer, BlueSkies.locate("progression/unlock_nature_key_trade"))) {
            PacketHandler.sendToClient(new DisplayToastPacket((byte)(start + 1)), serverPlayer);
         }

         if (state.getBlock() == SkiesBlocks.poison_keystone
            && !EntityUtil.hasAdvancement(serverPlayer, BlueSkies.locate("progression/unlock_poison_key_trade"))) {
            PacketHandler.sendToClient(new DisplayToastPacket((byte)(start + 2)), serverPlayer);
         }
      }

      if (worldIn.getDifficulty().getId() <= Difficulty.PEACEFUL.getId()) {
         return playLockedWithMessage(
            player, pos, SkiesPlayerEvents.inBossFight(player) ? "tile.keystone.leaving_in_fight" : "tile.keystone.peaceful_difficulty"
         );
      }

      if (!player.mayBuild()) {
         return InteractionResult.PASS;
      }

      if (worldIn.getBlockEntity(pos) instanceof KeystoneBlockEntity tile) {
         ItemStack heldItem = player.getItemInHand(handIn);
         if (player.isCreative() && heldItem.getItem() instanceof SpawnEggItem) {
            EntityType<?> entityType = ((SpawnEggItem)heldItem.getItem()).getType(LegacyNbt.get(heldItem));
            tile.setContainedMob(entityType);
            return InteractionResult.SUCCESS;
         } else if ((tile.isCorrectKey(heldItem) || tile.isCorrectArc(heldItem.getItem())) && SkiesPlayerEvents.inBossFight(player)) {
            return playLockedWithMessage(player, pos, "tile.keystone.respawning_in_fight");
         } else if (tile.getSpawnsBoss() && tile.isCorrectArc(heldItem.getItem()) && player.getCooldowns().isOnCooldown(heldItem.getItem())) {
            return playLockedWithMessage(player, pos, "tile.keystone.arc_on_cooldown");
         } else if (tile.getSpawnsBoss()
            && (tile.isCorrectKey(heldItem) || tile.isCorrectArc(heldItem.getItem()))
            && this.spawnBoss(tile, state, worldIn, pos, player, heldItem)) {
            return InteractionResult.SUCCESS;
         } else if ((!tile.getSpawnsBoss() || !tile.getKeysRequired()) && this.teleportPlayer(tile, state, worldIn, pos, player, hit.getDirection())) {
            return InteractionResult.SUCCESS;
         } else {
            return tile.getKeysRequired() ? playLockedWithMessage(player, pos, "tile.keystone.requires_keys") : InteractionResult.PASS;
         }
      } else {
         return InteractionResult.PASS;
      }
   }

   public boolean spawnBoss(KeystoneBlockEntity tile, BlockState state, Level worldIn, BlockPos pos, Player player, ItemStack heldItem) {
      if (!worldIn.isClientSide && !player.isCreative() && !EntityUtil.hasPlayerProgressedTo(player, tile.isEverbrightBased(), tile.getRequiredProgressionId())
         )
       {
         return false;
      }

      tile.spawnMob(tile.isCorrectArc(heldItem.getItem()));
      tile.teleportClosePlayers(worldIn);
      tile.setKeysRequired(false);
      if (tile.isCorrectKey(heldItem)) {
         if (!player.isCreative() && !worldIn.isClientSide) {
            heldItem.shrink(4);
         }

         if (worldIn.isClientSide) {
            player.displayClientMessage(Component.translatable("tile.keystone.remove_key_requirement"), true);
         }

         player.awardStat(Stats.ITEM_USED.get(heldItem.getItem()));
      } else if (tile.isCorrectArc(heldItem.getItem())) {
         if (!player.isCreative()) {
            player.getCooldowns().addCooldown(heldItem.getItem(), 3600);
         }

         if (worldIn.isClientSide) {
            player.displayClientMessage(Component.translatable("tile.keystone.arc_drained", new Object[]{heldItem.getItem().getDescription()}), true);
         }

         player.awardStat(Stats.ITEM_USED.get(heldItem.getItem()));
      }

      if (worldIn.isClientSide) {
         worldIn.playLocalSound(
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            SkiesSounds.BLOCK_KEYSTONE_UNLOCK,
            SoundSource.BLOCKS,
            1.0F,
            EntityUtil.getSoundPitchWithStart(worldIn.random, 1.0F),
            false
         );
         worldIn.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SkiesSounds.BLOCK_KEYSTONE_TELEPORT, SoundSource.BLOCKS, 0.5F, 1.0F, false);
      }

      return true;
   }

   public boolean teleportPlayer(KeystoneBlockEntity tile, BlockState state, Level worldIn, BlockPos pos, Player player, Direction clickDirection) {
      if (!player.isCreative() && !EntityUtil.hasPlayerProgressedTo(player, tile.isEverbrightBased(), tile.getRequiredProgressionId())) {
         return false;
      }

      tile.teleportPlayer(player);
      return true;
   }

   public static InteractionResult playLockedWithMessage(Player player, BlockPos pos, String message) {
      player.level()
         .playSound(
            null,
            pos.getX() + 0.5F,
            pos.getY() + 0.5F,
            pos.getZ() + 0.5F,
            SkiesSounds.BLOCK_KEYSTONE_LOCKED,
            SoundSource.BLOCKS,
            1.0F,
            EntityUtil.getSoundPitchWithStart(player.level().getRandom(), 1.0F)
         );
      player.displayClientMessage(Component.translatable(message), true);
      return InteractionResult.SUCCESS;
   }

   public abstract static class DirectionalKeystoneBlock extends KeystoneBlock {
      public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

      public DirectionalKeystoneBlock() {
         this.registerDefaultState((BlockState)this.defaultBlockState().setValue(FACING, Direction.NORTH));
      }

      public BlockState getStateForPlacement(BlockPlaceContext context) {
         return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
      }

      public BlockState rotate(BlockState state, Rotation rotation) {
         return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
      }

      public BlockState mirror(BlockState state, Mirror mirrorIn) {
         return state.rotate(mirrorIn.getRotation((Direction)state.getValue(FACING)));
      }

      protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
         builder.add(new Property[]{FACING});
      }
   }
}

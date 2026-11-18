package com.legacy.blue_skies.items.tools.weapons;

import com.legacy.blue_skies.blocks.dungeon.SkyDecayingSpikeBlock;
import com.legacy.blue_skies.entities.hostile.boss.summons.DecayingSpikeEntity;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.StringUtil;
import com.legacy.structure_gel.api.util.Positions;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class AlchemyScrollItem extends Item {
   public AlchemyScrollItem(Properties properties) {
      super(properties);
   }

   public int getUseDuration(ItemStack stack) {
      return 72000;
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.BOW;
   }

   public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
      ItemStack useStack = player.getItemInHand(hand);
      player.startUsingItem(hand);
      if (useStack.getItem() == this && player.getUsedItemHand() == hand) {
         player.level().playSound(null, player, SkiesSounds.ENTITY_PLAYER_PREPARE_ROCKS, SoundSource.PLAYERS, 1.0F, 1.0F);
         beginSpikeSpawning(player, false);
      }

      return InteractionResultHolder.pass(useStack);
   }

   public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityIn, int timeLeft) {
      int i = this.getUseDuration(stack) - timeLeft;
      if (entityIn instanceof Player && i > 20) {
         Player player = (Player)entityIn;
         player.getCooldowns().addCooldown(this, 80);
         ItemStack itemstack = entityIn.getItemInHand(player.getUsedItemHand());
         itemstack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(player.getUsedItemHand()));
         beginSpikeSpawning(player, true);
         player.awardStat(Stats.ITEM_USED.get(this));
         player.level().playSound(null, player, SkiesSounds.ENTITY_PLAYER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 1.0F);
      }
   }

   private static void spawnSpikeLine(Player playerIn, double posX, double posZ, double maxY, double posY) {
      BlockPos blockpos = Positions.blockPos(posX, posY, posZ);
      boolean flag = playerIn.level().isEmptyBlock(blockpos);
      double d0 = 0.0;
      if (flag) {
         BlockPos blockpos2 = Positions.blockPos(posX, blockpos.getY() + d0 + 5.0, posZ);
         DecayingSpikeEntity block = new DecayingSpikeEntity(playerIn.level(), blockpos2, SkiesBlocks.decaying_spike.defaultBlockState(), playerIn);
         block.setDownwards(true);
         block.time = 200;
         block.dropItem = false;
         playerIn.level().addFreshEntity(block);
      }
   }

   private static void spawnSpikeCircle(Player playerIn, double posX, double posZ, double maxY, double posY) {
      BlockPos blockpos = Positions.blockPos(posX, posY, posZ);
      boolean flag = false;
      double d0 = 0.0;

      do {
         if (playerIn.level().isEmptyBlock(blockpos) && !playerIn.level().isEmptyBlock(blockpos.below())) {
            if (!playerIn.level().isEmptyBlock(blockpos)) {
               BlockState iblockstate = playerIn.level().getBlockState(blockpos);
               VoxelShape voxelshape = iblockstate.getCollisionShape(playerIn.level(), blockpos);
               if (voxelshape != null) {
                  d0 = voxelshape.max(Axis.Y);
               }
            }

            flag = true;
            break;
         }

         blockpos = blockpos.below();
      } while (blockpos.getY() >= Mth.floor(maxY) - 1);

      if (flag) {
         BlockPos blockpos2 = Positions.blockPos(blockpos.getX() + d0, blockpos.getY() + d0, blockpos.getZ() + d0);
         DecayingSpikeEntity block = new DecayingSpikeEntity(
            playerIn.level(), blockpos2, (BlockState)SkiesBlocks.decaying_spike.defaultBlockState().setValue(SkyDecayingSpikeBlock.DOWNWARD, false), playerIn
         );
         block.setDownwards(false);
         block.setPos(blockpos2.getX() + 0.5, blockpos2.getY(), blockpos2.getZ() + 0.5);
         block.time = 200;
         block.setDeltaMovement(0.0, 0.7, 0.0);
         block.dropItem = false;
         playerIn.level().addFreshEntity(block);
      }
   }

   private static void playSpikeParticles(Player playerIn, double posX, double posY, double posZ, double offsetX, double offsetY, double offsetZ) {
      int count = 20;
      if (playerIn.level() instanceof ServerLevel) {
         ((ServerLevel)playerIn.level())
            .sendParticles(
               new BlockParticleOption(ParticleTypes.BLOCK, SkiesBlocks.decaying_spike.defaultBlockState()),
               posX,
               posY,
               posZ,
               count,
               offsetX,
               offsetY,
               offsetZ,
               0.05
            );
      }
   }

   public static void beginSpikeSpawning(Player playerIn, boolean spawnSpikes) {
      beginSpikeSpawning(playerIn, spawnSpikes, 8);
   }

   public static void beginSpikeSpawning(Player playerIn, boolean spawnSpikes, int particleAmount) {
      Vec3 look = playerIn.getViewVector(1.0F);
      double targetX = playerIn.getX() + look.x() * 3.0;
      double targetZ = playerIn.getZ() + look.z() * 3.0;
      double d0 = playerIn.getY();
      double d1 = playerIn.getY() + 1.0;
      float f = (float)Mth.atan2(targetZ - playerIn.getZ(), targetX - playerIn.getX());
      if (playerIn.isShiftKeyDown()) {
         for (int k = 0; k < particleAmount; k++) {
            float f2 = f + k * (float) Math.PI * 2.0F / 8.0F + (float) (Math.PI * 2.0 / 5.0);
            playSpikeParticles(
               playerIn,
               playerIn.getX() + Mth.cos(f2) * 2.0,
               playerIn.getY() + 0.2,
               playerIn.getZ() + Mth.sin(f2) * 2.0,
               playerIn.getBbWidth() / 4.0F,
               playerIn.getBbHeight() / 4.0F,
               playerIn.getBbWidth() / 4.0F
            );
            if (spawnSpikes) {
               spawnSpikeCircle(playerIn, playerIn.getX() + Mth.cos(f2) * 2.0, playerIn.getZ() + Mth.sin(f2) * 2.0, d0, d1);
            }
         }
      } else {
         for (int l = 0; l < particleAmount; l++) {
            double d2 = 1.25 * (l + 1);
            playSpikeParticles(
               playerIn,
               playerIn.getX() + Mth.cos(f) * d2,
               playerIn.getY() + 5.0,
               playerIn.getZ() + Mth.sin(f) * d2,
               playerIn.getBbWidth() / 4.0F,
               playerIn.getBbHeight() / 4.0F,
               playerIn.getBbWidth() / 4.0F
            );
            if (spawnSpikes) {
               spawnSpikeLine(playerIn, playerIn.getX() + Mth.cos(f) * d2, playerIn.getZ() + Mth.sin(f) * d2, d0, d1);
            }
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> lore, TooltipFlag flagIn) {
      lore.add(Component.literal(StringUtil.getAbilityText("gui.blue_skies.item.ability.alchemy_scroll")));
   }
}

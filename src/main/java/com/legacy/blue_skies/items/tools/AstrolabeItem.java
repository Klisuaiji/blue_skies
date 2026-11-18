package com.legacy.blue_skies.items.tools;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.blocks.construction.StarEmitterBlock;
import com.legacy.blue_skies.events.SkiesPlayerEvents;
import com.legacy.blue_skies.registries.SkiesPoiTypes;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.blue_skies.util.StringUtil;
import com.legacy.structure_gel.api.util.Positions;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class AstrolabeItem extends Item implements Vanishable {
   public static final int MAX_DISTANCE = 300;

   public AstrolabeItem(Properties properties) {
      super(properties);
   }

   public int getUseDuration(ItemStack stack) {
      return 72000;
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.BOW;
   }

   public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (stack.getItem() != this || player.getUsedItemHand() != hand) {
         return InteractionResultHolder.pass(stack);
      } else if (getTeleportCooldown(stack) > 0) {
         player.displayClientMessage(Component.translatable("gui.blue_skies.tooltip.astrolabe_cooldown"), true);
         return InteractionResultHolder.fail(stack);
      } else if (SkiesPlayerEvents.inBossFight(player)) {
         player.displayClientMessage(Component.translatable("tile.keystone.leaving_in_fight"), true);
         return InteractionResultHolder.fail(stack);
      } else {
         BlockPos teleportPos = getTeleportPos(stack);
         if (teleportPos == null) {
            player.displayClientMessage(Component.translatable("gui.blue_skies.tooltip.astrolabe_pos_not_set"), true);
            return InteractionResultHolder.fail(stack);
         } else {
            Optional<ResourceKey<Level>> teleportDim = getTeleportDimension(stack);
            int distance = (int)EntityUtil.getDistanceToPos(player.blockPosition(), teleportPos);
            if (distance <= 300 && (!teleportDim.isPresent() || teleportDim.get().location().equals(player.level().dimension().location()))) {
               player.startUsingItem(hand);
               return InteractionResultHolder.pass(stack);
            } else {
               player.displayClientMessage(Component.translatable("gui.blue_skies.tooltip.astrolabe_too_far"), true);
               return InteractionResultHolder.fail(stack);
            }
         }
      }
   }

   public void releaseUsing(ItemStack stack, Level world, LivingEntity entityIn, int timeLeft) {
      int i = this.getUseDuration(stack) - timeLeft;
      if (entityIn instanceof Player player && i > 20) {
         BlockPos teleportPos = getTeleportPos(stack);
         if (!world.isClientSide && teleportPos != null) {
            try {
               Vec3 safeTeleportPos = StarEmitterBlock.findTeleportPoint(entityIn.getType(), world, teleportPos)
                  .orElse(Vec3.atLowerCornerOf(getTeleportPos(stack)));
               int distance = (int)EntityUtil.getDistanceToPos(player.blockPosition(), Positions.blockPos(safeTeleportPos));
               if (world instanceof ServerLevel
                  && !((ServerLevel)world).getPoiManager().existsAtPosition(SkiesPoiTypes.STAR_EMITTER.getKey(), getTeleportPos(stack))) {
                  player.displayClientMessage(Component.translatable("gui.blue_skies.tooltip.astrolabe_no_emitter"), true);
                  player.stopUsingItem();
                  return;
               }

               Optional<ResourceKey<Level>> teleportDim = getTeleportDimension(stack);
               if (distance > 300 || teleportDim.isPresent() && !teleportDim.get().location().equals(player.level().dimension().location())) {
                  player.displayClientMessage(Component.translatable("gui.blue_skies.tooltip.astrolabe_too_far"), true);
                  player.stopUsingItem();
                  return;
               }

               if (player.randomTeleport(safeTeleportPos.x(), safeTeleportPos.y(), safeTeleportPos.z(), true)) {
                  setTeleportCooldown(stack, distance);
                  world.playSound(
                     (Player)null,
                     safeTeleportPos.x(),
                     safeTeleportPos.y(),
                     safeTeleportPos.z(),
                     SoundEvents.CHORUS_FRUIT_TELEPORT,
                     SoundSource.PLAYERS,
                     1.0F,
                     1.0F
                  );
                  player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                  player.stopUsingItem();
               }
            } catch (Exception var11) {
               BlueSkies.LOGGER.error(var11);
            }

            return;
         }
      }
   }

   public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
      if (getTeleportCooldown(stack) > 0 && entity.tickCount % 20 == 0) {
         setTeleportCooldown(stack, getTeleportCooldown(stack) - 1);
         Optional<ResourceKey<Level>> teleportDim = getTeleportDimension(stack);
         if (getTeleportCooldown(stack) == 0 && teleportDim.isPresent() && teleportDim.get().location().equals(world.dimension().location())) {
            entity.level().playSound((Player)null, entity, SkiesSounds.ITEM_ASTROLABE_READY, SoundSource.PLAYERS, 1.0F, 1.0F);
         }
      }
   }

   public static int getTeleportCooldown(ItemStack stack) {
      CompoundTag compoundnbt = stack.getTag();
      return compoundnbt == null ? 0 : compoundnbt.getInt("TeleportCooldown");
   }

   public static void setTeleportCooldown(ItemStack stack, int cooldownIn) {
      CompoundTag compoundnbt = stack.getOrCreateTag();
      compoundnbt.putInt("TeleportCooldown", cooldownIn);
   }

   @Nullable
   public static BlockPos getTeleportPos(ItemStack stack) {
      CompoundTag compoundnbt = stack.getTag();
      return compoundnbt == null ? null : NbtUtils.readBlockPos(compoundnbt.getCompound("TeleportPosition"));
   }

   public static void setTeleportPos(ItemStack stack, BlockPos pos) {
      CompoundTag compoundnbt = stack.getOrCreateTag();
      compoundnbt.put("TeleportPosition", NbtUtils.writeBlockPos(pos));
   }

   public static Optional<ResourceKey<Level>> getTeleportDimension(ItemStack stack) {
      CompoundTag compoundnbt = stack.getTag();
      return compoundnbt == null ? Optional.empty() : Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compoundnbt.get("TeleportDimension")).result();
   }

   public static void setTeleportDimension(ItemStack stack, ResourceKey<Level> dimension) {
      CompoundTag compoundnbt = stack.getOrCreateTag();
      Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, dimension).result().ifPresent(dim -> compoundnbt.put("TeleportDimension", dim));
   }

   public static boolean isWithinDistance(ItemStack stack, LivingEntity entity, Level world) {
      if (entity != null && entity.level() != null && stack != null) {
         BlockPos tpPos = getTeleportPos(stack);
         Optional<ResourceKey<Level>> dimension = getTeleportDimension(stack);
         if (tpPos != null && dimension.isPresent() && dimension.get().location().equals(entity.level().dimension().location())) {
            return (int)EntityUtil.getDistanceToPos(entity.blockPosition(), tpPos) < 300;
         }
      }

      return false;
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> lore, TooltipFlag flagIn) {
      lore.add(Component.translatable(StringUtil.getAbilityText("gui.blue_skies.item.ability.teleportation")));
      if (getTeleportCooldown(stack) > 0) {
         lore.add(Component.literal(ChatFormatting.YELLOW + "Teleport Cooldown: " + getTeleportCooldown(stack)));
      }
   }
}

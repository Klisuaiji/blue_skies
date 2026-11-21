package com.legacy.blue_skies.items.tools.weapons;

import com.legacy.blue_skies.entities.projectile.VenomSpitEntity;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.StringUtil;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class VenomSacItem extends Item {
   public static final int MAX_USE_TIME = 20;

   public VenomSacItem(Properties builder) {
      super(builder);
   }

   public int getUseDuration(ItemStack stack) {
      return 72000;
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.BOW;
   }

   public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
      player.startUsingItem(hand);
      return InteractionResultHolder.pass(player.getItemInHand(hand));
   }

   public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
      int i = this.getUseDuration(stack) - timeLeft;
      if (entity instanceof Player && i >= 20) {
         Player player = (Player)entity;
         ItemStack heldStack = player.getItemInHand(player.getUsedItemHand());
         world.playSound(
            (Player)null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SkiesSounds.ENTITY_PLAYER_SPIT,
            SoundSource.NEUTRAL,
            0.5F,
            (world.random.nextFloat() - world.random.nextFloat()) * 0.1F + 1.5F
         );
         if (!world.isClientSide) {
            heldStack.hurtAndBreak(1, player, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
            VenomSpitEntity spitEntity = new VenomSpitEntity(world, player, false);
            spitEntity.setPos(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
            this.fireSpit(spitEntity, player, player.getXRot(), player.getYRot(), 2.0F, 1.5F, 1.0F);
            world.addFreshEntity(spitEntity);
         }

         player.awardStat(Stats.ITEM_USED.get(this));
      }
   }

   public void fireSpit(VenomSpitEntity spit, Entity player, float pitch, float yaw, float arc, float velocity, float inaccuracy) {
      float x = -Mth.sin(yaw * (float) (Math.PI / 180.0)) * Mth.cos(pitch * (float) (Math.PI / 180.0));
      float y = -Mth.sin((pitch + arc) * (float) (Math.PI / 180.0));
      float z = Mth.cos(yaw * (float) (Math.PI / 180.0)) * Mth.cos(pitch * (float) (Math.PI / 180.0));
      spit.shoot(x, y, z, velocity, inaccuracy);
      Vec3 vector3d = player.getDeltaMovement();
      spit.setDeltaMovement(spit.getDeltaMovement().add(vector3d.x, player.onGround() ? 0.0 : vector3d.y, vector3d.z));
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, Level world, List<Component> lore, TooltipFlag flagIn) {
      lore.add(Component.literal(StringUtil.getAbilityText("gui.blue_skies.item.ability.venom_sac")));
   }
}

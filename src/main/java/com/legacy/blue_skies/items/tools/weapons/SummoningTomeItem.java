package com.legacy.blue_skies.items.tools.weapons;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.entities.hostile.boss.summons.ArtificialGolemEntity;
import com.legacy.blue_skies.entities.projectile.FluctuantSphereEntity;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.StringUtil;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SummoningTomeItem extends Item {
   public SummoningTomeItem(Properties properties) {
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
         if (canSummonGolems(useStack)) {
            List<ArtificialGolemEntity> golems = world.getEntitiesOfClass(
               ArtificialGolemEntity.class, player.getBoundingBox().inflate(20.0), golem -> golem.playerSpawned() && golem.getCaster().equals(player)
            );
            if (!golems.isEmpty()) {
               player.displayClientMessage(Component.translatable("gui.blue_skies.tooltip.too_many_golems"), true);
               player.stopUsingItem();
               return InteractionResultHolder.fail(useStack);
            }
         }

         player.level()
            .playSound(
               null,
               player,
               canSummonGolems(useStack) ? SkiesSounds.ENTITY_PLAYER_PREPARE_SUMMONING : SkiesSounds.ENTITY_PLAYER_PREPARE_FLUCTUANT_SPHERE,
               SoundSource.PLAYERS,
               1.0F,
               1.0F
            );
      }

      return InteractionResultHolder.pass(useStack);
   }

   public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityIn, int timeLeft) {
      int i = this.getUseDuration(stack) - timeLeft;
      boolean summonsGolems = canSummonGolems(stack);
      if (entityIn instanceof Player && i > (summonsGolems ? 20 : 10)) {
         Player player = (Player)entityIn;
         ItemStack itemstack = player.getItemInHand(player.getUsedItemHand());
         itemstack.hurtAndBreak(1, player, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
         if (!worldIn.isClientSide()) {
            if (summonsGolems) {
               BlockPos pos = player.blockPosition();
               BlockPos spawnPos = new BlockPos(pos.getX() + (worldIn.random.nextInt(6) - 3), pos.getY(), pos.getZ() + (worldIn.random.nextInt(6) - 3));
               if (worldIn instanceof ServerLevelAccessor) {
                  ArtificialGolemEntity golem = new ArtificialGolemEntity(worldIn, player);
                  golem.moveTo(spawnPos, 0.0F, 0.0F);
                  golem.finalizeSpawn(
                     (ServerLevelAccessor)worldIn,
                     worldIn.getCurrentDifficultyAt(
                        new BlockPos(spawnPos.getX(), worldIn.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, spawnPos.getX(), spawnPos.getZ()), spawnPos.getZ())
                     ),
                     MobSpawnType.MOB_SUMMONED,
                     (SpawnGroupData)null,
                     (CompoundTag)null
                  );
                  golem.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
                  golem.setHealth(golem.getMaxHealth());
                  if (worldIn.random.nextFloat() < 0.3F) {
                     golem.setRanged(true);
                  }

                  worldIn.addFreshEntity(golem);
                  player.getCooldowns().addCooldown(this, 1200);
                  if (worldIn instanceof ServerLevel) {
                     for (int h = 0; h < 20; h++) {
                        double dx = worldIn.random.nextGaussian() * 0.02;
                        double dy = worldIn.random.nextGaussian() * 0.02;
                        double dz = worldIn.random.nextGaussian() * 0.02;
                        ((ServerLevel)worldIn)
                           .sendParticles(
                              ParticleTypes.POOF,
                              golem.getX() + worldIn.random.nextFloat() * golem.getBbWidth() * 2.0F - golem.getBbWidth(),
                              golem.getY() + worldIn.random.nextFloat() * golem.getBbHeight(),
                              golem.getZ() + worldIn.random.nextFloat() * golem.getBbWidth() * 2.0F - golem.getBbWidth(),
                              1,
                              dx,
                              dy,
                              dz,
                              0.0
                           );
                     }
                  }

                  if (player instanceof ServerPlayer) {
                     CriteriaTriggers.SUMMONED_ENTITY.trigger((ServerPlayer)player, golem);
                  }
               }
            } else {
               Vec3 lookVector = player.getViewVector(1.0F);
               FluctuantSphereEntity projectile = new FluctuantSphereEntity(worldIn, player, lookVector.x * 1.0, lookVector.y * 1.0, lookVector.z * 1.0);
               projectile.setPos(player.getX() + lookVector.x * 1.0, player.getY() + player.getBbHeight() / 2.0F + 0.2F, player.getZ() + lookVector.z * 1.0);
               projectile.setYRot(player.yHeadRot);
               player.level().addFreshEntity(projectile);
               player.getCooldowns().addCooldown(this, 80);
            }
         }

         player.playSound(SkiesSounds.ENTITY_PLAYER_CAST_SPELL, 1.0F, 1.0F);
      }
   }

   public static boolean canSummonGolems(ItemStack stack) {
      CompoundTag compoundnbt = LegacyNbt.get(stack);
      return compoundnbt.getBoolean("SummonsGolems");
   }

   public static ItemStack setSummonsGolems(ItemStack stack, boolean canSummon) {
      CompoundTag compoundnbt = LegacyNbt.getOrCreate(stack);
      compoundnbt.putBoolean("SummonsGolems", canSummon);
      return stack;
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, Level worldIn, List<Component> lore, TooltipFlag flagIn) {
      lore.add(
         Component.literal(
            StringUtil.getAbilityText("gui.blue_skies.item.ability.summoning_tome." + (canSummonGolems(stack) ? "artificial_golem" : "fluctuant_sphere"))
         )
      );
   }
}

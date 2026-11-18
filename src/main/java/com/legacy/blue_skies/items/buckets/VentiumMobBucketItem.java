package com.legacy.blue_skies.items.buckets;

import com.legacy.blue_skies.entities.passive.fish.JellyDrifterEntity;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.util.StringUtil;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class VentiumMobBucketItem extends VentiumBucketItem {
   private final Supplier<? extends EntityType<?>> heldEntityType;
   private final Supplier<? extends SoundEvent> emptySound;

   public VentiumMobBucketItem(
      Supplier<? extends EntityType<? extends LivingEntity>> heldEntityType,
      Supplier<? extends SoundEvent> emptySound,
      Supplier<? extends Fluid> fluid,
      Properties builder
   ) {
      super(fluid, builder);
      this.heldEntityType = heldEntityType;
      this.emptySound = emptySound;
   }

   public void checkExtraContent(Player player, Level level, ItemStack stack, BlockPos pos) {
      if (level instanceof ServerLevel) {
         this.spawn((ServerLevel)level, stack, pos);
         level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
      }
   }

   protected void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos) {
      level.playSound(player, pos, this.getEmptySound(), SoundSource.NEUTRAL, 1.0F, 1.0F);
   }

   private void spawn(ServerLevel level, ItemStack bucketStack, BlockPos pos) {
      if (this.getHeldEntityType().spawn(level, bucketStack, (Player)null, pos, MobSpawnType.BUCKET, true, false) instanceof Bucketable bucketable) {
         bucketable.loadFromBucketTag(bucketStack.getOrCreateTag());
         bucketable.setFromBucket(true);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
      if (this.getHeldEntityType() == SkiesEntityTypes.JELLY_DRIFTER) {
         CompoundTag compoundnbt = stack.getTag();
         if (compoundnbt != null && compoundnbt.contains("Color", 99)) {
            byte i = compoundnbt.getByte("Color");
            ChatFormatting[] formatting = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};
            tooltip.add(
               Component.translatable("entity.blue_skies.jelly_drifter.color." + StringUtil.toLower(JellyDrifterEntity.Colors.byId(i).name()))
                  .withStyle(formatting)
            );
         }
      }
   }

   protected EntityType<?> getHeldEntityType() {
      return (EntityType<?>)this.heldEntityType.get();
   }

   public SoundEvent getEmptySound() {
      return this.emptySound.get();
   }
}

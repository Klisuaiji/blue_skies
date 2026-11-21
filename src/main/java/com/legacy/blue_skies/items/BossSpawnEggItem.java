package com.legacy.blue_skies.items;

import com.legacy.blue_skies.util.StringUtil;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

public class BossSpawnEggItem extends DeferredSpawnEggItem {
   public BossSpawnEggItem(Supplier<EntityType<? extends Mob>> pDefaultType, int pBackgroundColor, int pHighlightColor, Properties pProperties) {
      super(pDefaultType, pBackgroundColor, pHighlightColor, pProperties);
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, Level worldIn, List<Component> lore, TooltipFlag flagIn) {
      lore.add(StringUtil.BOSS_EGG);
   }

   public InteractionResult useOn(UseOnContext pContext) {
      return !pContext.getPlayer().isCreative() ? InteractionResult.FAIL : super.useOn(pContext);
   }

   public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
      return !pPlayer.isCreative() ? InteractionResultHolder.fail(pPlayer.getItemInHand(pHand)) : super.use(pLevel, pPlayer, pHand);
   }
}

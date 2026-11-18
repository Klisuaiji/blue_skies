package com.legacy.blue_skies.items.block;

import com.legacy.blue_skies.client.renders.SkiesBEWLR;
import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class SkyChestBlockItem extends BlockItem {
   public SkyChestBlockItem(Block block, Properties properties) {
      super(block, properties);
   }

   public void initializeClient(Consumer<IClientItemExtensions> consumer) {
      consumer.accept(new IClientItemExtensions() {
         public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return SkiesBEWLR.INSTANCE;
         }
      });
   }
}

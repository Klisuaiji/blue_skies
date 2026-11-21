package com.legacy.blue_skies.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IPlantable;
import com.legacy.blue_skies.compat.PlantType;

public class SkyPlantableBlockItem extends ItemNameBlockItem implements IPlantable {
   public SkyPlantableBlockItem(Block plantBlockIn, Properties properties) {
      super(plantBlockIn, properties);
   }

   public BlockState getPlant(BlockGetter world, BlockPos pos) {
      return this.getBlock().defaultBlockState();
   }

   public PlantType getPlantType(BlockGetter world, BlockPos pos) {
      return PlantType.CROP;
   }
}

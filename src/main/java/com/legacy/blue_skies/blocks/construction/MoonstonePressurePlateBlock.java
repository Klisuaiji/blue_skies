package com.legacy.blue_skies.blocks.construction;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;

public class MoonstonePressurePlateBlock extends BasePressurePlateBlock {
   public static final MapCodec<MoonstonePressurePlateBlock> CODEC = RecordCodecBuilder.mapCodec(
      p_308833_ -> p_308833_.group(BlockSetType.CODEC.fieldOf("block_set_type").forGetter(p_304917_ -> p_304917_.type), propertiesCodec())
         .apply(p_308833_, MoonstonePressurePlateBlock::new)
   );
   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

   public MoonstonePressurePlateBlock(BlockSetType type, Properties properties) {
      super(properties, type);
      this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(POWERED, false));
   }

   protected int getSignalStrength(Level level, BlockPos pos) {
      AABB bb = TOUCH_AABB.move(pos);
      List<? extends Entity> list = level.getEntitiesOfClass(Player.class, bb);
      if (!list.isEmpty()) {
         for (Entity entity : list) {
            if (!entity.isIgnoringBlockTriggers()) {
               return 15;
            }
         }
      }

      return 0;
   }

   protected int getSignalForState(BlockState state) {
      return state.getValue(POWERED) ? 15 : 0;
   }

   protected BlockState setSignalForState(BlockState state, int strength) {
      return (BlockState)state.setValue(POWERED, strength > 0);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{POWERED});
   }

   protected MapCodec<? extends BasePressurePlateBlock> codec() {
      return CODEC;
   }
}

package com.legacy.blue_skies.blocks.construction;

import com.legacy.blue_skies.client.gui.menu.FoodPrepMenu;
import com.legacy.blue_skies.registries.SkiesStats;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FoodPrepTableBlock extends HorizontalDirectionalBlock {
   public static final MapCodec<FoodPrepTableBlock> CODEC = simpleCodec(FoodPrepTableBlock::new);
   private static final Component CONTAINER_NAME = Component.translatable("gui.blue_skies.food_prep_table");
   private static final VoxelShape SHAPE = Shapes.or(
      Block.box(0.0, 9.0, 0.0, 16.0, 12.0, 16.0),
      new VoxelShape[]{
         Block.box(0.0, 0.0, 0.0, 3.0, 9.0, 3.0),
         Block.box(13.0, 0.0, 0.0, 16.0, 9.0, 3.0),
         Block.box(13.0, 0.0, 13.0, 16.0, 9.0, 16.0),
         Block.box(0.0, 0.0, 13.0, 3.0, 9.0, 16.0)
      }
   );

   public FoodPrepTableBlock(Properties builder) {
      super(builder);
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return SHAPE;
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (world.isClientSide) {
         return InteractionResult.SUCCESS;
      }

      player.openMenu(state.getMenuProvider(world, pos));
      player.awardStat(SkiesStats.INTERACT_WITH_FOOD_PREP_TABLE);
      return InteractionResult.CONSUME;
   }

   public MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
      return new SimpleMenuProvider((id, inventory, player) -> new FoodPrepMenu(id, inventory, ContainerLevelAccess.create(world, pos)), CONTAINER_NAME);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return CODEC;
   }
}

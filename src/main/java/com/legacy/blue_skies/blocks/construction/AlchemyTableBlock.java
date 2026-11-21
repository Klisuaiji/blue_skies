package com.legacy.blue_skies.blocks.construction;

import com.legacy.blue_skies.client.gui.menu.AlchemyTableMenu;
import com.legacy.blue_skies.registries.SkiesStats;
import com.legacy.blue_skies.util.DirectionUtil;
import com.legacy.structure_gel.api.util.VoxelShapeUtil;
import com.mojang.serialization.MapCodec;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class AlchemyTableBlock extends HorizontalDirectionalBlock {
   public static final MapCodec<AlchemyTableBlock> CODEC = simpleCodec(AlchemyTableBlock::new);
   private static final Component CONTAINER_NAME = Component.translatable("gui.blue_skies.alchemy");
   private static final Map<Direction, VoxelShape> SHAPES = new HashMap<>();

   public AlchemyTableBlock(Properties properties) {
      super(properties);
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return SHAPES.get(state.getValue(FACING));
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (world.isClientSide) {
         return InteractionResult.SUCCESS;
      }

      player.openMenu(state.getMenuProvider(world, pos));
      player.awardStat(SkiesStats.INTERACT_WITH_ALCHEMY_TABLE);
      return InteractionResult.CONSUME;
   }

   public MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
      return new SimpleMenuProvider((id, inventory, player) -> new AlchemyTableMenu(id, inventory, ContainerLevelAccess.create(world, pos)), CONTAINER_NAME);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return CODEC;
   }

   static {
      VoxelShape shape = Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0), Block.box(0.0, 10.0, 11.0, 16.0, 16.0, 16.0));

      for (Direction dir : DirectionUtil.HORIZONTAL) {
         SHAPES.put(dir, VoxelShapeUtil.rotate(shape, dir));
      }
   }
}

package com.legacy.blue_skies.blocks.misc;

import com.legacy.blue_skies.client.particle.StarFlareParticle;
import com.legacy.blue_skies.util.LogicUtil;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class StarFlareBlock extends Block {
   private static final VoxelShape FLARE_SHAPE = Block.box(6.0, 6.0, 6.0, 10.0, 10.0, 10.0);
   private static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 15);

   public StarFlareBlock() {
      super(Properties.of().pushReaction(PushReaction.DESTROY).strength(0.0F).sound(SoundType.WOOL).lightLevel(value -> 15).noCollission());
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return FLARE_SHAPE;
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.INVISIBLE;
   }

   public PushReaction getPistonPushReaction(BlockState state) {
      return PushReaction.DESTROY;
   }

   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
      if (level.isClientSide) {
         this.animateTick(state, level, pos, level.random);
      }
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack heldItem = player.getItemInHand(hand);
      if (player.mayBuild() && heldItem.getItem() instanceof DyeItem dye) {
         int appliedColor = dye.getDyeColor().getId();
         if (appliedColor != (Integer)state.getValue(COLOR)) {
            level.setBlockAndUpdate(pos, (BlockState)this.defaultBlockState().setValue(COLOR, appliedColor));
            if (!player.isCreative()) {
               heldItem.shrink(1);
            }

            player.awardStat(Stats.ITEM_USED.get(heldItem.getItem()));
            return InteractionResult.SUCCESS;
         }
      }

      return InteractionResult.PASS;
   }

   @OnlyIn(Dist.CLIENT)
   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
      if (level.isClientSide) {
         DyeColor color = DyeColor.values()[state.getValue(COLOR)];

         for (int i = rand.nextInt(2) + 1; i > 0; i--) {
            double offset = 0.1;
            double x = LogicUtil.plusOrMinus(pos.getX() + 0.5, offset, rand);
            double y = LogicUtil.plusOrMinus(pos.getY() + 0.5, offset, rand);
            double z = LogicUtil.plusOrMinus(pos.getZ() + 0.5, offset, rand);
            double motion = 0.018;
            double dx = LogicUtil.plusOrMinus(motion, rand);
            double dy = LogicUtil.plusOrMinus(motion, rand);
            double dz = LogicUtil.plusOrMinus(motion, rand);
            level.addParticle(StarFlareParticle.dyeToRGB(color), x, y, z, dx, dy, dz);
         }

         level.addParticle(StarFlareParticle.dyeToRGB(color), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
      }
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{COLOR});
   }
}

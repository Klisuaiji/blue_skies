package com.legacy.blue_skies.blocks.construction;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableList.Builder;
import com.legacy.blue_skies.items.tools.AstrolabeItem;
import com.legacy.blue_skies.registries.SkiesParticles;
import com.legacy.blue_skies.registries.SkiesSounds;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StarEmitterBlock extends Block {
   private static final VoxelShape SHAPE = Shapes.or(
      Block.box(2.0, 0.0, 2.0, 14.0, 2.0, 14.0), new VoxelShape[]{Block.box(4.0, 5.0, 4.0, 12.0, 13.0, 12.0), Block.box(7.0, 2.0, 7.0, 9.0, 5.0, 9.0)}
   );
   private static final ImmutableList<Vec3i> HORIZONTAL_TELEPORT_POINTS = ImmutableList.of(
      new Vec3i(0, 0, -1),
      new Vec3i(-1, 0, 0),
      new Vec3i(0, 0, 1),
      new Vec3i(1, 0, 0),
      new Vec3i(-1, 0, -1),
      new Vec3i(1, 0, -1),
      new Vec3i(-1, 0, 1),
      new Vec3i(1, 0, 1)
   );
   private static final ImmutableList<Vec3i> TELEPORT_POINTS = new Builder()
      .addAll(HORIZONTAL_TELEPORT_POINTS)
      .addAll(HORIZONTAL_TELEPORT_POINTS.stream().map(Vec3i::below).iterator())
      .addAll(HORIZONTAL_TELEPORT_POINTS.stream().map(Vec3i::above).iterator())
      .add(new Vec3i(0, 1, 0))
      .build();

   public StarEmitterBlock(Properties properties) {
      super(properties);
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return SHAPE;
   }

   public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
      level.addParticle(
         SkiesParticles.STAR_GLIMMER,
         pos.getX() + 0.3 + rand.nextDouble() * 0.4,
         pos.getY() + 0.4 + rand.nextDouble() * 0.4,
         pos.getZ() + 0.3 + rand.nextDouble() * 0.4,
         0.0,
         0.0,
         0.0
      );
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack stack = player.getItemInHand(hand);
      if (stack.getItem() instanceof AstrolabeItem) {
         if (level.isClientSide) {
            return InteractionResult.SUCCESS;
         }

         level.playSound((Player)null, pos, SkiesSounds.BLOCK_STAR_EMITTER_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
         player.displayClientMessage(Component.translatable("gui.blue_skies.tooltip.astrolabe_pos_set"), true);
         AstrolabeItem.setTeleportPos(stack, pos);
         AstrolabeItem.setTeleportDimension(stack, level.dimension());
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.PASS;
      }
   }

   public static Optional<Vec3> findTeleportPoint(EntityType<?> entity, CollisionGetter level, BlockPos pos) {
      Optional<Vec3> optional = getAvailablePoints(entity, level, pos, true);
      return optional.isPresent() ? optional : getAvailablePoints(entity, level, pos, false);
   }

   private static Optional<Vec3> getAvailablePoints(EntityType<?> type, CollisionGetter level, BlockPos pos, boolean checkCanSpawn) {
      MutableBlockPos blockpos$mutable = new MutableBlockPos();
      UnmodifiableIterator var5 = TELEPORT_POINTS.iterator();

      while (var5.hasNext()) {
         Vec3i vector3i = (Vec3i)var5.next();
         blockpos$mutable.set(pos).move(vector3i);
         Vec3 vector3d = DismountHelper.findSafeDismountLocation(type, level, blockpos$mutable, checkCanSpawn);
         if (vector3d != null) {
            return Optional.of(vector3d);
         }
      }

      return Optional.empty();
   }
}

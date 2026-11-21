package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.blocks.util.IFluidLoggable;
import com.legacy.blue_skies.data.objects.tags.SkiesBiomeTags;
import com.legacy.blue_skies.entities.projectile.SpearEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SkyDripstoneBlock extends Block implements IFluidLoggable, Fallable {
   private static final VoxelShape[] STALAGMITE_SHAPES = new VoxelShape[]{
      Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0),
      Block.box(4.0, 0.0, 4.0, 12.0, 11.0, 12.0),
      Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0),
      Block.box(0.5, 0.0, 0.5, 15.5, 16.0, 15.5)
   };
   private static final VoxelShape[] STALACTITE_SHAPES = new VoxelShape[]{
      Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0),
      Block.box(4.0, 3.0, 4.0, 12.0, 16.0, 12.0),
      Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0),
      Block.box(0.5, 0.0, 0.5, 15.5, 16.0, 15.5)
   };
   public static final EnumProperty<SkyDripstoneBlock.Section> SECTION = EnumProperty.create("section", SkyDripstoneBlock.Section.class);
   public static final EnumProperty<SkyDripstoneBlock.Type> TYPE = EnumProperty.create("type", SkyDripstoneBlock.Type.class);
   private static final float GROW_CHANCE = 0.011377778F;
   private static final VoxelShape REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

   public SkyDripstoneBlock(Properties properties) {
      super(properties.offsetType(OffsetType.XZ).randomTicks());
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(SECTION, SkyDripstoneBlock.Section.TIP))
               .setValue(TYPE, SkyDripstoneBlock.Type.STALAGMITE))
            .setValue(FLUIDLOGGED, IFluidLoggable.FluidLogging.EMPTY)
      );
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      Vec3 offset = state.getOffset(level, pos);
      return (state.getValue(TYPE) == SkyDripstoneBlock.Type.STALAGMITE ? STALAGMITE_SHAPES : STALACTITE_SHAPES)[((SkyDripstoneBlock.Section)state.getValue(
               SECTION
            ))
            .ordinal()]
         .move(offset.x, offset.y, offset.z);
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      SkyDripstoneBlock.Type type = (SkyDripstoneBlock.Type)state.getValue(TYPE);
      if (state.getValue(SECTION) == SkyDripstoneBlock.Section.TIP && !level.getBlockState(pos.relative(type.growthDir.getOpposite())).is(this)) {
         return Shapes.empty();
      }

      Vec3 offset = state.getOffset(level, pos);
      return (type == SkyDripstoneBlock.Type.STALAGMITE ? STALAGMITE_SHAPES : STALACTITE_SHAPES)[((SkyDripstoneBlock.Section)state.getValue(SECTION)).ordinal()]
         .move(offset.x, offset.y, offset.z);
   }

   public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
      int fluidLight = ((IFluidLoggable.FluidLogging)state.getValue(FLUIDLOGGED)).fluidState.createLegacyBlock().getLightEmission(level, pos);
      int blockLight = super.getLightEmission(state, level, pos);
      return Math.max(fluidLight, blockLight);
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return this.getStateForPlacement(context.getLevel(), context.getClickedPos(), context.getNearestLookingDirections());
   }

   @Nullable
   public BlockState getStateForPlacement(Level level, BlockPos pos, Direction... directions) {
      for (Direction dir : directions) {
         if (dir.getAxis() == Axis.Y) {
            BlockState state = (BlockState)this.defaultBlockState()
               .setValue(TYPE, dir == SkyDripstoneBlock.Type.STALAGMITE.growthDir ? SkyDripstoneBlock.Type.STALACTITE : SkyDripstoneBlock.Type.STALAGMITE);
            if (state.canSurvive(level, pos)) {
               return (BlockState)state.setValue(FLUIDLOGGED, IFluidLoggable.FluidLogging.from(level, pos));
            }
         }
      }

      return null;
   }

   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      Direction growthDir = ((SkyDripstoneBlock.Type)state.getValue(TYPE)).growthDir;
      BlockState attachedTo = level.getBlockState(pos.relative(growthDir.getOpposite()));
      return attachedTo.is(this) || attachedTo.isFaceSturdy(level, pos.relative(growthDir.getOpposite()), growthDir);
   }

   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
      if (!state.canSurvive(level, currentPos)) {
         level.scheduleTick(currentPos, state.getBlock(), 0);
         return state;
      }

      IFluidLoggable.FluidLogging fluid = (IFluidLoggable.FluidLogging)state.getValue(FLUIDLOGGED);
      if (fluid != IFluidLoggable.FluidLogging.EMPTY) {
         level.scheduleTick(currentPos, fluid.fluid, fluid.fluid.getTickDelay(level));
      }

      SkyDripstoneBlock.Type type = (SkyDripstoneBlock.Type)state.getValue(TYPE);
      Direction growthDir = type.growthDir;
      BlockState attached = level.getBlockState(currentPos.relative(growthDir));
      SkyDripstoneBlock.Section section;
      if (attached.is(this)) {
         if (attached.getValue(TYPE) == type) {
            section = level.getBlockState(currentPos.relative(growthDir.getOpposite())).is(this)
               ? SkyDripstoneBlock.Section.MIDDLE
               : SkyDripstoneBlock.Section.getWider((SkyDripstoneBlock.Section)attached.getValue(SECTION));
         } else {
            section = SkyDripstoneBlock.Section.TIP_MERGE;
         }
      } else {
         section = SkyDripstoneBlock.Section.TIP;
      }

      return (BlockState)state.setValue(SECTION, section);
   }

   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      if (state.getValue(TYPE) == SkyDripstoneBlock.Type.STALAGMITE) {
         if (!this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
         }
      } else {
         MutableBlockPos checkPos = pos.mutable();
         BlockState checkBlock = state;

         while (checkBlock.is(this) && checkBlock.getValue(TYPE) == SkyDripstoneBlock.Type.STALACTITE) {
            FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, checkPos, checkBlock);
            if (((SkyDripstoneBlock.Section)checkBlock.getValue(SECTION)).damaging) {
               float damagePerDist = Math.max(1 + pos.getY() - checkPos.getY(), 6);
               fallingBlock.setHurtsEntities(damagePerDist, 40);
               break;
            }

            checkPos.move(Direction.DOWN);
            checkBlock = level.getBlockState(checkPos);
         }
      }
   }

   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      if (this.shouldDrip(state) && this.getFluidAbove(state, level, pos) != Fluids.EMPTY) {
         float chance = rand.nextFloat();
         if (chance < 0.011377778F) {
            if (rand.nextBoolean()) {
               BlockPos placePos = pos.below();
               if (level.getBlockState(placePos).canBeReplaced()) {
                  level.setBlock(
                     placePos,
                     (BlockState)((BlockState)this.defaultBlockState().setValue(TYPE, SkyDripstoneBlock.Type.STALACTITE))
                        .setValue(FLUIDLOGGED, IFluidLoggable.FluidLogging.from(level, placePos)),
                     3
                  );
               }
            } else {
               MutableBlockPos checkPos = pos.below().mutable();
               BlockState checkState = level.getBlockState(checkPos);

               for (int i = 0; i < 16 && canDripThrough(level, checkPos, checkState); i++) {
                  checkPos.move(Direction.DOWN);
                  checkState = level.getBlockState(checkPos);
               }

               BlockPos placePos = checkPos.immutable().above();
               if (level.getBlockState(placePos).canBeReplaced()) {
                  BlockState placeState = this.defaultBlockState();
                  if (placeState.canSurvive(level, placePos)) {
                     level.setBlock(placePos, (BlockState)placeState.setValue(FLUIDLOGGED, IFluidLoggable.FluidLogging.from(level, placePos)), 3);
                  }
               }
            }
         }
      }
   }

   private Fluid getFluidAbove(BlockState state, Level level, BlockPos pos) {
      BlockState checkState = state;
      MutableBlockPos checkPos = pos.mutable();

      for (int i = 0; i < 10 && checkState.is(this); i++) {
         checkPos.move(Direction.UP);
         checkState = level.getBlockState(checkPos);
      }

      return level.getFluidState(checkPos.above()).getType();
   }

   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
      if (this.shouldDrip(state) && rand.nextFloat() < 0.12F) {
         Vec3 offset = state.getOffset(level, pos);
         double x = pos.getX() + 0.5 + offset.x;
         double y = pos.getY() + 0.0625;
         double z = pos.getZ() + 0.5 + offset.z;
         Fluid fluid = this.getDripFluid(state, level, pos);
         ParticleOptions particleOps = fluid.is(FluidTags.LAVA) ? ParticleTypes.DRIPPING_DRIPSTONE_LAVA : ParticleTypes.DRIPPING_DRIPSTONE_WATER;
         level.addParticle(particleOps, x, y, z, 0.0, 0.0, 0.0);
      }
   }

   private Fluid getDripFluid(BlockState state, Level level, BlockPos pos) {
      Fluid fluidAbove = this.getFluidAbove(state, level, pos);
      return (Fluid)(fluidAbove != Fluids.EMPTY || !level.dimensionType().ultraWarm() && !level.getBiome(pos).is(SkiesBiomeTags.DRIPSTONES_DRIP_LAVA)
         ? fluidAbove
         : Fluids.LAVA);
   }

   private boolean shouldDrip(BlockState state) {
      return state.getValue(SECTION) == SkyDripstoneBlock.Section.TIP
         && state.getValue(TYPE) == SkyDripstoneBlock.Type.STALACTITE
         && state.getValue(FLUIDLOGGED) == IFluidLoggable.FluidLogging.EMPTY;
   }

   public FluidState getFluidState(BlockState state) {
      return ((IFluidLoggable.FluidLogging)state.getValue(FLUIDLOGGED)).fluidState;
   }

   public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
      return state.getValue(SECTION) == SkyDripstoneBlock.Section.TIP
         && level.getBlockState(pos.relative(((SkyDripstoneBlock.Type)state.getValue(TYPE)).growthDir.getOpposite())).getBlock() != this;
   }

   public PushReaction getPistonPushReaction(BlockState state) {
      return PushReaction.DESTROY;
   }

   public void onProjectileHit(Level level, BlockState state, BlockHitResult hitResult, Projectile projectile) {
      BlockPos blockpos = hitResult.getBlockPos();
      if (!level.isClientSide
         && projectile.mayInteract(level, blockpos)
         && (projectile instanceof ThrownTrident || projectile instanceof SpearEntity)
         && projectile.getDeltaMovement().length() > 0.6) {
         level.destroyBlock(blockpos, true);
      }
   }

   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDist) {
      if (state.getValue(TYPE) == SkyDripstoneBlock.Type.STALAGMITE && ((SkyDripstoneBlock.Section)state.getValue(SECTION)).damaging) {
         entity.causeFallDamage(fallDist + 2.0F, 2.0F, level.damageSources().stalagmite());
      } else {
         super.fallOn(level, state, pos, entity, fallDist);
      }
   }

   public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity fallingBlock) {
      if (!fallingBlock.isSilent()) {
         level.levelEvent(1045, pos, 0);
      }
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{SECTION, TYPE, FLUIDLOGGED});
   }

   private static boolean canDripThrough(BlockGetter level, BlockPos pos, BlockState state) {
      if (state.isAir()) {
         return true;
      } else {
         return !state.isSolidRender(level, pos) && state.getFluidState().isEmpty()
            ? !Shapes.joinIsNotEmpty(REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK, state.getCollisionShape(level, pos), BooleanOp.AND)
            : false;
      }
   }

   public enum Section implements StringRepresentable {
      TIP_MERGE("tip_merge", true),
      TIP("tip", true),
      MIDDLE("middle", false),
      BASE("base", false);

      final String name;
      final boolean damaging;

      Section(String name, boolean damaging) {
         this.name = name;
         this.damaging = damaging;
      }

      public static SkyDripstoneBlock.Section getWider(SkyDripstoneBlock.Section section) {
         return switch (section) {
            case TIP -> MIDDLE;
            case MIDDLE -> BASE;
            default -> BASE;
         };
      }

      public static SkyDripstoneBlock.Section getNarrower(SkyDripstoneBlock.Section section) {
         return switch (section) {
            case MIDDLE -> TIP;
            case BASE -> MIDDLE;
            default -> TIP;
         };
      }

      public String getSerializedName() {
         return this.name;
      }
   }

   public enum Type implements StringRepresentable {
      STALAGMITE("stalagmite", Direction.UP),
      STALACTITE("stalactite", Direction.DOWN);

      final String name;
      public final Direction growthDir;

      Type(String name, Direction growthDir) {
         this.name = name;
         this.growthDir = growthDir;
      }

      public String getSerializedName() {
         return this.name;
      }
   }
}

package com.legacy.blue_skies.blocks.misc;

import com.legacy.blue_skies.block_entity.TrophyBlockEntity;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.util.DirectionUtil;
import com.legacy.structure_gel.api.util.VoxelShapeUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TrophyBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
   public static final MapCodec<TrophyBlock> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(TrophyBlock.Type.CODEC.fieldOf("type").forGetter(b -> b.type), propertiesCodec()).apply(instance, TrophyBlock::new)
   );
   public static final EnumProperty<TrophyBlock.Tier> TIER = EnumProperty.create("tier", TrophyBlock.Tier.class);
   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   private final TrophyBlock.Type type;
   private final Map<Direction, VoxelShape> shapes = new HashMap<>();
   private final Supplier<EntityType<?>> boss;

   public TrophyBlock(TrophyBlock.Type type, Properties builder) {
      super(builder);
      this.type = type;
      this.boss = type.boss;

      for (Direction dir : DirectionUtil.HORIZONTAL) {
         this.shapes.put(dir, VoxelShapeUtil.rotate(type.shape, dir));
      }

      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(TIER, TrophyBlock.Tier.BRONZE)).setValue(FACING, Direction.NORTH))
            .setValue(WATERLOGGED, false)
      );
   }

   protected MapCodec<? extends BaseEntityBlock> codec() {
      return CODEC;
   }

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return this.shapes.get(state.getValue(FACING));
   }

   public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader world, BlockPos pos, Player player) {
      return ((TrophyBlock.Tier)state.getValue(TIER)).getItem(this);
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TrophyBlockEntity(pos, state);
   }

   @OnlyIn(Dist.CLIENT)
   public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
      return 1.0F;
   }

   public boolean useShapeForLightOcclusion(BlockState state) {
      return true;
   }

   public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
      return true;
   }

   public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
      BlockEntity tile = world.getBlockEntity(pos);
      if (tile instanceof TrophyBlockEntity) {
         ((TrophyBlockEntity)tile).setLabel(stack.hasCustomHoverName() ? stack.getHoverName() : this.boss.get().getDescription());
      }
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      Direction facing = context.getHorizontalDirection().getOpposite();
      boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
      return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, facing)).setValue(WATERLOGGED, waterlogged);
   }

   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
      }

      return state;
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{TIER, FACING, WATERLOGGED});
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   public BlockState rotate(BlockState state, Rotation rot) {
      return (BlockState)state.setValue(FACING, rot.rotate((Direction)state.getValue(FACING)));
   }

   public BlockState mirror(BlockState state, Mirror mirror) {
      return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
   }

   public static enum Tier implements StringRepresentable {
      BRONZE("bronze"),
      SILVER("silver"),
      GOLD("gold"),
      PLATINUM("platinum");

      public final String name;

      private Tier(String name) {
         this.name = name;
      }

      public String getSerializedName() {
         return this.name;
      }

      public ItemStack getItem(TrophyBlock trophy) {
         ItemStack stack = new ItemStack(trophy);
         CompoundTag nbt = stack.getOrCreateTag();
         CompoundTag states = new CompoundTag();
         states.putString("tier", this.getSerializedName());
         nbt.put("BlockStateTag", states);
         return stack;
      }

      public static TrophyBlock.Tier from(String name) {
         for (TrophyBlock.Tier t : values()) {
            if (t.name.equals(name)) {
               return t;
            }
         }

         return BRONZE;
      }
   }

   public static enum Type implements StringRepresentable {
      SUMMONER("summoner", () -> SkiesEntityTypes.SUMMONER, Shapes.or(Block.box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0), Block.box(4.0, 4.0, 6.0, 12.0, 16.0, 10.0))),
      ALCHEMIST(
         "alchemist",
         () -> SkiesEntityTypes.ALCHEMIST,
         Shapes.or(
            Block.box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            new VoxelShape[]{Block.box(3.0, 4.0, 7.0, 13.0, 6.0, 9.0), Block.box(3.0, 14.0, 7.0, 13.0, 16.0, 9.0), Block.box(4.0, 6.0, 7.5, 12.0, 14.0, 8.5)}
         )
      ),
      STARLIT_CRUSHER(
         "starlit_crusher",
         () -> SkiesEntityTypes.STARLIT_CRUSHER,
         Shapes.or(
            Block.box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0), new VoxelShape[]{Block.box(3.0, 10.0, 5.0, 13.0, 16.0, 11.0), Block.box(7.0, 4.0, 7.0, 9.0, 10.0, 9.0)}
         )
      ),
      ARACHNARCH(
         "arachnarch",
         () -> SkiesEntityTypes.ARACHNARCH,
         Shapes.or(
            Block.box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            new VoxelShape[]{
               Block.box(6.0, 4.0, 7.0, 10.0, 8.0, 9.0),
               Block.box(7.0, 8.0, 7.0, 9.0, 10.0, 9.0),
               Block.box(4.0, 10.0, 7.0, 12.0, 16.0, 9.0),
               Block.box(0.0, 4.0, 7.5, 16.0, 16.0, 8.5)
            }
         )
      );

      public static final Codec<TrophyBlock.Type> CODEC = StringRepresentable.fromEnum(TrophyBlock.Type::values);
      final String name;
      public final Supplier<EntityType<?>> boss;
      public final VoxelShape shape;

      private Type(String name, Supplier<EntityType<?>> boss, VoxelShape shape) {
         this.name = name;
         this.boss = boss;
         this.shape = shape;
      }

      public String getSerializedName() {
         return this.name;
      }
   }
}

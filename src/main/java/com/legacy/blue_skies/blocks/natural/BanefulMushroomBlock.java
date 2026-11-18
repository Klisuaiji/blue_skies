package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.client.particle.data.PoisonData;
import com.legacy.blue_skies.registries.SkiesVillagers;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BanefulMushroomBlock extends BushBlock {
   public static final MapCodec<BanefulMushroomBlock> CODEC = simpleCodec(BanefulMushroomBlock::new);
   private static final BooleanProperty OPEN = BooleanProperty.create("open");
   protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);

   public BanefulMushroomBlock(Properties properties) {
      super(properties);
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(OPEN, false));
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      Vec3 offset = state.getOffset(level, pos);
      return SHAPE.move(offset.x, offset.y, offset.z);
   }

   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      if ((Boolean)state.getValue(OPEN)) {
         level.setBlock(pos, this.defaultBlockState(), 3);
      }
   }

   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      poisonTarget(state, level, pos, entity, this, OPEN);
   }

   public static void poisonTarget(BlockState state, Level level, BlockPos pos, Entity entity, Block block, BooleanProperty openProperty) {
      if (entity instanceof LivingEntity living && living.canBeAffected(new MobEffectInstance(MobEffects.POISON))) {
         if (entity instanceof Villager villager && villager.getVillagerData().getType() == SkiesVillagers.SHADED_WOODLANDS) {
            return;
         }

         int durration = 20 * (level.getDifficulty().getId() + 3);
         int amplifier = level.getDifficulty().getId() / 2;
         if (level.isClientSide() && !(Boolean)state.getValue(openProperty)) {
            block.animateTick(level.getBlockState(pos), level, pos, level.random);
         }

         level.setBlockAndUpdate(pos, (BlockState)block.defaultBlockState().setValue(openProperty, true));
         if (!living.hasEffect(MobEffects.POISON)) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, durration, amplifier));
         }

         level.scheduleTick(pos, block, 100);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
      if ((Boolean)state.getValue(OPEN) && rand.nextFloat() < 0.7F) {
         Vec3 offset = state.getOffset(level, pos);

         for (int i = rand.nextInt(2) + 1; i > 0; i--) {
            float dx = (rand.nextFloat() - 0.5F) / 7.0F;
            float dy = rand.nextFloat() / 10.0F;
            float dz = (rand.nextFloat() - 0.5F) / 7.0F;
            level.addParticle(new PoisonData(false), pos.getX() + 0.5 + offset.x, pos.getY() + 0.5, pos.getZ() + 0.5 + offset.z, dx, dy, dz);
         }
      }
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{OPEN});
   }

   protected MapCodec<? extends BushBlock> codec() {
      return CODEC;
   }
}

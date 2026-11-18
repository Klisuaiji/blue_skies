package com.legacy.blue_skies.entities.hostile.boss.summons;

import com.google.common.collect.Lists;
import com.legacy.blue_skies.blocks.dungeon.SkyDecayingSpikeBlock;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesDamageSources;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.structure_gel.api.util.Positions;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Entity.MovementEmission;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public class DecayingSpikeEntity extends FallingBlockEntity {
   public static final EntityDataAccessor<Boolean> DOWNWARD = SynchedEntityData.defineId(DecayingSpikeEntity.class, EntityDataSerializers.BOOLEAN);
   private BlockState state = this.getDownwards()
      ? (BlockState)SkiesBlocks.decaying_spike.defaultBlockState().setValue(SkyDecayingSpikeBlock.DOWNWARD, false)
      : SkiesBlocks.decaying_spike.defaultBlockState();
   private int timeExisted = 0;
   public boolean upward;
   private LivingEntity caster;
   private UUID casterUuid;

   public DecayingSpikeEntity(EntityType<? extends DecayingSpikeEntity> type, Level worldIn) {
      super(SkiesEntityTypes.DECAYING_SPIKE, worldIn);
   }

   public DecayingSpikeEntity(Level world, BlockPos pos, BlockState state, LivingEntity casterIn) {
      this(SkiesEntityTypes.DECAYING_SPIKE, world);
      this.blocksBuilding = true;
      this.setCaster(casterIn);
      this.setState(state);
      this.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
   }

   public void setCaster(@Nullable LivingEntity caster) {
      this.caster = caster;
      this.casterUuid = caster == null ? null : caster.getUUID();
   }

   @Nullable
   public LivingEntity getCaster() {
      if (this.caster == null && this.casterUuid != null && this.level() instanceof ServerLevel) {
         Entity entity = ((ServerLevel)this.level()).getEntity(this.casterUuid);
         if (entity instanceof LivingEntity) {
            this.caster = (LivingEntity)entity;
         }
      }

      return this.caster;
   }

   public void setPos(double x, double y, double z) {
      super.setPos(x, y, z);
      if (this.level().isClientSide && (this.state == null || this.state == Blocks.AIR.defaultBlockState())) {
         this.state = this.level().getBlockState(this.blockPosition());
      }
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(DOWNWARD, true);
   }

   public void tick() {
      if (this.getBlockState() != null && (this.getBlockState() == null || !this.getBlockState().isAir())) {
         this.xo = this.getX();
         this.yo = this.getY();
         this.zo = this.getZ();
         this.timeExisted++;
         if (!this.isNoGravity() || !this.upward) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
         }

         this.move(MoverType.SELF, this.getDeltaMovement());
         Block block = SkiesBlocks.decaying_spike;
         if (!this.level().isClientSide) {
            BlockPos blockpos1 = this.blockPosition();
            if (!this.onGround()) {
               if (this.timeExisted > 100 && !this.level().isClientSide && (blockpos1.getY() < 1 || blockpos1.getY() > 256)
                  || this.timeExisted > 600
                  || !this.getDownwards() && this.timeExisted >= 15) {
                  if (this.level() instanceof ServerLevel) {
                     ((ServerLevel)this.level())
                        .sendParticles(
                           new BlockParticleOption(ParticleTypes.BLOCK, SkiesBlocks.lunar_stone.defaultBlockState()),
                           this.getX(),
                           this.getY(0.6),
                           this.getZ(),
                           10,
                           this.getBbWidth() / 4.0F,
                           this.getBbHeight() / 4.0F,
                           this.getBbWidth() / 4.0F,
                           0.05
                        );
                  }

                  this.killSpike();
               }
            } else {
               BlockState blockstate = this.level().getBlockState(blockpos1);
               if (this.level().isEmptyBlock(Positions.blockPos(this.getX(), this.getY() - 0.01F, this.getZ()))
                  && FallingBlock.isFree(this.level().getBlockState(Positions.blockPos(this.getX(), this.getY() - 0.01F, this.getZ())))) {
                  this.setOnGround(false);
                  return;
               }

               if (blockstate.getBlock() != Blocks.MOVING_PISTON) {
                  this.killSpike();
                  if (!(this.getCaster() instanceof Player) || this.getCaster() instanceof Player && !EntityUtil.isInDungeon(this.level(), blockpos1)) {
                     boolean flag2 = blockstate.canBeReplaced(
                        new DirectionalPlaceContext(this.level(), blockpos1, Direction.DOWN, ItemStack.EMPTY, Direction.UP)
                     );
                     boolean flag3 = this.state.canSurvive(this.level(), blockpos1);
                     if (flag2 && flag3) {
                        if (this.state.hasProperty(BlockStateProperties.WATERLOGGED) && this.level().getFluidState(blockpos1).getType() == Fluids.WATER) {
                           this.state = (BlockState)this.state.setValue(BlockStateProperties.WATERLOGGED, true);
                        }

                        this.level().setBlock(blockpos1, this.state, 3);
                     }
                  } else if (this.level() instanceof ServerLevel) {
                     ((ServerLevel)this.level())
                        .sendParticles(
                           new BlockParticleOption(ParticleTypes.BLOCK, SkiesBlocks.lunar_stone.defaultBlockState()),
                           this.getX(),
                           this.getY(0.6),
                           this.getZ(),
                           10,
                           this.getBbWidth() / 4.0F,
                           this.getBbHeight() / 4.0F,
                           this.getBbWidth() / 4.0F,
                           0.05
                        );
                  }

                  if (block instanceof Fallable) {
                     ((Fallable)block).onBrokenAfterFall(this.level(), blockpos1, this);
                  }
               }
            }

            if (this.getDeltaMovement().y() != 0.0) {
               for (Entity entity : Lists.newArrayList(this.level().getEntities(this, this.getBoundingBox()))) {
                  this.damage(entity, 0.0F);
               }
            }
         }

         if (this.timeExisted > 100) {
            if (this.level() instanceof ServerLevel) {
               ((ServerLevel)this.level())
                  .sendParticles(
                     new BlockParticleOption(ParticleTypes.BLOCK, SkiesBlocks.lunar_stone.defaultBlockState()),
                     this.getX(),
                     this.getY(0.6),
                     this.getZ(),
                     10,
                     this.getBbWidth() / 4.0F,
                     this.getBbHeight() / 4.0F,
                     this.getBbWidth() / 4.0F,
                     0.05
                  );
            }

            this.killSpike();
         }

         this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
      } else {
         this.killSpike();
      }
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      int i = Mth.ceil(distance - 1.0F);
      if (i > 0) {
         for (Entity entity : Lists.newArrayList(this.level().getEntities(this, this.getBoundingBox()))) {
            this.damage(entity, 1.0F);
         }
      }

      return false;
   }

   private void damage(Entity hitEntity, float amp) {
      LivingEntity caster = this.getCaster();
      if (hitEntity.isAlive() && !hitEntity.isInvulnerable() && hitEntity != caster) {
         if (caster != null && caster.isAlliedTo(hitEntity)) {
            return;
         }

         hitEntity.hurt(SkiesDamageSources.get(hitEntity.level()).fallingSpike(this, caster), 5.0F + amp);
      }
   }

   private void killSpike() {
      this.discard();
   }

   public void setState(BlockState state) {
      this.state = state;
   }

   public BlockState getBlockState() {
      return this.state;
   }

   public boolean getDownwards() {
      return (Boolean)this.entityData.get(DOWNWARD);
   }

   public void setDownwards(boolean downwardIn) {
      this.entityData.set(DOWNWARD, downwardIn);
   }

   protected MovementEmission getMovementEmission() {
      return MovementEmission.EVENTS;
   }

   public boolean isPickable() {
      return this.isAlive();
   }

   public void handleEntityEvent(byte id) {
      super.handleEntityEvent(id);
   }

   protected void readAdditionalSaveData(CompoundTag compound) {
      this.setDownwards(compound.getBoolean("Downward"));
      this.state = Block.stateById(compound.getInt("blockstateId"));
      this.casterUuid = compound.getUUID("OwnerUUID");
   }

   protected void addAdditionalSaveData(CompoundTag compound) {
      compound.putBoolean("Downward", this.getDownwards());
      compound.putInt("blockstateId", Block.getId(this.state));
      if (this.casterUuid != null) {
         compound.putUUID("OwnerUUID", this.casterUuid);
      }
   }
}

package com.legacy.blue_skies.entities.util.base;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class SkiesMonsterEntity extends Monster {
   protected SkiesMonsterEntity(EntityType<? extends SkiesMonsterEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes();
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn
   ) {
      if (reason == MobSpawnType.NATURAL) {
         int rot = worldIn.getRandom().nextInt(360);
         this.setYRot(rot);
         this.setYHeadRot(rot);
      }

      // 1.21.1: Mob#finalizeSpawn lost its CompoundTag parameter (spawn NBT is applied elsewhere).
      return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
   }

   public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
      return 0.0F;
   }

   public void spawnBlockParticles(BlockState state, int count) {
      if (this.level() instanceof ServerLevel sl) {
         sl.sendParticles(
            new BlockParticleOption(ParticleTypes.BLOCK, state),
            this.getX(),
            this.getY() + this.getBbHeight() / 1.5,
            this.getZ(),
            count,
            this.getBbWidth() / 4.0F,
            this.getBbHeight() / 4.0F,
            this.getBbWidth() / 4.0F,
            0.05
         );
      }
   }

   public float getMoveForward() {
      return this.zza;
   }
}

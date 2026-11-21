package com.legacy.blue_skies.entities.projectile;

import com.legacy.blue_skies.entities.hostile.boss.StarlitCrusherEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntWallEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;

public class EntLeafEntity extends ThrowableProjectile {
   public EntLeafEntity(EntityType<? extends EntLeafEntity> type, Level world) {
      super(type, world);
   }

   public EntLeafEntity(EntityType<? extends EntLeafEntity> type, LivingEntity thrower, Level world) {
      super(type, thrower, world);
   }

   protected void onHit(HitResult result) {
      Type raytraceresult$type = result.getType();
      if (raytraceresult$type == Type.BLOCK) {
         BlockHitResult traceResult = (BlockHitResult)result;
         BlockState blockstate = this.level().getBlockState(traceResult.getBlockPos());
         blockstate.onProjectileHit(this.level(), blockstate, traceResult, this);
         this.discard();
      }

      if (raytraceresult$type == Type.ENTITY) {
         Entity entity = ((EntityHitResult)result).getEntity();
         if (this.getOwner() != null && !entity.isAlliedTo(this.getOwner()) && !(entity instanceof EntWallEntity)) {
            int i = 1 + this.level().getDifficulty().getId();
            if (this.getOwner() instanceof StarlitCrusherEntity) {
               i = 1 + ((StarlitCrusherEntity)this.getOwner()).getDifficultyID();
            }

            if (!entity.isInvulnerable()) {
               entity.hurt(this.damageSources().thrown(this, this.getOwner()), i);
               this.discard();
            }
         }
      }
   }

   public void tick() {
      super.tick();
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
   }
}

package com.legacy.blue_skies.entities.projectile;

import com.google.common.collect.Lists;
import com.legacy.blue_skies.entities.hostile.boss.SummonerEntity;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBoss;
import com.legacy.blue_skies.registries.SkiesDamageSources;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesParticles;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.GeometryHelper;
import java.util.List;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.EventHooks;

public class FluctuantSphereEntity extends AbstractHurtingProjectile {
   public FluctuantSphereEntity(EntityType<? extends FluctuantSphereEntity> type, Level world) {
      super(type, world);
   }

   public FluctuantSphereEntity(Level worldIn, SummonerEntity summoner) {
      this(SkiesEntityTypes.FLUCTUANT_SPHERE, worldIn);
      this.setOwner(summoner);
      this.setPos(
         summoner.getX() - (summoner.getBbWidth() + 1.0F) * 0.5 * Mth.sin(summoner.yBodyRot * (float) (Math.PI / 180.0)),
         summoner.getY() + summoner.getEyeHeight() - 0.1F,
         summoner.getZ() + (summoner.getBbWidth() + 1.0F) * 0.5 * Mth.cos(summoner.yBodyRot * (float) (Math.PI / 180.0))
      );
   }

   @OnlyIn(Dist.CLIENT)
   public FluctuantSphereEntity(Level worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
      super(SkiesEntityTypes.FLUCTUANT_SPHERE, x, y, z, accelX, accelY, accelZ, worldIn);
   }

   public FluctuantSphereEntity(Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
      super(SkiesEntityTypes.FLUCTUANT_SPHERE, shooter, accelX, accelY, accelZ, worldIn);
   }

   protected void onHit(HitResult result) {
      Type raytraceresult$type = result.getType();
      if (raytraceresult$type == Type.BLOCK && !this.level().isClientSide && this.tickCount > 20 || this.tickCount >= 100) {
         this.vanish();
      }
   }

   public boolean isPickable() {
      return false;
   }

   public void tick() {
      Entity entity = this.getOwner();
      if (this.level().isClientSide || (entity == null || entity.isAlive()) && this.level().isLoaded(this.blockPosition())) {
         super.tick();
         if (this.shouldBurn()) {
            this.setSecondsOnFire(1);
         }

         HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, x$0 -> this.canHitEntity(x$0));
         if (raytraceresult.getType() != Type.MISS && !EventHooks.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
         }

         this.checkInsideBlocks();
         Vec3 vector3d = this.getDeltaMovement();
         double x = this.getX() + vector3d.x;
         double y = this.getY() + vector3d.y;
         double z = this.getZ() + vector3d.z;
         ProjectileUtil.rotateTowardsMovement(this, 0.2F);
         float inirtia = this.getInertia();
         if (this.isInWater()) {
            for (int i = 0; i < 4; i++) {
               this.level()
                  .addParticle(ParticleTypes.BUBBLE, x - vector3d.x * 0.25, y - vector3d.y * 0.25, z - vector3d.z * 0.25, vector3d.x, vector3d.y, vector3d.z);
            }

            inirtia = 0.8F;
         }

         this.setDeltaMovement(vector3d.add(this.xPower, this.yPower, this.zPower).scale(inirtia));
         this.setPos(x, y, z);
      } else {
         this.discard();
      }

      if (this.level() instanceof ServerLevel) {
         ServerLevel world = (ServerLevel)this.level();

         for (ServerPlayer p : world.players()) {
            world.sendParticles(p, this.getTrailParticle(), true, this.getX(), this.getY() + 0.4, this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
         }
      }

      int timeUntilHit = 8;
      if (this.getOwner() instanceof SummonerEntity) {
         SummonerEntity summoner = (SummonerEntity)this.getOwner();
         timeUntilHit = summoner.getDifficultyID() == ISkyBoss.BossLevel.SILVER.getId()
            ? 8
            : (
               summoner.getDifficultyID() == ISkyBoss.BossLevel.GOLD.getId()
                  ? 10
                  : (summoner.getDifficultyID() >= ISkyBoss.BossLevel.PLATINUM.getId() ? 14 : 0)
            );
      }

      List<Entity> list = Lists.newArrayList(this.level().getEntities(this, this.getBoundingBox().inflate(1.5)));

      for (Entity e : list) {
         if (e != null
            && e instanceof LivingEntity
            && ((LivingEntity)e).invulnerableTime <= timeUntilHit
            && this.getOwner() != null
            && e != this.getOwner()
            && !e.isAlliedTo(this.getOwner())) {
            this.causeDamage(e, Math.max(1.0F, 3.0F / (list.size() * 0.5F)));
         }
      }

      if (!this.level().isClientSide && this.tickCount > 120) {
         this.vanish();
      }
   }

   public void causeDamage(Entity entity, float amount) {
      boolean flag = entity.hurt(SkiesDamageSources.get(entity.level()).fluctuantSphere(this, this.getOwner()), amount);
      if (entity instanceof LivingEntity && flag && this.level() instanceof ServerLevel) {
         GeometryHelper.drawParticleLine(
            (ServerLevel)this.level(),
            SkiesParticles.FLUCTUANT_SPHERE_BEAM,
            this.position().add(0.0, 0.5, 0.0),
            new Vec3(entity.getX(), entity.getY() + entity.getBbHeight() / 2.0F, entity.getZ()),
            0.2,
            0.0,
            true
         );
      }
   }

   public void vanish() {
      if (this.level() instanceof ServerLevel sl) {
         double d0 = this.random.nextGaussian() * 0.02;
         double d1 = this.random.nextGaussian() * 0.02;
         double d2 = this.random.nextGaussian() * 0.02;
         double d3 = 10.0;
         sl.sendParticles(
            ParticleTypes.POOF,
            this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth() - d0 * d3,
            this.getY() + this.random.nextFloat() * this.getBbHeight() - d1 * d3,
            this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth() - d2 * d3,
            20,
            d0,
            d1,
            d2,
            0.1
         );
      }

      this.discard();
      this.level().playSound((Player)null, this.blockPosition(), SkiesSounds.ENTITY_FLUCTUANT_SPHERE_DISAPPEAR, SoundSource.HOSTILE, 1.0F, 2.0F);
   }

   protected ParticleOptions getTrailParticle() {
      return SkiesParticles.FLUCTUANT_SPHERE;
   }

   public boolean hurt(DamageSource source, float amount) {
      return false;
   }

   protected boolean shouldBurn() {
      return false;
   }

   protected float getInertia() {
      return 0.7F;
   }
}

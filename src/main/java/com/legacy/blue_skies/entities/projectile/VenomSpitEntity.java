package com.legacy.blue_skies.entities.projectile;

import com.legacy.blue_skies.client.particle.data.PoisonData;
import com.legacy.blue_skies.registries.SkiesDamageSources;
import com.legacy.blue_skies.registries.SkiesEffects;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.EventHooks;

public class VenomSpitEntity extends Projectile {
   protected static final EntityDataAccessor<Boolean> DEADLY = SynchedEntityData.defineId(VenomSpitEntity.class, EntityDataSerializers.BOOLEAN);

   public VenomSpitEntity(EntityType<? extends VenomSpitEntity> type, Level world) {
      super(type, world);
   }

   public VenomSpitEntity(Level worldIn, LivingEntity spider) {
      this(worldIn, spider, true);
   }

   public VenomSpitEntity(Level worldIn, LivingEntity spider, boolean spawnOffsetted) {
      this(SkiesEntityTypes.VENOM_SPIT, worldIn);
      super.setOwner(spider);
      if (spawnOffsetted) {
         this.setPos(
            spider.getX() - (spider.getBbWidth() + 1.0F) * 0.5 * Mth.sin(spider.yBodyRot * (float) (Math.PI / 180.0)),
            spider.getY() + spider.getEyeHeight() - 0.1F,
            spider.getZ() + (spider.getBbWidth() + 1.0F) * 0.5 * Mth.cos(spider.yBodyRot * (float) (Math.PI / 180.0))
         );
      }
   }

   @OnlyIn(Dist.CLIENT)
   public VenomSpitEntity(Level worldIn, double x, double y, double z, double xDir, double yDir, double zDir) {
      this(SkiesEntityTypes.VENOM_SPIT, worldIn);
      this.setPos(x, y, z);

      for (int i = 0; i < 7; i++) {
         double d0 = 0.4 + 0.1 * i;
         worldIn.addParticle(ParticleTypes.SPIT, x, y, z, xDir * d0, yDir, zDir * d0);
      }

      this.setDeltaMovement(xDir, yDir, zDir);
   }

   public void tick() {
      super.tick();
      Vec3 motion = this.getDeltaMovement();
      HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, x$0 -> this.canHitEntity(x$0));
      if (hitResult.getType() != Type.MISS && !EventHooks.onProjectileImpact(this, hitResult)) {
         this.onHit(hitResult);
      }

      double newX = this.getX() + motion.x;
      double newY = this.getY() + motion.y;
      double newZ = this.getZ() + motion.z;
      this.updateRotation();
      if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockStateBase::isAir)) {
         this.discard();
      } else if (this.isInWaterOrBubble()) {
         this.discard();
      } else {
         this.setDeltaMovement(motion.scale(0.99F));
         if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.06F, 0.0));
         }

         this.setPos(newX, newY, newZ);
      }

      if (this.level().isClientSide()) {
         this.spawnParticles();
      }
   }

   @OnlyIn(Dist.CLIENT)
   private void spawnParticles() {
      for (int i = 0; i < 6; i++) {
         float dx = (this.random.nextFloat() - 0.5F) / 7.0F;
         float dy = this.random.nextFloat() / 10.0F;
         float dz = (this.random.nextFloat() - 0.5F) / 7.0F;
         this.level()
            .addParticle(
               new PoisonData(false), this.blockPosition().getX() + 0.5, this.blockPosition().getY() + 0.5F, this.blockPosition().getZ() + 0.5, dx, dy, dz
            );
      }
   }

   public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
      Vec3 vec3d = new Vec3(x, y, z)
         .normalize()
         .add(
            this.random.nextGaussian() * 0.0075F * inaccuracy,
            this.random.nextGaussian() * 0.0075F * inaccuracy,
            this.random.nextGaussian() * 0.0075F * inaccuracy
         )
         .scale(velocity);
      this.setDeltaMovement(vec3d);
      double f = vec3d.horizontalDistance();
      this.setYRot((float)(Mth.atan2(vec3d.x, z) * 180.0F / (float)Math.PI));
      this.setYRot((float)(Mth.atan2(vec3d.y, f) * 180.0F / (float)Math.PI));
      this.yRotO = this.getYRot();
      this.xRotO = this.getXRot();
   }

   protected void onHitEntity(EntityHitResult result) {
      Entity owner = this.getOwner();
      Entity target = result.getEntity();
      if (owner != null && target != null && !owner.isAlliedTo(target)) {
         if (this.isDeadly()) {
            boolean attack = target.hurt(SkiesDamageSources.get(target.level()).venomSpit(this, owner), 3.0F);
            if (target instanceof LivingEntity living && attack) {
               int durration = 5 * Math.max(3, this.level().getDifficulty().getId() + 1);
               int amplifier = this.level().getDifficulty().getId() / 2;
               living.addEffect(new MobEffectInstance(SkiesEffects.DEADLY_VENOM, durration, amplifier));
            }

            if (target instanceof Player playerentity) {
               ItemStack playerItem = playerentity.isUsingItem() ? playerentity.getUseItem() : ItemStack.EMPTY;
               if (!playerItem.isEmpty() && playerItem.canPerformAction(ItemAbilities.SHIELD_BLOCK)) {
                  playerentity.disableShield(true);
                  playerentity.getCooldowns().addCooldown(playerItem.getItem(), 60);
                  this.level().broadcastEntityEvent(playerentity, (byte)30);
               }
            }
         } else {
            boolean attack = target.hurt(SkiesDamageSources.get(target.level()).venomSpit(this, owner), 1.0F);
            if (target instanceof LivingEntity living && attack) {
               int durration = 20 * (this.level().getDifficulty().getId() + 3);
               int amplifier = this.level().getDifficulty().getId() / 2;
               living.addEffect(new MobEffectInstance(MobEffects.POISON, durration, amplifier));
            }
         }

         if (!this.level().isClientSide()) {
            this.discard();
         }
      }
   }

   protected void onHitBlock(BlockHitResult pResult) {
      super.onHitBlock(pResult);
      if (!this.level().isClientSide()) {
         this.discard();
      }
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      builder.define(DEADLY, false);
   }

   protected void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setDeadly(compound.getBoolean("Deadly"));
   }

   protected void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("Deadly", this.isDeadly());
   }

   public boolean isDeadly() {
      return (Boolean)this.entityData.get(DEADLY);
   }

   public void setDeadly(boolean deadly) {
      this.entityData.set(DEADLY, deadly);
   }

   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
      super.recreateFromPacket(packet);
      this.setDeltaMovement(packet.getXa(), packet.getYa(), packet.getZa());
   }
}

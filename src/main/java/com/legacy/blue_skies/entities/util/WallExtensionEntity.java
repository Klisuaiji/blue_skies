package com.legacy.blue_skies.entities.util;

import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntWallEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.entity.PartEntity;

public class WallExtensionEntity extends PartEntity<EntWallEntity> {
   public final EntWallEntity wall;
   public final String name;
   private final EntityDimensions size;

   public WallExtensionEntity(EntWallEntity wall, String name, float boxWidth, float boxHeight) {
      super(wall);
      this.size = EntityDimensions.scalable(boxWidth, boxHeight);
      this.refreshDimensions();
      this.wall = wall;
      this.name = name;
   }

   protected void defineSynchedData() {
   }

   protected void readAdditionalSaveData(CompoundTag tag) {
   }

   protected void addAdditionalSaveData(CompoundTag tag) {
   }

   public boolean isPickable() {
      return true;
   }

   public boolean hurt(DamageSource source, float amount) {
      return this.isInvulnerableTo(source) ? false : this.wall.hurt(source, amount);
   }

   public boolean is(Entity entity) {
      return this == entity || this.wall == entity;
   }

   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      throw new UnsupportedOperationException();
   }

   public EntityDimensions getDimensions(Pose pose) {
      return this.size;
   }

   public boolean shouldBeSaved() {
      return false;
   }

   public boolean canBeCollidedWith() {
      return true;
   }
}

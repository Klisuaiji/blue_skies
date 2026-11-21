package com.legacy.blue_skies.entities;

import com.legacy.blue_skies.registries.SkiesEntityTypes;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BossItemEntity extends ItemEntity {
   protected static final EntityDataAccessor<Optional<UUID>> VISIBLE_PLAYER_UNIQUE_ID = SynchedEntityData.defineId(
      BossItemEntity.class, EntityDataSerializers.OPTIONAL_UUID
   );

   public BossItemEntity(EntityType<? extends ItemEntity> type, Level world) {
      super(type, world);
   }

   public BossItemEntity(Level worldIn, double x, double y, double z) {
      this(SkiesEntityTypes.BOSS_ITEM, worldIn);
      this.setPos(x, y, z);
      this.setYRot(this.random.nextFloat() * 360.0F);
      this.setDeltaMovement(this.random.nextDouble() * 0.2 - 0.1, 0.2, this.random.nextDouble() * 0.2 - 0.1);
      this.setInvulnerable(true);
   }

   public BossItemEntity(Level worldIn, double x, double y, double z, ItemStack stack) {
      this(worldIn, x, y, z);
      this.setItem(stack);
      this.lifespan = stack.getItem() == null ? 6000 : stack.getEntityLifespan(worldIn);
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(VISIBLE_PLAYER_UNIQUE_ID, Optional.empty());
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      if (this.getVisiblePlayerId() != null) {
         compound.putUUID("VisiblePlayerID", this.getVisiblePlayerId());
      }
   }

   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      UUID uuid;
      if (compound.hasUUID("VisiblePlayerID")) {
         uuid = compound.getUUID("VisiblePlayerID");
      } else {
         String s = compound.getString("VisiblePlayerID");
         uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
      }

      if (uuid != null) {
         this.setVisiblePlayerId(uuid);
      }
   }

   public UUID getVisiblePlayerId() {
      return (UUID)((Optional)this.entityData.get(VISIBLE_PLAYER_UNIQUE_ID)).orElse((UUID)null);
   }

   public void setVisiblePlayerId(UUID ownerId) {
      this.entityData.set(VISIBLE_PLAYER_UNIQUE_ID, Optional.ofNullable(ownerId));
   }

   public void playerTouch(Player entityIn) {
      if (this.getVisiblePlayerId() == null || this.getVisiblePlayerId().equals(entityIn.getUUID())) {
         super.playerTouch(entityIn);
      }
   }
}

package com.legacy.blue_skies.util;

import com.google.common.collect.Lists;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.data.objects.tags.SkiesStructureTags;
import com.legacy.blue_skies.entities.util.MovingEntitySound;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.s_to_c.DisplayToastPacket;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Pointer;
import com.legacy.structure_gel.api.structure.StructureAccessHelper;
import com.legacy.structure_gel.api.util.Positions;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiManager.Occupancy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.FireworkRocketItem.Shape;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class EntityUtil {
   public static final Predicate<Entity> CAN_ATTACK = EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE);

   public static float getDistanceToPos(Vec3i startingPos, Vec3i farPos) {
      return (float)GeometryHelper.calcDistance(Vec3.atCenterOf(startingPos), Vec3.atCenterOf(farPos));
   }

   public static BlockPos getPosWithHeightmap(BlockPos pos, Types heightType, Level world) {
      return Positions.blockPos(pos.getX(), world.getHeight(heightType, pos.getX(), pos.getZ()), pos.getZ());
   }

   public static boolean isPosInStructure(Level world, BlockPos pos, Pointer<Structure> structure) {
      return world instanceof ServerLevel sl && StructureAccessHelper.isInStructurePiece(sl, (Structure)structure.get(sl), pos);
   }

   public static boolean canSpawnWithoutFlooding(LivingEntity entityIn) {
      List<? extends LivingEntity> list = entityIn.level()
         .getEntitiesOfClass(entityIn.getClass(), entityIn.getBoundingBox().inflate(10.0), EntitySelector.LIVING_ENTITY_STILL_ALIVE);
      return list.size() < 2;
   }

   public static boolean isInDungeon(Level world, BlockPos pos) {
      return world instanceof ServerLevel serverlevel && serverlevel.structureManager().getStructureWithPieceAt(pos, SkiesStructureTags.DUNGEONS).isValid();
   }

   @OnlyIn(Dist.CLIENT)
   public static void playMovingSound(SoundEvent soundIn, LivingEntity entityIn, float volumeIn, float pitchIn) {
      SoundManager soundHandler = Minecraft.getInstance().getSoundManager();
      soundHandler.play(new MovingEntitySound(entityIn, soundIn, volumeIn, pitchIn, false));
   }

   public static boolean hasPlayerProgressedTo(Player player, boolean bright, int progressionId) {
      return SkiesPlayer.getIfPresent(
         player, skyPlayer -> bright ? skyPlayer.getBrightProgression() >= progressionId : skyPlayer.getDawnProgression() >= progressionId, () -> false
      );
   }

   public static boolean hasPlayerCompletedProgression(Player player) {
      return SkiesPlayer.getIfPresent(player, EntityUtil::hasPlayerCompletedProgression, () -> false);
   }

   public static boolean hasPlayerCompletedProgression(SkiesPlayer skyPlayer) {
      int maxProgression = 3;
      return skyPlayer.getBrightProgression() >= maxProgression && skyPlayer.getDawnProgression() >= maxProgression || skyPlayer.getPlayer().isCreative();
   }

   public static boolean isVillagerWorkTime(Villager villager) {
      return villager.getBrain() != null && villager.getBrain().getSchedule().getActivityAt((int)(villager.level().getDayTime() % 24000L)) == Activity.WORK;
   }

   public static float getSoundPitchWithStart(RandomSource rand, float basePitch) {
      return getSoundPitchWithStart(rand, basePitch, 0.2F);
   }

   public static float getSoundPitchWithStart(RandomSource rand, float basePitch, float range) {
      return (rand.nextFloat() - rand.nextFloat()) * range + basePitch;
   }

   public static ItemStack makeFirework(DyeColor color, int flightTime, DyeColor fadeColor) {
      ItemStack itemstack = new ItemStack(Items.FIREWORK_ROCKET, 1);
      ItemStack itemstack1 = new ItemStack(Items.FIREWORK_STAR);
      CompoundTag compoundnbt = itemstack1.getOrCreateTagElement("Explosion");
      List<Integer> list = Lists.newArrayList();
      list.add(color.getFireworkColor());
      compoundnbt.putIntArray("Colors", list);
      List<Integer> fadelist = Lists.newArrayList();
      fadelist.add(fadeColor.getFireworkColor());
      compoundnbt.putIntArray("FadeColors", fadelist);
      compoundnbt.putByte("Type", (byte)Shape.LARGE_BALL.getId());
      CompoundTag compoundnbt1 = itemstack.getOrCreateTagElement("Fireworks");
      ListTag listnbt = new ListTag();
      compoundnbt.putBoolean("Flicker", true);
      CompoundTag compoundnbt2 = itemstack1.getTagElement("Explosion");
      if (compoundnbt2 != null) {
         listnbt.add(compoundnbt2);
      }

      compoundnbt1.putByte("Flight", (byte)flightTime);
      if (!listnbt.isEmpty()) {
         compoundnbt1.put("Explosions", listnbt);
      }

      return itemstack;
   }

   public static boolean hasAdvancement(ServerPlayer player, ResourceLocation advancement) {
      return player != null && player.getServer() != null && player.getServer().getAdvancements().get(advancement) != null
         ? player.getAdvancements().getOrStartProgress(player.getServer().getAdvancements().get(advancement)).isDone()
         : false;
   }

   public static void sendJournalToast(Player player) {
      if (player instanceof ServerPlayer) {
         PacketHandler.sendToClient(new DisplayToastPacket((byte)0), (ServerPlayer)player);
      }
   }

   public static boolean canEntitySeePos(LivingEntity entity, BlockPos posToLookAt) {
      if (entity != null) {
         Vec3 vec3d = entity.getViewVector(1.0F).normalize();
         Vec3 vec3d1 = new Vec3(
            posToLookAt.getX() - entity.getX(), posToLookAt.getY() - (entity.getY() + entity.getEyeHeight()), posToLookAt.getZ() - entity.getZ()
         );
         double d0 = vec3d1.length();
         vec3d1 = vec3d1.normalize();
         double d1 = vec3d.dot(vec3d1);
         return !(d1 < 1.0 / d0);
      } else {
         return false;
      }
   }

   public static boolean canSeeSkyFromBelowWater(Level level, BlockPos pPos) {
      return canSeeSkyFromBelowWater(level, level.dimension(), pPos);
   }

   public static boolean canSeeSkyFromBelowWater(LevelReader level, ResourceKey<Level> key, BlockPos pPos) {
      if (key != null && !SkiesDimensions.inSkyDimension(key)) {
         return level.canSeeSkyFromBelowWater(pPos);
      }

      int seaLevel = level.getSeaLevel() + 1;
      if (pPos.getY() >= seaLevel) {
         return level.canSeeSky(pPos);
      }

      BlockPos blockpos = Positions.blockPos(pPos.getX(), seaLevel + 1, pPos.getZ());
      if (!level.canSeeSky(blockpos)) {
         return false;
      }

      for (BlockPos blockpos1 = blockpos.below(); blockpos1.getY() > pPos.getY(); blockpos1 = blockpos1.below()) {
         BlockState blockstate = level.getBlockState(blockpos1);
         if (blockstate.getLightBlock(level, blockpos1) > 0 && !blockstate.liquid()) {
            return false;
         }
      }

      return true;
   }

   public static BlockPos getPosInFront(LivingEntity entity, double distance) {
      return Positions.blockPos(
         entity.getX() - (entity.getBbWidth() + 1.0F) * distance * Mth.sin(entity.yBodyRot * (float) (Math.PI / 180.0)),
         entity.getEyeY() - 0.1F,
         entity.getZ() + (entity.getBbWidth() + 1.0F) * distance * Mth.cos(entity.yBodyRot * (float) (Math.PI / 180.0))
      );
   }

   public static List<BlockPos> createSquareOffsetsFromPos(BlockPos pos, double doubleOffset) {
      List<BlockPos> positions = Lists.newArrayList();
      int offset = (int)doubleOffset;
      positions.add(pos.offset(offset, 0, offset));
      positions.add(pos.offset(offset, 0, -offset));
      positions.add(pos.offset(-offset, 0, -offset));
      positions.add(pos.offset(-offset, 0, offset));
      return positions;
   }

   public static List<BlockPos> createDiamondOffsetsFromPos(BlockPos pos, int offset) {
      List<BlockPos> positions = Lists.newArrayList();
      positions.add(pos.relative(Direction.NORTH, offset));
      positions.add(pos.relative(Direction.EAST, offset));
      positions.add(pos.relative(Direction.SOUTH, offset));
      positions.add(pos.relative(Direction.WEST, offset));
      return positions;
   }

   public static boolean hasNoCollisionsInPath(LivingEntity entity, BlockPos pos) {
      Vec3 movementPos = new Vec3(pos.getX() - entity.getX(), pos.getY() - entity.getY(), pos.getZ() - entity.getZ());
      double vecLength = movementPos.length();
      movementPos = movementPos.normalize();
      AABB axisalignedbb = entity.getBoundingBox();

      for (int i = 1; i < Mth.ceil(vecLength); i++) {
         axisalignedbb = axisalignedbb.expandTowards(movementPos);
         if (!entity.level().noCollision(entity, axisalignedbb)) {
            return false;
         }
      }

      return true;
   }

   public static BlockPos getClosestPosInList(BlockPos startingPos, List<BlockPos> list) {
      BlockPos closest = null;

      for (BlockPos outsidePos : list) {
         if (closest == null) {
            closest = outsidePos.immutable();
         } else {
            double newDist = getDistanceToPos(outsidePos, startingPos);
            double oldDist = getDistanceToPos(closest, startingPos);
            if (newDist < oldDist) {
               closest = outsidePos.immutable();
            }
         }
      }

      return closest;
   }

   public static int getIndexOfClosestPos(BlockPos startingPos, List<BlockPos> list) {
      BlockPos closest = null;
      int closestIndex = 0;

      for (int i = 0; i < list.size(); i++) {
         BlockPos outsidePos = list.get(i);
         if (closest == null) {
            closest = outsidePos.immutable();
         } else {
            double newDist = getDistanceToPos(outsidePos, startingPos);
            double oldDist = getDistanceToPos(closest, startingPos);
            if (newDist < oldDist) {
               closestIndex = i;
               closest = outsidePos.immutable();
            }
         }
      }

      return closestIndex;
   }

   public static void attachToEntity(float segmentSeparation, LivingEntity follower, LivingEntity parent) {
      if (follower instanceof Mob mobFollower) {
         mobFollower.setYRot(mobFollower.yHeadRot);
         mobFollower.getLookControl().setLookAt(parent, 180.0F, 180.0F);
      }

      Vec3 difference = new Vec3(follower.getX() - parent.getX(), follower.getY() - parent.getY(), follower.getZ() - parent.getZ()).normalize();
      follower.setPos(
         segmentSeparation * difference.x() + parent.getX(),
         segmentSeparation * difference.y() + parent.getY(),
         segmentSeparation * difference.z() + parent.getZ()
      );
   }

   public static Stream<PoiRecord> getPoisInCircle(Entity entity, ResourceKey<PoiType> type, int radius) {
      if (entity.level() instanceof ServerLevel level) {
         BlockPos var7 = entity.blockPosition();
         PoiManager pointofinterestmanager = level.getPoiManager();
         pointofinterestmanager.ensureLoadedAndValid(entity.level(), var7, radius);
         return level.getPoiManager().getInRange(poiType -> poiType.is(type), var7, radius, Occupancy.ANY);
      } else {
         return Stream.empty();
      }
   }
}

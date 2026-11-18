package com.legacy.blue_skies.entities.ai;

import com.google.common.collect.Lists;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.registries.SkiesVillagers;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.blue_skies.util.GeometryHelper;
import com.legacy.structure_gel.api.util.Positions;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class NightwatcherRobGoal extends Goal {
   private final Villager villager;
   private final double speed;
   private Villager targetVillager;
   private NightwatcherRobGoal.Task task = NightwatcherRobGoal.Task.ROBBING;
   private Vec3 awayVec = null;
   private List<UUID> robbedVilagers = Lists.newArrayList();
   private int robbingDelay = 0;
   private BlockPos prevPos;

   public NightwatcherRobGoal(Villager creatureIn, double speedIn) {
      this.speed = speedIn;
      this.villager = creatureIn;
      this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
   }

   public void start() {
      AABB radiusCheck = new AABB(this.villager.blockPosition()).inflate(35.0, 5.0, 35.0);
      Stream<Villager> villagers = this.villager
         .level()
         .getEntities(
            this.villager, radiusCheck, entity -> entity instanceof Villager villager && villager.isSleeping() && villager.distanceTo(this.villager) <= 33.0F
         )
         .stream()
         .map(e -> (Villager)e)
         .sorted((e1, e2) -> Double.compare(e1.distanceTo(this.villager), e2.distanceTo(this.villager)));
      if (this.robbingDelay <= 0) {
         Optional<Villager> target = villagers.filter(e -> !this.robbedVilagers.contains(e.getUUID())).findFirst();
         if (target.isPresent()) {
            this.targetVillager = target.get();
         }
      }
   }

   public void tick() {
      if (!EntityUtil.isVillagerWorkTime(this.villager)) {
         this.robbedVilagers.clear();
      }

      Optional<GlobalPos> jobSite = this.villager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
      if (this.targetVillager != null) {
         if (!this.targetVillager.isAlive()) {
            this.stop();
            return;
         }

         if (this.targetVillager.isSleeping()
            && (
               !jobSite.isPresent()
                  || jobSite.isPresent() && GeometryHelper.calcDistance(Vec3.atCenterOf(jobSite.get().pos()), this.villager.position()) <= 38.0
            )) {
            if (this.task == NightwatcherRobGoal.Task.ROBBING) {
               for (BlockPos pos : BlockPos.betweenClosedStream(this.villager.getBoundingBox().inflate(0.1F)).map(BlockPos::immutable).toList()) {
                  BlockState state = this.villager.level().getBlockState(pos);
                  if (state.getBlock() instanceof DoorBlock door && state.is(BlockTags.WOODEN_DOORS)) {
                     door.setOpen(this.villager, this.villager.level(), state, pos, this.task == NightwatcherRobGoal.Task.ROBBING);
                  }
               }
            } else if (this.prevPos != this.villager.blockPosition()) {
               BlockState state;
               if (this.prevPos != null
                  && (state = this.villager.level().getBlockState(this.prevPos)).getBlock() instanceof DoorBlock door
                  && state.is(BlockTags.WOODEN_DOORS)) {
                  door.setOpen(this.villager, this.villager.level(), state, this.prevPos, this.task == NightwatcherRobGoal.Task.ROBBING);
               }

               this.prevPos = this.villager.blockPosition();
            }

            if (this.task == NightwatcherRobGoal.Task.ROBBING) {
               double distance = this.targetVillager.distanceTo(this.villager);
               if (distance < 3.0) {
                  this.villager
                     .getMoveControl()
                     .setWantedPosition(this.targetVillager.getX(), this.targetVillager.getY(), this.targetVillager.getZ(), this.speed);
               }

               this.villager.getNavigation().moveTo(this.targetVillager, this.speed);
               this.villager.getLookControl().setLookAt(this.targetVillager, 40.0F, 40.0F);
               if (distance < 1.0) {
                  this.robbedVilagers.add(this.targetVillager.getUUID());
                  this.villager.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
                  this.task = NightwatcherRobGoal.Task.RUNNING;
               }
            } else if (this.task == NightwatcherRobGoal.Task.RUNNING) {
               if (this.awayVec == null) {
                  if (jobSite.isPresent()) {
                     this.awayVec = Vec3.atCenterOf(jobSite.get().pos());
                  } else {
                     this.awayVec = LandRandomPos.getPosAway(this.villager, 20, 7, this.targetVillager.position());
                  }
               } else if (EntityUtil.getDistanceToPos(this.villager.blockPosition(), Positions.blockPos(this.awayVec)) > 1.3F) {
                  this.villager.getNavigation().moveTo(this.awayVec.x, this.awayVec.y, this.awayVec.z, 0.7F);
               } else {
                  this.robbingDelay = 40;
                  this.villager.playSound(SkiesSounds.ENTITY_VILLAGER_WORK_NIGHTWATCHER, 1.0F, 1.0F);
                  this.stop();
               }
            }
         } else {
            this.stop();
         }
      }
   }

   public void stop() {
      if (!EntityUtil.isVillagerWorkTime(this.villager)) {
         this.robbedVilagers.clear();
      }

      this.targetVillager = null;
      this.awayVec = null;
      this.task = NightwatcherRobGoal.Task.ROBBING;
   }

   public boolean canUse() {
      return this.villager.getVillagerData().getProfession() != SkiesVillagers.NIGHTWATCHER.get()
         ? false
         : EntityUtil.isVillagerWorkTime(this.villager) && this.robbingDelay-- <= 0;
   }

   public boolean canContinueToUse() {
      return this.targetVillager != null
         && this.task.ordinal() < 2
         && EntityUtil.isVillagerWorkTime(this.villager)
         && this.villager.getVillagerData().getProfession() == SkiesVillagers.NIGHTWATCHER.get()
         && super.canContinueToUse();
   }

   public boolean requiresUpdateEveryTick() {
      return true;
   }

   private static enum Task {
      ROBBING,
      RUNNING;
   }
}

package com.legacy.blue_skies.entities.ai;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesVillagers;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.blue_skies.util.GeometryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public class ShovelSnowGoal extends MoveToBlockGoal {
   private final Villager villager;
   private int currentTask;

   public ShovelSnowGoal(Villager villagerIn, double speedIn) {
      super(villagerIn, speedIn, 20);
      this.villager = villagerIn;
   }

   public boolean canUse() {
      if (this.villager.getVillagerData().getProfession() == SkiesVillagers.SHOVELER.get() && !this.villager.level().isRaining()) {
         if (this.nextStartTick <= 0) {
            if (!EventHooks.getMobGriefingEvent(this.villager.level(), this.villager)) {
               return false;
            }

            this.currentTask = -1;
         }

         return EntityUtil.isVillagerWorkTime(this.villager) && super.canUse();
      } else {
         return false;
      }
   }

   public boolean canContinueToUse() {
      return !this.villager.level().isRaining()
         && EntityUtil.isVillagerWorkTime(this.villager)
         && this.villager.getVillagerData().getProfession() == SkiesVillagers.SHOVELER.get()
         && super.canContinueToUse();
   }

   public void tick() {
      super.tick();
      this.villager
         .getLookControl()
         .setLookAt(this.blockPos.getX() + 0.5, this.blockPos.getY() + 1, this.blockPos.getZ() + 0.5, 10.0F, this.villager.getMaxHeadXRot());
      this.moveMobToBlock();
      if (GeometryHelper.calcDistance(this.villager.position(), Vec3.atBottomCenterOf(this.getMoveToTarget())) <= 1.0
         || this.getMoveToTarget().closerToCenterThan(this.villager.position(), this.acceptedDistance())) {
         Level world = this.villager.level();
         BlockPos blockpos = this.getMoveToTarget();
         if (world.getBlockState(blockpos).getBlock() instanceof SnowLayerBlock) {
            world.destroyBlock(blockpos, false);
         }

         this.currentTask = -1;
         this.nextStartTick = 20;
      }
   }

   public void start() {
      this.villager.getNavigation().stop();
      super.start();
      this.villager
         .getLookControl()
         .setLookAt(
            this.getMoveToTarget().getX() + 0.5, this.getMoveToTarget().getY(), this.getMoveToTarget().getZ() + 0.5, 10.0F, this.villager.getMaxHeadXRot()
         );
   }

   public void stop() {
      super.stop();
   }

   protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
      Block block = worldIn.getBlockState(pos).getBlock();
      if (block == SkiesBlocks.coarse_turquoise_dirt) {
         pos = pos.above();
         BlockState iblockstate = worldIn.getBlockState(pos);
         block = iblockstate.getBlock();
         if (block instanceof SnowLayerBlock && (this.currentTask == 0 || this.currentTask < 0)) {
            this.currentTask = 0;
            return true;
         }
      }

      return false;
   }
}

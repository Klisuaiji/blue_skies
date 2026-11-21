package com.legacy.blue_skies.entities.villager.tasks;

import com.google.common.collect.ImmutableMap;
import com.legacy.blue_skies.entities.villager.GatekeeperEntity;
import com.legacy.structure_gel.api.util.Positions;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.phys.Vec3;

public class GatekeeperWalkToVillageTask extends Behavior<GatekeeperEntity> {
   private final float speedModifier;
   private final int closeEnoughDistance;

   public GatekeeperWalkToVillageTask(float p_i51557_1_, int p_i51557_2_) {
      super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
      this.speedModifier = p_i51557_1_;
      this.closeEnoughDistance = p_i51557_2_;
   }

   protected boolean checkExtraStartConditions(ServerLevel worldIn, GatekeeperEntity owner) {
      return !worldIn.isVillage(owner.blockPosition());
   }

   protected void start(ServerLevel worldIn, GatekeeperEntity entityIn, long gameTimeIn) {
      PoiManager pointofinterestmanager = worldIn.getPoiManager();
      int i = pointofinterestmanager.sectionsToVillage(SectionPos.of(entityIn.blockPosition()));
      Vec3 vec3d = null;

      for (int j = 0; j < 5; j++) {
         Vec3 vec3d1 = LandRandomPos.getPos(entityIn, 15, 7, p_225444_1_ -> -worldIn.sectionsToVillage(SectionPos.of(p_225444_1_)));
         if (vec3d1 != null) {
            int k = pointofinterestmanager.sectionsToVillage(SectionPos.of(Positions.blockPos(vec3d1)));
            if (k < i) {
               vec3d = vec3d1;
               break;
            }

            if (k == i) {
               vec3d = vec3d1;
            }
         }
      }

      if (vec3d != null) {
         entityIn.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3d, this.speedModifier, this.closeEnoughDistance));
      }
   }
}

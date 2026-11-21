package com.legacy.blue_skies.entities.villager.tasks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.legacy.blue_skies.entities.villager.GatekeeperEntity;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.mojang.datafixers.util.Pair;
import java.util.Optional;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.behavior.AcquirePoi;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.InteractWith;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.entity.ai.behavior.JumpOnBed;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.PlayTagWithOtherKids;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetClosestHomeAsWalkTarget;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.behavior.SleepInBed;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.behavior.UpdateActivityFromSchedule;
import net.minecraft.world.entity.ai.behavior.ValidateNearbyPoi;
import net.minecraft.world.entity.ai.behavior.VillageBoundRandomStroll;
import net.minecraft.world.entity.ai.behavior.WakeUp;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

public class GatekeeperTasks {
   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super GatekeeperEntity>>> core(float speed) {
      return ImmutableList.of(
         Pair.of(0, new Swim(0.8F)),
         Pair.of(0, InteractWithDoor.create()),
         Pair.of(0, new LookAtTargetSink(45, 90)),
         Pair.of(0, new GatekeeperPanicTask()),
         Pair.of(0, WakeUp.create()),
         Pair.of(1, new MoveToTargetSink(180, 220)),
         Pair.of(2, new GatekeeperTradeTask(speed)),
         Pair.of(10, AcquirePoi.create(p -> p.is(PoiTypes.HOME), MemoryModuleType.HOME, false, Optional.empty())),
         Pair.of(10, AcquirePoi.create(p -> p.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT, true, Optional.empty()))
      );
   }

   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super GatekeeperEntity>>> work(float speed) {
      return ImmutableList.of(bipedLookTasks(), Pair.of(10, SetLookAndInteract.create(EntityType.PLAYER, 4)), Pair.of(99, UpdateActivityFromSchedule.create()));
   }

   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super GatekeeperEntity>>> play(float speed) {
      return ImmutableList.of(
         Pair.of(0, new MoveToTargetSink(80, 120)),
         lookTasks(),
         Pair.of(5, PlayTagWithOtherKids.create()),
         Pair.of(
            5,
            new RunOne(
               ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_ABSENT),
               ImmutableList.of(
                  Pair.of(InteractWith.of(SkiesEntityTypes.GATEKEEPER, 8, MemoryModuleType.INTERACTION_TARGET, speed, 2), 2),
                  Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, speed, 2), 1),
                  Pair.of(VillageBoundRandomStroll.create(speed), 1),
                  Pair.of(SetWalkTargetFromLookTarget.create(speed, 2), 1),
                  Pair.of(new JumpOnBed(speed), 2),
                  Pair.of(new DoNothing(20, 40), 2)
               )
            )
         ),
         Pair.of(99, UpdateActivityFromSchedule.create())
      );
   }

   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super GatekeeperEntity>>> rest(float speed) {
      return ImmutableList.of(
         Pair.of(2, new GatekeeperStayNearPointTask(MemoryModuleType.HOME, speed, 1, 150, 1200)),
         Pair.of(3, ValidateNearbyPoi.create(p -> p.is(PoiTypes.HOME), MemoryModuleType.HOME)),
         Pair.of(3, new SleepInBed()),
         Pair.of(
            5,
            new RunOne(
               ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT),
               ImmutableList.of(
                  Pair.of(SetClosestHomeAsWalkTarget.create(speed), 1),
                  Pair.of(RandomStroll.stroll(speed), 4),
                  Pair.of(new GatekeeperWalkToVillageTask(speed, 4), 2),
                  Pair.of(new DoNothing(20, 40), 2)
               )
            )
         ),
         bipedLookTasks(),
         Pair.of(99, UpdateActivityFromSchedule.create())
      );
   }

   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super GatekeeperEntity>>> idle(float speed) {
      return ImmutableList.of(
         Pair.of(
            2,
            new RunOne(
               ImmutableList.of(
                  Pair.of(InteractWith.of(SkiesEntityTypes.GATEKEEPER, 8, MemoryModuleType.INTERACTION_TARGET, speed, 2), 2),
                  Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, speed, 2), 1),
                  Pair.of(VillageBoundRandomStroll.create(speed), 1),
                  Pair.of(SetWalkTargetFromLookTarget.create(speed, 2), 1),
                  Pair.of(new JumpOnBed(speed), 1),
                  Pair.of(new DoNothing(30, 60), 1)
               )
            )
         ),
         Pair.of(3, SetLookAndInteract.create(EntityType.PLAYER, 4)),
         lookTasks(),
         Pair.of(99, UpdateActivityFromSchedule.create())
      );
   }

   private static Pair<Integer, BehaviorControl<LivingEntity>> lookTasks() {
      return Pair.of(
         5,
         new RunOne(
            ImmutableList.of(
               Pair.of(SetEntityLookTarget.create(EntityType.CAT, 8.0F), 8),
               Pair.of(SetEntityLookTarget.create(SkiesEntityTypes.GATEKEEPER, 8.0F), 2),
               Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 2),
               Pair.of(SetEntityLookTarget.create(MobCategory.CREATURE, 8.0F), 1),
               Pair.of(SetEntityLookTarget.create(MobCategory.WATER_CREATURE, 8.0F), 1),
               Pair.of(SetEntityLookTarget.create(MobCategory.MONSTER, 8.0F), 1),
               Pair.of(new DoNothing(30, 60), 2)
            )
         )
      );
   }

   private static Pair<Integer, BehaviorControl<LivingEntity>> bipedLookTasks() {
      return Pair.of(
         5,
         new RunOne(
            ImmutableList.of(
               Pair.of(SetEntityLookTarget.create(EntityType.VILLAGER, 8.0F), 2),
               Pair.of(SetEntityLookTarget.create(SkiesEntityTypes.GATEKEEPER, 8.0F), 2),
               Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 2),
               Pair.of(new DoNothing(30, 60), 8)
            )
         )
      );
   }
}

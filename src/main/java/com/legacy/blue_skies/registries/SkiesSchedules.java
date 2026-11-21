package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.ScheduleBuilder;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesSchedules {
   public static final Lazy<Schedule> NIGHT_OWL = Lazy.of(
      () -> buildSchedule("night_owl")
         .changeActivityAt(0, Activity.REST)
         .changeActivityAt(13000, Activity.IDLE)
         .changeActivityAt(13000, Activity.WORK)
         .changeActivityAt(20000, Activity.IDLE)
         .build()
   );

   private static ScheduleBuilder buildSchedule(String string) {
      return new ScheduleBuilder(new Schedule());
   }

   public static void init(RegisterEvent event) {
      event.register(Registries.SCHEDULE, BlueSkies.locate("night_owl"), NIGHT_OWL);
   }
}

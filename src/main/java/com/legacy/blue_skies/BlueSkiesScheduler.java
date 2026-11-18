package com.legacy.blue_skies;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

public class BlueSkiesScheduler {
   private ScheduledExecutorService scheduler;

   public void start(IEventBus forgeBus) {
      if (this.scheduler == null) {
         this.scheduler = Executors.newScheduledThreadPool(2);
         this.scheduler.scheduleAtFixedRate(this::updateHoliday, 0L, 30L, TimeUnit.MINUTES);
         this.scheduler.scheduleAtFixedRate(this::updateSupporters, 0L, 1L, TimeUnit.DAYS);
         forgeBus.addListener(this::onServerStopped);
      }
   }

   public void runChecks() {
      this.updateHoliday();
      this.updateSupporters();
   }

   private void updateHoliday() {
      Calendar cal = Calendar.getInstance();
      int month = cal.get(2);
      int date = cal.get(5);
      BlueSkies.isChristmas = month == 11 && date >= 24 && date <= 26;
      BlueSkies.isHalloween = month == 9 && date >= 29 && date <= 31;
   }

   private void updateSupporters() {
      BlueSkies.ML_SUPPORTER.refresh();
   }

   protected void onServerStopped(ServerStoppedEvent event) {
      this.scheduler.shutdownNow();
   }
}

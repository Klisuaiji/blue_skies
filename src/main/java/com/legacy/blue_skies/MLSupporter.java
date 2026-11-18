package com.legacy.blue_skies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MLSupporter {
   private static final String SUPPORTER_URL = "https://moddinglegacy.com/supporters-changelogs/supporters.txt";
   private final Logger logger;
   private final String modID;
   private Map<UUID, MLSupporter.Supporter> supporters = new HashMap<>();

   public MLSupporter(String modID) {
      this.logger = LogManager.getLogger("ModdingLegacy/" + modID + "/Supporter");
      this.modID = modID;
   }

   public Map<UUID, MLSupporter.Supporter> getSupporters() {
      return this.supporters;
   }

   @Nullable
   public MLSupporter.Supporter get(Player player) {
      return this.supporters.get(player.getUUID());
   }

   public MLSupporter.Rank getRank(Player player) {
      MLSupporter.Supporter supporter = this.get(player);
      return supporter != null ? supporter.rank : MLSupporter.Rank.UNKNOWN;
   }

   public void refresh() {
      new MLSupporter.GetSupportersThread(this).start();
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder("Modding Legacy supporter list: ").append(this.modID);

      for (MLSupporter.Supporter supporter : this.supporters.values()) {
         builder.append("\n\t").append(supporter.toString());
      }

      return builder.toString();
   }

   private static class GetSupportersThread extends Thread {
      private final MLSupporter mlSupporter;

      private GetSupportersThread(MLSupporter mlSupporterInstance) {
         super("Modding Legacy/" + mlSupporterInstance.modID + "/Supporters thread");
         this.mlSupporter = mlSupporterInstance;
         this.setDaemon(true);
      }

      @Override
      public void run() {
         try {
            this.mlSupporter
               .logger
               .info("Attempting to load the Modding Legacy supporters list from https://moddinglegacy.com/supporters-changelogs/supporters.txt");
            List<String> lines = new ArrayList<>();
            URL url = new URL("https://moddinglegacy.com/supporters-changelogs/supporters.txt");
            HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
               lines.add(line);
            }

            reader.close();
            this.mlSupporter.supporters = lines.stream().filter(s -> !s.isEmpty() && !s.startsWith("#")).map(l -> {
               try {
                  return MLSupporter.Supporter.fromString(l);
               } catch (Throwable var3x) {
                  this.mlSupporter.logger.error("Failed to load supporter entry for a player!", var3x);
                  return null;
               }
            }).filter(s -> s != null).collect(Collectors.toMap(s -> s.uuid, s -> (MLSupporter.Supporter)s));
            this.mlSupporter.logger.info("Successfully loaded the Modding Legacy supporters list.");
         } catch (IOException var6) {
            this.mlSupporter
               .logger
               .info(
                  "Couldn't load the Modding Legacy supporters list. You may be offline or our website could be having issues. If you are a supporter, some cosmetic features may not work.",
                  var6
               );
         } catch (Exception var7) {
            this.mlSupporter
               .logger
               .info("Failed to load the Modding Legacy supporters list. If you are a supporter, some cosmetic features may not work.", var7);
         }
      }
   }

   public static enum Rank {
      UNKNOWN(-1, false, false),
      DEV(0, true, true),
      SUPPORTER(1, true, true);

      public final int id;
      private final boolean isSupporter;
      private final boolean hasPerks;

      private Rank(int id, boolean isSupporter, boolean hasPerks) {
         this.id = id;
         this.isSupporter = isSupporter;
         this.hasPerks = hasPerks;
      }

      public boolean isDeveloper() {
         return this == DEV;
      }

      public boolean isSupporter() {
         return this.isSupporter;
      }

      public boolean hasPerks() {
         return this.hasPerks;
      }

      public static MLSupporter.Rank fromID(int id) {
         for (MLSupporter.Rank rank : values()) {
            if (rank.id == id) {
               return rank;
            }
         }

         return UNKNOWN;
      }
   }

   public static class Supporter {
      public final String name;
      public final UUID uuid;
      public final MLSupporter.Rank rank;

      public Supporter(String name, UUID uuid, MLSupporter.Rank rank) {
         this.name = name;
         this.uuid = uuid;
         this.rank = rank;
      }

      @Override
      public String toString() {
         return String.format("[name=%s, UUID=%s, rank=%s]", this.name, this.uuid.toString(), this.rank.name());
      }

      public static MLSupporter.Supporter fromString(String string) {
         String[] values = string.split(",");
         return new MLSupporter.Supporter(values[0], UUID.fromString(values[1]), MLSupporter.Rank.fromID(Integer.parseInt(values[2])));
      }
   }
}

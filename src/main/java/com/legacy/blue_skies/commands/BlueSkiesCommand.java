package com.legacy.blue_skies.commands;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.items.LootBagItem;
import com.legacy.blue_skies.items.util.IRarityItem;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.s_to_c.UpdateSupportersPacket;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.NaturalSpawner.SpawnState;

public class BlueSkiesCommand {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("blue_skies");
      command.then(
         ((LiteralArgumentBuilder)Commands.literal("refresh_supporters").requires(BlueSkiesCommand::isOp)).executes(BlueSkiesCommand::refreshSupporters)
      );
      LiteralArgumentBuilder<CommandSourceStack> setProgression = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("set_progression")
         .requires(BlueSkiesCommand::isOp);
      setProgression.then(
         Commands.literal("everbright")
            .then(
               Commands.argument("level", IntegerArgumentType.integer(0, 4))
                  .executes(context -> setProgression(context, IntegerArgumentType.getInteger(context, "level"), true))
            )
      );
      setProgression.then(
         Commands.literal("everdawn")
            .then(
               Commands.argument("level", IntegerArgumentType.integer(0, 4))
                  .executes(context -> setProgression(context, IntegerArgumentType.getInteger(context, "level"), false))
            )
      );
      command.then(setProgression);
      LiteralArgumentBuilder<CommandSourceStack> setWeather = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("weather")
         .requires(BlueSkiesCommand::isOp);
      setWeather.then(
         Commands.literal("clear")
            .executes(context -> dispatcher.execute("execute in minecraft:overworld run weather clear", (CommandSourceStack)context.getSource()))
      );
      setWeather.then(
         Commands.literal("rain")
            .executes(context -> dispatcher.execute("execute in minecraft:overworld run weather rain", (CommandSourceStack)context.getSource()))
      );
      setWeather.then(
         Commands.literal("thunder")
            .executes(context -> dispatcher.execute("execute in minecraft:overworld run weather thunder", (CommandSourceStack)context.getSource()))
      );
      command.then(setWeather);
      LiteralArgumentBuilder<CommandSourceStack> setLoreStatus = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("set_lore_used")
         .requires(BlueSkiesCommand::isOp);
      setLoreStatus.then(
         Commands.argument("used", BoolArgumentType.bool()).executes(context -> setLoreStatus(context, BoolArgumentType.getBool(context, "used")))
      );
      command.then(setLoreStatus);
      LiteralArgumentBuilder<CommandSourceStack> getMobCaps = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("get_mob_caps")
         .requires(BlueSkiesCommand::isOp);
      getMobCaps.executes(context -> getMobCaps(context));
      command.then(getMobCaps);
      LiteralArgumentBuilder<CommandSourceStack> lootBag = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal("loot_bag")
            .requires(BlueSkiesCommand::isOp))
         .then(
            Commands.argument("runs", IntegerArgumentType.integer(0))
               .executes(context -> simulateLootTables(context, IntegerArgumentType.getInteger(context, "runs")))
         );
      command.then(lootBag);
      dispatcher.register(command);
   }

   private static boolean isOp(CommandSourceStack source) {
      return source.hasPermission(2);
   }

   private static int simulateLootTables(CommandContext<CommandSourceStack> context, int runs) {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      ServerLevel level = source.getLevel();
      ServerPlayer player = source.getPlayer();
      if (player == null) {
         source.sendFailure(Component.literal("That command must be ran by a player holding a loot bag."));
         return 0;
      }

      ItemStack heldItem = player.getMainHandItem();
      if (!(heldItem.getItem() instanceof LootBagItem)) {
         source.sendFailure(Component.literal("You need to hold a loot bag."));
         return 0;
      }

      Component rarity = null;
      if (heldItem.getItem() instanceof IRarityItem rarityItem) {
         rarity = Component.translatable(String.format("gui.blue_skies.item.rarity.%s", rarityItem.getRarityTag(heldItem).getId()));
      }

      String rarityText = rarity == null ? "" : rarity.getString() + " ";
      source.sendSuccess(() -> Component.literal("Rolling " + runs + " times with " + rarityText + heldItem.getHoverName().getString()), true);
      Set<Item> obtainedItems = new HashSet<>();
      List<Set<Item>> rolledItems = new ArrayList<>();

      for (int i = 0; i < runs; i++) {
         Set<Item> items = new HashSet<>();

         for (ItemStack stack : LootBagItem.getRandomItems(level, player, LootBagItem.getLootTable(heldItem))) {
            if (!stack.isEmpty()) {
               Item item = stack.getItem();
               obtainedItems.add(item);
               items.add(item);
            }
         }

         rolledItems.add(items);
      }

      source.sendSuccess(
         () -> Component.literal("Item obtain chances for " + rarityText + heldItem.getHoverName().getString() + " with " + runs + " rolls:"), true
      );
      if (obtainedItems.isEmpty()) {
         source.sendSuccess(() -> Component.literal("  No items obtained").withStyle(ChatFormatting.GRAY), false);
      } else {
         Map<Item, Integer> itemRates = new HashMap<>();

         for (Item item : obtainedItems) {
            int obtainedTotal = 0;

            for (Set<Item> rolled : rolledItems) {
               if (rolled.contains(item)) {
                  obtainedTotal++;
               }
            }

            itemRates.put(item, obtainedTotal);
         }

         DecimalFormat format = new DecimalFormat("0.##");
         itemRates.entrySet()
            .stream()
            .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
            .forEach(
               e -> source.sendSuccess(
                  () -> Component.literal(
                        " "
                           + format.format((float)((Integer)e.getValue()).intValue() / runs * 100.0F)
                           + "% "
                           + ((Item)e.getKey()).getDefaultInstance().getDisplayName().getString()
                     )
                     .withStyle(ChatFormatting.GRAY),
                  true
               )
            );
      }

      return 1;
   }

   private static int refreshSupporters(CommandContext<CommandSourceStack> context) {
      try {
         ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Updating supporters list. This may take a moment to complete."), true);
         BlueSkies.ML_SUPPORTER.refresh();

         for (ServerLevel world : ((CommandSourceStack)context.getSource()).getServer().getAllLevels()) {
            PacketHandler.sendToClients(new UpdateSupportersPacket(), world);
         }

         return 1;
      } catch (Throwable e) {
         ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Failed to update the supporters list."));
         BlueSkies.LOGGER.error(e);
         return 0;
      }
   }

   private static int setProgression(CommandContext<CommandSourceStack> context, int level, boolean isBright) {
      try {
         return SkiesPlayer.getIfPresent(
            ((CommandSourceStack)context.getSource()).getPlayerOrException(),
            player -> {
               if (((CommandSourceStack)context.getSource()).getPlayer() != null) {
                  if (isBright) {
                     player.setBrightProgression((byte)level);
                  } else {
                     player.setDawnProgression((byte)level);
                  }

                  player.syncDataToClient();
                  ((CommandSourceStack)context.getSource())
                     .sendSuccess(
                        () -> Component.literal("Updated progression for " + ((CommandSourceStack)context.getSource()).getPlayer().getName().getString()), true
                     );
                  return 1;
               } else {
                  ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("That command needs to be run by a player."), true);
                  return 0;
               }
            },
            () -> 0
         );
      } catch (CommandSyntaxException e) {
         ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("That command needs to be run by a player."), true);
         return 0;
      }
   }

   private static int setLoreStatus(CommandContext<CommandSourceStack> context, boolean usedLore) {
      try {
         return SkiesPlayer.getIfPresent(
            ((CommandSourceStack)context.getSource()).getPlayerOrException(),
            player -> {
               if (((CommandSourceStack)context.getSource()).getPlayer() != null) {
                  player.setUsedBlueLore(usedLore);
                  ((CommandSourceStack)context.getSource())
                     .sendSuccess(
                        () -> Component.literal("Updated lore status for " + ((CommandSourceStack)context.getSource()).getPlayer().getName().getString()), true
                     );
                  return 1;
               } else {
                  ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("That command needs to be run by a player."), true);
                  return 0;
               }
            },
            () -> 0
         );
      } catch (CommandSyntaxException e) {
         ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("That command needs to be run by a player."), true);
         return 0;
      }
   }

   private static int getMobCaps(CommandContext<CommandSourceStack> context) {
      try {
         SpawnState manager = ((CommandSourceStack)context.getSource()).getLevel().getChunkSource().getLastSpawnState();

         for (MobCategory classification : MobCategory.values()) {
            int spawnableChunkCount = manager.getSpawnableChunkCount();
            int cap = classification.getMaxInstancesPerChunk() * spawnableChunkCount / (int)Math.pow(17.0, 2.0);
            int count = manager.getMobCategoryCounts().getInt(classification);
            ((CommandSourceStack)context.getSource())
               .sendSuccess(() -> Component.literal(String.format("%s %d / %d", classification.getName(), count, cap)), true);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return 1;
   }
}

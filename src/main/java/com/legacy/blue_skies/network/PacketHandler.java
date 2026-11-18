package com.legacy.blue_skies.network;

import com.legacy.blue_skies.network.c_to_s.ChangeTomePacket;
import com.legacy.blue_skies.network.c_to_s.GetClientMotionPacket;
import com.legacy.blue_skies.network.c_to_s.HandleScreenClickPacket;
import com.legacy.blue_skies.network.c_to_s.OpenInventoryPacket;
import com.legacy.blue_skies.network.c_to_s.OpenedLorePacket;
import com.legacy.blue_skies.network.c_to_s.OpenedPerksScreenPacket;
import com.legacy.blue_skies.network.c_to_s.SpawnSupporterPetPacket;
import com.legacy.blue_skies.network.s_to_c.BossTypePacket;
import com.legacy.blue_skies.network.s_to_c.DisplayToastPacket;
import com.legacy.blue_skies.network.s_to_c.DungeonMusicPacket;
import com.legacy.blue_skies.network.s_to_c.IllagerTeleportPacket;
import com.legacy.blue_skies.network.s_to_c.SetInvisiblePacket;
import com.legacy.blue_skies.network.s_to_c.SetMouseStackPacket;
import com.legacy.blue_skies.network.s_to_c.SpawnParticlePacket;
import com.legacy.blue_skies.network.s_to_c.SyncDataRegistryPacket;
import com.legacy.blue_skies.network.s_to_c.SyncItemInSlotPacket;
import com.legacy.blue_skies.network.s_to_c.SyncPlayerPacket;
import com.legacy.blue_skies.network.s_to_c.UpdateBlueLorePacket;
import com.legacy.blue_skies.network.s_to_c.UpdateNatureHealthPacket;
import com.legacy.blue_skies.network.s_to_c.UpdateSupportersPacket;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.ConfigurationPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber(
   bus = Bus.MOD
)
public class PacketHandler {
   @SubscribeEvent
   public static void registerPayloads(RegisterPayloadHandlerEvent event) {
      IPayloadRegistrar registrar = event.registrar("blue_skies").versioned("1").optional();
      playPacket(registrar, new SpawnParticlePacket.Handler());
      playPacket(registrar, new BossTypePacket.Handler());
      playPacket(registrar, new SetMouseStackPacket.Handler());
      playPacket(registrar, new SyncPlayerPacket.Handler());
      playPacket(registrar, new SetInvisiblePacket.Handler());
      playPacket(registrar, new DungeonMusicPacket.Handler());
      playPacket(registrar, new UpdateSupportersPacket.Handler());
      playPacket(registrar, new UpdateNatureHealthPacket.Handler());
      playPacket(registrar, new UpdateBlueLorePacket.Handler());
      playPacket(registrar, new IllagerTeleportPacket.Handler());
      playPacket(registrar, new SyncDataRegistryPacket.Handler());
      playPacket(registrar, new DisplayToastPacket.Handler());
      playPacket(registrar, new SyncItemInSlotPacket.Handler());
      playPacket(registrar, new OpenInventoryPacket.Arcs.Handler());
      playPacket(registrar, new OpenInventoryPacket.Vanilla.Handler());
      playPacket(registrar, new OpenInventoryPacket.Mount.Handler());
      playPacket(registrar, new GetClientMotionPacket.Handler());
      playPacket(registrar, new SpawnSupporterPetPacket.Handler());
      playPacket(registrar, new ChangeTomePacket.Handler());
      playPacket(registrar, new OpenedLorePacket.Handler());
      playPacket(registrar, new OpenedPerksScreenPacket.Handler());
      playPacket(registrar, new HandleScreenClickPacket.Handler());
   }

   private static <T extends PacketHandler.ModPacket<T, ?>> void playPacket(IPayloadRegistrar registrar, PacketHandler.PlayHandler<T> handler) {
      registrar.play(handler.id(), handler::decoder, handler::handlePacket);
   }

   private static <T extends PacketHandler.ModPacket<T, ?>> void configurationPacket(IPayloadRegistrar registrar, PacketHandler.ConfigurationHandler<T> handler) {
      registrar.configuration(handler.id(), handler::decoder, handler::handlePacket);
   }

   public static void sendToClient(CustomPacketPayload packet, ServerPlayer serverPlayer) {
      if (!(serverPlayer instanceof FakePlayer)) {
         serverPlayer.connection.send(packet);
      }
   }

   public static void sendToClients(CustomPacketPayload packet, ServerLevel level) {
      level.players().forEach(player -> sendToClient(packet, player));
   }

   public static void sendToClients(CustomPacketPayload packet, ServerLevel level, BlockPos pos) {
      sendToClients(packet, level, pos, 256);
   }

   public static void sendToClients(CustomPacketPayload packet, ServerLevel level, BlockPos pos, int range) {
      Vec3 centerPos = Vec3.atCenterOf(pos);
      level.players().forEach(player -> {
         if (centerPos.distanceTo(player.position()) <= range) {
            sendToClient(packet, player);
         }
      });
   }

   public static void sendToClients(CustomPacketPayload packet) {
      MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
      if (server != null) {
         server.getPlayerList().getPlayers().forEach(player -> sendToClient(packet, player));
      }
   }

   public static void sendToServer(CustomPacketPayload packet) {
      Minecraft.getInstance().getConnection().send(packet);
   }

   public interface ConfigurationHandler<T extends PacketHandler.ModPacket<T, ?>> extends PacketHandler.Handler<T, ConfigurationPayloadContext> {
      private void handlePacket(T packet, ConfigurationPayloadContext context) {
         if (context.flow().isServerbound()) {
            this.handleServerbound(packet, context);
         } else {
            this.handleClientbound(packet, context);
         }
      }
   }

   protected interface Handler<T extends PacketHandler.ModPacket<T, ?>, C extends IPayloadContext> extends PacketHandler.Serializer<T> {
      void handler(T var1, C var2);

      @OnlyIn(Dist.CLIENT)
      default void handleClientbound(T packet, C context) {
         this.handler(packet, context);
      }

      default void handleServerbound(T packet, C context) {
         this.handler(packet, context);
      }
   }

   public interface ModPacket<T extends PacketHandler.ModPacket<T, ?>, H extends PacketHandler.Handler<T, ?>> extends CustomPacketPayload {
      H handler();

      default ResourceLocation id() {
         return this.handler().id();
      }

      default void write(FriendlyByteBuf buff) {
         this.handler().encoder(this.getSelf(), buff);
      }

      default T getSelf() {
         return (T)this;
      }
   }

   public interface PlayHandler<T extends PacketHandler.ModPacket<T, ?>> extends PacketHandler.Handler<T, PlayPayloadContext> {
      private void handlePacket(T packet, PlayPayloadContext context) {
         if (context.flow().isServerbound()) {
            this.handleServerbound(packet, context);
         } else {
            this.handleClientbound(packet, context);
         }
      }
   }

   protected interface Serializer<T extends PacketHandler.ModPacket<T, ?>> {
      ResourceLocation id();

      void encoder(T var1, FriendlyByteBuf var2);

      T decoder(FriendlyByteBuf var1);

      @Nullable
      default MinecraftServer server(IPayloadContext context) {
         return context.level().<MinecraftServer>map(Level::getServer).orElseGet(ServerLifecycleHooks::getCurrentServer);
      }

      @Nullable
      default ServerPlayer serverPlayer(IPayloadContext context) {
         Optional<Player> player = context.player();
         return player.isPresent() && player.get() instanceof ServerPlayer s ? s : null;
      }

      @Nullable
      default ServerLevel serverLevel(IPayloadContext context) {
         Optional<Level> level = context.level();
         return level.isPresent() && level.get() instanceof ServerLevel s ? s : null;
      }
   }
}

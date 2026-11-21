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
import javax.annotation.Nullable;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

/**
 * Networking entry point, ported to the NeoForge 1.21.1 payload API.
 *
 * <p>The original mod used the 1.20.4 {@code IPayloadRegistrar#play} API where a single
 * registration covered both directions. On 1.21.1 the equivalent is
 * {@link PayloadRegistrar#playBidirectional}; the flow is dispatched inside the handler
 * with {@link IPayloadContext#flow()} so the existing per-packet direction hooks keep
 * working unchanged.</p>
 *
 * <p>Payload codecs use {@link RegistryFriendlyByteBuf} because 1.21.1 item stacks must be
 * encoded through their registry-aware stream codecs (data components).</p>
 */
@EventBusSubscriber(bus = Bus.MOD)
public class PacketHandler {
   @SubscribeEvent
   public static void registerPayloads(RegisterPayloadHandlersEvent event) {
      PayloadRegistrar registrar = event.registrar("1").optional();
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

   private static <T extends PacketHandler.ModPacket<T, ?>> void playPacket(PayloadRegistrar registrar, PacketHandler.Handler<T> handler) {
      registrar.playBidirectional(handler.type(), handler.codec(), (packet, context) -> {
         if (context.flow().isServerbound()) {
            handler.handleServerbound(packet, context);
         } else {
            handler.handleClientbound(packet, context);
         }
      });
   }

   public static void sendToClient(CustomPacketPayload packet, ServerPlayer serverPlayer) {
      if (!(serverPlayer instanceof FakePlayer)) {
         PacketDistributor.sendToPlayer(serverPlayer, packet);
      }
   }

   public static void sendToClients(CustomPacketPayload packet, ServerLevel level) {
      PacketDistributor.sendToPlayersInDimension(level, packet);
   }

   public static void sendToClients(CustomPacketPayload packet, ServerLevel level, net.minecraft.core.BlockPos pos) {
      sendToClients(packet, level, pos, 256);
   }

   public static void sendToClients(CustomPacketPayload packet, ServerLevel level, net.minecraft.core.BlockPos pos, int range) {
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
      PacketDistributor.sendToServer(packet);
   }

   public interface Handler<T extends PacketHandler.ModPacket<T, ?>> extends PacketHandler.Serializer<T> {
      void handler(T packet, IPayloadContext context);

      @OnlyIn(Dist.CLIENT)
      default void handleClientbound(T packet, IPayloadContext context) {
         this.handler(packet, context);
      }

      default void handleServerbound(T packet, IPayloadContext context) {
         this.handler(packet, context);
      }
   }

   public interface ModPacket<T extends PacketHandler.ModPacket<T, ?>, H extends PacketHandler.Handler<T>> extends CustomPacketPayload {
      H handler();

      default ResourceLocation id() {
         return this.handler().id();
      }

      @Override
      default CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
         return this.handler().type();
      }
   }

   public interface PlayHandler<T extends PacketHandler.ModPacket<T, ?>> extends PacketHandler.Handler<T> {
   }

   protected interface Serializer<T extends PacketHandler.ModPacket<T, ?>> {
      ResourceLocation id();

      default CustomPacketPayload.Type<T> type() {
         return new CustomPacketPayload.Type<>(this.id());
      }

      default StreamCodec<RegistryFriendlyByteBuf, T> codec() {
         return StreamCodec.of((buff, packet) -> this.encoder(packet, buff), this::decoder);
      }

      void encoder(T packet, RegistryFriendlyByteBuf buff);

      T decoder(RegistryFriendlyByteBuf buff);

      @Nullable
      default MinecraftServer server(IPayloadContext context) {
         Level level = context.player().level();
         return level.getServer();
      }

      @Nullable
      default ServerPlayer serverPlayer(IPayloadContext context) {
         return context.player() instanceof ServerPlayer serverPlayer ? serverPlayer : null;
      }

      @Nullable
      default ServerLevel serverLevel(IPayloadContext context) {
         return context.player().level() instanceof ServerLevel serverLevel ? serverLevel : null;
      }
   }
}

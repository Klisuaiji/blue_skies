package com.legacy.structure_gel.core.network;

import com.legacy.structure_gel.core.network.bi_directional.UpdateBuildingToolPacket;
import com.legacy.structure_gel.core.network.c_to_s.ActionHistoryPacket;
import com.legacy.structure_gel.core.network.c_to_s.ExportPalettePacket;
import com.legacy.structure_gel.core.network.c_to_s.LeftClickBuildingToolPacket;
import com.legacy.structure_gel.core.network.c_to_s.MiddleClickBuildingToolPacket;
import com.legacy.structure_gel.core.network.c_to_s.ProcessDeletePacket;
import com.legacy.structure_gel.core.network.c_to_s.RequestClipboardPacket;
import com.legacy.structure_gel.core.network.c_to_s.RequestRegistryKeysPacket;
import com.legacy.structure_gel.core.network.c_to_s.RightClickBuildingToolPacket;
import com.legacy.structure_gel.core.network.c_to_s.UpdateDataHandlerPacket;
import com.legacy.structure_gel.core.network.c_to_s.UpdateDynamicSpawnerPacket;
import com.legacy.structure_gel.core.network.s_to_c.EditClipboardPacket;
import com.legacy.structure_gel.core.network.s_to_c.PlaySoundPacket;
import com.legacy.structure_gel.core.network.s_to_c.SendRegistryKeysToClientPacket;
import com.legacy.structure_gel.core.network.s_to_c.SendStructureInfoToClientPacket;
import com.legacy.structure_gel.core.network.s_to_c.ToggleViewBoundsPacket;
import com.legacy.structure_gel.core.network.s_to_c.UpdateGelPlayerPacket;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber(bus = Bus.MOD)
public class SGPacketHandler {
   // 1.21.1 NeoForge: channels are replaced by CustomPacketPayload + PayloadRegistrar.
   // Payloads must be registered through RegisterPayloadHandlersEvent (fired on the mod bus).
   @SubscribeEvent
   public static void registerPayloads(RegisterPayloadHandlersEvent event) {
      PayloadRegistrar registrar = event.registrar("9");
      playToClient(registrar, new EditClipboardPacket.Handler());
      playToClient(registrar, new PlaySoundPacket.Handler());
      playToClient(registrar, new SendRegistryKeysToClientPacket.Handler());
      playToClient(registrar, new SendStructureInfoToClientPacket.Handler());
      playToClient(registrar, new ToggleViewBoundsPacket.Handler());
      playToClient(registrar, new UpdateGelPlayerPacket.Handler());
      playToServer(registrar, new ActionHistoryPacket.Handler());
      playToServer(registrar, new LeftClickBuildingToolPacket.Handler());
      playToServer(registrar, new RightClickBuildingToolPacket.Handler());
      playToServer(registrar, new MiddleClickBuildingToolPacket.Handler());
      playToServer(registrar, new RequestClipboardPacket.Handler());
      playToServer(registrar, new RequestRegistryKeysPacket.Handler());
      playToServer(registrar, new UpdateDynamicSpawnerPacket.Handler());
      playToServer(registrar, new UpdateDataHandlerPacket.Handler());
      playToServer(registrar, new ProcessDeletePacket.Handler());
      playToServer(registrar, new ExportPalettePacket.Handler());
      playBidirectional(registrar, new UpdateBuildingToolPacket.Handler());
   }

   private static <T extends SGPacketHandler.ModPacket<T, ?>> void playToClient(PayloadRegistrar registrar, SGPacketHandler.PlayHandler<T> handler) {
      registrar.playToClient(handler.type(), codec(handler), payloadHandler(handler));
   }

   private static <T extends SGPacketHandler.ModPacket<T, ?>> void playToServer(PayloadRegistrar registrar, SGPacketHandler.PlayHandler<T> handler) {
      registrar.playToServer(handler.type(), codec(handler), payloadHandler(handler));
   }

   private static <T extends SGPacketHandler.ModPacket<T, ?>> void playBidirectional(PayloadRegistrar registrar, SGPacketHandler.PlayHandler<T> handler) {
      registrar.playBidirectional(handler.type(), codec(handler), payloadHandler(handler));
   }

   private static <T extends SGPacketHandler.ModPacket<T, ?>> StreamCodec<RegistryFriendlyByteBuf, T> codec(SGPacketHandler.Serializer<T> serializer) {
      return new StreamCodec<RegistryFriendlyByteBuf, T>() {
         @Override
         public T decode(RegistryFriendlyByteBuf buff) {
            return serializer.decoder(buff);
         }

         @Override
         public void encode(RegistryFriendlyByteBuf buff, T packet) {
            serializer.encoder(packet, buff);
         }
      };
   }

   private static <T extends SGPacketHandler.ModPacket<T, ?>> IPayloadHandler<T> payloadHandler(SGPacketHandler.PlayHandler<T> handler) {
      return (packet, context) -> {
         if (context.flow().isServerbound()) {
            handler.handleServerbound(packet, context);
         } else {
            handler.handleClientbound(packet, context);
         }
      };
   }

   public static void sendToClient(CustomPacketPayload packet, ServerPlayer serverPlayer) {
      if (!(serverPlayer instanceof FakePlayer)) {
         PacketDistributor.sendToPlayer(serverPlayer, packet);
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
      PacketDistributor.sendToServer(packet);
   }

   protected interface Handler<T extends SGPacketHandler.ModPacket<T, ?>> extends SGPacketHandler.Serializer<T> {
      void handler(T var1, IPayloadContext var2);

      default CustomPacketPayload.Type<T> type() {
         return new CustomPacketPayload.Type<>(this.id());
      }

      @OnlyIn(Dist.CLIENT)
      default void handleClientbound(T packet, IPayloadContext context) {
         this.handler(packet, context);
      }

      default void handleServerbound(T packet, IPayloadContext context) {
         this.handler(packet, context);
      }
   }

   public interface ModPacket<T extends SGPacketHandler.ModPacket<T, ?>, H extends SGPacketHandler.Handler<T>> extends CustomPacketPayload {
      H handler();

      @Override
      default CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
         return this.handler().type();
      }

      default ResourceLocation id() {
         return this.handler().id();
      }

      default void write(RegistryFriendlyByteBuf buff) {
         this.handler().encoder(this.getSelf(), buff);
      }

      default T getSelf() {
         return (T)this;
      }
   }

   public interface PlayHandler<T extends SGPacketHandler.ModPacket<T, ?>> extends SGPacketHandler.Handler<T> {
   }

   protected interface Serializer<T extends SGPacketHandler.ModPacket<T, ?>> {
      ResourceLocation id();

      void encoder(T var1, RegistryFriendlyByteBuf var2);

      T decoder(RegistryFriendlyByteBuf var1);

      @Nullable
      default MinecraftServer server(IPayloadContext context) {
         // 1.21.1: IPayloadContext has no level(); resolve the server from the sending player.
         return context.player().level() instanceof ServerLevel serverLevel ? serverLevel.getServer() : ServerLifecycleHooks.getCurrentServer();
      }

      @Nullable
      default ServerPlayer serverPlayer(IPayloadContext context) {
         // 1.21.1: IPayloadContext#player() returns a Player directly instead of an Optional.
         return context.player() instanceof ServerPlayer serverPlayer ? serverPlayer : null;
      }

      @Nullable
      default ServerLevel serverLevel(IPayloadContext context) {
         return context.player().level() instanceof ServerLevel serverLevel ? serverLevel : null;
      }
   }
}

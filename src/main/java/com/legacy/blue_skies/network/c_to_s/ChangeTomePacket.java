package com.legacy.blue_skies.network.c_to_s;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.menu.SummoningTableMenu;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.registries.SkiesStats;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ChangeTomePacket implements PacketHandler.ModPacket<ChangeTomePacket, ChangeTomePacket.Handler> {
   private int containerId;
   private boolean enablingGolems;

   public ChangeTomePacket(int containerId, boolean golemsEnabled) {
      this.containerId = containerId;
      this.enablingGolems = golemsEnabled;
   }

   public ChangeTomePacket.Handler handler() {
      return new ChangeTomePacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<ChangeTomePacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("change_tome");
      }

      public void encoder(ChangeTomePacket packet, RegistryFriendlyByteBuf buff) {
         buff.writeInt(packet.containerId);
         buff.writeBoolean(packet.enablingGolems);
      }

      public ChangeTomePacket decoder(RegistryFriendlyByteBuf buff) {
         return new ChangeTomePacket(buff.readInt(), buff.readBoolean());
      }

      public void handler(ChangeTomePacket packet, IPayloadContext context) {
         context.enqueueWork(() -> handlePacket(packet, this.serverPlayer(context)));
      }

      private static void handlePacket(ChangeTomePacket packet, Player player) {
         if (player.containerMenu != null
            && player.containerMenu.containerId == packet.containerId
            && !player.isSpectator()
            && player.containerMenu instanceof SummoningTableMenu) {
            ((SummoningTableMenu)player.containerMenu).onTomeButtonPressed(packet.enablingGolems);
            player.level()
               .playSound(
                  null,
                  player.blockPosition(),
                  SkiesSounds.BLOCK_SUMMONING_TABLE_TOME_CHANGE,
                  SoundSource.BLOCKS,
                  1.0F,
                  player.getRandom().nextFloat() * 0.1F + 0.9F
               );
            player.awardStat(SkiesStats.TOMES_CHANGED);
         }
      }
   }
}

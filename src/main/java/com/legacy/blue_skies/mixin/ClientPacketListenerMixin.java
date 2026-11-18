package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.data.objects.tags.SkiesEntityTags;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ClientPacketListener.class})
public class ClientPacketListenerMixin {
   private static Entity blue_skies$lastRidden;

   @ModifyVariable(
      at = @At("STORE"),
      method = {"handleSetEntityPassengersPacket(Lnet/minecraft/network/protocol/game/ClientboundSetPassengersPacket;)V"},
      index = 2
   )
   private Entity modify$entity(Entity entity) {
      blue_skies$lastRidden = entity;
      return entity;
   }

   @ModifyArg(
      at = @At(
         value = "INVOKE",
         target = "net/minecraft/network/chat/Component.translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;"
      ),
      method = {"handleSetEntityPassengersPacket"}
   )
   private String modify$pKey(String pKey) {
      return blue_skies$lastRidden != null && blue_skies$lastRidden.getType().is(SkiesEntityTags.BOSSES) ? "gui.blue_skies.tooltip.boss_grab" : pKey;
   }
}

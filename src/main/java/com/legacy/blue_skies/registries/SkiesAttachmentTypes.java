package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.structure_gel.api.registry.RegistrarHolder;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Static;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

@RegistrarHolder
public class SkiesAttachmentTypes {
   public static final RegistrarHandler<AttachmentType<?>> HANDLER = RegistrarHandler.getOrCreate(Keys.ATTACHMENT_TYPES, "blue_skies");
   public static final Static<AttachmentType<SkiesPlayer>> PLAYER_ATTACHMENT = HANDLER.createStatic(
      "player_attachment", () -> AttachmentType.builder(h -> new SkiesPlayer((Player)h)).serialize(SkiesPlayer.Serializer.INSTANCE).copyOnDeath().build()
   );
}

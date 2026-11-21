package com.legacy.structure_gel.api.util;

import com.mojang.serialization.Codec;
import java.util.UUID;

public class SGCodecs {
   // Fully qualified UUID references: the field name shadows the type inside its own initializer.
   public static final Codec<UUID> UUID = Codec.STRING.xmap(java.util.UUID::fromString, java.util.UUID::toString);
}

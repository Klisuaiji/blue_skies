package com.legacy.blue_skies.blocks.portal;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.registries.SkiesPoiTypes;
import com.legacy.structure_gel.api.block.GelPortalBlock;
import com.legacy.structure_gel.api.dimension.portal.GelTeleporter;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class EverbrightPortalBlock extends SkyPortalBlock {
   public static final MapCodec<EverbrightPortalBlock> CODEC = simpleCodec(EverbrightPortalBlock::new);

   public EverbrightPortalBlock(Properties props) {
      super(props);
   }

   public GelTeleporter getTeleporter(ServerLevel serverLevel, GelPortalBlock portal) {
      return new SkyPortalBlock.Teleporter(
         serverLevel,
         SkiesDimensions::everbrightKey,
         () -> Level.OVERWORLD,
         () -> (PoiType)SkiesPoiTypes.EVERBRIGHT_PORTAL.get(),
         () -> portal,
         () -> SkiesBlocks.turquoise_stonebrick.defaultBlockState()
      );
   }

   @Override
   boolean isEverbright() {
      return true;
   }

   public MapCodec<? extends GelPortalBlock> codec() {
      return CODEC;
   }
}

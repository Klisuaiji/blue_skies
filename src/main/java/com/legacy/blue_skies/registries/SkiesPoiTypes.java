package com.legacy.blue_skies.registries;

import com.legacy.structure_gel.api.registry.RegistrarHolder;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Static;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.RegisterEvent;

@RegistrarHolder
public class SkiesPoiTypes {
   public static final RegistrarHandler<PoiType> HANDLER = RegistrarHandler.getOrCreate(Registries.POINT_OF_INTEREST_TYPE, "blue_skies");
   public static final Static<PoiType> STAR_EMITTER = basic("star_emitter", () -> SkiesBlocks.star_emitter);
   public static final Static<PoiType> TROUGH = basic("trough", () -> SkiesBlocks.trough);
   public static final Static<PoiType> TOOL_BOX = basic("tool_box", () -> SkiesBlocks.tool_box);
   public static final Static<PoiType> BAG_OF_SPOILS = basic("bag_of_spoils", () -> SkiesBlocks.bag_of_spoils);
   public static final Static<PoiType> SUMMONING_TABLE = basic("summoning_table", () -> SkiesBlocks.summoning_table);
   public static final Static<PoiType> ALCHEMY_TABLE = basic("alchemy_table", () -> SkiesBlocks.alchemy_table);
   public static final Static<PoiType> EVERBRIGHT_PORTAL = withTicketCount("everbright_portal", () -> SkiesBlocks.everbright_portal, 0);
   public static final Static<PoiType> EVERDAWN_PORTAL = withTicketCount("everdawn_portal", () -> SkiesBlocks.everdawn_portal, 0);
   public static final Static<PoiType> SPIDER_NEST = withTicketCount("spider_nest", () -> SkiesBlocks.spider_nest, 0);
   public static final Static<PoiType> KEYSTONE = HANDLER.createStatic(
      "keystone",
      () -> new PoiType(
         getAllStates(SkiesBlocks.bright_blinding_keystone, SkiesBlocks.dawn_blinding_keystone, SkiesBlocks.nature_keystone, SkiesBlocks.poison_keystone), 0, 1
      )
   );
   public static final Static<PoiType> WARDING_PEARL = HANDLER.createStatic(
      "warding_pearl", () -> new PoiType(getAllStates(SkiesBlocks.warding_pearl, SkiesBlocks.wall_warding_pearl), 0, 1)
   );

   public static void init(RegisterEvent event) {
      HANDLER.registerValues(event);
   }

   private static Static<PoiType> withTicketCount(String name, Supplier<Block> block, int ticket) {
      return HANDLER.createStatic(name, () -> new PoiType(getAllStates(block.get()), ticket, 1));
   }

   private static Static<PoiType> basic(String name, Supplier<Block> block) {
      return withTicketCount(name, block, 1);
   }

   private static Set<BlockState> getAllStates(Block block) {
      return Set.copyOf(block.getStateDefinition().getPossibleStates());
   }

   public static Set<BlockState> getAllStates(Block... blocks) {
      return Arrays.stream(blocks).flatMap(b -> getAllStates(b).stream()).collect(Collectors.toSet());
   }
}

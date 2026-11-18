package com.legacy.blue_skies.registries;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class SkiesEmissives {
   public static final Map<Block, Set<RenderType>> EMISSIVES = Maps.newHashMap();

   public static void initBlocks() {
      solid(SkiesBlocks.everbright_moonstone_ore);
      solid(SkiesBlocks.everdawn_moonstone_ore);
      solid(SkiesBlocks.glowing_poison_stone);
      solid(SkiesBlocks.glowing_poison_stone_slab);
      solid(SkiesBlocks.glowing_poison_stone_stairs);
      solid(SkiesBlocks.cinderstone);
      solid(SkiesBlocks.cinderstone_slab);
      solid(SkiesBlocks.cinderstone_stairs);
      solid(SkiesBlocks.cinderstone_wall);
      transparent(SkiesBlocks.nightcress);
      transparent(SkiesBlocks.potted_nightcress);
      transparent(SkiesBlocks.lucentroot);
      transparent(SkiesBlocks.potted_lucentroot);
      transparent(SkiesBlocks.glimmer_reed);
      transparent(SkiesBlocks.warding_pearl);
      transparent(SkiesBlocks.wall_warding_pearl);
      none(SkiesBlocks.polished_cinderstone);
      none(SkiesBlocks.polished_cinderstone_slab);
      none(SkiesBlocks.polished_cinderstone_stairs);
      none(SkiesBlocks.polished_cinderstone_wall);
   }

   private static <B extends Block> void solid(B block) {
      register(block, RenderType.cutout(), RenderType.solid());
   }

   private static <B extends Block> void transparent(B block) {
      register(block, RenderType.cutout(), RenderType.cutoutMipped());
   }

   private static <B extends Block> void none(B block) {
      register(block, RenderType.solid());
   }

   public static <B extends Block> void register(B block, RenderType... types) {
      block.properties.emissiveRendering((state, world, pos) -> true);
      EMISSIVES.put(block, Set.of(types));
   }
}

package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.blocks.construction.AlchemyTableBlock;
import com.legacy.blue_skies.blocks.construction.BagOfSpoilsBlock;
import com.legacy.blue_skies.blocks.construction.FoodPrepTableBlock;
import com.legacy.blue_skies.blocks.construction.HorizoniteForgeBlock;
import com.legacy.blue_skies.blocks.construction.LitSlabBlock;
import com.legacy.blue_skies.blocks.construction.LitStairsBlock;
import com.legacy.blue_skies.blocks.construction.MoonstonePressurePlateBlock;
import com.legacy.blue_skies.blocks.construction.SkyBanefulPotBlock;
import com.legacy.structure_gel.api.registry.RegistrarHolder;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Static;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;

@RegistrarHolder
public class SkiesBlockTypes {
   public static final RegistrarHandler<MapCodec<? extends Block>> HANDLER = RegistrarHandler.getOrCreate(Registries.BLOCK_TYPE, "blue_skies");
   public static final Static<MapCodec<AlchemyTableBlock>> ALCHEMY_TABLE = HANDLER.createStatic("alchemy_table", () -> AlchemyTableBlock.CODEC);
   public static final Static<MapCodec<BagOfSpoilsBlock>> BAG_OF_SPOILS = HANDLER.createStatic("bag_of_spoils", () -> BagOfSpoilsBlock.CODEC);
   public static final Static<MapCodec<FoodPrepTableBlock>> FOOD_PREP_TABLE = HANDLER.createStatic("food_prep_table", () -> FoodPrepTableBlock.CODEC);
   public static final Static<MapCodec<HorizoniteForgeBlock>> HORIZONITE_FORGE = HANDLER.createStatic("horizonite_forge", () -> HorizoniteForgeBlock.CODEC);
   public static final Static<MapCodec<LitSlabBlock>> LIT_SLAB = HANDLER.createStatic("lit_slab", () -> LitSlabBlock.CODEC);
   public static final Static<MapCodec<LitStairsBlock>> LIT_STAIRS = HANDLER.createStatic("lit_stairs", () -> LitStairsBlock.CODEC);
   public static final Static<MapCodec<MoonstonePressurePlateBlock>> MOONSTONE_PRESSURE_PLATE = HANDLER.createStatic(
      "moonstone_pressure_plate", () -> MoonstonePressurePlateBlock.CODEC
   );
   public static final Static<MapCodec<FlowerPotBlock>> BANEFUL_FLOWER_POT = HANDLER.createStatic("baneful_flower_pot", () -> SkyBanefulPotBlock.CODEC);
}

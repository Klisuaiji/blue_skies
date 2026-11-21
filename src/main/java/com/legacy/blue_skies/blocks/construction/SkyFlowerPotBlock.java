package com.legacy.blue_skies.blocks.construction;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SkyFlowerPotBlock extends FlowerPotBlock {
   public static final MapCodec<FlowerPotBlock> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(flowerCodec(), propertiesCodec()).apply(instance, (flower, prop) -> new SkyFlowerPotBlock(() -> flower, prop))
   );

   public SkyFlowerPotBlock(Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> flower, Properties properties) {
      super(emptyPot, flower, properties);
      ((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(BuiltInRegistries.BLOCK.getKey(flower.get()), () -> this);
   }

   public SkyFlowerPotBlock(Supplier<? extends Block> flower, Properties properties) {
      this(() -> (FlowerPotBlock)Blocks.FLOWER_POT, flower, properties);
   }

   public SkyFlowerPotBlock(Supplier<? extends Block> flower) {
      this(flower, Properties.of().strength(0.0F));
   }

   protected static <T extends FlowerPotBlock> RecordCodecBuilder<T, Block> flowerCodec() {
      return BuiltInRegistries.BLOCK.byNameCodec().fieldOf("potted").forGetter(block -> block.getPotted());
   }

   public MapCodec<FlowerPotBlock> codec() {
      return CODEC;
   }
}

package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockSetType.PressurePlateSensitivity;

public class SkiesBlockSets {
   public static final BlockSetType BLUEBRIGHT = register("bluebright");
   public static final BlockSetType STARLIT = register("starlit");
   public static final BlockSetType FROSTBRIGHT = register("frostbright");
   public static final BlockSetType LUNAR = register("lunar");
   public static final BlockSetType DUSK = register("dusk");
   public static final BlockSetType MAPLE = register("maple");
   public static final BlockSetType COMET = register("comet");
   public static final BlockSetType TURQUOISE_STONE = register(
      new BlockSetType(
         BlueSkies.find("turquoise_stone"),
         true,
         true,
         false,
         PressurePlateSensitivity.MOBS,
         SoundType.STONE,
         SoundEvents.IRON_DOOR_CLOSE,
         SoundEvents.IRON_DOOR_OPEN,
         SoundEvents.IRON_TRAPDOOR_CLOSE,
         SoundEvents.IRON_TRAPDOOR_OPEN,
         SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF,
         SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
         SoundEvents.STONE_BUTTON_CLICK_OFF,
         SoundEvents.STONE_BUTTON_CLICK_ON
      )
   );
   public static final BlockSetType LUNAR_STONE = register(
      new BlockSetType(
         BlueSkies.find("lunar_stone"),
         true,
         true,
         false,
         PressurePlateSensitivity.MOBS,
         SoundType.STONE,
         SoundEvents.IRON_DOOR_CLOSE,
         SoundEvents.IRON_DOOR_OPEN,
         SoundEvents.IRON_TRAPDOOR_CLOSE,
         SoundEvents.IRON_TRAPDOOR_OPEN,
         SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF,
         SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
         SoundEvents.STONE_BUTTON_CLICK_OFF,
         SoundEvents.STONE_BUTTON_CLICK_ON
      )
   );
   public static final BlockSetType MOONSTONE = register("moonstone");
   public static final BlockSetType CRYSTALLIZED = register("crystallized");

   public static BlockSetType register(String key) {
      return BlockSetType.register(new BlockSetType(BlueSkies.find(key)));
   }

   public static BlockSetType register(BlockSetType type) {
      return BlockSetType.register(type);
   }

   public static void init() {
   }
}

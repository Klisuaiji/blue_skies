package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockSetType.PressurePlateSensitivity;

public class SkiesBlockSets {
   // 1.21.1: BlockSetType is a final record and its registry helper is private. Custom
   // block set types are simply constructed; the internal name->type map only exists to
   // decode vanilla block set types and is not required for runtime use.
   public static final BlockSetType BLUEBRIGHT = new BlockSetType(BlueSkies.find("bluebright"));
   public static final BlockSetType STARLIT = new BlockSetType(BlueSkies.find("starlit"));
   public static final BlockSetType FROSTBRIGHT = new BlockSetType(BlueSkies.find("frostbright"));
   public static final BlockSetType LUNAR = new BlockSetType(BlueSkies.find("lunar"));
   public static final BlockSetType DUSK = new BlockSetType(BlueSkies.find("dusk"));
   public static final BlockSetType MAPLE = new BlockSetType(BlueSkies.find("maple"));
   public static final BlockSetType COMET = new BlockSetType(BlueSkies.find("comet"));
   public static final BlockSetType TURQUOISE_STONE = new BlockSetType(
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
   );
   public static final BlockSetType LUNAR_STONE = new BlockSetType(
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
   );
   public static final BlockSetType MOONSTONE = new BlockSetType(BlueSkies.find("moonstone"));
   public static final BlockSetType CRYSTALLIZED = new BlockSetType(BlueSkies.find("crystallized"));

   public static void init() {
   }
}

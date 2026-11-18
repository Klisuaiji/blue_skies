package com.legacy.blue_skies.mixin;

import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromBlockMemory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({SetWalkTargetFromBlockMemory.class})
public class StayNearPointTaskMixin {
}

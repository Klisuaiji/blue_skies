package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.blocks.natural.SkyFarmlandBlock;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.sensing.SecondaryPoiSensor;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({SecondaryPoiSensor.class})
public class SecondaryPoiSensorMixin {
   @Inject(
      at = {@At(
         shift = Shift.AFTER,
         value = "INVOKE",
         target = "net/minecraft/core/BlockPos.offset(III)Lnet/minecraft/core/BlockPos;"
      )},
      method = {"doTick"},
      cancellable = true,
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private void doTick(ServerLevel level, Villager villager, CallbackInfo ci, ResourceKey<Level> resourcekey, BlockPos blockpos, List<GlobalPos> list) {
      if (villager.getVillagerData().getProfession() == VillagerProfession.FARMER && level.getBlockState(blockpos).getBlock() instanceof SkyFarmlandBlock) {
         list.add(GlobalPos.of(resourcekey, blockpos));
      }
   }
}

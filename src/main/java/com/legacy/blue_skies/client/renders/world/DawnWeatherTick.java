package com.legacy.blue_skies.client.renders.world;

import com.legacy.blue_skies.client.audio.ambient.SkiesWeatherAmbientSounds;
import com.legacy.blue_skies.data.objects.tags.SkiesBiomeTags;
import com.legacy.blue_skies.registries.SkiesSounds;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DawnWeatherTick {
   public static final DawnWeatherTick INSTANCE = new DawnWeatherTick();
   private int rainSoundTime;
   private SoundInstance desertWind;

   public void tick(int ticks, ClientLevel level, Minecraft mc, Camera camera) {
      this.tickLoops(mc.getSoundManager(), level, ticks, camera);
      float f = level.getRainLevel(1.0F) / (Minecraft.useFancyGraphics() ? 1.0F : 2.0F);
      if (!(f <= 0.0F)) {
         Options options = mc.options;
         Random random = new Random(ticks * 312987231L);
         BlockPos blockpos = camera.getBlockPosition();
         BlockPos blockpos1 = null;
         int i = (int)(100.0F * f * f) / (options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

         for (int j = 0; j < i; j++) {
            int k = random.nextInt(21) - 10;
            int l = random.nextInt(21) - 10;
            BlockPos blockpos2 = level.getHeightmapPos(Types.MOTION_BLOCKING, blockpos.offset(k, 0, l)).below();
            Biome biome = (Biome)level.getBiome(blockpos2).value();
            if (blockpos2.getY() > 0
               && blockpos2.getY() <= blockpos.getY() + 10
               && blockpos2.getY() >= blockpos.getY() - 10
               && biome.getPrecipitationAt(blockpos2) == Precipitation.RAIN
               && biome.warmEnoughToRain(blockpos2)) {
               blockpos1 = blockpos2;
               if (options.particles().get() == ParticleStatus.MINIMAL) {
                  break;
               }

               double d0 = random.nextDouble();
               double d1 = random.nextDouble();
               BlockState blockstate = level.getBlockState(blockpos2);
               FluidState fluidstate = level.getFluidState(blockpos2);
               VoxelShape voxelshape = blockstate.getCollisionShape(level, blockpos2);
               double d2 = voxelshape.max(Axis.Y, d0, d1);
               double d3 = fluidstate.getHeight(level, blockpos2);
               double d4 = Math.max(d2, d3);
               ParticleOptions iparticledata = !fluidstate.is(FluidTags.LAVA) && !blockstate.is(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(blockstate)
                  ? ParticleTypes.RAIN
                  : ParticleTypes.SMOKE;
               level.addParticle(iparticledata, blockpos2.getX() + d0, blockpos2.getY() + d4, blockpos2.getZ() + d1, 0.0, 0.0, 0.0);
            }
         }

         if (blockpos1 != null && random.nextInt(3) < this.rainSoundTime++) {
            this.rainSoundTime = 0;
            if (blockpos1.getY() > blockpos.getY() + 1 && level.getHeightmapPos(Types.MOTION_BLOCKING, blockpos).getY() > Mth.floor(blockpos.getY())) {
               level.playLocalSound(
                  blockpos1,
                  level.thunderLevel <= 0.0F ? SkiesSounds.AMBIENT_RAIN_DRIZZLE : SoundEvents.WEATHER_RAIN_ABOVE,
                  SoundSource.WEATHER,
                  0.1F,
                  0.5F,
                  false
               );
            } else {
               level.playLocalSound(
                  blockpos1, level.thunderLevel <= 0.0F ? SkiesSounds.AMBIENT_RAIN_DRIZZLE : SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false
               );
            }
         }
      }
   }

   private void tickLoops(SoundManager soundManager, ClientLevel level, int ticks, Camera camera) {
      BlockPos blockpos = camera.getBlockPosition();
      if (level.getBrightness(LightLayer.SKY, blockpos) > 0) {
         if (level.isRaining()
            && !soundManager.isActive(this.desertWind)
            && level.registryAccess().registryOrThrow(Registries.BIOME).getOrCreateTag(SkiesBiomeTags.HAS_SANDSTORMS).contains(level.getBiome(blockpos))
            && (this.desertWind != null && !soundManager.isActive(this.desertWind) || this.desertWind == null && !soundManager.isActive(this.desertWind))
            && camera.getEntity() instanceof LocalPlayer player) {
            soundManager.play(this.desertWind = new SkiesWeatherAmbientSounds.SandstormSound(player));
         }
      }
   }
}

package com.legacy.blue_skies.util;

import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public final class GeometryHelper {
   public static void drawParticleLine(
      ServerLevel world, ParticleOptions particle, Vec3 firstPos, Vec3 secondPos, double particleSpacing, double particleSpeed, boolean forced
   ) {
      drawParticleLine(world, particle, firstPos, secondPos, particleSpacing, particleSpeed, Vec3.ZERO, forced);
   }

   public static void drawParticleLine(
      ServerLevel world,
      ParticleOptions particle,
      Vec3 firstPos,
      Vec3 secondPos,
      double particleSpacing,
      double particleSpeed,
      Vec3 particleOffset,
      boolean forced
   ) {
      if (particleSpacing == 0.0) {
         particleSpacing = 0.1;
      }

      Vec3 angle = calcAngle(firstPos, secondPos);
      double magnitude = calcDistance(firstPos, secondPos);
      if (magnitude == 0.0) {
         magnitude = 1.0E-4;
      }

      double intervalX = angle.x() / magnitude * particleSpacing;
      double intervalY = angle.y() / magnitude * particleSpacing;
      double intervalZ = angle.z() / magnitude * particleSpacing;

      for (double i = 0.0; i <= magnitude / particleSpacing; i++) {
         if (forced) {
            for (ServerPlayer p : world.players()) {
               world.sendParticles(
                  p,
                  particle,
                  true,
                  firstPos.x() - intervalX * i,
                  firstPos.y() - intervalY * i,
                  firstPos.z() - intervalZ * i,
                  1,
                  particleOffset.x(),
                  particleOffset.y(),
                  particleOffset.z(),
                  particleSpeed
               );
            }
         } else {
            world.sendParticles(
               particle,
               firstPos.x() - intervalX * i,
               firstPos.y() - intervalY * i,
               firstPos.z() - intervalZ * i,
               1,
               particleOffset.x(),
               particleOffset.y(),
               particleOffset.z(),
               particleSpeed
            );
         }
      }
   }

   public static double calcDistance(Vec3 firstPos, Vec3 secondPos) {
      double x = calcExponent(firstPos.x() - secondPos.x());
      double y = calcExponent(firstPos.y() - secondPos.y());
      double z = calcExponent(firstPos.z() - secondPos.z());
      return Math.sqrt(x + y + z);
   }

   public static double calcDistance(int x1, int z1, int x2, int z2) {
      double x = calcExponent(x2 - x1);
      double z = calcExponent(z2 - z1);
      return Math.sqrt(x + z);
   }

   public static Vec3 calcAngle(Vec3 firstPos, Vec3 secondPos) {
      double x = firstPos.x() - secondPos.x();
      double y = firstPos.y() - secondPos.y();
      double z = firstPos.z() - secondPos.z();
      return new Vec3(x, y, z);
   }

   public static double calcAngleRad(double arcLength, double radius, double multiplier) {
      if (radius == 0.0) {
         radius = 1.0E-4;
      }

      return arcLength * multiplier / radius;
   }

   public static double calcExponent(double i) {
      return i * i;
   }

   public static boolean isPointInsideRec(Vec3i pos, Vec3i minPos, Vec3i maxPos) {
      return pos.getX() >= minPos.getX()
         && pos.getX() <= maxPos.getX()
         && pos.getZ() >= minPos.getZ()
         && pos.getZ() <= maxPos.getZ()
         && pos.getY() >= minPos.getY()
         && pos.getY() <= maxPos.getY();
   }

   public static boolean isInEllipsoid(double a, double b, double c, double x, double y, double z) {
      x = a - x / 2.0;
      y = b - y / 2.0;
      z = c - z / 2.0;
      return (x - a) * (x - a) / a + (y - b) * (y - b) / b + (z - c) * (z - c) / c < 1.0;
   }
}

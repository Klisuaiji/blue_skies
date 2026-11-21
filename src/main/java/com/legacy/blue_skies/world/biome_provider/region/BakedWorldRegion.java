package com.legacy.blue_skies.world.biome_provider.region;

import com.legacy.blue_skies.world.biome_provider.provider.AbstractBiomeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;

public class BakedWorldRegion {
   private static final byte DATA_VERSION = 1;
   private final int regionX;
   private final int regionZ;
   private final double coordScale;
   private final boolean shouldRescale;
   private final int biomeSize;
   private final byte[] biomes;
   private final int heightSize;
   private final short[] heights;
   private static final String DATA_VERSION_KEY = "data_version";
   private static final String REGION_POS_KEY = "pos";
   private static final String COORD_SCALE_KEY = "coord_scale";
   private static final String BIOME_SIZE_KEY = "biome_size";
   private static final String BIOMES_KEY = "biomes";
   private static final String HEIGHT_SIZE_KEY = "height_size";
   private static final String HEIGHTS_KEY = "heights";

   public BakedWorldRegion(BiomeRegion biomeRegion, HeightRegion heightRegion) {
      if (biomeRegion.scale != heightRegion.scale) {
         throw new IllegalStateException(
            String.format("The BiomeRegion has a different scale than the HeightRegion at [%d, %d]", biomeRegion.regionX * 1024, biomeRegion.regionZ * 1024)
         );
      }

      if (biomeRegion.regionX == heightRegion.regionX && biomeRegion.regionZ == heightRegion.regionZ) {
         biomeRegion.bake();
         heightRegion.bake();
         this.regionX = biomeRegion.regionX;
         this.regionZ = biomeRegion.regionZ;
         this.coordScale = biomeRegion.scale / 1024.0;
         this.shouldRescale = this.coordScale != 1.0;
         int[][] biomes = biomeRegion.getData();
         this.biomeSize = biomes.length;
         this.biomes = new byte[this.biomeSize * this.biomeSize];

         for (int x = 0; x < this.biomeSize; x++) {
            for (int z = 0; z < this.biomeSize; z++) {
               this.biomes[x * this.biomeSize + z] = (byte)biomes[x][z];
            }
         }

         int[][] heights = heightRegion.getData();
         this.heightSize = heights.length;
         this.heights = new short[this.heightSize * this.heightSize];

         for (int x = 0; x < this.heightSize; x++) {
            for (int z = 0; z < this.heightSize; z++) {
               this.heights[x * this.heightSize + z] = (short)heights[x][z];
            }
         }
      } else {
         throw new IllegalStateException(
            String.format(
               "BiomeRegion[%d, %d] has a different position than the HeightRegion[%d, %d]",
               biomeRegion.regionX,
               biomeRegion.regionZ,
               heightRegion.regionX,
               heightRegion.regionZ
            )
         );
      }
   }

   public BakedWorldRegion(CompoundTag tag) {
      int[] pos = tag.getIntArray("pos");
      if (pos.length < 2) {
         throw new IllegalStateException(
            String.format(
               "Biome region loaded from tag has an invalid \"%s\" field. It must be an int array of size 2. Actual value: %s",
               "pos",
               tag.get("pos").getAsString()
            )
         );
      }

      this.regionX = pos[0];
      this.regionZ = pos[1];
      this.coordScale = tag.getDouble("coord_scale");
      this.shouldRescale = this.coordScale != 1.0;
      this.biomeSize = tag.getInt("biome_size");
      this.biomes = tag.getByteArray("biomes");
      this.heightSize = tag.getInt("height_size");
      int[] compressedHeights = tag.getIntArray("heights");
      short[] heights = new short[compressedHeights.length * 2];

      for (int i = 0; i < compressedHeights.length; i++) {
         int h = compressedHeights[i];
         int heightIndex = i * 2;
         heights[heightIndex] = (short)(h >> 16);
         heights[heightIndex + 1] = (short)(h & 32767);
      }

      this.heights = heights;
   }

   public CompoundTag save() {
      CompoundTag tag = new CompoundTag();
      tag.putByte("data_version", (byte)1);
      tag.putIntArray("pos", new int[]{this.regionX, this.regionZ});
      tag.putDouble("coord_scale", this.coordScale);
      tag.putInt("biome_size", this.biomeSize);
      tag.putByteArray("biomes", this.biomes);
      tag.putInt("height_size", this.heightSize);
      int[] compressedHeights = new int[this.heights.length / 2];

      for (int i = 0; i < this.heights.length; i += 2) {
         compressedHeights[i / 2] = this.heights[i] << 16 | this.heights[i + 1];
      }

      tag.putIntArray("heights", compressedHeights);
      return tag;
   }

   public int getBiome(int x, int z) {
      return this.getWithinScale(this.biomes, this.biomeSize, x, z);
   }

   public int getHeight(int x, int z, AbstractBiomeProvider biomeProv) {
      double noiseScale = 0.01;
      double na = biomeProv.noiseA.getValue(x * noiseScale, z * noiseScale, false);
      double nb = biomeProv.noiseB.getValue(x * noiseScale, z * noiseScale, false);
      int offset = 4;
      int xO = (int)Math.round(na * offset);
      int zO = (int)Math.round(nb * offset);
      int total = 0;
      int count = 0;
      int blendR = 7;
      int r2 = blendR * blendR;
      int minX = this.regionX * 1024;
      int maxX = minX + 1024 - 1;
      int minZ = this.regionZ * 1024;
      int maxZ = minZ + 1024 - 1;

      for (int dx = -blendR; dx <= blendR; dx++) {
         int dx2 = dx * dx;

         for (int dz = -blendR; dz <= blendR; dz++) {
            int xPos = x + dx + xO;
            int zPos = z + dz + zO;
            if (dx2 + dz * dz <= r2) {
               BakedWorldRegion region;
               if (xPos >= minX && xPos <= maxX && zPos >= minZ && zPos <= maxZ) {
                  region = this;
               } else {
                  region = biomeProv.getWorldRegion(new ChunkPos(new BlockPos(xPos, 0, zPos)));
               }

               count++;
               total += region.getWithinScale(region.heights, region.heightSize, xPos, zPos);
            }
         }
      }

      return total / count;
   }

   private byte getWithinScale(byte[] data, int size, int x, int z) {
      if (this.shouldRescale) {
         x = (int)Math.floor(x * this.coordScale);
         z = (int)Math.floor(z * this.coordScale);
      }

      int s = size - 1;
      return data[(x & s) * size + (z & s)];
   }

   private short getWithinScale(short[] data, int size, int x, int z) {
      if (this.shouldRescale) {
         x = (int)Math.floor(x * this.coordScale);
         z = (int)Math.floor(z * this.coordScale);
      }

      int s = size - 1;
      return data[(x & s) * size + (z & s)];
   }
}

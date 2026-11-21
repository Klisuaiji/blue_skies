package com.legacy.blue_skies.world.biome_provider.region;

import com.legacy.blue_skies.world.biome_provider.provider.AbstractBiomeProvider;
import java.util.Random;

public abstract class AbstractRegion<T> {
   public static final int BLOCK_WIDTH = 1024;
   public static final double BLOCK_WIDTH_D = 1024.0;
   public static final double CHUNK_WIDTH_D = 64.0;
   public final AbstractBiomeProvider biomeProvider;
   public final long levelSeed;
   public final int regionX;
   public final int regionZ;
   protected int scale;
   protected int scaleMinusOne;
   private boolean baked = false;

   public AbstractRegion(AbstractBiomeProvider biomeProvider, int regionX, int regionZ, int scale) {
      this.biomeProvider = biomeProvider;
      this.levelSeed = (biomeProvider.seed == 0L ? 1L : biomeProvider.seed) * 643001449L;
      this.regionX = regionX;
      this.regionZ = regionZ;
      this.setScale(scale);
   }

   public int getScale() {
      return this.scale;
   }

   public void setScale(int scale) {
      this.scale = scale;
      this.scaleMinusOne = scale - 1;
   }

   public abstract int[][] getData();

   public int getGlobalX(int x) {
      return this.toGlobalValue(x, this.regionX);
   }

   public int getGlobalZ(int z) {
      return this.toGlobalValue(z, this.regionZ);
   }

   protected int toGlobalValue(int val, int regionVal) {
      int scale = this.scale / 2;
      return regionVal * scale + val;
   }

   protected Random getRand(long seedModifier, int x, int z) {
      return new Random((this.levelSeed + seedModifier) * ((long)this.getGlobalX(x) << 32 | this.getGlobalZ(z) & 4294967295L));
   }

   public int getDirect(int x, int z) {
      return this.getData()[x][z];
   }

   public int get(int x, int z) {
      return this.getData()[x < 0 ? 0 : (x >= this.scale ? this.scaleMinusOne : x)][z < 0 ? 0 : (z >= this.scale ? this.scaleMinusOne : z)];
   }

   public int getLeft(int x, int z) {
      return this.get(x - 1, z);
   }

   public int getRight(int x, int z) {
      return this.get(x + 1, z);
   }

   public int getDown(int x, int z) {
      return this.get(x, z - 1);
   }

   public int getUp(int x, int z) {
      return this.get(x, z + 1);
   }

   public void bake() {
      if (!this.baked) {
         this.bakeData();
         this.baked = true;
      }
   }

   protected abstract void bakeData();

   public void transform(T func) {
      this.transform(func, 0L);
   }

   public abstract void transform(T var1, long var2);
}

package com.legacy.blue_skies.world.biome_provider;

import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.provider.AbstractBiomeProvider;
import com.legacy.blue_skies.world.biome_provider.provider.EverbrightBiomeProvider;
import com.legacy.blue_skies.world.biome_provider.provider.EverdawnBiomeProvider;
import com.legacy.blue_skies.world.biome_provider.region.BakedWorldRegion;
import com.mojang.datafixers.util.Pair;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import javax.swing.JFrame;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.WorldgenRandom.Algorithm;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class BlueSkiesBiomeProviderTest {
   private static BlueSkiesBiomeProviderTest.RunMode runMode = BlueSkiesBiomeProviderTest.RunMode.PREVIEW;
   private static BlueSkiesBiomeProviderTest.DrawMode drawMode = BlueSkiesBiomeProviderTest.DrawMode.BIOMES;
   private static BlueSkiesBiomeProviderTest.Dimension dimension = BlueSkiesBiomeProviderTest.Dimension.EVERBRIGHT;
   private static Long seed = 4L;
   private static int minY = 0;
   private static int maxY = 255;
   private static int zoomAmount = 1;
   private static final Map<Pair<BlueSkiesBiomeProviderTest.Dimension, Long>, AbstractBiomeProvider> PROVIDER_CACHE = new ConcurrentHashMap<>();
   private static final BiFunction<BlueSkiesBiomeProviderTest.Dimension, Long, AbstractBiomeProvider> PROVIDERS = new BiFunction<BlueSkiesBiomeProviderTest.Dimension, Long, AbstractBiomeProvider>() {
      public AbstractBiomeProvider apply(BlueSkiesBiomeProviderTest.Dimension dimension, Long seed) {
         return BlueSkiesBiomeProviderTest.PROVIDER_CACHE.computeIfAbsent(Pair.of(dimension, seed), cacheKey -> {
            AbstractBiomeProvider prov = switch ((BlueSkiesBiomeProviderTest.Dimension)cacheKey.getFirst()) {
               case EVERBRIGHT -> new EverbrightBiomeProvider(BlueSkiesBiomeProviderTest.minY, BlueSkiesBiomeProviderTest.maxY);
               case EVERDAWN -> new EverdawnBiomeProvider(BlueSkiesBiomeProviderTest.minY, BlueSkiesBiomeProviderTest.maxY);
               default -> throw new IllegalStateException("Invalid dimension id: " + cacheKey.getFirst());
            };
            prov.setSeed((Long)cacheKey.getSecond());
            return prov;
         });
      }
   };

   public static void main(String[] args) {
      SharedConstants.tryDetectVersion();
      Bootstrap.bootStrap();
      if (seed == null) {
         seed = new Random().nextLong();
      }

      System.out.println("Blue Skies: Population");
      System.out.println("--- Settings ---");
      System.out.println("  Seed:      " + seed);
      System.out.println("  Run Mode:  " + runMode);
      System.out.println("  Draw Mode: " + drawMode);
      System.out.println("  Dimension: " + dimension);
      System.out.println("");
      System.out.println("--- Controls ---");
      System.out.println("  Navigate with the arrow keys");
      System.out.println("  Press [+] to zoom in or [-] to zoom out");
      System.out.println("  Press [Space] to change draw mode");
      System.out.println("  Press [D] to change dimensions");
      System.out.println("  Press [R] to reset the biome providers");
      if (runMode == BlueSkiesBiomeProviderTest.RunMode.PREVIEW) {
         BlueSkiesBiomeProviderTest.BiomeJFrame frame = new BlueSkiesBiomeProviderTest.BiomeJFrame(seed);
         int size = 1500;
         frame.setSize(size, size);
         frame.setVisible(true);
         frame.setDefaultCloseOperation(3);
         frame.addKeyListener(frame);
         frame.addMouseListener(frame);
      } else if (runMode == BlueSkiesBiomeProviderTest.RunMode.BENCHMARK) {
         int runs = 100;
         System.out.println("\nRunning benchmark for " + runs + " regions...");
         AbstractBiomeProvider biomeProv = PROVIDERS.apply(dimension, seed);
         long totalTime = 0L;

         for (int i = 0; i < runs; i++) {
            long startTime = System.currentTimeMillis();
            BakedWorldRegion region = biomeProv.getWorldRegion(new ChunkPos(i * 64, 0));
            long endTime = System.currentTimeMillis();
            totalTime += endTime - startTime;
            System.out.println((int)((float)i / runs * 100.0F) + "% " + (int)((float)totalTime / (i + 1)) + "ms current average");
         }

         System.out.println(runs + " Regions took " + (int)((float)totalTime / runs) + "ms on average");
      } else if (runMode == BlueSkiesBiomeProviderTest.RunMode.EXPERIMENTAL) {
         BlueSkiesBiomeProviderTest.NoiseJFrame frame = new BlueSkiesBiomeProviderTest.NoiseJFrame();
         int size = 900;
         frame.setSize(size, size);
         frame.setVisible(true);
         frame.setDefaultCloseOperation(3);
      }
   }

   public static class BiomeJFrame extends JFrame implements KeyListener, MouseListener {
      private static final long serialVersionUID = -7526607779010180920L;
      public AbstractBiomeProvider biomeProv;
      private int sliceZ = 100;
      private int xOffsetAmount = 0;
      private int zOffsetAmount = 0;

      public BiomeJFrame(long seed) {
         super("seed: " + seed);
         this.biomeProv = BlueSkiesBiomeProviderTest.PROVIDERS.apply(BlueSkiesBiomeProviderTest.dimension, seed);
      }

      @Override
      public void paint(Graphics g) {
         Map<Integer, Color> biomes = new HashMap<>();
         biomes.put(BiomeIds.FROZ, Color.white);
         biomes.put(BiomeIds.NEUT, Color.green);
         biomes.put(BiomeIds.HOTT, Color.red);
         biomes.put(BiomeIds.PLAINS, Color.green);
         biomes.put(BiomeIds.DEEP_OCEAN, new Color(0, 0, 180));
         biomes.put(BiomeIds.OCEAN, new Color(0, 0, 255));
         biomes.put(BiomeIds.CALMING_SKIES, new Color(71, 202, 255));
         biomes.put(BiomeIds.BRIGHTLANDS, new Color(191, 226, 245));
         biomes.put(BiomeIds.SLUSHLANDS, new Color(64, 102, 183));
         biomes.put(BiomeIds.FROSTBITTEN_FOREST, new Color(81, 66, 53));
         biomes.put(BiomeIds.FROSTBITTEN_FOREST_CLEARING, new Color(145, 119, 94));
         biomes.put(BiomeIds.PEEKING_OCEAN, new Color(0, 0, 255));
         biomes.put(BiomeIds.DEEP_PEEKING_OCEAN, new Color(0, 0, 180));
         biomes.put(BiomeIds.BRUMBLE_FOREST, new Color(50, 0, 180));
         biomes.put(BiomeIds.MIDDAY_SHORE, new Color(7, 0, 50));
         biomes.put(BiomeIds.SNOW_COVERED_PINES, new Color(206, 173, 255));
         biomes.put(BiomeIds.BRISK_MEADOW, new Color(108, 94, 255));
         biomes.put(BiomeIds.POLAR_HIGHLAND, new Color(43, 181, 144));
         biomes.put(BiomeIds.UNORTHODOX_VALLEY, new Color(110, 48, 255));
         biomes.put(BiomeIds.SHADED_WOODLANDS, new Color(183, 117, 255));
         biomes.put(BiomeIds.CRYSTAL_DUNES, new Color(241, 216, 255));
         biomes.put(BiomeIds.CRYSTAL_DUNES_SPIKES, new Color(157, 140, 165));
         biomes.put(BiomeIds.SUNSET_MAPLE_FOREST, new Color(188, 88, 67));
         biomes.put(BiomeIds.RISING_CREEK, new Color(37, 55, 219));
         biomes.put(BiomeIds.RISING_CREEK_EDGE, new Color(43, 60, 245));
         biomes.put(BiomeIds.MOONLIT_RESERVOIR, new Color(75, 155, 160));
         biomes.put(BiomeIds.CRYSTAL_ROUGHS, new Color(88, 90, 206));
         biomes.put(BiomeIds.SEARING_GRASSLAND, new Color(183, 40, 66));
         biomes.put(BiomeIds.CRESCENT_ORCHARD, new Color(79, 22, 112));
         biomes.put(BiomeIds.CRESCENT_ORCHARD_LAKE, new Color(138, 39, 196));
         int xOffset = 512 * this.xOffsetAmount;
         int zOffset = 512 * this.zOffsetAmount;

         for (int z = 0 + zOffset; z <= this.getHeight() + zOffset; z++) {
            for (int x = 0 + xOffset; x <= this.getWidth() + xOffset; x++) {
               int px = x * BlueSkiesBiomeProviderTest.zoomAmount;
               int pz = z * BlueSkiesBiomeProviderTest.zoomAmount;
               int chunkX = px >> 4;
               int chunkZ = pz >> 4;
               BakedWorldRegion region = this.biomeProv.getWorldRegion(new ChunkPos(chunkX, chunkZ));
               if (region != null) {
                  if (BlueSkiesBiomeProviderTest.drawMode == BlueSkiesBiomeProviderTest.DrawMode.BIOMES) {
                     int biome = region.getBiome(px, pz);
                     Color color = biomes.get(biome);
                     if (color == null) {
                        color = Color.black;
                        System.out.println("Undefined color: " + biome);
                     }

                     g.setColor(color);
                     g.fillRect(x - xOffset, z - zOffset, 1, 1);
                  }

                  if (BlueSkiesBiomeProviderTest.drawMode == BlueSkiesBiomeProviderTest.DrawMode.TOPOGRAPHY) {
                     int rawHeight = region.getHeight(px, pz, this.biomeProv);
                     int height = rawHeight / 10 * 10;
                     Color color = Color.WHITE;
                     if (rawHeight <= 62) {
                        color = new Color(0, 0, (int)(height / 62.0 * 255.0));
                     } else {
                        int c = (int)((height - 60.0) / 195.0 * 255.0);
                        color = new Color(c, c, c);
                     }

                     g.setColor(color);
                     g.fillRect(x - xOffset, z - zOffset, 1, 1);
                  }

                  if (BlueSkiesBiomeProviderTest.drawMode == BlueSkiesBiomeProviderTest.DrawMode.GRAY_TOPOGRAPHY) {
                     int height = region.getHeight(px, pz, this.biomeProv);
                     Color color = new Color(height, height, height);
                     g.setColor(color);
                     g.fillRect(x - xOffset, z - zOffset, 1, 1);
                  }
               }
            }
         }

         int regionSize = 1024 / BlueSkiesBiomeProviderTest.zoomAmount;
         int top = this.sliceZ - regionSize * this.zOffsetAmount;
         int bottom = top + BlueSkiesBiomeProviderTest.maxY;
         g.setColor(Color.RED);
         g.fillRect(0, bottom, this.getWidth(), 1);
         g.fillRect(0, top, this.getWidth(), 1);
         g.setColor(Color.BLUE);
         g.fillRect(0, bottom - 62, this.getWidth(), 1);

         for (int x = 0 + xOffset; x < this.getWidth() + xOffset; x++) {
            int px = x * BlueSkiesBiomeProviderTest.zoomAmount;
            int height = this.biomeProv.getWorldRegion(new ChunkPos(px >> 4, this.sliceZ >> 4)).getHeight(px, this.sliceZ, this.biomeProv);
            g.setColor(Color.GREEN);
            g.fillRect(x - xOffset, bottom - height, 1, 1);
         }

         g.setColor(Color.black);
         g.drawRect(0, 0, regionSize, regionSize);
      }

      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
         int key = e.getKeyCode();
         if (key == 37) {
            this.xOffsetAmount--;
            this.repaint(0L, 0, 0, this.getWidth(), this.getHeight());
         }

         if (key == 39) {
            this.xOffsetAmount++;
            this.repaint(0L, 0, 0, this.getWidth(), this.getHeight());
         }

         if (key == 38) {
            this.zOffsetAmount--;
            this.repaint(0L, 0, 0, this.getWidth(), this.getHeight());
         }

         if (key == 40) {
            this.zOffsetAmount++;
            this.repaint(0L, 0, 0, this.getWidth(), this.getHeight());
         }

         if (key == 32) {
            int next = BlueSkiesBiomeProviderTest.drawMode.ordinal() + 1;
            if (next >= BlueSkiesBiomeProviderTest.DrawMode.values().length) {
               next = 0;
            }

            BlueSkiesBiomeProviderTest.drawMode = BlueSkiesBiomeProviderTest.DrawMode.values()[next];
            this.repaint(0L, 0, 0, this.getWidth(), this.getHeight());
         }

         if ((key == 61 || key == 107) && BlueSkiesBiomeProviderTest.zoomAmount > 1) {
            BlueSkiesBiomeProviderTest.zoomAmount--;
            this.repaint(0L, 0, 0, this.getWidth(), this.getHeight());
         }

         if ((key == 45 || key == 109) && BlueSkiesBiomeProviderTest.zoomAmount < 3) {
            BlueSkiesBiomeProviderTest.zoomAmount++;
            this.repaint(0L, 0, 0, this.getWidth(), this.getHeight());
         }

         if (key == 68) {
            int next = BlueSkiesBiomeProviderTest.dimension.ordinal() + 1;
            if (next >= BlueSkiesBiomeProviderTest.Dimension.values().length) {
               next = 0;
            }

            BlueSkiesBiomeProviderTest.dimension = BlueSkiesBiomeProviderTest.Dimension.values()[next];
            this.biomeProv = BlueSkiesBiomeProviderTest.PROVIDERS.apply(BlueSkiesBiomeProviderTest.dimension, BlueSkiesBiomeProviderTest.seed);
            this.repaint(0L, 0, 0, this.getWidth(), this.getHeight());
         }

         if (key == 82) {
            BlueSkiesBiomeProviderTest.PROVIDER_CACHE.clear();
            this.biomeProv = BlueSkiesBiomeProviderTest.PROVIDERS.apply(BlueSkiesBiomeProviderTest.dimension, BlueSkiesBiomeProviderTest.seed);
            this.repaint(0L, 0, 0, this.getWidth(), this.getHeight());
         }
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }

      @Override
      public void mouseClicked(MouseEvent e) {
         this.sliceZ = e.getY() + 1024 / BlueSkiesBiomeProviderTest.zoomAmount * this.zOffsetAmount;
         this.repaint(0L, 0, 0, this.getWidth(), this.getHeight());
      }

      @Override
      public void mousePressed(MouseEvent e) {
      }

      @Override
      public void mouseReleased(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
   }

   private enum Dimension {
      EVERBRIGHT,
      EVERDAWN;
   }

   private enum DrawMode {
      BIOMES,
      TOPOGRAPHY,
      GRAY_TOPOGRAPHY;
   }

   public static class NoiseJFrame extends JFrame {
      private static final long serialVersionUID = 1L;
      WorldgenRandom rand = new WorldgenRandom(Algorithm.XOROSHIRO.newInstance(0L));
      PerlinSimplexNoise perlinSimplex = new PerlinSimplexNoise(this.rand, List.of(0, 2, 7));
      SimplexNoise simplex = new SimplexNoise(this.rand);
      PerlinNoise perlin = PerlinNoise.create(this.rand, List.of(0));
      ImprovedNoise improved = new ImprovedNoise(this.rand);
      double scale = 0.01;

      @Override
      public void paint(Graphics g) {
         for (int z = 0; z <= this.getHeight(); z++) {
            for (int x = 0; x <= this.getWidth(); x++) {
               double val = 0.0;
               val = this.perlinSimplex.getValue(x * this.scale, z * this.scale, true);
               g.setColor(val < 0.0 ? Color.black : Color.white);
               g.fillRect(x, z, 1, 1);
            }
         }
      }
   }

   private enum RunMode {
      EXPERIMENTAL,
      PREVIEW,
      BENCHMARK;
   }
}

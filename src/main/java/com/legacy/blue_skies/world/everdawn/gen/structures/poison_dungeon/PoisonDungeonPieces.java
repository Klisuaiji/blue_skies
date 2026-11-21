package com.legacy.blue_skies.world.everdawn.gen.structures.poison_dungeon;

import com.google.common.collect.ImmutableList;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.block_entity.KeystoneBlockEntity;
import com.legacy.blue_skies.blocks.dungeon.PoisonKeystoneBlock;
import com.legacy.blue_skies.data.providers.SkiesLootProv;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.blue_skies.world.util.structure_processors.VinesProcessor;
import com.legacy.structure_gel.api.structure.GelTemplateStructurePiece;
import com.legacy.structure_gel.api.structure.IColoredBoundingBox;
import com.legacy.structure_gel.api.structure.processor.RandomBlockSwapProcessor;
import com.legacy.structure_gel.api.structure.processor.RemoveGelStructureProcessor;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class PoisonDungeonPieces {
   private static final ResourceLocation TREE = BlueSkies.locate("dungeons/everdawn/poison/tree");
   private static final ResourceLocation TUNNEL = BlueSkies.locate("dungeons/everdawn/poison/entry_tunnel");

   public static void assemble(
      StructureTemplateManager templateManager,
      BlockPos surfacePos,
      Rotation rotation,
      StructurePiecesBuilder pieces,
      RandomSource rand,
      RandomState randomState
   ) {
      List<PoisonDungeonPieces.Floor> floors = new ArrayList<>();
      int y = 45;
      if (surfacePos.getY() < 60) {
         surfacePos = new BlockPos(surfacePos.getX(), 60, surfacePos.getZ());
      }

      floors.add(new PoisonDungeonPieces.Floor(0, new BlockPos(surfacePos.getX(), y, surfacePos.getZ()), Pair.of(8, 8), rand));

      for (int i = 0; i < 3; i++) {
         floors.add(new PoisonDungeonPieces.Floor(i + 1, new BlockPos(surfacePos.getX(), y - (i + 1) * 10, surfacePos.getZ()), floors.get(i).exitPair, rand));
      }

      floors.add(
         new PoisonDungeonPieces.BossFloor(
            floors.size(), new BlockPos(surfacePos.getX(), y - floors.size() * 10, surfacePos.getZ()), floors.get(floors.size() - 1).exitPair, rand
         )
      );
      floors.forEach(floor -> floor.addPieces(pieces, templateManager));

      for (int i = y + 5; i <= surfacePos.getY(); i++) {
         pieces.addPiece(new PoisonDungeonPieces.Piece(templateManager, new BlockPos(surfacePos.getX() + 2, i, surfacePos.getZ() + 2), TUNNEL, rand));
      }

      pieces.addPiece(new PoisonDungeonPieces.Piece(templateManager, surfacePos.offset(-6, 0, -7), TREE, rand));
   }

   public static class BossFloor extends PoisonDungeonPieces.Floor {
      public BossFloor(int level, BlockPos worldPos, Pair<Integer, Integer> center, RandomSource rand) {
         super(level, worldPos, center, rand);
      }

      @Override
      public void init(Pair<Integer, Integer> center) {
         int middle = 8;
         Pair<Integer, Integer> bossPos = Pair.of(middle, middle);
         if ((Integer)center.getFirst() >= (Integer)bossPos.getFirst() - 3
            && (Integer)center.getFirst() <= (Integer)bossPos.getFirst() + 3
            && (Integer)center.getSecond() >= (Integer)bossPos.getSecond() - 3
            && (Integer)center.getSecond() <= (Integer)bossPos.getSecond() + 3) {
            bossPos = Pair.of(middle + 5, middle + 5);
         }

         this.set((Integer)center.getFirst(), (Integer)center.getSecond(), new PoisonDungeonPieces.Intersection.Cross());
         this.connect((Integer)center.getFirst(), (Integer)center.getSecond(), (Integer)bossPos.getFirst() + 1, (Integer)bossPos.getSecond() + 1);

         for (int x = (Integer)bossPos.getFirst(); x < bossPos.getFirst() + 3; x++) {
            for (int z = (Integer)bossPos.getSecond(); z < bossPos.getSecond() + 3; z++) {
               this.set(x, z, null);
            }
         }

         this.set((Integer)bossPos.getFirst(), (Integer)bossPos.getSecond(), new PoisonDungeonPieces.BossRoom(Rotation.NONE));
         List<Direction> conns = this.get((Integer)bossPos.getFirst(), (Integer)bossPos.getSecond())
            .getSurroundingConnections((Integer)bossPos.getFirst(), (Integer)bossPos.getSecond(), this);
         Rotation rot = Rotation.NONE;
         if (conns.contains(Direction.SOUTH)) {
            rot = Rotation.CLOCKWISE_180;
         } else if (conns.contains(Direction.EAST)) {
            rot = Rotation.CLOCKWISE_90;
         } else if (conns.contains(Direction.WEST)) {
            rot = Rotation.COUNTERCLOCKWISE_90;
         }

         this.set((Integer)bossPos.getFirst(), (Integer)bossPos.getSecond(), new PoisonDungeonPieces.BossRoom(rot));
         this.updateIntersections();
         this.changeToEntry(center);
      }

      @Override
      public void connect(int x, int z, int x1, int z1) {
         if (Math.abs(x - x1) < Math.abs(z - z1)) {
            super.connect(x, z, x1, z);
            super.connect(x1, z, x1, z1);
         } else {
            super.connect(x, z, x, z1);
            super.connect(x, z1, x1, z1);
         }
      }
   }

   public static class BossRoom extends PoisonDungeonPieces.Segment {
      public BossRoom(Rotation rotation) {
         this.rotation = rotation;
      }

      @Override
      protected void initPieces() {
         this.addPieces(4, "boss_room");
      }

      @Override
      public List<Direction> getConnections() {
         return List.of(this.rotation.rotate(Direction.NORTH));
      }

      @Override
      public List<Direction> getSurroundingConnections(int x, int z, PoisonDungeonPieces.Floor floor) {
         List<Direction> directions = new ArrayList<>();
         if (x > 0 && z < 15 && floor.get(x - 1, z + 1) != null && floor.get(x - 1, z + 1).getConnections().contains(Direction.EAST)) {
            directions.add(Direction.WEST);
         }

         if (x < 13 && z > 0 && floor.get(x + 3, z + 1) != null && floor.get(x + 3, z + 1).getConnections().contains(Direction.WEST)) {
            directions.add(Direction.EAST);
         }

         if (x < 15 && z > 0 && floor.get(x + 1, z - 1) != null && floor.get(x + 1, z - 1).getConnections().contains(Direction.SOUTH)) {
            directions.add(Direction.NORTH);
         }

         if (x < 15 && z < 13 && floor.get(x + 1, z + 3) != null && floor.get(x + 1, z + 3).getConnections().contains(Direction.NORTH)) {
            directions.add(Direction.SOUTH);
         }

         return directions;
      }
   }

   public static class Floor {
      public List<List<PoisonDungeonPieces.Segment>> map = new ArrayList<>();
      public final BlockPos worldPos;
      public Pair<Integer, Integer> exitPair = Pair.of(8, 8);
      public final RandomSource rand;
      public final int level;
      public static final int size = 16;

      public Floor(int level, BlockPos worldPos, Pair<Integer, Integer> center, RandomSource rand) {
         this.level = level;
         this.worldPos = worldPos;
         this.rand = rand;

         for (int x = 0; x < 16; x++) {
            List<PoisonDungeonPieces.Segment> subList = new ArrayList<>(16);

            for (int z = 0; z < 16; z++) {
               subList.add(null);
            }

            this.map.add(subList);
         }

         this.init(center);
      }

      public void init(Pair<Integer, Integer> center) {
         this.set((Integer)center.getFirst(), (Integer)center.getSecond(), new PoisonDungeonPieces.Intersection.Cross());
         List<Pair<Integer, Integer>> firstBranches = new ArrayList<>();

         for (int i = 0; i < this.rand.nextInt(2) + 3; i++) {
            firstBranches.add(Pair.of(this.rand.nextInt(16), this.rand.nextInt(16)));
         }

         firstBranches.forEach(
            pair -> this.connect((Integer)center.getFirst(), (Integer)center.getSecond(), (Integer)pair.getFirst(), (Integer)pair.getSecond())
         );

         for (int i = 0; i < this.rand.nextInt(5) + 6; i++) {
            Pair<Integer, Integer> from = this.getRandomSegmentPos();
            Pair<Integer, Integer> to = this.randomPair();
            this.connect((Integer)from.getFirst(), (Integer)from.getSecond(), (Integer)to.getFirst(), (Integer)to.getSecond());
         }

         this.updateIntersections();
         this.changeToEntry(center);
         List<Pair<Integer, Integer>> deadEnds = new ArrayList<>();
         List<Pair<Integer, Integer>> crosses = new ArrayList<>();
         List<Pair<Integer, Integer>> anyOfThem = new ArrayList<>();

         for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
               PoisonDungeonPieces.Segment seg = this.get(x, z);
               if (seg != null && seg instanceof PoisonDungeonPieces.Intersection) {
                  if (!Pair.of(x, z).equals(center)) {
                     if (seg instanceof PoisonDungeonPieces.Intersection.End) {
                        deadEnds.add(Pair.of(x, z));
                     } else if (seg instanceof PoisonDungeonPieces.Intersection.Cross) {
                        crosses.add(Pair.of(x, z));
                     }
                  } else {
                     anyOfThem.add(Pair.of(x, z));
                  }
               }
            }
         }

         if (!deadEnds.isEmpty()) {
            this.exitPair = deadEnds.get(this.rand.nextInt(deadEnds.size()));
            this.set(
               (Integer)this.exitPair.getFirst(),
               (Integer)this.exitPair.getSecond(),
               new PoisonDungeonPieces.Stairway.End(this.get((Integer)this.exitPair.getFirst(), (Integer)this.exitPair.getSecond()).rotation)
            );
         } else {
            if (!crosses.isEmpty()) {
               this.exitPair = crosses.get(this.rand.nextInt(crosses.size()));
            } else {
               this.exitPair = anyOfThem.get(this.rand.nextInt(anyOfThem.size()));
            }

            this.set(
               (Integer)this.exitPair.getFirst(),
               (Integer)this.exitPair.getSecond(),
               new PoisonDungeonPieces.Stairway.Cross(this.get((Integer)this.exitPair.getFirst(), (Integer)this.exitPair.getSecond()).rotation)
            );
         }

         List<Pair<Integer, Integer>> intersections = new ArrayList<>();

         for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
               PoisonDungeonPieces.Segment checking = this.get(x, z);
               if (checking != null && checking instanceof PoisonDungeonPieces.Intersection && !((PoisonDungeonPieces.Intersection)checking).entry) {
                  intersections.add(Pair.of(x, z));
               }
            }
         }

         intersections.forEach(p -> {
            if (this.rand.nextFloat() < 0.4F) {
               ((PoisonDungeonPieces.Intersection)this.get((Pair<Integer, Integer>)p)).setChest(true);
            }
         });
         PoisonDungeonPieces.Intersection keyRoom = (PoisonDungeonPieces.Intersection)this.get(intersections.get(this.rand.nextInt(intersections.size())));
         keyRoom.setKey(true);
         keyRoom.setChest(true);
      }

      public void changeToEntry(Pair<Integer, Integer> position) {
         PoisonDungeonPieces.Segment segOld = this.get((Integer)position.getFirst(), (Integer)position.getSecond());
         PoisonDungeonPieces.Segment segNew = new PoisonDungeonPieces.Intersection.Cross(segOld.rotation, true);
         if (segOld != null) {
            if (segOld instanceof PoisonDungeonPieces.Intersection.End) {
               segNew = new PoisonDungeonPieces.Intersection.End(segOld.rotation, true);
            } else if (segOld instanceof PoisonDungeonPieces.Intersection.IShape) {
               segNew = new PoisonDungeonPieces.Intersection.IShape(segOld.rotation, true);
            } else if (segOld instanceof PoisonDungeonPieces.Intersection.LShape) {
               segNew = new PoisonDungeonPieces.Intersection.LShape(segOld.rotation, true);
            } else if (segOld instanceof PoisonDungeonPieces.Intersection.TShape) {
               segNew = new PoisonDungeonPieces.Intersection.TShape(segOld.rotation, true);
            }
         }

         this.set((Integer)position.getFirst(), (Integer)position.getSecond(), segNew);
      }

      public void updateIntersections() {
         for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
               if (this.get(x, z) != null) {
                  if (this.get(x, z) instanceof PoisonDungeonPieces.Intersection) {
                     List<Direction> conns = ((PoisonDungeonPieces.Intersection)this.get(x, z)).getSurroundingConnections(x, z, this);
                     switch (conns.size()) {
                        case 1:
                           Rotation rot = Rotation.NONE;
                           if (conns.contains(Direction.EAST)) {
                              rot = Rotation.CLOCKWISE_90;
                           } else if (conns.contains(Direction.SOUTH)) {
                              rot = Rotation.CLOCKWISE_180;
                           } else if (conns.contains(Direction.WEST)) {
                              rot = Rotation.COUNTERCLOCKWISE_90;
                           }

                           this.set(x, z, new PoisonDungeonPieces.Intersection.End(rot, false));
                           break;
                        case 2:
                           if (conns.containsAll(ImmutableList.of(Direction.NORTH, Direction.SOUTH))
                              && !conns.containsAll(ImmutableList.of(Direction.EAST, Direction.WEST))) {
                              this.set(
                                 x, z, new PoisonDungeonPieces.Intersection.IShape(this.rand.nextBoolean() ? Rotation.NONE : Rotation.CLOCKWISE_180, false)
                              );
                           } else if (!conns.containsAll(ImmutableList.of(Direction.NORTH, Direction.SOUTH))
                              && conns.containsAll(ImmutableList.of(Direction.EAST, Direction.WEST))) {
                              this.set(
                                 x,
                                 z,
                                 new PoisonDungeonPieces.Intersection.IShape(
                                    this.rand.nextBoolean() ? Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90, false
                                 )
                              );
                           }

                           for (Rotation rot : Rotation.values()) {
                              if (conns.containsAll(ImmutableList.of(rot.rotate(Direction.NORTH), rot.rotate(Direction.WEST)))) {
                                 this.set(x, z, new PoisonDungeonPieces.Intersection.LShape(rot, false));
                              }
                           }
                           break;
                        case 3:
                           Rotation rot = Rotation.NONE;
                           if (!conns.contains(Direction.WEST)) {
                              rot = Rotation.CLOCKWISE_90;
                           } else if (!conns.contains(Direction.NORTH)) {
                              rot = Rotation.CLOCKWISE_180;
                           } else if (!conns.contains(Direction.EAST)) {
                              rot = Rotation.COUNTERCLOCKWISE_90;
                           }

                           this.set(x, z, new PoisonDungeonPieces.Intersection.TShape(rot, false));
                     }
                  } else if (this.get(x, z) instanceof PoisonDungeonPieces.Hallway) {
                     this.set(
                        x,
                        z,
                        new PoisonDungeonPieces.Hallway(this.get(x, z).rotation.getRotated(this.rand.nextBoolean() ? Rotation.NONE : Rotation.CLOCKWISE_180))
                     );
                  }
               }
            }
         }
      }

      public Pair<Integer, Integer> getRandomSegmentPos() {
         PoisonDungeonPieces.Segment segment = null;
         Pair<Integer, Integer> pair = Pair.of(8, 8);

         while (segment == null || segment != null && segment instanceof PoisonDungeonPieces.BossRoom) {
            pair = this.randomPair();
            segment = this.get(pair);
         }

         return pair;
      }

      public Pair<Integer, Integer> randomPair() {
         return Pair.of(this.rand.nextInt(16), this.rand.nextInt(16));
      }

      @Nullable
      public PoisonDungeonPieces.Segment get(int x, int z) {
         return this.map.get(x).get(z);
      }

      @Nullable
      public PoisonDungeonPieces.Segment get(Pair<Integer, Integer> pair) {
         return this.get((Integer)pair.getFirst(), (Integer)pair.getSecond());
      }

      public void set(int x, int z, PoisonDungeonPieces.Segment segment) {
         this.map.get(x).set(z, segment);
      }

      public void connect(int x, int z, int x1, int z1) {
         this.set(x, z, new PoisonDungeonPieces.Intersection.Cross());
         if (this.get(x1, z1) == null || !(this.get(x1, z1) instanceof PoisonDungeonPieces.BossRoom)) {
            this.set(x1, z1, new PoisonDungeonPieces.Intersection.Cross());
         }

         if (this.rand.nextBoolean()) {
            for (int placements = 0; x != x1 && placements < 50; placements++) {
               if (x < x1) {
                  x++;
               }

               if (x > x1) {
                  x--;
               }

               if (this.get(x, z) == null) {
                  this.set(x, z, new PoisonDungeonPieces.Hallway(Rotation.CLOCKWISE_90));
               } else if (!this.get(x, z).equals(new PoisonDungeonPieces.Hallway(Rotation.CLOCKWISE_90))) {
                  this.set(x, z, new PoisonDungeonPieces.Intersection.Cross());
               }
            }

            if (x == x1) {
               this.set(x, z, new PoisonDungeonPieces.Intersection.Cross());
            }

            for (int placements = 0; z != z1 && placements < 50; placements++) {
               if (z < z1) {
                  z++;
               }

               if (z > z1) {
                  z--;
               }

               if (this.get(x, z) == null) {
                  this.set(x, z, new PoisonDungeonPieces.Hallway(Rotation.NONE));
               } else if (!this.get(x, z).equals(new PoisonDungeonPieces.Hallway(Rotation.NONE))) {
                  this.set(x, z, new PoisonDungeonPieces.Intersection.Cross());
               }
            }
         } else {
            for (int placements = 0; z != z1 && placements < 50; placements++) {
               if (z < z1) {
                  z++;
               }

               if (z > z1) {
                  z--;
               }

               if (this.get(x, z) == null) {
                  this.set(x, z, new PoisonDungeonPieces.Hallway(Rotation.NONE));
               } else if (!this.get(x, z).equals(new PoisonDungeonPieces.Hallway(Rotation.NONE))) {
                  this.set(x, z, new PoisonDungeonPieces.Intersection.Cross());
               }
            }

            if (z == z1) {
               this.set(x, z, new PoisonDungeonPieces.Intersection.Cross());
            }

            for (int placements = 0; x != x1 && placements < 50; placements++) {
               if (x < x1) {
                  x++;
               }

               if (x > x1) {
                  x--;
               }

               if (this.get(x, z) == null) {
                  this.set(x, z, new PoisonDungeonPieces.Hallway(Rotation.CLOCKWISE_90));
               } else if (!this.get(x, z).equals(new PoisonDungeonPieces.Hallway(Rotation.CLOCKWISE_90))) {
                  this.set(x, z, new PoisonDungeonPieces.Intersection.Cross());
               }
            }
         }
      }

      public void addPieces(StructurePiecesBuilder pieces, StructureTemplateManager templateManager) {
         for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
               PoisonDungeonPieces.Segment seg = this.get(x, z);
               if (seg != null) {
                  int yOffset = seg instanceof PoisonDungeonPieces.Stairway ? -5 : 0;
                  int xOffset = seg instanceof PoisonDungeonPieces.BossRoom ? -2 : 0;
                  pieces.addPiece(
                     new PoisonDungeonPieces.Piece(
                        templateManager,
                        new BlockPos(this.worldPos.getX() + (x - 8) * 9 + xOffset, this.worldPos.getY() + yOffset, this.worldPos.getZ() + (z - 8) * 9),
                        seg,
                        this.level,
                        this.rand
                     )
                  );
               }
            }
         }
      }
   }

   public static class Hallway extends PoisonDungeonPieces.Segment {
      public Hallway(Rotation rotation) {
         this.rotation = rotation;
      }

      @Override
      protected void initPieces() {
         for (int i = 0; i < 4; i++) {
            this.addPieces(i, "hallway/thin_" + i, "hallway/thick_" + i);
         }

         this.addPieces(4, "hallway/thick_4");
      }

      @Override
      public List<Direction> getConnections() {
         return ImmutableList.of(this.rotation.rotate(Direction.NORTH), this.rotation.rotate(Direction.SOUTH));
      }
   }

   public abstract static class Intersection extends PoisonDungeonPieces.Segment {
      public boolean hasConnection = false;
      public boolean entry = false;
      public boolean chestRoom = false;
      public boolean key = false;
      public String type = "cross";

      @Override
      protected void initPieces() {
         String prefix = "intersection/" + (this.entry ? "entry/" : (this.chestRoom ? "room/" : "")) + this.type;

         for (int floor = 0; floor < 5; floor++) {
            this.addPieces(floor, prefix + (this.chestRoom && floor < 4 ? "_chest_" : "_") + floor);
         }
      }

      public void setChest(boolean hasChest) {
         this.chestRoom = hasChest;
      }

      public void setKey(boolean key) {
         this.key = key;
      }

      public static class Cross extends PoisonDungeonPieces.Intersection {
         public Cross() {
            this(Rotation.NONE, false);
         }

         public Cross(Rotation rotation, boolean entry) {
            this.rotation = rotation;
            this.entry = entry;
         }

         @Override
         public List<Direction> getConnections() {
            return ImmutableList.of(Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH);
         }
      }

      public static class End extends PoisonDungeonPieces.Intersection {
         public End(Rotation rotation, boolean entry) {
            this.rotation = rotation;
            this.entry = entry;
            this.type = "end";
         }

         @Override
         public List<Direction> getConnections() {
            return ImmutableList.of(this.rotation.rotate(Direction.NORTH));
         }
      }

      public static class IShape extends PoisonDungeonPieces.Intersection {
         public IShape(Rotation rotation, boolean entry) {
            this.rotation = rotation;
            this.entry = entry;
            this.type = "i";
         }

         @Override
         public List<Direction> getConnections() {
            return ImmutableList.of(this.rotation.rotate(Direction.NORTH), this.rotation.rotate(Direction.SOUTH));
         }
      }

      public static class LShape extends PoisonDungeonPieces.Intersection {
         public LShape(Rotation rotation, boolean entry) {
            this.rotation = rotation;
            this.entry = entry;
            this.type = "l";
         }

         @Override
         public List<Direction> getConnections() {
            return ImmutableList.of(this.rotation.rotate(Direction.NORTH), this.rotation.rotate(Direction.WEST));
         }
      }

      public static class TShape extends PoisonDungeonPieces.Intersection {
         public TShape(Rotation rotation, boolean entry) {
            this.rotation = rotation;
            this.entry = entry;
            this.type = "t";
         }

         @Override
         public List<Direction> getConnections() {
            return ImmutableList.of(this.rotation.rotate(Direction.NORTH), this.rotation.rotate(Direction.EAST), this.rotation.rotate(Direction.WEST));
         }
      }
   }

   public static class Piece extends GelTemplateStructurePiece implements IColoredBoundingBox {
      private final boolean hasKey;

      public Piece(StructureTemplateManager templateManager, BlockPos pos, PoisonDungeonPieces.Segment segment, int level, RandomSource rand) {
         super((StructurePieceType)SkiesStructures.POISON_DUNGEON.getPieceType().get(), 0, templateManager, segment.getRandom(level, rand), pos);
         this.hasKey = segment instanceof PoisonDungeonPieces.Intersection ? ((PoisonDungeonPieces.Intersection)segment).key : false;
         this.rotation = segment.rotation;
         this.templatePosition = pos;
         this.setupPlaceSettings(templateManager);
      }

      public Piece(StructureTemplateManager templateManager, BlockPos pos, ResourceLocation name, RandomSource rand) {
         super((StructurePieceType)SkiesStructures.POISON_DUNGEON.getPieceType().get(), 0, templateManager, name, pos);
         this.hasKey = false;
         this.rotation = Rotation.NONE;
         this.templatePosition = pos;
         this.setupPlaceSettings(templateManager);
      }

      public Piece(StructurePieceSerializationContext context, CompoundTag nbt) {
         super((StructurePieceType)SkiesStructures.POISON_DUNGEON.getPieceType().get(), nbt, context.structureTemplateManager());
         this.hasKey = nbt.getBoolean("HasKey");
         this.setupPlaceSettings(context.structureTemplateManager());
      }

      protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
         super.addAdditionalSaveData(context, nbt);
         nbt.putBoolean("HasKey", this.hasKey);
      }

      public StructurePlaceSettings getPlaceSettings(StructureTemplateManager templateManager) {
         Vec3i sizePos = ((StructureTemplate)templateManager.get(this.makeTemplateLocation()).get()).getSize();
         BlockPos centerPos = new BlockPos(sizePos.getX() / 2, 0, sizePos.getZ() / 2);
         StructurePlaceSettings settings = new StructurePlaceSettings().setKeepLiquids(false).setRotation(this.rotation).setRotationPivot(centerPos);
         settings.addProcessor(RemoveGelStructureProcessor.INSTANCE);
         settings.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
         settings.addProcessor(new VinesProcessor());
         String key = this.templateName;
         if (key.endsWith("_0")) {
            for (Block block : new Block[]{SkiesBlocks.poison_stone_slab, SkiesBlocks.poison_stone_stairs}) {
               settings.addProcessor(new RandomBlockSwapProcessor(block, 0.5F, Blocks.AIR));
            }

            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.poison_stone, 0.02F, SkiesBlocks.glowing_poison_stone));
         } else if (key.endsWith("_1")) {
            for (Block block : new Block[]{
               SkiesBlocks.poison_stone_slab, SkiesBlocks.poison_stone_stairs, SkiesBlocks.poison_stonebrick_slab, SkiesBlocks.poison_stonebrick_stairs
            }) {
               settings.addProcessor(new RandomBlockSwapProcessor(block, 0.5F, Blocks.AIR));
            }

            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.poison_stone, 0.02F, SkiesBlocks.glowing_poison_stone));
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.poison_stone, 0.3F, SkiesBlocks.rough_poison_stonebrick));
         } else if (key.endsWith("_2")) {
            for (Block block : new Block[]{
               SkiesBlocks.poison_stone_slab, SkiesBlocks.poison_stone_stairs, SkiesBlocks.poison_stonebrick_slab, SkiesBlocks.poison_stonebrick_stairs
            }) {
               settings.addProcessor(new RandomBlockSwapProcessor(block, 0.5F, Blocks.AIR));
            }

            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.poison_stone, 0.02F, SkiesBlocks.glowing_poison_stone));
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.poison_stone, 0.75F, SkiesBlocks.rough_poison_stonebrick));
         } else if (key.endsWith("_3")) {
            settings.addProcessor(new RandomBlockSwapProcessor(Blocks.GLOWSTONE, 0.5F, SkiesBlocks.rough_poison_stonebrick));
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.poison_stonebrick, 0.5F, SkiesBlocks.rough_poison_stonebrick));
            if (!key.contains("_chest")) {
               settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.spider_webbing, 0.8F, Blocks.AIR));
            }
         } else if (key.endsWith("_4") && !key.contains("_chest")) {
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.spider_webbing, 0.8F, Blocks.AIR));
         }

         if (this.makeTemplateLocation() == PoisonDungeonPieces.TUNNEL) {
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.lunar_stone, 0.8F, SkiesBlocks.lunar_cobblestone));
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.lunar_stone, 0.5F, SkiesBlocks.poison_stone));
         }

         if (key.contains("_chest")) {
            settings.addProcessor(new RandomBlockSwapProcessor(Blocks.COBWEB, 0.3F, Blocks.AIR));
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.spider_webbing, 0.3F, Blocks.AIR));
         }

         if (key.contains("boss_room")) {
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.poison_stonebrick, 0.08F, SkiesBlocks.rough_poison_stonebrick));
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.poison_stone, 0.08F, SkiesBlocks.glowing_poison_stone));
         }

         return settings;
      }

      protected void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, RandomSource rand, BoundingBox sbb) {
         if (key.contains("chest")) {
            BlockPos downPos = pos.below();
            if (sbb.isInside(downPos)) {
               RandomizableContainer.setBlockEntityLootTable(level, rand, downPos, this.hasKey ? SkiesLootProv.POISON_CHEST_KEY : SkiesLootProv.POISON_CHEST);
            }

            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
         }

         if (key.contains("spawner")) {
            level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);
            if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
               spawner.setEntityId(rand.nextBoolean() ? SkiesEntityTypes.VENOM_SPIDER : EntityType.CAVE_SPIDER, rand);
            }
         }

         if ("keystone".equals(key)) {
            level.setBlock(pos, SkiesBlocks.rough_poison_stonebrick.defaultBlockState(), 3);
            if (level.getBlockEntity(pos.above(2)) instanceof KeystoneBlockEntity.PoisonKeystoneBlockEntity keystone) {
               Direction dir = (Direction)level.getBlockState(pos.above(2)).getValue(PoisonKeystoneBlock.FACING);
               keystone.setTeleportOffset(new BlockPos(0, -1, 0).relative(dir, -3));
               keystone.setSpawnOffset(new BlockPos(0, -1, 0).relative(dir, -12));
            }
         }
      }

      public int getBoundingBoxColor() {
         return this.hasKey ? IColoredBoundingBox.RED : IColoredBoundingBox.GREEN;
      }
   }

   public abstract static class Segment {
      public List<Direction> connections = new ArrayList<>();
      public Map<Integer, List<ResourceLocation>> pieces = new HashMap<>();
      public Rotation rotation = Rotation.NONE;

      public ResourceLocation getRandom(int level, RandomSource rand) {
         this.initPieces();
         if (this.pieces.containsKey(level)) {
            return this.pieces.get(level).get(rand.nextInt(this.pieces.get(level).size()));
         } else if (this.pieces.entrySet().stream().findFirst().isPresent()) {
            Entry<Integer, List<ResourceLocation>> entry = this.pieces.entrySet().stream().findFirst().get();
            return entry.getValue().get(rand.nextInt(entry.getValue().size()));
         } else {
            BlueSkies.LOGGER.warn(String.format("Missing structure pieces for %s on floor %d", SkiesStructures.POISON_DUNGEON.getRegistryName(), level));
            return ResourceLocation.parse("empty");
         }
      }

      public List<Direction> getConnections() {
         return ImmutableList.of();
      }

      public List<Direction> getSurroundingConnections(int x, int z, PoisonDungeonPieces.Floor floor) {
         List<Direction> directions = new ArrayList<>();
         if (x > 0 && floor.get(x - 1, z) != null && floor.get(x - 1, z).getConnections().contains(Direction.EAST)) {
            directions.add(Direction.WEST);
         }

         if (x < 15 && floor.get(x + 1, z) != null && floor.get(x + 1, z).getConnections().contains(Direction.WEST)) {
            directions.add(Direction.EAST);
         }

         if (z > 0 && floor.get(x, z - 1) != null && floor.get(x, z - 1).getConnections().contains(Direction.SOUTH)) {
            directions.add(Direction.NORTH);
         }

         if (z < 15 && floor.get(x, z + 1) != null && floor.get(x, z + 1).getConnections().contains(Direction.NORTH)) {
            directions.add(Direction.SOUTH);
         }

         return directions;
      }

      protected void addPieces(int level, String... names) {
         this.pieces.put(level, Arrays.asList(names).stream().map(s -> BlueSkies.locate("dungeons/everdawn/poison/" + s)).collect(Collectors.toList()));
      }

      public boolean equals(PoisonDungeonPieces.Segment segment) {
         return this.rotation == segment.rotation && this.getClass() == segment.getClass();
      }

      protected abstract void initPieces();
   }

   public abstract static class Stairway extends PoisonDungeonPieces.Segment {
      public Stairway(Rotation rotation) {
         this.rotation = rotation;
      }

      public static class Cross extends PoisonDungeonPieces.Stairway {
         public Cross(Rotation rotation) {
            super(rotation);
         }

         @Override
         protected void initPieces() {
            for (int i = 0; i < 4; i++) {
               this.addPieces(i, "stairway/cross_" + i);
            }
         }
      }

      public static class End extends PoisonDungeonPieces.Stairway {
         public End(Rotation rotation) {
            super(rotation);
         }

         @Override
         protected void initPieces() {
            for (int i = 0; i < 4; i++) {
               this.addPieces(i, "stairway/end_" + i);
            }
         }
      }
   }
}

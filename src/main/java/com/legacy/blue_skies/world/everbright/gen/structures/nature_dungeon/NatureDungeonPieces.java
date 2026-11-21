package com.legacy.blue_skies.world.everbright.gen.structures.nature_dungeon;

import com.google.common.collect.ImmutableList;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.block_entity.KeystoneBlockEntity;
import com.legacy.blue_skies.blocks.dungeon.PoisonKeystoneBlock;
import com.legacy.blue_skies.blocks.natural.FruitLeavesBlock;
import com.legacy.blue_skies.data.providers.SkiesLootProv;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.blue_skies.util.GeometryHelper;
import com.legacy.blue_skies.util.MLSimplexNoise;
import com.legacy.blue_skies.util.StringUtil;
import com.legacy.blue_skies.world.util.structure_processors.VinesProcessor;
import com.legacy.structure_gel.api.structure.GelTemplateStructurePiece;
import com.legacy.structure_gel.api.structure.IColoredBoundingBox;
import com.legacy.structure_gel.api.structure.processor.RandomBlockSwapProcessor;
import com.legacy.structure_gel.api.structure.processor.RemoveGelStructureProcessor;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class NatureDungeonPieces {
   private static final ResourceLocation HALL_6 = locatePiece("hallways/hallway_6");
   private static final ResourceLocation HALL_7 = locatePiece("hallways/hallway_7");
   private static final ResourceLocation HALL_8 = locatePiece("hallways/hallway_8");
   private static final ResourceLocation HALL_9 = locatePiece("hallways/hallway_9");
   private static final Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> floor1Map = makeFloorMap(1, 20, 3);
   private static final Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> floor1EntryMap = makeEntryMap(1, 1);
   private static final List<ResourceLocation> floor1Layers = makeFloorLayer(1, 12);
   private static final Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> floor2Map = makeFloorMap(2, 20, 4);
   private static final Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> floor2EntryMap = makeEntryMap(2, 1);
   private static final Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> floor3Map = makeFloorMap(3, 18, 4);
   private static final Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> floor3EntryMap = makeEntryMap(3, 1);
   private static final Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> floor4Map = makeFloorMap(4, 18, 4);
   private static final Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> floor4EntryMap = makeEntryMap(4, 1);
   private static final ResourceLocation BOSS_HALL = locatePiece("boss/hallway");
   private static final ResourceLocation BOSS_ROOM = locatePiece("boss/boss_room");
   private static final ResourceLocation WALL_ENTRY = locatePiece("wall/floor_1_entry");
   private static final NatureDungeonPieces.Wall WALL_1 = new NatureDungeonPieces.Wall(1);
   private static final NatureDungeonPieces.Wall WALL_2 = new NatureDungeonPieces.Wall(2);
   private static final NatureDungeonPieces.Wall WALL_3 = new NatureDungeonPieces.Wall(3);
   private static final NatureDungeonPieces.Wall WALL_4 = new NatureDungeonPieces.Wall(4);
   private static final ResourceLocation STAIRS = locatePiece("entry_stairs");

   private static Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> makeFloorMap(int floor, int width, int variations) {
      Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> map = new HashMap<>();

      for (NatureDungeonPieces.Room.ConnectionType type : NatureDungeonPieces.Room.ConnectionType.values()) {
         List<ResourceLocation> pieces = new ArrayList<>();
         switch (type) {
            case EXIT:
               pieces.add(locatePiece(String.format("exits/floor_%d/exit_%d_0", floor, width)));
               break;
            default:
               for (int i = 0; i < variations; i++) {
                  pieces.add(locatePiece(String.format("rooms/floor_%d/room_%d_%s_%d", floor, width, type.toString(), i)));
               }
         }

         map.put(type, pieces);
      }

      return map;
   }

   private static Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> makeEntryMap(int floor, int variations) {
      Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> map = new HashMap<>();

      for (NatureDungeonPieces.Room.ConnectionType type : new NatureDungeonPieces.Room.ConnectionType[]{
         NatureDungeonPieces.Room.ConnectionType.CROSS,
         NatureDungeonPieces.Room.ConnectionType.T,
         NatureDungeonPieces.Room.ConnectionType.L,
         NatureDungeonPieces.Room.ConnectionType.I,
         NatureDungeonPieces.Room.ConnectionType.END
      }) {
         List<ResourceLocation> pieces = new ArrayList<>();

         for (int i = 0; i < variations; i++) {
            pieces.add(locatePiece(String.format("entry/floor_%d/entry_%s_%d", floor, type.toString(), i)));
         }

         map.put(type, pieces);
      }

      return map;
   }

   private static List<ResourceLocation> makeFloorLayer(int floor, int rooms) {
      List<ResourceLocation> list = new ArrayList<>();

      for (int i = 0; i < rooms; i++) {
         list.add(locatePiece(String.format("rooms/floor_%d/inside_%d", floor, i)));
      }

      return list;
   }

   public static ResourceLocation locatePiece(String path) {
      return BlueSkies.locate("dungeons/everbright/nature/" + path);
   }

   public static void assemble(
      StructureTemplateManager templateManager,
      BlockPos worldPos,
      Rotation rotation,
      StructurePiecesBuilder pieces,
      WorldgenRandom random,
      ChunkGenerator chunkGen,
      LevelHeightAccessor level,
      RandomState randomState
   ) {
      int f1Room = 20;
      int f1Hall = 9;
      int f2Room = 20;
      int f2Hall = 7;
      int f3Room = 18;
      int f3Hall = 8;
      int f4Room = 18;
      int f4Hall = 6;
      worldPos = worldPos.offset(-67, 0, -67);
      NatureDungeonPieces.FirstFloor floor1 = new NatureDungeonPieces.FirstFloor(
         1,
         f1Room,
         f1Hall,
         NatureDungeonPieces.Floor.potentialExits.get(random.nextInt(NatureDungeonPieces.Floor.potentialExits.size())),
         worldPos,
         random,
         HALL_9,
         floor1Map,
         floor1EntryMap,
         floor1Layers,
         WALL_1
      );
      NatureDungeonPieces.Floor floor2 = new NatureDungeonPieces.Floor(
         2, f2Room, f2Hall, floor1.exitPos, worldPos.offset(4, 15, 4), random, HALL_7, floor2Map, floor2EntryMap, WALL_2
      );
      NatureDungeonPieces.Floor floor3 = new NatureDungeonPieces.Floor(
         3, f3Room, f3Hall, floor2.exitPos, worldPos.offset(7, 30, 7), random, HALL_8, floor3Map, floor3EntryMap, WALL_3
      );
      NatureDungeonPieces.Floor floor4 = new NatureDungeonPieces.Floor(
         4, f4Room, f4Hall, floor3.exitPos, worldPos.offset(11, 45, 11), random, HALL_6, floor4Map, floor4EntryMap, WALL_4
      );
      NatureDungeonPieces.BossFloor bossFloor = new NatureDungeonPieces.BossFloor(floor4.exitPos, worldPos.offset(52, 60, 52), random);
      int fillHeight = 14;
      int f1Stone = f1Room * 5 + f1Hall * 4;
      int f2Stone = f2Room * 5 + f2Hall * 4;
      int f3Stone = f3Room * 5 + f3Hall * 4;
      int f4Stone = f4Room * 5 + f4Hall * 4;
      pieces.addPiece(new NatureDungeonPieces.AirBubblePiece(worldPos.offset(f1Stone / 2, 7, f1Stone / 2).relative(floor1.getEntryDir(), f1Stone / 2 + 5)));
      pieces.addPiece(new NatureDungeonPieces.StoneFillPiece(worldPos, new BlockPos(f1Stone, fillHeight, f1Stone), 0));
      pieces.addPiece(new NatureDungeonPieces.StoneFillPiece(worldPos.offset(4, 15, 4), new BlockPos(f2Stone, fillHeight, f2Stone), 0));
      pieces.addPiece(new NatureDungeonPieces.StoneFillPiece(worldPos.offset(7, 30, 7), new BlockPos(f3Stone, fillHeight, f3Stone), 0));
      pieces.addPiece(new NatureDungeonPieces.StoneFillPiece(worldPos.offset(11, 45, 11), new BlockPos(f4Stone, fillHeight - 3, f4Stone), 0));
      pieces.addPiece(new NatureDungeonPieces.StoneFillPiece(worldPos.offset(-5, -1, -5), new BlockPos(f1Stone + 9, 0, f1Stone + 9), 1));
      pieces.addPiece(new NatureDungeonPieces.GardenPiece(worldPos.offset(11, 56, 11)));
      bossFloor.addPieces(pieces, templateManager);
      floor4.addPieces(pieces, templateManager);
      floor3.addPieces(pieces, templateManager);
      floor2.addPieces(pieces, templateManager);
      floor1.addPieces(pieces, templateManager);
      floor1.addWalls(pieces, templateManager, chunkGen, level, randomState);
      floor2.addWalls(pieces, templateManager, chunkGen, level, randomState);
      floor3.addWalls(pieces, templateManager, chunkGen, level, randomState);
      floor4.addWalls(pieces, templateManager, chunkGen, level, randomState);
   }

   public static class AirBubblePiece extends StructurePiece {
      private static final int BOUND_WIDTH = 15;
      private static final int BOUND_HEIGHT = 25;

      protected AirBubblePiece(BlockPos pos) {
         super(
            (StructurePieceType)SkiesStructures.NATURE_DUNGEON.getPieceType("air_bubble").get(),
            0,
            BoundingBox.fromCorners(pos.offset(-15, -25, -15), pos.offset(15, 25, 15))
         );
      }

      public AirBubblePiece(StructurePieceSerializationContext context, CompoundTag nbt) {
         super((StructurePieceType)SkiesStructures.NATURE_DUNGEON.getPieceType("air_bubble").get(), nbt);
      }

      protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
      }

      public void postProcess(
         WorldGenLevel world,
         StructureManager structureManager,
         ChunkGenerator chunkGen,
         RandomSource rand,
         BoundingBox bounds,
         ChunkPos chunkPos,
         BlockPos pos
      ) {
         BlockPos center = this.boundingBox.getCenter();

         for (int x = -15; x < 15; x++) {
            for (int z = -15; z < 15; z++) {
               for (int y = -25; y < 25; y++) {
                  if (GeometryHelper.isInEllipsoid(15.0, 25.0, 15.0, x, y, z)) {
                     BlockPos placePos = center.offset(x, y, z);
                     if (bounds.isInside(placePos)) {
                        world.setBlock(placePos, Blocks.AIR.defaultBlockState(), 2);
                     }
                  }
               }
            }
         }
      }
   }

   protected static class BossFloor {
      private final Pair<Integer, Integer> entry;
      private final BlockPos worldPos;
      private final RandomSource rand;

      public BossFloor(Pair<Integer, Integer> entry, BlockPos worldPos, RandomSource random) {
         this.entry = entry;
         this.worldPos = worldPos;
         this.rand = random;
      }

      public void addPieces(StructurePiecesBuilder pieces, StructureTemplateManager templateManager) {
         Rotation rot;
         if (this.entry.equals(NatureDungeonPieces.Floor.potentialExits.get(0))) {
            rot = Rotation.COUNTERCLOCKWISE_90;
         } else if (this.entry.equals(NatureDungeonPieces.Floor.potentialExits.get(1))) {
            rot = Rotation.CLOCKWISE_90;
         } else if (this.entry.equals(NatureDungeonPieces.Floor.potentialExits.get(2))) {
            rot = Rotation.NONE;
         } else {
            rot = Rotation.CLOCKWISE_180;
         }
         pieces.addPiece(new NatureDungeonPieces.Piece(templateManager, this.worldPos.offset(switch (rot) {
            case COUNTERCLOCKWISE_90 -> new BlockPos(-29, 0, -4);
            case CLOCKWISE_90 -> new BlockPos(41, 0, -4);
            case NONE -> new BlockPos(6, 0, -39);
            default -> new BlockPos(6, 0, 31);
         }), NatureDungeonPieces.BOSS_HALL, 5, this.rand, rot));
         Direction.NORTH.getClockWise();
         pieces.addPiece(new NatureDungeonPieces.Piece(templateManager, this.worldPos, NatureDungeonPieces.BOSS_ROOM, 5, this.rand, rot));
      }
   }

   protected static class FirstFloor extends NatureDungeonPieces.Floor {
      private final List<ResourceLocation> layers;

      public FirstFloor(
         int floor,
         int roomWidth,
         int hallLength,
         Pair<Integer, Integer> entry,
         BlockPos worldPos,
         WorldgenRandom random,
         ResourceLocation hallway,
         Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> roomOptions,
         Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> entryOptions,
         List<ResourceLocation> layers,
         NatureDungeonPieces.Wall wall
      ) {
         super(floor, roomWidth, hallLength, entry, worldPos, random, hallway, roomOptions, entryOptions, wall);
         this.layers = layers;
      }

      @Override
      public void updateEntry() {
         this.setConnection((Integer)this.entryPos.getFirst(), (Integer)this.entryPos.getSecond(), this.getEntryDir(), true);
      }

      public Direction getEntryDir() {
         if (this.entryPos.equals(NatureDungeonPieces.Floor.potentialExits.get(0))) {
            return Direction.WEST;
         } else if (this.entryPos.equals(NatureDungeonPieces.Floor.potentialExits.get(1))) {
            return Direction.EAST;
         } else {
            return this.entryPos.equals(NatureDungeonPieces.Floor.potentialExits.get(2)) ? Direction.NORTH : Direction.SOUTH;
         }
      }

      public void addStairs(
         Direction entrySide,
         StructurePiecesBuilder pieces,
         StructureTemplateManager templateManager,
         ChunkGenerator chunkGen,
         LevelHeightAccessor level,
         RandomState randomState
      ) {
         Rotation rot;
         BlockPos offset;
         switch (entrySide) {
            case NORTH:
               rot = Rotation.CLOCKWISE_90;
               offset = new BlockPos(70, -3, -9);
               break;
            case SOUTH:
            default:
               rot = Rotation.COUNTERCLOCKWISE_90;
               offset = new BlockPos(65, -3, 144);
               break;
            case EAST:
               rot = Rotation.CLOCKWISE_180;
               offset = new BlockPos(144, -3, 70);
               break;
            case WEST:
               rot = Rotation.NONE;
               offset = new BlockPos(-9, -3, 65);
         }

         for (int i = 0; i < 10; i++) {
            int xOffset = entrySide.getAxis() == Axis.X ? i * 4 * entrySide.getAxisDirection().getStep() : 0;
            int zOffset = entrySide.getAxis() == Axis.Z ? i * 4 * entrySide.getAxisDirection().getStep() : 0;
            BlockPos stairPos = this.worldPos.offset(offset.offset(xOffset, i * -4, zOffset));
            pieces.addPiece(new NatureDungeonPieces.Piece(templateManager, stairPos, NatureDungeonPieces.STAIRS, this.floor, this.rand, rot, 2));
            if (stairPos.getY() < chunkGen.getBaseHeight(stairPos.getX(), stairPos.getZ(), Types.WORLD_SURFACE_WG, level, randomState)) {
               break;
            }
         }
      }

      @Override
      public void addRoom(
         int x,
         int z,
         int roomOffset,
         int xOffset,
         int zOffset,
         NatureDungeonPieces.Room room,
         StructureTemplateManager templateManager,
         StructurePiecesBuilder pieces
      ) {
         super.addRoom(x, z, roomOffset, xOffset, zOffset, room, templateManager, pieces);
         Pair<Integer, Integer> pos = Pair.of(x, z);
         if (!this.exitPos.equals(pos) && !this.entryPos.equals(pos)) {
            pieces.addPiece(
               new NatureDungeonPieces.Piece(
                  templateManager,
                  new BlockPos(this.worldPos.getX() + x * roomOffset + xOffset, this.worldPos.getY(), this.worldPos.getZ() + z * roomOffset + zOffset),
                  room,
                  this.floor,
                  this.rand,
                  this.layers.get(this.rand.nextInt(this.layers.size()))
               )
            );
         }
      }

      @Override
      public ResourceLocation getWallAddStairs(
         int i,
         Direction dir,
         StructurePiecesBuilder pieces,
         StructureTemplateManager templateManager,
         ChunkGenerator chunkGen,
         LevelHeightAccessor level,
         RandomState randomState
      ) {
         if (i == 2) {
            if (this.entryPos.equals(switch (dir) {
               case NORTH -> (Pair)NatureDungeonPieces.Floor.potentialExits.get(2);
               default -> (Pair)NatureDungeonPieces.Floor.potentialExits.get(3);
               case EAST -> (Pair)NatureDungeonPieces.Floor.potentialExits.get(1);
               case WEST -> (Pair)NatureDungeonPieces.Floor.potentialExits.get(0);
            })) {
               this.addStairs(dir, pieces, templateManager, chunkGen, level, randomState);
               return NatureDungeonPieces.WALL_ENTRY;
            }
         }

         return super.getWallAddStairs(i, dir, pieces, templateManager, chunkGen, level, randomState);
      }
   }

   protected static class Floor {
      protected final List<List<NatureDungeonPieces.Room>> map;
      protected final Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> roomOptions;
      protected final Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> entryOptions;
      protected final int floor;
      protected final int roomWidth;
      protected final int hallLength;
      protected final BlockPos worldPos;
      protected final RandomSource rand;
      protected final ResourceLocation hallway;
      protected final NatureDungeonPieces.Wall wall;
      protected Pair<Integer, Integer> entryPos;
      protected Pair<Integer, Integer> exitPos;
      protected static final int size = 5;
      protected static final List<Pair<Integer, Integer>> potentialExits = new ArrayList<>();
      protected static final Direction[] DIRECTIONS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

      public Floor(
         int floor,
         int roomWidth,
         int hallLength,
         Pair<Integer, Integer> entry,
         BlockPos worldPos,
         RandomSource rand,
         ResourceLocation hallway,
         Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> roomOptions,
         Map<NatureDungeonPieces.Room.ConnectionType, List<ResourceLocation>> entryOptions,
         NatureDungeonPieces.Wall wall
      ) {
         this.floor = floor;
         this.roomWidth = roomWidth;
         this.hallLength = hallLength;
         this.entryPos = entry;
         this.worldPos = worldPos;
         this.rand = rand;
         this.hallway = hallway;
         this.roomOptions = roomOptions;
         this.entryOptions = entryOptions;
         this.wall = wall;
         this.map = new ArrayList<>(5);

         for (int x = 0; x < 5; x++) {
            List<NatureDungeonPieces.Room> subList = new ArrayList<>(5);

            for (int z = 0; z < 5; z++) {
               subList.add(new NatureDungeonPieces.Room());
            }

            this.map.add(subList);
         }

         this.init();
      }

      public void init() {
         List<Pair<Integer, Integer>> filteredExitPoses = potentialExits.stream().filter(p -> !p.equals(this.entryPos)).collect(Collectors.toList());
         this.exitPos = filteredExitPoses.get(this.rand.nextInt(filteredExitPoses.size()));
         Pair<Integer, Integer> pointA = this.randomPos();
         if (!this.connectMainPath(this.entryPos, pointA)) {
            this.connectMainPath(pointA, this.exitPos);
         }

         int attempts = 0;
         int maxAttempts = 15;

         for (boolean needsConnections = true; needsConnections && attempts < 15; attempts++) {
            boolean checkAgain = false;

            for (int x = 0; x < 5; x++) {
               for (int z = 0; z < 5; z++) {
                  Pair<Integer, Integer> pos = Pair.of(x, z);
                  NatureDungeonPieces.Room room = this.get(pos).get();
                  if (!room.mainPath && !room.isConnectedToMain) {
                     checkAgain = true;
                     List<Direction> connectionOptions = new ArrayList<>();

                     for (Direction dir : DIRECTIONS) {
                        Pair<Integer, Integer> offsetPos = this.offset(x, z, dir);
                        this.get(offsetPos).ifPresent(adjacentRoom -> {
                           if ((adjacentRoom.mainPath || adjacentRoom.isConnectedToMain) && !offsetPos.equals(this.exitPos)) {
                              connectionOptions.add(dir);
                           }
                        });
                     }

                     if (!connectionOptions.isEmpty()) {
                        this.setConnection(x, z, connectionOptions.get(this.rand.nextInt(connectionOptions.size())), true);
                        room.isConnectedToMain = true;
                     }
                  }
               }
            }

            needsConnections = checkAgain;
         }

         if (attempts >= 15) {
            BlueSkies.LOGGER.warn("Nature dungeon could not finish connecting rooms due to reaching max attempts at " + this.worldPos);
         }

         for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
               NatureDungeonPieces.Room room = this.get(x, z).get();
               if (!room.isConnectedToMain && !room.mainPath) {
                  BlueSkies.LOGGER.warn("Nature dungeon found invalid room at " + x + ", " + z);
               }
            }
         }

         this.updateEntry();

         for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
               Pair<Integer, Integer> pos = Pair.of(x, z);
               if (this.get(pos).isPresent()) {
                  NatureDungeonPieces.Room room = this.get(pos).get();
                  List<Direction> conns = this.getSurroundingConnections(x, z);
                  if (this.exitPos.equals(pos)) {
                     Rotation rot = Rotation.NONE;
                     if (conns.contains(Direction.EAST)) {
                        rot = Rotation.CLOCKWISE_90;
                     } else if (conns.contains(Direction.SOUTH)) {
                        rot = Rotation.CLOCKWISE_180;
                     } else if (conns.contains(Direction.WEST)) {
                        rot = Rotation.COUNTERCLOCKWISE_90;
                     }

                     room.setPieces(this.roomOptions.get(NatureDungeonPieces.Room.ConnectionType.EXIT));
                     room.rotation = rot;
                  } else {
                     boolean isEntry = this.entryPos.equals(pos);
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

                           room.setPieces(
                              isEntry
                                 ? this.entryOptions.get(NatureDungeonPieces.Room.ConnectionType.END)
                                 : this.roomOptions.get(NatureDungeonPieces.Room.ConnectionType.END)
                           );
                           room.rotation = rot;
                           break;
                        case 2:
                           if (conns.containsAll(ImmutableList.of(Direction.NORTH, Direction.SOUTH))
                              && !conns.containsAll(ImmutableList.of(Direction.EAST, Direction.WEST))) {
                              room.setPieces(
                                 isEntry
                                    ? this.entryOptions.get(NatureDungeonPieces.Room.ConnectionType.I)
                                    : this.roomOptions.get(NatureDungeonPieces.Room.ConnectionType.I)
                              );
                              room.rotation = this.rand.nextBoolean() ? Rotation.NONE : Rotation.CLOCKWISE_180;
                           } else if (!conns.containsAll(ImmutableList.of(Direction.NORTH, Direction.SOUTH))
                              && conns.containsAll(ImmutableList.of(Direction.EAST, Direction.WEST))) {
                              room.setPieces(
                                 isEntry
                                    ? this.entryOptions.get(NatureDungeonPieces.Room.ConnectionType.I)
                                    : this.roomOptions.get(NatureDungeonPieces.Room.ConnectionType.I)
                              );
                              room.rotation = this.rand.nextBoolean() ? Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90;
                           }

                           for (Rotation rot : Rotation.values()) {
                              if (conns.containsAll(ImmutableList.of(rot.rotate(Direction.NORTH), rot.rotate(Direction.WEST)))) {
                                 room.setPieces(
                                    isEntry
                                       ? this.entryOptions.get(NatureDungeonPieces.Room.ConnectionType.L)
                                       : this.roomOptions.get(NatureDungeonPieces.Room.ConnectionType.L)
                                 );
                                 room.rotation = rot;
                                 break;
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

                           room.setPieces(
                              isEntry
                                 ? this.entryOptions.get(NatureDungeonPieces.Room.ConnectionType.T)
                                 : this.roomOptions.get(NatureDungeonPieces.Room.ConnectionType.T)
                           );
                           room.rotation = rot;
                           break;
                        default:
                           room.setPieces(
                              isEntry
                                 ? this.entryOptions.get(NatureDungeonPieces.Room.ConnectionType.CROSS)
                                 : this.roomOptions.get(NatureDungeonPieces.Room.ConnectionType.CROSS)
                           );
                           room.rotation = Rotation.getRandom(this.rand);
                     }
                  }
               }
            }
         }

         List<Pair<Integer, Integer>> potentialKeyRooms = new ArrayList<>();

         for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
               Pair<Integer, Integer> pos = Pair.of(x, z);
               NatureDungeonPieces.Room room = this.get(pos).get();
               if (!room.mainPath && !pos.equals(this.entryPos) && !pos.equals(this.exitPos) && room.isConnectedToMain) {
                  potentialKeyRooms.add(pos);
               }
            }
         }

         List<Pair<Integer, Integer>> potentialKeyDeadEnds = potentialKeyRooms.stream()
            .filter(p -> this.getSurroundingConnections((Integer)p.getFirst(), (Integer)p.getSecond()).size() == 1)
            .collect(Collectors.toList());
         if (!potentialKeyDeadEnds.isEmpty()) {
            this.get(potentialKeyDeadEnds.get(this.rand.nextInt(potentialKeyDeadEnds.size()))).get().hasKey = true;
         } else if (potentialKeyRooms.size() > 0) {
            this.get(potentialKeyRooms.get(this.rand.nextInt(potentialKeyRooms.size()))).get().hasKey = true;
         } else {
            BlueSkies.LOGGER.error("Nature dungeon at " + this.worldPos + " didn't have any valid key roooms");
         }
      }

      public void updateEntry() {
      }

      public boolean connectMainPath(Pair<Integer, Integer> pointA, Pair<Integer, Integer> pointB) {
         int x = (Integer)pointA.getFirst();
         int z = (Integer)pointA.getSecond();
         int x1 = (Integer)pointB.getFirst();
         int z1 = (Integer)pointB.getSecond();
         List<Pair<Integer, Integer>> path = new ArrayList<>();
         path.add(pointA);
         if (this.rand.nextBoolean()) {
            label83:
            while (true) {
               if (x == x1) {
                  while (true) {
                     if (z == z1) {
                        break label83;
                     }

                     if (z > z1) {
                        z--;
                     }

                     if (z < z1) {
                        z++;
                     }

                     path.add(Pair.of(x, z));
                  }
               }

               if (x > x1) {
                  x--;
               }

               if (x < x1) {
                  x++;
               }

               path.add(Pair.of(x, z));
            }
         } else {
            for (; z != z1; path.add(Pair.of(x, z))) {
               if (z > z1) {
                  z--;
               }

               if (z < z1) {
                  z++;
               }
            }

            for (; x != x1; path.add(Pair.of(x, z))) {
               if (x > x1) {
                  x--;
               }

               if (x < x1) {
                  x++;
               }
            }
         }

         path.add(pointB);
         Pair<Integer, Integer> prev = path.get(0);

         for (int i = 0; i < path.size(); i++) {
            Pair<Integer, Integer> point = path.get(i);
            if ((Integer)point.getSecond() < (Integer)prev.getSecond()) {
               this.setConnection((Integer)point.getFirst(), (Integer)point.getSecond(), Direction.SOUTH, true);
            }

            if ((Integer)point.getSecond() > (Integer)prev.getSecond()) {
               this.setConnection((Integer)point.getFirst(), (Integer)point.getSecond(), Direction.NORTH, true);
            }

            if ((Integer)point.getFirst() > (Integer)prev.getFirst()) {
               this.setConnection((Integer)point.getFirst(), (Integer)point.getSecond(), Direction.WEST, true);
            }

            if ((Integer)point.getFirst() < (Integer)prev.getFirst()) {
               this.setConnection((Integer)point.getFirst(), (Integer)point.getSecond(), Direction.EAST, true);
            }

            this.get(point).ifPresent(r -> r.mainPath = true);
            if (point.equals(this.exitPos)) {
               return true;
            }

            prev = point;
         }

         return false;
      }

      public void setConnection(int x, int z, Direction side, boolean isConnected) {
         NatureDungeonPieces.Room room = this.get(x, z).get();
         switch (side) {
            case NORTH:
               room.north = isConnected;
               this.get(x, z - 1).ifPresent(r -> r.south = isConnected);
               break;
            case SOUTH:
               room.south = isConnected;
               this.get(x, z + 1).ifPresent(r -> r.north = isConnected);
               break;
            case EAST:
               room.east = isConnected;
               this.get(x + 1, z).ifPresent(r -> r.west = isConnected);
               break;
            default:
               room.west = isConnected;
               this.get(x - 1, z).ifPresent(r -> r.east = isConnected);
         }
      }

      public List<Direction> getSurroundingConnections(int x, int z) {
         List<Direction> list = new ArrayList<>();
         NatureDungeonPieces.Room room = this.get(x, z).get();

         for (Direction dir : DIRECTIONS) {
            if (room.isConnected(dir)) {
               list.add(dir);
            }
         }

         return list;
      }

      public Pair<Integer, Integer> randomPos() {
         return Pair.of(this.rand.nextInt(5), this.rand.nextInt(5));
      }

      @Nullable
      public Optional<NatureDungeonPieces.Room> get(int x, int z) {
         return x >= 0 && x < 5 && z >= 0 && z < 5 ? Optional.of(this.map.get(x).get(z)) : Optional.empty();
      }

      @Nullable
      public Optional<NatureDungeonPieces.Room> get(Pair<Integer, Integer> pair) {
         return this.get((Integer)pair.getFirst(), (Integer)pair.getSecond());
      }

      @Nullable
      public Optional<NatureDungeonPieces.Room> get(int x, int z, Direction offset) {
         return this.get(this.offset(x, z, offset));
      }

      public void set(int x, int z, NatureDungeonPieces.Room segment) {
         this.map.get(x).set(z, segment);
      }

      public Pair<Integer, Integer> offset(int x, int z, Direction direction) {
         return Pair.of(x + direction.getStepX(), z + direction.getStepZ());
      }

      public void addPieces(StructurePiecesBuilder piece, StructureTemplateManager templateManager) {
         int roomOffset = this.roomWidth + this.hallLength;
         int hallOffset = this.roomWidth / 2 - 3;

         for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
               NatureDungeonPieces.Room room = this.get(x, z).get();
               if (room != null) {
                  int xOffset = room.rotation != Rotation.CLOCKWISE_90 && room.rotation != Rotation.CLOCKWISE_180 ? 0 : -1;
                  int zOffset = room.rotation != Rotation.COUNTERCLOCKWISE_90 && room.rotation != Rotation.CLOCKWISE_180 ? 0 : -1;
                  this.addRoom(x, z, roomOffset, xOffset, zOffset, room, templateManager, piece);
                  if ((x * 5 + z) % 2 == 0) {
                     byte var10;
                     byte var11;
                     switch (this.floor) {
                        case 1:
                           var10 = 1;
                           var11 = -1;
                           break;
                        case 2:
                           var10 = 0;
                           var11 = 0;
                           break;
                        case 3:
                           var10 = 0;
                           var11 = -1;
                           break;
                        default:
                           var10 = -1;
                           var11 = 0;
                     }

                     if (room.north && this.get(x, z, Direction.NORTH).isPresent()) {
                        piece.addPiece(
                           new NatureDungeonPieces.Piece(
                              templateManager,
                              this.worldPos.offset(x * roomOffset + hallOffset, 0, z * roomOffset - this.hallLength),
                              this.hallway,
                              this.floor,
                              this.rand,
                              Rotation.NONE
                           )
                        );
                     }

                     if (room.south && this.get(x, z, Direction.SOUTH).isPresent()) {
                        piece.addPiece(
                           new NatureDungeonPieces.Piece(
                              templateManager,
                              this.worldPos.offset(x * roomOffset + hallOffset, 0, z * roomOffset + this.roomWidth),
                              this.hallway,
                              this.floor,
                              this.rand,
                              Rotation.NONE
                           )
                        );
                     }

                     if (room.east && this.get(x, z, Direction.EAST).isPresent()) {
                        piece.addPiece(
                           new NatureDungeonPieces.Piece(
                              templateManager,
                              this.worldPos.offset(x * roomOffset + this.roomWidth + var10, 0, z * roomOffset + hallOffset + var11),
                              this.hallway,
                              this.floor,
                              this.rand,
                              Rotation.CLOCKWISE_90
                           )
                        );
                     }

                     if (room.west && this.get(x, z, Direction.WEST).isPresent()) {
                        piece.addPiece(
                           new NatureDungeonPieces.Piece(
                              templateManager,
                              this.worldPos.offset(x * roomOffset - this.hallLength + var10, 0, z * roomOffset + hallOffset + var11),
                              this.hallway,
                              this.floor,
                              this.rand,
                              Rotation.CLOCKWISE_90
                           )
                        );
                     }
                  }
               }
            }
         }
      }

      public void addWalls(
         StructurePiecesBuilder pieces, StructureTemplateManager templateManager, ChunkGenerator chunkGen, LevelHeightAccessor level, RandomState randomState
      ) {
         int roomOffset = this.roomWidth + this.hallLength;
         int width = this.roomWidth * 5 + this.hallLength * 4;

         for (int i = 0; i < 5; i++) {
            BlockPos northPos = this.worldPos.offset(i * roomOffset + (this.floor > 2 ? 17 : 19), 0, -5);
            BlockPos southPos = this.worldPos.offset(i * roomOffset, 0, width + 4);
            BlockPos westPos = this.worldPos.offset(-5, 0, i * roomOffset);
            BlockPos eastPos = this.worldPos.offset(width + 4, 0, i * roomOffset + (this.floor > 2 ? 17 : 19));
            pieces.addPiece(
               new NatureDungeonPieces.Piece(
                  templateManager,
                  northPos,
                  this.getWallAddStairs(i, Direction.NORTH, pieces, templateManager, chunkGen, level, randomState),
                  this.floor,
                  this.rand,
                  Rotation.CLOCKWISE_90,
                  1
               )
            );
            pieces.addPiece(
               new NatureDungeonPieces.Piece(
                  templateManager,
                  southPos,
                  this.getWallAddStairs(i, Direction.SOUTH, pieces, templateManager, chunkGen, level, randomState),
                  this.floor,
                  this.rand,
                  Rotation.COUNTERCLOCKWISE_90,
                  1
               )
            );
            pieces.addPiece(
               new NatureDungeonPieces.Piece(
                  templateManager,
                  westPos,
                  this.getWallAddStairs(i, Direction.WEST, pieces, templateManager, chunkGen, level, randomState),
                  this.floor,
                  this.rand,
                  Rotation.NONE,
                  1
               )
            );
            pieces.addPiece(
               new NatureDungeonPieces.Piece(
                  templateManager,
                  eastPos,
                  this.getWallAddStairs(i, Direction.EAST, pieces, templateManager, chunkGen, level, randomState),
                  this.floor,
                  this.rand,
                  Rotation.CLOCKWISE_180,
                  1
               )
            );
            if (i < 4) {
               pieces.addPiece(
                  new NatureDungeonPieces.Piece(
                     templateManager, northPos.offset(this.hallLength, 0, 0), this.wall.hall, this.floor, this.rand, Rotation.CLOCKWISE_90, 1
                  )
               );
               pieces.addPiece(
                  new NatureDungeonPieces.Piece(
                     templateManager, southPos.offset(this.roomWidth, 0, 0), this.wall.hall, this.floor, this.rand, Rotation.COUNTERCLOCKWISE_90, 1
                  )
               );
               pieces.addPiece(
                  new NatureDungeonPieces.Piece(templateManager, westPos.offset(0, 0, this.roomWidth), this.wall.hall, this.floor, this.rand, Rotation.NONE, 1)
               );
               pieces.addPiece(
                  new NatureDungeonPieces.Piece(
                     templateManager, eastPos.offset(0, 0, this.hallLength), this.wall.hall, this.floor, this.rand, Rotation.CLOCKWISE_180, 1
                  )
               );
            }

            if (i == 0) {
               pieces.addPiece(
                  new NatureDungeonPieces.Piece(
                     templateManager,
                     northPos.offset(width - (this.floor > 2 ? 13 : 15), 0, 0),
                     this.wall.corner,
                     this.floor,
                     this.rand,
                     Rotation.CLOCKWISE_90,
                     1
                  )
               );
               pieces.addPiece(
                  new NatureDungeonPieces.Piece(
                     templateManager, southPos.offset(-5, 0, 0), this.wall.corner, this.floor, this.rand, Rotation.COUNTERCLOCKWISE_90, 1
                  )
               );
               pieces.addPiece(
                  new NatureDungeonPieces.Piece(templateManager, westPos.offset(0, 0, -5), this.wall.corner, this.floor, this.rand, Rotation.NONE, 1)
               );
               pieces.addPiece(
                  new NatureDungeonPieces.Piece(
                     templateManager,
                     eastPos.offset(0, 0, width - (this.floor > 2 ? 13 : 15)),
                     this.wall.corner,
                     this.floor,
                     this.rand,
                     Rotation.CLOCKWISE_180,
                     1
                  )
               );
            }
         }
      }

      public ResourceLocation getWallAddStairs(
         int i,
         Direction dir,
         StructurePiecesBuilder pieces,
         StructureTemplateManager templateManager,
         ChunkGenerator chunkGen,
         LevelHeightAccessor level,
         RandomState randomState
      ) {
         return this.wall.room;
      }

      public void addRoom(
         int x,
         int z,
         int roomOffset,
         int xOffset,
         int zOffset,
         NatureDungeonPieces.Room room,
         StructureTemplateManager templateManager,
         StructurePiecesBuilder pieces
      ) {
         pieces.addPiece(
            new NatureDungeonPieces.Piece(
               templateManager, this.worldPos.offset(x * roomOffset + xOffset, 0, z * roomOffset + zOffset), room, this.floor, this.rand
            )
         );
      }

      static {
         potentialExits.add(Pair.of(0, 2));
         potentialExits.add(Pair.of(4, 2));
         potentialExits.add(Pair.of(2, 0));
         potentialExits.add(Pair.of(2, 4));
      }
   }

   public static class GardenPiece extends StructurePiece {
      private static int size = 114;

      protected GardenPiece(BlockPos pos) {
         super(
            (StructurePieceType)SkiesStructures.NATURE_DUNGEON.getPieceType("garden").get(),
            0,
            BoundingBox.fromCorners(pos, pos.offset(size - 1, 21, size - 1))
         );
      }

      public GardenPiece(StructurePieceSerializationContext context, CompoundTag nbt) {
         super((StructurePieceType)SkiesStructures.NATURE_DUNGEON.getPieceType("garden").get(), nbt);
      }

      protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
      }

      public void postProcess(
         WorldGenLevel world,
         StructureManager structureManager,
         ChunkGenerator chunkGen,
         RandomSource rand,
         BoundingBox bounds,
         ChunkPos chunkPos,
         BlockPos pos
      ) {
         this.generateBox(
            world,
            bounds,
            this.boundingBox.minX(),
            this.boundingBox.minY(),
            this.boundingBox.minZ(),
            this.boundingBox.maxX(),
            this.boundingBox.minY() + 2,
            this.boundingBox.maxZ(),
            SkiesBlocks.turquoise_dirt.defaultBlockState(),
            SkiesBlocks.turquoise_dirt.defaultBlockState(),
            false
         );
         SimplexNoise noise = new MLSimplexNoise(1337L);

         for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
               double h = (noise.getValue((this.boundingBox.minX() + x) / 50.0, (this.boundingBox.minZ() + z) / 50.0) + 1.0) / 2.0 * 3.0;
               int border2 = 2;
               if ((x < border2 - 1 || z < border2 - 1 || x > size - border2 || z > size - border2) && h > 2.0) {
                  h = 1.0;
               }

               for (int y = 0; y < h - 1.0; y++) {
                  this.placeBlock(
                     world,
                     SkiesBlocks.turquoise_dirt.defaultBlockState(),
                     this.boundingBox.minX() + x,
                     this.boundingBox.minY() + 3 + y,
                     this.boundingBox.minZ() + z,
                     bounds
                  );
               }

               this.placeBlock(
                  world,
                  SkiesBlocks.turquoise_comet_grass_block.defaultBlockState(),
                  this.boundingBox.minX() + x,
                  this.boundingBox.minY() + 3 + (int)h,
                  this.boundingBox.minZ() + z,
                  bounds
               );
               if (rand.nextFloat() < 0.33) {
                  this.placeBlock(
                     world,
                     (rand.nextFloat() < 0.07 ? SkiesBlocks.blush_blossom : SkiesBlocks.short_comet_grass).defaultBlockState(),
                     this.boundingBox.minX() + x,
                     this.boundingBox.minY() + 4 + (int)h,
                     this.boundingBox.minZ() + z,
                     bounds
                  );
               }
            }
         }
      }
   }

   public static class Piece extends GelTemplateStructurePiece implements IColoredBoundingBox {
      private final int floor;
      private final boolean hasKey;
      private final boolean hasChests;
      private final int wallFlags;

      public Piece(StructureTemplateManager templateManager, BlockPos pos, NatureDungeonPieces.Room room, int floor, RandomSource rand) {
         super((StructurePieceType)SkiesStructures.NATURE_DUNGEON.getPieceType("main").get(), 0, templateManager, room.getRandom(rand), pos);
         this.floor = floor;
         this.hasKey = room.hasKey;
         this.hasChests = room.hasKey || rand.nextFloat() < 0.5F;
         this.wallFlags = 0;
         this.rotation = room.rotation;
         this.setupPlaceSettings(templateManager);
      }

      public Piece(StructureTemplateManager templateManager, BlockPos pos, NatureDungeonPieces.Room room, int floor, RandomSource rand, ResourceLocation layer) {
         super((StructurePieceType)SkiesStructures.NATURE_DUNGEON.getPieceType("main").get(), 0, templateManager, layer, pos);
         this.floor = floor;
         this.hasKey = room.hasKey;
         this.hasChests = room.hasKey || rand.nextFloat() < 0.5F;
         this.wallFlags = 0;
         this.rotation = room.rotation;
         this.setupPlaceSettings(templateManager);
      }

      public Piece(StructureTemplateManager templateManager, BlockPos pos, ResourceLocation name, int floor, RandomSource rand, Rotation rotation) {
         this(templateManager, pos, name, floor, rand, rotation, 0);
      }

      public Piece(
         StructureTemplateManager templateManager, BlockPos pos, ResourceLocation name, int floor, RandomSource rand, Rotation rotation, int componentType
      ) {
         super((StructurePieceType)SkiesStructures.NATURE_DUNGEON.getPieceType("main").get(), componentType, templateManager, name, pos);
         this.floor = floor;
         this.hasKey = false;
         this.hasChests = false;
         this.wallFlags = componentType == 1 ? rand.nextInt(10) : 0;
         this.rotation = rotation;
         this.setupPlaceSettings(templateManager);
      }

      public Piece(StructurePieceSerializationContext context, CompoundTag nbt) {
         super((StructurePieceType)SkiesStructures.NATURE_DUNGEON.getPieceType("main").get(), nbt, context.structureTemplateManager());
         this.floor = nbt.getByte("Floor");
         this.hasKey = nbt.getBoolean("HasKey");
         this.hasChests = nbt.getBoolean("HasChests");
         this.wallFlags = nbt.getByte("WallFlags");
         this.setupPlaceSettings(context.structureTemplateManager());
      }

      protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
         super.addAdditionalSaveData(context, nbt);
         nbt.putByte("Floor", (byte)this.floor);
         nbt.putBoolean("HasKey", this.hasKey);
         nbt.putBoolean("HasChests", this.hasChests);
         nbt.putByte("WallFlags", (byte)this.wallFlags);
      }

      public StructurePlaceSettings getPlaceSettings(StructureTemplateManager structureManager) {
         Vec3i sizePos = ((StructureTemplate)structureManager.get(this.makeTemplateLocation()).get()).getSize();
         BlockPos centerPos = new BlockPos(sizePos.getX() / 2, 0, sizePos.getZ() / 2);
         StructurePlaceSettings settings = new StructurePlaceSettings().setKeepLiquids(false).setRotation(this.rotation);
         if (this.genDepth == 0) {
            settings.setRotationPivot(centerPos);
         }

         settings.addProcessor(RemoveGelStructureProcessor.INSTANCE);
         settings.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
         if (this.genDepth == 0) {
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.short_comet_grass, 0.5F, Blocks.AIR));
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.short_turquoise_grass, 0.5F, Blocks.AIR));
            settings.addProcessor(new VinesProcessor());
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.nature_stonebrick, 0.1F, SkiesBlocks.glowing_nature_stonebrick));
            settings.addProcessor(new RandomBlockSwapProcessor(Blocks.IRON_BLOCK, SkiesBlocks.nature_stonebrick));
            settings.addProcessor(new RandomBlockSwapProcessor(Blocks.LIGHT_BLUE_WOOL, 0.5F, SkiesBlocks.nature_stone));
            settings.addProcessor(new RandomBlockSwapProcessor(Blocks.LIGHT_BLUE_WOOL, SkiesBlocks.starlit_planks));
            settings.addProcessor(new RandomBlockSwapProcessor(Blocks.LIME_WOOL, 0.5F, SkiesBlocks.nature_stone));
            settings.addProcessor(new RandomBlockSwapProcessor(Blocks.LIME_WOOL, SkiesBlocks.nature_stonebrick));
            settings.addProcessor(new RandomBlockSwapProcessor(Blocks.GREEN_WOOL, 0.5F, SkiesBlocks.nature_stone));
            settings.addProcessor(new RandomBlockSwapProcessor(Blocks.GREEN_WOOL, SkiesBlocks.carved_nature_stonebrick));
            settings.addProcessor(new RandomBlockSwapProcessor(Blocks.PINK_WOOL, 0.5F, SkiesBlocks.nature_stone_slab));
            settings.addProcessor(new RandomBlockSwapProcessor(Blocks.PINK_WOOL, Blocks.AIR));
            if (this.floor == 1 || this.floor == 2) {
               settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.nature_stone, 0.03F, SkiesBlocks.glowing_nature_stone));
            } else if (this.floor == 5) {
               settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.nature_stone, 0.1F, SkiesBlocks.glowing_nature_stone));
            }
         } else if (this.genDepth == 1) {
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.short_turquoise_grass, 0.5F, Blocks.AIR));
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.nature_stone, 0.05F, SkiesBlocks.glowing_nature_stone));
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.nature_stonebrick, 0.07F, SkiesBlocks.nature_stone));
            settings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.nature_stonebrick, 0.1F, SkiesBlocks.glowing_nature_stonebrick));
            if (this.wallFlags < 2) {
               settings.addProcessor(new RandomBlockSwapProcessor(Blocks.GREEN_WOOL, SkiesBlocks.nature_stone));
            } else if (this.wallFlags < 4) {
               settings.addProcessor(new RandomBlockSwapProcessor(Blocks.RED_WOOL, SkiesBlocks.nature_stonebrick));
            } else if (this.wallFlags < 6) {
               settings.addProcessor(new RandomBlockSwapProcessor(Blocks.BLUE_WOOL, SkiesBlocks.nature_stone));
            } else if (this.wallFlags < 8) {
               settings.addProcessor(new RandomBlockSwapProcessor(Blocks.PINK_WOOL, SkiesBlocks.nature_stonebrick));
            }
         }

         return settings;
      }

      @Nullable
      public BlockState modifyState(ServerLevelAccessor world, RandomSource rand, BlockPos pos, BlockState originalState) {
         Block block = originalState.getBlock();
         if (this.genDepth == 0) {
            if (block == Blocks.OAK_LOG) {
               if (rand.nextFloat() < 0.5F && originalState.hasProperty(RotatedPillarBlock.AXIS)) {
                  return (BlockState)SkiesBlocks.nature_stone_pillar
                     .defaultBlockState()
                     .setValue(RotatedPillarBlock.AXIS, (Axis)originalState.getValue(RotatedPillarBlock.AXIS));
               }

               return SkiesBlocks.nature_stone.defaultBlockState();
            }

            if (block instanceof LeavesBlock && originalState.hasProperty(LeavesBlock.PERSISTENT)) {
               BlockState persistentLeaf = (BlockState)originalState.setValue(LeavesBlock.PERSISTENT, false);
               if (persistentLeaf.hasProperty(FruitLeavesBlock.GROWTH) && rand.nextFloat() < 0.3F) {
                  persistentLeaf = (BlockState)persistentLeaf.setValue(
                     FruitLeavesBlock.GROWTH, rand.nextBoolean() ? FruitLeavesBlock.Growth.CAN_GROW : FruitLeavesBlock.Growth.GROWN
                  );
               }

               return persistentLeaf;
            }
         } else if (this.genDepth == 1) {
            if (block == Blocks.SMOOTH_STONE_SLAB) {
               if (rand.nextFloat() < 0.5F) {
                  return null;
               }

               return SkiesBlocks.nature_stone_slab.defaultBlockState();
            }

            if (block == Blocks.RED_WOOL || block == Blocks.PINK_WOOL) {
               return SkiesBlocks.nature_stone.defaultBlockState();
            }

            if (block == Blocks.BLUE_WOOL || block == Blocks.GREEN_WOOL) {
               return SkiesBlocks.nature_stonebrick.defaultBlockState();
            }
         }

         return originalState;
      }

      protected void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor worldIn, RandomSource rand, BoundingBox sbb) {
         if (key.startsWith("spawner")) {
            if (!this.hasChests && !(rand.nextInt() < 0.7F)) {
               String[] split = key.split("-");
               Block block = (Block)BuiltInRegistries.BLOCK.get(ResourceLocation.parse(split[1]));
               if (block != null) {
                  worldIn.setBlock(pos, block.defaultBlockState(), 3);
               }
            } else {
               worldIn.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 3);
               if (worldIn.getBlockEntity(pos) instanceof SpawnerBlockEntity) {
                  SpawnerBlockEntity tile = (SpawnerBlockEntity)worldIn.getBlockEntity(pos);
                  tile.setEntityId(SkiesEntityTypes.STONELET, rand);
               }
            }
         }

         if (key.startsWith("chest")) {
            if (this.hasChests) {
               if (key.endsWith("extra")) {
                  RandomizableContainer.setBlockEntityLootTable(worldIn, rand, pos.below(), SkiesLootProv.NATURE_CHEST);
               } else {
                  RandomizableContainer.setBlockEntityLootTable(
                     worldIn, rand, pos.below(), this.hasKey ? SkiesLootProv.NATURE_CHEST_KEY : SkiesLootProv.NATURE_CHEST
                  );
               }
            } else {
               worldIn.setBlock(pos.below(), Blocks.AIR.defaultBlockState(), 3);
            }

            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
         }

         if ("keystone".equals(key)) {
            worldIn.setBlock(pos, SkiesBlocks.starlit_planks.defaultBlockState(), 3);
            if (worldIn.getBlockEntity(pos.above(2)) instanceof KeystoneBlockEntity.NatureKeystoneBlockEntity keystone) {
               keystone.setTeleportOffset(
                  new BlockPos(0, -1, 0).relative((Direction)worldIn.getBlockState(pos.above(2)).getValue(PoisonKeystoneBlock.FACING), -3)
               );
               keystone.setSpawnOffset(
                  new BlockPos(0, -1, 0).relative((Direction)worldIn.getBlockState(pos.above(2)).getValue(PoisonKeystoneBlock.FACING), -15)
               );
            }
         }
      }

      public int getBoundingBoxColor() {
         return this.hasKey ? IColoredBoundingBox.RED : IColoredBoundingBox.GREEN;
      }
   }

   protected static class Room {
      protected boolean north = false;
      protected boolean south = false;
      protected boolean east = false;
      protected boolean west = false;
      protected boolean mainPath = false;
      protected boolean isConnectedToMain = false;
      protected boolean hasKey = false;
      protected Rotation rotation = Rotation.NONE;
      private List<ResourceLocation> structures = new ArrayList<>();

      public Room() {
      }

      public void setPieces(List<ResourceLocation> structures) {
         this.structures.clear();
         this.structures.addAll(structures);
      }

      public ResourceLocation getRandom(RandomSource rand) {
         return this.structures.get(rand.nextInt(this.structures.size()));
      }

      public boolean isConnected(Direction direction) {
         switch (direction) {
            case NORTH:
               return this.north;
            case SOUTH:
               return this.south;
            case EAST:
               return this.east;
            case WEST:
               return this.west;
            default:
               return false;
         }
      }

      protected enum ConnectionType {
         CROSS,
         T,
         L,
         I,
         END,
         EXIT;

         @Override
         public String toString() {
            return StringUtil.toLower(this.name());
         }
      }
   }

   public static class StoneFillPiece extends StructurePiece {
      protected StoneFillPiece(BlockPos pos, BlockPos size, int componentType) {
         super(
            (StructurePieceType)SkiesStructures.NATURE_DUNGEON.getPieceType("stone_fill").get(), componentType, BoundingBox.fromCorners(pos, pos.offset(size))
         );
      }

      public StoneFillPiece(StructurePieceSerializationContext context, CompoundTag nbt) {
         super((StructurePieceType)SkiesStructures.NATURE_DUNGEON.getPieceType("stone_fill").get(), nbt);
      }

      protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
      }

      public void postProcess(
         WorldGenLevel world,
         StructureManager structureManager,
         ChunkGenerator chunkGen,
         RandomSource rand,
         BoundingBox bounds,
         ChunkPos chunkPos,
         BlockPos pos
      ) {
         if (this.genDepth == 0) {
            this.generateBox(
               world,
               bounds,
               this.boundingBox.minX(),
               this.boundingBox.minY(),
               this.boundingBox.minZ(),
               this.boundingBox.maxX(),
               this.boundingBox.maxY(),
               this.boundingBox.maxZ(),
               SkiesBlocks.nature_stone.defaultBlockState(),
               SkiesBlocks.nature_stone.defaultBlockState(),
               false
            );
         } else if (this.genDepth == 1) {
            for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); x++) {
               for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); z++) {
                  this.fillColumnDown(world, SkiesBlocks.nature_stone.defaultBlockState(), x, this.boundingBox.maxY(), z, bounds);
               }
            }
         }
      }
   }

   protected static class Wall {
      protected final ResourceLocation room;
      protected final ResourceLocation hall;
      protected final ResourceLocation corner;

      public Wall(int floor) {
         this.room = NatureDungeonPieces.locatePiece("wall/floor_" + floor + "_room");
         this.hall = NatureDungeonPieces.locatePiece("wall/floor_" + floor + "_hall");
         this.corner = NatureDungeonPieces.locatePiece("wall/floor_" + floor + "_corner");
      }
   }
}

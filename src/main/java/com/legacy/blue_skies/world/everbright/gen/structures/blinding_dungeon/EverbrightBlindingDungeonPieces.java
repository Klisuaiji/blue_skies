package com.legacy.blue_skies.world.everbright.gen.structures.blinding_dungeon;

import com.google.common.collect.Lists;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.block_entity.KeystoneBlockEntity;
import com.legacy.blue_skies.data.providers.SkiesLootProv;
import com.legacy.blue_skies.items.util.ToolUtils;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.structure_gel.api.structure.GelTemplateStructurePiece;
import com.legacy.structure_gel.api.structure.processor.RandomBlockSwapProcessor;
import com.legacy.structure_gel.api.structure.processor.RemoveGelStructureProcessor;
import java.util.List;
import java.util.UUID;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.Vec3;

public class EverbrightBlindingDungeonPieces {
   private static final ResourceLocation base = locatePiece("base");
   private static final ResourceLocation segment = locatePiece("segment");
   private static final ResourceLocation segmentTop = locatePiece("segment_top");
   private static final ResourceLocation towerTop = locatePiece("tower_top");
   private static final ResourceLocation bridge = locatePiece("bridge");
   private static final ResourceLocation prisonTower = locatePiece("small_tower_prison");
   private static final ResourceLocation studyTower = locatePiece("small_tower_study");
   private static final ResourceLocation witchTower = locatePiece("small_tower_witch");
   private static final List<ResourceLocation> libraryVariants = Lists.newArrayList(
      new ResourceLocation[]{
         locatePiece("small_tower_library_1"), locatePiece("small_tower_library_2"), locatePiece("small_tower_library_3"), locatePiece("small_tower_library_4")
      }
   );

   public static void assemble(
      StructureTemplateManager templateManager, BlockPos pos, Rotation rotation, StructurePiecesBuilder pieces, RandomSource random, RandomState randomState
   ) {
      pieces.addPiece(new EverbrightBlindingDungeonPieces.Piece(templateManager, base, pos, rotation));

      for (int i = 1; i <= 4; i++) {
         pieces.addPiece(new EverbrightBlindingDungeonPieces.Piece(templateManager, segment, pos.above(8 * i), rotation));
      }

      pieces.addPiece(new EverbrightBlindingDungeonPieces.Piece(templateManager, segmentTop, pos.above(40), rotation));
      pieces.addPiece(new EverbrightBlindingDungeonPieces.Piece(templateManager, towerTop, pos.offset(-6, 48, -6), rotation));
      int i = random.nextInt(3) + 2;
      pieces.addPiece(new EverbrightBlindingDungeonPieces.Piece(templateManager, bridge, pos.offset(6, i * 9 - 2 - i, -5), Rotation.COUNTERCLOCKWISE_90));
      pieces.addPiece(new EverbrightBlindingDungeonPieces.Piece(templateManager, prisonTower, pos.offset(2, i * 9 - 5 - i, -19), Rotation.COUNTERCLOCKWISE_90));
      i = random.nextInt(3) + 2;
      pieces.addPiece(new EverbrightBlindingDungeonPieces.Piece(templateManager, bridge, pos.offset(-6, i * 9 - 2 - i, 7), Rotation.CLOCKWISE_180));
      pieces.addPiece(new EverbrightBlindingDungeonPieces.Piece(templateManager, studyTower, pos.offset(-19, i * 9 - 5 - i, 2), Rotation.CLOCKWISE_180));
      i = random.nextInt(3) + 2;
      pieces.addPiece(new EverbrightBlindingDungeonPieces.Piece(templateManager, bridge, pos.offset(6, i * 9 - 2 - i, 19), Rotation.CLOCKWISE_90));
      pieces.addPiece(new EverbrightBlindingDungeonPieces.Piece(templateManager, witchTower, pos.offset(2, i * 9 - 5 - i, 23), Rotation.CLOCKWISE_90));
      i = random.nextInt(3) + 2;
      pieces.addPiece(new EverbrightBlindingDungeonPieces.Piece(templateManager, bridge, pos.offset(18, i * 9 - 2 - i, 7), Rotation.NONE));
      pieces.addPiece(
         new EverbrightBlindingDungeonPieces.Piece(
            templateManager, libraryVariants.get(random.nextInt(libraryVariants.size())), pos.offset(23, i * 9 - 5 - i, 2), Rotation.NONE
         )
      );
   }

   static ResourceLocation locatePiece(String location) {
      return BlueSkies.locate("dungeons/everbright/blinding/" + location);
   }

   public static class Piece extends GelTemplateStructurePiece {
      public Piece(StructureTemplateManager structureManager, ResourceLocation name, BlockPos pos, Rotation rotation) {
         super(
            (StructurePieceType)SkiesStructures.EVERBRIGHT_BLINDING_DUNGEON.getPieceType().get(),
            0,
            structureManager,
            name,
            getPlacementSettings(structureManager, name, pos, rotation),
            pos
         );
         this.rotation = rotation;
      }

      public Piece(StructurePieceSerializationContext context, CompoundTag nbt) {
         super(
            (StructurePieceType)SkiesStructures.EVERBRIGHT_BLINDING_DUNGEON.getPieceType().get(),
            nbt,
            context.structureTemplateManager(),
            name -> getPlacementSettings(
               context.structureTemplateManager(),
               name,
               new BlockPos(nbt.getInt("TPX"), nbt.getInt("TPY"), nbt.getInt("TPZ")),
               Rotation.valueOf(nbt.getString("Rot"))
            )
         );
      }

      private static StructurePlaceSettings getPlacementSettings(
         StructureTemplateManager structureManager, ResourceLocation name, BlockPos pos, Rotation rotation
      ) {
         Vec3i sizePos = ((StructureTemplate)structureManager.get(name).get()).getSize();
         BlockPos centerPos = new BlockPos(sizePos.getX() / 2, 0, sizePos.getZ() / 2);
         StructurePlaceSettings placementSettings = new StructurePlaceSettings()
            .setKeepLiquids(false)
            .setRotation(rotation)
            .setMirror(Mirror.NONE)
            .setRotationPivot(centerPos);
         placementSettings.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
         placementSettings.addProcessor(RemoveGelStructureProcessor.INSTANCE);
         placementSettings.addProcessor(new RandomBlockSwapProcessor(SkiesBlocks.blinding_stone, 0.1F, SkiesBlocks.glowing_blinding_stone));
         return placementSettings;
      }

      public void postProcess(
         WorldGenLevel worldIn,
         StructureManager structureManager,
         ChunkGenerator chunkGeneratorIn,
         RandomSource randomIn,
         BoundingBox bounds,
         ChunkPos chunkPosIn,
         BlockPos pos
      ) {
         super.postProcess(worldIn, structureManager, chunkGeneratorIn, randomIn, bounds, chunkPosIn, pos);
         if (this.templateName.equals(EverbrightBlindingDungeonPieces.base.toString())) {
            for (int x = 0; x < this.template.getSize().getX(); x++) {
               for (int z = 0; z < this.template.getSize().getZ(); z++) {
                  if (!this.getBlock(worldIn, x, 0, z, bounds).isAir()) {
                     if (this.getBlock(worldIn, x, 0, z, bounds).getBlock() == SkiesBlocks.bluebright_log) {
                        this.fillColumnDown(worldIn, SkiesBlocks.bluebright_log.defaultBlockState(), x, -1, z, bounds);
                     } else {
                        this.fillColumnDown(worldIn, SkiesBlocks.turquoise_stonebrick.defaultBlockState(), x, -1, z, bounds);
                     }
                  }
               }
            }
         }
      }

      protected void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, RandomSource rand, BoundingBox sbb) {
         if ("keystone".equals(key)) {
            level.setBlock(pos, SkiesBlocks.bright_blinding_keystone.defaultBlockState(), 3);
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof KeystoneBlockEntity.BrightBlindingKeystoneBlockEntity) {
               ((KeystoneBlockEntity.BrightBlindingKeystoneBlockEntity)tileEntity).setTeleportOffset(new BlockPos(0, 7, 5));
               ((KeystoneBlockEntity.BrightBlindingKeystoneBlockEntity)tileEntity).setSpawnOffset(new BlockPos(0, 7, 0));
            }
         } else if ("villager".equals(key)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            Villager entity = (Villager)EntityType.VILLAGER.create(level.getLevel());
            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            entity.setVillagerData(entity.getVillagerData().setType(VillagerType.byBiome(level.getBiome(pos))));
            level.addFreshEntity(entity);
         } else if ("vindicator".equals(key)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            Vindicator entity = (Vindicator)EntityType.VINDICATOR.create(level.getLevel());
            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            entity.setPersistenceRequired();
            ItemStack axe = new ItemStack(SkiesItems.aquite_axe, 1);
            ToolUtils.setStickType(axe, SkiesItems.bluebright_stick);
            entity.setItemSlot(EquipmentSlot.MAINHAND, axe);
            level.addFreshEntity(entity);
         } else if ("witch".equals(key)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            Witch entity = (Witch)EntityType.WITCH.create(level.getLevel());
            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            entity.setPersistenceRequired();
            level.addFreshEntity(entity);
         } else if ("cat".equals(key)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            Cat entity = (Cat)EntityType.CAT.create(level.getLevel());
            entity.setPos(pos.getX() + 0.5, pos.getY() - 1, pos.getZ() + 0.5);
            entity.setPersistenceRequired();
            entity.setTame(true);
            entity.setCollarColor(DyeColor.values()[rand.nextInt(DyeColor.values().length)]);
            entity.setOwnerUUID(UUID.fromString("f2914dae-441a-4254-aa5c-2ec4d917b7a6"));
            entity.setInSittingPose(true);
            entity.setVariant((CatVariant)((Reference)BuiltInRegistries.CAT_VARIANT.getRandom(rand).get()).value());
            level.addFreshEntity(entity);
         } else if ("armor_stand".equals(key)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            ArmorStand entity = (ArmorStand)EntityType.ARMOR_STAND.create(level.getLevel());
            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            entity.lookAt(Anchor.EYES, new Vec3(0.0, 0.0, 0.0));
            entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(SkiesItems.pyrope_chestplate));
            level.addFreshEntity(entity);
         } else if (key.startsWith("chest")) {
            String[] data = key.split("-");
            if (data.length > 1) {
               ResourceLocation loot = SkiesLootProv.BLINDING_LOOT.getOrDefault(data[1], SkiesLootProv.BLINDING_LIBRARY_KEY);
               RandomizableContainer.setBlockEntityLootTable(level, rand, pos.below(), loot);
            }
         }
      }
   }
}

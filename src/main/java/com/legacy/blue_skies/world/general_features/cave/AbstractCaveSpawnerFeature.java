package com.legacy.blue_skies.world.general_features.cave;

import com.google.common.collect.Lists;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.structure_gel.api.structure.GelTemplate;
import com.legacy.structure_gel.api.structure.base.IModifyState;
import com.legacy.structure_gel.api.structure.processor.RemoveGelStructureProcessor;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public abstract class AbstractCaveSpawnerFeature extends AbstractCaveFeature<NoneFeatureConfiguration> {
   protected final List<AbstractCaveSpawnerFeature.SpawnerTemplateData> templates;

   public AbstractCaveSpawnerFeature(Codec<NoneFeatureConfiguration> codec, AbstractCaveSpawnerFeature.SpawnerTemplateData... templates) {
      super(codec);
      this.templates = Lists.newArrayList(templates);
   }

   public boolean generate(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, NoneFeatureConfiguration config) {
      BlockPos placePos = pos;
      boolean flag = false;
      Rotation rotation = Rotation.getRandom(rand);
      StructureTemplateManager manager = world.getLevel().getServer().getStructureManager();
      AbstractCaveSpawnerFeature.SpawnerTemplateData templateData = this.getRandomTemplate(rand);
      StructureTemplate template = manager.getOrCreate(templateData.templateName);
      Vec3i size = template.getSize(rotation);
      placePos = template.getZeroPositionWithTransform(placePos, Mirror.NONE, rotation);
      placePos = pos.offset(-(pos.getX() % 16), 0, -(pos.getZ() % 16));
      BlockPos centerPos = placePos.offset(size.getX() / 2, 0, size.getZ() / 2);

      for (int i = 0; i < 7; i++) {
         BlockState lowerState = world.getBlockState(centerPos.below(i + 1));
         if (world.isEmptyBlock(centerPos.below(i)) && lowerState.canOcclude()) {
            placePos = placePos.below(i + 2);
            flag = true;
            break;
         }
      }

      if (flag) {
         BoundingBox bounds = BoundingBox.fromCorners(placePos, placePos.offset(size));
         StructurePlaceSettings placementSettings = new StructurePlaceSettings()
            .setRotation(rotation)
            .setRandom(rand)
            .setBoundingBox(bounds)
            .setRotationPivot(new BlockPos(size.getX() / 2, 0, size.getZ() / 2))
            .addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR)
            .addProcessor(RemoveGelStructureProcessor.INSTANCE);
         GelTemplate.placeInWorld(template, world, placePos, placePos, placementSettings, rand, 4, templateData);
         StructurePlaceSettings tempSettings = new StructurePlaceSettings()
            .setRotation(rotation)
            .setRotationPivot(new BlockPos(size.getX() / 2, 0, size.getZ() / 2));
         List<StructureBlockInfo> infos = template.filterBlocks(placePos, tempSettings, Blocks.STRUCTURE_BLOCK, false);

         for (StructureBlockInfo blockInfo : StructureTemplate.processBlockInfos(world, placePos, placePos, tempSettings, infos, template)) {
            if (blockInfo.nbt() != null) {
               StructureMode mode = StructureMode.valueOf(blockInfo.nbt().getString("mode"));
               if (mode == StructureMode.DATA) {
                  String function = blockInfo.nbt().getString("metadata");
                  this.setBlock(world, blockInfo.pos(), Blocks.AIR.defaultBlockState());
                  templateData.handleDataMarker(function, blockInfo.pos(), world, rand, bounds);
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean isValidPos(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, NoneFeatureConfiguration config) {
      return world.isEmptyBlock(pos);
   }

   @Override
   public boolean shouldGenerate(WorldGenLevel world, BlockPos pos) {
      return !world.getLevel().structureManager().hasAnyStructureAt(pos);
   }

   protected AbstractCaveSpawnerFeature.SpawnerTemplateData getRandomTemplate(RandomSource rand) {
      return this.templates.get(rand.nextInt(this.templates.size()));
   }

   public abstract static class SpawnerTemplateData implements IModifyState {
      public final ResourceLocation templateName;
      public final Supplier<EntityType<?>> spawnerMob;

      public SpawnerTemplateData(Supplier<EntityType<?>> spawnerMob, ResourceLocation templateName) {
         this.templateName = templateName;
         this.spawnerMob = spawnerMob;
      }

      public SpawnerTemplateData(Supplier<EntityType<?>> spawnerMob, String templateSuffix) {
         this(spawnerMob, BlueSkies.locate("cave_spawners/" + templateSuffix));
      }

      @Nullable
      public BlockState modifyState(ServerLevelAccessor world, RandomSource rand, BlockPos pos, BlockState originalState) {
         return originalState;
      }

      public void handleDataMarker(String function, BlockPos pos, WorldGenLevel world, RandomSource rand, BoundingBox bounds) {
         if (function.equals("spawner")) {
            this.placeSpawner(pos, world, rand, bounds);
         } else if (function.equals("chest")) {
            RandomizableContainer.setBlockEntityLootTable(world, rand, pos.below(), this.getLoot());
         }
      }

      protected void placeSpawner(BlockPos pos, WorldGenLevel world, RandomSource rand, BoundingBox bounds) {
         this.setBlockState(world, pos, Blocks.SPAWNER.defaultBlockState());
         if (world.getBlockEntity(pos) instanceof SpawnerBlockEntity tile) {
            tile.setEntityId(this.spawnerMob.get(), rand);
         } else {
            BlueSkies.LOGGER.warn("{} couldn't find mob spawner at ({}, {}, {})", this.getClass().getSimpleName(), pos.getX(), pos.getY(), pos.getZ());
         }
      }

      protected void setBlockState(LevelWriter world, BlockPos pos, BlockState state) {
         world.setBlock(pos, state, 4);
      }

      protected abstract ResourceLocation getLoot();
   }
}

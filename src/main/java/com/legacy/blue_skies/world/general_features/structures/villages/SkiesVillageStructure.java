package com.legacy.blue_skies.world.general_features.structures.villages;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.providers.SkiesLootProv;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesJigsawTypes;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.blue_skies.registries.SkiesVillagers;
import com.legacy.structure_gel.api.structure.jigsaw.ExtendedJigsawStructurePiece;
import com.legacy.structure_gel.api.structure.jigsaw.IPieceFactory;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapabilityType;
import com.legacy.structure_gel.api.structure.jigsaw.IPieceFactory.Context;
import com.mojang.serialization.Codec;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class SkiesVillageStructure {
   public static class Capability implements JigsawCapability {
      public static final SkiesVillageStructure.Capability INSTANCE = new SkiesVillageStructure.Capability();
      public static final Codec<SkiesVillageStructure.Capability> CODEC = Codec.unit(INSTANCE);

      public JigsawCapabilityType<?> getType() {
         return SkiesJigsawTypes.VILLAGE;
      }

      public IPieceFactory getPieceFactory() {
         return SkiesVillageStructure.Piece::new;
      }
   }

   public static class Piece extends ExtendedJigsawStructurePiece {
      private static final Map<String, EntityType<?>> BASIC_SPAWNS = new HashMap<>();

      public Piece(Context context) {
         super(context);
      }

      public Piece(StructurePieceSerializationContext context, CompoundTag nbt) {
         super(context, nbt);
      }

      public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, RandomSource rand, BoundingBox bounds) {
         if (BASIC_SPAWNS.containsKey(key)) {
            this.spawnEntity(BASIC_SPAWNS.get(key), pos, level, rand, null);
         } else if (key.equals("cod")) {
            this.spawnEntity(EntityType.COD, pos, level, rand, cod -> {
               level.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
               cod.setPersistenceRequired();
            });
         } else if (key.contains("villager")) {
            this.spawnEntity(
               EntityType.VILLAGER,
               pos,
               level,
               rand,
               villager -> {
                  if (key.contains("-")) {
                     villager.setVillagerData(
                        villager.getVillagerData()
                           .setType(
                              BuiltInRegistries.VILLAGER_TYPE.getOptional(BlueSkies.locate(key.split("-")[1])).orElseGet(() -> SkiesVillagers.CALMING_SKIES)
                           )
                     );
                  }

                  if (rand.nextFloat() < 0.1F) {
                     if (rand.nextBoolean()) {
                        villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.NITWIT));
                     } else {
                        villager.setBaby(true);
                     }
                  }
               }
            );
         } else if (key.startsWith("chest")) {
            this.setAir(level, pos);
            String[] data = key.split("-");
            ResourceLocation lootTable = BuiltInLootTables.VILLAGE_PLAINS_HOUSE;
            if (data.length > 2) {
               SkiesVillageProfessions profession = SkiesVillageProfessions.from(data[2]);
               if (profession != null) {
                  if (data[1].equals("bright")) {
                     lootTable = SkiesLootProv.VILLAGE_EVERBRIGHT_PROFESSIONS.get(profession);
                  } else if (data[1].equals("dawn")) {
                     lootTable = SkiesLootProv.VILLAGE_EVERDAWN_PROFESSIONS.get(profession);
                  }
               }
            } else if (data.length > 1) {
               SkiesVillageBiomes biome = SkiesVillageBiomes.from(data[1]);
               if (biome != null) {
                  lootTable = SkiesLootProv.VILLAGE_BIOME_CHESTS.get(biome);
               }
            }

            RandomizableContainer.setBlockEntityLootTable(level, rand, pos.below(), lootTable);
         }
      }

      private <T extends Entity> void spawnEntity(
         EntityType<T> entityType, BlockPos pos, ServerLevelAccessor level, RandomSource rand, Consumer<T> modifyEntity
      ) {
         this.setAir(level, pos);
         T entity = (T)this.createEntity(entityType, level, pos, this.rotation);
         if (modifyEntity != null) {
            modifyEntity.accept(entity);
         }

         if (entity instanceof Mob) {
            ((Mob)entity).finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);
         }

         level.addFreshEntity(entity);
      }

      public StructurePieceType getType() {
         return (StructurePieceType)SkiesStructures.BLUE_SKIES_VILLAGE.getPieceType().get();
      }

      static {
         BASIC_SPAWNS.put("golem", EntityType.IRON_GOLEM);
         BASIC_SPAWNS.put("azulfo", SkiesEntityTypes.AZULFO);
         BASIC_SPAWNS.put("ram", SkiesEntityTypes.STARDUST_RAM);
         BASIC_SPAWNS.put("monitor", SkiesEntityTypes.SHADE_MONITOR);
         BASIC_SPAWNS.put("camel", SkiesEntityTypes.CRYSTAL_CAMEL);
      }
   }
}

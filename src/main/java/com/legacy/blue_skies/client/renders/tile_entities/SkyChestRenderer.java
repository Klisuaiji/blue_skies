package com.legacy.blue_skies.client.renders.tile_entities;

import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.registries.SkiesBlockEntityTypes;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Triple;

@OnlyIn(Dist.CLIENT)
public class SkyChestRenderer<T extends BlockEntity & LidBlockEntity> extends ChestRenderer<T> {
   private static final Map<BlockEntityType<?>, Triple<Material, Material, Material>> CHEST_MATERIALS = new HashMap<BlockEntityType<?>, Triple<Material, Material, Material>>() {
      private static final long serialVersionUID = -5505549653590035348L;

      {
         this.put(
            SkiesBlockEntityTypes.BLUEBRIGHT_CHEST,
            Triple.of(
               SkiesRenderRefs.BLUEBRIGHT_CHEST_LOCATION_LEFT, SkiesRenderRefs.BLUEBRIGHT_CHEST_LOCATION, SkiesRenderRefs.BLUEBRIGHT_CHEST_LOCATION_RIGHT
            )
         );
         this.put(
            SkiesBlockEntityTypes.STARLIT_CHEST,
            Triple.of(SkiesRenderRefs.STARLIT_CHEST_LOCATION_LEFT, SkiesRenderRefs.STARLIT_CHEST_LOCATION, SkiesRenderRefs.STARLIT_CHEST_LOCATION_RIGHT)
         );
         this.put(
            SkiesBlockEntityTypes.FROSTBRIGHT_CHEST,
            Triple.of(
               SkiesRenderRefs.FROSTBRIGHT_CHEST_LOCATION_LEFT, SkiesRenderRefs.FROSTBRIGHT_CHEST_LOCATION, SkiesRenderRefs.FROSTBRIGHT_CHEST_LOCATION_RIGHT
            )
         );
         this.put(
            SkiesBlockEntityTypes.LUNAR_CHEST,
            Triple.of(SkiesRenderRefs.LUNAR_CHEST_LOCATION_LEFT, SkiesRenderRefs.LUNAR_CHEST_LOCATION, SkiesRenderRefs.LUNAR_CHEST_LOCATION_RIGHT)
         );
         this.put(
            SkiesBlockEntityTypes.DUSK_CHEST,
            Triple.of(SkiesRenderRefs.DUSK_CHEST_LOCATION_LEFT, SkiesRenderRefs.DUSK_CHEST_LOCATION, SkiesRenderRefs.DUSK_CHEST_LOCATION_RIGHT)
         );
         this.put(
            SkiesBlockEntityTypes.MAPLE_CHEST,
            Triple.of(SkiesRenderRefs.MAPLE_CHEST_LOCATION_LEFT, SkiesRenderRefs.MAPLE_CHEST_LOCATION, SkiesRenderRefs.MAPLE_CHEST_LOCATION_RIGHT)
         );
         this.put(
            SkiesBlockEntityTypes.COMET_CHEST,
            Triple.of(SkiesRenderRefs.COMET_CHEST_LOCATION_LEFT, SkiesRenderRefs.COMET_CHEST_LOCATION, SkiesRenderRefs.COMET_CHEST_LOCATION_RIGHT)
         );
      }
   };

   public SkyChestRenderer(Context context) {
      super(context);
   }

   @OnlyIn(Dist.CLIENT)
   public AABB getRenderBoundingBox(T blockEntity) {
      return new AABB(Vec3.atCenterOf(blockEntity.getBlockPos().offset(-1, 0, -1)), Vec3.atCenterOf(blockEntity.getBlockPos().offset(2, 2, 2)));
   }

   protected Material getMaterial(T blockEntity, ChestType chestType) {
      boolean isDouble = chestType != ChestType.SINGLE;
      boolean isLeft = chestType == ChestType.LEFT;
      Triple<Material, Material, Material> matRefs = CHEST_MATERIALS.get(blockEntity.getType());
      Material material;
      if (BlueSkiesConfig.COMMON.isChristmas()) {
         material = isDouble ? (isLeft ? Sheets.CHEST_XMAS_LOCATION_LEFT : Sheets.CHEST_XMAS_LOCATION_RIGHT) : Sheets.CHEST_XMAS_LOCATION;
      } else if (BlueSkiesConfig.COMMON.isHalloween() && !isDouble) {
         material = SkiesRenderRefs.PUMPKIN_CHEST_LOCATION;
      } else {
         material = isDouble ? (isLeft ? (Material)matRefs.getLeft() : (Material)matRefs.getRight()) : (Material)matRefs.getMiddle();
      }

      return material;
   }
}

package com.legacy.blue_skies.blocks.portal;

import com.google.common.collect.ImmutableList;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.tags.SkiesStructureTags;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.registries.SkiesParticles;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.LogicUtil;
import com.legacy.structure_gel.api.block.GelPortalBlock;
import com.legacy.structure_gel.api.dimension.portal.GelTeleporter;
import com.legacy.structure_gel.api.structure.StructureAccessHelper;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.BlockUtil.FoundRectangle;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class SkyPortalBlock extends GelPortalBlock {
   public static final List<Block> ALLOWED_INSIDE_BLOCKS = ImmutableList.of(
      Blocks.FIRE, SkiesBlocks.blue_fire, SkiesBlocks.black_fire, SkiesBlocks.upsidedown_blue_fire, SkiesBlocks.upsidedown_black_fire
   );
   private final boolean isEverbright = this.isEverbright();

   public SkyPortalBlock(Properties props) {
      super(props);
   }

   public static final Properties makeProps() {
      return Properties.ofFullCopy(Blocks.NETHER_PORTAL).lootFrom(() -> Blocks.AIR);
   }

   @OnlyIn(Dist.CLIENT)
   public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
      if (rand.nextInt(190) == 0) {
         if (this == SkiesBlocks.everdawn_portal) {
            worldIn.playLocalSound(
               pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SkiesSounds.BLOCK_EVERDAWN_PORTAL, SoundSource.BLOCKS, 0.3F, 1.0F, false
            );
         } else {
            worldIn.playLocalSound(
               pos.getX() + 0.5,
               pos.getY() + 0.5,
               pos.getZ() + 0.5,
               SkiesSounds.BLOCK_EVERBRIGHT_PORTAL,
               SoundSource.BLOCKS,
               0.4F,
               rand.nextFloat() * 0.4F + 0.8F,
               false
            );
         }
      }

      for (int i = 0; i < 2; i++) {
         double x = LogicUtil.plusOrMinus(pos.getX() + 0.5, 0.45, rand);
         double y = LogicUtil.plusOrMinus(pos.getY() + 0.5, 0.45, rand);
         double z = LogicUtil.plusOrMinus(pos.getZ() + 0.5, 0.45, rand);
         double dx = LogicUtil.plusOrMinus(0.06, rand);
         double dy = this.isEverbright ? -1.0E-4 : 1.0E-4;
         double dz = LogicUtil.plusOrMinus(0.001, rand);
         if (stateIn.getValue(AXIS) == Axis.X) {
            dx = LogicUtil.plusOrMinus(0.001, rand);
            dz = LogicUtil.plusOrMinus(0.06, rand);
            z = pos.getZ() + 0.5;
         } else {
            x = pos.getX() + 0.5;
         }

         worldIn.addParticle(this.isEverbright ? SkiesParticles.EVERBRIGHT_PORTAL : SkiesParticles.EVERDAWN_PORTAL, x, y, z, dx, dy, dz);
      }
   }

   public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
      BlockState frameBlock = (BlockState)this.getTeleporter(null).getFrameBlock().get();

      for (int y = -1; y <= 3; y++) {
         if (placer.getDirection() != Direction.NORTH && placer.getDirection() != Direction.SOUTH) {
            for (int z = -1; z <= 2; z++) {
               worldIn.setBlock(pos.offset(0, y, z), frameBlock, 18);
            }
         } else {
            for (int x = -1; x <= 2; x++) {
               worldIn.setBlock(pos.offset(x, y, 0), frameBlock, 18);
            }
         }
      }

      for (int y = 0; y <= 2; y++) {
         if (placer.getDirection() != Direction.NORTH && placer.getDirection() != Direction.SOUTH) {
            for (int z = 0; z <= 1; z++) {
               worldIn.setBlock(pos.offset(0, y, z), (BlockState)this.defaultBlockState().setValue(AXIS, Axis.Z), 18);
            }
         } else {
            for (int x = 0; x <= 1; x++) {
               worldIn.setBlock(pos.offset(x, y, 0), (BlockState)this.defaultBlockState().setValue(AXIS, Axis.X), 18);
            }
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public SoundInstance getTriggerSound() {
      return this.isEverbright
         ? SimpleSoundInstance.forLocalAmbience(SkiesSounds.BLOCK_EVERBRIGHT_PORTAL_TRIGGER, new Random().nextFloat() * 0.2F + 0.9F, 0.4F)
         : SimpleSoundInstance.forLocalAmbience(SkiesSounds.BLOCK_EVERDAWN_PORTAL_TRIGGER, new Random().nextFloat() * 0.2F + 0.9F, 0.35F);
   }

   @OnlyIn(Dist.CLIENT)
   public SoundInstance getTravelSound() {
      return this.isEverbright
         ? SimpleSoundInstance.forLocalAmbience(SkiesSounds.BLOCK_EVERBRIGHT_PORTAL_TRAVEL, new Random().nextFloat() * 0.4F + 0.8F, 1.0F)
         : SimpleSoundInstance.forLocalAmbience(SkiesSounds.BLOCK_EVERDAWN_PORTAL_TRAVEL, new Random().nextFloat() * 0.2F + 0.9F, 1.0F);
   }

   abstract boolean isEverbright();

   public static final class Teleporter extends GelTeleporter {
      public Teleporter(
         ServerLevel world,
         Supplier<ResourceKey<Level>> dimension1,
         Supplier<ResourceKey<Level>> dimension2,
         Supplier<PoiType> portalPOI,
         Supplier<GelPortalBlock> portalBlock,
         Supplier<BlockState> frameBlock
      ) {
         super(world, dimension1, dimension2, portalPOI, portalBlock, frameBlock, SkyPortalBlock.Teleporter::makeNew);
      }

      public Optional<FoundRectangle> findPortalAround(BlockPos startPos, boolean toNether) {
         Optional<FoundRectangle> result = super.findPortalAround(startPos, toNether);
         return result.isPresent() ? result : super.findPortalAround(shiftPosition(this, startPos), toNether);
      }

      public static Optional<FoundRectangle> makeNew(GelTeleporter teleporter, BlockPos startPos, Axis enterAxis) {
         return GelTeleporter.createAndFindPortalSurface(teleporter, shiftPosition(teleporter, startPos), enterAxis);
      }

      private static BlockPos shiftPosition(GelTeleporter teleporter, BlockPos startPos) {
         BlockPos newPos = startPos;
         ResourceKey<Level> destWorldKey = teleporter.getLevel().dimension();
         ResourceLocation destWorldLocation = destWorldKey.location();
         if (destWorldLocation.equals(SkiesDimensions.EVERBRIGHT_ID) || destWorldLocation.equals(SkiesDimensions.EVERDAWN_ID)) {
            ServerLevel destWorld = teleporter.getLevel().getServer().getLevel(destWorldKey);
            int moves = 0;

            boolean shiftPortal;
            do {
               shiftPortal = false;
               if (StructureAccessHelper.hasStructureChunkInRange(
                  destWorld.getChunkSource().getGeneratorState(), SkiesStructureTags.DUNGEONS, destWorld.registryAccess(), new ChunkPos(newPos), 1
               )) {
                  shiftPortal = true;
                  newPos = newPos.north(64);
                  moves++;
                  break;
               }

               if (moves > 10) {
                  BlueSkies.LOGGER.info("Hit the portal position distance shifting limit.");
                  break;
               }
            } while (shiftPortal);
         }

         return newPos;
      }
   }
}

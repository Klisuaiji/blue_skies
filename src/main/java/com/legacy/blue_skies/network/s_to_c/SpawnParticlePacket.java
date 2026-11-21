package com.legacy.blue_skies.network.s_to_c;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.registries.SkiesParticles;
import com.legacy.blue_skies.util.LogicUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SpawnParticlePacket implements PacketHandler.ModPacket<SpawnParticlePacket, SpawnParticlePacket.Handler> {
   private final byte id;
   private final float x;
   private final float y;
   private final float z;

   public SpawnParticlePacket(byte id, BlockPos pos) {
      this.id = id;
      this.x = pos.getX();
      this.y = pos.getY();
      this.z = pos.getZ();
   }

   public SpawnParticlePacket(byte id, Vec3 vec) {
      this.id = id;
      this.x = (float)vec.x;
      this.y = (float)vec.y;
      this.z = (float)vec.z;
   }

   public SpawnParticlePacket.Handler handler() {
      return new SpawnParticlePacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<SpawnParticlePacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("spawn_particle");
      }

      public void encoder(SpawnParticlePacket packet, FriendlyByteBuf buff) {
         buff.writeByte(packet.id);
         buff.writeFloat(packet.x);
         buff.writeFloat(packet.y);
         buff.writeFloat(packet.z);
      }

      public SpawnParticlePacket decoder(FriendlyByteBuf buff) {
         return new SpawnParticlePacket(buff.readByte(), new Vec3(buff.readFloat(), buff.readFloat(), buff.readFloat()));
      }

      public void handler(SpawnParticlePacket packet, IPayloadContext context) {
         if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> handlePacket(packet));
         }
      }

      @OnlyIn(Dist.CLIENT)
      private static void handlePacket(SpawnParticlePacket packet) {
         Level world = Minecraft.getInstance().level;
         switch (packet.id) {
            case 0:
               double spread = 0.3F;
               double speed = 0.02F;

               for (int i = 0; i < 6; i++) {
                  double x = LogicUtil.plusOrMinus(packet.x + 0.5F, spread, world.random);
                  double y = LogicUtil.plusOrMinus(packet.y + 0.5F, spread, world.random);
                  double z = LogicUtil.plusOrMinus(packet.z + 0.5F, spread, world.random);
                  double dx = LogicUtil.plusOrMinus(speed, world.random);
                  double dy = LogicUtil.plusOrMinus(speed, world.random);
                  double dz = LogicUtil.plusOrMinus(speed, world.random);
                  world.addParticle(ParticleTypes.FLAME, x, y, z, dx, dy, dz);
               }
               break;
            case 1:
               double speed = 0.02F;

               for (int i = 0; i < 1; i++) {
                  double x = LogicUtil.plusOrMinus(packet.x, 0.3F, world.random);
                  double y = LogicUtil.plusOrMinus(packet.y + 0.25F, 0.5F, world.random);
                  double z = LogicUtil.plusOrMinus(packet.z, 0.3F, world.random);
                  double dx = LogicUtil.plusOrMinus(speed, world.random);
                  double dy = LogicUtil.plusOrMinus(speed, world.random);
                  double dz = LogicUtil.plusOrMinus(speed, world.random);
                  world.addParticle(SkiesParticles.DUSK_SMOKE, x, y, z, dx, dy, dz);
               }
               break;
            default:
               BlueSkies.LOGGER.warn("Trying to spawn particle with unknown id: " + packet.id);
         }
      }
   }
}

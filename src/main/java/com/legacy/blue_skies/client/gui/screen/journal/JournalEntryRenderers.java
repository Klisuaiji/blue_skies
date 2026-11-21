package com.legacy.blue_skies.client.gui.screen.journal;

import com.google.gson.JsonObject;
import com.legacy.blue_skies.data.objects.IToJson;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class JournalEntryRenderers {
   public static class EntityDisplay implements JournalEntryRenderers.IRenderable {
      private static float rotation = 0.0F;
      private static LivingEntity entity;
      private final EntityType<?> entityType;
      private final int scale;
      private final int yOffset;

      public EntityDisplay(EntityType<?> entityType, int scale, int yOffset) {
         this.entityType = entityType;
         this.scale = scale;
         this.yOffset = yOffset;
      }

      public EntityType<?> getEntity() {
         return this.entityType;
      }

      @OnlyIn(Dist.CLIENT)
      @Override
      public void init(Minecraft mc) {
         if (entity != null) {
            entity.discard();
         }

         Level world = mc.level;
         Entity newEntity = this.entityType.create(world);
         entity = newEntity instanceof LivingEntity ? (LivingEntity)newEntity : (LivingEntity)EntityType.PIG.create(world);
         entity.setXRot(0.0F);
         entity.setYRot(0.0F);
         entity.yBodyRot = entity.yHeadRot = 0.0F;
         rotation = (float)(-Util.getMillis()) * 0.03F - 45.0F;
      }

      @OnlyIn(Dist.CLIENT)
      @Override
      public void render(GuiGraphics graphics, Minecraft mc, int width, int height, float partialTicks) {
         PoseStack poseStack = graphics.pose();
         poseStack.pushPose();
         int xOff = width / 2 + 70;
         int yOff = height / 2 + this.yOffset;
         poseStack.translate(xOff, yOff, 200.0F);
         float rotationTick = rotation + (float)Util.getMillis() * 0.03F;
         poseStack.mulPose(Axis.YP.rotationDegrees(rotationTick));
         poseStack.mulPose(Axis.XP.rotationDegrees((float)Math.sin(rotationTick / 20.0F) * 2.0F));
         poseStack.translate(0.0F, 0.0F, -50.0F);
         graphics.enableScissor(xOff - 62, yOff - 101, xOff + 61, yOff + 68);
         InventoryScreen.renderEntityInInventoryFollowsAngle(graphics, -xOff - 61, -yOff, xOff + 61, yOff + 68, this.scale, -1.0F, 0.0F, 0.0F, entity);
         graphics.disableScissor();
         poseStack.popPose();
         RenderSystem.applyModelViewMatrix();
      }

      @Override
      public String getType() {
         return "entity";
      }

      @Override
      public JsonObject toJson() {
         JsonObject json = new JsonObject();
         json.addProperty("entity", BuiltInRegistries.ENTITY_TYPE.getKey(this.entityType).toString());
         json.addProperty("scale", this.scale);
         json.addProperty("y_offset", this.yOffset);
         return json;
      }
   }

   public interface IRenderable extends IToJson<JournalEntryRenderers.IRenderable> {
      @OnlyIn(Dist.CLIENT)
      void render(GuiGraphics var1, Minecraft var2, int var3, int var4, float var5);

      String getType();

      @OnlyIn(Dist.CLIENT)
      default void init(Minecraft mc) {
      }
   }

   public static class ImageDisplay implements JournalEntryRenderers.IRenderable {
      private final ResourceLocation image;

      public ImageDisplay(ResourceLocation image) {
         this.image = image;
      }

      @OnlyIn(Dist.CLIENT)
      @Override
      public void render(GuiGraphics graphics, Minecraft mc, int width, int height, float partialTicks) {
         int bookHeight = 220;
         graphics.blit(this.image, width / 2, (height - bookHeight) / 2, 0.0F, 0.0F, 153, bookHeight, 256, 256);
      }

      @Override
      public String getType() {
         return "image";
      }

      @Override
      public JsonObject toJson() {
         JsonObject json = new JsonObject();
         json.addProperty("image", this.image.toString());
         return json;
      }
   }

   public static class ItemIcon implements JournalEntryRenderers.IRenderable {
      private final ItemStack item;

      public ItemIcon(ItemLike item) {
         this.item = new ItemStack(item);
      }

      @OnlyIn(Dist.CLIENT)
      @Override
      public void render(GuiGraphics graphics, Minecraft mc, int x, int y, float partialTicks) {
         graphics.renderItem(this.item, x, y);
      }

      @Override
      public String getType() {
         return "item";
      }

      @Override
      public JsonObject toJson() {
         JsonObject json = new JsonObject();
         json.addProperty("item", BuiltInRegistries.ITEM.getKey(this.item.getItem()).toString());
         return json;
      }
   }

   public static class TextureIcon implements JournalEntryRenderers.IRenderable {
      private final ResourceLocation texture;

      public TextureIcon(ResourceLocation texture) {
         this.texture = texture;
      }

      @OnlyIn(Dist.CLIENT)
      @Override
      public void render(GuiGraphics graphics, Minecraft mc, int x, int y, float partialTicks) {
         graphics.blit(this.texture, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      }

      @Override
      public String getType() {
         return "texture";
      }

      @Override
      public JsonObject toJson() {
         JsonObject json = new JsonObject();
         json.addProperty("texture", this.texture.toString());
         return json;
      }
   }
}

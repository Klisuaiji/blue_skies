package com.legacy.blue_skies.client.events;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.client.IncompatHandler;
import com.legacy.blue_skies.client.SkiesClientUtil;
import com.legacy.blue_skies.client.audio.ambient.DungeonAmbientSoundHandler;
import com.legacy.blue_skies.client.gui.screen.ArcsScreen;
import com.legacy.blue_skies.client.gui.screen.PerksScreen;
import com.legacy.blue_skies.client.gui.screen.journal.BlueJournalScreen;
import com.legacy.blue_skies.client.gui.screen.widgets.InventoryTabButton;
import com.legacy.blue_skies.client.gui.screen.widgets.SimpleInventoryTabButton;
import com.legacy.blue_skies.client.renders.world.BrightWeatherRenderer;
import com.legacy.blue_skies.client.renders.world.BrightWeatherTick;
import com.legacy.blue_skies.client.renders.world.DawnWeatherRenderer;
import com.legacy.blue_skies.client.renders.world.DawnWeatherTick;
import com.legacy.blue_skies.client.renders.world.StarrySkyRenderer;
import com.legacy.blue_skies.data.objects.tags.SkiesBiomeTags;
import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import com.legacy.blue_skies.entities.util.base.SkiesMountEntity;
import com.legacy.blue_skies.events.SkiesPlayerEvents;
import com.legacy.blue_skies.items.tools.weapons.SpearItem;
import com.legacy.blue_skies.items.util.IFalsiteItem;
import com.legacy.blue_skies.items.util.ToolUtils;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.c_to_s.OpenInventoryPacket;
import com.legacy.blue_skies.network.c_to_s.OpenedLorePacket;
import com.legacy.blue_skies.network.c_to_s.OpenedPerksScreenPacket;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.registries.SkiesItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.DimensionSpecialEffects.OverworldEffects;
import net.minecraft.client.renderer.DimensionSpecialEffects.SkyType;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler.LoopSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.VersionChecker.Status;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent.Pre;
import net.neoforged.neoforge.client.event.ScreenEvent.Opening;
import net.neoforged.neoforge.client.event.ScreenEvent.Init.Post;
import net.neoforged.neoforge.client.event.ViewportEvent.RenderFog;
import com.legacy.blue_skies.compat.LegacyGuiOverlay;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;
import org.joml.Matrix4f;

public class SkiesClientEvents {
   private static final Minecraft mc = Minecraft.getInstance();
   public static float sandstormStrength = 0.0F;
   public static float snowstormStrength = 0.0F;
   public static int clientTicks;
   public static int inventoryTabCount = 0;
   private static boolean inDungeon = false;
   private static DungeonAmbientSoundHandler dungeonAmbientSoundHandler = new DungeonAmbientSoundHandler(mc.player, mc.getSoundManager());
   private static final ResourceLocation BLUE_SKIES_ICONS = BlueSkies.locate("textures/gui/icons.png");
   public static final ResourceLocation UPDATE_LOGO = BlueSkies.locate("textures/gui/update_logo.png");
   private static final DimensionSpecialEffects EVERBRIGHT_RENDER_INFO = new SkiesClientEvents.EverbrightRenderInfo();
   private static final DimensionSpecialEffects EVERDAWN_RENDER_INFO = new SkiesClientEvents.EverdawnRenderInfo();

   public static void registerOverlays(RegisterGuiOverlaysEvent event) {
      event.registerAbove(
         LegacyGuiOverlay.PLAYER_HEALTH.id(), BlueSkies.locate("nature_arc_health_bonus"), (gui, graphics, partialTicks, screenWidth, screenHeight) -> {
            if (!mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
               gui.setupOverlayRenderState(true, false);
               RenderSystem.enableBlend();
               float health = SkiesPlayer.getIfPresent(mc.player, SkiesPlayer::getNatureHealth, () -> 0.0F);
               int width = mc.getWindow().getGuiScaledWidth();
               int height = mc.getWindow().getGuiScaledHeight();

               for (int i = 0; i < health / 2.0F; i++) {
                  int x = width / 2 - 91 + i * 8;
                  int y = height - gui.leftHeight;
                  graphics.blit(BLUE_SKIES_ICONS, x, y, health - i * 2 <= 1.0F ? 9 : 0, 0, 9, 9);
               }

               if (health > 0.0F) {
                  gui.leftHeight += 10;
               }

               RenderSystem.disableBlend();
            }
         }
      );
   }

   public static void initDimensionRenderInfo(RegisterDimensionSpecialEffectsEvent event) {
      event.register(SkiesDimensions.EVERBRIGHT_ID, EVERBRIGHT_RENDER_INFO);
      event.register(SkiesDimensions.EVERDAWN_ID, EVERDAWN_RENDER_INFO);
   }

   @SubscribeEvent
   public static void onPlayerRender(Pre event) {
      Player player = event.getEntity();
      if (SkiesPlayer.getIfPresent(player, SkiesPlayer::hasFullDuskInvis, () -> false) && !mc.player.isSpectator()) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onOpenGui(Opening event) {
      Screen screen = event.getScreen();
      if (mc.screen != null
         && mc.screen.getClass().getPackageName().startsWith("net.optifine.shaders")
         && (screen == null || screen != null && !screen.getClass().getPackageName().startsWith("net.optifine.shaders"))) {
         new Timer("Blue Skies Warning Timer").schedule(IncompatHandler.Optifine.getTask(), 5000L);
      }

      if (screen != null) {
         if (screen instanceof TitleScreen) {
            SkiesPlayerEvents.spawnDimensionID = 0;
            if (BlueSkiesConfig.CLIENT.isPanoramaCustom()) {
               SkiesClientEvents.Panoramas.setNewPanorama();
               event.setNewScreen(new TitleScreen());
            }
         }

         if ((screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen)
            && mc.player != null
            && mc.player.getVehicle() != null
            && mc.player.getVehicle() instanceof SkiesMountEntity) {
            event.setCanceled(true);
            PacketHandler.sendToServer(new OpenInventoryPacket.Mount());
         }
      }
   }

   @SubscribeEvent
   public static void openGuiEvent(Post event) {
      if (BlueSkies.isIndevBuild() && event.getScreen() instanceof CreateWorldScreen) {
         Component overworld = Component.literal("Spawn: Overworld");
         Component bright = Component.literal("Spawn: Everbright");
         Component dawn = Component.literal("Spawn: Everdawn");
         event.addListener(
            Button.builder(SkiesPlayerEvents.spawnDimensionID == 1 ? bright : (SkiesPlayerEvents.spawnDimensionID == 2 ? dawn : overworld), button -> {
               if (SkiesPlayerEvents.spawnDimensionID == 0) {
                  button.setMessage(bright);
               } else if (SkiesPlayerEvents.spawnDimensionID == 1) {
                  button.setMessage(dawn);
               } else {
                  button.setMessage(overworld);
               }

               if (SkiesPlayerEvents.spawnDimensionID < 2) {
                  SkiesPlayerEvents.spawnDimensionID++;
               } else {
                  SkiesPlayerEvents.spawnDimensionID = 0;
               }
            }).bounds(10, 25, 98, 20).build()
         );
      }

      Screen screen = event.getScreen();
      if (areInventoryTabsVisible(screen)) {
         boolean simpleFlag = BlueSkiesConfig.CLIENT.useSimpleInventoryTabs() || screen.getClass() == CreativeModeInventoryScreen.class;
         SkiesClientEvents.InventoryButtonFactory buttonFactory = simpleFlag ? SimpleInventoryTabButton::new : InventoryTabButton::new;
         inventoryTabCount = 0;
         event.addListener(
            buttonFactory.create(
               0,
               screen,
               !(screen.getClass() == InventoryScreen.class ^ screen.getClass() == CreativeModeInventoryScreen.class),
               inventoryTabCount++,
               button -> {
                  Player player = mc.player;
                  player.containerMenu.setCarried(ItemStack.EMPTY);
                  PacketHandler.sendToServer(new OpenInventoryPacket.Vanilla());
                  if (mc.gameMode.isServerControlledInventory()) {
                     mc.player.sendOpenInventory();
                  } else {
                     mc.setScreen(new InventoryScreen(player));
                  }
               }
            )
         );
         event.addListener(buttonFactory.create(1, screen, screen.getClass() != ArcsScreen.class, inventoryTabCount++, button -> {
            Player player = mc.player;
            player.containerMenu.setCarried(ItemStack.EMPTY);
            PacketHandler.sendToServer(new OpenInventoryPacket.Arcs());
         }));
         SkiesPlayer.ifPresent(
            mc.player,
            player -> {
               if (player.hasUsedBlueLore()) {
                  event.addListener(
                     (simpleFlag ? SimpleInventoryTabButton::new : InventoryTabButton::new)
                        .createWithSound(3, screen, screen.getClass() != BlueJournalScreen.class, inventoryTabCount++, button -> {
                           if (mc.player.containerMenu.getCarried().isEmpty()) {
                              SkiesPlayer.ifPresent(mc.player, skyPlayer -> skyPlayer.setOpenedBlueLore(true));
                              PacketHandler.sendToServer(new OpenedLorePacket());
                              BlueJournalScreen.open(true);
                           }
                        }, SoundEvents.BOOK_PAGE_TURN)
                  );
               }
            }
         );
         if (BlueSkies.ML_SUPPORTER.getRank(mc.player).hasPerks()) {
            event.addListener(buttonFactory.create(2, screen, screen.getClass() != PerksScreen.class, inventoryTabCount++, button -> {
               if (mc.player.containerMenu.getCarried().isEmpty()) {
                  mc.setScreen(new PerksScreen());
                  PacketHandler.sendToServer(new OpenedPerksScreenPacket());
               }
            }));
         }
      }
   }

   public static boolean areInventoryTabsVisible(Screen screen) {
      return screen.getClass() == InventoryScreen.class
         || screen.getClass() == CreativeModeInventoryScreen.class
         || screen.getClass() == ArcsScreen.class
         || screen.getClass() == PerksScreen.class;
   }

   @SubscribeEvent
   public static void drawScreenEvent(net.neoforged.neoforge.client.event.ScreenEvent.Render.Post event) {
      Screen screen = event.getScreen();
      if (screen instanceof TitleScreen && BlueSkies.isIndevBuild()) {
         Status status = NeoForgeVersion.getStatus();
         String text = "This is a Blue Skies indev build; it may be extremely unfinished or unplayable.";
         String subtitle = "Please make sure you know what you are doing!";
         int height = status != Status.BETA && status != Status.BETA_OUTDATED ? 5 : screen.height - 25;
         event.getGuiGraphics().drawCenteredString(mc.font, Component.translatable(text).withStyle(ChatFormatting.RED), screen.width / 2, height, 16777215);
         event.getGuiGraphics()
            .drawCenteredString(
               mc.font,
               Component.translatable(subtitle).withStyle(new ChatFormatting[]{ChatFormatting.RED, ChatFormatting.BOLD, ChatFormatting.UNDERLINE}),
               screen.width / 2,
               height + 11,
               16777215
            );
      }

      if (screen instanceof MerchantScreen tradeScreen
         && ((MerchantMenu)tradeScreen.getMenu()).getOffers().size() >= 2
         && ((MerchantMenu)tradeScreen.getMenu()).getOffers().get(1) != null
         && ((MerchantOffer)((MerchantMenu)tradeScreen.getMenu()).getOffers().get(1)).result.getItem() == SkiesItems.blue_journal) {
         SkiesPlayer.ifPresent(mc.player, skyPlayer -> {
            if (!skyPlayer.hasUsedBlueLore()) {
               SkiesClientUtil.renderExclamation(event.getGuiGraphics(), screen.width / 2 - 52, screen.height / 2 - 40);
            }
         });
      }
   }

   @SubscribeEvent
   public static void onRenderToolTip(ItemTooltipEvent event) {
      ItemStack stack = event.getItemStack();
      if (stack != null) {
         List<Component> lore = event.getToolTip();
         if (stack.getItem() instanceof SpearItem) {
            int index = 0;

            for (int i = 0; i < event.getToolTip().size(); i++) {
               if (lore.get(i) instanceof MutableComponent comp
                  && comp.getContents() instanceof TranslatableContents tcomp
                  && tcomp.getKey().equals("item.modifiers." + EquipmentSlot.MAINHAND.getName())) {
                  index = i;
               }
            }

            lore.add(index, Component.empty());
            lore.add(
               index,
               Component.translatable(
                     "gui.blue_skies.item.spear.thrown_damage",
                     new Object[]{ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(ToolUtils.getProjectileDamage(8.0F, ((SpearItem)stack.getItem()).getStick()))}
                  )
                  .withStyle(ChatFormatting.DARK_GREEN)
            );
            lore.add(index, Component.translatable("gui.blue_skies.item.spear.when_thrown").withStyle(ChatFormatting.GRAY));
         }

         if (stack.getItem() instanceof IFalsiteItem && ((IFalsiteItem)stack.getItem()).isFalsiteCompatible(stack) && IFalsiteItem.hasUses(stack)) {
            lore.add(
               1,
               Component.translatable(
                     "gui.blue_skies.item.falsite_uses", new Object[]{IFalsiteItem.getFalsiteUses(stack), IFalsiteItem.getMaxFalsiteUses(stack)}
                  )
                  .withStyle(ChatFormatting.GRAY)
            );
         }
      }
   }

   @SubscribeEvent(priority = EventPriority.LOW)
   public static void onFogRender(RenderFog event) {
      if ((event.getMode() == FogMode.FOG_TERRAIN || event.getMode() == FogMode.FOG_SKY) && event.getType() == FogType.NONE) {
         if (snowstormStrength > 2.0F && mc.level.dimension() == SkiesDimensions.everbrightKey()) {
            event.setNearPlaneDistance(Math.min(event.getNearPlaneDistance(), -8.0F));
            event.setFarPlaneDistance(Math.min(event.getFarPlaneDistance(), 240.0F + -snowstormStrength));
            event.setCanceled(true);
         }

         if (sandstormStrength > -8.0F && mc.level.dimension() == SkiesDimensions.everdawnKey()) {
            event.setNearPlaneDistance(Math.min(event.getNearPlaneDistance(), -8.0F));
            event.setFarPlaneDistance(Math.min(event.getFarPlaneDistance(), 220.0F + -sandstormStrength));
            event.setCanceled(true);
         }
      }
   }

   @SubscribeEvent
   public static void onClientTick(ClientTickEvent.Post event) {
      clientTicks++;
      Level world = mc.level;
      LocalPlayer player = mc.player;
      if (world != null && world.isClientSide) {
         if (player != null) {
            if (IncompatHandler.Optifine.shouldWarn && SkiesDimensions.inSkyDimension(player)) {
               player.displayClientMessage(IncompatHandler.Optifine.SHADER_MSG, false);
               IncompatHandler.Optifine.shouldWarn = false;
            }

            SkiesPlayer.ifPresent(player, skyPlayer -> {
               skyPlayer.clientTick();
               dungeonAmbientSoundHandler.dungeonType = skyPlayer.getDungeonMusicId();
            });
            dungeonAmbientSoundHandler.tick();
            if (SkiesDimensions.inSkyDimension(player)) {
               for (AmbientSoundHandler handler : player.ambientSoundHandlers) {
                  if (handler instanceof BiomeAmbientSoundsHandler biomeSoundHandler) {
                     Object2ObjectArrayMap<Biome, LoopSoundInstance> loopSounds = biomeSoundHandler.loopSounds;
                     if ((dungeonAmbientSoundHandler.dungeonType == null || dungeonAmbientSoundHandler.dungeonType == SkiesDungeonType.NONE)
                        && !(player.getY() + player.getEyeHeight() < world.getSeaLevel() + 1)) {
                        ObjectCollection<LoopSoundInstance> values = loopSounds.values();
                        values.removeIf(sound -> sound.volume <= 0.0F);
                        values.removeIf(AbstractTickableSoundInstance::isStopped);
                        Biome biome = (Biome)player.level().getBiomeManager().getNoiseBiomeAtPosition(player.getX(), player.getY(), player.getZ()).value();
                        values.forEach(LoopSoundInstance::fadeOut);
                        biome.getAmbientLoop().ifPresent(soundEvent -> loopSounds.compute(biome, (b, s) -> {
                           if (s == null) {
                              s = new LoopSoundInstance((SoundEvent)soundEvent.value());
                              s.volume = 0.01F;
                              mc.getSoundManager().play(s);
                           }

                           s.fadeIn();
                           return s;
                        }));
                     } else {
                        ObjectCollection<LoopSoundInstance> values = loopSounds.values();
                        values.forEach(LoopSoundInstance::fadeOut);
                        values.removeIf(AbstractTickableSoundInstance::isStopped);
                        values.removeIf(sound -> sound.volume <= 0.0F);
                     }
                  }
               }
            }

            boolean everbright = world.dimension() == SkiesDimensions.everbrightKey();
            boolean everdawn = world.dimension() == SkiesDimensions.everdawnKey();
            if (!everbright && !everdawn) {
               return;
            }

            if (everbright) {
               Entity entity = mc.getCameraEntity();
               BlockPos blockpos = entity.blockPosition();
               float maxSnowStrength = world.isThundering()
                     && ((Biome)world.getBiome(blockpos).value()).getPrecipitationAt(blockpos) != Precipitation.NONE
                     && world.thunderLevel > 0.0F
                  ? 210.0F
                  : (!world.isThundering() && world.rainLevel > 0.0F ? 200.0F : 200.0F);
               if ((
                     (!inDungeon || world.getBrightness(LightLayer.SKY, blockpos) > 0) && blockpos.getY() >= world.getSeaLevel() + 1
                        || world.getBrightness(LightLayer.SKY, blockpos) > 0
                        || !(snowstormStrength > 2.0F)
                  )
                  && !(snowstormStrength > maxSnowStrength)) {
                  if (world.getBrightness(LightLayer.SKY, blockpos) <= 12 && snowstormStrength > 160.0F) {
                     snowstormStrength--;
                  } else if ((blockpos.getY() >= world.getSeaLevel() + 1 || world.getBrightness(LightLayer.SKY, blockpos) > 0)
                     && world.isRaining()
                     && snowstormStrength < maxSnowStrength) {
                     snowstormStrength++;
                  } else if (snowstormStrength > 2.0F && !world.isRaining() && snowstormStrength <= maxSnowStrength
                     || world.rainLevel < 1.0F && snowstormStrength > 2.0F) {
                     snowstormStrength--;
                  }
               } else {
                  snowstormStrength--;
               }
            } else if (everdawn) {
               Entity entity = mc.getCameraEntity();
               BlockPos blockpos = entity.blockPosition();
               Holder<Biome> biome = world.getBiome(blockpos);
               float min = -8.0F;
               float max = 200.0F;
               if (blockpos.getY() < world.getSeaLevel() + 1 && world.getBrightness(LightLayer.SKY, blockpos) <= 0 && sandstormStrength > min) {
                  sandstormStrength--;
               } else if (SkiesPlayerEvents.inBossFight(player)
                  && sandstormStrength > 10.0F
                  && world.getBrightness(LightLayer.SKY, blockpos) <= 5
                  && blockpos.getY() >= world.getSeaLevel() + 1) {
                  sandstormStrength--;
               } else if (world.getBrightness(LightLayer.SKY, blockpos) <= 12 && sandstormStrength > 160.0F) {
                  sandstormStrength--;
               } else if (world.rainLevel > 0.0F
                  && world.registryAccess().registryOrThrow(Registries.BIOME).getOrCreateTag(SkiesBiomeTags.HAS_SANDSTORMS).contains(biome)
                  && sandstormStrength < max) {
                  sandstormStrength++;
               } else if (sandstormStrength > min
                     && !world.registryAccess().registryOrThrow(Registries.BIOME).getOrCreateTag(SkiesBiomeTags.HAS_SANDSTORMS).contains(biome)
                     && sandstormStrength <= max
                  || world.rainLevel < 1.0F && sandstormStrength > min) {
                  sandstormStrength--;
               }
            }

            SkiesPlayer.ifPresent(player, skyPlayer -> inDungeon = skyPlayer.getDungeonMusicId() == SkiesDungeonType.NATURE);
         }
      }
   }

   private static class EverbrightRenderInfo extends DimensionSpecialEffects {
      public EverbrightRenderInfo() {
         super(185.0F, true, SkyType.NORMAL, false, false);
      }

      public Vec3 getBrightnessDependentFogColor(Vec3 color, float brightness) {
         return color.multiply(brightness * 0.94F + 0.06F, brightness * 0.94F + 0.06F, brightness * 0.91F + 0.09F);
      }

      public boolean isFoggyAt(int x, int z) {
         return false;
      }

      public boolean renderSky(
         ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog
      ) {
         StarrySkyRenderer.BRIGHT_INSTANCE.render(ticks, partialTick, poseStack, level, camera, projectionMatrix, setupFog);
         return true;
      }

      public boolean renderSnowAndRain(ClientLevel level, int ticks, float partialTick, LightTexture lightTexture, double camX, double camY, double camZ) {
         BrightWeatherRenderer.INSTANCE.renderRainSnow(ticks, partialTick, level, lightTexture, camX, camY, camZ);
         return true;
      }

      public boolean tickRain(ClientLevel level, int ticks, Camera camera) {
         BrightWeatherTick.INSTANCE.tick(ticks, level, SkiesClientEvents.mc, camera);
         return true;
      }
   }

   private static class EverdawnRenderInfo extends OverworldEffects {
      public EverdawnRenderInfo() {
      }

      public boolean renderSky(
         ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog
      ) {
         StarrySkyRenderer.DAWN_INSTANCE.render(ticks, partialTick, poseStack, level, camera, projectionMatrix, setupFog);
         return true;
      }

      public boolean renderSnowAndRain(ClientLevel level, int ticks, float partialTick, LightTexture lightTexture, double camX, double camY, double camZ) {
         DawnWeatherRenderer.INSTANCE.renderRainSnow(ticks, partialTick, level, lightTexture, camX, camY, camZ);
         return true;
      }

      public boolean tickRain(ClientLevel level, int ticks, Camera camera) {
         DawnWeatherTick.INSTANCE.tick(ticks, level, SkiesClientEvents.mc, camera);
         return true;
      }
   }

   public interface InventoryButtonFactory {
      Button create(int var1, Screen var2, boolean var3, int var4, OnPress var5);
   }

   public interface InventoryButtonFactorySound {
      Button createWithSound(int var1, Screen var2, boolean var3, int var4, OnPress var5, SoundEvent var6);
   }

   public static class Panoramas {
      private static final ResourceLocation BRIGHT_BACKGROUND = BlueSkies.locate("textures/block/dirt_grass/turquoise_dirt.png");
      private static final ResourceLocation DAWN_BACKGROUND = BlueSkies.locate("textures/block/dirt_grass/lunar_dirt.png");
      private static final ResourceLocation PANORAMA_GRADIENT = BlueSkies.locate("textures/gui/panorama/panorama_overlay.png");
      private static final ResourceLocation PANORAMA_EMPTY = ResourceLocation.parse("textures/gui/title/background/panorama_overlay.png");
      private static Random rand = new Random();
      private static int panoramaCount = 0;
      protected static final Map<Integer, SkiesClientEvents.Panoramas.PanoramaData> PANORAMAS = new HashMap<>();

      protected static void setNewPanorama() {
         SkiesClientEvents.Panoramas.PanoramaData panorama = PANORAMAS.get(rand.nextInt(panoramaCount));
         TitleScreen.CUBE_MAP = new CubeMap(panorama.texture());
         TitleScreen.PANORAMA_OVERLAY = panorama.overlay();

         try {
            Screen.BACKGROUND_LOCATION = panorama.menuBackground();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      public static void registerPanoramas() {
         register("calming_skies", true, true);
         register("snow_covered_pines", true, true);
         register("brisk_meadow", true, true);
         register("frostbitten_forest", true, false);
         register("peeking_ocean", true, false, BlueSkies.locate("textures/block/misc/midnight_sand.png"));
         register("nature_dungeon", true, false, BlueSkies.locate("textures/block/dungeon/nature_stonebrick.png"));
         register("shaded_woodlands", false, false);
         register("crystal_roughs", false, false, BlueSkies.locate("textures/block/misc/crystal_sand.png"));
         register("sunset_maple_forest", false, false);
         register("moonlit_reservoir", false, false);
         register("poison_dungeon", false, false, BlueSkies.locate("textures/block/dungeon/poison_stonebrick.png"));
      }

      public static void register(String panorama, boolean everbright, boolean shouldHaveGradient) {
         ResourceLocation background = everbright ? BRIGHT_BACKGROUND : DAWN_BACKGROUND;
         register(panorama, everbright, shouldHaveGradient, background);
      }

      public static void register(String panorama, boolean everbright, boolean shouldHaveGradient, ResourceLocation background) {
         String prefix = everbright ? "everbright/" : "everdawn/";
         String panLoc = "textures/gui/panorama/";
         ResourceLocation gradient = shouldHaveGradient ? PANORAMA_GRADIENT : PANORAMA_EMPTY;
         PANORAMAS.put(
            panoramaCount, new SkiesClientEvents.Panoramas.PanoramaData(BlueSkies.locate(panLoc + prefix + panorama + "/panorama"), gradient, background)
         );
         panoramaCount++;
      }

      private record PanoramaData(ResourceLocation texture, ResourceLocation overlay, ResourceLocation menuBackground) {
      }
   }
}

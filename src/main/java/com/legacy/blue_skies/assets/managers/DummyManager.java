package com.legacy.blue_skies.assets.managers;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class DummyManager extends SimplePreparableReloadListener<Boolean> {
   protected Boolean prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
      return true;
   }

   protected void apply(Boolean pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
   }
}

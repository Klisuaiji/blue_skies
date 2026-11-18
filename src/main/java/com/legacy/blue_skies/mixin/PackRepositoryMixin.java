package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.client.init.SkiesResourcePackHandler;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({PackRepository.class})
public abstract class PackRepositoryMixin {
   @ModifyReturnValue(
      method = {"rebuildSelected"},
      at = {@At("RETURN")}
   )
   private List<Pack> gns$rebuildSelected(List<Pack> original) {
      List<Pack> selected = new ArrayList<>(original);
      Pack legacy = SkiesResourcePackHandler.LEGACY_PACK;
      Pack pa = this.getPack("programmer_art");
      if (!selected.contains(pa)) {
         if (selected.contains(legacy)) {
            selected.remove(legacy);
            return selected;
         } else {
            return original;
         }
      } else {
         if (!selected.contains(legacy)) {
            for (int i = 0; i < selected.size(); i++) {
               if (selected.get(i) == pa) {
                  selected.add(i + 1, legacy);
                  break;
               }
            }
         }

         return selected;
      }
   }

   @Shadow
   public Pack getPack(String id) {
      throw new IllegalStateException();
   }
}

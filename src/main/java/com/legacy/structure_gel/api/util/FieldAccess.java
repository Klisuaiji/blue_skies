package com.legacy.structure_gel.api.util;

import java.lang.reflect.Field;
import javax.annotation.Nullable;

/**
 * Small reflective field accessor used where 1.20.4 relied on Structure Gel's access transformer
 * (the transformer config is not applied to the 1.21.1 NeoForge compile classpath).
 * <p>
 * 1.21.1 note: vanilla classes keep the same Mojang field names, so a plain {@link Field} lookup is
 * enough. Access is validated eagerly when the constant is created, so a mapping mistake fails fast
 * at class initialization instead of silently returning nothing at runtime.
 */
public final class FieldAccess {
   private FieldAccess() {
   }

   public static Field field(Class<?> owner, String name) {
      try {
         Field field = owner.getDeclaredField(name);
         field.setAccessible(true);
         return field;
      } catch (NoSuchFieldException e) {
         throw new IllegalStateException("Unable to find field " + owner.getName() + "." + name + " on the 1.21.1 classpath", e);
      }
   }

   @Nullable
   @SuppressWarnings("unchecked")
   public static <T> T get(Field field, Object owner) {
      try {
         return (T) field.get(owner);
      } catch (IllegalAccessException e) {
         throw new IllegalStateException("Unable to read field " + field, e);
      }
   }

   public static void set(Field field, Object owner, Object value) {
      try {
         field.set(owner, value);
      } catch (IllegalAccessException e) {
         throw new IllegalStateException("Unable to write field " + field, e);
      }
   }
}

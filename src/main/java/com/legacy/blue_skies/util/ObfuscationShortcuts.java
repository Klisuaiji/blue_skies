package com.legacy.blue_skies.util;

import java.lang.reflect.Field;

public class ObfuscationShortcuts {
   public static void setObfuscatedValue(Field field, Object clazz, Object changeTo) {
      try {
         field.set(clazz, changeTo);
      } catch (IllegalArgumentException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }
   }
}

package com.legacy.blue_skies.util;

import java.lang.reflect.Field;

public class ObfuscationShortcuts {
   public static void setObfuscatedValue(Field field, Object clazz, Object changeTo) {
      try {
         field.set(clazz, changeTo);
      } catch (IllegalArgumentException var4) {
         var4.printStackTrace();
      } catch (IllegalAccessException var5) {
         var5.printStackTrace();
      }
   }
}

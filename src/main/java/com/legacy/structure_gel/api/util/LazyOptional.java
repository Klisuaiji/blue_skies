package com.legacy.structure_gel.api.util;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.neoforged.neoforge.common.util.Lazy;

/**
 * 1.21.1 note: NeoForge's {@link Lazy} is a final class now (it was an interface in 1.20.4),
 * so this wrapper can no longer implement it. It exposes the same Supplier contract and keeps
 * the "is this value present yet" tracking used by the Registrar system.
 */
public class LazyOptional<T> implements Supplier<T> {
   protected final Lazy<T> lazy;
   protected boolean isPresent = false;

   public static <T> LazyOptional<T> of(Supplier<T> supplier) {
      return new LazyOptional<>(supplier);
   }

   public static <T> LazyOptional<T> concurrent(Supplier<T> supplier) {
      return new LazyOptional.Concurrent<>(supplier);
   }

   protected LazyOptional(Supplier<T> supplier) {
      this.lazy = Lazy.of(supplier);
   }

   @Nullable
   @Override
   public T get() {
      if (!this.isPresent) {
         this.isPresent = true;
      }

      return this.lazy.get();
   }

   public <E extends T> T orElseGet(Supplier<E> orElse) {
      return (T)(this.isPresent ? this.get() : orElse.get());
   }

   public T getOrThrow(String message, Object... args) throws IllegalStateException {
      if (this.isPresent) {
         return this.get();
      } else {
         throw new IllegalStateException(String.format(message, args));
      }
   }

   public boolean isPresent() {
      return this.isPresent;
   }

   public static class Concurrent<T> extends LazyOptional<T> {
      private volatile Object lock = new Object();

      protected Concurrent(Supplier<T> supplier) {
         super(supplier);
      }

      @Nullable
      @Override
      public final T get() {
         if (!this.isPresent) {
            synchronized (this.lock) {
               this.lock = null;
               return super.get();
            }
         } else {
            return super.get();
         }
      }
   }
}

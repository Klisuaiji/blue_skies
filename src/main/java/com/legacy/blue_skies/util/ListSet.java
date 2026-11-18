package com.legacy.blue_skies.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import javax.annotation.Nullable;

public class ListSet<E> implements List<E>, Set<E>, Cloneable {
   private final List<E> list;
   @Nullable
   private Set<E> set;

   public ListSet() {
      this.list = new ArrayList<>();
   }

   public ListSet(Collection<E> backingData) {
      this.list = new ArrayList<>(backingData);
   }

   public static <E> ListSet<E> copyOf(Collection<E> backingData) {
      return new ListSet<>(backingData);
   }

   @SafeVarargs
   public static <E> ListSet<E> of(E... backingData) {
      return new ListSet<>(List.of(backingData));
   }

   public static <E> Collector<E, ?, ListSet<E>> toListSet() {
      return new Collector<E, ListSet<E>, ListSet<E>>() {
         @Override
         public Supplier<ListSet<E>> supplier() {
            return ListSet::new;
         }

         @Override
         public BiConsumer<ListSet<E>, E> accumulator() {
            return ListSet::add;
         }

         @Override
         public BinaryOperator<ListSet<E>> combiner() {
            return (left, right) -> {
               left.addAll(right);
               return right;
            };
         }

         @Override
         public Function<ListSet<E>, ListSet<E>> finisher() {
            return o -> o;
         }

         @Override
         public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.IDENTITY_FINISH);
         }
      };
   }

   public List<E> getList() {
      return this.list;
   }

   public Set<E> getSet() {
      if (this.set == null) {
         this.set = new HashSet<>(this.list);
      }

      return this.set;
   }

   public ListSet<E> clone() {
      return new ListSet<>(this.list);
   }

   @Override
   public int size() {
      return this.list.size();
   }

   @Override
   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   @Override
   public boolean contains(Object o) {
      return this.getSet().contains(o);
   }

   @Override
   public Iterator<E> iterator() {
      return this.list.iterator();
   }

   @Override
   public Object[] toArray() {
      return this.list.toArray();
   }

   @Override
   public <T> T[] toArray(T[] a) {
      return (T[])this.list.toArray(a);
   }

   @Override
   public boolean add(E e) {
      this.list.add(e);
      return this.getSet().add(e);
   }

   @Override
   public boolean remove(Object o) {
      boolean ret = this.getSet().remove(o);
      if (ret) {
         this.list.remove(o);
      }

      return ret;
   }

   @Override
   public boolean containsAll(Collection<?> c) {
      return this.getSet().containsAll(c);
   }

   @Override
   public boolean addAll(Collection<? extends E> c) {
      boolean ret = this.list.addAll(c);
      this.getSet().addAll(c);
      return ret;
   }

   @Override
   public boolean addAll(int index, Collection<? extends E> c) {
      boolean ret = this.list.addAll(index, c);
      this.getSet().addAll(c);
      return ret;
   }

   @Override
   public boolean removeAll(Collection<?> c) {
      boolean ret = this.list.removeAll(c);
      this.getSet().removeAll(c);
      return ret;
   }

   @Override
   public boolean retainAll(Collection<?> c) {
      boolean ret = this.list.retainAll(c);
      this.getSet().retainAll(c);
      return ret;
   }

   @Override
   public void clear() {
      this.list.clear();
      if (this.set != null) {
         this.set.clear();
         this.set = null;
      }
   }

   @Override
   public E get(int index) {
      return this.list.get(index);
   }

   @Override
   public E set(int index, E element) {
      E ret = this.list.set(index, element);
      if (ret != null) {
         this.getSet().remove(ret);
      }

      this.getSet().add(element);
      return ret;
   }

   @Override
   public void add(int index, E element) {
      this.list.add(index, element);
      this.getSet().add(element);
   }

   @Override
   public E remove(int index) {
      E ret = this.list.remove(index);
      if (ret != null) {
         this.getSet().remove(ret);
      }

      return ret;
   }

   @Override
   public int indexOf(Object o) {
      return this.list.indexOf(o);
   }

   @Override
   public int lastIndexOf(Object o) {
      return this.list.lastIndexOf(o);
   }

   @Override
   public ListIterator<E> listIterator() {
      return this.list.listIterator();
   }

   @Override
   public ListIterator<E> listIterator(int index) {
      return this.list.listIterator(index);
   }

   @Override
   public List<E> subList(int fromIndex, int toIndex) {
      return this.list.subList(fromIndex, toIndex);
   }

   @Override
   public Spliterator<E> spliterator() {
      return this.list.spliterator();
   }
}

package org.infinispan.commons.util;

import org.infinispan.commons.equivalence.AnyEquivalence;
import org.infinispan.commons.equivalence.Equivalence;
import org.infinispan.commons.equivalence.EquivalentHashMap;
import org.infinispan.commons.equivalence.EquivalentHashSet;
import org.infinispan.commons.equivalence.EquivalentLinkedHashMap;
import org.infinispan.commons.util.concurrent.jdk8backported.BoundedEquivalentConcurrentHashMapV8;
import org.infinispan.commons.util.concurrent.jdk8backported.ConcurrentParallelHashMapV8;
import org.infinispan.commons.util.concurrent.jdk8backported.EquivalentConcurrentHashMapV8;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A factory for ConcurrentMaps.
 *
 * @author Manik Surtani
 * @author Galder Zamarreño
 * @since 5.1
 */
public class CollectionFactory {
   private static final float DEFAULT_LOAD_FACTOR = 0.75f;

   public static int computeCapacity(int expectedSize) {
      return computeCapacity(expectedSize, DEFAULT_LOAD_FACTOR);
   }

   public static int computeCapacity(int expectedSize, float loadFactor) {
      return (int) (expectedSize / loadFactor + 1.0f);
   }


   public static <K, V> ConcurrentMap<K, V> makeConcurrentMap() {
      return new ConcurrentHashMap<K, V>();
   }

   public static <K, V> ConcurrentMap<K, V> makeConcurrentMap(int initCapacity) {
      return new ConcurrentHashMap<K, V>(initCapacity);
   }

   public static <K, V> ConcurrentMap<K, V> makeConcurrentMap(int initCapacity, int concurrencyLevel) {
      return new ConcurrentHashMap<K, V>(initCapacity, 0.75f, concurrencyLevel);
   }
   
   public static <K, V> ConcurrentMap<K, V> makeConcurrentParallelMap(int initCapacity, int concurrencyLevel) {
      return new ConcurrentParallelHashMapV8<K, V>(initCapacity, AnyEquivalence.getInstance(),
            AnyEquivalence.getInstance());
   }

   public static <K, V> ConcurrentMap<K, V> makeConcurrentMap(int initCapacity, float loadFactor, int concurrencyLevel) {
      return new ConcurrentHashMap<K, V>(initCapacity, loadFactor, concurrencyLevel);
   }

   public static <K, V> ConcurrentMap<K, V> makeConcurrentMap(
         Equivalence<? super K> keyEq, Equivalence<? super V> valueEq) {
      if (requiresEquivalent(keyEq, valueEq))
         return new EquivalentConcurrentHashMapV8<K, V>(keyEq, valueEq);
      else
         return makeConcurrentMap();
   }

   public static <K, V> ConcurrentMap<K, V> makeConcurrentMap(
         int initCapacity, Equivalence<? super K> keyEq, Equivalence<? super V> valueEq) {
      if (requiresEquivalent(keyEq, valueEq))
         return new EquivalentConcurrentHashMapV8<K, V>(initCapacity, keyEq, valueEq);
      else
         return makeConcurrentMap(initCapacity);
   }

   public static <K, V> ConcurrentMap<K, V> makeConcurrentMap(
         int initCapacity, int concurrencyLevel, Equivalence<? super K> keyEq, Equivalence<? super V> valueEq) {
      if (requiresEquivalent(keyEq, valueEq))
         return new EquivalentConcurrentHashMapV8<K, V>(
               initCapacity, concurrencyLevel, keyEq, valueEq);
      else
         return makeConcurrentMap(initCapacity, concurrencyLevel);
   }
   
   public static <K, V> ConcurrentMap<K, V> makeConcurrentParallelMap(
         int initCapacity, int concurrencyLevel, Equivalence<? super K> keyEq, Equivalence<? super V> valueEq) {
      if (requiresEquivalent(keyEq, valueEq))
         return new ConcurrentParallelHashMapV8<K, V>(
               initCapacity, concurrencyLevel, keyEq, valueEq);
      else
         return makeConcurrentParallelMap(initCapacity, concurrencyLevel);
   }

   public static <K, V> ConcurrentMap<K, V> makeConcurrentMap(
         int initCapacity, float loadFactor, int concurrencyLevel,
         Equivalence<? super K> keyEq, Equivalence<? super V> valueEq) {
      if (requiresEquivalent(keyEq, valueEq))
         return new EquivalentConcurrentHashMapV8<K, V>(
               initCapacity, loadFactor, concurrencyLevel, keyEq, valueEq);
      else
         return makeConcurrentMap(initCapacity, loadFactor, concurrencyLevel);
   }

   public static <K, V> ConcurrentMap<K, V> makeBoundedConcurrentMap(int maxSize) {
      return new BoundedEquivalentConcurrentHashMapV8<K, V>(maxSize,
         AnyEquivalence.getInstance(), AnyEquivalence.getInstance());
   }

   public static <K, V> Map<K, V> makeMap(
         Equivalence<? super K> keyEq, Equivalence<? super V> valueEq) {
      if (requiresEquivalent(keyEq, valueEq))
         return new EquivalentHashMap<K, V>(keyEq, valueEq);
      else
         return new HashMap<K, V>();
   }

   public static <K, V> Map<K, V> makeMap(
         int initialCapacity, Equivalence<? super K> keyEq, Equivalence<? super V> valueEq) {
      if (requiresEquivalent(keyEq, valueEq))
         return new EquivalentHashMap<K, V>(initialCapacity, keyEq, valueEq);
      else
         return new HashMap<K, V>(initialCapacity);
   }

   public static <K, V> Map<K, V> makeMap(
         Map<? extends K, ? extends V> entries, Equivalence<? super K> keyEq, Equivalence<? super V> valueEq) {
      if (requiresEquivalent(keyEq, valueEq))
         return new EquivalentHashMap<K, V>(entries, keyEq, valueEq);
      else
         return new HashMap<K, V>(entries);
   }

   public static <K, V> Map<K, V> makeLinkedMap(int initialCapacity,
         float loadFactor, EquivalentLinkedHashMap.IterationOrder iterationOrder,
         Equivalence<? super K> keyEq, Equivalence<? super V> valueEq) {
      if (requiresEquivalent(keyEq, valueEq))
         return new EquivalentLinkedHashMap<K, V>(initialCapacity, loadFactor, iterationOrder, keyEq, valueEq);
      else
         return new LinkedHashMap<K, V>(initialCapacity, loadFactor, iterationOrder.toJdkAccessOrder());
   }

   public static <T> Set<T> makeSet(Equivalence<? super T> entryEq) {
      if (requiresEquivalent(entryEq))
         return new EquivalentHashSet<T>(entryEq);
      else
         return new HashSet<T>();
   }

   public static <T> Set<T> makeSet(int initialCapacity, Equivalence<? super T> entryEq) {
      if (requiresEquivalent(entryEq))
         return new EquivalentHashSet<T>(initialCapacity, entryEq);
      else
         return new HashSet<T>(initialCapacity);
   }

   /**
    * Create a Set backed by the specified array.
    *
    * @param entries the array by which the list will be backed
    * @param <T> type of elements
    * @return a set view of the specified array
    */
   @SafeVarargs
   public static <T> Set<T> makeSet(T... entries) {
      return new HashSet<T>(Arrays.asList(entries));
   }

   private static <K, V> boolean requiresEquivalent(
         Equivalence<K> keyEq, Equivalence<V> valueEq) {
      AnyEquivalence<Object> instance = AnyEquivalence.getInstance();
      return keyEq != instance || valueEq != instance;
   }

   private static <T> boolean requiresEquivalent(Equivalence<T> typeEq) {
      return typeEq != AnyEquivalence.getInstance();
   }

}

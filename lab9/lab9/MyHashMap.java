package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 *
 * @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;
    private static final double MIN_LF = 0.25;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /**
     * Computes the hash function of the given key. Consists of
     * computing the hashcode, followed by modding by the number of buckets.
     * To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /*
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        // 根据哈希表的结构确定
        return buckets[hash(key)].get(key);
    }

    private void resize(int capacity) {
        ArrayMap<K, V>[] temp = new ArrayMap[buckets.length];
        int tempSize = this.size;
        System.arraycopy(buckets, 0, temp, 0, buckets.length);
        buckets = new ArrayMap[capacity];
        clear();
        // 重新计算之前每个元素在新hash表中的位置
        for (ArrayMap<K, V> arraymap : temp) {
            for (K key : arraymap) {
                buckets[hash(key)].put(key, arraymap.get(key));
            }
        }
        this.size = tempSize;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (loadFactor() > MAX_LF) {
            resize(buckets.length * 2);
        }
        // 没有的话就重新赋值且size++,一定要在赋值前先判断这个！！
        if (buckets[hash(key)].get(key) == null) {
            size++;
        }
        // 如果之前的哈希表里有这个key就重新赋值
        buckets[hash(key)].put(key, value);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (ArrayMap<K, V> map : buckets) {
            for (K key : map) {
                set.add(key);
            }
        }
        return set;
    }

    /*
     * Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key) {
        if (get(key) != null) {
            // 判断是否需要resize
            if (loadFactor() < MIN_LF) {
                resize(buckets.length / 2);
            }
            size--;
            return buckets[hash(key)].remove(key);
        }
        return null;
    }

    /*
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value) {
        if (get(key) == value) {
            if (loadFactor() < MIN_LF) {
                resize(buckets.length / 2);
            }
            size--;
            return buckets[hash(key)].remove(key, value);
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        Set<K> set = keySet();
        return set.iterator();
    }
}

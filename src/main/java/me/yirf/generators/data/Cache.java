package me.yirf.generators.data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Cache<K, V> {

    private ConcurrentHashMap<K, V> data = new ConcurrentHashMap<K, V>();

    public synchronized void set(K key, V value) {
        data.put(key, value);
    }

    public synchronized V get(K key) {
        return data.get(key);
    }

    public synchronized void remove(K key) {
        data.remove(key);
    }

    public synchronized Set<K> keys() {
        return new HashSet<>(data.keySet());
    }

    public synchronized List<K> values() {
        return new ArrayList<>(data.keySet());
    }
}

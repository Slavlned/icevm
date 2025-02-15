package com.slavlend.Vm.Optimization;

import lombok.Getter;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Getter
public class FasterMap<K, V> {
    private final HashMap<K, Integer> keyIndexMap;
    private final ArrayList<K> keys;
    private final ArrayList<V> values;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public FasterMap() {
        this.keyIndexMap = new HashMap<>();
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    public FasterMap(FasterMap<K, V> other) {
        this.keyIndexMap = new HashMap<>(other.keyIndexMap);
        this.keys = new ArrayList<>(other.keys);
        this.values = new ArrayList<>(other.values);
    }

    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            if (keyIndexMap.containsKey(key)) {
                values.set(keyIndexMap.get(key), value);
            } else {
                keyIndexMap.put(key, keys.size());
                keys.add(key);
                values.add(value);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean containsKey(K key) {
        lock.readLock().lock();
        try {
            return keyIndexMap.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public V get(K key) {
        lock.readLock().lock();
        try {
            Integer index = keyIndexMap.get(key);
            return (index != null) ? values.get(index) : null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void remove(K key) {
        lock.writeLock().lock();
        try {
            Integer index = keyIndexMap.remove(key);
            if (index != null) {
                int lastIndex = keys.size() - 1;
                if (index < lastIndex) {
                    K lastKey = keys.get(lastIndex);
                    V lastValue = values.get(lastIndex);

                    keys.set(index, lastKey);
                    values.set(index, lastValue);
                    keyIndexMap.put(lastKey, index);
                }
                keys.remove(lastIndex);
                values.remove(lastIndex);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void putAll(FasterMap<K, V> other) {
        lock.writeLock().lock();
        try {
            for (K key : other.keys) {
                put(key, other.get(key));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<V> values() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(values);
        } finally {
            lock.readLock().unlock();
        }
    }
}

package com.fasfood.client.support;

import java.time.Duration;

public interface Cacher<K, V> {
    void put(K key, V value, Duration duration);

    default void put(K key, V value) {
        this.put(key, value, null);
    }

    V get(K key);

    void remove(K key);

    boolean hasKey(K key);
}

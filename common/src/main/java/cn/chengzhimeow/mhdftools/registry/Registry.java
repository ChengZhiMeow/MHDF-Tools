package cn.chengzhimeow.mhdftools.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Registry<K, V> {
    protected final Map<K, V> registry = this.defaultRegistry();

    public Map<K, V> defaultRegistry() {
        return new ConcurrentHashMap<>();
    }

    public boolean replace() {
        return false;
    }

    public boolean has(K key) {
        return this.registry.containsKey(key);
    }

    public void register(K key, V value) {
        if (!this.replace() && this.has(key)) {
            throw new IllegalArgumentException(key + " is already registered");
        }
        this.registry.put(key, value);
    }

    public void unregister(K key) {
        if (!this.has(key)) {
            throw new IllegalArgumentException(key + " is not registered");
        }
        this.registry.remove(key);
    }

    public V get(K key) {
        return this.registry.get(key);
    }

    public V getOrDefault(K key, V defaultValue) {
        return this.registry.getOrDefault(key, defaultValue);
    }

    public K getId(V value) {
        for (Map.Entry<K, V> entry : this.registry.entrySet()) {
            return entry.getKey();
        }
        return null;
    }
}


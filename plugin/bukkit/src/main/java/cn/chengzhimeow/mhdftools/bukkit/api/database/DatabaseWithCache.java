package cn.chengzhimeow.mhdftools.bukkit.api.database;

import net.nyana.cache.service.CacheService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DatabaseWithCache<V, K> {
    private final CacheService<String, V> cache;
    private final Function<String, K> keyByString;
    private final Function<V, String> valueKey;
    private final Function<K, V> databaseGet;
    private final Supplier<java.util.List<V>> databaseList;

    public DatabaseWithCache(
            CacheService<String, V> cache,
            Function<String, K> keyByString,
            Function<V, String> valueKey,
            Function<K, V> databaseGet,
            Supplier<java.util.List<V>> databaseList
    ) {
        this.cache = cache;
        this.keyByString = keyByString;
        this.valueKey = valueKey;
        this.databaseGet = databaseGet;
        this.databaseList = databaseList;
    }

    public void put(V value) {
        this.cache.put(this.valueKey.apply(value), value);
    }

    public V get(String key) {
        V cachedValue = this.cache.get(key);
        if (cachedValue != null) return cachedValue;

        V databaseValue = this.databaseGet.apply(this.keyByString.apply(key));
        if (databaseValue == null) {
            return null;
        }

        this.cache.put(key, databaseValue);
        return databaseValue;
    }

    public Map<String, V> entries() {
        Map<String, V> entries = new LinkedHashMap<>();
        for (V value : this.databaseList.get()) {
            entries.put(this.valueKey.apply(value), value);
        }
        entries.putAll(this.cache.entries());
        entries.forEach((key, value) -> {
            if (!this.cache.containsKey(key)) this.cache.put(key, value);
        });
        return entries;
    }

    public void removeCacheOnly(V value) {
        this.cache.remove(this.valueKey.apply(value));
    }
}

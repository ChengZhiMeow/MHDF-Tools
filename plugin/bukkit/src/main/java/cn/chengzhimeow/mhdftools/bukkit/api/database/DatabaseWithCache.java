package cn.chengzhimeow.mhdftools.bukkit.api.database;

import net.nyana.cache.service.CacheService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DatabaseWithCache<V, K> implements AutoCloseable {
    private final CacheService<String, V> cache;
    private final Function<String, K> keyByString;
    private final Function<V, String> valueKey;
    private final Function<K, V> databaseGet;
    private final Supplier<java.util.List<V>> databaseList;
    private final Consumer<V> databaseUpdate;
    private final Consumer<V> databaseDelete;

    public DatabaseWithCache(
            CacheService<String, V> cache,
            Function<String, K> keyByString,
            Function<V, String> valueKey,
            Function<K, V> databaseGet,
            Supplier<java.util.List<V>> databaseList,
            Consumer<V> databaseUpdate,
            Consumer<V> databaseDelete
    ) {
        this.cache = cache;
        this.keyByString = keyByString;
        this.valueKey = valueKey;
        this.databaseGet = databaseGet;
        this.databaseList = databaseList;
        this.databaseUpdate = databaseUpdate;
        this.databaseDelete = databaseDelete;
    }

    public void init() {
        this.cache.init();
    }

    public void put(String key, V value) {
        this.databaseUpdate.accept(value);
        this.cache.put(key, value);
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

    public void remove(String key) {
        V value = this.cache.get(key);
        if (value == null) {
            value = this.databaseGet.apply(this.keyByString.apply(key));
        }
        if (value != null) {
            this.databaseDelete.accept(value);
        }
        this.cache.remove(key);
    }

    @Override
    public void close() throws Exception {
        if (this.cache instanceof AutoCloseable closeable) {
            closeable.close();
        }
    }
}

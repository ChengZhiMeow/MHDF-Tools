package cn.chengzhimeow.mhdftools.bukkit.api.database;

import net.nyana.cache.NyanaCache;
import net.nyana.cache.hashmap.HashMapCacheService;
import net.nyana.cache.redis.RedisCacheService;
import net.nyana.cache.redis.client.RedisClient;
import net.nyana.cache.serialization.CacheSerializer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DatabaseWithCache<V, K> implements AutoCloseable {
    private final CacheSerializer<V> serializer;
    private final HashMapCacheService<String, byte[]> local;
    private final RedisCacheService<byte[]> redis;
    private final Function<String, K> keyByString;
    private final Function<V, String> valueKey;
    private final Function<K, V> databaseGet;
    private final Supplier<java.util.List<V>> databaseList;
    private final Consumer<V> databaseUpdate;
    private final Consumer<V> databaseDelete;

    @SuppressWarnings("unchecked")
    public DatabaseWithCache(
            NyanaCache cache,
            RedisClient redisClient,
            String namespace,
            Class<V> type,
            Function<String, K> keyByString,
            Function<V, String> valueKey,
            Function<K, V> databaseGet,
            Supplier<java.util.List<V>> databaseList,
            Consumer<V> databaseUpdate,
            Consumer<V> databaseDelete
    ) {
        CacheSerializer<?> serializer = cache.serializationRegistry.get(type);
        if (serializer == null) {
            throw new IllegalArgumentException("No cache serializer registered for " + type.getName());
        }

        this.serializer = (CacheSerializer<V>) serializer;
        this.local = new HashMapCacheService<>(cache);
        this.redis = redisClient == null ? null : new RedisCacheService<>(cache, redisClient, namespace, false, false, byte[].class);
        this.keyByString = keyByString;
        this.valueKey = valueKey;
        this.databaseGet = databaseGet;
        this.databaseList = databaseList;
        this.databaseUpdate = databaseUpdate;
        this.databaseDelete = databaseDelete;
    }

    public void init() {
        if (this.redis == null) {
            return;
        }
        this.redis.entries().forEach((key, value) -> this.local.put(key, value, this.redis.remainingExpireSeconds(key)));
    }

    public void update(V value) {
        this.databaseUpdate.accept(value);
        this.put(value);
    }

    public void put(V value) {
        String key = this.valueKey.apply(value);
        byte[] bytes = this.serializer.toBytes(value);
        if (this.redis != null) {
            this.redis.put(key, bytes);
        }
        this.local.put(key, bytes);
    }

    public V get(String key) {
        byte[] localValue = this.local.get(key);
        if (localValue != null) {
            return this.serializer.byBytes(localValue);
        }

        if (this.redis != null) {
            byte[] redisValue = this.redis.get(key);
            if (redisValue != null) {
                this.local.put(key, redisValue, this.redis.remainingExpireSeconds(key));
                return this.serializer.byBytes(redisValue);
            }
        }

        V databaseValue = this.databaseGet.apply(this.keyByString.apply(key));
        if (databaseValue == null) {
            return null;
        }

        byte[] bytes = this.serializer.toBytes(databaseValue);
        if (this.redis != null) {
            this.redis.put(key, bytes);
        }
        this.local.put(key, bytes);
        return databaseValue;
    }

    public Map<String, V> entries() {
        Map<String, V> entries = new LinkedHashMap<>();
        for (V value : this.databaseList.get()) {
            entries.put(this.valueKey.apply(value), value);
        }
        if (this.redis != null) {
            this.redis.entries().forEach((key, value) -> entries.put(key, this.serializer.byBytes(value)));
        }
        this.local.entries().forEach((key, value) -> entries.put(key, this.serializer.byBytes(value)));
        entries.forEach((key, value) -> {
            byte[] bytes = this.serializer.toBytes(value);
            if (!this.local.containsKey(key)) {
                this.local.put(key, bytes);
            }
            if (this.redis != null && !this.redis.containsKey(key)) {
                this.redis.put(key, bytes);
            }
        });
        return entries;
    }

    public void remove(String key) {
        V value = this.get(key);
        if (value != null) {
            this.databaseDelete.accept(value);
        }
        if (this.redis != null) {
            this.redis.remove(key);
        }
        this.local.remove(key);
    }

    public void remove(V value) {
        this.remove(this.valueKey.apply(value));
    }

    public void removeCacheOnly(V value) {
        String key = this.valueKey.apply(value);
        if (this.redis != null) {
            this.redis.remove(key);
        }
        this.local.remove(key);
    }

    @Override
    public void close() {
        if (this.redis != null) {
            this.redis.close();
        }
    }
}

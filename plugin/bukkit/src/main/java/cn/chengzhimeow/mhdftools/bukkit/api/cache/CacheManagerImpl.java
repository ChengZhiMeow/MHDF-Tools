package cn.chengzhimeow.mhdftools.bukkit.api.cache;

import cn.chengzhimeow.mhdftools.bukkit.common.config.CacheSetting;
import net.nyana.cache.NyanaCache;
import net.nyana.cache.hashmap.HashMapCacheService;
import net.nyana.cache.redis.RedisHashMapCacheService;
import net.nyana.cache.redis.client.RedisClient;
import net.nyana.cache.redis.client.RedisConfig;
import net.nyana.cache.serialization.CacheSerializer;
import net.nyana.cache.service.CacheService;

import java.util.ArrayList;
import java.util.List;

public final class CacheManagerImpl implements CacheManager, AutoCloseable {
    private final NyanaCache cache = new NyanaCache();
    private final RedisClient redisClient;
    private final List<AutoCloseable> closeables = new ArrayList<>();

    public CacheManagerImpl() {
        this.redisClient = CacheSetting.getInstance().getConfig().isRedis() ? new RedisClient(this.redisConfig()) : null;
    }

    @Override
    public NyanaCache getCache() {
        return this.cache;
    }

    @Override
    public <V> void registerSerializer(Class<V> type, CacheSerializer<V> serializer) {
        this.cache.serializationRegistry.register(type, serializer);
    }

    @Override
    public <V> CacheService<String, V> createCache(String namespace, Class<V> type) {
        CacheService<String, V> service = this.redisClient == null
                                          ? new HashMapCacheService<>(this.cache)
                                          : new RedisHashMapCacheService<>(this.cache, this.redisClient, "mhdftools:" + CacheSetting.getInstance().getConfig().server() + ":" + namespace, true);
        if (namespace.startsWith("database:")) return service;
        if (service instanceof AutoCloseable closeable)
            this.closeables.add(closeable);
        return service;
    }

    private RedisConfig redisConfig() {
        CacheSetting.Config.Redis redis = CacheSetting.getInstance().getConfig().redis();
        String host = redis.host();
        String hostName = host.contains(":") ? host.substring(0, host.lastIndexOf(":")) : host;
        String port = host.contains(":") ? host.substring(host.lastIndexOf(":") + 1) : "";
        RedisConfig.Builder builder = RedisConfig.builder(hostName);
        try {
            if (!port.isBlank()) {
                builder.port(Integer.parseInt(port));
            }
        } catch (NumberFormatException ignored) {
        }

        if (redis.user() != null && redis.password() != null) {
            builder.auth(redis.user(), redis.password());
        } else if (redis.user() != null) {
            builder.username(redis.user());
        } else if (redis.password() != null) {
            builder.password(redis.password());
        }
        return builder.build();
    }

    @Override
    public void close() {
        for (AutoCloseable closeable : this.closeables) {
            try {
                closeable.close();
            } catch (Throwable ignored) {
            }
        }
        this.closeables.clear();
        if (this.redisClient != null) this.redisClient.close();
    }
}

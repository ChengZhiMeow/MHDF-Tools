package cn.chengzhimeow.mhdftools.bukkit.api.cache;

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
    private final CacheSettings settings;
    private final RedisClient redisClient;
    private final List<AutoCloseable> closeables = new ArrayList<>();

    public CacheManagerImpl(CacheSettings settings) {
        this.settings = settings;
        this.redisClient = settings.isRedis() ? new RedisClient(this.redisConfig(settings)) : null;
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
                                          : new RedisHashMapCacheService<>(this.cache, this.redisClient, "mhdftools:" + this.settings.getServerId() + ":" + namespace, type);
        service.init();
        if (service instanceof AutoCloseable closeable) {
            this.closeables.add(closeable);
        }
        return service;
    }

    private RedisConfig redisConfig(CacheSettings settings) {
        CacheSettings.RedisSettings redis = settings.getRedis();
        String host = redis.getHost();
        String hostName = host.contains(":") ? host.substring(0, host.lastIndexOf(":")) : host;
        String port = host.contains(":") ? host.substring(host.lastIndexOf(":") + 1) : "";
        RedisConfig.Builder builder = RedisConfig.builder(hostName);
        try {
            if (!port.isBlank()) {
                builder.port(Integer.parseInt(port));
            }
        } catch (NumberFormatException ignored) {
        }

        if (redis.getUser() != null && redis.getPassword() != null) {
            builder.auth(redis.getUser(), redis.getPassword());
        } else if (redis.getUser() != null) {
            builder.username(redis.getUser());
        } else if (redis.getPassword() != null) {
            builder.password(redis.getPassword());
        }
        return builder.build();
    }

    @Override
    public void close() {
        for (AutoCloseable closeable : this.closeables) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
        this.closeables.clear();
        if (this.redisClient != null) this.redisClient.close();
    }
}

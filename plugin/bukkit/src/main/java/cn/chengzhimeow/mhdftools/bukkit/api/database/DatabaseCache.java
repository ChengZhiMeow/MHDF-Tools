package cn.chengzhimeow.mhdftools.bukkit.api.database;

import cn.chengzhimeow.mhdftools.api.entity.database.data.*;
import cn.chengzhimeow.mhdftools.bukkit.api.database.serializer.*;
import net.nyana.cache.NyanaCache;
import net.nyana.cache.redis.client.RedisClient;
import net.nyana.cache.redis.client.RedisConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DatabaseCache implements AutoCloseable {
    private final NyanaCache cache = new NyanaCache();
    private final CacheSettings settings;
    private final RedisClient redisClient;
    private final List<DatabaseWithCache<?, ?>> caches = new ArrayList<>();

    public DatabaseCache(CacheSettings settings) {
        this.settings = settings;
        this.registerSerializers();
        this.redisClient = settings.isRedis() ? new RedisClient(this.redisConfig()) : null;
    }

    public <V, K> DatabaseWithCache<V, K> create(
            String name,
            Class<V> type,
            Function<String, K> keyByString,
            Function<V, String> valueKey,
            Function<K, V> databaseGet,
            Supplier<List<V>> databaseList,
            Consumer<V> databaseUpdate,
            Consumer<V> databaseDelete
    ) {
        DatabaseWithCache<V, K> cache = new DatabaseWithCache<>(
                this.cache,
                this.redisClient,
                "mhdftools:" + this.settings.getServer() + ":" + name,
                type,
                keyByString,
                valueKey,
                databaseGet,
                databaseList,
                databaseUpdate,
                databaseDelete
        );
        cache.init();
        this.caches.add(cache);
        return cache;
    }

    private void registerSerializers() {
        this.cache.serializationRegistry.register(PlayerData.class, new PlayerDataSerializer());
        this.cache.serializationRegistry.register(EconomyData.class, new EconomyDataSerializer());
        this.cache.serializationRegistry.register(FlyStatus.class, new FlyStatusSerializer());
        this.cache.serializationRegistry.register(PvpStatus.class, new PvpStatusSerializer());
        this.cache.serializationRegistry.register(VanishStatus.class, new VanishStatusSerializer());
        this.cache.serializationRegistry.register(NickData.class, new NickDataSerializer());
        this.cache.serializationRegistry.register(HomeData.class, new HomeDataSerializer());
        this.cache.serializationRegistry.register(IgnoreData.class, new IgnoreDataSerializer());
        this.cache.serializationRegistry.register(WarpData.class, new WarpDataSerializer());
        this.cache.serializationRegistry.register(BackData.class, new BackDataSerializer());
    }

    private RedisConfig redisConfig() {
        CacheSettings.RedisSettings redis = this.settings.getRedis();
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
        this.caches.forEach(DatabaseWithCache::close);
        this.caches.clear();
        if (this.redisClient != null) {
            this.redisClient.close();
        }
    }
}

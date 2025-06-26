package cn.chengzhiya.mhdftools.manager.cache.impl;

import cn.chengzhiya.mhdftools.entity.config.CacheConfig;
import cn.chengzhiya.mhdftools.manager.cache.AbstractCacheManager;
import cn.chengzhiya.mhdftools.manager.redis.RedisClient;
import io.lettuce.core.api.async.RedisAsyncCommands;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.Set;

@Getter
public final class RedisCacheManager extends AbstractCacheManager {
    private RedisClient redisClient;

    public RedisCacheManager(CacheConfig cacheConfig) {
        super(cacheConfig);
    }

    @Override
    public void init() {
        this.redisClient = new RedisClient(getCacheConfig().getServerId(), getCacheConfig().getRedisConfig());
        this.redisClient.connect();
    }

    @Override
    public void close() {
        if (this.getRedisClient() != null) {
            this.getRedisClient().close();
        }
    }

    @Override
    public void put(String table, String key, String value) {
        String prefix = this.getPrefix() + table;

        RedisAsyncCommands<String, String> command = this.getRedisClient().getRedisConnection().async();
        command.set(prefix + ":" + key, value);
    }

    @Override
    public void remove(String table, String key) {
        String prefix = this.getPrefix() + table;

        RedisAsyncCommands<String, String> command = this.getRedisClient().getRedisConnection().async();
        command.del(prefix + ":" + key);
    }

    @Override
    @SneakyThrows
    public String get(String table, String key) {
        String prefix = this.getPrefix() + table;

        RedisAsyncCommands<String, String> command = this.getRedisClient().getRedisConnection().async();
        return command.get(prefix + ":" + key).get();
    }

    @Override
    @SneakyThrows
    public Set<String> keys(String table) {
        String prefix = this.getPrefix() + table;

        RedisAsyncCommands<String, String> command = this.getRedisClient().getRedisConnection().async();
        return new HashSet<>(command.keys(prefix + ":*").get());
    }
}

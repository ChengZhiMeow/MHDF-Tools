package cn.chengzhimeow.mhdftools.bukkit.manager.cache.impl;

import cn.chengzhimeow.mhdftools.bukkit.entity.config.CacheConfig;
import cn.chengzhimeow.mhdftools.bukkit.manager.cache.CacheManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.redis.RedisClient;
import io.lettuce.core.api.async.RedisAsyncCommands;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.Set;

@Getter
public final class RedisCacheManager extends CacheManager {
    private RedisClient redisClient;

    public RedisCacheManager(CacheConfig cacheConfig) {
        super(cacheConfig);
    }

    @Override
    public void init() {
        this.redisClient = new RedisClient(super.getCacheConfig().getServerId(), super.getCacheConfig().getRedisConfig());
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

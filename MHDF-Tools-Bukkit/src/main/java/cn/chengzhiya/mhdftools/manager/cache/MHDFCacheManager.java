package cn.chengzhiya.mhdftools.manager.cache;

import cn.chengzhiya.mhdftools.entity.config.CacheConfig;
import cn.chengzhiya.mhdftools.entity.config.RedisConfig;
import cn.chengzhiya.mhdftools.manager.cache.impl.MapCacheManager;
import cn.chengzhiya.mhdftools.manager.cache.impl.RedisCacheManager;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;

@Getter
public final class MHDFCacheManager implements CacheManager {
    private CacheManager cacheManager;

    public MHDFCacheManager() {
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("cacheSettings");
        if (config == null) {
            return;
        }

        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setHost(config.getString("redis.host"));
        redisConfig.setUser(config.getString("redis.user"));
        redisConfig.setPassword(config.getString("redis.password"));

        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setType(config.getString("type"));
        cacheConfig.setServerId(config.getString("server"));
        cacheConfig.setRedisConfig(redisConfig);

        this.cacheManager = switch (cacheConfig.getType()) {
            case "map" -> new MapCacheManager(cacheConfig);
            case "redis" -> new RedisCacheManager(cacheConfig);
            default -> throw new RuntimeException("不兼容的缓存类型");
        };
    }

    @Override
    public void init() {
        this.getCacheManager().init();
    }

    @Override
    public void close() {
        this.getCacheManager().close();
    }

    @Override
    public void put(String table, String key, String value) {
        this.getCacheManager().put(table, key, value);
    }

    @Override
    public void remove(String table, String key) {
        this.getCacheManager().remove(table, key);
    }

    @Override
    public String get(String table, String key) {
        return this.getCacheManager().get(table, key);
    }

    @Override
    public Set<String> keys(String table) {
        return this.getCacheManager().keys(table);
    }
}

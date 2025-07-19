package cn.chengzhiya.mhdftools.manager.cache;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.config.CacheConfig;
import cn.chengzhiya.mhdftools.entity.config.RedisConfig;
import cn.chengzhiya.mhdftools.manager.cache.impl.MapCacheManager;
import cn.chengzhiya.mhdftools.manager.cache.impl.RedisCacheManager;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public final class MHDFCacheManager {
    private CacheManager cacheManager;

    public MHDFCacheManager() {
        ConfigurationSection config = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("cacheSettings");
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
}

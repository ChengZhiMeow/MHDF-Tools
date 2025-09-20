package cn.chengzhimeow.mhdftools.bukkit.manager.cache;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.entity.config.CacheConfig;
import cn.chengzhimeow.mhdftools.bukkit.entity.config.RedisConfig;
import cn.chengzhimeow.mhdftools.bukkit.manager.cache.impl.MapCacheManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.cache.impl.RedisCacheManager;
import lombok.Getter;

@Getter
public final class MHDFCacheManager {
    private CacheManager cacheManager;

    public MHDFCacheManager() {
        ConfigurationSection config = ConfigSetting.getSettingInstance().getData().getConfigurationSection("cacheSettings");
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

package cn.chengzhimeow.mhdftools.bukkit.entity.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class CacheConfig {
    private String type;
    private String serverId;
    private RedisConfig redisConfig;
}

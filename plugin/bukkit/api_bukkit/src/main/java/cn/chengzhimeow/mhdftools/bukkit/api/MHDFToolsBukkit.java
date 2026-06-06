package cn.chengzhimeow.mhdftools.bukkit.api;

import cn.chengzhimeow.mhdftools.bukkit.api.cache.CacheManager;
import cn.chengzhimeow.mhdftools.bukkit.api.manager.ItemManager;
import cn.chengzhimeow.mhdftools.bukkit.api.redis.RedisManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MHDFToolsBukkit extends JavaPlugin {
    @Getter
    @Setter
    private static MHDFToolsBukkit instance;

    public abstract RedisManager getRedisManager();

    public abstract CacheManager getCacheManager();

    public abstract ItemManager getItemManager();
}

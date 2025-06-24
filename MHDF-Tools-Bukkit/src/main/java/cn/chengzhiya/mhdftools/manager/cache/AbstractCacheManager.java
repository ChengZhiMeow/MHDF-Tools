package cn.chengzhiya.mhdftools.manager.cache;

import cn.chengzhiya.mhdftools.entity.config.CacheConfig;
import lombok.Getter;

@Getter
public abstract class AbstractCacheManager implements CacheManager {
    private final CacheConfig cacheConfig;

    public AbstractCacheManager(CacheConfig config) {
        this.cacheConfig = config;
    }

    /**
     * 获取缓存前缀
     *
     * @return 缓存前缀
     */
    public String getPrefix() {
        return getCacheConfig().getServerId() + "mhdf-tools-";
    }
}

package cn.chengzhiya.mhdftools.manager.cache;

import cn.chengzhiya.mhdftools.entity.config.CacheConfig;
import lombok.Getter;

import java.util.Set;

@Getter
public abstract class CacheManager {
    private final CacheConfig cacheConfig;

    public CacheManager(CacheConfig config) {
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

    /**
     * 初始化缓存
     */
    abstract public void init();

    /**
     * 关闭缓存
     */
    abstract public void close();

    /**
     * 修改指定表id的表下指定key的缓存数据
     *
     * @param table 表id
     * @param key   写入的key
     * @param value 写入的值
     */
    abstract public void put(String table, String key, String value);

    /**
     * 删除指定表id的表下指定key的缓存数据
     *
     * @param table 表id
     * @param key   删除的key
     */
    abstract public void remove(String table, String key);

    /**
     * 读取指定表id的表下指定key的缓存数据
     *
     * @param table 表id
     * @param key   key
     * @return 缓存数据
     */
    abstract public String get(String table, String key);

    /**
     * 读取指定表id的表 的缓存key列表
     *
     * @param table 表id
     * @return 缓存key列表
     */
    abstract public Set<String> keys(String table);
}

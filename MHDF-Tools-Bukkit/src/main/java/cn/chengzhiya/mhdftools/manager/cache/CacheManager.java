package cn.chengzhiya.mhdftools.manager.cache;

import java.util.Set;

public interface CacheManager {
    /**
     * 初始化缓存
     */
    void init();

    /**
     * 关闭缓存
     */
    void close();

    /**
     * 修改指定表id的表下指定key的缓存数据
     *
     * @param table 表id
     * @param key   写入的key
     * @param value 写入的值
     */
    void put(String table, String key, String value);

    /**
     * 删除指定表id的表下指定key的缓存数据
     *
     * @param table 表id
     * @param key   删除的key
     */
    void remove(String table, String key);

    /**
     * 读取指定表id的表下指定key的缓存数据
     *
     * @param table 表id
     * @param key   key
     * @return 缓存数据
     */
    String get(String table, String key);

    /**
     * 读取指定表id的表 的缓存key列表
     *
     * @param table 表id
     * @return 缓存key列表
     */
    Set<String> keys(String table);
}

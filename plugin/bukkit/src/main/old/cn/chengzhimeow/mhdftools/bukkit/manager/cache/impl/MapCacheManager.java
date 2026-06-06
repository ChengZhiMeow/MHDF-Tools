package cn.chengzhimeow.mhdftools.bukkit.manager.cache.impl;

import cn.chengzhimeow.mhdftools.bukkit.entity.config.CacheConfig;
import cn.chengzhimeow.mhdftools.bukkit.manager.cache.CacheManager;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class MapCacheManager extends CacheManager {
    private Map<String, Map<String, String>> map;

    public MapCacheManager(CacheConfig cacheConfig) {
        super(cacheConfig);
    }

    @Override
    public void init() {
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public void close() {
        if (this.getMap() != null) {
            this.map.clear();
        }
    }

    @Override
    public void put(String table, String key, String value) {
        String prefix = this.getPrefix() + table;

        Map<String, String> map = Objects.requireNonNullElse(this.getMap().get(prefix), new ConcurrentHashMap<>());
        map.put(key, value);

        this.getMap().put(prefix, map);
    }

    @Override
    public void remove(String table, String key) {
        String prefix = this.getPrefix() + table;

        Map<String, String> map = Objects.requireNonNullElse(this.getMap().get(prefix), new ConcurrentHashMap<>());
        map.remove(key);

        this.getMap().put(prefix, map);
    }

    @Override
    public String get(String table, String key) {
        String prefix = this.getPrefix() + table;

        Map<String, String> map = Objects.requireNonNullElse(this.getMap().get(prefix), new ConcurrentHashMap<>());
        return map.get(key);
    }

    @Override
    public Set<String> keys(String table) {
        String prefix = this.getPrefix() + table;

        Map<String, String> map = Objects.requireNonNullElse(this.getMap().get(prefix), new ConcurrentHashMap<>());
        return map.keySet();
    }
}

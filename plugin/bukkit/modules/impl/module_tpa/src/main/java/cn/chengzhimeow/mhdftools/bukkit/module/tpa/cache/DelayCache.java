package cn.chengzhimeow.mhdftools.bukkit.module.tpa.cache;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import net.nyana.cache.service.CacheService;

public final class DelayCache {
    private CacheService<String, Long> cache;

    public void init() {
        this.cache = MHDFToolsBukkit.getInstance().getCacheManager().createCache("module:tpa:delay", Long.class);
        this.cache.init();
    }

    public void close() {
        if (!(this.cache instanceof AutoCloseable closeable)) return;

        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }

    public Long get(String playerName) {
        return this.cache.get(playerName);
    }

    public void put(String playerName, long endTime, long expireSeconds) {
        this.cache.put(playerName, endTime, expireSeconds);
    }
}

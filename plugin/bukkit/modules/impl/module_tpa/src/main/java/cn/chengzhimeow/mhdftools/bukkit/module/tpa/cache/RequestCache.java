package cn.chengzhimeow.mhdftools.bukkit.module.tpa.cache;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import net.nyana.cache.service.CacheService;

public final class RequestCache {
    private CacheService<String, String> cache;

    public void init() {
        this.cache = MHDFToolsBukkit.getInstance().getCacheManager().createCache("module:tpa:request", String.class);
        this.cache.init();
    }

    public void close() {
        if (!(this.cache instanceof AutoCloseable closeable)) return;

        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }

    public String get(String playerName) {
        return this.cache.get(playerName);
    }

    public void put(String playerName, String targetName, long expireSeconds) {
        this.cache.put(playerName, targetName, expireSeconds);
    }

    public void remove(String playerName) {
        this.cache.remove(playerName);
    }
}

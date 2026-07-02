package cn.chengzhimeow.mhdftools.bukkit.module.core.cache;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import net.nyana.cache.service.CacheService;

public final class ServerTeleportCache {
    private CacheService<String, byte[]> cache;

    public void init() {
        this.cache = MHDFToolsBukkit.getInstance().getCacheManager().createCache("server_teleport", byte[].class);
        this.cache.init();
    }

    public void close() {
        if (!(this.cache instanceof AutoCloseable closeable)) return;

        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }

    public byte[] get(String name) {
        return this.cache.get(name);
    }

    public void put(String name, byte[] data) {
        this.cache.put(name, data, 60L);
    }

    public void remove(String name) {
        this.cache.remove(name);
    }
}

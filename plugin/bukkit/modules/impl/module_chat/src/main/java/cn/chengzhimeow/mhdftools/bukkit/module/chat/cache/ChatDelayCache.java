package cn.chengzhimeow.mhdftools.bukkit.module.chat.cache;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import net.nyana.cache.service.CacheService;

public final class ChatDelayCache {
    private CacheService<String, Long> cache;

    public void init() {
        this.cache = MHDFToolsBukkit.getInstance().getCacheManager().createCache("module:chat:delay", Long.class);
        this.cache.init();
    }

    public void close() {
        if (!(this.cache instanceof AutoCloseable closeable)) return;

        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }

    public void put(String name, int delaySeconds) {
        this.cache.put(name, System.currentTimeMillis() + delaySeconds * 1000L, (long) Math.max(delaySeconds, 1));
    }

    public long remainingSeconds(String name) {
        Long until = this.cache.get(name);
        if (until == null) return 0L;

        long remaining = (until - System.currentTimeMillis() + 999L) / 1000L;
        if (remaining <= 0L) {
            this.cache.remove(name);
            return 0L;
        }
        return remaining;
    }
}

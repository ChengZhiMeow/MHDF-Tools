package cn.chengzhimeow.mhdftools.bukkit.module.chat.cache;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import net.nyana.cache.service.CacheService;

public final class LastChatCache {
    private CacheService<String, String> cache;

    public void init() {
        this.cache = MHDFToolsBukkit.getInstance().getCacheManager().createCache("module:chat:last_chat", String.class);
        this.cache.init();
    }

    public void close() {
        if (!(this.cache instanceof AutoCloseable closeable)) return;

        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }

    public void put(String name, String message) {
        this.cache.put(name, message, 60L);
    }

    public boolean isSpam(String name, String message) {
        String last = this.cache.get(name);
        return last != null && last.equals(message);
    }
}

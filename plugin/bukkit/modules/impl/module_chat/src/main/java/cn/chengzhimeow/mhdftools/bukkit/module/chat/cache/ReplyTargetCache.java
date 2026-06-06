package cn.chengzhimeow.mhdftools.bukkit.module.chat.cache;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import net.nyana.cache.service.CacheService;

public final class ReplyTargetCache {
    private CacheService<String, String> cache;

    public void init() {
        this.cache = MHDFToolsBukkit.getInstance().getCacheManager().createCache("module:chat:reply_target", String.class);
    }

    public String get(String sender) {
        return this.cache.get(sender);
    }

    public void bind(String sender, String target) {
        this.cache.put(sender, target);
        this.cache.put(target, sender);
    }
}

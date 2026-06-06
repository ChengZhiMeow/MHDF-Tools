package cn.chengzhimeow.mhdftools.bukkit.api.cache;

import net.nyana.cache.NyanaCache;
import net.nyana.cache.serialization.CacheSerializer;
import net.nyana.cache.service.CacheService;

public interface CacheManager {
    NyanaCache getCache();

    <V> void registerSerializer(Class<V> type, CacheSerializer<V> serializer);

    <V> CacheService<String, V> createCache(String namespace, Class<V> type);
}

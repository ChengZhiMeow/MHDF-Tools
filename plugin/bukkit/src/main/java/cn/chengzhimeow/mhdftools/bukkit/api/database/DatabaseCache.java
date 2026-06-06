package cn.chengzhimeow.mhdftools.bukkit.api.database;

import cn.chengzhimeow.mhdftools.api.entity.database.data.*;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.api.database.serializer.*;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DatabaseCache {
    public DatabaseCache() {
        this.registerSerializers();
    }

    public <V, K> DatabaseWithCache<V, K> create(
            String name,
            Class<V> type,
            Function<String, K> keyByString,
            Function<V, String> valueKey,
            Function<K, V> databaseGet,
            Supplier<List<V>> databaseList
    ) {
        return new DatabaseWithCache<>(
                MHDFToolsBukkit.getInstance().getCacheManager().createCache("database:" + name, type),
                keyByString,
                valueKey,
                databaseGet,
                databaseList
        );
    }

    private void registerSerializers() {
        MHDFToolsBukkit.getInstance().getCacheManager().registerSerializer(PlayerData.class, new PlayerDataSerializer());
        MHDFToolsBukkit.getInstance().getCacheManager().registerSerializer(EconomyData.class, new EconomyDataSerializer());
        MHDFToolsBukkit.getInstance().getCacheManager().registerSerializer(FlyStatus.class, new FlyStatusSerializer());
        MHDFToolsBukkit.getInstance().getCacheManager().registerSerializer(PvpStatus.class, new PvpStatusSerializer());
        MHDFToolsBukkit.getInstance().getCacheManager().registerSerializer(VanishStatus.class, new VanishStatusSerializer());
        MHDFToolsBukkit.getInstance().getCacheManager().registerSerializer(NickData.class, new NickDataSerializer());
        MHDFToolsBukkit.getInstance().getCacheManager().registerSerializer(HomeData.class, new HomeDataSerializer());
        MHDFToolsBukkit.getInstance().getCacheManager().registerSerializer(IgnoreData.class, new IgnoreDataSerializer());
        MHDFToolsBukkit.getInstance().getCacheManager().registerSerializer(WarpData.class, new WarpDataSerializer());
        MHDFToolsBukkit.getInstance().getCacheManager().registerSerializer(BackData.class, new BackDataSerializer());
    }
}

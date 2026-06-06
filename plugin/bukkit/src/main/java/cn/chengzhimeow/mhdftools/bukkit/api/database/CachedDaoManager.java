package cn.chengzhimeow.mhdftools.bukkit.api.database;

import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class CachedDaoManager<V, K> extends AbstractDaoManager<V, K> {
    private final DatabaseWithCache<V, K> cache;
    private final Function<K, String> keyToString;

    public CachedDaoManager(
            DatabaseManager databaseManager,
            String name,
            Class<V> type,
            Function<K, String> keyToString,
            Function<String, K> keyByString,
            Function<V, String> valueKey
    ) {
        super(databaseManager.getDatabase());
        this.keyToString = keyToString;
        this.cache = databaseManager.getCache().create(
                name,
                type,
                keyByString,
                valueKey,
                super::getById,
                super::getList
        );
    }

    @Override
    public List<V> getList() {
        return new ArrayList<>(this.cache.entries().values());
    }

    @Override
    public V getById(K key) {
        return this.cache.get(this.keyToString.apply(key));
    }

    @Override
    public V getByIdOrDefault(K key, V defaultValue) {
        V value = this.getById(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public void update(V value) {
        super.update(value);
        this.cache.put(value);
    }

    @Override
    public void update(V value, boolean async) {
        if (!async) {
            this.update(value);
            return;
        }

        this.getInstance().getDatabaseThread().execute(() -> this.update(value));
    }

    @Override
    public void delete(V value) {
        super.delete(value);
        this.cache.removeCacheOnly(value);
    }

    @Override
    public void delete(V value, boolean async) {
        if (!async) {
            this.delete(value);
            return;
        }

        this.getInstance().getDatabaseThread().execute(() -> this.delete(value));
    }

    protected List<V> cacheList() {
        return this.getList();
    }
}

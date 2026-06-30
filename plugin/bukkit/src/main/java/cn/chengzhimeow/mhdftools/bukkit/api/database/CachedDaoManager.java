package cn.chengzhimeow.mhdftools.bukkit.api.database;

import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class CachedDaoManager<V, K> extends AbstractDaoManager<V, K> {
    private final DatabaseWithCache<V, K> cache;
    private final Function<K, String> keyToString;
    private final Function<V, String> valueKey;

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
        this.valueKey = valueKey;
        this.cache = databaseManager.getCache().create(
                name,
                type,
                keyByString,
                valueKey,
                super::getById,
                super::getList,
                super::update,
                super::delete
        );
    }

    public void initCache() {
        this.cache.init();
    }

    public void closeCache() throws Exception {
        this.cache.close();
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
        this.cache.put(this.valueKey.apply(value), value);
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
        this.cache.remove(this.valueKey.apply(value));
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

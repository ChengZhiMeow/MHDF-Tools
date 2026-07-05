package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhimeow.mhdftools.api.manager.feature.WarpDataManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.CachedDaoManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;
import lombok.Setter;

@Setter
public final class WarpDataManagerImpl extends CachedDaoManager<WarpData, String> implements WarpDataManager {
    private boolean enable;

    public WarpDataManagerImpl(DatabaseManager databaseManager) {
        super(databaseManager, "warp", WarpData.class, key -> key, key -> key, WarpData::getWarp);
    }

    @Override
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public boolean hasData(String name) {
        return this.getById(name) != null;
    }

    @Override
    public WarpData get(String name) {
        return this.getByIdOrDefault(name, () -> new WarpData(name));
    }
}

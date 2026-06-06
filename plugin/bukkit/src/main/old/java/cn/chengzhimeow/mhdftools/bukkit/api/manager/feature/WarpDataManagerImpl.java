package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhimeow.mhdftools.api.manager.feature.WarpDataManager;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;

public final class WarpDataManagerImpl extends AbstractDaoManager<WarpData, String> implements WarpDataManager {
    public WarpDataManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    public boolean hasData(String name) {
        return super.getById(name) != null;
    }

    @Override
    public WarpData get(String name) {
        return super.getByIdOrDefault(name, new WarpData(name));
    }
}

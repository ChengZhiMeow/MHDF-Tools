package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.database.data.WarpData;

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

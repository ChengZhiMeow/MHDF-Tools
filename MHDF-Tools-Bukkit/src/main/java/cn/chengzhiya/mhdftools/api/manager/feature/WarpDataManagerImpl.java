package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhiya.mhdftools.api.manager.database.AbstractDaoManager;

public final class WarpDataManagerImpl extends AbstractDaoManager<WarpData, String> implements WarpDataManager {
    @Override
    public boolean hasData(String name) {
        return getById(name) != null;
    }

    @Override
    public WarpData get(String name) {
        return getByIdOrDefault(name, new WarpData(name));
    }
}

package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.FlyStatus;
import cn.chengzhiya.mhdftools.api.manager.database.AbstractDaoManager;

import java.util.UUID;

public final class FlyStatusManagerImpl extends AbstractDaoManager<FlyStatus, UUID> implements FlyStatusManager {
    @Override
    public boolean isEnable(MHDFToolsPlayer player) {
        FlyStatus status = getById(player.getUuid());
        return status != null && status.isEnable();
    }

    @Override
    public FlyStatus get(MHDFToolsPlayer player) {
        return getByIdOrDefault(player.getUuid(), new FlyStatus(player));
    }
}

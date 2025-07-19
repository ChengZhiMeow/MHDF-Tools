package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.FlyStatus;

import java.util.UUID;

public final class FlyStatusManagerImpl extends AbstractDaoManager<FlyStatus, UUID> implements FlyStatusManager {
    public FlyStatusManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    public boolean isEnable(MHDFToolsPlayer player) {
        FlyStatus status = super.getById(player.getUuid());
        return status != null && status.isEnable();
    }

    @Override
    public FlyStatus get(MHDFToolsPlayer player) {
        return super.getByIdOrDefault(player.getUuid(), new FlyStatus(player));
    }
}

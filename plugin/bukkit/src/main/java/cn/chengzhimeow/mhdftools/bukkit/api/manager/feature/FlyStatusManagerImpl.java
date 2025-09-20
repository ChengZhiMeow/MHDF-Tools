package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.FlyStatus;
import cn.chengzhimeow.mhdftools.api.manager.feature.FlyStatusManager;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;

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

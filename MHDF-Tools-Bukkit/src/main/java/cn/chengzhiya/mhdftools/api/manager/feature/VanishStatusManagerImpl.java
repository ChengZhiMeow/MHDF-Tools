package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.VanishStatus;

import java.util.UUID;

public final class VanishStatusManagerImpl extends AbstractDaoManager<VanishStatus, UUID> implements VanishStatusManager {
    public VanishStatusManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    public boolean isEnable(MHDFToolsPlayer player) {
        VanishStatus status = super.getById(player.getUuid());
        return status != null && status.isEnable();
    }

    @Override
    public VanishStatus get(MHDFToolsPlayer player) {
        return super.getByIdOrDefault(player.getUuid(), new VanishStatus(player));
    }
}

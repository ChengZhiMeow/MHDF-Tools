package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.VanishStatus;
import cn.chengzhiya.mhdftools.api.manager.database.AbstractDaoManager;

import java.util.UUID;

public final class VanishStatusManagerImpl extends AbstractDaoManager<VanishStatus, UUID> implements VanishStatusManager {
    @Override
    public boolean isEnable(MHDFToolsPlayer player) {
        VanishStatus status = getById(player.getUuid());
        return status != null && status.isEnable();
    }

    @Override
    public VanishStatus get(MHDFToolsPlayer player) {
        return getByIdOrDefault(player.getUuid(), new VanishStatus(player));
    }
}

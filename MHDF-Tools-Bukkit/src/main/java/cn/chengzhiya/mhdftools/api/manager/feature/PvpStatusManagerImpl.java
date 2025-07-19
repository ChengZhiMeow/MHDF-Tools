package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.PvpStatus;

import java.util.UUID;

public final class PvpStatusManagerImpl extends AbstractDaoManager<PvpStatus, UUID> implements PvpStatusManager {
    public PvpStatusManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    public boolean isEnable(MHDFToolsPlayer player) {
        PvpStatus status = super.getById(player.getUuid());
        return status != null && status.isEnable();
    }

    @Override
    public PvpStatus get(MHDFToolsPlayer player) {
        return super.getByIdOrDefault(player.getUuid(), new PvpStatus(player));
    }

    @Override
    public boolean getDefaultValue() {
        return Main.instance.getConfigManager().getConfigManager().getData().getBoolean("pvpSettings.default");
    }
}

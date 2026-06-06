package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.PvpStatus;
import cn.chengzhimeow.mhdftools.api.manager.feature.PvpStatusManager;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;

import java.util.UUID;

public final class PvpStatusManagerImpl extends AbstractDaoManager<PvpStatus, UUID> implements PvpStatusManager {
    public PvpStatusManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    public boolean isEnable(MHDFToolsPlayer player) {
        PvpStatus status = this.get(player);
        return status.isEnable();
    }

    @Override
    public PvpStatus get(MHDFToolsPlayer player) {
        return super.getByIdOrDefault(player.getUuid(), new PvpStatus(player));
    }

    @Override
    public boolean getDefaultValue() {
        return ConfigSetting.getInstance().getData().getBoolean("pvpSettings.default");
    }
}

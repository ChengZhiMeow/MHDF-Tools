package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.PvpStatus;
import cn.chengzhimeow.mhdftools.api.manager.feature.PvpStatusManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.CachedDaoManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;
import cn.chengzhimeow.mhdftools.bukkit.module.pvp.config.ConfigSetting;
import lombok.Setter;

import java.util.UUID;

@Setter
public final class PvpStatusManagerImpl extends CachedDaoManager<PvpStatus, UUID> implements PvpStatusManager {
    private boolean enable;

    public PvpStatusManagerImpl(DatabaseManager databaseManager) {
        super(databaseManager, "pvp", PvpStatus.class, UUID::toString, UUID::fromString, data -> data.getPlayer().toString());
    }

    @Override
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public boolean isEnable(MHDFToolsPlayer player) {
        return this.get(player).isEnable();
    }

    @Override
    public PvpStatus get(MHDFToolsPlayer player) {
        return this.getByIdOrDefault(player.getUuid(), new PvpStatus(player, this.getDefaultValue()));
    }

    @Override
    public boolean getDefaultValue() {
        return this.getConfig().defaultValue();
    }

    private ConfigSetting.Config getConfig() {
        ConfigSetting setting = ConfigSetting.getInstance();
        if (setting.getConfig() == null) {
            setting.saveDefaultFile();
            setting.update();
            setting.reload();
        }
        return setting.getConfig();
    }
}

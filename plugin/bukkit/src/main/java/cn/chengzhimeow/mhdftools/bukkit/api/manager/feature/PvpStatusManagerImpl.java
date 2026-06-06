package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.PvpStatus;
import cn.chengzhimeow.mhdftools.api.manager.feature.PvpStatusManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.CachedDaoManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;

import java.util.UUID;

public final class PvpStatusManagerImpl extends CachedDaoManager<PvpStatus, UUID> implements PvpStatusManager {
    private final DatabaseManager databaseManager;

    public PvpStatusManagerImpl(DatabaseManager databaseManager) {
        super(databaseManager, "pvp", PvpStatus.class, UUID::toString, UUID::fromString, data -> data.getPlayer().toString());
        this.databaseManager = databaseManager;
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
        return this.databaseManager.isDefaultPvp();
    }
}

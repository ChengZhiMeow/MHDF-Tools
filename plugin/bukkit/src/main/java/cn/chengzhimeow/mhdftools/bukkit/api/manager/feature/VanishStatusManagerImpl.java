package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.VanishStatus;
import cn.chengzhimeow.mhdftools.api.manager.feature.VanishStatusManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.CachedDaoManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;

import java.util.UUID;

public final class VanishStatusManagerImpl extends CachedDaoManager<VanishStatus, UUID> implements VanishStatusManager {
    public VanishStatusManagerImpl(DatabaseManager databaseManager) {
        super(databaseManager, "vanish", VanishStatus.class, UUID::toString, UUID::fromString, data -> data.getPlayer().toString());
    }

    @Override
    public boolean isEnable(MHDFToolsPlayer player) {
        VanishStatus status = this.getById(player.getUuid());
        return status != null && status.isEnable();
    }

    @Override
    public VanishStatus get(MHDFToolsPlayer player) {
        return this.getByIdOrDefault(player.getUuid(), new VanishStatus(player));
    }
}

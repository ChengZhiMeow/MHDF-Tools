package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.FlyStatus;
import cn.chengzhimeow.mhdftools.api.manager.feature.FlyStatusManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.CachedDaoManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;
import lombok.Setter;

import java.util.UUID;

@Setter
public final class FlyStatusManagerImpl extends CachedDaoManager<FlyStatus, UUID> implements FlyStatusManager {
    private boolean enable;

    public FlyStatusManagerImpl(DatabaseManager databaseManager) {
        super(databaseManager, "fly", FlyStatus.class, UUID::toString, UUID::fromString, data -> data.getPlayer().toString());
    }

    @Override
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public boolean isEnable(MHDFToolsPlayer player) {
        FlyStatus status = this.getById(player.getUuid());
        return status != null && status.isEnable();
    }

    @Override
    public FlyStatus get(MHDFToolsPlayer player) {
        return this.getByIdOrDefault(player.getUuid(), () -> new FlyStatus(player));
    }
}

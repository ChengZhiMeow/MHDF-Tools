package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.NickData;
import cn.chengzhimeow.mhdftools.api.manager.feature.NickDataManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.CachedDaoManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;

import java.util.UUID;

public final class NickDataManagerImpl extends CachedDaoManager<NickData, UUID> implements NickDataManager {
    public NickDataManagerImpl(DatabaseManager databaseManager) {
        super(databaseManager, "nick", NickData.class, UUID::toString, UUID::fromString, data -> data.getPlayer().toString());
    }

    @Override
    public boolean hasData(MHDFToolsPlayer player) {
        return this.getById(player.getUuid()) != null;
    }

    @Override
    public NickData get(MHDFToolsPlayer player) {
        return this.getByIdOrDefault(player.getUuid(), new NickData(player));
    }
}

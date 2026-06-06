package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.NickData;
import cn.chengzhimeow.mhdftools.api.manager.feature.NickDataManager;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;

import java.util.UUID;

public final class NickDataManagerImpl extends AbstractDaoManager<NickData, UUID> implements NickDataManager {
    public NickDataManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    public boolean hasData(MHDFToolsPlayer player) {
        return super.getById(player.getUuid()) != null;
    }

    @Override
    public NickData get(MHDFToolsPlayer player) {
        return super.getByIdOrDefault(player.getUuid(), new NickData(player));
    }
}

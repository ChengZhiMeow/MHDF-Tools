package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.NickData;

import java.util.UUID;

public final class NickDataManagerImpl extends AbstractDaoManager<NickData, UUID> implements NickDataManager {
    public NickDataManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    public boolean hasData(MHDFToolsPlayer player) {
        return getById(player.getUuid()) != null;
    }

    @Override
    public NickData get(MHDFToolsPlayer player) {
        return getByIdOrDefault(player.getUuid(), new NickData(player));
    }
}

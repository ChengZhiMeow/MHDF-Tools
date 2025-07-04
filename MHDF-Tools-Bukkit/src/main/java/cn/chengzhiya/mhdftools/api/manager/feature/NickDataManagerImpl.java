package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.NickData;
import cn.chengzhiya.mhdftools.api.manager.database.AbstractDaoManager;

import java.util.UUID;

public final class NickDataManagerImpl extends AbstractDaoManager<NickData, UUID> implements NickDataManager {
    @Override
    public boolean hasData(MHDFToolsPlayer player) {
        return getById(player.getUuid()) != null;
    }

    @Override
    public NickData get(MHDFToolsPlayer player) {
        return getByIdOrDefault(player.getUuid(), new NickData(player));
    }
}

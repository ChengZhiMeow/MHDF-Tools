package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.EconomyData;
import cn.chengzhiya.mhdftools.util.math.BigDecimalUtil;

import java.util.UUID;

public final class EconomyDataManagerImpl extends AbstractDaoManager<EconomyData, UUID> implements EconomyDataManager {
    public EconomyDataManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    public String getMoneyName() {
        return Main.instance.getConfigManager().getConfigManager().getData().getString("economySettings.name");
    }

    @Override
    public boolean hasData(MHDFToolsPlayer player) {
        return getById(player.getUuid()) != null;
    }

    @Override
    public EconomyData get(MHDFToolsPlayer player) {
        return getByIdOrDefault(
                player.getUuid(),
                new EconomyData(player, BigDecimalUtil.toBigDecimal(Main.instance.getConfigManager().getConfigManager().getData().getDouble("economySettings.default")))
        );
    }
}

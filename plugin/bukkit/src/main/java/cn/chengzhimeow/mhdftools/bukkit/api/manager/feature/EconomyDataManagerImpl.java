package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.EconomyData;
import cn.chengzhimeow.mhdftools.api.manager.feature.EconomyDataManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.CachedDaoManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;

import java.math.BigDecimal;
import java.util.UUID;

public final class EconomyDataManagerImpl extends CachedDaoManager<EconomyData, UUID> implements EconomyDataManager {
    private final DatabaseManager databaseManager;

    public EconomyDataManagerImpl(DatabaseManager databaseManager) {
        super(databaseManager, "economy", EconomyData.class, UUID::toString, UUID::fromString, data -> data.getPlayer().toString());
        this.databaseManager = databaseManager;
    }

    @Override
    public String getMoneyName() {
        return this.databaseManager.getMoneyName();
    }

    @Override
    public boolean hasData(MHDFToolsPlayer player) {
        return this.getById(player.getUuid()) != null;
    }

    @Override
    public EconomyData get(MHDFToolsPlayer player) {
        EconomyData data = this.getById(player.getUuid());
        if (data != null) {
            return data;
        }

        data = new EconomyData();
        data.setPlayer(player.getUuid());
        data.setMoney(BigDecimal.valueOf(this.databaseManager.getDefaultMoney()));
        return data;
    }
}

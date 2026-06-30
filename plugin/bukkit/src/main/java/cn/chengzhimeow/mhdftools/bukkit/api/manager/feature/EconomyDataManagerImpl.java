package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.EconomyData;
import cn.chengzhimeow.mhdftools.api.manager.feature.EconomyDataManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.CachedDaoManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.config.ConfigSetting;
import lombok.Setter;

import java.util.UUID;

@Setter
public final class EconomyDataManagerImpl extends CachedDaoManager<EconomyData, UUID> implements EconomyDataManager {
    private boolean enable;

    public EconomyDataManagerImpl(DatabaseManager databaseManager) {
        super(databaseManager, "economy", EconomyData.class, UUID::toString, UUID::fromString, data -> data.getPlayer().toString());
    }

    @Override
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public String getMoneyName() {
        return this.getConfig().moneyName();
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
        data.setMoney(this.getConfig().defaultMoney());
        return data;
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

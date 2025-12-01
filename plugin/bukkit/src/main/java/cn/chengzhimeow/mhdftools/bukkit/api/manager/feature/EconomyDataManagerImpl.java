package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.EconomyData;
import cn.chengzhimeow.mhdftools.api.manager.feature.EconomyDataManager;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.math.BigDecimalUtil;
import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;

import java.util.UUID;

public final class EconomyDataManagerImpl extends AbstractDaoManager<EconomyData, UUID> implements EconomyDataManager {
    public EconomyDataManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    public String getMoneyName() {
        return ConfigSetting.getInstance().getData().getString("economySettings.name");
    }

    @Override
    public boolean hasData(MHDFToolsPlayer player) {
        return super.getById(player.getUuid()) != null;
    }

    @Override
    public EconomyData get(MHDFToolsPlayer player) {
        return super.getByIdOrDefault(
                player.getUuid(),
                new EconomyData(player, BigDecimalUtil.toBigDecimal(ConfigSetting.getInstance().getData().getDouble("economySettings.default")))
        );
    }
}

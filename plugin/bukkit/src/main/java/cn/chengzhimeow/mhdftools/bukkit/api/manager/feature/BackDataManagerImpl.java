package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.BackData;
import cn.chengzhimeow.mhdftools.api.manager.feature.BackDataManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.CachedDaoManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;

import java.util.Comparator;
import java.util.List;

public final class BackDataManagerImpl extends CachedDaoManager<BackData, Integer> implements BackDataManager {
    public BackDataManagerImpl(DatabaseManager databaseManager) {
        super(databaseManager, "back", BackData.class, String::valueOf, Integer::valueOf, data -> String.valueOf(data.getId()));
    }

    @Override
    public List<BackData> getList(MHDFToolsPlayer player, int amount) {
        return this.limit(this.cacheList().stream()
                .filter(data -> data.getPlayer().equals(player.getUuid()))
                .sorted(Comparator.comparingInt(BackData::getId).reversed())
                .toList(), amount);
    }

    @Override
    public List<BackData> getList(MHDFToolsPlayer player, String type, int amount) {
        return this.limit(this.cacheList().stream()
                .filter(data -> data.getPlayer().equals(player.getUuid()))
                .filter(data -> data.getType().equals(type))
                .sorted(Comparator.comparingInt(BackData::getId).reversed())
                .toList(), amount);
    }

    private List<BackData> limit(List<BackData> data, int amount) {
        if (amount <= 0 || data.size() <= amount) {
            return data;
        }
        return data.subList(0, amount);
    }
}

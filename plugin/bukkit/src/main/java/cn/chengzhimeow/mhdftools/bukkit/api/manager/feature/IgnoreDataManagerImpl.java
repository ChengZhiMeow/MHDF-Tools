package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.IgnoreData;
import cn.chengzhimeow.mhdftools.api.manager.feature.IgnoreDataManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.CachedDaoManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;
import lombok.Setter;

import java.util.List;

@Setter
public final class IgnoreDataManagerImpl extends CachedDaoManager<IgnoreData, Integer> implements IgnoreDataManager {
    private boolean enable;

    public IgnoreDataManagerImpl(DatabaseManager databaseManager) {
        super(databaseManager, "ignore", IgnoreData.class, String::valueOf, Integer::valueOf, data -> String.valueOf(data.getId()));
    }

    @Override
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public List<IgnoreData> getList(MHDFToolsPlayer player) {
        return this.cacheList().stream()
                .filter(data -> data.getPlayer().equals(player.getUuid()))
                .toList();
    }

    @Override
    public boolean hasData(MHDFToolsPlayer player, MHDFToolsPlayer ignore) {
        return this.getList(player).stream().anyMatch(data -> data.getIgnore().equals(ignore.getUuid()));
    }

    @Override
    public IgnoreData get(MHDFToolsPlayer player, MHDFToolsPlayer ignore) {
        return this.getList(player).stream()
                .filter(data -> data.getIgnore().equals(ignore.getUuid()))
                .findFirst()
                .orElseGet(() -> new IgnoreData(player, ignore));
    }
}

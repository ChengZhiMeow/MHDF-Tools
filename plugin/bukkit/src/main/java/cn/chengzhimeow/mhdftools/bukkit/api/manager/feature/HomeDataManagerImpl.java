package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhimeow.mhdftools.api.manager.feature.HomeDataManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.CachedDaoManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;
import lombok.Setter;

import java.util.List;

@Setter
public final class HomeDataManagerImpl extends CachedDaoManager<HomeData, Integer> implements HomeDataManager {
    private boolean enable;

    public HomeDataManagerImpl(DatabaseManager databaseManager) {
        super(databaseManager, "home", HomeData.class, String::valueOf, Integer::valueOf, data -> String.valueOf(data.getId()));
    }

    @Override
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public List<HomeData> getList(MHDFToolsPlayer player) {
        return this.cacheList().stream()
                .filter(data -> data.getPlayer().equals(player.getUuid()))
                .toList();
    }

    @Override
    public boolean hasData(MHDFToolsPlayer player, String name) {
        return this.getList(player).stream().anyMatch(data -> data.getHome().equalsIgnoreCase(name));
    }

    @Override
    public HomeData get(MHDFToolsPlayer player, String name) {
        return this.getList(player).stream()
                .filter(data -> data.getHome().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> new HomeData(player, name));
    }
}

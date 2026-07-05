package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.PlayerData;
import cn.chengzhimeow.mhdftools.api.manager.feature.PlayerDataManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.CachedDaoManager;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class PlayerDataManagerImpl extends CachedDaoManager<PlayerData, UUID> implements PlayerDataManager {
    public PlayerDataManagerImpl(DatabaseManager databaseManager) {
        super(databaseManager, "player", PlayerData.class, UUID::toString, UUID::fromString, data -> data.getPlayer().toString());
    }

    @Override
    public boolean hasData(MHDFToolsPlayer player) {
        return this.getById(player.getUuid()) != null;
    }

    @Override
    public PlayerData get(MHDFToolsPlayer player) {
        return this.getByIdOrDefault(player.getUuid(), () -> new PlayerData(player.getUuid(), player.getNameOrNull()));
    }

    @Override
    public PlayerData get(String name) {
        return this.cacheList().stream()
                .filter(data -> data.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> new PlayerData(Bukkit.getOfflinePlayer(name).getUniqueId(), name));
    }

    @Override
    public @Nullable PlayerData getOrNull(String name) {
        return this.cacheList().stream()
                .filter(data -> data.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}

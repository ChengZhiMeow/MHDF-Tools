package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.PlayerData;
import cn.chengzhiya.mhdftools.api.manager.database.AbstractDaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.util.UUID;

public final class PlayerDataManagerImpl extends AbstractDaoManager<PlayerData, UUID> implements PlayerDataManager {
    @Override
    public boolean hasData(MHDFToolsPlayer player) {
        return getById(player.getUuid()) != null;
    }

    @Override
    public PlayerData get(MHDFToolsPlayer player) {
        return getByIdOrDefault(
                player.getUuid(),
                player.getPlayer() != null ? new PlayerData(player.getPlayer()) : new PlayerData(player)
        );
    }

    @Override
    @SneakyThrows
    public PlayerData get(String name) {
        QueryBuilder<PlayerData, UUID> queryBuilder = getQueryBuilder();
        queryBuilder.setWhere(
                queryBuilder.where()
                        .eq("name", name)
        );

        return queryFirstOrDefault(queryBuilder, new PlayerData(Bukkit.getOfflinePlayer(name)));
    }
}

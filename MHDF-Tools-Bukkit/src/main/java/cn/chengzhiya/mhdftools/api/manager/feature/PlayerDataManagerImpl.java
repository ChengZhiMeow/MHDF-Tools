package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.PlayerData;
import com.j256.ormlite.stmt.QueryBuilder;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.util.UUID;

public final class PlayerDataManagerImpl extends AbstractDaoManager<PlayerData, UUID> implements PlayerDataManager {
    public PlayerDataManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    public boolean hasData(MHDFToolsPlayer player) {
        return super.getById(player.getUuid()) != null;
    }

    @Override
    public PlayerData get(MHDFToolsPlayer player) {
        return super.getByIdOrDefault(
                player.getUuid(),
                player.getPlayer() != null ? new PlayerData(player.getPlayer()) : new PlayerData(player)
        );
    }

    @Override
    @SneakyThrows
    public PlayerData get(String name) {
        QueryBuilder<PlayerData, UUID> queryBuilder = super.getQueryBuilder();
        queryBuilder.setWhere(
                queryBuilder.where()
                        .eq("name", name)
        );

        return super.queryFirstOrDefault(queryBuilder, new PlayerData(Bukkit.getOfflinePlayer(name)));
    }
}

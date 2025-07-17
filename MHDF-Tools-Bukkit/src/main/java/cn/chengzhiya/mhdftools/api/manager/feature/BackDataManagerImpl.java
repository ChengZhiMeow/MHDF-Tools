package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.BackData;
import com.j256.ormlite.stmt.QueryBuilder;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public final class BackDataManagerImpl extends AbstractDaoManager<BackData, Integer> implements BackDataManager {
    public BackDataManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    @SneakyThrows
    public List<BackData> getList(MHDFToolsPlayer player, int amount) {
        QueryBuilder<BackData, Integer> queryBuilder = super.getQueryBuilder();
        queryBuilder.setWhere(queryBuilder.where()
                .eq("player", player.getUuid())
        );
        queryBuilder.orderBy("id", false);
        if (amount > 0) {
            queryBuilder.limit((long) amount);
        }

        return super.queryForList(queryBuilder, new ArrayList<>());
    }

    @Override
    @SneakyThrows
    public List<BackData> getList(MHDFToolsPlayer player, String type, int amount) {
        QueryBuilder<BackData, Integer> queryBuilder = super.getQueryBuilder();
        queryBuilder.setWhere(queryBuilder.where()
                .eq("player", player.getUuid())
                .and()
                .eq("type", type)
        );
        queryBuilder.orderBy("id", false);
        if (amount > 0) {
            queryBuilder.limit((long) amount);
        }

        return super.queryForList(queryBuilder, new ArrayList<>());
    }
}

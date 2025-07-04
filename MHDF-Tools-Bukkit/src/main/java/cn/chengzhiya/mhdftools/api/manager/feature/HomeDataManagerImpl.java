package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhiya.mhdftools.api.manager.database.AbstractDaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import lombok.SneakyThrows;

import java.util.List;

public final class HomeDataManagerImpl extends AbstractDaoManager<HomeData, Integer> implements HomeDataManager {
    @Override
    @SneakyThrows
    public List<HomeData> getList(MHDFToolsPlayer player) {
        QueryBuilder<HomeData, Integer> queryBuilder = getQueryBuilder();

        Where<HomeData, Integer> where = queryBuilder.where();
        where.eq("player", player.getUuid());
        queryBuilder.setWhere(where);


        return queryForList(queryBuilder);
    }

    @SneakyThrows
    private QueryBuilder<HomeData, Integer> getQueryBuilder(MHDFToolsPlayer player, String name) {
        QueryBuilder<HomeData, Integer> queryBuilder = getQueryBuilder();

        Where<HomeData, Integer> where = queryBuilder.where();
        where
                .eq("player", player.getUuid())
                .and()
                .eq("home", name);
        queryBuilder.setWhere(where);

        return queryBuilder;
    }

    @Override
    public boolean hasData(MHDFToolsPlayer player, String name) {
        return queryFirst(getQueryBuilder(player, name)) != null;
    }

    @Override
    public HomeData get(MHDFToolsPlayer player, String name) {
        return queryFirstOrDefault(getQueryBuilder(player, name), new HomeData(player, name));
    }
}

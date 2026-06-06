package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhimeow.mhdftools.api.manager.feature.HomeDataManager;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import lombok.SneakyThrows;

import java.util.List;

public final class HomeDataManagerImpl extends AbstractDaoManager<HomeData, Integer> implements HomeDataManager {
    public HomeDataManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    @SneakyThrows
    public List<HomeData> getList(MHDFToolsPlayer player) {
        QueryBuilder<HomeData, Integer> queryBuilder = super.getQueryBuilder();

        Where<HomeData, Integer> where = queryBuilder.where();
        where.eq("player", player.getUuid());
        queryBuilder.setWhere(where);


        return super.queryForList(queryBuilder);
    }

    @SneakyThrows
    private QueryBuilder<HomeData, Integer> getQueryBuilder(MHDFToolsPlayer player, String name) {
        QueryBuilder<HomeData, Integer> queryBuilder = super.getQueryBuilder();

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
        return super.queryFirst(this.getQueryBuilder(player, name)) != null;
    }

    @Override
    public HomeData get(MHDFToolsPlayer player, String name) {
        return super.queryFirstOrDefault(this.getQueryBuilder(player, name), new HomeData(player, name));
    }
}

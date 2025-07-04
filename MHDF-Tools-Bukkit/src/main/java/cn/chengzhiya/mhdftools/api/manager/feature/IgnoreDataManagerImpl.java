package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.IgnoreData;
import cn.chengzhiya.mhdftools.api.manager.database.AbstractDaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import lombok.SneakyThrows;

import java.util.List;

public final class IgnoreDataManagerImpl extends AbstractDaoManager<IgnoreData, Integer> implements IgnoreDataManager {
    @Override
    @SneakyThrows
    public List<IgnoreData> getList(MHDFToolsPlayer player) {
        QueryBuilder<IgnoreData, Integer> queryBuilder = getQueryBuilder();

        Where<IgnoreData, Integer> where = queryBuilder.where();
        where.eq("player", player.getUuid());
        queryBuilder.setWhere(where);


        return queryForList(queryBuilder);
    }

    @SneakyThrows
    private QueryBuilder<IgnoreData, Integer> getQueryBuilder(MHDFToolsPlayer player, MHDFToolsPlayer ignore) {
        QueryBuilder<IgnoreData, Integer> queryBuilder = getQueryBuilder();

        Where<IgnoreData, Integer> where = queryBuilder.where();
        where
                .eq("player", player.getUuid())
                .and()
                .eq("ignore", ignore.getUuid());
        queryBuilder.setWhere(where);

        return queryBuilder;
    }

    @Override
    public boolean hasData(MHDFToolsPlayer player, MHDFToolsPlayer ignore) {
        return queryFirst(getQueryBuilder(player, ignore)) != null;
    }

    @Override
    public IgnoreData get(MHDFToolsPlayer player, MHDFToolsPlayer ignore) {
        return queryFirstOrDefault(getQueryBuilder(player, ignore), new IgnoreData(player, ignore));
    }
}

package cn.chengzhimeow.mhdftools.bukkit.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.IgnoreData;
import cn.chengzhimeow.mhdftools.api.manager.feature.IgnoreDataManager;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import lombok.SneakyThrows;

import java.util.List;

public final class IgnoreDataManagerImpl extends AbstractDaoManager<IgnoreData, Integer> implements IgnoreDataManager {
    public IgnoreDataManagerImpl() {
        super(Main.instance.getDatabaseManager().getDatabase());
    }

    @Override
    @SneakyThrows
    public List<IgnoreData> getList(MHDFToolsPlayer player) {
        QueryBuilder<IgnoreData, Integer> queryBuilder = super.getQueryBuilder();

        Where<IgnoreData, Integer> where = queryBuilder.where();
        where.eq("player", player.getUuid());
        queryBuilder.setWhere(where);


        return super.queryForList(queryBuilder);
    }

    @SneakyThrows
    private QueryBuilder<IgnoreData, Integer> getQueryBuilder(MHDFToolsPlayer player, MHDFToolsPlayer ignore) {
        QueryBuilder<IgnoreData, Integer> queryBuilder = super.getQueryBuilder();

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
        return super.queryFirst(this.getQueryBuilder(player, ignore)) != null;
    }

    @Override
    public IgnoreData get(MHDFToolsPlayer player, MHDFToolsPlayer ignore) {
        return super.queryFirstOrDefault(this.getQueryBuilder(player, ignore), new IgnoreData(player, ignore));
    }
}

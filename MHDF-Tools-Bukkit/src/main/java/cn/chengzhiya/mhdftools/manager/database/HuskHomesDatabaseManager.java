package cn.chengzhiya.mhdftools.manager.database;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.DatabaseConfig;
import cn.chengzhiya.mhdftools.entity.database.huskhomes.HuskHomesHomeData;
import cn.chengzhiya.mhdftools.entity.database.huskhomes.HuskHomesPositionData;
import cn.chengzhiya.mhdftools.entity.database.huskhomes.HuskHomesPositionInfoData;
import cn.chengzhiya.mhdftools.entity.database.huskhomes.HuskHomesWarpData;
import cn.chengzhiya.mhdftools.util.config.plugin.HuskHomesConfigUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.DatabaseTableConfig;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.List;
import java.util.UUID;

public final class HuskHomesDatabaseManager extends AbstractDatabaseManager {
    private Dao<HuskHomesHomeData, UUID> huskHomesHomeDataDao;
    private Dao<HuskHomesWarpData, UUID> huskHomesWarpDataDao;
    private Dao<HuskHomesPositionInfoData, Integer> huskHomesSavePositionDataDao;
    private Dao<HuskHomesPositionData, Integer> huskHomesPositionDataDao;

    public HuskHomesDatabaseManager() {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setType(getDatabaseConfig().getString("type"));

        databaseConfig.setHost(
                getDatabaseConfig().getString("credentials.host") + ":" + getDatabaseConfig().getInt("mysql.port")
        );
        databaseConfig.setDatabase(getDatabaseConfig().getString("credentials.database"));
        databaseConfig.setUser(getDatabaseConfig().getString("credentials.username"));
        databaseConfig.setPassword(getDatabaseConfig().getString("credentials.password"));

        databaseConfig.setFile(
                new File(HuskHomesConfigUtil.getDataFolder(), "HuskHomesData.db")
        );

        setConfig(databaseConfig);
    }

    /**
     * 获取数据库配置项实例
     *
     * @return 数据库配置项实例
     */
    private ConfigurationSection getDatabaseConfig() {
        return HuskHomesConfigUtil.getConfig().getConfigurationSection("databaseSettings");
    }

    /**
     * 获取指定key的表名称
     *
     * @param key key
     * @return 表名称
     */
    private String getTableName(String key) {
        return getDatabaseConfig().getString("table_names." + key);
    }

    /**
     * 初始化数据库数据实例
     */
    @SneakyThrows
    public void initDao() {
        {
            DatabaseTableConfig<HuskHomesHomeData> tableConfig = DatabaseTableConfig.fromClass(
                    Main.instance.getDatabaseManager().getConnectionSource().getDatabaseType(),
                    HuskHomesHomeData.class
            );
            tableConfig.setTableName(getTableName("HOME_DATA"));

            this.huskHomesHomeDataDao = DaoManager.createDao(getConnectionSource(), tableConfig);
        }
        {
            DatabaseTableConfig<HuskHomesWarpData> tableConfig = DatabaseTableConfig.fromClass(
                    Main.instance.getDatabaseManager().getConnectionSource().getDatabaseType(),
                    HuskHomesWarpData.class
            );
            tableConfig.setTableName(getTableName("WARP_DATA"));

            this.huskHomesWarpDataDao = DaoManager.createDao(getConnectionSource(), tableConfig);
        }
        {
            DatabaseTableConfig<HuskHomesPositionInfoData> tableConfig = DatabaseTableConfig.fromClass(
                    Main.instance.getDatabaseManager().getConnectionSource().getDatabaseType(),
                    HuskHomesPositionInfoData.class
            );
            tableConfig.setTableName(getTableName("SAVED_POSITION_DATA"));

            this.huskHomesSavePositionDataDao = DaoManager.createDao(getConnectionSource(), tableConfig);
        }
        {
            DatabaseTableConfig<HuskHomesPositionData> tableConfig = DatabaseTableConfig.fromClass(
                    Main.instance.getDatabaseManager().getConnectionSource().getDatabaseType(),
                    HuskHomesPositionData.class
            );
            tableConfig.setTableName(getTableName("HOME_DATA"));

            this.huskHomesPositionDataDao = DaoManager.createDao(getConnectionSource(), tableConfig);
        }
    }

    /**
     * 获取HuskHomes家数据实例列表
     *
     * @return HuskHomes家数据实例列表
     */
    @SneakyThrows
    public List<HuskHomesHomeData> getHuskHomesHomeDataList() {
        return this.huskHomesHomeDataDao.queryForAll();
    }

    /**
     * 获取HuskHomes传送点数据实例列表
     *
     * @return HuskHomes传送点数据实例列表
     */
    @SneakyThrows
    public List<HuskHomesWarpData> getHuskHomesWarpDataList() {
        return this.huskHomesWarpDataDao.queryForAll();
    }

    /**
     * 获取指定位置信息数据ID的位置信息数据实例
     *
     * @param id 位置信息数据ID
     * @return 位置信息数据实例
     */
    @SneakyThrows
    public HuskHomesPositionInfoData getHuskHomesPositionInfoData(int id) {
        return this.huskHomesSavePositionDataDao.queryForId(id);
    }

    /**
     * 获取指定位置数据ID的位置数据实例
     *
     * @param id 位置数据ID
     * @return 位置数据实例
     */
    @SneakyThrows
    public HuskHomesPositionData getHuskHomesPositionData(int id) {
        return this.huskHomesPositionDataDao.queryForId(id);
    }
}

package cn.chengzhiya.mhdftools.manager.database;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.DatabaseConfig;
import cn.chengzhiya.mhdftools.entity.database.cmi.CmiUserData;
import cn.chengzhiya.mhdftools.util.config.plugin.CmiConfigUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.DatabaseTableConfig;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class CmiDatabaseManager extends AbstractDatabaseManager {
    private final String prefix;
    private Dao<CmiUserData, UUID> cmiUserDataDao;

    public CmiDatabaseManager() {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setType(getDatabaseConfig().getString("storage.method"));

        databaseConfig.setHost(
                getDatabaseConfig().getString("mysql.hostname")
        );
        databaseConfig.setDatabase(getDatabaseConfig().getString("mysql.database"));
        databaseConfig.setUser(getDatabaseConfig().getString("mysql.username"));
        databaseConfig.setPassword(getDatabaseConfig().getString("mysql.password"));

        databaseConfig.setFile(
                new File(CmiConfigUtil.getDataFolder(), "cmi.sqlite.db")
        );

        this.prefix = !getConfig().getType().toLowerCase(Locale.ROOT).equals("sqlite") ?
                getDatabaseConfig().getString("mysql.tablePrefix") : "";

        setConfig(databaseConfig);
    }

    /**
     * 获取数据库配置项实例
     *
     * @return 数据库配置项实例
     */
    private ConfigurationSection getDatabaseConfig() {
        return CmiConfigUtil.getDatabaseInfoConfig();
    }

    /**
     * 初始化数据库数据实例
     */
    @SneakyThrows
    public void initDao() {
        {
            DatabaseTableConfig<CmiUserData> tableConfig = DatabaseTableConfig.fromClass(
                    Main.instance.getDatabaseManager().getConnectionSource().getDatabaseType(),
                    CmiUserData.class
            );
            tableConfig.setTableName(this.prefix + "users");

            cmiUserDataDao = DaoManager.createDao(getConnectionSource(), tableConfig);
        }
    }

    /**
     * 获取Cmi用户数据实例列表
     *
     * @return Cmi用户数据实例列表
     */
    @SneakyThrows
    public List<CmiUserData> getCmiUserDataList() {
        return cmiUserDataDao.queryForAll();
    }
}

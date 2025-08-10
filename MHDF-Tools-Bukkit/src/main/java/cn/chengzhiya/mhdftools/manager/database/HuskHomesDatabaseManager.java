package cn.chengzhiya.mhdftools.manager.database;

import cn.chengzhiya.mhdfdatabase.MHDFDatabase;
import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import cn.chengzhiya.mhdfdatabase.entity.DatabaseConfig;
import cn.chengzhiya.mhdfdatabase.entity.DatabaseConnectConfig;
import cn.chengzhiya.mhdfdatabase.impl.H2DatabaseServiceImpl;
import cn.chengzhiya.mhdfdatabase.impl.MySQLDatabaseServiceImpl;
import cn.chengzhiya.mhdftools.entity.database.data.huskhomes.HuskHomesHomeData;
import cn.chengzhiya.mhdftools.entity.database.data.huskhomes.HuskHomesPositionData;
import cn.chengzhiya.mhdftools.entity.database.data.huskhomes.HuskHomesPositionInfoData;
import cn.chengzhiya.mhdftools.entity.database.data.huskhomes.HuskHomesWarpData;
import cn.chengzhiya.mhdftools.util.config.plugin.HuskHomesConfigUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@Getter
public final class HuskHomesDatabaseManager {
    private final DatabaseConfig config = new DatabaseConfig();
    private HuskHomesHomeDataManager homeDataManager;
    private HuskHomesWarpDataManager warpDataManager;
    private HuskHomesPositionInfoDataManager positionInfoDataManager;
    private HuskHomesPositionDataManager positionDataManager;
    private MHDFDatabase database;

    @SneakyThrows
    public HuskHomesDatabaseManager() {
        this.initConfig();
        this.database = new MHDFDatabase(this.config, MySQLDatabaseServiceImpl.class, H2DatabaseServiceImpl.class);
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
        return this.getDatabaseConfig().getString("table_names." + key);
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        DatabaseConnectConfig connectConfig = new DatabaseConnectConfig();
        connectConfig.setHost(this.getDatabaseConfig().getString("credentials.host") + this.getDatabaseConfig().getString("credentials.port"));
        connectConfig.setDatabase(this.getDatabaseConfig().getString("credentials.database"));
        connectConfig.setUser(this.getDatabaseConfig().getString("credentials.username"));
        connectConfig.setPassword(this.getDatabaseConfig().getString("credentials.password"));
        connectConfig.setFile(new File(HuskHomesConfigUtil.getDataFolder(), "HuskHomesData.db"));

        String type = Objects.requireNonNull(this.getDatabaseConfig().getString("type")).toLowerCase();
        this.config.setType(switch (type) {
            case "sqllite", "h2" -> "h2";
            case "mariadb", "mysql" -> "mysql";
            default -> throw new IllegalStateException("不兼容的数据库类型: " + type);
        });

        this.config.setConnectConfig(connectConfig);
    }

    /**
     * 连接数据库
     */
    public void connect() {
        this.getDatabase().getDatabaseService().connect();
    }

    /**
     * 关闭数据库连接
     */
    public void close() {
        this.getDatabase().getDatabaseService().close();
        this.database = null;
    }

    /**
     * 创建所有表
     */
    public void initTable() {
        this.getDatabase().addTable(HuskHomesHomeData.class, this.getTableName("HOME_DATA"));
        this.getDatabase().addTable(HuskHomesWarpData.class, this.getTableName("WARP_DATA"));
        this.getDatabase().addTable(HuskHomesPositionInfoData.class, this.getTableName("SAVED_POSITION_DATA"));
        this.getDatabase().addTable(HuskHomesPositionData.class, this.getTableName("POSITION_DATA"));
        this.getDatabase().createAllTable();

        this.homeDataManager = new HuskHomesHomeDataManager(this.getDatabase());
        this.warpDataManager = new HuskHomesWarpDataManager(this.getDatabase());
        this.positionInfoDataManager = new HuskHomesPositionInfoDataManager(this.getDatabase());
        this.positionDataManager = new HuskHomesPositionDataManager(this.getDatabase());
    }

    public static class HuskHomesHomeDataManager extends AbstractDaoManager<HuskHomesHomeData, UUID> {
        public HuskHomesHomeDataManager(MHDFDatabase instance) {
            super(instance);
        }
    }

    public static class HuskHomesWarpDataManager extends AbstractDaoManager<HuskHomesWarpData, UUID> {
        public HuskHomesWarpDataManager(MHDFDatabase instance) {
            super(instance);
        }
    }

    public static class HuskHomesPositionInfoDataManager extends AbstractDaoManager<HuskHomesPositionInfoData, Integer> {
        public HuskHomesPositionInfoDataManager(MHDFDatabase instance) {
            super(instance);
        }
    }

    public static class HuskHomesPositionDataManager extends AbstractDaoManager<HuskHomesPositionData, Integer> {
        public HuskHomesPositionDataManager(MHDFDatabase instance) {
            super(instance);
        }
    }
}

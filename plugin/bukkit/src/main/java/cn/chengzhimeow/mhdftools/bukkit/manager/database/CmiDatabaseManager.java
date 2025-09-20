package cn.chengzhimeow.mhdftools.bukkit.manager.database;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.entity.database.data.cmi.CmiUserData;
import cn.chengzhimeow.mhdftools.bukkit.util.config.plugin.CmiConfigUtil;
import cn.chengzhiya.mhdfdatabase.MHDFDatabase;
import cn.chengzhiya.mhdfdatabase.dao.AbstractDaoManager;
import cn.chengzhiya.mhdfdatabase.entity.DatabaseConfig;
import cn.chengzhiya.mhdfdatabase.entity.DatabaseConnectConfig;
import cn.chengzhiya.mhdfdatabase.impl.H2DatabaseServiceImpl;
import cn.chengzhiya.mhdfdatabase.impl.MySQLDatabaseServiceImpl;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@Getter
public final class CmiDatabaseManager {
    private final DatabaseConfig config = new DatabaseConfig();
    private CmiUserDataManager userDataManager;
    private MHDFDatabase database;
    private String prefix;

    @SneakyThrows
    public CmiDatabaseManager() {
        this.initConfig();
        this.database = new MHDFDatabase(this.config, MySQLDatabaseServiceImpl.class, H2DatabaseServiceImpl.class);
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
     * 初始化配置
     */
    private void initConfig() {
        DatabaseConnectConfig connectConfig = new DatabaseConnectConfig();
        connectConfig.setHost(this.getDatabaseConfig().getString("mysql.hostname"));
        connectConfig.setDatabase(this.getDatabaseConfig().getString("mysql.database"));
        connectConfig.setUser(this.getDatabaseConfig().getString("mysql.username"));
        connectConfig.setPassword(this.getDatabaseConfig().getString("mysql.password"));
        connectConfig.setFile(new File(CmiConfigUtil.getDataFolder(), "cmi.sqlite.db"));

        String type = Objects.requireNonNull(this.getDatabaseConfig().getString("storage.method")).toLowerCase();
        this.config.setType(switch (type) {
            case "sqllite", "h2" -> "h2";
            case "mariadb", "mysql" -> "mysql";
            default -> throw new IllegalStateException("不兼容的数据库类型: " + type);
        });

        this.prefix = !this.getConfig().getType().equals("h2") ?
                this.getDatabaseConfig().getString("mysql.tablePrefix") : "";

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
        this.getDatabase().addTable(CmiUserData.class, this.getPrefix() + "users");
        this.getDatabase().createAllTable();

        this.userDataManager = new CmiUserDataManager(this.getDatabase());
    }

    public static class CmiUserDataManager extends AbstractDaoManager<CmiUserData, UUID> {
        public CmiUserDataManager(MHDFDatabase instance) {
            super(instance);
        }
    }
}

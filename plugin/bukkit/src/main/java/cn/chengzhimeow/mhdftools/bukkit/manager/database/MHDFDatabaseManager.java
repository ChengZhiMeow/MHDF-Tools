package cn.chengzhimeow.mhdftools.bukkit.manager.database;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.api.entity.database.Dao;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhiya.mhdfdatabase.MHDFDatabase;
import cn.chengzhiya.mhdfdatabase.entity.DatabaseConfig;
import cn.chengzhiya.mhdfdatabase.entity.DatabaseConnectConfig;
import cn.chengzhiya.mhdfdatabase.impl.H2DatabaseServiceImpl;
import cn.chengzhiya.mhdfdatabase.impl.MySQLDatabaseServiceImpl;
import lombok.Getter;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Objects;

@Getter
public final class MHDFDatabaseManager {
    private final DatabaseConfig config = new DatabaseConfig();
    private MHDFDatabase database;

    @SneakyThrows
    public MHDFDatabaseManager() {
        this.initConfig();
        this.database = new MHDFDatabase(this.config, MySQLDatabaseServiceImpl.class, H2DatabaseServiceImpl.class);
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        ConfigurationSection database = ConfigSetting.getInstance().getData().getConfigurationSection("databaseSettings");
        if (database == null) {
            return;
        }

        DatabaseConnectConfig connectConfig = new DatabaseConnectConfig();
        connectConfig.setHost(database.getString("mysql.host"));
        connectConfig.setDatabase(database.getString("mysql.database"));
        connectConfig.setUser(database.getString("mysql.user"));
        connectConfig.setPassword(database.getString("mysql.password"));
        connectConfig.setFile(
                new File(Main.instance.getDataFolder(), Objects.requireNonNull(database.getString("h2.file")))
        );

        ConfigurationSection prams = database.getConfigurationSection("prams");
        if (prams != null) {
            for (String key : prams.getKeys(false)) {
                String value = prams.getString(key);
                if (value == null) {
                    continue;
                }

                connectConfig.getPramHashMap().put(key, value);
            }
        }

        this.config.setType(Objects.requireNonNull(database.getString("type")));
        this.config.setConnectConfig(connectConfig);
    }

    /**
     * 连接数据库
     */
    public void connect() {
        this.database.getDatabaseService().connect();
    }

    /**
     * 关闭数据库连接
     */
    public void close() {
        this.database.getDatabaseService().close();
        this.database = null;
    }

    /**
     * 创建所有表
     */
    public void initTable() {
        Reflections reflections = new Reflections(Dao.class.getPackageName());
        for (Class<? extends Dao> clazz : reflections.getSubTypesOf(Dao.class)) {
            if (Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }

            this.database.addTable(clazz);
        }

        this.database.createAllTable();
    }
}

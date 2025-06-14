package cn.chengzhiya.mhdftools.manager.database;

import cn.chengzhiya.mhdftools.entity.AbstractDao;
import cn.chengzhiya.mhdftools.entity.DatabaseConfig;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import com.j256.ormlite.table.TableUtils;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;
import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Objects;

public final class MHDFDatabaseManager extends AbstractDatabaseManager {
    public MHDFDatabaseManager() {
        ConfigurationSection database = ConfigUtil.getConfig().getConfigurationSection("databaseSettings");
        if (database == null) {
            return;
        }

        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setType(database.getString("type"));

        databaseConfig.setHost(database.getString("mysql.host"));
        databaseConfig.setDatabase(database.getString("mysql.database"));
        databaseConfig.setUser(database.getString("mysql.user"));
        databaseConfig.setPassword(database.getString("mysql.password"));

        databaseConfig.setFile(
                new File(ConfigUtil.getDataFolder(), Objects.requireNonNull(database.getString("h2.file")))
        );

        setConfig(databaseConfig);
    }

    /**
     * 初始化表
     */
    @SneakyThrows
    public void initTable() {
        Reflections reflections = new Reflections(AbstractDao.class.getPackageName());

        for (Class<? extends AbstractDao> clazz : reflections.getSubTypesOf(AbstractDao.class)) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                AbstractDao abstractDao = clazz.getDeclaredConstructor().newInstance();
                TableUtils.createTableIfNotExists(getConnectionSource(), abstractDao.getClass());
            }
        }
    }
}

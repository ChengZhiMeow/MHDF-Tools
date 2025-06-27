package cn.chengzhiya.mhdftools.manager.database.impl;

import cn.chengzhiya.mhdftools.entity.config.DatabaseConfig;
import cn.chengzhiya.mhdftools.entity.database.Dao;
import cn.chengzhiya.mhdftools.manager.database.AbstractDatabaseManager;
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

    @SneakyThrows
    public void initTable() {
        Reflections reflections = new Reflections(Dao.class.getPackageName());

        for (Class<? extends Dao> clazz : reflections.getSubTypesOf(Dao.class)) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                try {
                    Dao dao = clazz.getDeclaredConstructor().newInstance();
                    TableUtils.createTableIfNotExists(getConnectionSource(), dao.getClass());
                } catch (Exception ignored) {
                }
            }
        }
    }
}

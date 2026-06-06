package cn.chengzhimeow.mhdftools.bukkit.common.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public final class DatabaseSetting extends AbstractYamlSetting<DatabaseSetting.Config> {
    @Getter(lazy = true)
    private static final DatabaseSetting instance = new DatabaseSetting();

    private DatabaseSetting() {
    }

    @Override
    public String originFilePath() {
        return "database.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();

        ConfigurationSection mysql = super.getData().getConfigurationSection("mysql");
        ConfigurationSection h2 = super.getData().getConfigurationSection("h2");
        ConfigurationSection pramsSection = super.getData().getConfigurationSection("prams");
        Map<String, String> prams = new HashMap<>();
        if (pramsSection != null) {
            for (String key : pramsSection.getKeys(false)) {
                String value = pramsSection.getString(key);
                if (value != null) prams.put(key, value);
            }
        }

        this.config = new Config(
                super.getData().getString("type", "h2"),
                new Config.MySQL(
                        mysql == null ? "127.0.0.1:3306" : mysql.getString("host", "127.0.0.1:3306"),
                        mysql == null ? "mhdf_tools" : mysql.getString("database", "mhdf_tools"),
                        mysql == null ? "root" : mysql.getString("user", "root"),
                        mysql == null ? "root" : mysql.getString("password", "root")
                ),
                new Config.H2(h2 == null ? "database.db" : h2.getString("file", "database.db")),
                prams
        );
    }

    public record Config(
            String type,
            MySQL mysql,
            H2 h2,
            Map<String, String> prams
    ) {
        public record MySQL(
                String host,
                String database,
                String user,
                String password
        ) {
        }

        public record H2(
                String file
        ) {
        }
    }
}

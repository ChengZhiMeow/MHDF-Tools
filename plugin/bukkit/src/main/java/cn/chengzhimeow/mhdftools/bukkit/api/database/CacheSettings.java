package cn.chengzhimeow.mhdftools.bukkit.api.database;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import lombok.Getter;

@Getter
public final class CacheSettings {
    private final String type;
    private final String server;
    private final RedisSettings redis;

    public CacheSettings(ConfigurationSection section) {
        this.type = section == null ? "map" : section.getString("type", "map");
        this.server = section == null ? "default" : section.getString("server", "default");
        this.redis = new RedisSettings(section == null ? null : section.getConfigurationSection("redis"));
    }

    public boolean isRedis() {
        return this.type.equalsIgnoreCase("redis");
    }

    @Getter
    public static final class RedisSettings {
        private final String host;
        private final String user;
        private final String password;

        private RedisSettings(ConfigurationSection section) {
            this.host = section == null ? "localhost:6379" : section.getString("host", "localhost:6379");
            this.user = this.blankToNull(section == null ? null : section.getString("user"));
            this.password = this.blankToNull(section == null ? null : section.getString("password"));
        }

        private String blankToNull(String value) {
            if (value == null || value.isBlank()) {
                return null;
            }
            return value;
        }
    }
}

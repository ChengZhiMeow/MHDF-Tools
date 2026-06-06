package cn.chengzhimeow.mhdftools.bukkit.api.cache;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import lombok.Getter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Getter
public final class CacheSettings {
    private final String type;
    private final String serverId;
    private final RedisSettings redis;

    public CacheSettings(ConfigurationSection section) {
        this.type = section == null ? "map" : section.getString("type", "map");
        this.serverId = section == null ? "default" : section.getString("server", "default");
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
        private final String uri;

        private RedisSettings(ConfigurationSection section) {
            this.host = section == null ? "localhost:6379" : section.getString("host", "localhost:6379");
            this.user = this.blankToNull(section == null ? null : section.getString("user"));
            this.password = this.blankToNull(section == null ? null : section.getString("password"));
            this.uri = this.createUri();
        }

        private String blankToNull(String value) {
            if (value == null || value.isBlank()) {
                return null;
            }
            return value;
        }

        private String createUri() {
            if (this.host.startsWith("redis://") || this.host.startsWith("rediss://")) {
                return this.host;
            }

            StringBuilder builder = new StringBuilder("redis://");
            if (this.user != null || this.password != null) {
                if (this.user != null) {
                    builder.append(URLEncoder.encode(this.user, StandardCharsets.UTF_8));
                }
                if (this.password != null) {
                    builder.append(':').append(URLEncoder.encode(this.password, StandardCharsets.UTF_8));
                }
                builder.append('@');
            }
            return builder.append(this.host).append("/0").toString();
        }
    }
}

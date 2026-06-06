package cn.chengzhimeow.mhdftools.bukkit.common.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class CacheSetting extends AbstractYamlSetting<CacheSetting.Config> {
    @Getter(lazy = true)
    private static final CacheSetting instance = new CacheSetting();

    private CacheSetting() {
    }

    @Override
    public String originFilePath() {
        return "cache.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();

        ConfigurationSection redis = super.getData().getConfigurationSection("redis");
        String host = redis == null ? "localhost:6379" : redis.getString("host", "localhost:6379");
        String user = this.blankToNull(redis == null ? null : redis.getString("user"));
        String password = this.blankToNull(redis == null ? null : redis.getString("password"));
        this.config = new Config(
                super.getData().getString("type", "map"),
                super.getData().getString("server", "default"),
                new Config.Redis(host, user, password, this.createUri(host, user, password))
        );
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) return null;
        return value;
    }

    private String createUri(String host, String user, String password) {
        if (host.startsWith("redis://") || host.startsWith("rediss://")) return host;

        StringBuilder builder = new StringBuilder("redis://");
        if (user != null || password != null) {
            if (user != null) {
                builder.append(URLEncoder.encode(user, StandardCharsets.UTF_8));
            }
            if (password != null) {
                builder.append(':').append(URLEncoder.encode(password, StandardCharsets.UTF_8));
            }
            builder.append('@');
        }
        return builder.append(host).append("/0").toString();
    }

    public record Config(
            String type,
            String server,
            Redis redis
    ) {
        public boolean isRedis() {
            return this.type.equalsIgnoreCase("redis");
        }

        public record Redis(
                String host,
                String user,
                String password,
                String uri
        ) {
        }
    }
}

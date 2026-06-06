package cn.chengzhimeow.mhdftools.config.impl;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Locale;
import java.util.Objects;

public final class ProxySetting extends AbstractYamlSetting<ProxySetting.Config> {
    @Getter(lazy = true)
    private static final ProxySetting instance = new ProxySetting();
    @Getter private Config config;

    private ProxySetting() {
    }

    @Override
    public String originFilePath() {
        return "proxy.yml";
    }

    @Override
    public String filePath() {
        return "proxy.yml";
    }

    @Override
    public void reload() {
        super.reload();

        ConfigurationSection proxy = super.getData().getConfigurationSection("proxy");
        if (proxy == null || !proxy.getBoolean("enable")) {
            this.config = new Config(Proxy.NO_PROXY);
            return;
        }

        String type = proxy.getString("type");
        String host = proxy.getString("host");
        int port = proxy.getInt("port");
        if (type == null || host == null || port == 0) {
            this.config = new Config(Proxy.NO_PROXY);
            return;
        }

        this.config = new Config(new Proxy(Proxy.Type.valueOf(type.toUpperCase(Locale.ROOT)), new InetSocketAddress(Objects.requireNonNull(host), port)));
    }

    /**
     * 閼惧嘲褰囨禒锝囨倞鐎圭偘绶?     *
     *
     * @return 娴狅絿鎮婄€圭偘绶?
     */
    public Proxy getProxy() {
        return this.config.proxy();
    }

    public record Config(
            Proxy proxy
    ) {
    }
}

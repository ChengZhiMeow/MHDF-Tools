package cn.chengzhimeow.mhdftools.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.ccyaml.manager.AbstractYamlManager;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Locale;
import java.util.Objects;

public final class ProxySetting extends AbstractYamlManager {
    private static ProxySetting instance;

    private ProxySetting() {
        super(ConfigManager.getInstance().getYamlManager());
    }

    public static ProxySetting getSettingInstance() {
        if (ProxySetting.instance == null) ProxySetting.instance = new ProxySetting();
        return ProxySetting.instance;
    }

    @Override
    public String originFilePath() {
        return "proxy_zh.yml";
    }

    @Override
    public String filePath() {
        return "proxy.yml";
    }

    /**
     * 获取代理实例
     *
     * @return 代理实例
     */
    public Proxy getProxy() {
        ConfigurationSection config = super.getData().getConfigurationSection("proxy");

        if (config == null) return Proxy.NO_PROXY;
        if (!config.getBoolean("enable")) return Proxy.NO_PROXY;

        String type = config.getString("type");
        String host = config.getString("host");
        int port = config.getInt("port");
        if (type == null || host == null || port == 0) return Proxy.NO_PROXY;

        return new Proxy(Proxy.Type.valueOf(type.toUpperCase(Locale.ROOT)), new InetSocketAddress(Objects.requireNonNull(host), port));
    }
}

package cn.chengzhiya.mhdftools.manager.config;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdfyaml.manager.YamlManager;
import org.bukkit.configuration.ConfigurationSection;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Locale;
import java.util.Objects;

public final class ProxyManager extends YamlManager {
    public ProxyManager() {
        super(Main.instance.getYamlManager());
    }

    @Override
    public String getOriginFilePath() {
        return "proxy_zh.yml";
    }

    @Override
    public String getFilePath() {
        return "proxy.yml";
    }

    /**
     * 获取代理实例
     *
     * @return 代理实例
     */
    public Proxy getProxy() {
        ConfigurationSection config = super.getData().getConfigurationSection("proxySettings");
        if (config == null) {
            return Proxy.NO_PROXY;
        }

        if (!config.getBoolean("enable")) {
            return Proxy.NO_PROXY;
        }

        String type = config.getString("type");
        String host = config.getString("host");
        int port = config.getInt("port");
        if (type == null || host == null || port == 0) {
            return Proxy.NO_PROXY;
        }

        return new Proxy(Proxy.Type.valueOf(type.toUpperCase(Locale.ROOT)), new InetSocketAddress(Objects.requireNonNull(host), port));
    }
}

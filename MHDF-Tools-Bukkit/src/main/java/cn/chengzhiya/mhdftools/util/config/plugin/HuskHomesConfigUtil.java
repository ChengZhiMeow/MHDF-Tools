package cn.chengzhiya.mhdftools.util.config.plugin;

import cn.chengzhiya.mhdftools.Main;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class HuskHomesConfigUtil {
    @Getter
    private static final File dataFolder = new File(Main.instance.getDataFolder().getParent(), "HuskHomes");
    @Getter
    private static final File configFile = new File(dataFolder, "config.yml");
    private static YamlConfiguration config;

    /**
     * 重新加载配置文件
     */
    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * 获取配置文件实例
     *
     * @return 配置文件实例
     */
    public static YamlConfiguration getConfig() {
        if (config == null) {
            HuskHomesConfigUtil.reloadConfig();
        }

        return config;
    }
}

package cn.chengzhiya.mhdftools.util.config.plugin;

import cn.chengzhiya.mhdftools.Main;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class CmiConfigUtil {
    @Getter
    private static final File dataFolder = new File(Main.instance.getDataFolder().getParent(), "CMI");
    @Getter
    private static final File settingsFolder = new File(dataFolder, "Settings");
    @Getter
    private static final File databaseInfoFile = new File(settingsFolder, "DataBaseInfo.yml");
    @Getter
    private static final File savesFolder = new File(dataFolder, "Saves");
    @Getter
    private static final File warpFile = new File(savesFolder, "Warps.yml");
    @Getter
    private static final File configFile = new File(dataFolder, "config.yml");
    private static YamlConfiguration config;
    private static YamlConfiguration databaseInfo;
    private static YamlConfiguration warp;

    /**
     * 重新加载配置文件
     */
    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        databaseInfo = YamlConfiguration.loadConfiguration(databaseInfoFile);
        warp = YamlConfiguration.loadConfiguration(warpFile);
    }

    /**
     * 获取配置文件实例
     *
     * @return 配置文件实例
     */
    public static YamlConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }

        return config;
    }

    /**
     * 获取数据库配置文件实例
     *
     * @return 数据库配置文件实例
     */
    public static YamlConfiguration getDatabaseInfoConfig() {
        if (config == null) {
            reloadConfig();
        }

        return databaseInfo;
    }

    /**
     * 获取传送点数据文件实例
     *
     * @return 传送点数据文件实例
     */
    public static YamlConfiguration getWarpConfig() {
        if (warp == null) {
            reloadConfig();
        }

        return warp;
    }
}

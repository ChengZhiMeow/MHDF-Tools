package cn.chengzhimeow.mhdftools.bukkit.util.config.plugin;

import cn.chengzhimeow.ccyaml.configuration.yaml.YamlConfiguration;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;

public final class CmiConfigUtil {
    @Getter
    private static final File dataFolder = new File(Main.instance.getDataFolder().getParent(), "CMI");
    @Getter
    private static final File settingsFolder = new File(CmiConfigUtil.dataFolder, "Settings");
    @Getter
    private static final File databaseInfoFile = new File(CmiConfigUtil.settingsFolder, "DataBaseInfo.yml");
    @Getter
    private static final File savesFolder = new File(CmiConfigUtil.dataFolder, "Saves");
    @Getter
    private static final File warpFile = new File(CmiConfigUtil.savesFolder, "Warps.yml");
    @Getter
    private static final File configFile = new File(CmiConfigUtil.dataFolder, "config.yml");
    private static YamlConfiguration config;
    private static YamlConfiguration databaseInfo;
    private static YamlConfiguration warp;

    /**
     * 重新加载配置文件
     */
    @SneakyThrows
    public static void reloadConfig() {
        CmiConfigUtil.config = YamlConfiguration.loadConfiguration(CmiConfigUtil.configFile);
        CmiConfigUtil.databaseInfo = YamlConfiguration.loadConfiguration(CmiConfigUtil.databaseInfoFile);
        CmiConfigUtil.warp = YamlConfiguration.loadConfiguration(CmiConfigUtil.warpFile);
    }

    /**
     * 获取配置文件实例
     *
     * @return 配置文件实例
     */
    public static YamlConfiguration getConfig() {
        if (CmiConfigUtil.config == null) CmiConfigUtil.reloadConfig();
        return CmiConfigUtil.config;
    }

    /**
     * 获取数据库配置文件实例
     *
     * @return 数据库配置文件实例
     */
    public static YamlConfiguration getDatabaseInfoConfig() {
        if (CmiConfigUtil.config == null) CmiConfigUtil.reloadConfig();
        return CmiConfigUtil.databaseInfo;
    }

    /**
     * 获取传送点数据文件实例
     *
     * @return 传送点数据文件实例
     */
    public static YamlConfiguration getWarpConfig() {
        if (CmiConfigUtil.warp == null) CmiConfigUtil.reloadConfig();
        return CmiConfigUtil.warp;
    }
}

package cn.chengzhiya.mhdftools.util.config;

import cn.chengzhiya.mhdftools.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public final class ConfigUtil {
    /**
     * 获取插件数据文件夹
     */
    public static File getDataFolder() {
        return Main.instance.getDataFolder();
    }

    /**
     * 获取配置文件文件名称
     *
     * @return 配置文件文件名称
     */
    public static String getConfigFileName() {
        return "config_zh.yml";
    }

    /**
     * 获取配置文件实例
     *
     * @return 配置文件实例
     */
    public static FileConfiguration getConfig() {
        return Main.instance.getConfig();
    }

    /**
     * 保存配置文件
     */
    public static void saveConfig() {
        Main.instance.saveConfig();
    }

    /**
     * 重新加载配置文件
     */
    public static void reloadConfig() {
        YamlUtil.updateConfig(new File(getDataFolder(), "config.yml"), getConfigFileName());
        Main.instance.reloadConfig();
    }

    /**
     * 保存初始配置文件
     */
    public static void saveDefaultConfig() {
        FileUtil.saveResource("config.yml", getConfigFileName(), false);
    }
}

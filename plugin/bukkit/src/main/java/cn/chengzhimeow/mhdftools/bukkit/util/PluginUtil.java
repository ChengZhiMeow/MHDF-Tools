package cn.chengzhimeow.mhdftools.bukkit.util;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class PluginUtil {
    private static Boolean nativeSupportAdventureApi;

    /**
     * 获取插件版本号
     *
     * @return 插件版本号
     */
    public static String getVersion() {
        return Main.instance.getDescription().getVersion();
    }

    /**
     * 获取插件名称
     *
     * @return 插件名称
     */
    public static String getName() {
        return Main.instance.getDescription().getName();
    }

    /**
     * 是否安装指定插件
     *
     * @return 结果
     */
    public static boolean hasPlugin(String name) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        return plugin != null && plugin.isEnabled();
    }

    /**
     * 更新检测
     */
    public static void checkUpdate() {
        if (!ConfigSetting.getSettingInstance().getData().getBoolean("updateCheck")) {
        }

        // 等待 MHDF-HttpClient 库
    }
}

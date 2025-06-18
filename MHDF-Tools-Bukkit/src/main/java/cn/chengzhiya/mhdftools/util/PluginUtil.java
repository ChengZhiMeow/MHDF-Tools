package cn.chengzhiya.mhdftools.util;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;

public final class PluginUtil {
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
     * 更新检测
     */
    public static void checkUpdate() {
        if (!ConfigUtil.getConfig().getBoolean("updateCheck")) {
            return;
        }

        // 等待 MHDF-HttpClient 库
    }
}

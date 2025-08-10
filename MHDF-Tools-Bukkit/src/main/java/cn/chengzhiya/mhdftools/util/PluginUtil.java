package cn.chengzhiya.mhdftools.util;

import cn.chengzhiya.mhdftools.Main;
import org.bukkit.entity.Player;

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
     * 是否本地支持AdventureApi
     *
     * @return 结果
     */
    public static boolean isNativeSupportAdventureApi() {
        if (PluginUtil.nativeSupportAdventureApi == null) {
            try {
                Class.forName("net.kyori.adventure.text.Component");
                Player.class.getDeclaredMethod("displayName");
                PluginUtil.nativeSupportAdventureApi = true;
            } catch (NoSuchMethodError | ClassNotFoundException | NoSuchMethodException e) {
                PluginUtil.nativeSupportAdventureApi = false;
            }
        }

        return PluginUtil.nativeSupportAdventureApi;
    }

    /**
     * 更新检测
     */
    public static void checkUpdate() {
        if (!Main.instance.getConfigManager().getConfigManager().getData().getBoolean("updateCheck")) {
        }

        // 等待 MHDF-HttpClient 库
    }
}

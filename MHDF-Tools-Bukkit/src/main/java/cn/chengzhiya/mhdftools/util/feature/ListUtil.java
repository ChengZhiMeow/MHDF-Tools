package cn.chengzhiya.mhdftools.util.feature;

import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;

public final class ListUtil {
    /**
     * 获取服务端运行时实例
     *
     * @return 运行时实例
     */
    private static Runtime getRuntime() {
        return Runtime.getRuntime();
    }

    /**
     * 获取服务器当前TPS
     *
     * @return TPS数值
     */
    public static double getTps() {
        double tps;
        if (FoliaScheduler.isFolia()) {
            tps = 0.0;
        } else {
            tps = SpigotReflectionUtil.getTPS();
        }
        return Double.parseDouble(String.format("%.2f", tps));
    }

    /**
     * 获取服务器当前总内存
     *
     * @return 总内存数(单位 MB)
     */
    public static long getTotalMemory() {
        return ListUtil.getRuntime().totalMemory() / 1048576L;
    }

    /**
     * 获取服务器当前总空闲内存
     *
     * @return 总空闲内存(单位 MB)
     */
    public static long getFreeMemory() {
        return ListUtil.getRuntime().freeMemory() / 1048576L;
    }

    /**
     * 获取服务器当前总占用内存
     *
     * @return 总占用内存(单位 MB)
     */
    public static long getUsedMemory() {
        return ListUtil.getTotalMemory() - ListUtil.getFreeMemory();
    }
}

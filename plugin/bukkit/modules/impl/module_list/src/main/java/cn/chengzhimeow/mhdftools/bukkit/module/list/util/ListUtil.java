package cn.chengzhimeow.mhdftools.bukkit.module.list.util;

import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import net.nyana.reflection.clazz.NyanaClass;
import net.nyana.reflection.method.matcher.MethodMatchers;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;

public final class ListUtil {
    private static final Location defaultLocation = new Location(Bukkit.getWorlds().getFirst(), 0, 0, 0);
    private static MethodHandle foliaTpsMethodHandle = null;

    /**
     * 获取服务器当前TPS
     *
     * @return TPS数值
     */
    public static double getTps(@Nullable Location location) {
        double[] tps;
        if (FoliaScheduler.isFolia()) {
            if (foliaTpsMethodHandle == null) {
                foliaTpsMethodHandle = NyanaClass.of(Bukkit.getServer().getClass())
                        .getDeclaredNyanaMethod(MethodMatchers.mNamed("getRegionTPS"))
                        .unreflect();
            }

            try {
                Location foliaTpsLocation = location == null ? defaultLocation : location;
                tps = (double[]) foliaTpsMethodHandle.invoke(Bukkit.getServer(), foliaTpsLocation);
            } catch (Throwable throwable) {
                return 0d;
            }
        } else {
            tps = Bukkit.getTPS();
        }
        return Double.parseDouble(String.format("%.2f", tps[0]));
    }

    /**
     * 获取服务器当前总内存
     *
     * @return 总内存数(单位 MB)
     */
    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory() / 1048576L;
    }

    /**
     * 获取服务器当前总空闲内存
     *
     * @return 总空闲内存(单位 MB)
     */
    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory() / 1048576L;
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

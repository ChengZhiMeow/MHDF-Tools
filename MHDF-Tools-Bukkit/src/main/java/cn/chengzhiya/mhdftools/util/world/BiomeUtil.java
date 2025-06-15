package cn.chengzhiya.mhdftools.util.world;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.reflection.ReflectionUtil;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;

import java.util.Locale;

public final class BiomeUtil {
    /**
     * 获取指定群系key的群系实例
     *
     * @param key 群系key
     * @return 群系实例
     */
    public static Biome getBiome(String key) {
        if (Main.instance.getPluginHookManager().getPacketEventsHook().getServerVersion()
                .isNewerThanOrEquals(ServerVersion.V_1_21)
        ) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME)
                    .get(NamespacedKey.minecraft(key
                            .replace("_", ".")
                            .toLowerCase(Locale.ROOT)
                    ));
        }

        return ReflectionUtil.invokeMethod(
                ReflectionUtil.getMethod(Biome.class, "valueOf", true, String.class),
                Biome.class,
                key
        );
    }
}

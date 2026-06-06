package cn.chengzhimeow.mhdftools.bukkit.reflection.world;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lombok.SneakyThrows;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;

import java.lang.reflect.Method;
import java.util.Locale;

public final class BiomeUtil {
    private static Method Biome$method$valueOf;

    static {
        try {
            BiomeUtil.Biome$method$valueOf = Biome.class.getDeclaredMethod("valueOf", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定群系key的群系实例
     *
     * @param key 群系key
     * @return 群系实例
     */
    @SneakyThrows
    public static Biome getBiome(String key) {
        if (Main.instance.getPluginHookManager().getPacketEventsHook().getServerVersion()
                .isNewerThanOrEquals(ServerVersion.V_1_21_4)
        ) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME)
                    .get(NamespacedKey.minecraft(key
                            .replace("_", ".")
                            .toLowerCase(Locale.ROOT)
                    ));
        }

        return (Biome) BiomeUtil.Biome$method$valueOf.invoke(null, key);
    }
}

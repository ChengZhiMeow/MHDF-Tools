package cn.chengzhimeow.mhdftools.bukkit.reflection;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import lombok.SneakyThrows;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;

import java.lang.reflect.Method;
import java.util.Locale;

public final class SoundUtil {
    private static Method Sound$method$valueOf;

    static {
        try {
            SoundUtil.Sound$method$valueOf = Sound.class.getDeclaredMethod("valueOf", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定音效key的音效实例
     *
     * @param key 音效key
     * @return 音效实例
     */
    @SneakyThrows
    public static Sound getSound(String key) {
        if (Main.instance.getPluginHookManager().getPacketEventsHook().getServerVersion()
                .isNewerThanOrEquals(ServerVersion.V_1_21_4)
        ) {
            return Registry.SOUNDS
                    .get(NamespacedKey.minecraft(key
                            .replace("_", ".")
                            .toLowerCase(Locale.ROOT)
                    ));
        }

        return (Sound) SoundUtil.Sound$method$valueOf.invoke(null, key);
    }
}

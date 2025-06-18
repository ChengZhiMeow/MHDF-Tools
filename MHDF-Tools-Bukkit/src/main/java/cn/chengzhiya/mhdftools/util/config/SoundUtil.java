package cn.chengzhiya.mhdftools.util.config;

import cn.chengzhiya.mhdftools.exception.ResourceException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class SoundUtil {
    private static final File file = new File(ConfigUtil.getDataFolder(), "sound.yml");
    private static YamlConfiguration data;

    /**
     * 获取音效文件文件名称
     *
     * @return 音效文件文件名称
     */
    public static String getSoundFileName() {
        return "sound_zh.yml";
    }

    /**
     * 保存初始音效文件
     */
    public static void saveDefaultSound() throws ResourceException {
        FileUtil.saveResource("sound.yml", getSoundFileName(), false);
    }

    /**
     * 加载音效文件
     */
    public static void reloadSound() {
        YamlUtil.updateConfig(file, getSoundFileName());
        data = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 根据指定key获取对应音效
     */
    public static @NotNull String getSound(String key) {
        if (data == null) {
            reloadSound();
        }
        return data.getString(key, "");
    }
}

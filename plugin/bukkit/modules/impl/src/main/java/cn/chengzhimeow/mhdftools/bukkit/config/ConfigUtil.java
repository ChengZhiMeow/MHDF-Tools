package cn.chengzhimeow.mhdftools.bukkit.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;

import java.util.List;

public final class ConfigUtil {
    /**
     * 检测指定配置实例下的指定key列表对应的所有key是否为true
     *
     * @param keyList key列表
     * @return 结果
     */
    public static boolean equalsTrue(ConfigurationSection config, List<String> keyList) {
        for (String enableKey : keyList) {
            if (enableKey == null || enableKey.isEmpty()) continue;
            boolean result = config.getBoolean(enableKey);
            if (!result) return false;
        }

        return true;
    }
}

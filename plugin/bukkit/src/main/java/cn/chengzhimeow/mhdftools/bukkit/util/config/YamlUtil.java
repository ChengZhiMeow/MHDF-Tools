package cn.chengzhimeow.mhdftools.bukkit.util.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;

import java.util.List;

public final class YamlUtil {
    /**
     * 检测指定配置实例下的指定key列表对应的所有key是否为true
     *
     * @param keyList key列表
     * @return 结果
     */
    public static boolean equalsTrue(ConfigurationSection config, List<String> keyList) {
        boolean result = true;

        for (String enableKey : keyList) {
            if (enableKey == null || enableKey.isEmpty()) continue;
            result = config.getBoolean(enableKey);
        }

        return result;
    }
}

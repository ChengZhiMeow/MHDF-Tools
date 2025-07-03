package cn.chengzhiya.mhdftools.util.config;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.PluginUtil;
import cn.chengzhiya.mhdftools.util.reflection.ReflectionUtil;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class YamlUtil {
    /**
     * 获取配置项列表
     *
     * @param config 配置实例
     * @param key    目标key
     * @return 配置项列表
     */
    public static List<YamlConfiguration> getConfigurationSectionList(ConfigurationSection config, String key) {
        List<?> data = config.getList(key);
        if (data == null) {
            return new ArrayList<>();
        }

        Yaml yaml = new Yaml();

        return data.stream()
                .map(yaml::dump)
                .map(StringReader::new)
                .map(YamlConfiguration::loadConfiguration)
                .toList();
    }

    /**
     * 检测指定配置实例下的指定key列表对应的所有key是否为true
     *
     * @param keyList key列表
     * @return 结果
     */
    public static boolean equalsTrue(ConfigurationSection config, List<String> keyList) {
        boolean result = true;

        for (String enableKey : keyList) {
            if (enableKey == null || enableKey.isEmpty()) {
                continue;
            }

            result = config.getBoolean(enableKey);
        }

        return result;
    }

    /**
     * 更新配置文件
     *
     * @param configFile        配置文件文件实例
     * @param jarConfigFileName 插件内配置文件文件名称
     */
    @SneakyThrows
    public static void updateConfig(File configFile, String jarConfigFileName) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        String configVersion = config.getString("configVersion");
        if (configVersion != null && configVersion.equals(PluginUtil.getVersion())) {
            return;
        }

        URL url = Main.class.getClassLoader().getResource(jarConfigFileName);
        if (url == null) {
            return;
        }

        try (InputStream in = url.openStream()) {
            YamlConfiguration jarConfig = YamlConfiguration.loadConfiguration(new StringReader(new String(in.readAllBytes())));
            Set<String> jarConfigKeys = jarConfig.getKeys(true);
            Set<String> configKeys = config.getKeys(true);

            jarConfigKeys.removeAll(configKeys);

            // 过滤有父级的键
            {
                //noinspection ExtractMethodRecommender
                Set<String> filteredKeys = new HashSet<>();
                for (String key : jarConfigKeys) {
                    int lastDotIndex = key.lastIndexOf('.');
                    if (lastDotIndex == -1) {
                        filteredKeys.add(key);
                    } else {
                        String parentKey = key.substring(0, lastDotIndex);
                        if (configKeys.contains(parentKey)) {
                            filteredKeys.add(key);
                        }
                    }
                }
                jarConfigKeys = filteredKeys;
            }

            forKey:
            for (String key : jarConfigKeys) {
                String[] keyParts = key.split("\\.");
                StringBuilder prefixBuilder = new StringBuilder();

                // 绕过部分配置项使其不被更新加入
                for (int i = 0; i < keyParts.length; i++) {
                    String part = keyParts[i];

                    if (i > 0) {
                        prefixBuilder.append('.');
                    }
                    prefixBuilder.append(part);

                    List<String> prefixComments = jarConfig.getComments(prefixBuilder.toString());
                    if (prefixComments.contains("!noUpdate")) {
                        continue forKey;
                    }
                }

                // 更新配置值
                config.set(key, jarConfig.get(key));

                // 更新注释
                List<String> comments = jarConfig.getComments(key);
                if (!comments.isEmpty()) {
                    config.setComments(key, comments);
                }
            }

            config.set("configVersion", PluginUtil.getVersion());

            // 防止破坏横向格式
            YamlConfigurationOptions newOptions = config.options();
            newOptions.width(Integer.MAX_VALUE);
            ReflectionUtil.setFieldValue(
                    ReflectionUtil.getField(MemoryConfiguration.class, "options", true),
                    jarConfig,
                    newOptions
            );

            config.save(configFile);
        }
    }
}

package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdflibrary.MHDFLibrary;
import cn.chengzhiya.mhdflibrary.entity.DependencyConfig;
import cn.chengzhiya.mhdflibrary.entity.RelocateConfig;
import cn.chengzhiya.mhdflibrary.entity.RepositoryConfig;
import cn.chengzhiya.mhdflibrary.manager.LoggerManager;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@SuppressWarnings("unused")
public final class LibrariesManager {
    private final RepositoryConfig chengzhiMeow = new RepositoryConfig("https://maven.chengzhimeow.cn/releases/");
    private final RepositoryConfig codemc = new RepositoryConfig("https://repo.codemc.io/repository/maven-public/");

    /**
     * 下载并加载所有所需依赖
     */
    public void init() {
        MHDFLibrary mhdfLibrary = new MHDFLibrary(
                Main.class,
                new LibraryLoggerManager(),
                "cn.chengzhiya.mhdftools.libs",
                new File(Main.instance.getDataFolder(), "libs")
        );
        mhdfLibrary.getHttpManager().setProxy(Main.instance.getConfigManager().getProxyManager().getProxy());

        int currentVersion = Integer.parseInt(Bukkit.getMinecraftVersion().replace(".", ""));
        {
            YamlConfiguration config = Main.instance.getConfigManager().getLibraryManager().getData();
            for (String key : config.getKeys(true)) {
                ConfigurationSection section = config.getConfigurationSection(key);
                if (section == null) continue;

                String repo = section.getString("repo");
                boolean adventureIgnore = section.getBoolean("adventureIgnore");
                ConfigurationSection versionConfig = section.getConfigurationSection("version");
                ConfigurationSection relocateConfig = section.getConfigurationSection("relocate");
                if (repo == null || versionConfig == null) continue;

                int lastDot = key.lastIndexOf('.');
                String group = key.substring(0, lastDot);
                String artifact = key.substring(lastDot + 1);

                String version = versionConfig.getString("default.value");
                for (String versionKey : versionConfig.getKeys(false)) {
                    if (versionKey.equals("default")) continue;

                    ConfigurationSection versionSection = versionConfig.getConfigurationSection(versionKey);
                    if (versionSection == null) continue;

                    int targetVersion = Integer.parseInt(versionKey);
                    String type = section.getString("type");
                    String value = section.getString("value");
                    if (type == null || value == null) continue;

                    boolean result = switch (type) {
                        case "<" -> targetVersion < currentVersion;
                        case "<=" -> targetVersion <= currentVersion;
                        case "==" -> targetVersion == currentVersion;
                        case ">=" -> targetVersion >= currentVersion;
                        case ">" -> targetVersion > currentVersion;
                        default -> false;
                    };

                    if (result) {
                        version = value;
                        break;
                    }
                }

                mhdfLibrary.addDependencyConfig(new DependencyConfig(
                        group,
                        artifact,
                        version,
                        new RepositoryConfig(repo),
                        !(PluginUtil.isNativeSupportAdventureApi() && section.getBoolean("adventureIgnore")),
                        relocateConfig != null ? new RelocateConfig(
                                relocateConfig.getBoolean("enable", false),
                                relocateConfig.getBoolean("relocatableGroupId", true),
                                relocateConfig.getStringList("relocator").toArray(String[]::new)
                        ) : new RelocateConfig(false)
                ));
            }
        }

        mhdfLibrary.downloadDependencies();
        mhdfLibrary.loadDependencies();
    }

    /**
     * 处理文本
     *
     * @param string 文本
     * @return 处理后的文本
     */
    private String handleString(String string) {
        return string.replace("{}", ".");
    }

    static class LibraryLoggerManager implements LoggerManager {
        @Override
        public void log(String string) {
            Main.instance.getLogger().info(string);
        }
    }
}

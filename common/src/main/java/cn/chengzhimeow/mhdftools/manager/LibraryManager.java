package cn.chengzhimeow.mhdftools.manager;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.ccyaml.configuration.StringSectionData;
import cn.chengzhimeow.mhdftools.config.ConfigManager;
import cn.chengzhimeow.mhdftools.config.impl.LibrarySetting;
import cn.chengzhimeow.mhdftools.config.impl.ProxySetting;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import cn.chengzhiya.mhdflibrary.MHDFLibrary;
import cn.chengzhiya.mhdflibrary.entity.DependencyConfig;
import cn.chengzhiya.mhdflibrary.entity.RelocateConfig;
import cn.chengzhiya.mhdflibrary.entity.RepositoryConfig;
import cn.chengzhiya.mhdflibrary.manager.LoggerManager;
import lombok.Setter;

import java.io.File;
import java.util.List;
import java.util.Objects;

public final class LibraryManager {
    private static LibraryManager instance;

    public static LibraryManager getInstance() {
        if (LibraryManager.instance == null) LibraryManager.instance = new LibraryManager();
        return LibraryManager.instance;
    }

    @Setter
    private LoggerManager loggerManager;

    private LibraryManager() {
    }

    private boolean checkMcVersion(ConfigurationSection mcVersion) {
        if (mcVersion == null) return true;

        String type = mcVersion.getString("type", "==");
        assert type != null;

        String value = mcVersion.getString("value");
        if (value == null) return true;
        int version = Integer.parseInt(value.replace(".", ""));

        return switch (type) {
            case "<" -> PluginManager.getInstance().minecraftVersion < version;
            case "<=" -> PluginManager.getInstance().minecraftVersion <= version;
            case "==" -> PluginManager.getInstance().minecraftVersion == version;
            case ">=" -> PluginManager.getInstance().minecraftVersion >= version;
            case ">" -> PluginManager.getInstance().minecraftVersion > version;
            default -> true;
        };
    }

    public void init() {
        MHDFLibrary mhdfLibrary = new MHDFLibrary(
                LibraryManager.class,
                this.loggerManager,
                "cn.chengzhimeow.mhdftools.libs",
                new File(ConfigManager.getInstance().getDataFolder(), "libs")
        );
        mhdfLibrary.getHttpManager().setProxy(ProxySetting.getInstance().getProxy());

        for (ConfigurationSection config : LibrarySetting.getInstance().getData().getConfigurationSectionList("library")) {
            RepositoryConfig repo = new RepositoryConfig(config.getString("repo"));

            Object groupIdValue = config.get("group_id");
            String groupId = null;
            if (groupIdValue instanceof StringSectionData s) groupId = s.getValue();
            else if (groupIdValue instanceof List) {
                List<ConfigurationSection> sectionList = config.getConfigurationSectionList("group_id");
                for (ConfigurationSection section : sectionList) {
                    ConfigurationSection mcVersion = section.getConfigurationSection("mc_version");
                    if (!this.checkMcVersion(mcVersion)) continue;
                    groupId = section.getString("value");
                    break;
                }
            }

            Object artifactIdValue = config.get("artifact_id");
            String artifactId = null;
            if (artifactIdValue instanceof StringSectionData s) artifactId = s.getValue();
            else if (artifactIdValue instanceof List) {
                List<ConfigurationSection> sectionList = config.getConfigurationSectionList("artifact_id");
                for (ConfigurationSection section : sectionList) {
                    ConfigurationSection mcVersion = section.getConfigurationSection("mc_version");
                    if (!this.checkMcVersion(mcVersion)) continue;
                    artifactId = section.getString("value");
                    break;
                }
            }

            Object versionValue = config.get("version");
            String version = null;
            if (versionValue instanceof StringSectionData s) version = s.getValue();
            else if (versionValue instanceof List) {
                List<ConfigurationSection> sectionList = config.getConfigurationSectionList("version");
                for (ConfigurationSection section : sectionList) {
                    ConfigurationSection mcVersion = section.getConfigurationSection("mc_version");
                    if (!this.checkMcVersion(mcVersion)) continue;
                    version = section.getString("value");
                    break;
                }
            }

            if (config.has("dependency")) {
                String[] dependency = Objects.requireNonNull(config.getString("dependency")).split(":");
                groupId = dependency[0];
                if (dependency.length >= 2) artifactId = dependency[1];
                if (dependency.length == 3) version = dependency[2];
            }

            Boolean relocateGroupId = config.getBoolean("relocate.group_id", true);
            assert relocateGroupId != null;
            RelocateConfig relocate = new RelocateConfig(
                    config.getBoolean("relocate.enable"),
                    relocateGroupId,
                    config.getStringList("relocate.relocator").toArray(String[]::new)
            );

            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    groupId,
                    artifactId,
                    version,
                    repo,
                    relocate
            ));
        }

        mhdfLibrary.downloadDependencies();
        mhdfLibrary.loadDependencies();
    }
}

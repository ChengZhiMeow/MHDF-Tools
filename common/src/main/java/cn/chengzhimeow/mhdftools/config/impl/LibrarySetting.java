package cn.chengzhimeow.mhdftools.config.impl;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.ccyaml.configuration.StringSectionData;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import cn.chengzhiya.mhdflibrary.entity.DependencyConfig;
import cn.chengzhiya.mhdflibrary.entity.RelocateConfig;
import cn.chengzhiya.mhdflibrary.entity.RepositoryConfig;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class LibrarySetting extends AbstractYamlSetting<LibrarySetting.Config> {
    @Getter(lazy = true)
    private static final LibrarySetting instance = new LibrarySetting();
    @Getter private Config config;

    private LibrarySetting() {
    }

    @Override
    public String originFilePath() {
        return "library/" + PluginManager.getInstance().serverType.toString().toLowerCase() + ".yml";
    }

    @Override
    public String filePath() {
        return "library.yml";
    }

    @Override
    public void update() {
        if (!super.getData().getBoolean("update")) return;

        String version = PluginManager.getInstance().version;
        String configVersion = super.getData().getString("config_version");
        if (configVersion != null && configVersion.equals(version)) return;

        super.getFile().delete();
        super.saveDefaultFile();
    }

    @Override
    public void reload() {
        super.reload();

        List<DependencyConfig> libraries = new ArrayList<>();
        for (ConfigurationSection config : super.getData().getConfigurationSectionList("library")) {
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
            String dependencyVersion = null;
            if (versionValue instanceof StringSectionData s) dependencyVersion = s.getValue();
            else if (versionValue instanceof List) {
                List<ConfigurationSection> sectionList = config.getConfigurationSectionList("version");
                for (ConfigurationSection section : sectionList) {
                    ConfigurationSection mcVersion = section.getConfigurationSection("mc_version");
                    if (!this.checkMcVersion(mcVersion)) continue;
                    dependencyVersion = section.getString("value");
                    break;
                }
            }

            if (config.has("dependency")) {
                String[] dependency = Objects.requireNonNull(config.getString("dependency")).split(":");
                groupId = dependency[0];
                if (dependency.length >= 2) artifactId = dependency[1];
                if (dependency.length == 3) dependencyVersion = dependency[2];
            }

            Boolean relocateGroupId = config.getBoolean("relocate.group_id", true);
            assert relocateGroupId != null;
            RelocateConfig relocate = new RelocateConfig(
                    config.getBoolean("relocate.enable"),
                    relocateGroupId,
                    config.getStringList("relocate.relocator").toArray(String[]::new)
            );

            libraries.add(new DependencyConfig(
                    groupId,
                    artifactId,
                    dependencyVersion,
                    repo,
                    relocate
            ));
        }

        this.config = new Config(libraries);
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

    public record Config(
            List<DependencyConfig> libraries
    ) {
    }
}

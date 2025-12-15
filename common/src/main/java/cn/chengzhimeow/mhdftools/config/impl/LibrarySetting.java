package cn.chengzhimeow.mhdftools.config.impl;

import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import lombok.Getter;

public final class LibrarySetting extends AbstractYamlSetting {
    @Getter(lazy = true)
    private static final LibrarySetting instance = new LibrarySetting();

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
}

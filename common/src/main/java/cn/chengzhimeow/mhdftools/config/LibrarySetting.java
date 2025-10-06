package cn.chengzhimeow.mhdftools.config;

import cn.chengzhimeow.ccyaml.manager.AbstractYamlManager;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;

public final class LibrarySetting extends AbstractYamlManager {
    private static LibrarySetting instance;

    public static LibrarySetting getSettingInstance() {
        if (LibrarySetting.instance == null) LibrarySetting.instance = new LibrarySetting();
        return LibrarySetting.instance;
    }

    private LibrarySetting() {
        super(ConfigManager.getInstance().getYamlManager());
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

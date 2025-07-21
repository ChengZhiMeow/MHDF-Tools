package cn.chengzhiya.mhdftools.manager.config;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdfyaml.manager.YamlManager;

public final class LibraryManager extends YamlManager {
    public LibraryManager() {
        super(Main.instance.getYamlManager());
    }

    @Override
    public String getOriginFilePath() {
        return "library_zh.yml";
    }

    @Override
    public String getFilePath() {
        return "library.yml";
    }

    @Override
    public void update() {
        if (!super.getData().getBoolean("update")) {
            return;
        }

        String version = Main.instance.getDescription().getVersion();
        String configVersion = super.getData().getString("configVersion");
        if (configVersion != null && configVersion.equals(version)) {
            return;
        }

        super.getFile().delete();
        super.saveDefaultFile();
    }
}

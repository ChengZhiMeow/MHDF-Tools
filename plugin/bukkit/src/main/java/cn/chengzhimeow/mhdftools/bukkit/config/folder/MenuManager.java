package cn.chengzhimeow.mhdftools.bukkit.config.folder;

import cn.chengzhimeow.ccyaml.manager.AbstractFolderYamlManager;
import cn.chengzhimeow.mhdftools.config.ConfigManager;

public final class MenuManager extends AbstractFolderYamlManager {
    private static MenuManager instance;

    private MenuManager() {
        super(ConfigManager.getInstance().getYamlManager());
    }

    public static MenuManager getSettingInstance() {
        if (MenuManager.instance == null) {
            MenuManager.instance = new MenuManager();
        }
        return MenuManager.instance;
    }

    @Override
    public String originFilePath() {
        return "menu";
    }

    @Override
    public String filePath() {
        return "menu";
    }
}

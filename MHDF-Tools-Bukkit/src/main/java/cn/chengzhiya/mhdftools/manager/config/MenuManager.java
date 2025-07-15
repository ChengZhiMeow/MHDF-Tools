package cn.chengzhiya.mhdftools.manager.config;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdfyaml.manager.FolderYamlManager;

public final class MenuManager extends FolderYamlManager {
    public MenuManager() {
        super(Main.instance.getYamlManager());
    }

    @Override
    public String getOriginFolderPath() {
        return "menu";
    }

    @Override
    public String getFolderPath() {
        return "menu";
    }
}

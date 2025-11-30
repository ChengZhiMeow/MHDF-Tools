package cn.chengzhimeow.mhdftools.bukkit.module.config;

import cn.chengzhimeow.ccyaml.manager.AbstractYamlManager;
import cn.chengzhimeow.mhdftools.config.ConfigManager;

public abstract class ModuleConfigSetting extends AbstractYamlManager {
    public ModuleConfigSetting() {
        super(ConfigManager.getInstance().getYamlManager());
    }
}

package cn.chengzhimeow.mhdftools.config;

import cn.chengzhimeow.ccyaml.manager.AbstractYamlManager;

public abstract class AbstractYamlSetting extends AbstractYamlManager {
    public AbstractYamlSetting() {
        super(ConfigManager.getInstance().getYamlManager());
    }
}

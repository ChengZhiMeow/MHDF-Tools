package cn.chengzhimeow.mhdftools.config;

import cn.chengzhimeow.ccyaml.manager.AbstractYamlManager;
import lombok.Getter;

public abstract class AbstractYamlSetting<T> extends AbstractYamlManager {
    /**
     * 配置读取必须在 reload() 中缓存到 getConfig()，业务侧不要直接读取 getData()。
     */
    @Getter
    protected T config;

    public AbstractYamlSetting() {
        super(ConfigManager.getInstance().getYamlManager());
    }
}

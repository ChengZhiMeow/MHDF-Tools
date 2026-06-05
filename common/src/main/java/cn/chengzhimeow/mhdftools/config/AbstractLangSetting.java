package cn.chengzhimeow.mhdftools.config;

import cn.chengzhimeow.ccyaml.manager.AbstractYamlManager;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;

public abstract class AbstractLangSetting<T> extends AbstractYamlManager {
    @Getter protected T config;

    public AbstractLangSetting() {
        super(ConfigManager.getInstance().getYamlManager());
    }

    protected TextComponent component(String key) {
        return this.component(key, null);
    }

    protected TextComponent component(String key, TextComponent prefix) {
        return ColorUtil.color(super.getData().getString(key, ""))
                .replace("{version}", PluginManager.getInstance().version)
                .replace("{prefix}", prefix == null ? new TextComponent() : prefix);
    }
}

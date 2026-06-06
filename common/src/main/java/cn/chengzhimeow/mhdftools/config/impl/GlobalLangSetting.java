package cn.chengzhimeow.mhdftools.config.impl;

import cn.chengzhimeow.mhdftools.config.AbstractLangSetting;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;

public final class GlobalLangSetting extends AbstractLangSetting<GlobalLangSetting.Config> {
    @Getter(lazy = true)
    private static final GlobalLangSetting instance = new GlobalLangSetting();

    private GlobalLangSetting() {
    }

    @Override
    public String originFilePath() {
        return "lang/" + PluginManager.getInstance().serverType.toString().toLowerCase() + ".yml";
    }

    @Override
    public String filePath() {
        return "lang.yml";
    }

    @Override
    public void reload() {
        super.reload();
        TextComponent prefix = super.component("prefix");

        this.config = new Config(
                prefix,
                super.component("only_player", prefix),
                super.component("no_permission", prefix),
                super.component("usage_error", prefix),
                super.component("player_offline", prefix),
                super.component("enable", prefix),
                super.component("disable", prefix),
                super.component("black_world", prefix)
        );
    }

    public record Config(
            TextComponent prefix,
            TextComponent onlyPlayer,
            TextComponent noPermission,
            TextComponent usageError,
            TextComponent playerOffline,
            TextComponent enable,
            TextComponent disable,
            TextComponent blackWorld
    ) {
    }
}

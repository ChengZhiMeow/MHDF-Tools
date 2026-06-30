package cn.chengzhimeow.mhdftools.bukkit.module.tpahere;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.config.TpaHereMenuSetting;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;
import net.nyana.cache.service.CacheService;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;
    @Getter private CacheService<String, String> requestCache;
    @Getter private CacheService<String, String> delayCache;

    public ModuleMain() {
        super("tpahere");
        ModuleMain.instance = this;
    }

    @Override
    public void onLoad() {
        ConfigSetting.getInstance().saveDefaultFile();
        ConfigSetting.getInstance().update();
        ConfigSetting.getInstance().reload();

        LangSetting.getInstance().saveDefaultFile();
        LangSetting.getInstance().update();
        LangSetting.getInstance().reload();

        TpaHereMenuSetting.getInstance().saveDefaultFile();
        TpaHereMenuSetting.getInstance().update();
        TpaHereMenuSetting.getInstance().reload();

        this.requestCache = MHDFToolsBukkit.getInstance().getCacheManager().createCache("module:tpahere:request", String.class);
        this.delayCache = MHDFToolsBukkit.getInstance().getCacheManager().createCache("module:tpahere:delay", String.class);
    }

    @Override
    public boolean isEnable() {
        return ConfigSetting.getInstance().getConfig().enable();
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}

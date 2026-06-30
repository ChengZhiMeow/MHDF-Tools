package cn.chengzhimeow.mhdftools.bukkit.module.tpa;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.TpaMenuSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.message.TpaTeleportMessage;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;
import net.nyana.cache.service.CacheService;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;
    @Getter private CacheService<String, String> requestCache;
    @Getter private CacheService<String, String> delayCache;

    public ModuleMain() {
        super("tpa");
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

        TpaMenuSetting.getInstance().saveDefaultFile();
        TpaMenuSetting.getInstance().update();
        TpaMenuSetting.getInstance().reload();

        this.requestCache = MHDFToolsBukkit.getInstance().getCacheManager().createCache("module:tpa:request", String.class);
        this.delayCache = MHDFToolsBukkit.getInstance().getCacheManager().createCache("module:tpa:delay", String.class);
    }

    @Override
    public boolean isEnable() {
        return ConfigSetting.getInstance().getConfig().enable();
    }

    @Override
    public void onEnable() {
        MHDFToolsBukkit.getInstance().getRedisManager().register(TpaTeleportMessage.ID, TpaTeleportMessage.CODEC);
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}

package cn.chengzhimeow.mhdftools.bukkit.module.tpahere;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.cache.DelayCache;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.cache.RequestCache;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.config.TpaHereMenuSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.thread.TpaHereTimeoutThread;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;
    @Getter private final RequestCache requestCache = new RequestCache();
    @Getter private final DelayCache delayCache = new DelayCache();
    @Getter private TpaHereTimeoutThread timeoutThread;

    public ModuleMain() {
        super("tpahere");
        ModuleMain.instance = this;
    }

    @Override
    public void onLoad() {
        this.timeoutThread = new TpaHereTimeoutThread();
    }

    @Override
    public boolean isEnable() {
        return ConfigSetting.getInstance().getConfig().enable();
    }

    @Override
    public void onEnable() {
        this.requestCache.init();
        this.delayCache.init();
    }

    @Override
    public void onDisable() {
        this.requestCache.close();
        this.delayCache.close();
        if (this.timeoutThread != null) this.timeoutThread.close();
    }

    @Override
    public void reloadConfig() {
        ConfigSetting.getInstance().saveDefaultFile();
        ConfigSetting.getInstance().update();
        ConfigSetting.getInstance().reload();

        LangSetting.getInstance().saveDefaultFile();
        LangSetting.getInstance().update();
        LangSetting.getInstance().reload();

        TpaHereMenuSetting.getInstance().saveDefaultFile();
        TpaHereMenuSetting.getInstance().update();
        TpaHereMenuSetting.getInstance().reload();
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}

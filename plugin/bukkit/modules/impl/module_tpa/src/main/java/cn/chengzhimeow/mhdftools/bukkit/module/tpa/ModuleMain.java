package cn.chengzhimeow.mhdftools.bukkit.module.tpa;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.cache.DelayCache;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.cache.RequestCache;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.TpaMenuSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.message.TpaTeleportMessage;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.thread.TpaTimeoutThread;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;
    @Getter private final RequestCache requestCache = new RequestCache();
    @Getter private final DelayCache delayCache = new DelayCache();
    @Getter private TpaTimeoutThread timeoutThread;

    public ModuleMain() {
        super("tpa");
        ModuleMain.instance = this;
    }

    @Override
    public void onLoad() {
        this.timeoutThread = new TpaTimeoutThread();
    }

    @Override
    public boolean isEnable() {
        return ConfigSetting.getInstance().getConfig().enable();
    }

    @Override
    public void onEnable() {
        this.requestCache.init();
        this.delayCache.init();

        MHDFToolsBukkit.getInstance().getRedisManager().register(TpaTeleportMessage.ID, TpaTeleportMessage.CODEC);
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

        TpaMenuSetting.getInstance().saveDefaultFile();
        TpaMenuSetting.getInstance().update();
        TpaMenuSetting.getInstance().reload();
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}

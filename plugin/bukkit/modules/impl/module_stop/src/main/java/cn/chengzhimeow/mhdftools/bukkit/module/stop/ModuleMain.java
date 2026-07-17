package cn.chengzhimeow.mhdftools.bukkit.module.stop;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.stop.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.stop.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.stop.signal.StopServerSignalHandler;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.jetbrains.annotations.NotNull;
import sun.misc.Signal;

public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("stop");
        ModuleMain.instance = this;
    }

    @Override
    public boolean isEnable() {
        return ConfigSetting.getInstance().getConfig().enable();
    }

    @Override
    public void onEnable() {
        Signal signal = new Signal("INT");
        Signal.handle(signal, new StopServerSignalHandler());
    }

    @Override
    public void reloadConfig() {
        ConfigSetting.getInstance().saveDefaultFile();
        ConfigSetting.getInstance().update();
        ConfigSetting.getInstance().reload();

        LangSetting.getInstance().saveDefaultFile();
        LangSetting.getInstance().update();
        LangSetting.getInstance().reload();
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}

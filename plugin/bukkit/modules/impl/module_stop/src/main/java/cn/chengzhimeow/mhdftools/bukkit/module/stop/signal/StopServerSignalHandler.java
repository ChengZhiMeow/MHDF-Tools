package cn.chengzhimeow.mhdftools.bukkit.module.stop.signal;

import cn.chengzhimeow.mhdftools.bukkit.module.stop.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.stop.config.LangSetting;
import org.bukkit.Bukkit;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public final class StopServerSignalHandler implements SignalHandler {
    @Override
    public void handle(Signal sig) {
        if (!ConfigSetting.getInstance().getConfig().disableCtrlC()) {
            Bukkit.shutdown();
            return;
        }

        Bukkit.getConsoleSender().sendMessage(LangSetting.getInstance().getConfig().disableCtrlCStopServer());
    }
}

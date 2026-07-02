package cn.chengzhimeow.mhdftools.bukkit.module.tpa.thread;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.LangSetting;
import cn.chengzhimeow.mhdftools.thread.ThreadPool;

public final class TpaTimeoutThread {
    private final ThreadPool thread = new ThreadPool(1, "MHDF-Tools TPA Timeout Thread");

    public void schedule(String playerName, String targetName) {
        int timeout = Math.max(ConfigSetting.getInstance().getConfig().time(), 1);
        this.thread.schedule(() -> {
            String currentTarget = ModuleMain.instance.getRequestCache().get(playerName);
            if (!targetName.equals(currentTarget)) return;

            ModuleMain.instance.getRequestCache().remove(playerName);

            MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(playerName);
            MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(targetName);
            player.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().delay().message()
                    .replace("{player}", targetName));
            target.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().delay().timeOut()
                    .replace("{player}", playerName));
        }, timeout * 1000L);
    }

    public void close() {
        this.thread.kill();
    }
}

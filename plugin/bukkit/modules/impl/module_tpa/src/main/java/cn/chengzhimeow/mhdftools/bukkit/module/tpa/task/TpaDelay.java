package cn.chengzhimeow.mhdftools.bukkit.module.tpa.task;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Task;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.LangSetting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

final class TpaDelay extends Task {
    public TpaDelay() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                20L
        );
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String delayString = ModuleMain.instance.getDelayCache().get(player.getName());
            if (delayString == null) continue;

            int delay = Integer.parseInt(delayString);
            if (delay <= 0) {
                String targetName = ModuleMain.instance.getRequestCache().get(player.getName());
                ModuleMain.instance.getRequestCache().remove(player.getName());
                ModuleMain.instance.getDelayCache().remove(player.getName());

                if (targetName != null) {
                    player.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().delay().message()
                            .replace("{player}", targetName));
                    MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(targetName).sendMessage(
                            LangSetting.getInstance().getConfig().commands().tpa().delay().timeOut()
                                    .replace("{player}", player.getName())
                    );
                }
                continue;
            }

            ModuleMain.instance.getDelayCache().put(player.getName(), String.valueOf(delay - 1));
        }
    }
}

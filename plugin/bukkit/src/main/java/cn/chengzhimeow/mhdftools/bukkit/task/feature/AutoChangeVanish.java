package cn.chengzhimeow.mhdftools.bukkit.task.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.task.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("unused")
final class AutoChangeVanish extends Task {
    public AutoChangeVanish() {
        super(
                List.of("vanishSettings.enable"),
                20L
        );
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
            if (!mhdfPlayer.isEnableVanish()) {
                continue;
            }

            mhdfPlayer.hidePlayer();
        }
    }
}

package cn.chengzhiya.mhdftools.task.feature;

import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.task.AbstractTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("unused")
public final class AutoChangeVanish extends AbstractTask {
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

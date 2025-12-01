package cn.chengzhimeow.mhdftools.bukkit.task.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("unused")
final class TpaHereDelay extends Task {
    public TpaHereDelay() {
        super(
                List.of("tpahereSettings.enable"),
                20L
        );
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String delayString = Main.instance.getCacheManager().get("tpahereDelay", player.getName());
            if (delayString == null) {
                continue;
            }

            int delay = Integer.parseInt(delayString);
            if (delay <= 0) {
                Main.instance.getCacheManager().remove("tpahereDelay", player.getName());
                Main.instance.getCacheManager().remove("tpahereDelay", player.getName());
                return;
            }

            delay--;
            Main.instance.getCacheManager().put("tpahereDelay", player.getName(), String.valueOf(delay));
        }
    }
}

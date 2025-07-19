package cn.chengzhiya.mhdftools.task.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.task.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("unused")
final class TpaDelay extends Task {
    public TpaDelay() {
        super(
                List.of("tpaSettings.enable"),
                20L
        );
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String delayString = Main.instance.getCacheManager().get("tpaDelay", player.getName());
            if (delayString == null) {
                continue;
            }

            int delay = Integer.parseInt(delayString);
            if (delay <= 0) {
                Main.instance.getCacheManager().remove("tpaDelay", player.getName());
                Main.instance.getCacheManager().remove("tpaPlayer", player.getName());
                return;
            }

            delay--;
            Main.instance.getCacheManager().put("tpaDelay", player.getName(), String.valueOf(delay));
        }
    }
}

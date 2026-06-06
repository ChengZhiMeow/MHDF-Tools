package cn.chengzhimeow.mhdftools.bukkit.task.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Task;

import java.util.List;

final class RandomTeleportDelay extends Task {
    public RandomTeleportDelay() {
        super(
                List.of("chatSettings.enable"),
                20L
        );
    }

    @Override
    public void run() {
        for (String key : Main.instance.getCacheManager().keys("randomTeleportDelay")) {
            String delayData = Main.instance.getCacheManager().get("randomTeleportDelay", key);
            if (delayData == null) {
                continue;
            }

            int delay = Integer.parseInt(delayData);
            if (delay <= 0) {
                Main.instance.getCacheManager().remove("randomTeleportDelay", key);
                continue;
            }

            delay--;
            Main.instance.getCacheManager().put("randomTeleportDelay", key, String.valueOf(delay));
        }
    }
}

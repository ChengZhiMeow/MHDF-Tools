package cn.chengzhiya.mhdftools.task.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.task.Task;

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

package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.Metrics;

public final class BStatsManager {
    /**
     * 初始化bstats
     */
    public void init() {
        if (!Main.instance.getConfigManager().getConfigManager().getData().getBoolean("bStats")) {
            return;
        }

        new Metrics(24887);
    }
}

package cn.chengzhimeow.mhdftools.bukkit.module.feature;

import cn.chengzhimeow.ccscheduler.runnable.CCRunnable;
import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.config.ConfigUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Task extends CCRunnable {
    private final Module module;
    private final boolean enable;
    private final long time;

    public Task(@NotNull Module module, @NotNull List<String> enableKeyList, long time) {
        super(CCScheduler.getInstance());
        this.module = module;
        this.enable = ConfigUtil.equalsTrue(module.getConfig().getData(), enableKeyList);
        this.time = time;
    }

    public Task(@NotNull Module module, long time) {
        this(module, new ArrayList<>(), time);
    }
}

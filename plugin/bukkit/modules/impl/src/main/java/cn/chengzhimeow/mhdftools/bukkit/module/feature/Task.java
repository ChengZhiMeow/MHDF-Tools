package cn.chengzhimeow.mhdftools.bukkit.module.feature;

import cn.chengzhimeow.ccscheduler.runnable.CCRunnable;
import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class Task extends CCRunnable {
    private final Module module;
    private final boolean enable;
    private final long time;

    public Task(@NotNull Module module, boolean enable, long time) {
        super(CCScheduler.getInstance());
        this.module = module;
        this.enable = enable;
        this.time = time;
    }

    public Task(@NotNull Module module, long time) {
        this(module, true, time);
    }
}

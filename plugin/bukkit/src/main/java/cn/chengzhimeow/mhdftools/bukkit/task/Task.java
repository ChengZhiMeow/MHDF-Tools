package cn.chengzhimeow.mhdftools.bukkit.task;

import cn.chengzhimeow.ccscheduler.runnable.CCRunnable;
import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.config.YamlUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Task extends CCRunnable {
    private final boolean enable;
    private final Long time;

    public Task(List<String> enableKeyList, @NotNull Long time) {
        super(CCScheduler.getInstance());
        this.enable = YamlUtil.equalsTrue(ConfigSetting.getSettingInstance().getData(), enableKeyList);
        this.time = time;
    }

    public Task(@NotNull Long time) {
        this(new ArrayList<>(), time);
    }
}

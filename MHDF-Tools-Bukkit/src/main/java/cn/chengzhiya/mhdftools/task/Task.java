package cn.chengzhiya.mhdftools.task;

import cn.chengzhiya.mhdfscheduler.runnable.MHDFRunnable;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.config.YamlUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Task extends MHDFRunnable {
    private final boolean enable;
    private final Long time;

    public Task(List<String> enableKeyList, @NotNull Long time) {
        this.enable = YamlUtil.equalsTrue(Main.instance.getConfigManager().getConfigManager().getData(), enableKeyList);
        this.time = time;
    }

    public Task(@NotNull Long time) {
        this(new ArrayList<>(), time);
    }
}

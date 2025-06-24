package cn.chengzhiya.mhdftools.task;

import cn.chengzhiya.mhdfscheduler.runnable.MHDFRunnable;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.YamlUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractTask extends MHDFRunnable {
    private final boolean enable;
    private final Long time;

    public AbstractTask(List<String> enableKeyList, @NotNull Long time) {
        this.enable = YamlUtil.equalsTrue(ConfigUtil.getConfig(), enableKeyList);
        this.time = time;
    }

    public AbstractTask(@NotNull Long time) {
        this(new ArrayList<>(), time);
    }
}

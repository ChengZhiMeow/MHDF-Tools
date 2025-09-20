package cn.chengzhimeow.mhdftools.bukkit.task;

import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.config.YamlUtil;
import cn.chengzhiya.mhdfscheduler.runnable.MHDFRunnable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Task extends MHDFRunnable {
    private final boolean enable;
    private final Long time;

    public Task(List<String> enableKeyList, @NotNull Long time) {
        this.enable = YamlUtil.equalsTrue(ConfigSetting.getSettingInstance().getData(), enableKeyList);
        this.time = time;
    }

    public Task(@NotNull Long time) {
        this(new ArrayList<>(), time);
    }
}

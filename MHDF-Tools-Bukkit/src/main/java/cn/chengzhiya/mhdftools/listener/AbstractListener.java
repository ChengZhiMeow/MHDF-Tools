package cn.chengzhiya.mhdftools.listener;

import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.YamlUtil;
import lombok.Getter;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractListener implements Listener {
    private final boolean enable;

    public AbstractListener(List<String> enableKeyList) {
        this.enable = YamlUtil.equalsTrue(ConfigUtil.getConfig(), enableKeyList);
    }

    public AbstractListener() {
        this(new ArrayList<>());
    }
}

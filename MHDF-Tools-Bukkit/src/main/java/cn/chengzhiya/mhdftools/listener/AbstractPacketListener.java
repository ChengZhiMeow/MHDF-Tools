package cn.chengzhiya.mhdftools.listener;

import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.YamlUtil;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractPacketListener implements PacketListener {
    private final boolean enable;
    private final PacketListenerPriority priority;

    public AbstractPacketListener(List<String> enableKeyList, @NotNull PacketListenerPriority priority) {
        this.enable = YamlUtil.equalsTrue(ConfigUtil.getConfig(), enableKeyList);
        this.priority = priority;
    }

    public AbstractPacketListener(@NotNull PacketListenerPriority priority) {
        this(new ArrayList<>(), priority);
    }
}

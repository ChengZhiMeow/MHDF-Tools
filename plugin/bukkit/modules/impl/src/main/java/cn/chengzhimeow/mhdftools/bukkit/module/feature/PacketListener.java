package cn.chengzhimeow.mhdftools.bukkit.module.feature;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.config.ConfigUtil;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class PacketListener implements com.github.retrooper.packetevents.event.PacketListener {
    private final Module module;
    private final boolean enable;
    private final PacketListenerPriority priority;

    public PacketListener(@NotNull Module module, @NotNull List<String> enableKeyList, @NotNull PacketListenerPriority priority) {
        this.module = module;
        this.enable = ConfigUtil.equalsTrue(module.getConfig().getData(), enableKeyList);
        this.priority = priority;
    }

    public PacketListener(@NotNull Module module, @NotNull PacketListenerPriority priority) {
        this(module, new ArrayList<>(), priority);
    }
}

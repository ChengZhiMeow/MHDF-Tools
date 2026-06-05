package cn.chengzhimeow.mhdftools.bukkit.module.feature;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class PacketListener implements com.github.retrooper.packetevents.event.PacketListener {
    private final Module module;
    private final boolean enable;
    private final PacketListenerPriority priority;

    public PacketListener(@NotNull Module module, boolean enable, @NotNull PacketListenerPriority priority) {
        this.module = module;
        this.enable = enable;
        this.priority = priority;
    }

    public PacketListener(@NotNull Module module, @NotNull PacketListenerPriority priority) {
        this(module, true, priority);
    }
}

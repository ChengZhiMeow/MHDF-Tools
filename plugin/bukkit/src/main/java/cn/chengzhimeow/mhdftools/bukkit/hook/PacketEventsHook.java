package cn.chengzhimeow.mhdftools.bukkit.hook;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.packetevents.PacketEventsManager;

public final class PacketEventsHook extends Hook {
    @Override
    public void hook() {
        if (this.isEnable()) return;

        PacketEventsManager.getInstance().hook(Main.instance);
        this.setEnable(true);
    }

    @Override
    public void unhook() {
        if (!this.isEnable()) return;

        PacketEventsManager.getInstance().unhook();
        this.setEnable(false);
    }
}

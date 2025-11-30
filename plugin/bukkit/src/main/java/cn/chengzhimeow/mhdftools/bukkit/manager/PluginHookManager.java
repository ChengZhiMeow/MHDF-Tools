package cn.chengzhimeow.mhdftools.bukkit.manager;

import cn.chengzhimeow.mhdftools.bukkit.hook.PacketEventsHook;
import cn.chengzhimeow.mhdftools.bukkit.hook.VaultHook;
import cn.chengzhimeow.mhdftools.bukkit.util.PluginUtil;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public final class PluginHookManager {
    private final PacketEventsHook packetEventsHook = new PacketEventsHook();
    private final VaultHook vaultHook = new VaultHook();

    /**
     * 初始化所有对接的API
     */
    public void hook() {
        this.packetEventsHook.hook();

        if (PluginUtil.hasPlugin("Vault"))
            this.vaultHook.hook();
    }

    /**
     * 卸载所有对接的API
     */
    public void unhook() {
        this.packetEventsHook.unhook();

        this.vaultHook.unhook();
    }
}

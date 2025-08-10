package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.hook.*;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public final class PluginHookManager {
    private final PacketEventsHook packetEventsHook = new PacketEventsHook();
    private final PlaceholderApiHook placeholderAPIHook = new PlaceholderApiHook();
    private final CraftEngineHook craftEngineHook = new CraftEngineHook();
    private final MythicMobsHook mythicMobsHook = new MythicMobsHook();
    private final VaultHook vaultHook = new VaultHook();

    /**
     * 初始化所有对接的API
     */
    public void hook() {
        this.packetEventsHook.hook();

        this.placeholderAPIHook.hook();
        this.craftEngineHook.hook();
        this.mythicMobsHook.hook();
        this.vaultHook.hook();
    }

    /**
     * 卸载所有对接的API
     */
    public void unhook() {
        this.packetEventsHook.unhook();

        this.placeholderAPIHook.unhook();
        this.craftEngineHook.unhook();
        this.mythicMobsHook.unhook();
        this.vaultHook.unhook();
    }
}

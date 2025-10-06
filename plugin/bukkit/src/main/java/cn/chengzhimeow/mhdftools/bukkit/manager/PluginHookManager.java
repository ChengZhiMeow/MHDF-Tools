package cn.chengzhimeow.mhdftools.bukkit.manager;

import cn.chengzhimeow.mhdftools.bukkit.compatibility.item.Compatibility;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.item.CompatibilityRegistry;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.item.CraftEngineImpl;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.item.MythicMobsImpl;
import cn.chengzhimeow.mhdftools.bukkit.hook.PacketEventsHook;
import cn.chengzhimeow.mhdftools.bukkit.hook.PlaceholderApiHook;
import cn.chengzhimeow.mhdftools.bukkit.hook.VaultHook;
import cn.chengzhimeow.mhdftools.bukkit.util.PluginUtil;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public final class PluginHookManager {
    private final PacketEventsHook packetEventsHook = new PacketEventsHook();
    private final CompatibilityRegistry itemCompatibility = new CompatibilityRegistry();
    private final PlaceholderApiHook placeholderAPIHook = new PlaceholderApiHook();
    private final VaultHook vaultHook = new VaultHook();

    /**
     * 初始化所有对接的API
     */
    public void hook() {
        this.packetEventsHook.hook();

        if (PluginUtil.hasPlugin("CraftEngine"))
            this.itemCompatibility.register(Compatibility.Ids.CRAFT_ENGINE, new CraftEngineImpl());
        if (PluginUtil.hasPlugin("MythicMobs"))
            this.itemCompatibility.register(Compatibility.Ids.MYTHIC_MOBS, new MythicMobsImpl());

        if (PluginUtil.hasPlugin("PlaceholderAPI"))
            this.placeholderAPIHook.hook();
        if (PluginUtil.hasPlugin("Vault"))
            this.vaultHook.hook();
    }

    /**
     * 卸载所有对接的API
     */
    public void unhook() {
        this.packetEventsHook.unhook();

        this.placeholderAPIHook.unhook();
        this.vaultHook.unhook();
    }
}

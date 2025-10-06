package cn.chengzhimeow.mhdftools.bukkit.hook;

import cn.chengzhimeow.mhdftools.bukkit.hook.impl.VaultImpl;
import lombok.Getter;

@Getter
public final class VaultHook extends Hook {
    private VaultImpl api;

    /**
     * 初始化Vault的API
     */
    @Override
    public void hook() {
        this.api = new VaultImpl();
        super.enable = true;
    }

    /**
     * 卸载Vault的API
     */
    @Override
    public void unhook() {
        if (!super.enable) return;
        if (this.api != null) this.getApi().unhook();
        super.enable = false;
    }
}

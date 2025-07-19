package cn.chengzhiya.mhdftools.hook.impl;

import cn.chengzhiya.mhdftools.Main;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

@Getter
public final class VaultImpl {
    private boolean enableEconomy;
    private EconomyImpl economyImpl;
    private Economy economy;

    public VaultImpl() {
        this.enableEconomy = Main.instance.getConfigManager().getConfigManager().getData().getBoolean("economySettings.enable");
        if (this.isEnableEconomy()) {
            this.economyImpl = new EconomyImpl();
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            this.economy = rsp.getProvider();
        }
    }

    public void unhook() {
        if (this.isEnableEconomy()) {
            this.economyImpl.unhook();
        }
        this.enableEconomy = false;
        this.economyImpl = null;
        this.economy = null;
    }
}

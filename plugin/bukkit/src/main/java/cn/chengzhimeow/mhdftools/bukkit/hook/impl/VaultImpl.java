package cn.chengzhimeow.mhdftools.bukkit.hook.impl;

import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
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
        this.enableEconomy = ConfigSetting.getInstance().getData().getBoolean("economySettings.enable");
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

package cn.chengzhimeow.mhdftools.bukkit.module.economy;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.vault.VaultEconomyProvider;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;
    private VaultEconomyProvider vaultEconomyProvider;

    public ModuleMain() {
        super("economy");
        ModuleMain.instance = this;
    }

    @Override
    public void onLoad() {
        ConfigSetting.getInstance().saveDefaultFile();
        ConfigSetting.getInstance().update();
        ConfigSetting.getInstance().reload();

        LangSetting.getInstance().saveDefaultFile();
        LangSetting.getInstance().update();
        LangSetting.getInstance().reload();
    }

    @Override
    public boolean isEnable() {
        return ConfigSetting.getInstance().getConfig().enable();
    }

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) return;

        this.vaultEconomyProvider = new VaultEconomyProvider(this);
    }

    @Override
    public void onDisable() {
        if (this.vaultEconomyProvider == null) return;

        this.vaultEconomyProvider.unregister();
        this.vaultEconomyProvider = null;
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}

package cn.chengzhimeow.mhdftools.bukkit.hook.impl;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.math.BigDecimalUtil;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

import java.util.List;

public final class EconomyImpl extends AbstractEconomy {
    public EconomyImpl() {
        Bukkit.getServicesManager().register(Economy.class, this, Main.instance, ServicePriority.Normal);
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, this);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "MHDF-Tools";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return String.format("%.2f", amount);
    }

    @Override
    public String currencyNamePlural() {
        return this.currencyNameSingular();
    }

    public String currencyNameSingular() {
        return MHDFToolsAPI.getInstance().getEconomyDataManager().getMoneyName();
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        return mhdfPlayer.hasEconomyData();
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String world) {
        return this.hasAccount(player);
    }

    @Override
    public boolean hasAccount(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return this.hasAccount(player);
    }

    @Override
    public boolean hasAccount(String name, String world) {
        return this.hasAccount(name);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        return mhdfPlayer.getMoney().doubleValue();
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return this.getBalance(player);
    }

    @Override
    public double getBalance(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return this.getBalance(player);
    }

    @Override
    public double getBalance(String name, String world) {
        return this.getBalance(name);
    }

    @Override
    public boolean has(String name, double amount) {
        return this.getBalance(name) >= amount;
    }

    @Override
    public boolean has(String name, String world, double amount) {
        return this.has(name, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        mhdfPlayer.takeMoney(BigDecimalUtil.toBigDecimal(amount));

        return new EconomyResponse(amount, this.getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
        return this.withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return this.withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, String world, double amount) {
        return this.withdrawPlayer(name, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        double tax = 0;
        if (ConfigSetting.getInstance().getData().getBoolean("economySettings.personalIncomeTax.enable")) {
            tax = amount * ConfigSetting.getInstance().getData().getDouble("economySettings.personalIncomeTax.rate");
            player.getPlayer().sendMessage(LangSetting.getInstance().i18n("economy.tax")
                    .replace("{amount}", String.valueOf(tax))
            );
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        mhdfPlayer.addMoney(BigDecimalUtil.toBigDecimal(amount - tax));

        return new EconomyResponse(amount, this.getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
        return this.depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String name, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return this.depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String name, String world, double amount) {
        return this.depositPlayer(name, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String world) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse isBankOwner(String name, String world) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse isBankMember(String name, String world) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        if (mhdfPlayer.hasEconomyData()) {
            return false;
        }

        mhdfPlayer.setMoney(mhdfPlayer.getMoney());
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return this.createPlayerAccount(player);
    }

    @Override
    public boolean createPlayerAccount(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return this.createPlayerAccount(player);
    }

    @Override
    public boolean createPlayerAccount(String name, String world) {
        return this.createPlayerAccount(name);
    }
}

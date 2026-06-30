package cn.chengzhimeow.mhdftools.bukkit.module.economy.vault;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.manager.feature.EconomyDataManager;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.config.LangSetting;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public final class VaultEconomyProvider extends AbstractEconomy {
    private final ModuleMain module;

    public VaultEconomyProvider(ModuleMain module) {
        this.module = module;
        Bukkit.getServicesManager().register(Economy.class, this, module.getPlugin(), ServicePriority.Normal);
    }

    public void unregister() {
        Bukkit.getServicesManager().unregister(Economy.class, this);
    }

    @Override
    public boolean isEnabled() {
        return this.module.isEnable();
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

    @Override
    public String currencyNameSingular() {
        EconomyDataManager manager = MHDFToolsAPI.getInstance().getEconomyDataManager();
        if (!manager.isEnable()) return "金币";
        return manager.getMoneyName();
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return this.player(player).hasEconomyData();
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return this.hasAccount(player);
    }

    @Override
    public boolean hasAccount(String playerName) {
        return this.player(playerName).hasEconomyData();
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return this.hasAccount(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return this.player(player).getMoney().doubleValue();
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return this.getBalance(player);
    }

    @Override
    public double getBalance(String playerName) {
        return this.player(playerName).getMoney().doubleValue();
    }

    @Override
    public double getBalance(String playerName, String world) {
        return this.getBalance(playerName);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return this.getBalance(player) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return this.has(player, amount);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return this.getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return this.has(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return this.withdraw(this.player(player), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return this.withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return this.withdraw(this.player(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return this.withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return this.deposit(this.player(player), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return this.depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return this.deposit(this.player(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return this.depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return this.bankFailure();
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return this.bankFailure();
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return this.bankFailure();
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return this.bankFailure();
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return this.bankFailure();
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return this.bankFailure();
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return this.bankFailure();
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return this.bankFailure();
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return this.bankFailure();
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return this.bankFailure();
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return this.bankFailure();
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        MHDFToolsPlayer mhdfPlayer = this.player(player);
        if (mhdfPlayer.hasEconomyData()) return false;

        mhdfPlayer.setMoney(mhdfPlayer.getMoney());
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return this.createPlayerAccount(player);
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        MHDFToolsPlayer mhdfPlayer = this.player(playerName);
        if (mhdfPlayer.hasEconomyData()) return false;

        mhdfPlayer.setMoney(mhdfPlayer.getMoney());
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return this.createPlayerAccount(playerName);
    }

    private EconomyResponse withdraw(MHDFToolsPlayer player, double amount) {
        BigDecimal money = this.money(amount);
        if (money == null) {
            return new EconomyResponse(amount, player.getMoney().doubleValue(), EconomyResponse.ResponseType.FAILURE, "Amount cannot be negative");
        }
        if (player.getMoney().compareTo(money) < 0) {
            return new EconomyResponse(amount, player.getMoney().doubleValue(), EconomyResponse.ResponseType.FAILURE, "Not enough money");
        }

        player.takeMoney(money);
        return new EconomyResponse(amount, player.getMoney().doubleValue(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    private EconomyResponse deposit(MHDFToolsPlayer player, double amount) {
        BigDecimal money = this.money(amount);
        if (money == null) {
            return new EconomyResponse(amount, player.getMoney().doubleValue(), EconomyResponse.ResponseType.FAILURE, "Amount cannot be negative");
        }

        BigDecimal tax = BigDecimal.ZERO;
        if (ConfigSetting.getInstance().getConfig().personalIncomeTax().enable()) {
            tax = money.multiply(ConfigSetting.getInstance().getConfig().personalIncomeTax().rate()).setScale(2, RoundingMode.HALF_UP);
        }

        player.addMoney(money.subtract(tax));
        if (tax.signum() > 0) {
            player.sendMessage(LangSetting.getInstance().getConfig().economy().tax()
                    .replace("{amount}", tax.toPlainString())
                    .replace("{money_name}", ConfigSetting.getInstance().getConfig().moneyName()));
        }
        return new EconomyResponse(amount, player.getMoney().doubleValue(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    private BigDecimal money(double amount) {
        if (amount < 0 || Double.isNaN(amount) || Double.isInfinite(amount)) return null;

        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
    }

    private EconomyResponse bankFailure() {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    private MHDFToolsPlayer player(OfflinePlayer player) {
        return MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
    }

    private MHDFToolsPlayer player(String name) {
        return MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(name);
    }
}

package cn.chengzhimeow.mhdftools.bukkit.module.economy.config;

import cn.chengzhimeow.mhdftools.bukkit.api.economy.EconomyConfigManager;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

public final class ConfigSetting extends AbstractYamlSetting<ConfigSetting.Config> {
    @Getter(lazy = true)
    private static final ConfigSetting instance = new ConfigSetting();
    @Getter private Config config;

    private ConfigSetting() {
    }

    @Override
    public String originFilePath() {
        return "module/" + ModuleMain.instance.getId() + "/config.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();

        this.config = new Config(
                super.getData().getBoolean("enable"),
                BigDecimal.valueOf(super.getData().getDouble("default")),
                super.getData().getString("name", "金币"),
                new Config.PersonalIncomeTax(
                        super.getData().getBoolean("personalIncomeTax.enable"),
                        BigDecimal.valueOf(super.getData().getDouble("personalIncomeTax.rate"))
                ),
                super.getData().getStringList("moneyadminCommands"),
                super.getData().getStringList("moneyCommands"),
                super.getData().getStringList("payCommands")
        );

        EconomyConfigManager.getInstance().setMoneyName(super.config.moneyName);
        EconomyConfigManager.getInstance().setDefaultMoney(this.config.defaultMoney);
    }

    public record Config(
            boolean enable,
            BigDecimal defaultMoney,
            String moneyName,
            PersonalIncomeTax personalIncomeTax,
            List<String> moneyadminCommands,
            List<String> moneyCommands,
            List<String> payCommands
    ) {
        public record PersonalIncomeTax(
                boolean enable,
                BigDecimal rate
        ) {
        }
    }
}

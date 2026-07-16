package cn.chengzhimeow.mhdftools.bukkit.api.economy;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public final class EconomyConfigManager {
    @Getter(lazy = true)
    private static final EconomyConfigManager instance = new EconomyConfigManager();

    private String moneyName = "金币";
    private BigDecimal defaultMoney = BigDecimal.ZERO;
}

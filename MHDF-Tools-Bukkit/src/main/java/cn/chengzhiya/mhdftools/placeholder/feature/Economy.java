package cn.chengzhiya.mhdftools.placeholder.feature;

import cn.chengzhiya.mhdftools.placeholder.AbstractPlaceholder;
import cn.chengzhiya.mhdftools.util.feature.EconomyUtil;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public final class Economy extends AbstractPlaceholder {
    public Economy() {
        super(
                List.of("economySettings.enable")
        );
    }

    @Override
    public String placeholder(OfflinePlayer player, @NotNull String prams) {
        if (player == null) {
            return null;
        }
        if (prams.equals("money_amount")) {
            return EconomyUtil.getMoney(player).toString();
        }
        if (prams.equals("money_name")) {
            return EconomyUtil.getMoneyName();
        }

        return null;
    }
}

package cn.chengzhimeow.mhdftools.bukkit.placeholder.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.placeholder.Placeholder;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
final class Economy extends Placeholder {
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
            MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
            return mhdfPlayer.toString();
        }
        if (prams.equals("money_name")) {
            return MHDFToolsAPI.getInstance().getEconomyDataManager().getMoneyName();
        }

        return null;
    }
}

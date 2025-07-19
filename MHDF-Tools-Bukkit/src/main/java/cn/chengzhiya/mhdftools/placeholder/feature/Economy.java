package cn.chengzhiya.mhdftools.placeholder.feature;

import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.placeholder.Placeholder;
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
            MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
            return mhdfPlayer.toString();
        }
        if (prams.equals("money_name")) {
            return MHDFToolsAPIHelper.getInstance().getEconomyDataManager().getMoneyName();
        }

        return null;
    }
}

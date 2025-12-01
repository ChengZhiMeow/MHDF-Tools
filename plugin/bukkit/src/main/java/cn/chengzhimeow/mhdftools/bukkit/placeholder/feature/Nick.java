package cn.chengzhimeow.mhdftools.bukkit.placeholder.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.bukkit.placeholder.Placeholder;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
final class Nick extends Placeholder {
    public Nick() {
        super(
                List.of("nickSettings.enable")
        );
    }

    @Override
    public String placeholder(OfflinePlayer player, @NotNull String prams) {
        if (player == null) {
            return null;
        }
        if (prams.equals("nick_name")) {
            return MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player).getDisplayName();
        }

        return null;
    }
}

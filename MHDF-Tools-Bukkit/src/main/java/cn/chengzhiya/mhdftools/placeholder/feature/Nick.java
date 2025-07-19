package cn.chengzhiya.mhdftools.placeholder.feature;

import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.placeholder.Placeholder;
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
            return MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName();
        }

        return null;
    }
}

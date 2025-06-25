package cn.chengzhiya.mhdftools.placeholder.feature;

import cn.chengzhiya.mhdftools.placeholder.AbstractPlaceholder;
import cn.chengzhiya.mhdftools.util.feature.NickUtil;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public final class Nick extends AbstractPlaceholder {
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
            return NickUtil.getName(player);
        }

        return null;
    }
}

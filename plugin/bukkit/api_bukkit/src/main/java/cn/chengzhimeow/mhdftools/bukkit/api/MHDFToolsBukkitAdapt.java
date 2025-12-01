package cn.chengzhimeow.mhdftools.bukkit.api;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import org.bukkit.entity.Player;

public final class MHDFToolsBukkitAdapt {
    public static MHDFToolsPlayer adapt(Player player) {
        return MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
    }
}

package cn.chengzhimeow.mhdftools.bukkit.api.message;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.common.message.Messager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class PlayerMessager {
    public static void send(MHDFToolsPlayer target, Component message) {
        Player player = Bukkit.getPlayer(target.getUuid());
        if (player != null) {
            player.sendMessage(message);
            return;
        }

        Messager.publish(new PlayerMessage(
                BungeeCordManager.getInstance().getServerName(),
                target.getName(),
                MiniMessage.miniMessage().serialize(message)
        ));
    }

    private PlayerMessager() {
    }
}

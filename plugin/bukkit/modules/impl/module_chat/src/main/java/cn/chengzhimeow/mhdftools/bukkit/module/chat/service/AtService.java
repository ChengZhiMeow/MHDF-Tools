package cn.chengzhimeow.mhdftools.bukkit.module.chat.service;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.VanishStatus;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.LangSetting;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class AtService {
    public static final String AT_ALL = "*";

    public static Set<String> getAtList(Player player, String message) {
        Set<String> playerList = new HashSet<>();
        ConfigSetting.Config.At config = ConfigSetting.getInstance().getConfig().at();
        if (!config.enable()) return playerList;

        List<String> onlinePlayerList = new ArrayList<>(BungeeCordManager.getInstance().getPlayerList());
        for (VanishStatus vanishStatus : MHDFToolsAPI.getInstance().getVanishStatusManager().getList()) {
            if (!vanishStatus.isEnable()) continue;
            MHDFToolsPlayer vanishPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(vanishStatus.getPlayer());
            onlinePlayerList.remove(vanishPlayer.getName());
        }

        for (String playerName : onlinePlayerList) {
            if (message.contains(playerName)) {
                playerList.add(playerName);
            }
        }

        if (player.hasPermission("mhdftools.chat.at.all")) {
            for (String allMessage : config.allMessage()) {
                if (message.contains(allMessage)) {
                    playerList.add(AT_ALL);
                    break;
                }
            }
        }
        return playerList;
    }

    public static void at(Player player, String by, List<String> atList) {
        if (player == null) return;
        if (!atList.contains(AT_ALL) && !atList.contains(player.getName())) return;

        MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
        OfflinePlayer byPlayer = Bukkit.getOfflinePlayer(by);
        MHDFToolsPlayer sender = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(byPlayer.getUniqueId(), by);
        if (target.isIgnore(sender)) return;

        LangSetting.Config.Chat.At.AtTitle title = LangSetting.getInstance().getConfig().chat().at().title();

        player.showTitle(Title.title(
                title.title().replace("{by}", by),
                title.subtitle().replace("{by}", by),
                Title.Times.times(
                        Duration.ofMillis(title.in() * 50L),
                        Duration.ofMillis(title.step() * 50L),
                        Duration.ofMillis(title.out() * 50L)
                )
        ));
    }

    private AtService() {
    }
}

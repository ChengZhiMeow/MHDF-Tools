package cn.chengzhimeow.mhdftools.bukkit.module.chat.service;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.VanishStatus;
import cn.chengzhimeow.mhdftools.api.manager.feature.VanishStatusManager;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.LangSetting;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class AtService {
    public static final String AT_ALL = "*";

    public static Set<String> getAtList(Player player, String message) {
        Set<String> playerList = new HashSet<>();
        ConfigSetting.Config.At config = ConfigSetting.getInstance().getConfig().at();
        if (!config.enable()) return playerList;

        List<String> onlinePlayerList = new ArrayList<>(BungeeCordManager.getInstance().getPlayerList());
        VanishStatusManager vanishStatusManager = MHDFToolsAPI.getInstance().getVanishStatusManager();
        if (vanishStatusManager.isEnable()) {
            for (VanishStatus vanishStatus : vanishStatusManager.getList()) {
                if (!vanishStatus.isEnable()) continue;
                MHDFToolsPlayer vanishPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(vanishStatus.getPlayer());
                onlinePlayerList.remove(vanishPlayer.getName());
            }
        }

        for (String playerName : onlinePlayerList) {
            if (playerName == null || playerName.isBlank()) continue;
            if (matchesAt(message, playerName, config.patternFormat())) {
                playerList.add(playerName);
            }
        }

        if (player.hasPermission("mhdftools.chat.at.all")) {
            for (String allMessage : config.allMessage()) {
                if (allMessage == null || allMessage.isBlank()) continue;
                if (matchesAt(message, allMessage, config.patternFormat())) {
                    playerList.add(AT_ALL);
                    break;
                }
            }
        }
        return playerList;
    }

    public static String getAtTargetName(String target) {
        if (target == null) return "";
        return target.equals(AT_ALL)
                ? PlainTextComponentSerializer.plainText().serialize(LangSetting.getInstance().getConfig().chat().at().all())
                : target;
    }

    public static Pattern getAtPattern(String target) {
        if (target == null || target.isBlank()) return Pattern.compile("a^");
        ConfigSetting.Config.At config = ConfigSetting.getInstance().getConfig().at();
        String at = target.equals(AT_ALL)
                ? config.allMessage().stream()
                .filter(message -> message != null && !message.isBlank())
                .map(Pattern::quote)
                .reduce((first, second) -> first + "|" + second)
                .map(pattern -> "(?:" + pattern + ")")
                .orElse("a^")
                : Pattern.quote(target);
        return Pattern.compile(config.patternFormat().replace("{at}", at));
    }

    private static boolean matchesAt(String message, String target, String patternFormat) {
        if (target == null || target.isBlank()) return false;
        return Pattern.compile(patternFormat.replace("{at}", Pattern.quote(target))).matcher(message).find();
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

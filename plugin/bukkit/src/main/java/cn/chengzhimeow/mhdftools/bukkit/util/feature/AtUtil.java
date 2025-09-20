package cn.chengzhimeow.mhdftools.bukkit.util.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.VanishStatus;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.SoundSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class AtUtil {
    @Getter
    private static final String atAll = "all❤";

    /**
     * 获取文本中所有AT的目标列表
     *
     * @param message 文本
     * @return AT列表
     */
    public static Set<String> getAtList(Player player, String message) {
        ConfigurationSection config = ConfigSetting.getSettingInstance().getData().getConfigurationSection("chatSettings.at");
        if (config == null) return new HashSet<>();

        Set<String> playerList = new HashSet<>();

        // 禁止AT隐身玩家
        List<String> onlinePlayerList = Main.instance.getBungeeCordManager().getPlayerList();
        for (VanishStatus vanishStatus : MHDFToolsAPIHelper.getInstance().getVanishStatusManager().getList()) {
            if (!vanishStatus.isEnable()) continue;

            MHDFToolsPlayer mhdfVanishPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(vanishStatus.getPlayer());
            onlinePlayerList.remove(mhdfVanishPlayer.getName());
        }

        for (String playerName : onlinePlayerList) {
            if (!message.contains(playerName)) continue;

            playerList.add(playerName);
        }

        if (player.hasPermission("mhdftools.chat.at.all")) {
            for (String allMessage : config.getStringList("allMessage")) {
                if (!message.contains(allMessage)) continue;

                playerList.add(AtUtil.atAll);
                break;
            }
        }

        return playerList;
    }

    /**
     * at玩家
     *
     * @param player 玩家实例
     */
    public static void at(Player player, String by) {
        if (player == null) return;
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);

        // 屏蔽黑名单列表中的AT
        OfflinePlayer byPlayer = Bukkit.getOfflinePlayer(by);
        MHDFToolsPlayer mhdfByPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(byPlayer);
        if (mhdfPlayer.isIgnore(mhdfByPlayer)) return;

        // 发送AT提示
        String title = LangSetting.getSettingInstance().getString("chat.at.title")
                .replace("{by}", by);
        if (!title.isEmpty()) {
            String[] args = title.split("\\|");
            ActionUtil.sendTitle(player, args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        }

        // 播放音效
        String sound = SoundSetting.getSettingInstance().getData().getString("chat.at");
        if (sound != null && !sound.isEmpty()) {
            String[] args = sound.split("\\|");
            ActionUtil.playSound(player, args[0], Float.parseFloat(args[1]), Float.parseFloat(args[2]));
        }
    }

    /**
     * at玩家列表
     *
     * @param atList 玩家列表
     */
    public static void atList(Set<String> atList, String by) {
        for (String at : atList) {
            if (at.equals(AtUtil.atAll)) {
                Bukkit.getOnlinePlayers().forEach(p -> AtUtil.at(p, by));
                continue;
            }
            AtUtil.at(Bukkit.getPlayer(at), by);
        }
    }
}

package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.VanishStatus;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.config.SoundUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
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
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("chatSettings.at");
        if (config == null) {
            return new HashSet<>();
        }

        Set<String> playerList = new HashSet<>();

        // 禁止AT隐身玩家
        List<String> onlinePlayerList = Main.instance.getBungeeCordManager().getPlayerList();
        for (VanishStatus vanishStatus : MHDFToolsAPIHelper.getInstance().getVanishStatusManager().getList()) {
            if (!vanishStatus.isEnable()) {
                continue;
            }

            MHDFToolsPlayer mhdfVanishPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(vanishStatus.getPlayer());
            onlinePlayerList.remove(mhdfVanishPlayer.getName());
        }

        for (String playerName : onlinePlayerList) {
            if (!message.contains(playerName)) {
                continue;
            }

            playerList.add(playerName);
        }

        if (player.hasPermission("mhdftools.chat.at.all")) {
            for (String allMessage : config.getStringList("allMessage")) {
                if (!message.contains(allMessage)) {
                    continue;
                }

                playerList.add(atAll);
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
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (player == null) {
            return;
        }

        // 屏蔽黑名单列表中的AT
        OfflinePlayer byPlayer = Bukkit.getOfflinePlayer(by);
        MHDFToolsPlayer mhdfByPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(byPlayer);
        if (mhdfPlayer.isIgnore(mhdfByPlayer)) {
            return;
        }

        // 发送AT提示
        String title = LangUtil.getString("chat.at.title")
                .replace("{by}", by);
        if (!title.isEmpty()) {
            String[] args = title.split("\\|");
            ActionUtil.sendTitle(player, args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        }

        // 播放音效
        String sound = SoundUtil.getSound("chat.at");
        if (!sound.isEmpty()) {
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
            if (at.equals(atAll)) {
                Bukkit.getOnlinePlayers().forEach(p -> at(p, by));
                continue;
            }
            at(Bukkit.getPlayer(at), by);
        }
    }
}

package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.entity.database.data.NickData;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.database.NickDataUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class NickUtil {
    /**
     * 获取指定玩家UUID的匿名昵称
     *
     * @param uuid 玩家UUID
     * @return 匿名昵称
     */
    public static String getNickName(UUID uuid) {
        NickData nickData = NickDataUtil.getNickData(uuid);

        return nickData.getNick();
    }

    /**
     * 获取指定玩家实例的匿名昵称
     *
     * @param player 玩家实例
     * @return 匿名昵称
     */
    public static String getNickName(OfflinePlayer player) {
        return getNickName(player.getUniqueId());
    }

    /**
     * 获取指定玩家实例的游戏昵称
     *
     * @param player 玩家实例
     * @return 游戏昵称
     */
    public static String getName(OfflinePlayer player) {
        // 不处理功能未开启的情况
        if (!ConfigUtil.getConfig().getBoolean("nickSettings.enable")) {
            return player.getName();
        }

        String nickName = getNickName(player);
        if (nickName == null) {
            return player.getName();
        }
        return nickName;
    }

    /**
     * 设置指定玩家实例的匿名昵称
     *
     * @param player 玩家实例
     * @param name   匿名昵称
     */
    public static void setNickName(Player player, String name) {
        setNickDisplay(player, ColorUtil.color(name), true);

        NickData nickData = NickDataUtil.getNickData(player);
        nickData.setNick(name);
        NickDataUtil.updateNickData(nickData);
    }

    /**
     * 重置指定玩家实例的匿名昵称
     *
     * @param player 玩家实例
     */
    public static void resetNickName(Player player) {
        setNickDisplay(player, Component.text(player.getName()), false);

        NickDataUtil.removeNickData(player);
    }

    /**
     * 设置指定玩家实例的匿名昵称显示
     *
     * @param player 玩家实例
     * @param name   匿名昵称
     * @param show   是否可视
     */
    public static void setNickDisplay(Player player, TextComponent name, boolean show) {
        player.displayName(name);
        player.customName(name);
        player.playerListName(name);
        player.setCustomNameVisible(show);
    }
}

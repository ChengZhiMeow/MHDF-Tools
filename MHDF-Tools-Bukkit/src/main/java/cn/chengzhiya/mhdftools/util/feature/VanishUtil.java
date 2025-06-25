package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.database.data.VanishStatus;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.VanishStatusUtil;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public final class VanishUtil {
    /**
     * 给指定目标实例发送切换隐身的提示
     *
     * @param sender 接收信息的目标实例
     * @param player 开启隐身的玩家实例
     * @param enable 是否开启隐身
     */
    public static void sendChangeVanishMessage(CommandSender sender, Player player, boolean enable) {
        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.vanish.message")
                .replace("{player}", NickUtil.getName(player))
                .replace("{change}",
                        enable ? LangUtil.i18n("enable") : LangUtil.i18n("disable")
                )
        );
    }

    /**
     * 开启指定玩家实例的隐身模式
     *
     * @param player 玩家实例
     */
    public static void enableVanish(Player player) {
        VanishStatus vanishStatus = VanishStatusUtil.getVanishStatus(player);
        vanishStatus.setEnable(true);
        VanishStatusUtil.updateVanishStatus(vanishStatus);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (Main.instance.getPluginHookManager().getPacketEventsHook().getServerVersion()
                    .isNewerThanOrEquals(ServerVersion.V_1_12_2)
            ) {
                MHDFScheduler.getGlobalRegionScheduler().runTask(Main.instance, () ->
                        onlinePlayer.hidePlayer(Main.instance, player));
            } else {
                MHDFScheduler.getGlobalRegionScheduler().runTask(Main.instance, () ->
                        onlinePlayer.hidePlayer(player));
            }
        }
    }

    /**
     * 关闭指定玩家实例的隐身模式
     *
     * @param player 玩家实例
     */
    public static void disableVanish(Player player) {
        VanishStatus vanishStatus = VanishStatusUtil.getVanishStatus(player);
        vanishStatus.setEnable(false);
        VanishStatusUtil.updateVanishStatus(vanishStatus);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (Main.instance.getPluginHookManager().getPacketEventsHook().getServerVersion()
                    .isNewerThanOrEquals(ServerVersion.V_1_12_2)
            ) {
                MHDFScheduler.getGlobalRegionScheduler().runTask(Main.instance, () ->
                        onlinePlayer.showPlayer(Main.instance, player));
            } else {
                MHDFScheduler.getGlobalRegionScheduler().runTask(Main.instance, () ->
                        onlinePlayer.showPlayer(player));
            }
        }
    }
}

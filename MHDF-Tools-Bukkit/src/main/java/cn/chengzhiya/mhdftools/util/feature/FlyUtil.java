package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.entity.database.data.FlyStatus;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.FlyStatusUtil;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public final class FlyUtil {
    /**
     * 给指定目标实例发送切换飞行的提示
     *
     * @param sender 接收信息的目标实例
     * @param player 开启飞行的玩家实例
     * @param enable 是否开启飞行
     */
    public static void sendChangeFlyMessage(CommandSender sender, Player player, boolean enable) {
        ActionUtil.sendMessage(sender,
                LangUtil.i18n("commands.fly.message")
                        .replace("{player}", NickUtil.getName(player))
                        .replace("{change}",
                                enable ? LangUtil.i18n("enable") : LangUtil.i18n("disable")
                        )
        );
    }

    /**
     * 检测指定玩家的游戏模式是否可以飞行
     *
     * @param player 玩家实例
     */
    public static boolean isAllowedFlyingGameMode(Player player) {
        return player.getGameMode() == GameMode.CREATIVE ||
                player.getGameMode() == GameMode.SPECTATOR;
    }

    /**
     * 开启指定玩家实例的飞行模式
     *
     * @param player 玩家实例
     */
    public static void enableFly(Player player) {
        FlyStatus flyStatus = FlyStatusUtil.getFlyStatus(player);
        flyStatus.setEnable(true);
        FlyStatusUtil.updateFlyStatus(flyStatus);

        player.setAllowFlight(true);
    }

    /**
     * 关闭指定玩家实例的飞行模式
     *
     * @param player 玩家实例
     */
    public static void disableFly(Player player) {
        FlyStatus flyStatus = FlyStatusUtil.getFlyStatus(player);
        flyStatus.setEnable(false);
        FlyStatusUtil.updateFlyStatus(flyStatus);

        player.setAllowFlight(false);
    }
}

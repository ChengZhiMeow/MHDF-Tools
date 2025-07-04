package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class FakeChangeTimeUtil {
    /**
     * 给指定目标实例发送修改虚假时间的提示
     *
     * @param sender 接收信息的目标实例
     * @param player 切换时间的玩家实例
     * @param time   是否开启飞行
     */
    public static void sendFakeChangeTimeMessage(CommandSender sender, Player player, long time) {
        ActionUtil.sendMessage(sender,
                LangUtil.i18n("commands.fakeChangeTime.message")
                        .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                        .replace("{time}", String.valueOf(time))
        );
    }
}

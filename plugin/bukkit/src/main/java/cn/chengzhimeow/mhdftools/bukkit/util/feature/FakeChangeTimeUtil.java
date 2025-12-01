package cn.chengzhimeow.mhdftools.bukkit.util.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
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
        sender.sendMessage(
                LangSetting.getInstance().i18n("commands.fakeChangeTime.message")
                        .replace("{player}", MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                        .replace("{time}", String.valueOf(time))
        );
    }
}

package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.entity.Player;

public final class TpaUtil {
    /**
     * 使指定玩家实例给指定玩家ID的玩家发送tpa传送请求
     *
     * @param player     玩家实例
     * @param targetName 玩家ID
     */
    public static void sendTpaRequest(Player player, String targetName) {
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (!Main.instance.getBungeeCordManager().ifPlayerOnline(targetName)) {
            ActionUtil.sendMessage(player, LangUtil.i18n("playerOffline"));
            return;
        }

        if (targetName.equals(player.getName())) {
            ActionUtil.sendMessage(player, LangUtil.i18n("commands.tpa.sendSelf"));
            return;
        }

        String delay = Main.instance.getCacheManager().get("tpaDelay", player.getName());
        if (delay != null) {
            ActionUtil.sendMessage(player, LangUtil.i18n("commands.tpa.inDelay")
                    .replace("{delay}", delay)
            );
            return;
        }

        Main.instance.getCacheManager().put("tpaPlayer", player.getName(), targetName);
        Main.instance.getCacheManager().put("tpaDelay", player.getName(), String.valueOf(ConfigUtil.getConfig().getInt("tpaSettings.delay")));

        ActionUtil.sendMessage(player, LangUtil.i18n("commands.tpa.message")
                .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
        );

        MHDFToolsPlayer mhdfTargetPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(targetName);
        if (mhdfTargetPlayer.isIgnore(mhdfPlayer)) {
            return;
        }

        Main.instance.getBungeeCordManager().sendMessage(targetName, LangUtil.i18n("commands.tpa.requestMessage")
                .replaceByMiniMessage("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
        );
    }
}

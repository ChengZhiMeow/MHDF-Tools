package cn.chengzhimeow.mhdftools.bukkit.util.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import org.bukkit.entity.Player;

public final class TpaHereUtil {
    /**
     * 使指定玩家实例给指定玩家ID的玩家发送tpahere传送请求
     *
     * @param player     玩家实例
     * @param targetName 玩家ID
     */
    public static void sendTpaHereRequest(Player player, String targetName) {
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        if (!Main.instance.getBungeeCordManager().ifPlayerOnline(targetName)) {
            player.sendMessage(LangSetting.getInstance().i18n("playerOffline"));
            return;
        }

        if (targetName.equals(player.getName())) {
            player.sendMessage(LangSetting.getInstance().i18n("commands.tpahere.sendSelf"));
            return;
        }

        String delay = Main.instance.getCacheManager().get("tpahereDelay", player.getName());
        if (delay != null) {
            player.sendMessage(LangSetting.getInstance().i18n("commands.tpa.inDelay")
                    .replace("{delay}", delay)
            );
            return;
        }

        Main.instance.getCacheManager().put("tpaherePlayer", player.getName(), targetName);
        Main.instance.getCacheManager().put("tpahereDelay", player.getName(), String.valueOf(ConfigSetting.getInstance().getData().getInt("tpahereSettings.delay")));

        player.sendMessage(LangSetting.getInstance().i18n("commands.tpahere.message")
                .replace("{player}", targetName)
        );

        MHDFToolsPlayer mhdfTargetPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(targetName);
        if (mhdfTargetPlayer.isIgnore(mhdfPlayer)) return;

        Main.instance.getBungeeCordManager().sendMessage(targetName, LangSetting.getInstance().i18n("commands.tpahere.requestMessage")
                .replaceByMiniMessage("{player}", player.getName())
        );
    }
}

package cn.chengzhimeow.mhdftools.bukkit.util.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
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
            ActionUtil.sendMessage(player, LangSetting.getSettingInstance().i18n("playerOffline"));
            return;
        }

        if (targetName.equals(player.getName())) {
            ActionUtil.sendMessage(player, LangSetting.getSettingInstance().i18n("commands.tpa.sendSelf"));
            return;
        }

        String delay = Main.instance.getCacheManager().get("tpaDelay", player.getName());
        if (delay != null) {
            ActionUtil.sendMessage(player, LangSetting.getSettingInstance().i18n("commands.tpa.inDelay")
                    .replace("{delay}", delay)
            );
            return;
        }

        Main.instance.getCacheManager().put("tpaPlayer", player.getName(), targetName);
        Main.instance.getCacheManager().put("tpaDelay", player.getName(), String.valueOf(ConfigSetting.getSettingInstance().getData().getInt("tpaSettings.delay")));

        ActionUtil.sendMessage(player, LangSetting.getSettingInstance().i18n("commands.tpa.message")
                .replace("{player}", targetName)
        );

        MHDFToolsPlayer mhdfTargetPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(targetName);
        if (mhdfTargetPlayer.isIgnore(mhdfPlayer)) return;

        Main.instance.getBungeeCordManager().sendMessage(targetName, LangSetting.getSettingInstance().i18n("commands.tpa.requestMessage")
                .replaceByMiniMessage("{player}", player.getName())
        );
    }
}

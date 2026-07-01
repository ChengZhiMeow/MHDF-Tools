package cn.chengzhimeow.mhdftools.bukkit.module.tpa.service;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.entity.Player;

public final class TpaService {
    public static void sendRequest(Player sender, String targetName) {
        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        if (!BungeeCordManager.getInstance().ifPlayerOnline(targetName)) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
            return;
        }

        if (targetName.equals(sender.getName())) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().sendSelf());
            return;
        }

        Long delayEndTime = ModuleMain.instance.getDelayCache().get(sender.getName());
        if (delayEndTime != null) {
            long remaining = (delayEndTime - System.currentTimeMillis() + 999L) / 1000L;
            if (remaining > 0L) {
                sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().inDelay()
                        .replace("{delay}", String.valueOf(remaining)));
                return;
            }
        }

        int time = ConfigSetting.getInstance().getConfig().time();
        ModuleMain.instance.getRequestCache().put(sender.getName(), targetName, Math.max(time, 1L) + 1L);
        ModuleMain.instance.getTimeoutThread().schedule(sender.getName(), targetName);

        int delay = ConfigSetting.getInstance().getConfig().delay();
        if (delay > 0)
            ModuleMain.instance.getDelayCache().put(sender.getName(), System.currentTimeMillis() + delay * 1000L, (long) delay);

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().message()
                .replace("{player}", targetName));

        MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(targetName);
        if (target.isIgnore(player)) return;

        target.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().requestMessage()
                .replaceByMiniMessage("{player}", sender.getName()));
    }

    private TpaService() {
    }
}

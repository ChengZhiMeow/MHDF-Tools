package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.listener.AbstractListener;
import cn.chengzhimeow.mhdftools.bukkit.util.GroupUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.message.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

final class QuitMessage extends AbstractListener {
    public QuitMessage() {
        super(
                List.of("quitMessageSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ConfigurationSection config = ConfigSetting.getSettingInstance().getData().getConfigurationSection("quitMessageSettings");
        if (config == null) return;

        if (config.getBoolean("removeMessage")) {
            event.quitMessage(null);
            return;
        }

        String message = config.getString(GroupUtil.getGroup(player, config, "mhdftools.group.quitmessage.") + ".message");
        if (message == null) {
            event.quitMessage(null);
            return;
        }

        event.quitMessage(ColorUtil.color(Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, message))
                .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
        );
    }
}

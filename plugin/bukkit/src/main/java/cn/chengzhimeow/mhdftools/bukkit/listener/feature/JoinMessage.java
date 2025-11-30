package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibility;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibilityRegistry;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.listener.AbstractListener;
import cn.chengzhimeow.mhdftools.bukkit.util.GroupUtil;
import cn.chengzhimeow.mhdftools.bukkit.message.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

final class JoinMessage extends AbstractListener {
    public JoinMessage() {
        super(
                List.of("joinMessageSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ConfigurationSection config = ConfigSetting.getSettingInstance().getData().getConfigurationSection("joinMessageSettings");
        if (config == null) return;

        if (config.getBoolean("removeMessage")) {
            event.joinMessage(null);
            return;
        }

        String message = config.getString(GroupUtil.getGroup(player, config, "mhdftools.group.joinmessage.") + ".message");
        if (message == null) {
            event.joinMessage(null);
            return;
        }

        event.joinMessage(ColorUtil.color(PlaceholderCompatibilityRegistry.getInstance().parseString(PlaceholderCompatibility.PlaceholderCompatibilityIds.PLACEHOLDER_API, player, message))
                .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
        );
    }
}

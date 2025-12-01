package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.config.folder.CustomMenuManager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.CustomMenuUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

final class CustomMenu extends Listener {
    public CustomMenu() {
        super(
                List.of("customMenuSettings.enable")
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String[] args = event.getMessage().substring(1).split(" ");
        String command = args[0].replace("/", "");

        ConfigurationSection config = CustomMenuManager.getInstance().getCustomMenuByCommand(command);
        if (config == null) return;

        CustomMenuUtil.openCustomMenu(player, config);
        event.setCancelled(true);
    }
}

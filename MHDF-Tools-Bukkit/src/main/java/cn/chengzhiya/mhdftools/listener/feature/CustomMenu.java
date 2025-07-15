package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.feature.CustomMenuUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public final class CustomMenu extends AbstractListener {
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

        YamlConfiguration config = Main.instance.getConfigManager().getCustomMenuManager().getCustomMenuByCommand(command);
        CustomMenuUtil.openCustomMenu(player, config);
        event.setCancelled(true);
    }
}

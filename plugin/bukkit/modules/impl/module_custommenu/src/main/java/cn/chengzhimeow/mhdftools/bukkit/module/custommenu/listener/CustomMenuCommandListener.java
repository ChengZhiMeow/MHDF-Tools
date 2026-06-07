package cn.chengzhimeow.mhdftools.bukkit.module.custommenu.listener;

import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.config.MenuSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.menu.CustomMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

final class CustomMenuCommandListener extends Listener {
    public CustomMenuCommandListener() {
        super(ModuleMain.instance, ConfigSetting.getInstance().getConfig().enable());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().substring(1).split(" ")[0];

        MenuSetting.Config.Menu config = MenuSetting.getInstance().getMenuByCommand(command);
        if (config == null) return;

        event.setCancelled(true);
        new CustomMenu(player, config).openInventory();
    }
}

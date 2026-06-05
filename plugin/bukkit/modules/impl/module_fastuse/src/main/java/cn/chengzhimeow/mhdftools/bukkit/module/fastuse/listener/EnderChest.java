package cn.chengzhimeow.mhdftools.bukkit.module.fastuse.listener;

import cn.chengzhimeow.mhdftools.bukkit.module.fastuse.config.ConfigSetting;

import cn.chengzhimeow.mhdftools.bukkit.module.fastuse.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

final class EnderChest extends Listener {
    public EnderChest() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable() && ConfigSetting.getInstance().getConfig().items().enderChest()
        );
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.useItemInHand() == Event.Result.DENY) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("mhdftools.fastuse.ender_chest")) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.ENDER_CHEST) return;

        player.openInventory(player.getEnderChest());
    }
}

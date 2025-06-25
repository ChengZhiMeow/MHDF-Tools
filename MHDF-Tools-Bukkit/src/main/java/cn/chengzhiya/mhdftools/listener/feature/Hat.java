package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.listener.AbstractListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class Hat extends AbstractListener {
    public Hat() {
        super(
                List.of("hatSettings.enable")
        );
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onClickInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!player.hasPermission("mhdftools.commands.hat.menu")) {
            return;
        }

        if (event.getSlotType() != InventoryType.SlotType.ARMOR || event.getRawSlot() != 5) {
            return;
        }
        event.setCancelled(true);

        ItemStack oldHat = player.getInventory().getHelmet();
        ItemStack newHat = event.getCursor();

        player.getInventory().setHelmet(newHat);
        event.setCursor(oldHat);
    }
}

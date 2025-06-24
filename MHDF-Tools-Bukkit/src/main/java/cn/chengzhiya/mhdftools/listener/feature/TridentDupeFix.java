package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.menu.MenuUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TridentDupeFix extends AbstractListener {
    private final Set<String> useTridentSet = new HashSet<>();

    public TridentDupeFix() {
        super(
                List.of("tridentDupeFixSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (player.getInventory().getItemInMainHand().getType() != Material.TRIDENT &&
                player.getInventory().getItemInOffHand().getType() != Material.TRIDENT) {
            return;
        }

        useTridentSet.add(player.getName());
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (!(projectile instanceof Trident)) {
            return;
        }

        ProjectileSource source = projectile.getShooter();
        if (!(source instanceof Player player)) {
            return;
        }

        useTridentSet.remove(player.getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        useTridentSet.remove(player.getName());
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getSlotType() != InventoryType.SlotType.CRAFTING && event.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }

        if (!useTridentSet.contains(player.getName())) {
            return;
        }

        ItemStack item = MenuUtil.getClickItem(event);
        if (item == null) {
            return;
        }

        if (item.getType() != Material.TRIDENT) {
            return;
        }

        event.setCancelled(true);
        useTridentSet.remove(player.getName());
    }
}

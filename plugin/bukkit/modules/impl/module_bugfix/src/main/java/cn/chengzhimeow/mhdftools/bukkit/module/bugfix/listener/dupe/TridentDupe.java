package cn.chengzhimeow.mhdftools.bukkit.module.bugfix.listener.dupe;

import cn.chengzhimeow.mhdftools.bukkit.module.bugfix.config.ConfigSetting;

import cn.chengzhimeow.mhdftools.bukkit.common.inventory.InventoryUtil;
import cn.chengzhimeow.mhdftools.bukkit.module.bugfix.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
import java.util.UUID;

final class TridentDupe extends Listener {
    private final Set<UUID> useTridentPlayers = new HashSet<>();

    public TridentDupe() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().dupe().trident().enable()
        );
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        boolean isRight = event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK;
        if (!isRight) return;

        boolean isTrident = player.getInventory().getItemInMainHand().getType() == Material.TRIDENT ||
                player.getInventory().getItemInOffHand().getType() == Material.TRIDENT;
        if (!isTrident) return;

        this.useTridentPlayers.add(player.getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (!(projectile instanceof Trident)) return;

        ProjectileSource source = projectile.getShooter();
        if (!(source instanceof Player player)) return;

        this.useTridentPlayers.remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.useTridentPlayers.remove(player.getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        boolean clickCraftSlot = event.getSlotType() == InventoryType.SlotType.CRAFTING || event.getSlotType() == InventoryType.SlotType.RESULT;
        if (!clickCraftSlot) return;

        if (!this.useTridentPlayers.contains(player.getUniqueId())) return;

        ItemStack item = InventoryUtil.getClickItem(event);
        if (item == null) return;
        if (item.getType() != Material.TRIDENT) return;

        event.setCancelled(true);
        this.useTridentPlayers.remove(player.getUniqueId());
    }
}

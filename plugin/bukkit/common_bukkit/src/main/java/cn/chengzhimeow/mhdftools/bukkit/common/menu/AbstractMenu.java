package cn.chengzhimeow.mhdftools.bukkit.common.menu;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.thread.ThreadPool;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public abstract class AbstractMenu extends Menu implements InventoryHolder {
    private final Player player;
    private Inventory lastInventory;

    public AbstractMenu(@Nullable Player player) {
        this.player = player;
    }

    public AbstractMenu() {
        this(null);
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
    }

    @Override
    public void onOpen(@NotNull InventoryOpenEvent event) {
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event) {
    }

    protected abstract @NotNull Inventory buildInventory();

    public void updateInventory() {
        this.lastInventory = this.buildInventory();
    }

    @Override
    public @NotNull Inventory getInventory() {
        if (this.lastInventory == null) this.updateInventory();
        return this.lastInventory;
    }

    public void refreshInventory(@NotNull Player player) {
        if (this.lastInventory == null) return;

        Inventory inventory = player.getOpenInventory().getTopInventory();
        if (!(inventory.getHolder() instanceof AbstractMenu)) return;

        this.updateInventory();
        inventory.setContents(this.lastInventory.getContents());
        player.updateInventory();
    }

    public void refreshInventory() {
        if (this.player == null) return;
        this.refreshInventory(this.player);
    }

    public void openInventory(@NotNull Player player) {
        Menu.getMenuThread().execute(() -> {
            Inventory inventory = this.getInventory();
            CCScheduler.getInstance().getGlobalRegionScheduler().runTask(
                    MHDFToolsBukkit.getInstance(),
                    () -> player.openInventory(inventory)
            );
        });
    }

    public void openInventory() {
        if (this.player == null) return;
        this.openInventory(this.player);
    }
}

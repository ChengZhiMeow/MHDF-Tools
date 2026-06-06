package cn.chengzhimeow.mhdftools.bukkit.module.vanish.listener;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.config.ConfigSetting;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

final class VirtualContainer extends Listener {
    public VirtualContainer() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable()
        );
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!ConfigSetting.getInstance().getConfig().openContainerWithoutAnimation()) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        if (!MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId()).isEnableVanish()) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        VirtualContainerTarget target = this.toVirtualContainerTarget(block);
        if (target == null) return;

        event.setCancelled(true);

        VirtualContainerHolder holder = new VirtualContainerHolder(target);
        Inventory inventory = Bukkit.createInventory(holder, target.size(), target.title());
        holder.setInventory(inventory);
        inventory.setContents(target.contents());

        player.openInventory(inventory);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof VirtualContainerHolder holder)) return;
        holder.sync();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof VirtualContainerHolder holder)) return;
        holder.sync();
    }

    private VirtualContainerTarget toVirtualContainerTarget(Block block) {
        if (!(block.getState() instanceof Container container)) return null;

        return switch (container) {
            case Chest chest -> this.toChestTarget(chest);
            case Barrel barrel ->
                    new SingleContainerTarget(barrel.getInventory(), Component.translatable("container.barrel"));
            case ShulkerBox shulkerBox ->
                    new SingleContainerTarget(shulkerBox.getInventory(), Component.translatable("container.shulkerBox"));
            default -> null;
        };

    }

    private VirtualContainerTarget toChestTarget(Chest chest) {
        InventoryHolder holder = chest.getInventory().getHolder();
        if (holder instanceof DoubleChest doubleChest
                && doubleChest.getLeftSide() instanceof Chest left
                && doubleChest.getRightSide() instanceof Chest right) {
            return new DoubleChestContainerTarget(left.getBlockInventory(), right.getBlockInventory());
        }

        return new SingleContainerTarget(chest.getBlockInventory(), Component.translatable("container.chest"));
    }

    private interface VirtualContainerTarget {
        int size();

        Component title();

        ItemStack[] contents();

        void write(ItemStack[] contents);
    }

    private record SingleContainerTarget(
            Inventory inventory,
            Component title
    ) implements VirtualContainerTarget {
        @Override
        public int size() {
            return this.inventory.getSize();
        }

        @Override
        public ItemStack[] contents() {
            return Arrays.copyOf(this.inventory.getContents(), this.size());
        }

        @Override
        public void write(ItemStack[] contents) {
            this.inventory.setContents(Arrays.copyOf(contents, this.size()));
        }
    }

    private record DoubleChestContainerTarget(
            Inventory left,
            Inventory right
    ) implements VirtualContainerTarget {
        @Override
        public int size() {
            return this.left.getSize() + this.right.getSize();
        }

        @Override
        public Component title() {
            return Component.translatable("container.chestDouble");
        }

        @Override
        public ItemStack[] contents() {
            ItemStack[] contents = new ItemStack[this.size()];
            System.arraycopy(this.left.getContents(), 0, contents, 0, this.left.getSize());
            System.arraycopy(this.right.getContents(), 0, contents, this.left.getSize(), this.right.getSize());
            return contents;
        }

        @Override
        public void write(ItemStack[] contents) {
            this.left.setContents(Arrays.copyOfRange(contents, 0, this.left.getSize()));
            this.right.setContents(Arrays.copyOfRange(contents, this.left.getSize(), this.size()));
        }
    }

    private static final class VirtualContainerHolder implements InventoryHolder {
        private final VirtualContainerTarget target;
        private Inventory inventory;

        private VirtualContainerHolder(VirtualContainerTarget target) {
            this.target = target;
        }

        public void sync() {
            this.target.write(this.inventory.getContents());
        }

        @Override
        public Inventory getInventory() {
            return this.inventory;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }
    }
}

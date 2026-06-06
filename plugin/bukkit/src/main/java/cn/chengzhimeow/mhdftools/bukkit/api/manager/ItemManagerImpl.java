package cn.chengzhimeow.mhdftools.bukkit.api.manager;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.api.entity.BuilderItem;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.item.ItemCompatibilityRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class ItemManagerImpl extends ItemManager {
    @Override
    public @NotNull ItemStack buildItemStack(@NotNull BuilderItem item) {
        return this.buildItemStack(item, null);
    }

    @Override
    public @NotNull ItemStack buildItemStack(@NotNull BuilderItem item, @Nullable Player player) {
        ItemStack itemStack = this.createBaseItemStack(item);
        itemStack.setAmount(Math.max(item.getAmount(), 1));

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            if (item.getName() != null) meta.displayName(item.getName());
            if (!item.getLore().isEmpty()) meta.lore(item.getLore());
            if (item.getCustomModelData() != null && item.getCustomModelData() > 0) {
                meta.setCustomModelData(item.getCustomModelData());
            }

            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(MHDFToolsBukkit.getInstance(), "id"), PersistentDataType.STRING, item.getId());
            item.getPdc().forEach((key, value) ->
                    container.set(new NamespacedKey(MHDFToolsBukkit.getInstance(), key), PersistentDataType.STRING, value)
            );
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    private @NotNull ItemStack createBaseItemStack(@NotNull BuilderItem item) {
        if (!item.getBy().equalsIgnoreCase("vanilla")) {
            return ItemCompatibilityRegistry.getInstance().getItemById(item.getBy().toLowerCase(), item.getType());
        }

        if (item.getType().startsWith("head-")) {
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
            if (itemStack.getItemMeta() instanceof SkullMeta meta) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(item.getType().substring("head-".length()));
                meta.setOwningPlayer(offlinePlayer);
                itemStack.setItemMeta(meta);
            }
            return itemStack;
        }

        Material material = Material.matchMaterial(item.getType());
        return new ItemStack(Objects.requireNonNullElse(material, Material.AIR));
    }
}

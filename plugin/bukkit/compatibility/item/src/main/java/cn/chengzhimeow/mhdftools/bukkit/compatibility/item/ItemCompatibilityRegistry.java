package cn.chengzhimeow.mhdftools.bukkit.compatibility.item;

import cn.chengzhimeow.mhdftools.feature.ClassScanner;
import cn.chengzhimeow.mhdftools.registry.Registry;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class ItemCompatibilityRegistry extends Registry<String, ItemCompatibility> {
    @Getter(lazy = true)
    private static final ItemCompatibilityRegistry instance = new ItemCompatibilityRegistry();

    @Override
    @SneakyThrows
    public Map<String, ItemCompatibility> defaultRegistry() {
        Map<String, ItemCompatibility> map = new HashMap<>();
        for (Class<? extends ItemCompatibility> clazz : ClassScanner.scanSubTypeOf(ItemCompatibility.class)) {
            ItemCompatibility compatibility = clazz.getConstructor().newInstance();
            if (!compatibility.isEnable()) continue;
            map.put(compatibility.getId(), compatibility);
        }

        return map;
    }

    public @NotNull ItemStack getItemById(@NotNull String compatibilityId, @NotNull String id) {
        ItemCompatibility compatibility = super.registry.get(compatibilityId);
        if (compatibility == null) return new ItemStack(Material.AIR);

        return compatibility.getItemById(id);
    }

    public @Nullable String getIdByItemStack(@NotNull String compatibilityId, @NotNull ItemStack itemStack) {
        ItemCompatibility compatibility = super.registry.get(compatibilityId);
        if (compatibility == null) return null;

        return compatibility.getIdByItemStack(itemStack);
    }
}

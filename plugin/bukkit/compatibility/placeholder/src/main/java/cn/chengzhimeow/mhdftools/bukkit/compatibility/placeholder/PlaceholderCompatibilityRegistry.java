package cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder;

import cn.chengzhimeow.mhdftools.feature.ClassScanner;
import cn.chengzhimeow.mhdftools.registry.Registry;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class PlaceholderCompatibilityRegistry extends Registry<String, PlaceholderCompatibility> {
    @Getter(lazy = true)
    private static final PlaceholderCompatibilityRegistry instance = new PlaceholderCompatibilityRegistry();

    @Override
    @SneakyThrows
    public Map<String, PlaceholderCompatibility> defaultRegistry() {
        Map<String, PlaceholderCompatibility> map = new HashMap<>();
        for (Class<? extends PlaceholderCompatibility> clazz : ClassScanner.scanSubTypeOf(PlaceholderCompatibility.class)) {
            PlaceholderCompatibility compatibility = clazz.getConstructor().newInstance();
            if (!compatibility.isEnable()) continue;
            map.put(compatibility.getId(), compatibility);
        }

        return map;
    }

    public @Nullable String parseString(@NotNull String compatibilityId, @Nullable OfflinePlayer player, @Nullable String string) {
        PlaceholderCompatibility compatibility = super.get(compatibilityId);
        if (compatibility == null) return string;

        return compatibility.parseString(player, string);
    }
}

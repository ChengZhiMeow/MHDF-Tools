package cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

@Getter
public final class PlaceholderApiImpl implements PlaceholderCompatibility {
    private final boolean enable;
    private final String id = PlaceholderCompatibilityIds.PLACEHOLDER_API;

    public PlaceholderApiImpl() {
        this.enable = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    @Override
    public @Nullable String parseString(@Nullable OfflinePlayer player, @Nullable String string) {
        if (string == null) return null;
        return PlaceholderAPI.setPlaceholders(player, string);
    }
}

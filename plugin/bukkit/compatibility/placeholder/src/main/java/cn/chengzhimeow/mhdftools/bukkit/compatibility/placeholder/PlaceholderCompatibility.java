package cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlaceholderCompatibility {
    boolean isEnable();

    @NotNull String getId();

    @Nullable String parseString(@Nullable OfflinePlayer player, @Nullable String string);

    class PlaceholderCompatibilityIds {
        public static String PLACEHOLDER_API = "placeholder_api";
    }
}

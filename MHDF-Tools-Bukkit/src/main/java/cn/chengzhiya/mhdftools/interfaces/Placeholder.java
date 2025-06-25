package cn.chengzhiya.mhdftools.interfaces;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public interface Placeholder {
    String placeholder(OfflinePlayer player, @NotNull String placeholder);
}

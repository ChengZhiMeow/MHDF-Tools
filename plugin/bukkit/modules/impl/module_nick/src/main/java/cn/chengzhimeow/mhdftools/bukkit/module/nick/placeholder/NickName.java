package cn.chengzhimeow.mhdftools.bukkit.module.nick.placeholder;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Placeholder;
import cn.chengzhimeow.mhdftools.bukkit.module.nick.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.nick.config.ConfigSetting;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class NickName extends Placeholder {
    public NickName() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "nick_name"
        );
    }

    @Override
    public @Nullable String placeholder(@Nullable OfflinePlayer player, @NotNull String[] args) {
        if (player == null) return null;

        return MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId()).getDisplayName();
    }
}

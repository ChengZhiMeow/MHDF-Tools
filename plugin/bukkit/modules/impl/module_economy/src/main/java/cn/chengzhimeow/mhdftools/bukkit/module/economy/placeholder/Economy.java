package cn.chengzhimeow.mhdftools.bukkit.module.economy.placeholder;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Placeholder;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class Economy extends Placeholder {
    public Economy() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "money"
        );
    }

    @Override
    public @Nullable String placeholder(@Nullable OfflinePlayer player, @NotNull String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("name")) {
            return ConfigSetting.getInstance().getConfig().moneyName();
        }

        if (player == null) return null;
        if (args.length == 1 && args[0].equalsIgnoreCase("amount")) {
            MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
            return mhdfPlayer.getMoney().toPlainString();
        }

        return null;
    }
}

package cn.chengzhimeow.mhdftools.bukkit.module.home.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.home.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.home.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.home.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class DelHome extends Command {
    public DelHome() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "删除家",
                "mhdftools.commands.delhome",
                true,
                ConfigSetting.getInstance().getConfig().delHomeCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().delhome().usage())
                    .replace("{command}", label));
            return;
        }

        if (ConfigSetting.getInstance().getConfig().blackWorld().contains(sender.getWorld().getName())) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().blackWorld());
            return;
        }

        if (!this.isValidName(args[0])) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().home().invalidName());
            return;
        }

        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        if (!player.hasHome(args[0])) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().delhome().noHome()
                    .replace("{home}", args[0]));
            return;
        }

        player.deleteHome(args[0]);
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().delhome().message()
                .replace("{home}", args[0]));
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());

            return player.getHomeList().stream()
                    .map(HomeData::getHome)
                    .toList();
        }
        return new ArrayList<>();
    }

    private boolean isValidName(String name) {
        String regex = ConfigSetting.getInstance().getConfig().regex();
        return regex == null || regex.isBlank() || name.matches(regex);
    }
}

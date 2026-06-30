package cn.chengzhimeow.mhdftools.bukkit.module.home.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.home.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.home.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.home.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.home.menu.HomeMenu;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Home extends Command {
    public Home() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "传送到指定家",
                "mhdftools.commands.home",
                true,
                ConfigSetting.getInstance().getConfig().homeCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (ConfigSetting.getInstance().getConfig().blackWorld().contains(sender.getWorld().getName())) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().blackWorld());
            return;
        }

        if (args.length == 0) {
            new HomeMenu(sender, 1).openInventory();
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().home().openMenuMessage());
            return;
        }

        if (args.length != 1) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().home().usage())
                    .replace("{command}", label));
            return;
        }

        if (!this.isValidName(args[0])) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().home().invalidName());
            return;
        }

        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        if (!player.hasHome(args[0])) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().home().noHome()
                    .replace("{home}", args[0]));
            return;
        }

        player.teleport(player.getHome(args[0]).toBungeeCordLocation());
        player.sendMessage(LangSetting.getInstance().getConfig().commands().home().message()
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

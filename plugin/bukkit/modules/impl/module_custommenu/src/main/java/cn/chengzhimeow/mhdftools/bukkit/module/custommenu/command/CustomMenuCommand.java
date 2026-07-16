package cn.chengzhimeow.mhdftools.bukkit.module.custommenu.command;

import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.config.MenuSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.menu.CustomMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class CustomMenuCommand extends Command {
    public CustomMenuCommand() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                LangSetting.getInstance().getConfig().commands().custommenu().description(),
                "mhdftools.commands.custommenu",
                true,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().custommenu().usage())
                    .replace("{command}", label));
            return;
        }

        MenuSetting.Config.Menu config = MenuSetting.getInstance().getMenu(args[0]);
        if (config == null) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().custommenu().noMenu()
                    .replace("{menu}", args[0]));
            return;
        }

        new CustomMenu(sender, config).openInventory();
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().custommenu().message()
                .replace("{menu}", args[0]));
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return new ArrayList<>(MenuSetting.getInstance().getMenuIds());
        return List.of();
    }
}

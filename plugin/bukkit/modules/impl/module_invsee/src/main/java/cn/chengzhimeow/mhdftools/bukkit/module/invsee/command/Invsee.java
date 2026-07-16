package cn.chengzhimeow.mhdftools.bukkit.module.invsee.command;

import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.invsee.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.invsee.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.invsee.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.invsee.menu.ArmorMenu;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

final class Invsee extends Command {
    public Invsee() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                LangSetting.getInstance().getConfig().commands().invsee().description(),
                "mhdftools.commands.invsee",
                true,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().invsee().usage())
                    .replace("{command}", label));
            return;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
            return;
        }

        String type = args[1].toLowerCase();
        if (!this.openInventory(sender, target, type)) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().invsee().noType()
                    .replace("{type}", args[1]));
            return;
        }

        TextComponent typeName = LangSetting.getInstance().getConfig().commands().invsee().types().get(type);
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().invsee().message()
                .replace("{player}", target.getName())
                .replace("{type}", typeName));
    }

    private boolean openInventory(Player sender, Player target, String type) {
        switch (type) {
            case "inventory" -> sender.openInventory(target.getInventory());
            case "enderchest" -> sender.openInventory(target.getEnderChest());
            case "armor" -> new ArmorMenu(sender, target).openMenu();
            default -> {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return null;
        if (args.length == 2) return Arrays.asList("inventory", "enderchest", "armor");

        return List.of();
    }
}

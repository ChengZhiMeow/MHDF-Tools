package cn.chengzhimeow.mhdftools.bukkit.module.back.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.BackData;
import cn.chengzhimeow.mhdftools.bukkit.module.back.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.back.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.back.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.back.menu.BackMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.back.service.BackService;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Back extends Command {
    public Back() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "返回位置记录",
                "mhdftools.commands.back",
                true,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (ConfigSetting.getInstance().getConfig().blackWorld().contains(sender.getWorld().getName())) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().blackWorld());
            return;
        }

        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        List<BackData> backDataList;
        if (args.length < 1) {
            backDataList = player.getBackDataList(BackService.getMaxBack(sender));
        } else {
            switch (args[0]) {
                case "menu" -> {
                    new BackMenu(sender, 1).openInventory();
                    sender.sendMessage(LangSetting.getInstance().getConfig().commands().back().openMenuMessage());
                    return;
                }
                case "teleport", "death" ->
                        backDataList = player.getBackDataList(args[0], BackService.getMaxBack(sender));
                default -> {
                    sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                            .replace("{usage}", LangSetting.getInstance().getConfig().commands().back().usage())
                            .replace("{command}", label));
                    return;
                }
            }
        }

        if (backDataList.isEmpty()) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().back().noLocation());
            return;
        }

        player.teleport(backDataList.getFirst().toBungeeCordLocation());
        player.sendMessage(LangSetting.getInstance().getConfig().commands().back().message());
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("menu", "teleport", "death");
        }
        return new ArrayList<>();
    }
}

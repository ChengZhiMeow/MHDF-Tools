package cn.chengzhimeow.mhdftools.bukkit.module.fly.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.config.LangSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class FlyTime extends Command {
    public FlyTime() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "限时飞行",
                "mhdftools.commands.flytime",
                false,
                ConfigSetting.getInstance().getConfig().flytimeCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 3) {
            switch (args[0]) {
                case "set", "add", "take" -> {
                    MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(args[1]);
                    long inputTime;
                    try {
                        inputTime = Long.parseLong(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(LangSetting.getInstance().getConfig().commands().flytime().timeFormatError());
                        return;
                    }

                    TextComponent message;
                    if (args[0].equals("set")) {
                        target.setFlyTime(inputTime);
                        message = LangSetting.getInstance().getConfig().commands().flytime().subCommands().set().message();
                    } else if (args[0].equals("add")) {
                        target.addFlyTime(inputTime);
                        message = LangSetting.getInstance().getConfig().commands().flytime().subCommands().add().message();
                    } else {
                        target.takeFlyTime(inputTime);
                        message = LangSetting.getInstance().getConfig().commands().flytime().subCommands().take().message();
                    }

                    sender.sendMessage(message
                            .replace("{player}", args[1])
                            .replace("{change}", String.valueOf(inputTime))
                            .replace("{amount}", String.valueOf(target.getFlyTime())));
                    return;
                }
            }
        }

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().flytime().subCommands().help().message()
                .replace("{help_list}", ColorUtil.color(this.helpList(label)))
                .replace("{command}", label));
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 2 && (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take"))) {
            return null;
        }
        if (args.length == 1) return Arrays.asList("help", "set", "add", "take");
        return new ArrayList<>();
    }

    private String helpList(String label) {
        LangSetting.Config.Commands.FlyTime.SubCommands subCommands = LangSetting.getInstance().getConfig().commands().flytime().subCommands();
        return String.join("\n",
                "&8▪ &f" + subCommands.help().usage().replace("{command}", label).toMiniMessageString() + " &7- " + subCommands.help().description().toMiniMessageString(),
                "&8▪ &f" + subCommands.set().usage().replace("{command}", label).toMiniMessageString() + " &7- " + subCommands.set().description().toMiniMessageString(),
                "&8▪ &f" + subCommands.add().usage().replace("{command}", label).toMiniMessageString() + " &7- " + subCommands.add().description().toMiniMessageString(),
                "&8▪ &f" + subCommands.take().usage().replace("{command}", label).toMiniMessageString() + " &7- " + subCommands.take().description().toMiniMessageString()
        );
    }
}

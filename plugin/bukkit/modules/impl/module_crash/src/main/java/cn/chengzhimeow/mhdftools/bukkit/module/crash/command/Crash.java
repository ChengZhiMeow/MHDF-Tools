package cn.chengzhimeow.mhdftools.bukkit.module.crash.command;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkitAdapt;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.crash.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.crash.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.crash.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.crash.util.CrashUtil;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

final class Crash extends Command {
    public Crash() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "崩溃玩家客户端",
                "mhdftools.commands.crash",
                false,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1 && args.length != 2 && args.length != 3) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().crash().usage())
                    .replace("{command}", label));
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
            return;
        }

        String type = args.length == 1
                      ? ConfigSetting.getInstance().getConfig().defaultType()
                      : args[1].toLowerCase();

        if (type == null || !CrashUtil.crashPlayerClient(player, type)) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().crash().noType());
            return;
        }

        TextComponent typeText = switch (type) {
            case "explosion" -> LangSetting.getInstance().getConfig().commands().crash().types().explosion();
            case "invalid_teleport" ->
                    LangSetting.getInstance().getConfig().commands().crash().types().invalidTeleport();
            case "invalid_particle" ->
                    LangSetting.getInstance().getConfig().commands().crash().types().invalidParticle();
            default -> new TextComponent();
        };

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().crash().message()
                .replace("{player}", MHDFToolsBukkitAdapt.adapt(player).getDisplayName())
                .replace("{type}", typeText));
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return BungeeCordManager.getInstance().getBukkitPlayerList();
        if (args.length == 2) return Arrays.asList("explosion", "invalid_teleport", "invalid_particle");
        return super.tabCompleter(sender, label, args);
    }
}


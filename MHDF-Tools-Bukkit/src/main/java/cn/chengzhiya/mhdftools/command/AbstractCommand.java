package cn.chengzhiya.mhdftools.command;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.YamlUtil;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
public abstract class AbstractCommand implements TabExecutor, Command {
    private final boolean enable;
    private final String description;
    private final String permission;
    private final boolean onlyPlayer;
    private final String[] commands;

    public AbstractCommand(List<String> enableKeyList, @NotNull String description, String permission, boolean onlyPlayer, String... commands) {
        this.enable = YamlUtil.equalsTrue(Main.instance.getConfigManager().getConfigManager().getData(), enableKeyList);
        this.description = description;
        this.permission = permission;
        this.onlyPlayer = onlyPlayer;
        this.commands = commands;
    }

    public AbstractCommand(@NotNull String description, String permission, boolean onlyPlayer, String... commands) {
        this(new ArrayList<>(), description, permission, onlyPlayer, commands);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (onlyPlayer) {
            if (sender instanceof Player player) {
                execute(player, label, args);
            } else {
                ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("onlyPlayer"));
            }
            return false;
        }
        execute(sender, label, args);
        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, String @NotNull [] args) {
        List<String> tabComplete = new ArrayList<>();
        if (onlyPlayer) {
            if (sender instanceof Player player) {
                tabComplete = tabCompleter(player, label, args);
            }
        } else {
            tabComplete = tabCompleter(sender, label, args);
        }

        if (tabComplete == null) {
            tabComplete = Main.instance.getBungeeCordManager().getBukkitPlayerList();
        }
        return tabComplete.stream()
                .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(args[args.length - 1].toLowerCase(Locale.ROOT)))
                .toList();
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {

    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}

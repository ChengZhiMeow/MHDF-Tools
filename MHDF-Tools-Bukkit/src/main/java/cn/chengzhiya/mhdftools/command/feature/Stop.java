package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdfscheduler.runnable.MHDFRunnable;
import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
final class Stop extends AbstractCommand {
    private boolean stop = false;
    private Integer time = null;
    private TextComponent message = null;

    public Stop() {
        super(
                List.of("stopSettings.enable"),
                "更好的关服",
                "mhdftools.commands.stop",
                false,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("stopSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "help" -> {
                    if (args.length != 1) {
                        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                                .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.stop.subCommands.help.usage"))
                                .replace("{command}", label)
                        );
                        return;
                    }

                    ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.stop.subCommands.help.message")
                            .replace("{helpList}", Main.instance.getConfigManager().getLangManager().getHelpList("commands.stop.subCommands"))
                            .replace("{command}", label)
                    );
                    return;
                }
                case "confirm" -> {
                    if (args.length != 1) {
                        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                                .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.stop.subCommands.confirm.usage"))
                                .replace("{command}", label)
                        );
                        return;
                    }

                    if (getTime() == null || getMessage() == null) {
                        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.stop.subCommands.confirm.noStop"));
                        return;
                    }

                    confirmStop();
                    return;
                }
                case "cancel" -> {
                    if (args.length != 1) {
                        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                                .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.stop.subCommands.cancel.usage"))
                                .replace("{command}", label)
                        );
                        return;
                    }

                    if (!isStop()) {
                        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.stop.subCommands.cancel.noStop"));
                        return;
                    }

                    setStop(false);
                    ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.stop.subCommands.cancel.message"));
                    return;
                }
            }
        }

        if (isStop()) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.stop.subCommands.default.inStop"));
            return;
        }

        try {
            int defaultTime = Main.instance.getConfigManager().getConfigManager().getData().getInt("stopSettings.defaultCountdown");
            setTime(args.length >= 1 ? Integer.parseInt(args[0]) : defaultTime);
        } catch (NumberFormatException e) {
            sender.sendMessage(Main.instance.getConfigManager().getLangManager().i18n("commands.stop.timeFormatError"));
            return;
        }

        TextComponent defaultMessage = Main.instance.getConfigManager().getLangManager().i18n("commands.stop.defaultMessage");
        setMessage(args.length >= 2 ? ColorUtil.color(args[1]) : defaultMessage);

        if (Main.instance.getConfigManager().getConfigManager().getData().getBoolean("stopSettings.confirm")) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.stop.subCommands.default.message")
                    .replace("{time}", String.valueOf(getTime()))
                    .replace("{message}", getMessage())
            );
            return;
        }
        confirmStop();
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(Main.instance.getConfigManager().getLangManager().getKeys("commands.stop.subCommands"));
        }
        return new ArrayList<>();
    }

    /**
     * 确认关服
     */
    private void confirmStop() {
        setStop(true);
        startStopRunnable(getTime(), getMessage());

        setTime(null);
        setMessage(null);
    }

    /**
     * 开始倒计时关闭服务器
     *
     * @param time    倒计时
     * @param message 消息
     */
    private void startStopRunnable(int time, TextComponent message) {
        new MHDFRunnable() {
            private int countdown = time;

            @Override
            public void run() {
                if (!isStop()) {
                    this.cancel();
                    return;
                }

                if (countdown <= 0) {
                    setStop(false);
                    stopServer(message);
                    this.cancel();
                    return;
                }

                ActionUtil.broadcastMessage(Main.instance.getConfigManager().getLangManager().i18n("commands.stop.countdownMessage")
                        .replace("{countdown}", String.valueOf(countdown))
                );
                countdown--;
            }
        }.runTaskTimerAsynchronously(Main.instance, 0L, 20L);
    }

    /**
     * 关闭服务器
     *
     * @param message 消息
     */
    private void stopServer(TextComponent message) {
        MHDFScheduler.getGlobalRegionScheduler().runTask(Main.instance, () -> {
            Bukkit.savePlayers();

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.kick(Main.instance.getConfigManager().getLangManager().i18n("commands.stop.kickMessage")
                        .replace("{message}", message)
                );
            }

            Bukkit.shutdown();
        });
    }
}

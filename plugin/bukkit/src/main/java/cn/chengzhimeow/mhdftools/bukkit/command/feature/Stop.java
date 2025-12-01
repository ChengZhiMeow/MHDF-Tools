package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.ccscheduler.runnable.CCRunnable;
import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
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
final class Stop extends Command {
    private boolean stop = false;
    private Integer time = null;
    private TextComponent message = null;

    public Stop() {
        super(
                null,
                List.of("stopSettings.enable"),
                "更好的关服",
                "mhdftools.commands.stop",
                false,
                ConfigSetting.getInstance().getData().getStringList("stopSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "help" -> {
                    if (args.length != 1) {
                        sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                                .replace("{usage}", LangSetting.getInstance().i18n("commands.stop.subCommands.help.usage"))
                                .replace("{command}", label)
                        );
                        return;
                    }

                    sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.subCommands.help.message")
                            .replace("{helpList}", LangSetting.getInstance().getHelpList("commands.stop.subCommands"))
                            .replace("{command}", label)
                    );
                    return;
                }
                case "confirm" -> {
                    if (args.length != 1) {
                        sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                                .replace("{usage}", LangSetting.getInstance().i18n("commands.stop.subCommands.confirm.usage"))
                                .replace("{command}", label)
                        );
                        return;
                    }

                    if (this.getTime() == null || this.getMessage() == null) {
                        sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.subCommands.confirm.noStop"));
                        return;
                    }

                    this.confirmStop();
                    return;
                }
                case "cancel" -> {
                    if (args.length != 1) {
                        sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                                .replace("{usage}", LangSetting.getInstance().i18n("commands.stop.subCommands.cancel.usage"))
                                .replace("{command}", label)
                        );
                        return;
                    }

                    if (!this.isStop()) {
                        sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.subCommands.cancel.noStop"));
                        return;
                    }

                    this.setStop(false);
                    sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.subCommands.cancel.message"));
                    return;
                }
            }
        }

        if (this.isStop()) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.subCommands.default.inStop"));
            return;
        }

        try {
            int defaultTime = ConfigSetting.getInstance().getData().getInt("stopSettings.countdown.default");
            this.setTime(args.length >= 1 ? Integer.parseInt(args[0]) : defaultTime);
        } catch (NumberFormatException e) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.timeFormatError"));
            return;
        }

        TextComponent defaultMessage = LangSetting.getInstance().i18n("commands.stop.defaultMessage");
        this.setMessage(args.length >= 2 ? ColorUtil.color(args[1]) : defaultMessage);

        if (ConfigSetting.getInstance().getData().getBoolean("stopSettings.confirm")) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.subCommands.default.message")
                    .replace("{time}", String.valueOf(this.getTime()))
                    .replace("{message}", this.getMessage())
            );
            return;
        }
        this.confirmStop();
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(LangSetting.getInstance().getKeys("commands.stop.subCommands"));
        }
        return new ArrayList<>();
    }

    /**
     * 确认关服
     */
    private void confirmStop() {
        this.setStop(true);
        this.startStopRunnable(this.getTime(), this.getMessage());

        this.setTime(null);
        this.setMessage(null);
    }

    /**
     * 开始倒计时关闭服务器
     *
     * @param time    倒计时
     * @param message 消息
     */
    private void startStopRunnable(int time, TextComponent message) {
        new CCRunnable(CCScheduler.getInstance()) {
            private int countdown = time;

            @Override
            public void run() {
                if (!Stop.this.isStop()) {
                    this.cancel();
                    return;
                }

                if (countdown <= 0) {
                    Stop.this.setStop(false);
                    Stop.this.stopServer(message);
                    this.cancel();
                    return;
                }

                if (ConfigSetting.getInstance().getData().getIntList("stopSettings.countdown.messageTime").contains(countdown))
                    ActionUtil.broadcastMessage(LangSetting.getInstance().i18n("commands.stop.countdownMessage")
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
        CCScheduler.getInstance().getGlobalRegionScheduler().runTask(Main.instance, () -> {
            Bukkit.savePlayers();

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.kick(LangSetting.getInstance().i18n("commands.stop.kickMessage")
                        .replace("{message}", message)
                );
            }

            Bukkit.shutdown();
        });
    }
}

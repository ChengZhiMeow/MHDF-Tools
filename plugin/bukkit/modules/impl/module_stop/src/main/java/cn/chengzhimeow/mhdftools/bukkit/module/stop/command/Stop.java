package cn.chengzhimeow.mhdftools.bukkit.module.stop.command;

import cn.chengzhimeow.ccscheduler.runnable.CCRunnable;
import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.stop.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.stop.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.stop.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Stop extends Command {
    private boolean stop = false;
    private Integer time = null;
    private TextComponent message = null;

    public Stop() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "更好的关服",
                "mhdftools.commands.stop",
                false,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "help" -> {
                    if (args.length != 1) {
                        sender.sendMessage(GlobalLangSetting.getInstance().i18n("usage_error")
                                .replace("{usage}", LangSetting.getInstance().i18n("commands.stop.sub_commands.help.usage"))
                                .replace("{command}", label));
                        return;
                    }

                    sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.sub_commands.help.message")
                            .replace("{help_list}", LangSetting.getInstance().getHelpList("commands.stop.sub_commands"))
                            .replace("{command}", label));
                    return;
                }
                case "confirm" -> {
                    if (args.length != 1) {
                        sender.sendMessage(GlobalLangSetting.getInstance().i18n("usage_error")
                                .replace("{usage}", LangSetting.getInstance().i18n("commands.stop.sub_commands.confirm.usage"))
                                .replace("{command}", label));
                        return;
                    }

                    if (this.time == null || this.message == null) {
                        sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.sub_commands.confirm.no_stop"));
                        return;
                    }

                    this.confirmStop();
                    return;
                }
                case "cancel" -> {
                    if (args.length != 1) {
                        sender.sendMessage(GlobalLangSetting.getInstance().i18n("usage_error")
                                .replace("{usage}", LangSetting.getInstance().i18n("commands.stop.sub_commands.cancel.usage"))
                                .replace("{command}", label));
                        return;
                    }

                    if (!this.stop) {
                        sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.sub_commands.cancel.no_stop"));
                        return;
                    }

                    this.stop = false;
                    sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.sub_commands.cancel.message"));
                    return;
                }
            }
        }

        if (this.stop) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.sub_commands.default.in_stop"));
            return;
        }

        // 修改倒计时
        try {
            int defaultTime = ConfigSetting.getInstance().getConfig().countdown().defaultTime();
            this.time = args.length >= 1 ? Integer.parseInt(args[0]) : defaultTime;
        } catch (NumberFormatException e) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.time_format_error"));
            return;
        }

        // 修改关服提示
        {
            TextComponent defaultMessage = LangSetting.getInstance().i18n("commands.stop.default_message");
            this.message = args.length >= 2 ? ColorUtil.color(args[1]) : defaultMessage;
        }

        // 无需确认
        if (!ConfigSetting.getInstance().getConfig().confirm()) {
            this.confirmStop();
            return;
        }

        sender.sendMessage(LangSetting.getInstance().i18n("commands.stop.sub_commands.default.message")
                .replace("{time}", String.valueOf(this.time))
                .replace("{message}", this.message));
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return new ArrayList<>(LangSetting.getInstance().getKeys("commands.stop.sub_commands"));
        return new ArrayList<>();
    }

    /**
     * 确认关服
     */
    private void confirmStop() {
        this.stop = true;

        JavaPlugin plugin = ModuleMain.instance.getPlugin();
        new CCRunnable(CCScheduler.getInstance()) {
            private final Component message = Stop.this.message;
            private int countdown = Stop.this.time;

            @Override
            public void run() {
                if (!Stop.this.stop) {
                    this.cancel();
                    return;
                }

                if (countdown <= 0) {
                    Stop.this.stop = false;
                    this.cancel();

                    CCScheduler.getInstance().getGlobalRegionScheduler().runTask(plugin, () -> {
                        Bukkit.savePlayers();

                        Component kickMessage = LangSetting.getInstance().i18n("commands.stop.kick_message")
                                .replace("{message}", message);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.kick(kickMessage);
                        }

                        Bukkit.shutdown();
                    });
                    return;
                }

                if (ConfigSetting.getInstance().getConfig().countdown().showMessageTime().contains(countdown)) {
                    Bukkit.broadcast(LangSetting.getInstance().i18n("commands.stop.countdown")
                            .replace("{countdown}", String.valueOf(countdown)));
                }
                countdown--;
            }

            @Override
            public void cancel() {
                Stop.this.message = null;
                Stop.this.time = null;
                super.cancel();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }
}

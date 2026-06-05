package cn.chengzhimeow.mhdftools.bukkit.module.suicide.command;

import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.suicide.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.suicide.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.suicide.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class Suicide extends Command {
    public Suicide() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "自杀",
                "mhdftools.commands.suicide",
                true,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 世界黑名单
        if (ConfigSetting.getInstance().getConfig().blackWorld().contains(sender.getWorld().getName())) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().blackWorld());
            return;
        }

        // 无需确认
        if (!ConfigSetting.getInstance().getConfig().confirm()) {
            this.suicide(sender);
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().suicide().confirm());
            return;
        } else if (args.length == 1 && args[0].equals("confirm")) {
            this.suicide(sender);
            return;
        }

        // 输出帮助信息
        sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                .replace("{usage}", LangSetting.getInstance().getConfig().commands().suicide().usage())
                .replace("{command}", label));
    }

    private void suicide(Player player) {
        player.setHealth(0.0);
        player.sendMessage(LangSetting.getInstance().getConfig().commands().suicide().message());
    }
}

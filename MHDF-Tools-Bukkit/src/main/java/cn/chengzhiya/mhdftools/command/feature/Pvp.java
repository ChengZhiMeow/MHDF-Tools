package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class Pvp extends AbstractCommand {
    private final Set<UUID> nonPVPUser = new HashSet<>();

    public Pvp() {
        super(
                List.of("pvpSettings.enable"),
                "开关目标世界PVP模式",
                "mhdftools.commands.pvp",
                false,
                ConfigUtil.getConfig().getStringList("pvpSettings.commands").toArray(new String[0])
        );
    }


    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.pvp.usage"));
            return;
        }

        String value = Main.instance.getCacheManager().get("pvp", sender.getName());

        if ((value == null || value.isEmpty()) || value.contains("false")) {
            Main.instance.getCacheManager().put("pvp", sender.getName(), "true");
        } else {
            Main.instance.getCacheManager().put("pvp", sender.getName(), "false");
        }

        boolean isPVP = value != null && value.contains("true");
        TextComponent type = isPVP ? LangUtil.i18n("commands.pvp.enable") : LangUtil.i18n("commands.pvp.disable");

        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.pvp.message").replace("{type}", type));
    }
}

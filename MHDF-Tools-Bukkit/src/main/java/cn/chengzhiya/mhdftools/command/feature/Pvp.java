package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Pvp extends AbstractCommand {
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
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.pvp.usage"));
            return;
        }

        final String worldName = args[0];
        final World world = Bukkit.getWorld(worldName);

        if (world == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.pvp.worldNotExists"));
            return;
        }

        boolean currentPvp = world.getPVP();
        boolean newPvpState = !currentPvp;

        world.setPVP(newPvpState);
        String type = newPvpState ? "&c关闭" : "&a开启";
        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.pvp.message").replace("{type}", type));
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Bukkit.getWorlds().stream()
                    .map(World::getName)
                    .toList();
        }
        return new ArrayList<>();
    }
}

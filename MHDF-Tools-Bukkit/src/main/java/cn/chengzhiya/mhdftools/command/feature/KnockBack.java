package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.NickUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class KnockBack extends AbstractCommand {
    private final Random rand = new Random();

    public KnockBack() {
        super(
                List.of("knockBackSettings.enable"),
                "击退玩家",
                "mhdftools.commands.knockback",
                false,
                ConfigUtil.getConfig().getStringList("knockBackSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1 || args.length > 2) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.knockback.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        // 默认为normal
        String type = ConfigUtil.getConfig().getString("knockBackSettings.defaultType", "normal");
        String name = args[0];

        if (args.length == 2) {
            type = args[0].toLowerCase();
            name = args[1];
        }

        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
            return;
        }

        double x = ConfigUtil.getConfig().getDouble("knockBackSettings.vector.x");
        double y = ConfigUtil.getConfig().getDouble("knockBackSettings.vector.y");
        double z = ConfigUtil.getConfig().getDouble("knockBackSettings.vector.z");

        Vector vector;
        switch (type) {
            case "random" -> {
                Vector baseVector = new Vector(x, y, z);
                double magnitude = baseVector.length();
                double theta = rand.nextDouble() * 2 * Math.PI;
                double phi = rand.nextDouble() * Math.PI;
                double randX = Math.sin(phi) * Math.cos(theta);
                double randY = Math.sin(phi) * Math.sin(theta);
                double randZ = Math.cos(phi);
                vector = new Vector(randX, randY, randZ).multiply(magnitude);
            }
            case "normal" -> vector = new Vector(x, y, z);
            default -> {
                ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                        .replace("{usage}", LangUtil.i18n("commands.knockback.usage"))
                        .replace("{command}", label)
                );
                return;
            }
        }

        player.setVelocity(vector);
        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.knockback.message")
                .replace("{type}", type)
                .replace("{player}", NickUtil.getName(player))
        );
    }


    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("normal");
            completions.add("random");
            completions.addAll(Main.instance.getBungeeCordManager().getPlayerList());
        } else if (args.length == 2) {
            String firstArg = args[0].toLowerCase();
            if ("normal".equals(firstArg) || "random".equals(firstArg)) {
                completions.addAll(Main.instance.getBungeeCordManager().getPlayerList());
            }
        }
        return completions;
    }
}

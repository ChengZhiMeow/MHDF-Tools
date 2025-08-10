package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.command.Command;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

final class KnockBack extends Command {

    private final Random rand = new Random();

    public KnockBack() {
        super(
                List.of("knockBackSettings.enable"),
                "击退玩家",
                "mhdftools.commands.knockback",
                false,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("knockBackSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1 || args.length > 2) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.knockback.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        String type = Main.instance.getConfigManager().getConfigManager().getData().getString("knockBackSettings.defaultType", "normal");
        String name = args[0];

        if (args.length == 2) {
            type = args[0].toLowerCase();
            name = args[1];
        }
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("playerOffline"));
            return;
        }

        double x = Main.instance.getConfigManager().getConfigManager().getData().getDouble("knockBackSettings.vector.x");
        double y = Main.instance.getConfigManager().getConfigManager().getData().getDouble("knockBackSettings.vector.y");
        double z = Main.instance.getConfigManager().getConfigManager().getData().getDouble("knockBackSettings.vector.z");

        Vector vector;
        switch (type) {
            case "random" -> {
                Vector baseVector = new Vector(x, y, z);
                double magnitude = baseVector.length();
                double theta = this.rand.nextDouble() * 2 * Math.PI;
                double phi = this.rand.nextDouble() * Math.PI;
                double randX = Math.sin(phi) * Math.cos(theta);
                double randY = Math.sin(phi) * Math.sin(theta);
                double randZ = Math.cos(phi);
                vector = new Vector(randX, randY, randZ).multiply(magnitude);
            }
            case "normal" -> vector = new Vector(x, y, z);
            default -> {
                ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                        .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.knockback.usage"))
                        .replace("{command}", label)
                );
                return;
            }
        }

        player.setVelocity(vector);
        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.knockback.message")
                .replace("{type}", type)
                .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
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
package cn.chengzhimeow.mhdftools.bukkit.module.knockback.command;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkitAdapt;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.knockback.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.knockback.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.knockback.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

final class Knockback extends Command {
    private final Random random = new Random();

    public Knockback() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                LangSetting.getInstance().getConfig().commands().knockback().description(),
                "mhdftools.commands.knockback",
                false,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        ItemStack oldHelmet = sender.getInventory().getHelmet();
        ItemStack handItem = sender.getInventory().getItemInMainHand();

        if (handItem.getType() == Material.AIR) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().knockback().noType());
            return;
        }

        sender.getInventory().setItemInMainHand(oldHelmet);
        sender.getInventory().setHelmet(handItem);

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().knockback().message());
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1 && args.length != 2) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().knockback().usage())
                    .replace("{command}", label));
            return;
        }

        Player player = Bukkit.getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
            return;
        }

        String type = args.length == 1
                      ? ConfigSetting.getInstance().getConfig().defaultType()
                      : args[1].toLowerCase();

        double x = ConfigSetting.getInstance().getConfig().vector().x();
        double y = ConfigSetting.getInstance().getConfig().vector().y();
        double z = ConfigSetting.getInstance().getConfig().vector().z();
        Vector vector = new Vector(x, y, z);

        if (type == null || !this.knockbackPlayer(player, type, vector)) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().knockback().noType());
            return;
        }

        TextComponent typeText = switch (type) {
            case "normal" -> LangSetting.getInstance().getConfig().commands().knockback().types().normal();
            case "random" -> LangSetting.getInstance().getConfig().commands().knockback().types().random();
            default -> new TextComponent();
        };

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().knockback().message()
                .replace("{player}", MHDFToolsBukkitAdapt.adapt(player).getDisplayName())
                .replace("{type}", typeText));
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return BungeeCordManager.getInstance().getBukkitPlayerList();
        if (args.length == 2) return Arrays.asList("normal", "random");
        return super.tabCompleter(sender, label, args);
    }

    private boolean knockbackPlayer(Player player, String type, Vector vector) {
        switch (type) {
            case "normal" -> player.setVelocity(vector);
            case "random" -> {
                double theta = random.nextDouble() * 2 * Math.PI;
                double phi = random.nextDouble() * Math.PI;
                double randX = Math.sin(phi) * Math.cos(theta);
                double randY = Math.sin(phi) * Math.sin(theta);
                double randZ = Math.cos(phi);
                player.setVelocity(new Vector(randX, randY, randZ).multiply(vector.length()));
            }
            default -> {
                return false;
            }
        }
        return true;
    }
}

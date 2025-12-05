package cn.chengzhimeow.mhdftools.bukkit.module.knockback.command;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkitAdapt;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.knockback.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.knockback.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.knockback.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.knockback.util.KnockbackUtil;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

final class Knockback extends Command {
    public Knockback() {
        super(
                ModuleMain.instance,
                List.of("enable"),
                "击退玩家",
                "mhdftools.commands.knockback",
                false,
                ConfigSetting.getInstance().getData().getStringList("commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        ItemStack oldHelmet = sender.getInventory().getHelmet();
        ItemStack handItem = sender.getInventory().getItemInMainHand();

        if (handItem.getType() == Material.AIR) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.hat.no_item"));
            return;
        }

        sender.getInventory().setItemInMainHand(oldHelmet);
        sender.getInventory().setHelmet(handItem);

        sender.sendMessage(LangSetting.getInstance().i18n("commands.hat.message"));
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1 && args.length != 2) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.knockback.usage"))
                    .replace("{command}", label));
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().i18n("player_offline"));
            return;
        }

        String type = args.length == 1
                      ? ConfigSetting.getInstance().getData().getString("default_type")
                      : args[1].toLowerCase();

        double x = ConfigSetting.getInstance().getData().getDouble("vector.x");
        double y = ConfigSetting.getInstance().getData().getDouble("vector.y");
        double z = ConfigSetting.getInstance().getData().getDouble("vector.z");
        Vector vector = new Vector(x, y, z);

        if (type == null || !KnockbackUtil.knockbackPlayer(player, type, vector)) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.knockback.no_type"));
            return;
        }

        sender.sendMessage(LangSetting.getInstance().i18n("commands.knockback.message")
                .replace("{player}", MHDFToolsBukkitAdapt.adapt(player).getDisplayName())
                .replace("{type}", LangSetting.getInstance().i18n("commands.knockback.types." + type)));
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return BungeeCordManager.getInstance().getBukkitPlayerList();
        if (args.length == 2) return Arrays.asList("normal", "random");
        return super.tabCompleter(sender, label, args);
    }
}
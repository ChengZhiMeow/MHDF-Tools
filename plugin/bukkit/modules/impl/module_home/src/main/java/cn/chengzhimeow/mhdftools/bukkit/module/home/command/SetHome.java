package cn.chengzhimeow.mhdftools.bukkit.module.home.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkitAdapt;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.home.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.home.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.home.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

final class SetHome extends Command {
    public SetHome() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "设置家",
                "mhdftools.commands.sethome",
                true,
                ConfigSetting.getInstance().getConfig().setHomeCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().sethome().usage())
                    .replace("{command}", label));
            return;
        }

        if (ConfigSetting.getInstance().getConfig().blackWorld().contains(sender.getWorld().getName())) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().blackWorld());
            return;
        }

        if (!this.isValidName(args[0])) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().home().invalidName());
            return;
        }

        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        boolean exists = player.hasHome(args[0]);
        if (!ConfigSetting.getInstance().getConfig().existReplace() && exists) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().sethome().haveHome()
                    .replace("{home}", args[0]));
            return;
        }

        int maxHome = this.getMaxHome(sender);
        if (!exists && player.getHomeList().size() >= maxHome) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().sethome().isMax()
                    .replace("{amount}", String.valueOf(maxHome)));
            return;
        }

        Location location = sender.getLocation();
        player.setHome(args[0], new BungeeCordLocation(MHDFToolsBukkitAdapt.adapt(location)));

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().sethome().message()
                .replace("{home}", args[0]));
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
            return player.getHomeList().stream()
                    .map(HomeData::getHome)
                    .toList();
        }
        return new ArrayList<>();
    }

    private boolean isValidName(String name) {
        String regex = ConfigSetting.getInstance().getConfig().regex();
        return regex == null || regex.isBlank() || name.matches(regex);
    }

    private int getMaxHome(Player player) {
        List<Integer> amountList = new ArrayList<>(player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith("mhdftools.commands.home.max."))
                .map(permission -> permission.substring("mhdftools.commands.home.max.".length()))
                .filter(value -> value.matches("\\d+"))
                .map(Integer::parseInt)
                .toList());
        amountList.sort(Comparator.reverseOrder());

        return !amountList.isEmpty() ? amountList.get(0) : ConfigSetting.getInstance().getConfig().defaultMax();
    }
}

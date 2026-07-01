package cn.chengzhimeow.mhdftools.bukkit.module.tpa.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.common.message.Messager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.menu.TpaMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.message.TpaTeleportMessage;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.service.TpaService;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Tpa extends Command {
    public Tpa() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "请求传送到指定玩家位置",
                "mhdftools.commands.tpa",
                true,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (ConfigSetting.getInstance().getConfig().blackWorld().contains(sender.getWorld().getName())) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().blackWorld());
            return;
        }

        if (args.length == 0) {
            new TpaMenu(sender, 1).openInventory();
            return;
        }

        if (args.length == 1) {
            TpaService.sendRequest(sender, args[0]);
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("accept")) {
            this.accept(sender, args[1]);
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("reject")) {
            this.reject(sender, args[1]);
            return;
        }

        sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                .replace("{usage}", LangSetting.getInstance().getConfig().commands().tpa().usage())
                .replace("{command}", label));
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return BungeeCordManager.getInstance().getPlayerList();
        return new ArrayList<>();
    }

    private void accept(Player sender, String playerName) {
        String targetName = ModuleMain.instance.getRequestCache().get(playerName);
        if (targetName == null || !targetName.equals(sender.getName())) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().noRequest()
                    .replace("{player}", playerName));
            return;
        }

        ModuleMain.instance.getRequestCache().remove(playerName);

        if (!BungeeCordManager.getInstance().ifPlayerOnline(playerName)) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
            return;
        }

        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(playerName);
        MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        Player localPlayer = Bukkit.getPlayerExact(playerName);
        if (localPlayer != null) player.teleport(target);
        else
            Messager.publish(new TpaTeleportMessage(BungeeCordManager.getInstance().getServerName(), playerName, sender.getName()));

        player.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().accept().accepted()
                .replace("{player}", target.getDisplayName()));
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().accept().message()
                .replace("{player}", player.getDisplayName()));
    }

    private void reject(Player sender, String playerName) {
        String targetName = ModuleMain.instance.getRequestCache().get(playerName);
        if (targetName == null || !targetName.equals(sender.getName())) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().noRequest()
                    .replace("{player}", playerName));
            return;
        }

        ModuleMain.instance.getRequestCache().remove(playerName);

        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(playerName);
        MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        player.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().reject().rejected()
                .replace("{player}", target.getDisplayName()));
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().reject().message()
                .replace("{player}", player.getDisplayName()));
    }
}

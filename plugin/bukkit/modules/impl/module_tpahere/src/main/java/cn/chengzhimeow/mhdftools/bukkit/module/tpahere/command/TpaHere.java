package cn.chengzhimeow.mhdftools.bukkit.module.tpahere.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.menu.TpaHereMenu;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class TpaHere extends Command {
    public TpaHere() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "请求指定玩家传送到当前位置",
                "mhdftools.commands.tpahere",
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
            new TpaHereMenu(sender, 1).openInventory();
            return;
        }

        if (args.length == 1) {
            this.sendRequest(sender, args[0]);
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
                .replace("{usage}", LangSetting.getInstance().getConfig().commands().tpahere().usage())
                .replace("{command}", label));
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return BungeeCordManager.getInstance().getPlayerList();
        return new ArrayList<>();
    }

    private void sendRequest(Player sender, String targetName) {
        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        if (!BungeeCordManager.getInstance().ifPlayerOnline(targetName)) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
            return;
        }

        if (targetName.equals(sender.getName())) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpahere().sendSelf());
            return;
        }

        String delay = ModuleMain.instance.getDelayCache().get(sender.getName());
        if (delay != null) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpahere().inDelay()
                    .replace("{delay}", delay));
            return;
        }

        ModuleMain.instance.getRequestCache().put(sender.getName(), targetName);
        ModuleMain.instance.getDelayCache().put(sender.getName(), String.valueOf(ConfigSetting.getInstance().getConfig().delay()));
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpahere().message()
                .replace("{player}", targetName));

        MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(targetName);
        if (target.isIgnore(player)) return;

        target.sendMessage(LangSetting.getInstance().getConfig().commands().tpahere().requestMessage()
                .replaceByMiniMessage("{player}", sender.getName()));
    }

    private void accept(Player sender, String playerName) {
        String targetName = ModuleMain.instance.getRequestCache().get(playerName);
        if (targetName == null || !targetName.equals(sender.getName())) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpahere().noRequest()
                    .replace("{player}", playerName));
            return;
        }

        ModuleMain.instance.getRequestCache().remove(playerName);
        ModuleMain.instance.getDelayCache().remove(playerName);

        if (!BungeeCordManager.getInstance().ifPlayerOnline(playerName)) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
            return;
        }

        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(playerName);
        MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        target.teleport(player);
        player.sendMessage(LangSetting.getInstance().getConfig().commands().tpahere().accept().accepted()
                .replace("{player}", target.getDisplayName()));
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpahere().accept().message()
                .replace("{player}", player.getDisplayName()));
    }

    private void reject(Player sender, String playerName) {
        String targetName = ModuleMain.instance.getRequestCache().get(playerName);
        if (targetName == null || !targetName.equals(sender.getName())) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpahere().noRequest()
                    .replace("{player}", playerName));
            return;
        }

        ModuleMain.instance.getRequestCache().remove(playerName);
        ModuleMain.instance.getDelayCache().remove(playerName);

        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(playerName);
        MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        player.sendMessage(LangSetting.getInstance().getConfig().commands().tpahere().reject().rejected()
                .replace("{player}", target.getDisplayName()));
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().tpahere().reject().message()
                .replace("{player}", player.getDisplayName()));
    }
}

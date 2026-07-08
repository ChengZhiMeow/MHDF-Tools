package cn.chengzhimeow.mhdftools.bukkit.module.list.command;

import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.list.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.list.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.list.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.list.service.ListService;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class List extends Command {
    public List() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "查看在线列表",
                "mhdftools.commands.list",
                false,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().list().usage())
                    .replace("{command}", label));
            return;
        }

        boolean getPlayerListFromBungeeCord = ConfigSetting.getInstance().getConfig().getPlayerListFromBungeecord();
        java.util.List<String> playerList = getPlayerListFromBungeeCord
                                            ? BungeeCordManager.getInstance().getPlayerList()
                                            : BungeeCordManager.getInstance().getBukkitPlayerList();

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().list().message()
                .replace("{tps}", String.valueOf(ListService.getTps(sender instanceof Player player ? player.getLocation() : null)))
                .replace("{memory}", String.valueOf(ListService.getUsedMemory()))
                .replace("{max_memory}", String.valueOf(ListService.getTotalMemory()))
                .replace("{player_count}", String.valueOf(playerList.size()))
                .replace("{max_player_count}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{player_list}", playerList.toString()));
    }
}

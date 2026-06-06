package cn.chengzhimeow.mhdftools.bukkit.module.chat.listener;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.common.message.Messager;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.cache.DisplayCache;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.message.ChatBroadcastMessage;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.service.AtService;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.service.ChatService;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final class Chat extends Listener {
    public Chat() {
        super(ModuleMain.instance, ConfigSetting.getInstance().getConfig().enable());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        MHDFToolsPlayer sender = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
        ConfigSetting.Config config = ConfigSetting.getInstance().getConfig();
        String message = ChatService.sanitize(player, event.getMessage());

        if (config.delay().enable() && !player.hasPermission("mhdftools.bypass.chat.delay")) {
            long delay = ModuleMain.instance.getChatDelayCache().remainingSeconds(player.getName());
            if (delay > 0L) {
                player.sendMessage(LangSetting.getInstance().getConfig().chat().delay().replace("{delay}", String.valueOf(delay)));
                event.setCancelled(true);
                return;
            }
        }

        if (config.spam() && !player.hasPermission("mhdftools.bypass.chat.spam") && ModuleMain.instance.getLastChatCache().isSpam(player.getName(), message)) {
            player.sendMessage(LangSetting.getInstance().getConfig().chat().spam());
            event.setCancelled(true);
            return;
        }

        ModuleMain.instance.getChatDelayCache().put(player.getName(), config.delay().delay());
        ModuleMain.instance.getLastChatCache().put(player.getName(), message);

        TextComponent messageComponent = ColorUtil.color(message);
        messageComponent = ChatService.applyReplaceWord(player, messageComponent, message);

        List<byte[]> cacheDataList = new ArrayList<>();
        messageComponent = ChatService.applyShowItem(player, messageComponent, cacheDataList);
        messageComponent = ChatService.applyShowableContainer(sender, messageComponent, cacheDataList, config.showInventory(), DisplayCache.Type.INVENTORY, player.getInventory().getContents());
        messageComponent = ChatService.applyShowableContainer(sender, messageComponent, cacheDataList, config.showEnderChest(), DisplayCache.Type.ENDER_CHEST, player.getEnderChest().getContents());

        Set<String> atList = AtService.getAtList(player, message);
        messageComponent = ChatService.applyAt(messageComponent, message, atList);

        TextComponent formatMessage = ChatService.formatMessage(player, sender, messageComponent);
        List<String> sortedAtList = atList.stream().sorted().toList();

        Messager.send(
                new ChatBroadcastMessage(
                        BungeeCordManager.getInstance().getServerName(),
                        player.getName(),
                        formatMessage.toMiniMessageString(),
                        sortedAtList,
                        cacheDataList
                ),
                () -> {
                    Bukkit.getConsoleSender().sendMessage(formatMessage);
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        MHDFToolsPlayer targetPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(target.getUniqueId(), target.getName());
                        if (targetPlayer.isIgnore(sender)) continue;
                        target.sendMessage(formatMessage);
                        AtService.at(target, player.getName(), sortedAtList);
                    }
                }
        );

        event.setCancelled(true);
    }
}

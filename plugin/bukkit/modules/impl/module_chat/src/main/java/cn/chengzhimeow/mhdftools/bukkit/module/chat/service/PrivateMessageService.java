package cn.chengzhimeow.mhdftools.bukkit.module.chat.service;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.common.message.Messager;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.cache.DisplayCache;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.message.ChatPrivateMessage;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class PrivateMessageService {
    public static String replyTarget(String sender) {
        return ModuleMain.instance.getReplyTargetCache().get(sender);
    }

    public static void sendMsg(CommandSender sender, String target, String message) {
        String cleanMessage = ChatService.sanitize(sender, message);
        TextComponent component = ColorUtil.color(cleanMessage);
        component = ChatService.applyReplaceWord(sender, component, cleanMessage);
        List<byte[]> cacheDataList = new ArrayList<>();
        if (sender instanceof Player player) {
            MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
            ConfigSetting.Config config = ConfigSetting.getInstance().getConfig();
            component = ChatService.applyShowItem(player, component, cacheDataList);
            component = ChatService.applyShowableContainer(mhdfPlayer, component, cacheDataList, config.showInventory(), DisplayCache.Type.INVENTORY, player.getInventory().getContents());
            component = ChatService.applyShowableContainer(mhdfPlayer, component, cacheDataList, config.showEnderChest(), DisplayCache.Type.ENDER_CHEST, player.getEnderChest().getContents());
        }

        ModuleMain.instance.getReplyTargetCache().bind(sender.getName(), target);

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().msg().send()
                .replace("{player}", sender.getName())
                .replace("{target}", target)
                .replace("{message}", component));

        TextComponent receive = LangSetting.getInstance().getConfig().commands().msg().receive()
                .replace("{player}", sender.getName())
                .replace("{target}", target)
                .replace("{message}", component);

        Player targetPlayer = Bukkit.getPlayerExact(target);
        if (targetPlayer != null) {
            targetPlayer.sendMessage(receive);
            return;
        }

        if (BungeeCordManager.getInstance().ifPlayerOnline(target)) {
            Messager.publish(new ChatPrivateMessage(
                    BungeeCordManager.getInstance().getServerName(),
                    target,
                    receive.toMiniMessageString(),
                    cacheDataList
            ));
        }
    }

    private PrivateMessageService() {
    }
}

package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.AtUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.ChatUtil;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

final class Chat extends Listener {
    public Chat() {
        super(
                List.of("chatSettings.enable")
        );
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        ConfigurationSection config = ConfigSetting.getInstance().getData().getConfigurationSection("chatSettings");
        if (config == null) {
            return;
        }

        // 聊天延迟
        if (config.getBoolean("delay.enable")) {
            if (!player.hasPermission("mhdftools.bypass.chat.delay")) {
                String delayData = Main.instance.getCacheManager().get("chatDelay", player.getName());
                if (delayData != null) {
                    player.sendMessage(LangSetting.getInstance().i18n("chat.delay")
                            .replace("{delay}", delayData)
                    );
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // 限制使用颜色符号
        if (!player.hasPermission("mhdftools.chat.color")) {
            message = ChatColor.stripColor(ColorUtil.legacy(message));
        }

        // 限制使用miniMessage
        if (!player.hasPermission("mhdftools.chat.minimessage")) {
            Pattern pattern = Pattern.compile("%/?\\[\\^]+%");
            message = pattern.matcher(message).replaceAll("");
        }

        // 刷屏限制
        if (config.getBoolean("spam.enable")) {
            if (!player.hasPermission("mhdftools.bypass.chat.spam")) {
                String spamData = Main.instance.getCacheManager().get("lastChat", player.getName());
                if (spamData != null && spamData.equals(message)) {
                    player.sendMessage(LangSetting.getInstance().i18n("chat.spam"));
                    event.setCancelled(true);
                    return;
                }
            }
        }

        TextComponent messageComponent = ColorUtil.color(message);

        int delay = config.getInt("delay.delay");
        Main.instance.getCacheManager().put("chatDelay", player.getName(), String.valueOf(delay));
        Main.instance.getCacheManager().put("lastChat", player.getName(), message);

        // 替换词
        messageComponent = ChatUtil.applyReplaceWord(player, messageComponent, message);

        // 展示物品、背包、末影箱
        messageComponent = ChatUtil.applyShowItem(player, messageComponent);
        messageComponent = ChatUtil.applyShowInventory(player, messageComponent);
        messageComponent = ChatUtil.applyShowEnderChest(player, messageComponent);

        // AT玩家
        if (config.getBoolean("at.enable")) {
            Set<String> atList = AtUtil.getAtList(player, message);
            messageComponent = ChatUtil.applyAt(messageComponent, message, atList);
            Main.instance.getBungeeCordManager().atList(atList, player.getName());
        }

        // 发送消息
        TextComponent formatMessage = ChatUtil.formatMessage(player, messageComponent);
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        Main.instance.getBungeeCordManager().sendMessage(
                "console",
                formatMessage
        );
        for (String target : Main.instance.getBungeeCordManager().getPlayerList()) {
            if (ConfigSetting.getInstance().getData().getBoolean("ignoreSettings.enable")) {
                MHDFToolsPlayer mhdfTargetPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(target);
                if (mhdfTargetPlayer.isIgnore(mhdfPlayer)) {
                    continue;
                }
            }

            Main.instance.getBungeeCordManager().sendMessage(
                    target,
                    formatMessage
            );
        }

        event.setCancelled(true);
    }
}

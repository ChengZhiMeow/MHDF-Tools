package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.IgnoreDataUtil;
import cn.chengzhiya.mhdftools.util.feature.AtUtil;
import cn.chengzhiya.mhdftools.util.feature.ChatUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class Chat extends AbstractListener {
    public Chat() {
        super(
                List.of("chatSettings.enable")
        );
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("chatSettings");
        if (config == null) {
            return;
        }

        // 聊天延迟
        if (config.getBoolean("delay.enable")) {
            if (!player.hasPermission("mhdftools.bypass.chat.delay")) {
                String delayData = Main.instance.getCacheManager().get("chatDelay", player.getName());
                if (delayData != null) {
                    ActionUtil.sendMessage(player, LangUtil.i18n("chat.delay")
                            .replace("{delay}", delayData)
                    );
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // 限制使用颜色符号
        if (!player.hasPermission("mhdftools.chat.color")) {
            message = ChatColor.stripColor(ColorUtil.legacyColor(message));
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
                    ActionUtil.sendMessage(player, LangUtil.i18n("chat.spam"));
                    event.setCancelled(true);
                    return;
                }
            }
        }

        int delay = config.getInt("delay.delay");
        Main.instance.getCacheManager().put("chatDelay", player.getName(), String.valueOf(delay));
        Main.instance.getCacheManager().put("lastChat", player.getName(), message);

        // 替换词
        if (!player.hasPermission("mhdftools.bypass.chat.replaceWord")) {
            message = ChatUtil.applyBlackWord(message);
        }

        // 展示物品
        message = ChatUtil.applyShowItem(player, message);

        // 展示背包
        message = ChatUtil.applyShowInventory(player, message);

        // 展示末影箱
        message = ChatUtil.applyShowEnderChest(player, message);

        // AT玩家
        if (config.getBoolean("at.enable")) {
            Set<String> atList = AtUtil.getAtList(player, message);
            message = ChatUtil.applyAt(message, atList);
            Main.instance.getBungeeCordManager().atList(atList, player.getName());
        }

        // 发送消息
        String formatMessage = ChatUtil.getFormatMessage(player, message);

        Main.instance.getBungeeCordManager().sendMessage(
                "console",
                formatMessage
        );
        for (String target : Main.instance.getBungeeCordManager().getPlayerList()) {
            if (config.getBoolean("ignore.enable")) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
                if (IgnoreDataUtil.isIgnore(targetPlayer, player)) {
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

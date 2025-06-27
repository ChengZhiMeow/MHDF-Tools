package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.regex.Pattern;

public final class MsgUtil {
    /**
     * 发送私聊消息
     *
     * @param sender  命令执行着实例
     * @param target  目标玩家名称
     * @param message 消息
     */
    public static void sendMsg(CommandSender sender, String target, String message) {
        // 聊天延迟
        if (ConfigUtil.getConfig().getBoolean("chatSettings.delay.enable")) {
            if (!sender.hasPermission("mhdftools.bypass.chat.delay")) {
                String delayData = Main.instance.getCacheManager().get("chatDelay", sender.getName());
                if (delayData != null) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("chat.delay")
                            .replace("{delay}", delayData)
                    );
                    return;
                }
            }
        }

        // 限制使用颜色符号
        if (!sender.hasPermission("mhdftools.chat.color")) {
            message = ChatColor.stripColor(ColorUtil.legacyColor(message));
        }

        // 限制使用miniMessage
        if (!sender.hasPermission("mhdftools.chat.minimessage")) {
            Pattern pattern = Pattern.compile("</?[a-zA-Z0-9_:-]+>");
            message = pattern.matcher(message).replaceAll("");
        }

        // 刷屏限制
        if (ConfigUtil.getConfig().getBoolean("chatSettings.spam.enable")) {
            if (!sender.hasPermission("mhdftools.bypass.chat.spam")) {
                String spamData = Main.instance.getCacheManager().get("lastChat", sender.getName());
                if (spamData != null && spamData.equals(message)) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("chat.spam"));
                    return;
                }
            }
        }

        int delay = ConfigUtil.getConfig().getInt("chatSettings.delay.delay");
        Main.instance.getCacheManager().put("chatDelay", sender.getName(), String.valueOf(delay));
        Main.instance.getCacheManager().put("lastChat", sender.getName(), message);

        // 替换词
        if (!sender.hasPermission("mhdftools.bypass.chat.replaceWord")) {
            message = ChatUtil.applyBlackWord(message);
        }

        Main.instance.getCacheManager().put("reply", sender.getName(), target);
        Main.instance.getCacheManager().put("reply", target, sender.getName());

        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.msg.send")
                .replace("{player}", sender.getName())
                .replace("{target}", target)
                .replace("{message}", message)
        );
        Main.instance.getBungeeCordManager().sendMessage(target, LangUtil.i18n("commands.msg.receive")
                .replace("{player}", sender.getName())
                .replace("{target}", target)
                .replace("{message}", message)
        );
    }
}

package cn.chengzhimeow.mhdftools.bukkit.util.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.text.TextComponent;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.message.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (ConfigSetting.getSettingInstance().getData().getBoolean("chatSettings.delay.enable")) {
            if (!sender.hasPermission("mhdftools.bypass.chat.delay")) {
                String delayData = Main.instance.getCacheManager().get("chatDelay", sender.getName());
                if (delayData != null) {
                    ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("chat.delay")
                            .replace("{delay}", delayData)
                    );
                    return;
                }
            }
        }

        // 限制使用颜色符号
        if (!sender.hasPermission("mhdftools.chat.color")) {
            message = ChatColor.stripColor(ColorUtil.legacy(message));
        }

        // 限制使用miniMessage
        if (!sender.hasPermission("mhdftools.chat.minimessage")) {
            Pattern pattern = Pattern.compile("</?[a-zA-Z0-9_:-]+>");
            message = pattern.matcher(message).replaceAll("");
        }

        // 刷屏限制
        if (ConfigSetting.getSettingInstance().getData().getBoolean("chatSettings.spam.enable")) {
            if (!sender.hasPermission("mhdftools.bypass.chat.spam")) {
                String spamData = Main.instance.getCacheManager().get("lastChat", sender.getName());
                if (spamData != null && spamData.equals(message)) {
                    ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("chat.spam"));
                    return;
                }
            }
        }

        int delay = ConfigSetting.getSettingInstance().getData().getInt("chatSettings.delay.delay");
        Main.instance.getCacheManager().put("chatDelay", sender.getName(), String.valueOf(delay));
        Main.instance.getCacheManager().put("lastChat", sender.getName(), message);

        TextComponent messageComponent = ColorUtil.color(message);

        // 替换词
        messageComponent = ChatUtil.applyReplaceWord(sender, messageComponent, message);

        // 展示物品、背包、末影箱
        if (sender instanceof Player player) {
            messageComponent = ChatUtil.applyShowItem(player, messageComponent);
            messageComponent = ChatUtil.applyShowInventory(player, messageComponent);
            messageComponent = ChatUtil.applyShowEnderChest(player, messageComponent);
        }

        Main.instance.getCacheManager().put("reply", sender.getName(), target);
        Main.instance.getCacheManager().put("reply", target, sender.getName());

        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.msg.send")
                .replace("{player}", sender.getName())
                .replace("{target}", target)
                .replace("{message}", message)
        );
        Main.instance.getBungeeCordManager().sendMessage(target, LangSetting.getSettingInstance().i18n("commands.msg.receive")
                .replace("{player}", sender.getName())
                .replace("{target}", target)
                .replace("{message}", message)
        );
    }
}

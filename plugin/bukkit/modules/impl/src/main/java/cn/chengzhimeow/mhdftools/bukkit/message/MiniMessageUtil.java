package cn.chengzhimeow.mhdftools.bukkit.message;

import cn.chengzhimeow.mhdftools.bukkit.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class MiniMessageUtil {
    /**
     * miniMessage格式化
     *
     * @param message 文本
     * @return 文本实例
     */
    public static TextComponent miniMessage(String message) {
        return new TextComponent(MiniMessage.miniMessage().deserialize(message));
    }
}

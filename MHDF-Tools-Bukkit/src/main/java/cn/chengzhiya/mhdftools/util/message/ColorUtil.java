package cn.chengzhiya.mhdftools.util.message;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.util.PluginUtil;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

public final class ColorUtil {
    /**
     * 旧版彩色符号(例如: &e)处理
     *
     * @param message 文本
     * @return 处理后的文本
     */
    private static String legacy(@NotNull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * 处理旧版颜色符号
     *
     * @param message 文本
     * @return 处理后的文本
     */
    public static String legacyColor(@NotNull String message) {
        message = message
                .replace("{prefix}", Main.instance.getConfigManager().getLangManager().getString("prefix"))
                .replace("{version}", PluginUtil.getVersion());
        return legacy(message);
    }

    /**
     * 将旧版RGB颜色字符文本转换为miniMessage格式
     *
     * @param legacy 旧版颜色字符文本
     * @return miniMessage格式文本
     */
    public static String legacyColorToMiniMessage(@NotNull String legacy) {
        legacy = legacy.replace("&#", "#");
        return legacy.replaceAll("(?!:)(?<!<)#([0-9a-fA-F]{6})(?!>)(?!:)", "<#$1>");
    }

    /**
     * 将旧版颜色字符文本转换为miniMessage格式
     *
     * @param legacy 旧版颜色字符文本
     * @return miniMessage格式文本
     */
    public static String legacyToMiniMessage(@NotNull String legacy) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = legacy.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (isColorCode(chars[i])) {
                stringBuilder.append(chars[i]);
                continue;
            }
            if (i + 1 >= chars.length) {
                stringBuilder.append(chars[i]);
                continue;
            }
            switch (chars[i + 1]) {
                case '0' -> stringBuilder.append("<black>");
                case '1' -> stringBuilder.append("<dark_blue>");
                case '2' -> stringBuilder.append("<dark_green>");
                case '3' -> stringBuilder.append("<dark_aqua>");
                case '4' -> stringBuilder.append("<dark_red>");
                case '5' -> stringBuilder.append("<dark_purple>");
                case '6' -> stringBuilder.append("<gold>");
                case '7' -> stringBuilder.append("<gray>");
                case '8' -> stringBuilder.append("<dark_gray>");
                case '9' -> stringBuilder.append("<blue>");
                case 'a' -> stringBuilder.append("<green>");
                case 'b' -> stringBuilder.append("<aqua>");
                case 'c' -> stringBuilder.append("<red>");
                case 'd' -> stringBuilder.append("<light_purple>");
                case 'e' -> stringBuilder.append("<yellow>");
                case 'f' -> stringBuilder.append("<white>");
                case 'r' -> stringBuilder.append("<reset>");
                case 'l' -> stringBuilder.append("<b>");
                case 'm' -> stringBuilder.append("<st>");
                case 'o' -> stringBuilder.append("<i>");
                case 'n' -> stringBuilder.append("<u>");
                case 'k' -> stringBuilder.append("<obf>");
                case 'x' -> {
                    if (i + 13 >= chars.length
                            || isColorCode(chars[i + 2])
                            || isColorCode(chars[i + 4])
                            || isColorCode(chars[i + 6])
                            || isColorCode(chars[i + 8])
                            || isColorCode(chars[i + 10])
                            || isColorCode(chars[i + 12])) {
                        stringBuilder.append(chars[i]);
                        continue;
                    }
                    stringBuilder
                            .append("<#")
                            .append(chars[i + 3])
                            .append(chars[i + 5])
                            .append(chars[i + 7])
                            .append(chars[i + 9])
                            .append(chars[i + 11])
                            .append(chars[i + 13])
                            .append(">");
                    i += 12;
                }
                default -> {
                    stringBuilder.append(chars[i]);
                    continue;
                }
            }
            i++;
        }
        return stringBuilder.toString();
    }

    /**
     * 检测字符是否是颜色代码的字符
     *
     * @param c 字符
     * @return 结果
     */
    public static boolean isColorCode(char c) {
        return c != '§' && c != '&';
    }

    /**
     * 将旧版颜色符号转换为minimessage颜色符号
     *
     * @param message 文本
     * @return 处理后的文本
     */
    public static String miniMessage(@NotNull String message) {
        return legacyColorToMiniMessage(legacyToMiniMessage(legacyColor(message)));
    }

    /**
     * 处理miniMessage颜色符号
     *
     * @param message 文本
     * @return 处理后的文本
     */
    public static TextComponent color(@NotNull String message) {
        return new TextComponent(MiniMessageUtil.miniMessage("<!i>" + miniMessage(message)));
    }
}

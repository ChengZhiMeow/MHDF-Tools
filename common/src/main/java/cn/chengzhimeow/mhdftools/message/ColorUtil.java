package cn.chengzhimeow.mhdftools.message;

import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.jetbrains.annotations.NotNull;

public final class ColorUtil {

    /**
     * 检测字符是否是颜色代码的字符
     *
     * @param c 字符
     * @return 结果
     */
    private static boolean notColorCode(char c) {
        return c != '§' && c != '&';
    }

    /**
     * 将旧版颜色字符文本转换为miniMessage格式
     *
     * @param legacy 旧版颜色字符文本
     * @return miniMessage格式文本
     */
    private static String legacyToMiniMessage(@NotNull String legacy) {
        StringBuilder stringBuilder = new StringBuilder("<!i>");
        char[] chars = legacy.toCharArray();

        boolean notOnlyHex = true;
        for (int i = 0; i < chars.length; i++) {
            if (i + 1 >= chars.length) {
                stringBuilder.append(chars[i]);
                continue;
            }

            if (notOnlyHex && i + 6 < chars.length && chars[i] == '#') {
                stringBuilder
                        .append("<#")
                        .append(chars[i + 1])
                        .append(chars[i + 2])
                        .append(chars[i + 3])
                        .append(chars[i + 4])
                        .append(chars[i + 5])
                        .append(chars[i + 6])
                        .append(">");
                i += 6;
                continue;
            }
            notOnlyHex = notColorCode(chars[i]) && chars[i] != '<';

            if (ColorUtil.notColorCode(chars[i])) {
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
                case '#' -> {
                    if (i + 7 < chars.length) {
                        stringBuilder
                                .append("<#")
                                .append(chars[i + 2])
                                .append(chars[i + 3])
                                .append(chars[i + 4])
                                .append(chars[i + 5])
                                .append(chars[i + 6])
                                .append(chars[i + 7])
                                .append(">");
                        i += 6;
                    }
                }
                case 'x' -> {
                    if (i + 13 >= chars.length
                            || ColorUtil.notColorCode(chars[i + 2])
                            || ColorUtil.notColorCode(chars[i + 4])
                            || ColorUtil.notColorCode(chars[i + 6])
                            || ColorUtil.notColorCode(chars[i + 8])
                            || ColorUtil.notColorCode(chars[i + 10])
                            || ColorUtil.notColorCode(chars[i + 12])
                    ) {
                        stringBuilder.append(chars[i]);
                        i++;
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
                    i++;
                    continue;
                }
            }
            i++;
        }
        return stringBuilder.toString();
    }

    /**
     * 处理miniMessage颜色符号
     *
     * @param message 文本
     * @return 处理后的文本
     */
    public static TextComponent color(String message) {
        if (message == null) return new TextComponent();
        return new TextComponent(MiniMessageUtil.miniMessage(ColorUtil.legacyToMiniMessage(message)));
    }
}

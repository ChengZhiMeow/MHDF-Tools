package cn.chengzhiya.mhdftools.util.message;

public final class MessageUtil {
    /**
     * 拼接数组文本
     *
     * @param strings 文本数组
     * @param origin  数组起点
     * @param append  拼接内容
     * @return 拼接后的文本
     */
    public static String mergeString(String[] strings, int origin, String append) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = origin; i < strings.length; i++) {
            stringBuilder.append(strings[i]);
            if (i < strings.length - 1) {
                stringBuilder.append(append);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 拼接数组文本
     *
     * @param strings 文本数组
     * @param append  拼接内容
     * @return 拼接后的文本
     */
    public static String mergeString(String[] strings, String append) {
        return MessageUtil.mergeString(strings, 0, append);
    }

    /**
     * 拼接数组文本
     *
     * @param strings 文本数组
     * @param origin  数组起点
     * @return 拼接后的文本
     */
    public static String mergeString(String[] strings, int origin) {
        return MessageUtil.mergeString(strings, origin, " ");
    }

    /**
     * 拼接数组文本
     *
     * @param strings 文本数组
     * @return 拼接后的文本
     */
    public static String mergeString(String[] strings) {
        return MessageUtil.mergeString(strings, 0, " ");
    }

    /**
     * 格式化文本
     *
     * @param string 文本
     * @param args   参数
     * @return 格式化后的文本
     */
    public static String formatString(String string, String[] args) {
        for (Object var : args) {
            string = string.replaceFirst("\\{}", var.toString());
        }

        return string;
    }

    /**
     * 分割文本
     *
     * @param string 文本
     * @param split  从哪个文本开始分割
     * @param end    分割结束位置
     * @return 分割后的文本
     */
    public static String subString(String string, String split, int end) {
        int index = string.indexOf(split);
        if (index == -1) {
            return string;
        }
        return string.substring(index + split.length(), end);
    }

    /**
     * 分割文本
     *
     * @param string 文本
     * @param split  从哪个文本开始分割
     * @return 分割后的文本
     */
    public static String subString(String string, String split) {
        return MessageUtil.subString(string, split, string.length());
    }
}

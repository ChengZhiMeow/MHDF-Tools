package cn.chengzhimeow.mhdftools.message;

public final class StringUtil {
    /**
     * 拼接数组文本
     *
     * @param strings 文本数组
     * @param origin  数组起点
     * @param append  拼接内容
     * @return 拼接后的文本
     */
    public static String join(String[] strings, int origin, String append) {
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
    public static String join(String[] strings, String append) {
        return StringUtil.join(strings, 0, append);
    }

    /**
     * 拼接数组文本
     *
     * @param strings 文本数组
     * @param origin  数组起点
     * @return 拼接后的文本
     */
    public static String join(String[] strings, int origin) {
        return StringUtil.join(strings, origin, " ");
    }

    /**
     * 拼接数组文本
     *
     * @param strings 文本数组
     * @return 拼接后的文本
     */
    public static String join(String[] strings) {
        return StringUtil.join(strings, 0, " ");
    }

    /**
     * 格式化文本
     *
     * @param string 文本
     * @param args   参数
     * @return 格式化后的文本
     */
    public static String format(String string, String[] args) {
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
        return StringUtil.subString(string, split, string.length());
    }
}

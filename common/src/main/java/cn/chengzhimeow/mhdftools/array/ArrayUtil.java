package cn.chengzhimeow.mhdftools.array;

public final class ArrayUtil {
    /***
     * 拼接数组为字符串
     *
     * @param array 数组
     * @param index 开始索引
     * @param spilt 分割符
     * @return 拼接后的字符串
     */
    public static String join(Object[] array, int index, String spilt) {
        StringBuilder sb = new StringBuilder();
        int end = array.length;
        for (int i = index; i < end; i++) {
            Object element = array[i];
            sb.append(element);
            if (i < end - 1) sb.append(spilt);
        }
        return sb.toString();
    }
}

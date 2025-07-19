package cn.chengzhiya.mhdftools.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SuppressWarnings("unused")
public final class Base64Util {
    /**
     * base64编码
     *
     * @param bytes 被编码的数据
     * @return base64字符串
     */
    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * base64编码
     *
     * @param string 被编码的文本
     * @return base64字符串
     */
    public static String encode(String string) {
        return Base64Util.encode(string.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * base64解码
     *
     * @param bytes 被解码的数据
     * @return 原文本
     */
    public static byte[] decode(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    /**
     * base64解码
     *
     * @param string base64字符串
     * @return 原文本
     */
    public static byte[] decode(String string) {
        return Base64Util.decode(string.getBytes(StandardCharsets.UTF_8));
    }
}

package cn.chengzhimeow.mhdftools.console;

public abstract class LogManager {
    public static final String CONSOLE_PREFIX = "[MHDF-Tools] ";
    public static final String DEBUG_PREFIX = "[MHDF-Tools-调试] ";
    public static LogManager instance;

    /**
     * 日志消息
     *
     * @param message 文本
     * @param args 参数
     */
    public abstract void log(String message, String... args);

    /**
     * 调试消息
     *
     * @param message 文本
     * @param args 参数
     */
    public abstract void debug(String message, String... args);
}

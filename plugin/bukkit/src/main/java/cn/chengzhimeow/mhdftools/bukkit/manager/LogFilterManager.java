package cn.chengzhimeow.mhdftools.bukkit.manager;

import cn.chengzhimeow.mhdftools.bukkit.logger.LogFilter;
import com.j256.ormlite.logger.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public final class LogFilterManager {
    /**
     * 初始化日志过滤器
     */
    public void init() {
        // 关闭 ormLite 日志
        com.j256.ormlite.logger.Logger.setGlobalLogLevel(Level.OFF);

        // 关闭其他库日志
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(new LogFilter());
    }
}

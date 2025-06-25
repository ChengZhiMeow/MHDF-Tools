package cn.chengzhiya.mhdftools.manager.database;

import com.j256.ormlite.jdbc.DataSourceConnectionSource;

public interface DatabaseManager {
    /**
     * 连接并初始化数据库
     */
    void connect();

    /**
     * 关闭数据库连接源
     */
    void close();

    /**
     * 获取数据库连接实例
     *
     * @return 数据库连接实例
     */
    DataSourceConnectionSource getConnectionSource();

    /**
     * 初始化表
     */
    default void initTable() {
    }
}

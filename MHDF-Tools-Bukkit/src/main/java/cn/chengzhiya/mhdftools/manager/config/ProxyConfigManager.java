package cn.chengzhiya.mhdftools.manager.config;

import cn.chengzhiya.mhdftools.util.config.ProxyUtil;
import lombok.SneakyThrows;

public final class ProxyConfigManager {
    /**
     * 初始化代理配置文件
     */
    @SneakyThrows
    public void init() {
        ProxyUtil.saveDefaultProxy();
        ProxyUtil.reloadProxy();
    }
}

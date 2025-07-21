package cn.chengzhiya.mhdftools.manager.config;

import lombok.Getter;

@Getter
public final class ConfigMainManager {
    private final LibraryManager libraryManager = new LibraryManager();
    private final ConfigManager configManager = new ConfigManager();
    private final LangManager langManager = new LangManager();
    private final SoundManager soundManager = new SoundManager();
    private final ProxyManager proxyManager = new ProxyManager();
    private final MenuManager menuManager = new MenuManager();
    private final CustomMenuManager customMenuManager = new CustomMenuManager();

    /**
     * 初始化配置文件
     */
    public void init() {
        this.saveDefault();
        this.update();
        this.reload();
    }

    /**
     * 保存默认配置文件
     */
    public void saveDefault() {
        this.getLibraryManager().saveDefaultFile();
        this.getConfigManager().saveDefaultFile();
        this.getLangManager().saveDefaultFile();
        this.getSoundManager().saveDefaultFile();
        this.getProxyManager().saveDefaultFile();
        this.getMenuManager().saveDefaultFile();
        this.getCustomMenuManager().saveDefaultFile();
    }

    /**
     * 更新配置文件
     */
    public void update() {
        this.getLibraryManager().update();
        this.getConfigManager().update();
        this.getLangManager().update();
        this.getSoundManager().update();
        this.getProxyManager().update();
    }

    /**
     * 重载配置文件
     */
    public void reload() {
        this.getLibraryManager().reload();
        this.getConfigManager().reload();
        this.getLangManager().reload();
        this.getSoundManager().reload();
        this.getProxyManager().reload();
        this.getMenuManager().reload();
        this.getCustomMenuManager().reload();
    }
}

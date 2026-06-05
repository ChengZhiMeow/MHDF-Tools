package cn.chengzhimeow.mhdftools.library;

import cn.chengzhimeow.mhdftools.config.ConfigManager;
import cn.chengzhimeow.mhdftools.config.impl.LibrarySetting;
import cn.chengzhimeow.mhdftools.config.impl.ProxySetting;
import cn.chengzhiya.mhdflibrary.MHDFLibrary;
import cn.chengzhiya.mhdflibrary.entity.DependencyConfig;
import cn.chengzhiya.mhdflibrary.manager.LoggerManager;
import lombok.Setter;

import java.io.File;

public final class LibraryManager {
    private static LibraryManager instance;

    public static LibraryManager getInstance() {
        if (LibraryManager.instance == null) LibraryManager.instance = new LibraryManager();
        return LibraryManager.instance;
    }

    @Setter
    private LoggerManager loggerManager;

    private LibraryManager() {
    }

    public void init() {
        MHDFLibrary mhdfLibrary = new MHDFLibrary(
                LibraryManager.class,
                this.loggerManager,
                "cn.chengzhimeow.mhdftools.libs",
                new File(ConfigManager.getInstance().getDataFolder(), "libs")
        );
        mhdfLibrary.getHttpManager().setProxy(ProxySetting.getInstance().getProxy());

        for (DependencyConfig config : LibrarySetting.getInstance().getConfig().libraries()) {
            mhdfLibrary.addDependencyConfig(config);
        }

        mhdfLibrary.downloadDependencies();
        mhdfLibrary.loadDependencies();
    }
}

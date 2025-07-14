package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdflibrary.MHDFLibrary;
import cn.chengzhiya.mhdflibrary.entity.DependencyConfig;
import cn.chengzhiya.mhdflibrary.entity.RelocateConfig;
import cn.chengzhiya.mhdflibrary.entity.RepositoryConfig;
import cn.chengzhiya.mhdflibrary.manager.LoggerManager;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.PluginUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.ProxyUtil;

import java.io.File;

@SuppressWarnings("unused")
public final class LibrariesManager {
    private final RepositoryConfig chengzhiMeow = new RepositoryConfig("https://maven.chengzhimeow.cn/releases/");
    private final RepositoryConfig codemc = new RepositoryConfig("https://repo.codemc.io/repository/maven-public/");

    /**
     * 下载并加载所有所需依赖
     */
    public void init() {
        MHDFLibrary mhdfLibrary = new MHDFLibrary(
                Main.class,
                new LibraryLoggerManager(),
                "cn.chengzhiya.mhdftools.libs",
                new File(ConfigUtil.getDataFolder(), "libs")
        );
        mhdfLibrary.getHttpManager().setProxy(ProxyUtil.getProxy());

        // 依赖
        {
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("cn{}chengzhiya"),
                    "MHDF-Scheduler",
                    "1.0.1",
                    chengzhiMeow,
                    new RelocateConfig(true)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("cn{}chengzhiya"),
                    "MHDF-LangUtil",
                    "1.3.1",
                    chengzhiMeow,
                    new RelocateConfig(true)
            ));

            // JSON处理
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("com{}alibaba{}fastjson2"),
                    "fastjson2",
                    "2.0.53",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true)
            ));

            // 反射处理
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("org{}reflections"),
                    "reflections",
                    "0.10.2",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true)
            ));

            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("org{}javassist"),
                    "javassist",
                    "3.28.0-GA",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true, true,
                            "javassist"
                    )
            ));

            // packetevents-api
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("com{}github{}retrooper"),
                    "packetevents-api",
                    "2.8.0",
                    codemc,
                    new RelocateConfig(true, true,
                            handleString("io{}github{}retrooper")
                    )
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("com{}github{}retrooper"),
                    "packetevents-netty-common",
                    "2.8.0",
                    codemc,
                    new RelocateConfig(true, true,
                            handleString("io{}github{}retrooper")
                    )
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("com{}github{}retrooper"),
                    "packetevents-spigot",
                    "2.8.0",
                    codemc,
                    new RelocateConfig(true, true,
                            handleString("io{}github{}retrooper")
                    )
            ));

            // 数据库
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("cn{}chengzhiya"),
                    "MHDF-Database-API",
                    "1.0.2",
                    chengzhiMeow,
                    new RelocateConfig(true)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("cn{}chengzhiya"),
                    "MHDF-Database-MySQL",
                    "1.0.2",
                    chengzhiMeow,
                    new RelocateConfig(true)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("cn{}chengzhiya"),
                    "MHDF-Database-H2",
                    "1.0.2",
                    chengzhiMeow,
                    new RelocateConfig(true)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("com{}j256{}ormlite"),
                    "ormlite-core",
                    "6.1",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("com{}j256{}ormlite"),
                    "ormlite-jdbc",
                    "6.1",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("com{}zaxxer"),
                    "HikariCP",
                    "6.1.0",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("com{}mysql"),
                    "mysql-connector-j",
                    "9.1.0",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("com{}h2database"),
                    "h2",
                    "2.3.232",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true, false,
                            handleString("org{}h2")
                    )
            ));

            // redis
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("io{}lettuce"),
                    "lettuce-core",
                    "6.5.5.RELEASE",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("io{}projectreactor"),
                    "reactor-core",
                    "3.6.6",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true, true,
                            "reactor"
                    )
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("org{}reactivestreams"),
                    "reactive-streams",
                    "1.0.4",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true)
            ));

            // 数学表达式
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}objecthunter"),
                    "exp4j",
                    "0.4.8",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true)
            ));

            // adventure-api
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-platform-api",
                    "4.3.4",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true, false,
                            handleString("net{}kyori{}adventure{}platform")
                    )
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-platform-bukkit",
                    "4.4.0",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true, false,
                            handleString("net{}kyori{}adventure{}platform"),
                            handleString("net{}kyori{}adventure{}platform{}text{}serializer{}craftbukkit")
                    )
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-platform-facet",
                    "4.4.0",
                    MHDFLibrary.mavenCenterMirror,
                    new RelocateConfig(true, false,
                            handleString("net{}kyori{}adventure{}platform")
                    )
            ));

            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-api",
                    "4.21.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-key",
                    "4.21.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-nbt",
                    "4.21.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-platform-viaversion",
                    "4.4.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-text-logger-slf4j",
                    "4.21.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-text-minimessage",
                    "4.21.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-text-serializer-ansi",
                    "4.21.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-text-serializer-bungeecord",
                    "4.4.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-text-serializer-gson",
                    "4.21.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-text-serializer-gson-legacy-impl",
                    "4.21.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-text-serializer-json",
                    "4.21.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-text-serializer-json-legacy-impl",
                    "4.21.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-text-serializer-legacy",
                    "4.21.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "adventure-text-serializer-plain",
                    "4.21.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "ansi",
                    "1.0.3",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "examination-api",
                    "1.3.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "examination-string",
                    "1.3.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
            mhdfLibrary.addDependencyConfig(new DependencyConfig(
                    handleString("net{}kyori"),
                    "option",
                    "1.1.0",
                    MHDFLibrary.mavenCenterMirror,
                    !PluginUtil.isNativeSupportAdventureApi(),
                    new RelocateConfig(false)
            ));
        }

        mhdfLibrary.downloadDependencies();
        mhdfLibrary.loadDependencies();
    }

    /**
     * 处理文本
     *
     * @param string 文本
     * @return 处理后的文本
     */
    private String handleString(String string) {
        return string.replace("{}", ".");
    }

    static class LibraryLoggerManager implements LoggerManager {
        @Override
        public void log(String string) {
            Main.instance.getLogger().info(string);
        }
    }
}

package cn.chengzhimeow.mhdftools.plugin;

public final class PluginManager {
    private static PluginManager instance;

    public static PluginManager getInstance() {
        if (PluginManager.instance == null) PluginManager.instance = new PluginManager();
        return PluginManager.instance;
    }

    public String minecraftVersion;
    public ServerType serverType;
    public String version;

    private PluginManager() {
    }
}

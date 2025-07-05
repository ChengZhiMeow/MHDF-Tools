package cn.chengzhiya.mhdftools.util.config;

import cn.chengzhiya.mhdftools.exception.FileException;
import cn.chengzhiya.mhdftools.util.message.MessageUtil;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class CustomMenuConfigUtil {
    @Getter
    private static final File customMenuFolder = new File(ConfigUtil.getDataFolder(), "customMenu");
    @Getter
    private static final ConcurrentHashMap<String, YamlConfiguration> customMenuHashMap = new ConcurrentHashMap<>();
    @Getter
    private static final ConcurrentHashMap<String, String> customMenuIdCommandHashMap = new ConcurrentHashMap<>();

    /**
     * 保存初始自定义菜单
     */
    public static void saveDefaultCustomMenu() throws FileException {
        if (!ConfigUtil.getConfig().getBoolean("customMenuSettings.enable")) {
            return;
        }
        if (getCustomMenuFolder().exists()) {
            return;
        }

        FileUtil.createFolder(getCustomMenuFolder());
        FileUtil.saveResource("customMenu/example.yml", "customMenu/example.yml", false);
    }

    /**
     * 加载自定义菜单
     */
    public static void reloadCustomMenu() {
        if (!ConfigUtil.getConfig().getBoolean("customMenuSettings.enable")) {
            return;
        }

        getCustomMenuHashMap().clear();
        getCustomMenuIdCommandHashMap().clear();
        for (File file : FileUtil.listFiles(getCustomMenuFolder())) {
            String path = file.getPath().replace("/", "\\");
            if (!path.endsWith(".yml")) {
                return;
            }
            path = path.replace(".yml", "");
            path = MessageUtil.subString(path, "\\customMenu\\");

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String command : config.getStringList("commands")) {
                getCustomMenuIdCommandHashMap().put(command, path);
            }
            getCustomMenuHashMap().put(path, config);
        }
    }

    /**
     * 获取自定义菜单列表
     *
     * @return 自定义菜单列表
     */
    public static Set<String> getCustomMenuList() {
        return getCustomMenuHashMap().keySet();
    }

    /**
     * 获取菜单配置文件实例
     *
     * @param command 命令
     * @return 配置文件实例
     */
    public static String getCustomMenuByCommand(String command) {
        return getCustomMenuIdCommandHashMap().get(command);
    }

    /**
     * 获取菜单配置文件实例
     *
     * @param id 菜单ID
     * @return 配置文件实例
     */
    public static YamlConfiguration getCustomMenuConfig(String id) {
        return getCustomMenuHashMap().get(id);
    }
}

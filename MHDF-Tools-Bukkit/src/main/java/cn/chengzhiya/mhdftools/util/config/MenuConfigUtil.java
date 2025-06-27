package cn.chengzhiya.mhdftools.util.config;

import cn.chengzhiya.mhdftools.exception.FileException;
import cn.chengzhiya.mhdftools.util.message.MessageUtil;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public final class MenuConfigUtil {
    @Getter
    private static final File menuFolder = new File(ConfigUtil.getDataFolder(), "menu");
    @Getter
    private static final ConcurrentHashMap<String, YamlConfiguration> menuHashMap = new ConcurrentHashMap<>();

    /**
     * 保存初始菜单
     */
    public static void saveDefaultMenu() throws FileException {
        FileUtil.createFolder(getMenuFolder());
        if (ConfigUtil.getConfig().getBoolean("tpaSettings.enable")) {
            FileUtil.saveResource("menu/tpa.yml", "menu/tpa.yml", false);
        }
        if (ConfigUtil.getConfig().getBoolean("tpahereSettings.enable")) {
            FileUtil.saveResource("menu/tpahere.yml", "menu/tpahere.yml", false);
        }
        if (ConfigUtil.getConfig().getBoolean("homeSettings.enable")) {
            FileUtil.saveResource("menu/home.yml", "menu/home.yml", false);
        }
        if (ConfigUtil.getConfig().getBoolean("chatSettings.enable") && ConfigUtil.getConfig().getBoolean("chatSettings.showItem.enable")) {
            FileUtil.saveResource("menu/lookItem.yml", "menu/lookItem.yml", false);
        }
        if (ConfigUtil.getConfig().getBoolean("chatSettings.enable") && ConfigUtil.getConfig().getBoolean("chatSettings.showInventory.enable")) {
            FileUtil.saveResource("menu/lookInventory.yml", "menu/lookInventory.yml", false);
        }
        if (ConfigUtil.getConfig().getBoolean("chatSettings.enable") && ConfigUtil.getConfig().getBoolean("chatSettings.showEnderChest.enable")) {
            FileUtil.saveResource("menu/lookEnderChest.yml", "menu/lookEnderChest.yml", false);
        }
    }

    /**
     * 加载菜单
     */
    public static void reloadMenu() {
        getMenuHashMap().clear();
        for (File file : FileUtil.listFiles(getMenuFolder())) {
            String path = file.getPath().replace("/", "\\");
            if (!path.endsWith(".yml")) {
                return;
            }
            path = path.replace(".yml", "");
            path = MessageUtil.subString(path, "\\menu\\");

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            getMenuHashMap().put(path, config);
        }
    }

    /**
     * 获取菜单配置文件实例
     *
     * @param id 菜单ID
     * @return 配置文件实例
     */
    public static YamlConfiguration getMenuConfig(String id) {
        return getMenuHashMap().get(id);
    }
}

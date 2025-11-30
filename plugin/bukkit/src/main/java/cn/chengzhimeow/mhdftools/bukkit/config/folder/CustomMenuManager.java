package cn.chengzhimeow.mhdftools.bukkit.config.folder;

import cn.chengzhimeow.ccyaml.configuration.yaml.YamlConfiguration;
import cn.chengzhimeow.ccyaml.manager.AbstractFolderYamlManager;
import cn.chengzhimeow.mhdftools.bukkit.message.MessageUtil;
import cn.chengzhimeow.mhdftools.config.ConfigManager;

import java.io.File;
import java.util.List;

public final class CustomMenuManager extends AbstractFolderYamlManager {
    private static CustomMenuManager instance;

    public static CustomMenuManager getSettingInstance() {
        if (CustomMenuManager.instance == null) {
            CustomMenuManager.instance = new CustomMenuManager();
        }
        return CustomMenuManager.instance;
    }

    private CustomMenuManager() {
        super(ConfigManager.getInstance().getYamlManager());
    }

    @Override
    public void saveDefaultFile() {
        if (super.getFolder().exists()) {
            return;
        }

        super.saveDefaultFile();
    }

    @Override
    public String originFilePath() {
        return "customMenu";
    }

    @Override
    public String filePath() {
        return "customMenu";
    }

    /**
     * 获取自定义菜单ID列表
     *
     * @return 菜单ID列表
     */
    public List<String> getCustomMenuIdList() {
        return super.getFileList().stream()
                .map(File::getPath)
                .filter(s -> s.endsWith(".yml"))
                .map(s -> s.replace(".yml", ""))
                .map(s -> MessageUtil.subString(s, "\\customMenu\\"))
                .toList();
    }

    /**
     * 获取菜单配置文件实例
     *
     * @param id 菜单ID
     * @return 配置文件实例
     */
    public YamlConfiguration getCustomMenuById(String id) {
        return super.getData(super.getFileList().stream()
                .filter(f -> f.getPath().endsWith(id + ".yml"))
                .findFirst()
                .orElse(null)
        );
    }

    /**
     * 获取菜单配置文件实例
     *
     * @param command 命令
     * @return 配置文件实例
     */
    public YamlConfiguration getCustomMenuByCommand(String command) {
        return super.getDataList().stream()
                .filter(c -> c.getStringList("commands").contains(command))
                .findFirst()
                .orElse(null);
    }
}

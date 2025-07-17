package cn.chengzhiya.mhdftools.manager.config;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.message.MessageUtil;
import cn.chengzhiya.mhdfyaml.manager.FolderYamlManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public final class CustomMenuManager extends FolderYamlManager {
    public CustomMenuManager() {
        super(Main.instance.getYamlManager());
    }

    @Override
    public void saveDefaultFile() {
        if (super.getFolder().exists()) {
            return;
        }

        super.saveDefaultFile();
    }

    @Override
    public String getOriginFolderPath() {
        return "customMenu";
    }

    @Override
    public String getFolderPath() {
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

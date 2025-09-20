package cn.chengzhimeow.mhdftools.bukkit.placeholder;

import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.config.YamlUtil;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Placeholder {
    private final boolean enable;

    public Placeholder(List<String> enableKeyList) {
        this.enable = YamlUtil.equalsTrue(ConfigSetting.getSettingInstance().getData(), enableKeyList);
    }

    public Placeholder() {
        this(new ArrayList<>());
    }

    /**
     * 获取指定玩家实例指定变量的值
     *
     * @param player      玩家实例
     * @param placeholder 变量
     * @return 指定玩家实例下指定变量的值
     */
    abstract public String placeholder(OfflinePlayer player, @NotNull String placeholder);

    /**
     * 获取指定玩家实例指定变量的值
     *
     * @param player      玩家实例
     * @param placeholder 变量
     * @return 指定玩家实例下指定变量的值
     */
    public String onPlaceholder(OfflinePlayer player, @NotNull String placeholder) {
        if (!this.isEnable()) {
            return null;
        }

        return this.placeholder(player, placeholder);
    }
}

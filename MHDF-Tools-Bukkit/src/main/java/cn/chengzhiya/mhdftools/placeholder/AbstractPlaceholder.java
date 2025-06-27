package cn.chengzhiya.mhdftools.placeholder;

import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.YamlUtil;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractPlaceholder implements Placeholder {
    private final boolean enable;

    public AbstractPlaceholder(List<String> enableKeyList) {
        this.enable = YamlUtil.equalsTrue(ConfigUtil.getConfig(), enableKeyList);
    }

    public AbstractPlaceholder() {
        this(new ArrayList<>());
    }

    /**
     * 获取指定玩家实例指定变量的值
     *
     * @param player      玩家实例
     * @param placeholder 变量
     * @return 指定玩家实例下指定变量的值
     */
    public String onPlaceholder(OfflinePlayer player, @NotNull String placeholder) {
        if (!isEnable()) {
            return null;
        }

        return this.placeholder(player, placeholder);
    }
}

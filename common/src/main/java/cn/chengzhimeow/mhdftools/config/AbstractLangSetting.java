package cn.chengzhimeow.mhdftools.config;

import cn.chengzhimeow.ccyaml.manager.AbstractYamlManager;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import cn.chengzhimeow.mhdftools.text.TextComponentBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractLangSetting extends AbstractYamlManager {
    public AbstractLangSetting() {
        super(ConfigManager.getInstance().getYamlManager());
    }

    /**
     * 根据指定key获取语言文件对应文本并处理颜色
     *
     * @return 文本
     */
    public @NotNull TextComponent i18n(String key) {
        String message = super.getData().getString(key, "");
        assert message != null;

        return ColorUtil.color(message)
                .replace("{version}", PluginManager.getInstance().version)
                .replace("{prefix}", super.getData().getString(key, "")
                );
    }

    /**
     * 获取指定key下的项列表
     *
     * @return 项列表
     */
    public @NotNull Set<String> getKeys(String key) {
        return Objects.requireNonNull(super.getData().getConfigurationSection(key)).getKeys(false);
    }

    /**
     * 获取指定命令key命令信息文本实例
     *
     * @param command 命令key
     * @return 文本实例
     */
    public @NotNull TextComponent getCommandInfo(String command) {
        return this.i18n("commandInfoFormat")
                .replace("{usage}", this.i18n(command + ".usage"))
                .replace("{description}", this.i18n(command + ".description"));
    }

    /**
     * 获取命令帮助
     *
     * @param prefix      前缀
     * @param commandList 命令列表
     * @return 命令帮助文本实例
     */
    public @NotNull TextComponent getHelpList(String prefix, List<String> commandList) {
        TextComponentBuilder builder = new TextComponentBuilder();

        for (String command : commandList) {
            builder.append(this.getCommandInfo(prefix + "." + command));
            if (!command.equals(commandList.getLast())) {
                builder.appendNewline();
            }
        }

        return builder.build();
    }

    /**
     * 获取命令帮助
     *
     * @param prefix 前缀
     * @return 命令帮助文本实例
     */
    public @NotNull TextComponent getHelpList(String prefix) {
        return this.getHelpList(prefix, new ArrayList<>(this.getKeys(prefix)));
    }
}

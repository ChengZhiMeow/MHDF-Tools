package cn.chengzhiya.mhdftools.manager.config;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.text.TextComponentBuilder;
import cn.chengzhiya.mhdftools.util.PluginUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import cn.chengzhiya.mhdfyaml.manager.YamlManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class LangManager extends YamlManager {
    public LangManager() {
        super(Main.instance.getYamlManager());
    }

    @Override
    public String getOriginFilePath() {
        return "lang_zh.yml";
    }

    @Override
    public String getFilePath() {
        return "lang.yml";
    }

    /**
     * 根据指定key获取语言文件对应文本
     *
     * @return 文本
     */
    public @NotNull String getString(String key) {
        return super.getData().getString(key, "");
    }

    /**
     * 根据指定key获取语言文件对应文本并处理颜色
     *
     * @return 文本
     */
    public @NotNull TextComponent i18n(String key) {
        String message = Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(null, this.getString(key));
        return ColorUtil.color(message
                .replace("{prefix}", this.getString("prefix"))
                .replace("{version}", PluginUtil.getVersion())
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
            if (!command.equals(commandList.get(commandList.size() - 1))) {
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

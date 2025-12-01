package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.ConfigsManager;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import cn.chengzhimeow.mhdftools.text.TextComponentBuilder;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.imports.CmiImportUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.imports.HuskHomesImportUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class MHDFTools extends Command {
    public MHDFTools() {
        super(
                null,
                "梦之工具主命令",
                "mhdftools.commands.mhdftools",
                false,
                "mhdftools", "mt"
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                // 功能帮助
                case "feature" -> {
                    int page = args.length >= 2 ? Integer.parseInt(args[1]) : 1;

                    List<String> commandList = Main.instance.getCommandManager().getRegisterCommandIdList();
                    int maxPage = (int) Math.ceil(commandList.size() / 4.0);

                    if (page < 1) {
                        page = 1;
                    }
                    if (page > maxPage) {
                        page = maxPage;
                    }

                    sender.sendMessage(LangSetting.getInstance().i18n("commands.mhdftools.subCommands.feature.message")
                            .replace("{helpList}", this.getFeatureHelpMessage(page, commandList))
                            .replace("{page}", String.valueOf(page))
                            .replace("{maxPage}", String.valueOf(maxPage))
                            .replaceByMiniMessage("{lastPage}", String.valueOf(page - 1))
                            .replaceByMiniMessage("{nextPage}", String.valueOf(page + 1))
                            .replaceByMiniMessage("{command}", label)
                    );
                    return;
                }
                // 重载插件配置
                case "reload" -> {
                    ConfigsManager.getInstance().reloadAll();

                    sender.sendMessage(LangSetting.getInstance().i18n("commands.mhdftools.subCommands.reload.message")
                            .replace("{command}", label)
                    );
                    return;
                }
                // 导入插件数据
                case "import" -> {
                    if (args.length != 2) {
                        sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                                .replace("{usage}", LangSetting.getInstance().i18n("commands.mhdftools.subCommands.import.usage"))
                                .replace("{command}", label)
                        );
                        return;
                    }

                    switch (args[1]) {
                        case "huskhomes" -> HuskHomesImportUtil.importHuskHomesData(sender);
                        case "cmi" -> CmiImportUtil.importCmiData(sender);
                        default ->
                                sender.sendMessage(LangSetting.getInstance().i18n("commands.mhdftools.subCommands.import.pluginNotSupport"));
                    }
                    return;
                }
            }
        }

        // 输出帮助信息
        {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.mhdftools.subCommands.help.message")
                    .replace("{helpList}", LangSetting.getInstance().getHelpList("commands.mhdftools.subCommands"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(LangSetting.getInstance().getKeys("commands.mhdftools.subCommands"));
        }
        if (args.length == 2) {
            if (args[0].equals("import")) {
                return List.of("huskhomes", "cmi");
            }
        }
        return new ArrayList<>();
    }

    /**
     * 获取功能帮助文本实例
     *
     * @param page        页数
     * @param commandList 命令列表
     * @return 功能帮助文本实例
     */
    private TextComponent getFeatureHelpMessage(int page, List<String> commandList) {
        int start = (page - 1) * 4;
        int end = Math.min(commandList.size(), start + 4);

        TextComponentBuilder textComponentBuilder = new TextComponentBuilder();
        for (int i = start; i < end; i++) {
            String command = commandList.get(i);

            textComponentBuilder.append(LangSetting.getInstance().getCommandInfo("commands." + command)
                    .replace("{command}", command)
            );
            if (!command.equals(commandList.get(end - 1))) {
                textComponentBuilder.appendNewline();
            }
        }

        return textComponentBuilder.build();
    }
}

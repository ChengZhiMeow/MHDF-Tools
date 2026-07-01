package cn.chengzhimeow.mhdftools.bukkit.module.core.command;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.core.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.core.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.core.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.config.impl.ProxySetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import cn.chengzhimeow.mhdftools.text.TextComponentBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class MHDFTools extends Command {
    public MHDFTools() {
        super(
                ModuleMain.instance,
                "梦之工具主命令",
                "mhdftools.commands.mhdftools",
                false,
                cn.chengzhimeow.mhdftools.bukkit.module.core.config.ConfigSetting.getInstance().getConfig().mhdftoolsCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "feature" -> {
                    int page;
                    try {
                        page = args.length >= 2 ? Integer.parseInt(args[1]) : 1;
                    } catch (NumberFormatException ignored) {
                        page = 1;
                    }

                    List<String> commandList = Module.getRegisterCommandIdList();
                    int maxPage = Math.max(1, (int) Math.ceil(commandList.size() / 4.0));

                    if (page < 1) page = 1;
                    if (page > maxPage) page = maxPage;

                    sender.sendMessage(LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().feature().message()
                            .replace("{help_list}", this.getFeatureHelpMessage(page, commandList))
                            .replace("{page}", String.valueOf(page))
                            .replace("{max_page}", String.valueOf(maxPage))
                            .replaceByMiniMessage("{last_page}", String.valueOf(page - 1))
                            .replaceByMiniMessage("{next_page}", String.valueOf(page + 1))
                            .replaceByMiniMessage("{command}", label)
                    );
                    return;
                }
                case "reload" -> {
                    ProxySetting.getInstance().reload();
                    ConfigSetting.getInstance().reload();
                    GlobalLangSetting.getInstance().reload();

                    for (Module module : Module.getRegisterModuleList()) {
                        module.reloadConfig();
                    }

                    sender.sendMessage(LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().reload().message()
                            .replace("{command}", label)
                    );
                    return;
                }
            }
        }

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().help().message()
                .replace("{help_list}", this.getHelpMessage(label))
                .replace("{command}", label)
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("help", "feature", "reload");
        }
        return new ArrayList<>();
    }

    private TextComponent getHelpMessage(String label) {
        TextComponentBuilder textComponentBuilder = new TextComponentBuilder();
        textComponentBuilder.append(this.getSubCommandInfo(
                LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().feature().usage(),
                LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().feature().description(),
                label
        ));
        textComponentBuilder.appendNewline();
        textComponentBuilder.append(this.getSubCommandInfo(
                LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().help().usage(),
                LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().help().description(),
                label
        ));
        textComponentBuilder.appendNewline();
        textComponentBuilder.append(this.getSubCommandInfo(
                LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().reload().usage(),
                LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().reload().description(),
                label
        ));
        return textComponentBuilder.build();
    }

    private TextComponent getFeatureHelpMessage(int page, List<String> commandList) {
        if (commandList.isEmpty()) return new TextComponent();

        int start = (page - 1) * 4;
        int end = Math.min(commandList.size(), start + 4);

        TextComponentBuilder textComponentBuilder = new TextComponentBuilder();
        for (int i = start; i < end; i++) {
            String command = commandList.get(i);

            textComponentBuilder.append(LangSetting.getInstance().getConfig().commandInfoFormat()
                    .replace("{usage}", "/" + command)
                    .replace("{description}", command));
            if (!command.equals(commandList.get(end - 1))) {
                textComponentBuilder.appendNewline();
            }
        }

        return textComponentBuilder.build();
    }

    private TextComponent getSubCommandInfo(TextComponent usage, TextComponent description, String label) {
        return LangSetting.getInstance().getConfig().commandInfoFormat()
                .replace("{usage}", usage.replace("{command}", label))
                .replace("{description}", description);
    }
}

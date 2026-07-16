package cn.chengzhimeow.mhdftools.bukkit.module.core.command;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.core.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.core.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.core.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.config.impl.ProxySetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class MHDFTools extends Command {
    public MHDFTools() {
        super(
                ModuleMain.instance,
                LangSetting.getInstance().getConfig().commands().mhdftools().description(),
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

                    Map<String, Command> commands = Module.getRegisterCommandMap();
                    int maxPage = Math.max(1, (int) Math.ceil(commands.size() / 4.0));

                    if (page < 1) page = 1;
                    if (page > maxPage) page = maxPage;

                    sender.sendMessage(LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().feature().message()
                            .replace("{help_list}", this.getFeatureHelpMessage(page, commands.entrySet()))
                            .replace("{page}", String.valueOf(page))
                            .replace("{max_page}", String.valueOf(maxPage))
                            .replaceByMiniMessage("{last_page}", String.valueOf(page - 1))
                            .replaceByMiniMessage("{next_page}", String.valueOf(page + 1))
                            .replaceByMiniMessage("{command}", label)
                    );
                    return;
                }
                case "modules" -> {
                    StringBuilder loadModules = new StringBuilder();
                    StringBuilder unloadModules = new StringBuilder();
                    for (Module module : Module.getRegisterModuleList()) {
                        if (module.isEnable()) {
                            if (!loadModules.isEmpty()) loadModules.append(", ");
                            loadModules.append(module.getId());
                        } else {
                            if (!unloadModules.isEmpty()) unloadModules.append(", ");
                            unloadModules.append(module.getId());
                        }
                    }

                    sender.sendMessage(LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().modules().message()
                            .replace("{load_modules}", loadModules.toString())
                            .replace("{unload_modules}", unloadModules.toString())
                    );
                    return;
                }
                case "reload" -> {
                    ProxySetting.getInstance().reload();
                    ConfigSetting.getInstance().reload();
                    GlobalLangSetting.getInstance().reload();

                    for (Module module : Module.getRegisterModuleList()) {
                        if (!module.isEnable()) continue;
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
            return List.of("help", "feature", "modules", "reload");
        }
        return new ArrayList<>();
    }

    private Component getHelpMessage(String label) {
        return Component.empty()
                .append(this.getSubCommandInfo(
                        LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().help().usage(),
                        LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().help().description(),
                        label
                ))
                .appendNewline()
                .append(this.getSubCommandInfo(
                        LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().feature().usage(),
                        LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().feature().description(),
                        label
                ))
                .appendNewline()
                .append(this.getSubCommandInfo(
                        LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().modules().usage(),
                        LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().modules().description(),
                        label
                ))
                .appendNewline()
                .append(this.getSubCommandInfo(
                        LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().reload().usage(),
                        LangSetting.getInstance().getConfig().commands().mhdftools().subCommands().reload().description(),
                        label
                ));
    }

    private Component getFeatureHelpMessage(int page, Set<Map.Entry<String, Command>> commands) {
        if (commands.isEmpty()) return new TextComponent();

        int start = (page - 1) * 4;
        int end = Math.min(commands.size(), start + 4);

        List<Map.Entry<String, Command>> list = new ArrayList<>(commands);
        Component builder = Component.empty();
        for (int i = start; i < end; i++) {
            Map.Entry<String, Command> command = list.get(i);

            builder = builder.append(LangSetting.getInstance().getConfig().commandInfoFormat()
                    .replace("{usage}", "/" + command.getKey())
                    .replace("{description}", command.getValue().getDescription()));

            if (list.size() == i - 1) continue;
            builder = builder.appendNewline();
        }

        return builder;
    }

    private TextComponent getSubCommandInfo(TextComponent usage, TextComponent description, String label) {
        return LangSetting.getInstance().getConfig().commandInfoFormat()
                .replace("{usage}", usage.replace("{command}", label))
                .replace("{description}", description);
    }
}

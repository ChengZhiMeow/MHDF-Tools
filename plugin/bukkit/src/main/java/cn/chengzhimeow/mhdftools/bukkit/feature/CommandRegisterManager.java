package cn.chengzhimeow.mhdftools.bukkit.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.console.LogManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class CommandRegisterManager {
    @Getter(lazy = true)
    private static final CommandRegisterManager instance = new CommandRegisterManager();

    private CommandRegisterManager() {
    }

    @SneakyThrows
    public void registerCommands(Module module) {
        Reflections reflections = new Reflections(module.getClass().getPackageName());

        Set<Class<? extends Command>> commandClassSet = reflections.getSubTypesOf(Command.class).stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .sorted(Comparator.comparing(Class::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Class<? extends Command> clazz : commandClassSet) {
            Constructor<? extends Command> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Command command = constructor.newInstance();

            if (!command.isEnable()) continue;

            this.registerCommand(command);
            Module.getRegisterCommandIdList().add(command.getPermission().replace("mhdftools.commands.", ""));
        }
    }

    @SneakyThrows
    private void registerCommand(Command abstractCommand) {
        Constructor<PluginCommand> commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        commandConstructor.setAccessible(true);
        if (abstractCommand.getCommands().length == 0) {
            LogManager.instance.log("命令: " + abstractCommand.getClass() + " 注册失败, 原因: 找不到命令!");
            return;
        }

        PluginCommand command = commandConstructor.newInstance(abstractCommand.getCommands()[0], Main.instance);

        command.setAliases(Arrays.asList(abstractCommand.getCommands()));
        command.setDescription(abstractCommand.getDescription());
        command.setPermission(abstractCommand.getPermission());
        command.permissionMessage(GlobalLangSetting.getInstance().getConfig().noPermission());

        command.setExecutor(abstractCommand);
        command.setTabCompleter(abstractCommand);

        CommandMap commandMap = Main.instance.getServer().getCommandMap();
        commandMap.register(Main.instance.getName(), command);
    }
}

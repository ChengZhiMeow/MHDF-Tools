package cn.chengzhimeow.mhdftools.bukkit.manager.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.PluginUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.message.LogUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@SuppressWarnings({"deprecation", "unused"})
public final class CommandManager {
    private final List<String> registerCommandIdList = new ArrayList<>();

    /**
     * 注册所有启用的命令
     */
    @SneakyThrows
    public void init() {
        Reflections reflections = new Reflections(Command.class.getPackageName());

        for (Class<? extends Command> clazz : reflections.getSubTypesOf(Command.class)) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                Constructor<? extends Command> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                Command command = constructor.newInstance();

                if (command.isEnable()) {
                    this.registerCommand(command);
                    this.getRegisterCommandIdList().add(command.getPermission()
                            .replace("mhdftools.commands.", "")
                    );
                }
            }
        }
    }

    /**
     * 注册命令
     *
     * @param abstractCommand 命令实例
     */
    private void registerCommand(Command abstractCommand) throws Exception {
        Constructor<PluginCommand> commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        commandConstructor.setAccessible(true);
        if (abstractCommand.getCommands().length == 0) {
            LogUtil.log("命令: " + abstractCommand.getClass() + " 注册失败, 原因: 找不到命令!");
            return;
        }

        PluginCommand command = commandConstructor.newInstance(abstractCommand.getCommands()[0], Main.instance);

        command.setAliases(Arrays.asList(abstractCommand.getCommands()));
        command.setDescription(abstractCommand.getDescription());
        command.setPermission(abstractCommand.getPermission());
        command.permissionMessage(LangSetting.getSettingInstance().i18n("noPermission"));

        command.setExecutor(abstractCommand);
        command.setTabCompleter(abstractCommand);

        CommandMap commandMap = Main.instance.getServer().getCommandMap();
        commandMap.register(PluginUtil.getName(), command);
    }
}

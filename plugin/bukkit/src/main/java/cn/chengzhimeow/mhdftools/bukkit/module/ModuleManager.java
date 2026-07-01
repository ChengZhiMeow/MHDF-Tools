package cn.chengzhimeow.mhdftools.bukkit.module;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.exception.ModuleDisableException;
import cn.chengzhimeow.mhdftools.bukkit.exception.ModuleInitException;
import cn.chengzhimeow.mhdftools.bukkit.exception.ModuleLoadException;
import cn.chengzhimeow.mhdftools.bukkit.feature.*;
import cn.chengzhimeow.mhdftools.console.LogManager;
import lombok.Getter;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public final class ModuleManager {
    @Getter(lazy = true)
    private static final ModuleManager instance = new ModuleManager();

    @Getter
    private final Map<String, Module> modules = new LinkedHashMap<>();

    private ModuleManager() {
    }

    public void initModules() {
        Reflections reflections = new Reflections(Main.class.getPackageName());

        Set<Class<? extends Module>> moduleClassSet = reflections.getSubTypesOf(Module.class).stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .sorted(Comparator
                        .comparingInt((Class<? extends Module> clazz) -> {
                            ModulePriority priority = clazz.getAnnotation(ModulePriority.class);
                            if (priority == null) return 0;
                            return priority.value();
                        })
                        .thenComparing(Class::getName)
                )
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Class<? extends Module> moduleClass : moduleClassSet) {
            try {
                Constructor<? extends Module> constructor = moduleClass.getConstructor();
                constructor.setAccessible(true);

                Module module = constructor.newInstance();
                if (modules.containsKey(module.getId())) {
                    Module registedModule = modules.get(module.getId());
                    throw new ModuleInitException("模块 " + module.getId() + " 这个ID已经被 " + registedModule.getClass().getName() + " 注册了");
                }

                modules.put(module.getId(), module);
            } catch (Throwable throwable) {
                throw new ModuleInitException("初始化 " + moduleClass.getName() + " 模块的时候遇到了问题", throwable);
            }
        }
    }

    public void loadModule(Module module) {
        try {
            long time = System.currentTimeMillis();

            module.reloadConfig();
            module.onLoad();
            if (!module.isEnable()) {
                LogManager.instance.log("模块 " + module.getId() + " 已关闭因此不加载!");
                return;
            }
            module.onEnable();

            CommandRegisterManager.getInstance().registerCommands(module);
            ListenerRegisterManager.getInstance().registerListeners(module);
            PacketListenerRegisterManager.getInstance().registerPacketListeners(module);
            TaskRegisterManager.getInstance().registerTasks(module);
            PlaceholderRegisterManager.getInstance().registerPlaceholders(module);

            LogManager.instance.log("模块 " + module.getId() + " 加载完成, 耗时: " + (System.currentTimeMillis() - time) + "ms");
        } catch (Throwable throwable) {
            throw new ModuleLoadException("加载 " + module.getId() + " 模块的时候遇到了问题", throwable);
        }
    }

    public void loadModules() {
        List<Module> moduleList = new ArrayList<>(modules.values());
        Collections.reverse(moduleList);

        for (Module module : moduleList) {
            loadModule(module);
        }
    }

    public void unloadModule(Module module) {
        try {
            if (!module.isEnable()) return;
            module.onDisable();
            modules.remove(module.getId());

            LogManager.instance.log("模块 " + module.getId() + " 卸载完成!");
        } catch (Throwable throwable) {
            throw new ModuleDisableException("卸载 " + module.getId() + " 模块的时候遇到了问题", throwable);
        }
    }

    public void unloadModules() {
        for (Module module : new ArrayList<>(modules.values())) {
            unloadModule(module);
        }
        PlaceholderRegisterManager.getInstance().unregister();
    }
}

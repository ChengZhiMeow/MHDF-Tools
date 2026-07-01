package cn.chengzhimeow.mhdftools.bukkit.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Task;
import lombok.Getter;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class TaskRegisterManager {
    @Getter(lazy = true)
    private static final TaskRegisterManager instance = new TaskRegisterManager();

    private TaskRegisterManager() {
    }

    @SneakyThrows
    public void registerTasks(Module module) {
        Reflections reflections = new Reflections(module.getClass().getPackageName());

        Set<Class<? extends Task>> taskClassSet = reflections.getSubTypesOf(Task.class).stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .sorted(Comparator.comparing(Class::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Class<? extends Task> clazz : taskClassSet) {
            Constructor<? extends Task> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Task task = constructor.newInstance();

            if (!task.isEnable()) continue;

            task.runTaskTimerAsynchronously(Main.instance, 0L, task.getTime());
        }
    }
}

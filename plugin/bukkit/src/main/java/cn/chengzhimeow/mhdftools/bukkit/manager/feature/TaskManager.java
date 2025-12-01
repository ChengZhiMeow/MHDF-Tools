package cn.chengzhimeow.mhdftools.bukkit.manager.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Task;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@SuppressWarnings("unused")
public final class TaskManager {
    /**
     * 注册所有启用的计划任务
     */
    @SneakyThrows
    public void init() {
        Reflections reflections = new Reflections(Task.class.getPackageName());

        for (Class<? extends Task> clazz : reflections.getSubTypesOf(Task.class)) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                Constructor<? extends Task> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                Task task = constructor.newInstance();

                if (task.isEnable()) {
                    task.runTaskTimerAsynchronously(Main.instance, 0L, task.getTime());
                }
            }
        }
    }
}

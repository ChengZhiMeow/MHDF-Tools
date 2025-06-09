package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.task.AbstractTask;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;

@SuppressWarnings("unused")
public final class TaskManager {
    /**
     * 注册所有启用的计划任务
     */
    @SneakyThrows
    public void init() {
            Reflections reflections = new Reflections(AbstractTask.class.getPackageName());

            for (Class<? extends AbstractTask> clazz : reflections.getSubTypesOf(AbstractTask.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractTask abstractTask = clazz.getDeclaredConstructor().newInstance();
                    if (abstractTask.isEnable()) {
                        abstractTask.runTaskTimerAsynchronously(Main.instance, 0L, abstractTask.getTime());
                    }
                }
            }
    }
}
